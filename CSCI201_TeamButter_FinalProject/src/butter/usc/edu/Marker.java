package butter.usc.edu;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Marker {
	public int x;
	public int y;
	Image location = null;
	Marker(int xx, int yy){
		this.x = xx;
		this.y = yy;
		
		System.out.println(" | x: " + x + " | y : " + y);
		
		try {
			location = ImageIO.read(new File("assets/images/marker.gif"));
		} catch (IOException ex) {
			System.out.println("No file exists.");
		}
	}
		
	public void paintLocMarkers(Graphics g){	
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(location, this.x, this.y, null);
	}
	

}
