package butter.usc.edu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

public class GraphChartWindow extends JFrame{
	private JTable table;
	String selectedFreeway = "All Freeways";
	BufferedImage theGraph;
	Object[][] data;
	
	GraphChartWindow(){
		super("Graph");
		setSize(882, 814);
		setLocation(100,100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		getContentPane().setLayout(null);

		//Table Panel
		final JPanel TablePanel = new JPanel();
		TablePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		TablePanel.setBackground(new Color(176, 196, 222));
		TablePanel.setBounds(32, 545, 818, 214);
		TablePanel.setLayout(new GridLayout(0, 1, 0, 0));
		getContentPane().add(TablePanel);

		//Table
		data = convertTOData();

		Vector cols = new Vector();
		cols.addElement(new String("Car ID"));
		for(int i = 1; i<data[0].length; i++)										//TODO make it loop depending on database time intervals
		{
			cols.addElement(new String("Time Interval " + i));
		}
		DefaultTableModel tableModel = new DefaultTableModel(data, cols.toArray());
		tableModel.setColumnIdentifiers(cols);


		final JTable theTable = new JTable(tableModel);
		theTable.setBounds(25, 50, 950, 600);
		theTable.setBackground(Color.lightGray);
		theTable.setGridColor(Color.black);
		theTable.setRowHeight(23);
		JScrollPane scrollPane = new JScrollPane(theTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		theTable.setAutoResizeMode(0);
		for(int i = 1; i<cols.size(); i++)
		{
			theTable.getColumnModel().getColumn(i).setPreferredWidth(93);
		}
		TablePanel.add(scrollPane);

		//Generate new graph.jpg
		new Graph(data); 

		//Display graph.jpg panel
		final ChartPanel GraphPanel = new ChartPanel();
		GraphPanel.setBounds(32, 35, 818, 498);
		getContentPane().add(GraphPanel);

		//SelectFreeway ComboBox
		String[] freeways = {"All Freeways", "Freeway 1", "Freeway 2", "Freeway 3"};			//TODO change these to the correct freeways
		JComboBox freewayComboBox = new JComboBox(freeways);
		freewayComboBox.setBounds(32, 6, 156, 27);
		getContentPane().add(freewayComboBox);
		freewayComboBox.setSelectedIndex(0);
		freewayComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				JComboBox cb = (JComboBox)ae.getSource();
				selectedFreeway = (String)cb.getSelectedItem();
				System.out.println("You chose " + selectedFreeway + ".");
				data = convertTOData();
				new Graph(data);
				//Update graph to selected freeways.
				GraphPanel.repaint();
			}
		});

		setVisible(true);
	}


	Object[][] convertTOData()
	{
		//TODO create data variable using the DATABASE!!!!!!
		if(selectedFreeway == "All Freeways")
		{
			Object[][] data = {{"1","100","200","150"},{"2","1","1","1"},{"3","1","1","1"}};
			return data;
		}
		else
		{
			Object[][] data = {{"1","100","200","150"},{"2","300","350","200"}};
			return data;
		}
		
	}

	public static void main(String [] args)					//TODO delete this later!
	{
		new GraphChartWindow();
	}
	//================================================================================================================================================
	//Other classes
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

		Graph(Object[][] data){
			/**
			 * Creating the lines. Lines are called "series" and we add points using the ".add(int, int)" function.
			 * -Each series represents each of the unique cars that we are currently keeping track of.
			 * -The X-Coordinate represents each time interval (wave of data from the server)
			 * -The Y-Coordinate represents the speeds that each of the cars are going.
			 * 
			 * Only cars of the selected freeway will be shown on the graph.
			 */
			XYSeriesCollection dataset = new XYSeriesCollection();

			Vector<XYSeries> allLines = new Vector<XYSeries>();
			for(int i=0; i<data.length; i++)
			{
				allLines.add(new XYSeries("Car " + data[i][0]));

				for(int j=1; j<data[i].length; j++)
				{
					double a = Double.parseDouble(data[i][j].toString());
					allLines.get(i).add(a, j);
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
			theGraph = chart.createBufferedImage(818, 498);
		}
	}
}


//http://dvillela.servehttp.com:4000/apostilas/jfreechart_tutorial.pdf