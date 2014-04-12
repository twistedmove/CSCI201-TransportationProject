package butter.usc.edu;
/*
 *  Ramp class, creates a ramp given the name, freeway number, and the index of the coordinate in the respective array of path coordinates. 
 *  @author Kaitlyn
 */
public class Ramp {
	String name;
	int freeway;
	int indexOfCoordinate;
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
	public Ramp(String name, int freeway, int indexOfCoordinate) {
		this.name = name;
		this.freeway = freeway;
		this.indexOfCoordinate = indexOfCoordinate;
	}
}
