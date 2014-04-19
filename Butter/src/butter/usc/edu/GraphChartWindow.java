package butter.usc.edu;

import java.util.ArrayList;
import java.util.List;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;

import java.awt.GridLayout;
import javax.swing.JLabel;

public class GraphChartWindow extends JFrame{
	String selectedFreeway = "All Freeways";
	BufferedImage theGraph;
	Object[][] data;
	int showNumCars = 20;
	JTable theTable;

	GraphChartWindow(){
		super("Graph");
		setSize(1196, 814);
		setLocation(100,100);
		setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		setResizable(false);
		getContentPane().setLayout(null);

		//Table Panel
		JPanel TablePanel = new JPanel();
		TablePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		TablePanel.setBackground(new Color(176, 196, 222));
		TablePanel.setBounds(32, 545, 1130, 214);
		TablePanel.setLayout(new GridLayout(0, 1, 0, 0));
		getContentPane().add(TablePanel);

		//Table
		data = convertTOData();

		Vector cols = new Vector();
		cols.addElement(new String("Car ID"));
		for(int i = 1; i<data[0].length-1; i++)
		{
			cols.addElement(new String("Time Interval " + i));
		}
		DefaultTableModel tableModel = new DefaultTableModel(data, cols.toArray());
		tableModel.setColumnIdentifiers(cols);


		theTable = new JTable(tableModel);
		theTable.setBounds(25, 50, 950, 600);
		theTable.setBackground(Color.lightGray);
		theTable.setGridColor(Color.black);
		theTable.setRowHeight(23);
		JScrollPane scrollPane = new JScrollPane(theTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		theTable.setAutoResizeMode(0);
		for(int i = 1; i<cols.size(); i++)
		{
			theTable.getColumnModel().getColumn(i).setPreferredWidth(110);
		}
		TablePanel.add(scrollPane);

		//Generate new graph.jpg
		new Graph(data); 

		//Display graph.jpg panel
		final ChartPanel GraphPanel = new ChartPanel();
		GraphPanel.setBounds(32, 35, 1130, 498);
		getContentPane().add(GraphPanel);

		//SelectFreeway ComboBox
		String[] freeways = {"All Freeways", "10", "101", "105", "405"};
		JComboBox freewayComboBox = new JComboBox(freeways);
		freewayComboBox.setBounds(32, 6, 156, 27);
		getContentPane().add(freewayComboBox);
		freewayComboBox.setSelectedIndex(0);
		freewayComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				JComboBox cb = (JComboBox)ae.getSource();
				selectedFreeway = (String)cb.getSelectedItem();
				System.out.println("You chose " + selectedFreeway + ".");
				//Updating everything!
				data = convertTOData();
				new Graph(data);
				redrawTable();
				GraphPanel.repaint();
			}
		});

		JLabel numCarsLabel = new JLabel("Number of Cars to Display:");
		numCarsLabel.setBounds(909, 10, 176, 16);
		getContentPane().add(numCarsLabel);

		String[] numCarOptions = {"20", "30", "50", "All"};
		JComboBox numCarsDisplayCombo = new JComboBox(numCarOptions);
		numCarsDisplayCombo.setBounds(1085, 6, 73, 27);
		getContentPane().add(numCarsDisplayCombo);
		numCarsDisplayCombo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				JComboBox cb = (JComboBox)ae.getSource();
				if((String)cb.getSelectedItem() == "All")
				{
					showNumCars = -1;
				}
				else
				{
					showNumCars = Integer.parseInt((String)cb.getSelectedItem());
				}
				//Updating everything!
				data = convertTOData();
				new Graph(data);
				redrawTable();
				GraphPanel.repaint();
			}
		});
		setVisible(true);
	}

	/**
	 * Updates the Table with the new data. Replaces old table with a new one that takes into account
	 * the neccessary rows&columns from the new data.
	 */
	void redrawTable()
	{
		Vector cols = new Vector();
		cols.addElement(new String("Car ID"));
		for(int i = 1; i<data[0].length-1; i++)
		{
			cols.addElement(new String("Time Interval " + i));
		}
		DefaultTableModel tableModel = new DefaultTableModel(data, cols.toArray());
		tableModel.setColumnIdentifiers(cols);
		theTable.setModel(tableModel);
		for(int i = 1; i<cols.size(); i++)	//Set width of every cell.
		{
			theTable.getColumnModel().getColumn(i).setPreferredWidth(110);
		}
	}
	/**
	 * Reconnects to the database and parses through the data. Keeps track of all the unique
	 * cars and their speeds for each interval of time. Creates 2D Array of all the cars and 
	 * later converts it into an Object class to be returned at the end.
	 * @return fully formatted Object class
	 */

	Object[][] convertTOData()
	{
		final String USERNAME = "root";
		final String PASSWORD = "pass";
		String CONNECTION_URL = "jdbc:mysql://localhost:3306/";

		final String DATABASE = "traffic_history";
		final String HISTORICAL_TABLE = "historical_traffic_data";

		Connection connection = null;
		Statement statement = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		int id = 0;
		double speed = 0;
		String direction = null;
		String ramp = null;
		String freeway = null;
		int serverCall = 0;

		ArrayList< ArrayList<Double> > theCars = new ArrayList< ArrayList<Double> >();
		ArrayList<String> freeways = new ArrayList<String>();

		//Connecting to database
		try {
			connection = DriverManager.getConnection(CONNECTION_URL + DATABASE, USERNAME, PASSWORD);
			System.out.println("Connection to database acquired.");//TODO delete
		} catch (SQLException e) {System.out.println("Connection to database not acquired.");}
		//========================

		//Parsing through the database
		try {
			preparedStatement = connection.prepareStatement("SELECT * FROM " + HISTORICAL_TABLE);
			resultSet = preparedStatement.executeQuery();
			//Pulling Data
			while(resultSet.next())
			{
				id = resultSet.getInt("id");
				speed = resultSet.getDouble("speed");
				direction = resultSet.getString("direction");
				ramp = resultSet.getString("ramp");
				freeway = resultSet.getString("freeway");
				serverCall =  resultSet.getInt("serverCalls");

				int findRightID = -1;
				for(int i = 0; i< theCars.size(); i++)
				{
					if(theCars.get(i).get(0) == id)
					{
						findRightID = id;
						break;
					}
				}
				if(findRightID == -1)
				{
					theCars.add(new ArrayList<Double>());
					theCars.get(theCars.size()-1).add((double) id);
					for(int i= 1; i<serverCall; i++)
					{
						theCars.get(id).add(0.0);
					}
					freeways.add(freeway);
				}
				findRightID = id;
				theCars.get(findRightID).add(speed);
				theCars.get(findRightID).size();
			}

			for(int i = 0; i<theCars.size(); i++)
			{
				while(theCars.get(i).size()<= serverCall)
				{
					theCars.get(i).add(0.0);
				}
			}
			//If "All Freeways" is not selected, delete unneeded data points.
			if(!selectedFreeway.equals("All Freeways"))
			{
				boolean a = false;
				while(!a)
				{
					for(int i = 0; i<freeways.size(); i++)
					{
						if(!selectedFreeway.equals(freeways.get(i)))
						{
							if(theCars.size() == 1)	//If no cars left in the vector, return empty Object.
							{
								Object[][] data = {{"0"}};
								return data;
							}
							theCars.remove(i);
							freeways.remove(i);
							break;
						}
						if(i == freeways.size()-1)
							a = true;
					}
				}
			}	
			//Display the correct number of cars.
			int totalSize=showNumCars;
			if(totalSize == -1)
				totalSize = theCars.size();

			//Convert 2D ArrayList into Object class.
			Object[][] data = new Object[totalSize][serverCall+2];
			
			for(int i = 0; i<totalSize; i++)
			{
				data[i][0] = theCars.get(i).get(0).intValue();
				for(int j = 1; j<theCars.get(i).size(); j++)
				{
					data[i][j] = theCars.get(i).get(j);
				}
			}
			return data;
		} catch (SQLException e) {System.out.println("Unable to grab data.");}
		//===============
		 
		//If connecting to database fails~
		System.out.println("Could not grab data.");
		Object[][] data = {{"0"}};
		return data;
		//===============
	}

	public static void main(String [] args)	
	{
		new GraphChartWindow();
	}
	//================================================================================================================================================
	//Other classes
	//
	//================================================================================================================================================	

	//------- Graph Display Panel-------
	class ChartPanel extends JPanel{
		ChartPanel(){}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			setLayout(null);
			g.drawImage(theGraph,0,0,null);
		}
	}
	//------ Graph Creator ------
	public class Graph {

		/**
		 * Creating the lines. Lines are called "series" and we add points using the ".add(int, int)" function.
		 * -Each series represents each of the unique cars that we are currently keeping track of.
		 * -The X-Coordinate represents each time interval (wave of data from the server)
		 * -The Y-Coordinate represents the speeds that each of the cars are going.
		 * 
		 * Only cars of the selected freeway will be shown on the graph.
		 */
		Graph(Object[][] data){
			XYSeriesCollection dataset = new XYSeriesCollection();

			Vector<XYSeries> allLines = new Vector<XYSeries>();
			for(int i=0; i<data.length; i++)
			{
				allLines.add(new XYSeries("Car " + data[i][0]));

				for(int j=1; j<data[i].length-1; j++)
				{
					double a = Double.parseDouble(data[i][j].toString());
					allLines.get(i).add(j, a);
				}
				dataset.addSeries(allLines.get(allLines.size()-1));
			}

			// Generate the graph
			JFreeChart chart = ChartFactory.createXYLineChart(
					"Data Graph", 						// Title
					"Time Intervals (3 minutes)",		// x-axis Label
					"Speed (MPH)", 						// y-axis Label
					dataset, 							// Dataset
					PlotOrientation.VERTICAL, 			// Plot Orientation
					true, 								// Show Legend
					true, 								// Use tooltips
					false 								// Configure chart to generate URLs?
					);
			theGraph = chart.createBufferedImage(1130, 498);	//In case we want to "zoom out"
		}
	}
}
