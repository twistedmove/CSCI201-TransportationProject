package butter.usc.edu;

/**
 * Holds the polyline string extracted from Google Directions API
 * @author LorraineSposto
 *
 */
public class OverviewPolyline {
	String points;
	
	public OverviewPolyline(String points) {
		this.points = points;
	}
	
	public String toString() {
		return points;
	}
}
