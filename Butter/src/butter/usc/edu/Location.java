package butter.usc.edu;

import java.awt.Point;
import java.io.Serializable;

/**
 * Just a object to package up longitude and latitude pairs.
 * @author LorraineSposto
 *
 */
public class Location implements Serializable {
	private static final long serialVersionUID = 1856189671964327747L;
	public Point point;
	private double latitude;
	private double longitude;
	public Location previous;
	public Location next;
	
	public double previousDist;
	public double nextDist;
	
	Boolean isFirst = false;
	Boolean isLast = false;

	public Location(double latitude, double longitude) {
		this.longitude = longitude;
		this.latitude = latitude;
		point = new Point();
		point.y = (int) ((34.449805 - latitude)*2002.79);
		point.x = (int)((118.725 + longitude)*1655.64 + 6);
		// System.out.println("lat, long: " + latitude + ", " + longitude + ". " + point);
	}
	public void setPrev(Location l) {
		previous = l;
		l.next = this;
	}
	
	public void setNext(Location l) {
		next = l;
		l.previous = this;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	public String toString() {
		return (latitude + ", " + longitude);
	}
	
	public void setLast() {
		isLast = true;
	}
	public void setFirst() {
		isFirst = true;
	}
	
}