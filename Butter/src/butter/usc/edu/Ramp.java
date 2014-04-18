package butter.usc.edu;

import java.awt.Point;

/*
 *  Ramp class, creates a ramp given the name, freeway number, and the index of the coordinate in the respective array of path coordinates. 
 *  @author Kaitlyn
 */
public class Ramp {
	String name;
	int freeway;
	int indexOfCoordinate;
	Location l;
	//double latitude;
	//double longitude;
	/*
	public Ramp(String name, int freeway, double longitude, double latitude) {
		this.name = name;
		this.freeway = freeway;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	*/
	public Ramp(String name, int freeway, int indexOfCoordinate, Location l) {
		this.name = name;
		this.freeway = freeway;
		this.indexOfCoordinate = indexOfCoordinate;
		this.l = l;
		//System.out.println(name + ", " + indexOfCoordinate + " lat, long: " + l.getLatitude() + ", " + l.getLongitude());
	}
	public int getIndexOfCoordinate() {
		return indexOfCoordinate;
	}
}
