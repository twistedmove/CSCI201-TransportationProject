package butter.usc.edu;

import java.awt.Point;
import java.io.Serializable;
import java.util.Vector;

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
	double milesToNext;
	double milesToPrev;

	public Location previous;
	public Location next;

	///////////////////////////////////////////////////////////////////////////////////////////////////
	public int freeway;
	public int branchNum;
	public Location branch;
	public double milesToBranch;
	///////////////////////////////////////////////////////////////////////////////////////////////////

	//public double previousDist;
	//public double nextDist;

	Boolean isFirst = false;
	Boolean isLast = false;

	public Location(double latitude, double longitude, int freeway, int branchNum) {
		this.longitude = longitude;
		this.latitude = latitude;
		point = new Point();
		point.y = (int) ((34.449805 - latitude)*2002.79);
		point.x = (int)((118.725 + longitude)*1655.64 + 6);
		// System.out.println("lat, long: " + latitude + ", " + longitude + ". " + point);

		this.freeway = freeway;
		this.branchNum = branchNum;

	}
	public void setPrev(Location l) {
		previous = l;
		l.next = this;
		milesToPrev = getDistanceFromLatLonInM(this.latitude, this.longitude, l.latitude, l.longitude);
		l.milesToNext = this.milesToPrev;
	}

	public void setNext(Location l) {
		next = l;
		l.previous = this;
		milesToNext = getDistanceFromLatLonInM(this.latitude, this.longitude, l.latitude, l.longitude);
		l.milesToPrev = this.milesToNext;
	}

	public void setBranch(Location l) {
		branch = l;
		l.branch = this;
		milesToBranch = getDistanceFromLatLonInM(this.latitude, this.longitude, l.latitude, l.longitude);
		l.milesToBranch = this.milesToPrev;
	}

	public double distToPointAway(int pointsAway) { 
		/* positive pointsAway looks at the location pointsAway ahead of the current location. 
		 * For example, pointsAway = 1 would look at the "next" location. pointsAway = -2 would look at the previous of the previous location. 
		 */
		
		Location away = cloneLocation(this);
		
		if (pointsAway > 0) {
			for (int i = 0; i < pointsAway; i++) {
				away = cloneLocation(away.next);
			}

			return getDistanceFromLatLonInM(this.latitude, this.longitude, away.latitude, away.longitude);
		}
		else if (pointsAway < 0) {
			for (int i = 0; i > pointsAway; i--) {
				away = cloneLocation(away.previous);
			}
			return getDistanceFromLatLonInM(this.latitude, this.longitude, away.latitude, away.longitude);
		}

		// if the locations are equal, distance is 0
		return 0;
	}

	public Location getLocationAway(int pointsAway) {
		Location away = cloneLocation(this);

		if (pointsAway > 0) {
			for (int i = 0; i < pointsAway; i++) {
				away = cloneLocation(away.next);
			}
			return away;
		}
		else if (pointsAway < 0) {
			for (int i = 0; i > pointsAway; i--) {
				away = cloneLocation(away.previous);
			}
			return away;
		}
		return this;
	}

	public Location cloneLocation(Location l) {
		Location ln = null;
			ln = new Location(l.latitude, l.longitude, l.freeway, l.branchNum);
			ln.isFirst = l.isFirst;
			ln.isLast = l.isLast;
			ln.milesToBranch = l.milesToBranch;
			ln.milesToNext = l.milesToNext;
			ln.milesToPrev = l.milesToPrev;
			ln.next = l.next;
			ln.point = l.point;
			ln.previous = l.previous;
			ln.latitude = l.latitude;
			ln.longitude = l.longitude;
		return ln;
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
		String str = ("[" + latitude + ", " + longitude + "] " 
				+ "\n\t\tfreeway: " + freeway + " branch: " + branch);
		return str;
	}

	public void setLast() {
		isLast = true;
	}
	public void setFirst() {
		isFirst = true;
	}
	public static double getDistanceFromLatLonInM(double lat1, double lon1, double lat2, double lon2) {
		//double R = 6371; // Radius of the earth in km
		double R = 3958.756; // Radius of the earth in miles
		double dLat = deg2rad(lat2-lat1);  // deg2rad below
		double dLon = deg2rad(lon2-lon1); 
		double a = 
				Math.sin(dLat/2) * Math.sin(dLat/2) +
				Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
				Math.sin(dLon/2) * Math.sin(dLon/2)
				; 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		double d = R * c; // Distance in km
		return d;
	}
	public static double deg2rad(double deg) {
		return deg * (Math.PI/180);
	}
}