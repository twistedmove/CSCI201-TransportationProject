package butter.usc.edu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class AboutFrame extends JFrame{  
	private static final long serialVersionUID = 4297284355540307009L;
	Image mascot;
	Icon mascotIcon;

	public AboutFrame(Image m){  
		this.setSize(new Dimension(300,150));
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.mascot = m;
		this.setLayout(null);

		addLabels();
	}   

	private void addLabels(){
		JLabel aboutLabel = new JLabel("Butter");
		JLabel aboutDescriptionLabel = new JLabel("Made with lots of milk and love.");
		JLabel aboutDescriptionYear = new JLabel("Churning since 2014");
		aboutLabel.setFont(new Font("Helvetica", Font.BOLD, 15));
		aboutDescriptionLabel.setFont(new Font ("Helvetica", Font.PLAIN, 12));
		aboutDescriptionYear.setFont(new Font ("Helvetica", Font.PLAIN, 12));
		aboutLabel.setBounds(130, 40, 280, 40);
		aboutDescriptionLabel.setBounds(70, 80, 280, 40);
		aboutDescriptionYear.setBounds(100, 92, 280, 40);

		this.getContentPane().add(aboutLabel);
		this.getContentPane().add(aboutDescriptionLabel);
		this.getContentPane().add(aboutDescriptionYear);
	}
	
	@Override
	public void paint (Graphics g) {
        super.paint(g);
        g.drawImage(mascot, 125, 30, null); // see javadoc for more info on the parameters            
    }
}
