package butter.usc.edu;

import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

public class RoutePanel extends JPanel {
	List<Vertex> path;
	public RoutePanel() {
		super();
		this.path = null;
	}
	
	public void drawRoute(List<Vertex> path) {
		this.path = path;
		if(path != null) {
			this.repaint();
		}
	}
	
	protected void paintComponent(Graphics g) {
		if(path != null) {
			
		}
	}
}
