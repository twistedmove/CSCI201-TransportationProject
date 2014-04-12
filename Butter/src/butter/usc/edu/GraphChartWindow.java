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

public class GraphChartWindow extends JFrame{
	private JTable table;
	String selectedFreeway = "All Freeways";
	BufferedImage theGraph;
	private JTable DataTable;
	GraphChartWindow(){
		super("Graph");
		setSize(1059, 1022);
		setLocation(100,100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		getContentPane().setLayout(null);

		//Generate new graph.jpg
		new Graph(); 

		//Display graph.jpg panel
		final ChartPanel GraphPanel = new ChartPanel();
		GraphPanel.setBounds(32, 77, 1000, 600);
		getContentPane().add(GraphPanel);

		//Table Panel
		JPanel TablePanel = new JPanel();
		TablePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		TablePanel.setBackground(new Color(176, 196, 222));
		TablePanel.setBounds(32, 711, 1000, 270);
		getContentPane().add(TablePanel);
		TablePanel.setLayout(null);	
		DefaultTableModel model = new DefaultTableModel(5, 15);
		JTable DataTable = new JTable(model);

		DataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		DataTable.setTableHeader(null);
		DataTable.setRowHeight(34);
		DataTable.setGridColor(Color.BLACK);
		
		/*TableColumn c = new TableColumn(3);
        c.setHeaderValue("ID 6");
        DataTable.getColumnModel().addColumn(c);*/

		JScrollPane scrollPane = new JScrollPane(DataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		Dimension d = DataTable.getPreferredSize();
		scrollPane.setPreferredSize(new Dimension(d.width, 15*5));
		scrollPane.setBounds(new Rectangle(16, 16, 967, 235));
		TablePanel.add(scrollPane);

		//SelectFreeway ComboBox
		String[] freeways = {"All Freeways", "Freeway 1", "Freeway 2", "Freeway 3"};//TODO change these to the correct freeways
		JComboBox freewayComboBox = new JComboBox(freeways);
		freewayComboBox.setBounds(32, 47, 156, 27);
		getContentPane().add(freewayComboBox);
		freewayComboBox.setSelectedIndex(0);
		freewayComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				JComboBox cb = (JComboBox)ae.getSource();
				selectedFreeway = (String)cb.getSelectedItem();
				System.out.println("You chose " + selectedFreeway + ".");

				//Update graph to selected freeways.
				GraphPanel.repaint();
			}
		});



		setVisible(true);
	}

	public static void main(String [] args)
	{
		new GraphChartWindow();
	}

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
			theGraph = chart.createBufferedImage(1000,600);
		}
	}
}


//http://dvillela.servehttp.com:4000/apostilas/jfreechart_tutorial.pdf