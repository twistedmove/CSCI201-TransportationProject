package butter.usc.edu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

class AllCarsWrapper {
	
	public Vector<Car> allCars;
	private Lock lock;
	private Condition condition;
	
	public AllCarsWrapper() {
		allCars = new Vector<Car>();
		lock = new ReentrantLock();
		condition = lock.newCondition();
	}

	public Lock getLock() {
		return lock;
	}

	public Condition getCondition() {
		return condition;
	}
	
}

public class ButterGUI extends JFrame implements MouseListener{
	private static final long serialVersionUID = 367534120156013938L;

	public JPanel mapPanel;
	public PanelDraw mapPicPanel;
	public JLabel picLabel;
	public static boolean noCar;
	@SuppressWarnings("rawtypes")
	private static JComboBox fromHighway;
	@SuppressWarnings("rawtypes")
	private static JComboBox toHighway;
	@SuppressWarnings("rawtypes")
	private static JComboBox fromRamp;
	@SuppressWarnings("rawtypes")
	private static JComboBox toRamp;
	private static JTextArea jta;
	private TrafficHistoryDatabase trafficHistoryDatabase;
	private JPanel fromDestinationPanel;
	private JPanel destinationPanel;
	private JPanel toDestinationPanel;
	private static Vector<Marker> locationMarkers;
	private boolean clearLocation;
	private JButton searchButton;
	
	public static final String EXPORT_FILE = "Butter-Export.csv";
	
	Image mascot;
	ImageIcon sliced;
	public static AllCarsWrapper allCarsWrapper;
	private CarTimer carTimer;
	Image map;
	@SuppressWarnings("unused")
	private PathBank pb;
	@SuppressWarnings("unused")
	private RampBank rb;


	public ButterGUI () {
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("assets/images/buttericon.gif"));   
		this.setMinimumSize(new Dimension(1200,720));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setTitle("Butter");
		this.getContentPane().setLayout(null);
		this.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
				try {
					trafficHistoryDatabase.exportToCSV(EXPORT_FILE);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(ButterGUI.this, "Error exporting data to file.");
				}
			    trafficHistoryDatabase.dropDatabase();
			    deleteMapJPG();
			}
		});
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		clearLocation = false;
		allCarsWrapper = new AllCarsWrapper();
		locationMarkers = new Vector<Marker>();
		try {
			pb = new PathBank();
		} catch (TxtFormatException tfe) {
			jta.setText(jta.getText() + "Butter: TFE Exception. Contact Admin." + "\n");
		}
		rb = new RampBank();

		importImage();
		setupButterGUI();
	
		try {
			trafficHistoryDatabase = new TrafficHistoryDatabase();
			trafficHistoryDatabase.start();
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}
		
		carTimer = new CarTimer(mapPanel);
		new Thread(carTimer).start();
		
	/* TESTING PURPOSES
		Car c = new Car(1,60, "West", "Western Avenue, Normandie Avenue", "10");
		Car c1 = new Car(2,90, "South","Tujunga Avenue", "101");
		Car c2 = new Car(3,30, "East","Crenshaw Boulevard", "105");
		Car c3 = new Car(4,60, "North","Sherman Way", "405");
		Car c4 = new Car(5,20, "North","Sherman Way", "405");
		Car c5 = new Car(6,80, "East", "Western Avenue, Normandie Avenue", "10");
		Car c6 = new Car(7,45, "North","Los Angeles Street", "101");
		Car c7 = new Car(8,70, "West","Crenshaw Boulevard", "105");
		
		allCarsWrapper.allCars.add(c);
		allCarsWrapper.allCars.add(c1);
		allCarsWrapper.allCars.add(c2);
		allCarsWrapper.allCars.add(c3);
		allCarsWrapper.allCars.add(c4);
		allCarsWrapper.allCars.add(c5);
		allCarsWrapper.allCars.add(c6);
		allCarsWrapper.allCars.add(c7);
	*/	
		mapPanel.repaint();
	}
	
	private void importImage(){
		try {
			mascot = ImageIO.read(new File("assets/images/mascot.gif"));
			sliced = new ImageIcon("assets/images/sliced.gif");
		} catch (IOException ex) {
	        System.out.println("No file exists.");
		}
		
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setupButterGUI(){	
	
		/** 
		 * Setting up map panels and dependencies.
		 */
				mapPanel = new JPanel();
				mapPanel.setLocation(0, 0);
				mapPanel.setSize(903, 672);
				mapPanel.setMinimumSize(new Dimension(950, 680));
				
				try {
					String imageUrl = "https://maps.googleapis.com/maps/api/staticmap?center=34.090483+-118.293966&zoom=10&size=1280x1280&scale=2&sensor=false";
					String destinationFile = "map.jpg";
					URL url = new URL(imageUrl);
					InputStream is = url.openStream();
					OutputStream os = new FileOutputStream(destinationFile);

					byte[] b = new byte[2048];
					int length;

					while ((length = is.read(b)) != -1) {
						os.write(b, 0, length);
					}

					is.close();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
				mapPanel.setLayout(null);

				picLabel = new JLabel(new ImageIcon((new ImageIcon("map.jpg")).getImage().getScaledInstance(1450, 1450, java.awt.Image.SCALE_SMOOTH)));
				map = (new ImageIcon("map.jpg")).getImage().getScaledInstance(1450, 1450, java.awt.Image.SCALE_SMOOTH);
				mapPicPanel = new PanelDraw(map);
				mapPicPanel.setPreferredSize(new Dimension(1450,1450));
				mapPicPanel.addMouseListener(this);
				
				JScrollPane jsp = new JScrollPane(mapPicPanel);
				jsp.setBounds(6, 5, 895, 667);
					jsp.setPreferredSize(new Dimension (400,400));
					jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
					jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

					mapPanel.add(jsp);
		
				/** 
				 * Setting up to / from search dependencies.
				 */
					destinationPanel = new JPanel();
					destinationPanel.setLayout(null);
					destinationPanel.setBounds(907, 5, 286, 125);
					destinationPanel.setBorder(new TitledBorder("Destination"));
					fromDestinationPanel = new JPanel();
					fromDestinationPanel.setLayout(null);
					fromDestinationPanel.setBounds(4, 16, 278, 54);
					fromDestinationPanel.setBorder(new TitledBorder("From")); 
					toDestinationPanel = new JPanel();
					toDestinationPanel.setLayout(null);
					toDestinationPanel.setBounds(4, 66, 278, 54);
					toDestinationPanel.setBorder(new TitledBorder("To")); 

					Object hwyOptions [] = {"US 101", "I-105", "I-10", "I-405"};
					fromHighway = new JComboBox(hwyOptions);
					fromHighway.setBounds(8, 16, 70, 30);
					fromHighway.setSelectedIndex(0);
					fromHighway.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							DefaultComboBoxModel model = new DefaultComboBoxModel(RampBank.rampNames[fromHighway.getSelectedIndex()]);
							fromRamp.setModel(model);
						}
					});
					
					DefaultComboBoxModel model1 = new DefaultComboBoxModel(RampBank.rampNames[fromHighway.getSelectedIndex()]);
					fromRamp = new JComboBox();
					fromRamp.setModel(model1);
					fromRamp.setBounds(82, 16, 190, 30);
					
					toHighway = new JComboBox(hwyOptions);
					toHighway.setSelectedIndex(0);
					toHighway.setBounds(8, 16, 70, 30);
					toHighway.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							DefaultComboBoxModel model = new DefaultComboBoxModel(RampBank.rampNames[toHighway.getSelectedIndex()]);
							toRamp.setModel(model);						
						}						
					});
					
					DefaultComboBoxModel model2 = new DefaultComboBoxModel(RampBank.rampNames[toHighway.getSelectedIndex()]);
					toRamp = new JComboBox();
					toRamp.setModel(model2);
					toRamp.setBounds(82, 16, 190, 30);
					
					fromDestinationPanel.add(fromHighway);
					fromDestinationPanel.add(fromRamp);
					
					toDestinationPanel.add(toHighway);
					toDestinationPanel.add(toRamp);
					
				destinationPanel.add(fromDestinationPanel);
				destinationPanel.add(toDestinationPanel);
						
				/** 
				 * Setting up information text area dependencies.
				 */
				JPanel informationArea = new JPanel();
					informationArea.setBounds(907, 168, 287, 394);
					informationArea.setBorder (new TitledBorder(new EtchedBorder(), "Information"));
				jta = new JTextArea (23, 23);
			    	jta.setEditable (false);
			    JScrollPane scrollPane = new JScrollPane (jta);
			    	scrollPane.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			    	scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			    informationArea.add(scrollPane);
				
				
			    /** 
			     * Setting up buttons dependencies.
			     */
				searchButton = new JButton();
				searchButton.setText("Find Route");
				searchButton.setBounds(907, 134, 287, 30);
				searchButton.setBackground(new Color(59, 89, 182));
				searchButton.setForeground(Color.WHITE);
				searchButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (!clearLocation){
							try {
								dijkstraPath();
							} catch (SQLException sqle) {
								sqle.printStackTrace();
							} catch (Exception ee) {
								ee.printStackTrace();
							}
							clearLocation = true;
							searchButton.setText("Clear");
						} else if(clearLocation){
							locationMarkers.clear();
							clearLocation = false;
							searchButton.setText("Find Route");							
						}
					}
				});
			    
			    JButton viewDataButton = new JButton("View Data");
				viewDataButton.setBounds(907, 568, 287, 30);
				viewDataButton.setBackground(new Color(59, 89, 182));
				viewDataButton.setForeground(Color.WHITE);
				viewDataButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jta.setText(jta.getText() + "Butter - View Data!" + "\n");
						new GraphChartWindow();
					}
				});

				JButton exportDataButton = new JButton("Export Data");
				exportDataButton.setBounds(907, 603, 287, 30);
				exportDataButton.setBackground(new Color(59, 89, 182));
				exportDataButton.setForeground(Color.WHITE);
				exportDataButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jta.setText(jta.getText() + "Butter - Export Data!" + "\n");
						try {
							JFileChooser jfc = new JFileChooser(); 
					    	FileNameExtensionFilter csv = new FileNameExtensionFilter("CSV (.csv)", "csv");  
			            		jfc.addChoosableFileFilter(csv);  
			            		jfc.setFileFilter(csv);
			            	int result = jfc.showSaveDialog(null);	
			            	if(result != JFileChooser.APPROVE_OPTION){
								jta.setText(jta.getText() + "Butter - Export Canceled!" + "\n");
								return;
							} else {
								File file = jfc.getSelectedFile();
								String file_name = file.toString();
								if (!file_name.endsWith(".csv")){
								    file_name += ".csv";
								}
								trafficHistoryDatabase.exportToCSV(file_name);
								jta.setText(jta.getText() + "Butter - Export Successful!" + "\n");
							}
						} catch (IOException ioe){
							JOptionPane.showMessageDialog(ButterGUI.this, "Error exporting data to file.");
							ioe.getMessage();
						}
						
						
						
						
					}
				});

				JButton exitButton = new JButton("Exit");
				exitButton.setBounds(907, 638, 287, 30);
				exitButton.setBackground(new Color(59, 89, 182));
				exitButton.setForeground(Color.WHITE);
				exitButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (JOptionPane.showConfirmDialog(null,"Are you done using Butter?", "Slicing Butter", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE , sliced) == JOptionPane.YES_OPTION){
							trafficHistoryDatabase.dropDatabase();
							try {
								trafficHistoryDatabase.exportToCSV(EXPORT_FILE);
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(ButterGUI.this, "Error exporting data to file.");
							}
							deleteMapJPG();
							System.exit(0);
						}
					}
				});
				
				/**
				 *  Setting up menubar and about button.
				 */
				JMenuBar menuBar = new JMenuBar();
				setJMenuBar(menuBar);
				
				JMenu menuButter = new JMenu("Butter");
				menuBar.add(menuButter);
				
				JMenuItem menuItemAbout = new JMenuItem("About");
				menuItemAbout.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						AboutFrame abtFrame = new AboutFrame(mascot);	
						abtFrame.repaint();
					}
				});
				menuButter.add(menuItemAbout);	
				
				/**
				 * Import data button
				 */
				JMenu inportMenu = new JMenu("Import");
				menuBar.add(inportMenu);
				
				JMenuItem importButton = new JMenuItem("Import historical data");
				importButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							trafficHistoryDatabase.importFromCSV("test.csv");
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(ButterGUI.this, "Error importing data from file.");
							e1.printStackTrace();
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(ButterGUI.this, "Error importing data from file.");
							e1.printStackTrace();
						}
					}
				});
				inportMenu.add(importButton);	
				
				this.getContentPane().add(searchButton);
				this.getContentPane().add(viewDataButton);
				this.getContentPane().add(exportDataButton);
				this.getContentPane().add(exitButton);
				this.getContentPane().add(informationArea);
				this.getContentPane().add(destinationPanel);
				this.getContentPane().add(mapPanel);

				this.setVisible(true);
				this.pack();		
	}
	

	public List<Vertex> dijkstraPath() throws SQLException, Exception {
		Vector<Vertex> vertexList = new Vector<Vertex>();
		List<Vertex> path = null;
		
		for(Vector<Ramp> vr : RampBank.allRamps) {
			for(Ramp r : vr) {
				vertexList.add(new Vertex(r));
			}
		}

		/**
		 *  Inefficient, but functional!
		 */
		for(int i=0; i < vertexList.size(); ++i) {
			Vertex v = vertexList.get(i);
			for(Vector<Ramp>  vr : RampBank.allRamps) {
				for(Ramp ramp : vr) {
					
					if(v.name.equals(ramp.name) && v.freeway == ramp.freeway) {
						
						if(ramp.next != null) {
							Vertex next = null;
							for(Vertex n : vertexList) {
								if(n.name.equals(ramp.next.name) && n.freeway == ramp.next.freeway)
									next = n;
							}
							if(v.freeway == 10) {
								v.adjacencies.add(
										new Edge(next, trafficHistoryDatabase.getEdgeAverageTime(Car.WEST, ramp))
										);
							}
							else if(v.freeway == 101) {
								v.adjacencies.add(
										new Edge(next, trafficHistoryDatabase.getEdgeAverageTime(Car.NORTH, ramp))
										);
							}
							else if(v.freeway == 105) {
								v.adjacencies.add(
										new Edge(next, trafficHistoryDatabase.getEdgeAverageTime(Car.EAST, ramp))
										);
							}
							else if(v.freeway == 405) {
								v.adjacencies.add(
										new Edge(next, trafficHistoryDatabase.getEdgeAverageTime(Car.NORTH, ramp))
										);
							}
						}
						
						if(ramp.previous != null) {
							Vertex previous = null;
							for(Vertex n : vertexList) {
								if(n.name.equals(ramp.previous.name) && n.freeway == ramp.previous.freeway)
									previous = n;
							}
							if(v.freeway == 10) {
								v.adjacencies.add(
										new Edge(previous, trafficHistoryDatabase.getEdgeAverageTime(Car.EAST, ramp))
										);
							}
							else if(v.freeway == 101) {
								v.adjacencies.add(
										new Edge(previous, trafficHistoryDatabase.getEdgeAverageTime(Car.SOUTH, ramp))
										);
							}
							else if(v.freeway == 105) {
								v.adjacencies.add(
										new Edge(previous, trafficHistoryDatabase.getEdgeAverageTime(Car.WEST, ramp))
										);
							}
							else if(v.freeway == 405) {
								v.adjacencies.add(
										new Edge(previous, trafficHistoryDatabase.getEdgeAverageTime(Car.SOUTH, ramp))
										);
							}
						}
						
						if(ramp.branch != null) {
							Vertex branch = null;
							for(Vertex n : vertexList) {
								if(n.name.equals(ramp.branch.name) && n.freeway == ramp.branch.freeway)
									branch = n;
							}
							if(v.freeway == 10) {
								v.adjacencies.add(
										new Edge(branch, 0)
										);
							}
							else if(v.freeway == 101) {
								v.adjacencies.add(
										new Edge(branch, 0)
										);
							}
							else if(v.freeway == 105) {
								v.adjacencies.add(
										new Edge(branch, 0)
										);
							}
							else if(v.freeway == 405) {
								v.adjacencies.add(
										new Edge(branch, 0)
										);
							}
						}
						
					}
					
				}
			}

		}	
		
		boolean foundIndex = false;
		int startIndex = 0;
		for (int i =0; i<vertexList.size(); i++){
			if (fromRamp.getSelectedItem().equals(vertexList.get(i).getName()) && fromHighway.getSelectedItem().equals(vertexList.get(i).freewayName)){
				startIndex = i;
				foundIndex = true;
			}
		}
		
		
		if(!foundIndex)
				System.out.println("DIDN'T FIND INDEX");
		computePaths(vertexList.get(startIndex));

		boolean gotDestination = false;
		
		for (int i=0; i< vertexList.size(); i++) {
			Vertex v = vertexList.get(i);
			if (toRamp.getSelectedItem().equals(v.getName()) && !gotDestination){	
				noCar = true;
				jta.setText("");
				jta.setText(jta.getText() + "From: " + fromHighway.getSelectedItem() + " - " + fromRamp.getSelectedItem() + "\n");
				jta.setText(jta.getText() + "To: " + toHighway.getSelectedItem() + " - " + toRamp.getSelectedItem() + "\n");
				jta.setText(jta.getText() + "     Time to Destination: " + (v.minDistance * 60.0) + " minutes \n");
				path = getShortestPathTo(v);
				
				/**
				 *  Defining the path into the information box.
				 */
				jta.setText(jta.getText() + "\n-------------DIRECTIONS-------------");
				jta.setText(jta.getText() + "\n");
				for(int a =0; a<path.size(); a++) {
					if (path.size() == 1){
						jta.setText(jta.getText() + "You are at " + path.get(a) + "!" + "\n");
					} else if (path.size() == 2){
						if (a == 0){
							jta.setText(jta.getText() + "Start from " + path.get(a) + "\n");
						} else if (a == 1){ 
							jta.setText(jta.getText() + "Your destination " + path.get(a) + " is the next ramp.\n");
						}
					} else if (a == 0){
						jta.setText(jta.getText() + "Start from " + path.get(a) + " towards " + "\n");
					} else if (a == path.size()-1){
						jta.setText(jta.getText() + "Arrived at " + path.get(a) + "\n");
					} else {
						jta.setText(jta.getText() + "     ramp " + path.get(a) + " \u2192\n");
					}
					
				}
				
				if (locationMarkers.size() > 1){
					locationMarkers.clear();
				}
				
				for (int k=0; k<RampBank.allRamps.size(); k++){
					for (int l=0; l<RampBank.allRamps.get(k).size(); l++){
						for (int j =0; j<path.size(); j++){
							if ( RampBank.allRamps.get(k).get(l).name.equals(path.get(j).getName()) && RampBank.allRamps.get(k).get(l).freeway == path.get(j).freeway){
								Marker mark = new Marker(RampBank.allRamps.get(k).get(l).getLocation().point.x, RampBank.allRamps.get(k).get(l).getLocation().point.y);
								locationMarkers.add(mark);
							}
						}
					}
				}
				mapPicPanel.repaint();
				gotDestination = true;
			}
		}
		gotDestination = false;
		return path;
	}

	
	public static void computePaths(Vertex source){
        source.minDistance = 0.;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
	vertexQueue.add(source);

	while (!vertexQueue.isEmpty()) {
	    Vertex u = vertexQueue.poll();
            for (Edge e : u.adjacencies)
            {
                Vertex v = e.target;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
				
                if (distanceThroughU < v.minDistance) {
				    vertexQueue.remove(v);
		
				    v.minDistance = distanceThroughU ;
				    v.previous = u;
				    vertexQueue.add(v);
				}
            }
        }
    }
	

	
	
    public static List<Vertex> getShortestPathTo(Vertex target){
        List<Vertex> path = new ArrayList<Vertex>();
        
        for (Vertex vertex = target; vertex != null; vertex = vertex.previous) {
        	path.add(vertex);
        }     

        Collections.reverse(path);
        return path;
    }
	
	
	public static void main(String[] args) throws IOException {
		try {
			UIManager.setLookAndFeel(
            UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		new ButterGUI();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for (int i=0; i<allCarsWrapper.allCars.size(); i++){
			if (allCarsWrapper.allCars.get(i).checkPoint(e.getX(), e.getY())){
				noCar = false;
				setFocusCar(i);
				setTextCarInfo(i);
				break;
			} else {
				noCar = true;
			}
		}
	}
	
	public void setTextCarInfo(int carIndex) {
		jta.setText("");
		jta.setText(jta.getText() + "Butter - Car: " + allCarsWrapper.allCars.get(carIndex).getId() + "\n");
		jta.setText(jta.getText() + "            Speed: " + allCarsWrapper.allCars.get(carIndex).getSpeed() + "mph \n");
		jta.setText(jta.getText() + "            Freeway: " + allCarsWrapper.allCars.get(carIndex).getFreeway() + "\n");			
		jta.setText(jta.getText() + "            Direction: " + allCarsWrapper.allCars.get(carIndex).getDirection() + "\n");		
		jta.setText(jta.getText() + "            Ramp: " + allCarsWrapper.allCars.get(carIndex).getRamp() + "\n");
	}
	
	public static void updateTextCarInfo(Car c) {
		jta.setText("");
		jta.setText(jta.getText() + "Butter - Car: " + c.getId() + "\n");
		jta.setText(jta.getText() + "            Speed: " + c.getSpeed() + " mph \n");
		jta.setText(jta.getText() + "            Freeway: " + c.getFreeway() + "\n");			
		jta.setText(jta.getText() + "            Direction: " + c.getDirection() + "\n");		
		jta.setText(jta.getText() + "            Ramp: " + c.getRamp() + "\n");
	}
	
	public void deleteMapJPG(){
		boolean success = (new File ("map.jpg")).delete();
     if (success) {
        System.out.println("The file map.jpg has been successfully deleted."); 
     }
	}
	
	public void setFocusCar(int carIndex) {
		for (int i=0; i<allCarsWrapper.allCars.size(); i++){
			allCarsWrapper.allCars.get(i).TextIsDisplayed = false;
		}
		allCarsWrapper.allCars.get(carIndex).TextIsDisplayed = true;
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	public static Vector<Marker> getLocationMarkers() {
		return locationMarkers;
	}
}
