package butter.usc.edu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ButterGUI extends JFrame{
	private static final long serialVersionUID = 367534120156013938L;

	public JPanel mapPanel;
	public PanelDraw mapPicPanel;
	public JLabel picLabel;
	private JTextField fromDestinationText;
	private JTextField toDestinationText;
	private JComboBox fromHighway;
	private JComboBox toHighway;
	private JComboBox fromRamp;
	private JComboBox toRamp;
	private Vector<Car> cars;
	private TrafficHistoryDatabase trafficHistoryDatabase;
	
	Image mascot;
	ImageIcon sliced;
	private Vector<Car> allCars;
	Image map;
	private RampBank rb;

	public ButterGUI () {
		this.setMinimumSize(new Dimension(1200,720));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setTitle("Butter");
		this.getContentPane().setLayout(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		allCars = new Vector<Car>();
		rb = new RampBank();
		importImage();
		setupButterGUI();
		//int id, double speed, String direction, String onOffRamp, String freeway) {
	
		trafficHistoryDatabase = new TrafficHistoryDatabase();
		trafficHistoryDatabase.run();
		getCarData();
		
	/*
		Car c = new Car(1,60, "East", "Some Offramp", "Some freeway");
		Car c1 = new Car(2,80, "East","Some Offramp", "Some freeway");
		Car c2 = new Car(3,80, "North","Some Offramp", "Some freeway");
		allCars.add(c);
		allCars.add(c1);
		allCars.add(c2);
		
		final int timeSlice = 250; 
		Timer timer = new  Timer (timeSlice, new ActionListener ()                                       {
		public void actionPerformed (ActionEvent e) {
		//	p.removeAll();
	    	mapPanel.updateUI();
			for (int i = 0; i < allCars.size(); i++) {
				allCars.get(i).updateSpeed();
			}
			mapPanel.repaint();
		}
		});
		timer.start();
	*/	  
	}
	
	private void importImage(){
		try {
			mascot = ImageIO.read(new File("assets/images/mascot.gif"));
			sliced = new ImageIcon("assets/images/sliced.gif");
		} catch (IOException ex) {
	        System.out.println("No file exists.");
		}
		
	}
	
	/**
	 * Used to grab data from wherever and add it to the database.
	 * Will need threads...
	 */
	private void getCarData() {
		try {
			Vector<Car> cars = CarDeserializer.deserializeArrayFromURL("http://www-scf.usc.edu/~csci201/mahdi_project/test.json");
			trafficHistoryDatabase.addNewCarData(cars);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void setupButterGUI(){	
		// MAP STUFF
				mapPanel = new JPanel();
				mapPanel.setLocation(0, 0);
				mapPanel.setSize(903, 672);
				mapPanel.setMinimumSize(new Dimension(950, 680));
				
				try {
					String imageUrl = "http://maps.googleapis.com/maps/api/staticmap?center=Los+Angeles,CA&zoom=12&size=1440x1440&scale=2&sensor=false";
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

				picLabel = new JLabel(new ImageIcon((new ImageIcon("map.jpg")).getImage().getScaledInstance(1440, 1440, java.awt.Image.SCALE_SMOOTH)));
				
				map = (new ImageIcon("map.jpg")).getImage().getScaledInstance(1440, 1440, java.awt.Image.SCALE_SMOOTH);
				mapPicPanel = new PanelDraw();
				mapPicPanel.setPreferredSize(new Dimension(1440,1440));
				
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

					Object hwyOptions [] = {"US 101", "I-105", "I-110", "I-405"};
					fromHighway = new JComboBox(hwyOptions);
					fromHighway.setBounds(8, 16, 70, 30);
					fromHighway.setSelectedIndex(0);
					fromHighway.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							DefaultComboBoxModel model = new DefaultComboBoxModel(rb.rampNames[fromHighway.getSelectedIndex()]);
							fromRamp.setModel(model);
						}
					});
					DefaultComboBoxModel model1 = new DefaultComboBoxModel(rb.rampNames[fromHighway.getSelectedIndex()]);
					fromRamp = new JComboBox();
					fromRamp.setModel(model1);
					fromRamp.setBounds(82, 16, 190, 30);
					
					toHighway = new JComboBox(hwyOptions);
					toHighway.setSelectedIndex(0);
					toHighway.setBounds(8, 16, 70, 30);
					toHighway.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							DefaultComboBoxModel model = new DefaultComboBoxModel(rb.rampNames[toHighway.getSelectedIndex()]);
							toRamp.setModel(model);						
						}						
					});
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
				JTextArea jta = new JTextArea (24, 23);
			    	jta.setEditable (false);
			    JScrollPane scrollPane = new JScrollPane (jta);
			    	scrollPane.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			    informationArea.add(scrollPane);
				
				
			    // BUTTONS
				JButton searchButton = new JButton("Find Route");
				searchButton.setBounds(907, 134, 287, 30);
				searchButton.setBackground(new Color(59, 89, 182));
				searchButton.setForeground(Color.WHITE);
				searchButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// Add actions to get the selected locations from the check boxes, then find route accordingly
					}
				});
			    
			    JButton viewDataButton = new JButton("View Data");
				viewDataButton.setBounds(907, 568, 287, 30);
				viewDataButton.setBackground(new Color(59, 89, 182));
				viewDataButton.setForeground(Color.WHITE);
				viewDataButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
					}
				});

				JButton exportDataButton = new JButton("Export Data");
				exportDataButton.setBounds(907, 603, 287, 30);
				exportDataButton.setBackground(new Color(59, 89, 182));
				exportDataButton.setForeground(Color.WHITE);
				exportDataButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
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
	
	class PanelDraw extends JPanel{ 
		private static final long serialVersionUID = -8653010960482307907L;

		protected void paintComponent(Graphics g) {
			super.paintComponents(g);
			g.clearRect(0, 0, getWidth(), getHeight() );
			g.drawImage(map, 0, 0, null);
			for (int i = 0; i < allCars.size(); i++) {
				g.fillRect(allCars.get(i).location.x, allCars.get(i).location.y, 10, 10);
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		try {
			UIManager.setLookAndFeel(
            UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// do something
		} catch (ClassNotFoundException e) {
			// do something
		} catch (InstantiationException e) {
			// do something
		} catch (IllegalAccessException e) {
			// do something
		}
		new ButterGUI();
	}
}
