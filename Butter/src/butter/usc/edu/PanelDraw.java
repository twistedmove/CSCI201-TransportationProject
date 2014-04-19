package butter.usc.edu;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PanelDraw extends JPanel{ 
	private static final long serialVersionUID = -8653010960482307907L;

	private Image map;
	public PanelDraw(Image map) {
		super();
		this.map = map;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponents(g);			

		Image car0GreenEast = null, car0GreenNorth = null, car0GreenSouth = null, car0GreenWest = null, car0YellowEast = null, car0YellowNorth = null, car0YellowSouth = null, car0YellowWest = null, car0RedEast = null, car0RedNorth = null, car0RedSouth = null, car0RedWest = null;

		try {
			car0GreenEast = ImageIO.read(new File("assets/images/car0greeneast.gif"));
			car0GreenNorth = ImageIO.read(new File("assets/images/car0greennorth.gif"));
			car0GreenSouth = ImageIO.read(new File("assets/images/car0greensouth.gif"));
			car0GreenWest = ImageIO.read(new File("assets/images/car0greenwest.gif"));
			car0YellowEast = ImageIO.read(new File("assets/images/car0yelloweast.gif"));
			car0YellowNorth = ImageIO.read(new File("assets/images/car0yellownorth.gif"));
			car0YellowSouth = ImageIO.read(new File("assets/images/car0yellowsouth.gif"));
			car0YellowWest = ImageIO.read(new File("assets/images/car0yellowwest.gif"));
			car0RedEast = ImageIO.read(new File("assets/images/car0redeast.gif"));
			car0RedNorth = ImageIO.read(new File("assets/images/car0rednorth.gif"));
			car0RedSouth = ImageIO.read(new File("assets/images/car0redsouth.gif"));
			car0RedWest = ImageIO.read(new File("assets/images/car0redwest.gif"));
		} catch (IOException ex) {
			System.out.println("No file exists.");
		}

		g.clearRect(0, 0, getWidth(), getHeight() );
		g.drawImage(map, 0, 0, null);
		for (int i = 0; i < ButterGUI.allCarsWrapper.allCars.size(); i++) {
			Graphics2D g2d=(Graphics2D)g;
			AffineTransform old = g2d.getTransform();
			g2d.rotate(ButterGUI.allCarsWrapper.allCars.get(i).getRadiansToRotate(),ButterGUI.allCarsWrapper.allCars.get(i).point.x + 8, ButterGUI.allCarsWrapper.allCars.get(i).point.y + 6);//Math.PI * (3.0/4.0)
			if (ButterGUI.allCarsWrapper.allCars.get(i).getSpeed() >= 60){
				g2d.drawImage(car0GreenEast, ButterGUI.allCarsWrapper.allCars.get(i).point.x, ButterGUI.allCarsWrapper.allCars.get(i).point.y, null);
			} else if (ButterGUI.allCarsWrapper.allCars.get(i).getSpeed() < 60 && ButterGUI.allCarsWrapper.allCars.get(i).getSpeed() > 35){
				g2d.drawImage(car0YellowEast, ButterGUI.allCarsWrapper.allCars.get(i).point.x, ButterGUI.allCarsWrapper.allCars.get(i).point.y, null);
			} else {
				g2d.drawImage(car0RedEast, ButterGUI.allCarsWrapper.allCars.get(i).point.x, ButterGUI.allCarsWrapper.allCars.get(i).point.y, null);
			}
			g2d.setTransform(old);
		}
	}
}
