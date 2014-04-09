package butter.usc.edu;

import java.io.Serializable;

/**
 * Just a object to package up longitude and latitude pairs.
 * @author LorraineSposto
 *
 */
public class Location implements Serializable {
	private static final long serialVersionUID = 1856189671964327747L;

	private double latitude;

	private double longitude;

	public Location() {

	}

	public Location(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
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
}