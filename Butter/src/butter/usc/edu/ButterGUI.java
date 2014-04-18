package butter.usc.edu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
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
import javax.swing.Timer;
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
	@SuppressWarnings("rawtypes")
	private static JComboBox fromHighway;
	@SuppressWarnings("rawtypes")
	private static JComboBox toHighway;
	@SuppressWarnings("rawtypes")
	private static JComboBox fromRamp;
	@SuppressWarnings("rawtypes")
	private static JComboBox toRamp;
	private JTextArea jta;
	private TrafficHistoryDatabase trafficHistoryDatabase;
	private static DFSGraph dfsg;
	
	
	
	Image mascot;
	ImageIcon sliced;
//	private Vector<Car> allCars;
	public static AllCarsWrapper allCarsWrapper;
	private CarTimer carTimer;
	Image map;
	@SuppressWarnings("unused")
	private PathBank pb;
	private RampBank rb;

	public ButterGUI () {
		this.setMinimumSize(new Dimension(1200,720));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setTitle("Butter");
		this.getContentPane().setLayout(null);
		this.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
			    trafficHistoryDatabase.dropDatabase();
			}
		});
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//		allCars = new Vector<Car>();
		allCarsWrapper = new AllCarsWrapper();
		try {
			pb = new PathBank();
		} catch (TxtFormatException tfe) {
			jta.setText(jta.getText() + "Butter: TFE Exception. Contact Admin." + "\n");
		}
		rb = new RampBank();

		importImage();
		setupButterGUI();
		//int id, double speed, String direction, String onOffRamp, String freeway) {
	
		try {
			trafficHistoryDatabase = new TrafficHistoryDatabase();
//			trafficHistoryDatabase.start();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		carTimer = new CarTimer(mapPanel);
		new Thread(carTimer).start();
	
		Car c = new Car(1,60, "West", "Western Avenue, Normandie Avenue", "10");
		Car c1 = new Car(2,90, "West","Tujunga Avenue", "101");
		
		Car c2 = new Car(3,30, "East","Crenshaw Boulevard", "105");
		Car c3 = new Car(4,60, "North","Sherman Way", "405");
		Car c4 = new Car(5,20, "North","Sherman Way", "405");
		Car c5 = new Car(6,20, "East", "Western Avenue, Normandie Avenue", "10");
		Car c6 = new Car(7,45, "East","Los Angeles Street", "101");
		Car c7 = new Car(8,50, "West","Crenshaw Boulevard", "105");
		
		allCarsWrapper.allCars.add(c);
		allCarsWrapper.allCars.add(c1);
		allCarsWrapper.allCars.add(c2);
		allCarsWrapper.allCars.add(c3);
		allCarsWrapper.allCars.add(c4);
		allCarsWrapper.allCars.add(c5);
		allCarsWrapper.allCars.add(c6);
		allCarsWrapper.allCars.add(c7);
		
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
		// MAP STUFF
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
				//JScrollPane jsp = new JScrollPane(picLabel);
				jsp.setBounds(6, 5, 895, 667);
					jsp.setPreferredSize(new Dimension (400,400));
					jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
					jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

					mapPanel.add(jsp);
		
		// TO AND FROM SEARCH
				JPanel destinationPanel = new JPanel();
					destinationPanel.setLayout(null);
					destinationPanel.setBounds(907, 5, 286, 125);
					destinationPanel.setBorder(new TitledBorder("Destination"));
				JPanel fromDestinationPanel = new JPanel();
					fromDestinationPanel.setLayout(null);
					fromDestinationPanel.setBounds(4, 16, 278, 54);
					fromDestinationPanel.setBorder(new TitledBorder("From")); 
				JPanel toDestinationPanel = new JPanel();
					toDestinationPanel.setLayout(null);
					toDestinationPanel.setBounds(4, 66, 278, 54);
					toDestinationPanel.setBorder(new TitledBorder("To")); 

					Object hwyOptions [] = {"US 101", "I-105", "I-10", "I-405"};
					fromHighway = new JComboBox(hwyOptions);
					fromHighway.setBounds(8, 16, 70, 30);
					fromHighway.setSelectedIndex(0);
					fromHighway.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							@SuppressWarnings("static-access")
							DefaultComboBoxModel model = new DefaultComboBoxModel(rb.rampNames[fromHighway.getSelectedIndex()]);
							fromRamp.setModel(model);
						}
					});
					
					@SuppressWarnings("static-access")
					DefaultComboBoxModel model1 = new DefaultComboBoxModel(rb.rampNames[fromHighway.getSelectedIndex()]);
					fromRamp = new JComboBox();
					fromRamp.setModel(model1);
					fromRamp.setBounds(82, 16, 190, 30);
					
					toHighway = new JComboBox(hwyOptions);
					toHighway.setSelectedIndex(0);
					toHighway.setBounds(8, 16, 70, 30);
					toHighway.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							@SuppressWarnings("static-access")
							DefaultComboBoxModel model = new DefaultComboBoxModel(rb.rampNames[toHighway.getSelectedIndex()]);
							toRamp.setModel(model);						
						}						
					});
					
					@SuppressWarnings("static-access")
					DefaultComboBoxModel model2 = new DefaultComboBoxModel(rb.rampNames[toHighway.getSelectedIndex()]);
					toRamp = new JComboBox();
					toRamp.setModel(model2);
					toRamp.setBounds(82, 16, 190, 30);
					
					fromDestinationPanel.add(fromHighway);
					fromDestinationPanel.add(fromRamp);
					
					toDestinationPanel.add(toHighway);
					toDestinationPanel.add(toRamp);
					
				destinationPanel.add(fromDestinationPanel);
				destinationPanel.add(toDestinationPanel);
						
				// INFORMATION AREA
				JPanel informationArea = new JPanel();
					informationArea.setBounds(907, 168, 287, 394);
					informationArea.setBorder (new TitledBorder(new EtchedBorder(), "Information"));
				jta = new JTextArea (23, 23);
			    	jta.setEditable (false);
			    JScrollPane scrollPane = new JScrollPane (jta);
			    	scrollPane.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			    	scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			    informationArea.add(scrollPane);
				
				
			    // BUTTONS
				JButton searchButton = new JButton("Find Route");
				searchButton.setBounds(907, 134, 287, 30);
				searchButton.setBackground(new Color(59, 89, 182));
				searchButton.setForeground(Color.WHITE);
				searchButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						depthFirstSearch();
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
							System.exit(0);
						}
					}
				});
				
				// MENUBAR
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
	
	public static int depthFirstSearch(){
		Node n = null;
		// setting up the root node for the search
		if (fromHighway.getSelectedItem().equals("US 101")){
			int num = 0;
			for (int i=0; i<RampBank.ramps101.size(); i++){
				if (RampBank.ramps101.get(i).name == fromRamp.getSelectedItem()){
					num = RampBank.ramps101.get(i).indexOfCoordinate;
				}
			}
			Location l = PathBank.locations101.get(num);
			n = new Node(l);
		} else if (fromHighway.getSelectedItem().equals("I-10")){
			int num = 0;
			for (int i=0; i<RampBank.ramps10.size(); i++){
				if (RampBank.ramps10.get(i).name == fromRamp.getSelectedItem()){
					num = RampBank.ramps10.get(i).indexOfCoordinate;
				}
			}
			Location l = PathBank.locations10.get(num);
			n = new Node(l);
		} else if (fromHighway.getSelectedItem().equals("I-105")){
			int num = 0;
			for (int i=0; i<RampBank.ramps105.size(); i++){
				if (RampBank.ramps105.get(i).name == fromRamp.getSelectedItem()){
					num = RampBank.ramps105.get(i).indexOfCoordinate;
				}
			}
			Location l = PathBank.locations105.get(num);
			n = new Node(l);
		} else if (fromHighway.getSelectedItem().equals("I-405")){
			int num = 0;
			for (int i=0; i<RampBank.ramps405.size(); i++){
				if (RampBank.ramps405.get(i).name == fromRamp.getSelectedItem()){
					num = RampBank.ramps405.get(i).indexOfCoordinate;
				}
			}
			Location l = PathBank.locations405.get(num);
			n = new Node(l);
		}
		
		// adding nodes to nodelist
		dfsg = new DFSGraph();
		for (int i=0; i < PathBank.allLocations.size();i++){
			for (int j=0; j < PathBank.allLocations.get(i).size(); j++){
				dfsg.addNode(new Node(PathBank.allLocations.get(i).get(j)));
			}
		}
		
		dfsg.setInitialRoot(n);
		
		// connecting nodes
		// each freeway has 1 next null and 1 previous null locations
		for (int i=0; i<dfsg.nodeList.size(); i++){
			if (dfsg.nodeList.get(i).getLocation().branch != null){
				int index = findIndexofLocation(dfsg.nodeList.get(i).getLocation().branch);
				if (index != -1){
					dfsg.connectNode(dfsg.nodeList.get(i), dfsg.nodeList.get(index));
				}
			} else if (dfsg.nodeList.get(i).getLocation().next!= null){
				int index = findIndexofLocation(dfsg.nodeList.get(i).getLocation().next);
				if (index != -1){
					dfsg.connectNode(dfsg.nodeList.get(i), dfsg.nodeList.get(index));
				}
			}
		}
		
		
		
		dfsg.depthfirstsearch();
		System.out.println("done");
		return 0;
	}
	
	public static int findIndexofLocation(Location l){
		int val = -1;
		for (int i =0; i<dfsg.nodeList.size(); i++){
			if (l == dfsg.nodeList.get(i).getLocation()){
				val = i;
			}
		}
		return val;
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
				jta.setText("");
				jta.setText(jta.getText() + "Butter - Car: " + allCarsWrapper.allCars.get(i).getId() + "\n");
				jta.setText(jta.getText() + "            Speed: " + allCarsWrapper.allCars.get(i).getSpeed() + "\n");
				jta.setText(jta.getText() + "            Freeway: " + allCarsWrapper.allCars.get(i).getFreeway() + "\n");			
				jta.setText(jta.getText() + "            Direction: " + allCarsWrapper.allCars.get(i).getDirection() + "\n");		
				jta.setText(jta.getText() + "            Ramp: " + allCarsWrapper.allCars.get(i).getRamp() + "\n");
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
