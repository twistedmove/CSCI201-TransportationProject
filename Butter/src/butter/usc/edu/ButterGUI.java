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
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
	Image mascot;
	ImageIcon sliced;
	private Vector<Car> allCars;
	Image map;

	public ButterGUI () {
		this.setMinimumSize(new Dimension(1200,720));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setTitle("Butter");
		this.getContentPane().setLayout(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		allCars = new Vector<Car>();
		importImage();
		setupButterGUI();
		//int id, double speed, String direction, String onOffRamp, String freeway) {
	
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
		
	}
	
	private void importImage(){
		try {
			mascot = ImageIO.read(new File("assets/images/mascot.gif"));
			sliced = new ImageIcon("assets/images/sliced.gif");
		} catch (IOException ex) {
	        System.out.println("No file exists.");
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
					destinationPanel.setBounds(907, 5, 286, 105);
					destinationPanel.setBorder(new TitledBorder("Destination"));
				JPanel fromDestinationPanel = new JPanel();
					fromDestinationPanel.setLayout(null);
					fromDestinationPanel.setBounds(4, 20, 278, 40);
				JPanel toDestinationPanel = new JPanel();
					toDestinationPanel.setLayout(null);
					toDestinationPanel.setBounds(4, 60, 278, 40);
					
				fromDestinationText = new JTextField();
					fromDestinationText.setBackground(this.getBackground());
					fromDestinationText.setBorder(new TitledBorder("From")); 
				toDestinationText = new JTextField();
					toDestinationText.setBackground(this.getBackground());
					toDestinationText.setBorder(new TitledBorder("To")); 
				fromDestinationText.setBounds(0, 0, 277, 35);
				toDestinationText.setBounds(0, 0, 277, 35);
				fromDestinationPanel.add(fromDestinationText);
				toDestinationPanel.add(toDestinationText);
				destinationPanel.add(fromDestinationPanel);
				destinationPanel.add(toDestinationPanel);
						
				// INFORMATION AREA
				JPanel informationArea = new JPanel();
					informationArea.setBounds(907, 158, 287, 394);
					informationArea.setBorder (new TitledBorder(new EtchedBorder(), "Information"));
				JTextArea jta = new JTextArea (24, 23);
			    	jta.setEditable (false);
			    JScrollPane scrollPane = new JScrollPane (jta);
			    	scrollPane.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			    informationArea.add(scrollPane);
				
				
			    // BUTTONS
				JButton searchButton = new JButton("Search");
				searchButton.setBounds(907, 114, 287, 30);
				searchButton.setBackground(new Color(59, 89, 182));
				searchButton.setForeground(Color.WHITE);
				searchButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
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
