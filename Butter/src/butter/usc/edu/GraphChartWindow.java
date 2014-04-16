package butter.usc.edu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;

import java.awt.GridLayout;

public class GraphChartWindow extends JFrame{
	private static final long serialVersionUID = 1L;
	private JTable table;
	String selectedFreeway = "All Freeways";
	BufferedImage theGraph;
	private JTable DataTable;
	
	
	GraphChartWindow(){
		super("Graph");
		setSize(882, 814);
		setLocation(100,100);
		setResizable(false);
		getContentPane().setLayout(null);

		//Generate new graph.jpg
		new Graph(); 

		//Display graph.jpg panel
		final ChartPanel GraphPanel = new ChartPanel();
		GraphPanel.setBounds(32, 35, 818, 498);
		getContentPane().add(GraphPanel);

		//Table Panel
		JPanel TablePanel = new JPanel();
		TablePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		TablePanel.setBackground(new Color(176, 196, 222));
		TablePanel.setBounds(32, 545, 818, 214);
		TablePanel.setLayout(new GridLayout(0, 1, 0, 0));
		getContentPane().add(TablePanel);

		//Table
		Vector<String> car1 = new Vector<String>();
		car1.add("1");
		car1.add("30mph");
		car1.add("40mph");
		car1.add("50mph");
		Vector<String> car2 = new Vector<String>();
		car2.add("2");
		car2.add("40mph");
		car2.add("50mph");
		car2.add("60mph");
		Vector<Vector<String>> cars = new Vector<Vector<String>>();
		cars.add(car1);
		cars.add(car2);


		Vector<String> cols = new Vector<String>(10);
		cols.addElement(new String("Car ID"));
		for(int i = 1; i<car1.size(); i++)	//TODO make it loop depending on database time intervals
		{
			cols.addElement(new String("Time Interval " + i));
		}
		DefaultTableModel tableModel = new DefaultTableModel(2, car1.size());
		tableModel.setColumnIdentifiers(cols);


		JTable theTable = new JTable(tableModel);
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

		//SelectFreeway ComboBox
		String[] freeways = {"All Freeways", "Freeway 1", "Freeway 2", "Freeway 3"};			//TODO change these to the correct freeways
		JComboBox<?> freewayComboBox = new JComboBox<Object>(freeways);
		freewayComboBox.setBounds(32, 6, 156, 27);
		getContentPane().add(freewayComboBox);
		freewayComboBox.setSelectedIndex(0);
		freewayComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				JComboBox<?> cb = (JComboBox<?>)ae.getSource();
				selectedFreeway = (String)cb.getSelectedItem();
				System.out.println("You chose " + selectedFreeway + ".");

				//Update graph to selected freeways.
				GraphPanel.repaint();
			}
		});

		setVisible(true);
	}
	Object convertTOData(Vector<Vector<String>> a)
	{
		Object[][] data = new Object[a.size()][a.get(0).size()];
		for(int i = 0; i<a.size();i++)
		{
			for(int j = 0; j< a.get(0).size();j++)
			{
				data[i][j] = a.get(i).get(j);
			}
		}
		return data;
	}

	//------- Graph Display Panel-------
	class ChartPanel extends JPanel{
		private static final long serialVersionUID = 1L;

		ChartPanel(){}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			setLayout(null);
			g.drawImage(theGraph,0,0,null);
		}
	}
	//------ Graph Creator ------
	public class Graph {

		Graph(){
			/**
			 * Creating the lines. Lines are called "series" and we add points using the ".add(int, int)" function.
			 * -Each series represents each of the unique cars that we are currently keeping track of.
			 * -The X-Coordinate represents each time interval (wave of data from the server)
			 * -The Y-Coordinate represents the speeds that each of the cars are going.
			 * 
			 * Only cars of the selected freeway will be shown on the graph.
			 */

			//TODO if(selectedFreeway == "All Freeways"){} ------ Specify which cars to graph by Freeway.

			XYSeries ID1 = new XYSeries("Car 1");
			ID1.add(100, 1);
			ID1.add(200, 2);
			ID1.add(300, 3);
			ID1.add(400, 1);
			ID1.add(500, 5);

			// Add the series to your data set
			XYSeriesCollection dataset = new XYSeriesCollection();
			dataset.addSeries(ID1);

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
	
	
	public static void main(String [] args)					//TODO delete this later!
	{
		new GraphChartWindow();
	}
	
	
	
	
}


//http://dvillela.servehttp.com:4000/apostilas/jfreechart_tutorial.pdf