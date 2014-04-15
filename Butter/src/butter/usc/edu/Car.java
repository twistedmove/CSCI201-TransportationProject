package butter.usc.edu;

import java.awt.Point;

/**
 * Car class, so far only holding the essential values of a car as outlined in test.json
 * @author LorraineSposto
 *
 */
public class Car {
	private int id;
	private double speed;
	//private int pixToMove;
	private String direction;
	private String ramp;
	private String freeway;
	private int freewayIndex;
	private int rampIndex;
	private int coordinateIndex;
	public Point point;
	//public Location currentLocation;
	public static final String EAST = "East";
	public static final String WEST = "West";
	public static final String NORTH = "North";
	public static final String SOUTH = "South";
	//public static final int pixelsPerMile = 29;
	public static final int updatePerSec = 8;
	public static final double secPerHour = 360.00;

	public double milesPerTimeDiv;
	private Boolean sloped = false;
	private double slopeX, slopeY;
	private double intercept;
	private int counter;
	private int divisions;
	private Point temp;
	private Boolean slopeDone = false;

	public Car() {

	}

	public Car(int id, double speed, String direction, String ramp, String freeway) {
		this.id = id;
		this.speed = speed;
		this.direction = direction;
		this.ramp = ramp;
		this.freeway = freeway;
		//location = new Point(100,100);
		//	pixToMove = (int)((speed / secPerHour) * pixelsPerMile / updatePerSec);
		this.freewayIndex = getFreewayIndex(this);
		this.rampIndex = getRampIndex(this);
		this.point = RampBank.allRamps.get(freewayIndex).get(rampIndex).l.point;// PathBank.allLocations.get(freewayIndex).get(rampIndex).point;
		this.coordinateIndex = RampBank.allRamps.get(freewayIndex).get(rampIndex).indexOfCoordinate;
		milesPerTimeDiv = speed / secPerHour / updatePerSec;
		System.out.println("Car " + id + " with speed: " + speed + " miles per time div: " + milesPerTimeDiv);
		System.out.println(RampBank.allRamps.get(freewayIndex).get(rampIndex).name + " index coordinate: " + RampBank.allRamps.get(freewayIndex).get(rampIndex).indexOfCoordinate);

		System.out.println("lat, long: " + RampBank.allRamps.get(freewayIndex).get(rampIndex).l.getLatitude() + ", " + RampBank.allRamps.get(freewayIndex).get(rampIndex).l.getLongitude() + ". " + point);
	}

	public static int getFreewayIndex(Car c) {

		for (int i = 0; i < RampBank.freeways.length; i++) {
			if (c.freeway.equals(RampBank.freeways[i])) {
				return i;
			}
		}
		System.out.println("***Freeway not found*** ");
		return -1;
	}

	public static int getRampIndex(Car c) {
		for (int i = 0; i < RampBank.rampNames[c.freewayIndex].length; i++) {
			if (c.ramp.equals(RampBank.rampNames[c.freewayIndex][i])) {
				return i;
			}
		}
		System.out.println("Ramp not found");
		return -1;
	}

	public void updateSpeed() {
		Boolean increased = false;
		double tempX = 0;
		double tempY = 0;
		if (sloped) {
			// System.out.println("Sloped, ");
			point.x += (int) (slopeX); 
			point.y += (int) (slopeY);
			if (counter == (divisions - 1)) {
				sloped = false;
				slopeDone = true;
				//System.out.println("Quit");
			}
			else {
				counter++;
			}
			//System.out.println(point);
		}

		else {
			if (direction.equals(NORTH) ){
				if (freewayIndex == 3) { // If on the 405
					//System.out.println(point);
					//System.out.println(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).milesToNext);
					//System.out.println("Next should be: " + PathBank.allLocations.get(freewayIndex).get(coordinateIndex).next.point);
					tempX = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).next.point.x - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.x;
					tempY = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).next.point.y - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.y;
					if ((Math.abs(tempX) > 1 || Math.abs(tempY) > 1 ) && PathBank.allLocations.get(freewayIndex).get(coordinateIndex).milesToNext >= 2*milesPerTimeDiv && !slopeDone) {
						//slope = tempY / tempX;
						counter = 1;
						double rawDiv = Math.floor(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).milesToNext / milesPerTimeDiv);
						divisions = (int)(Math.min(rawDiv, Math.max(Math.abs(tempX), Math.abs(tempY))));
						slopeX = tempX / (double)divisions;
						slopeY = tempY / (double)divisions;
						//System.out.println("Divisions: " + divisions + " rawdiv " + rawDiv + " slope: " + slopeX +", " + slopeY + " total to move: " + tempX + ", " + tempY);

						//temp = point;
						sloped = true;
					}
					else { // This includes slopeDone
						coordinateIndex++;
						increased = true;
					}
				}
			}
			else if (direction.equals(SOUTH)) { 
				if (freewayIndex == 3) { // If on the 405
					System.out.println(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).milesToPrev);
					tempX = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.x - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).previous.point.x;
					tempY = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.y - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).previous.point.y;

					if ((Math.abs(tempX) > 1 || Math.abs(tempY) > 1 ) && PathBank.allLocations.get(freewayIndex).get(coordinateIndex).milesToNext >= 2*milesPerTimeDiv && !slopeDone) {
						//slope = tempY / tempX;
						counter = 1;
						double rawDiv = Math.floor(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).milesToPrev / milesPerTimeDiv);
						divisions = (int)(Math.min(rawDiv, Math.max(Math.abs(tempX), Math.abs(tempY))));
						slopeX = tempX / (double)divisions;
						slopeY = tempY / (double)divisions;
						//System.out.println("Divisions: " + divisions + " rawdiv " + rawDiv + " slope: " + slopeX +", " + slopeY + " total to move: " + tempX + ", " + tempY);

						//temp = point;
						sloped = true;
					}
					else {
						coordinateIndex--;
					}
				}
			}
			else if (direction.equals(EAST)) {
				if (freewayIndex == 1 || freewayIndex == 2) { // If on the 101, 105, or 10
					System.out.println(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).milesToNext);
					tempX = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).next.point.x - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.x;
					tempY = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).next.point.y - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.y;
					if ((Math.abs(tempX) > 1 || Math.abs(tempY) > 1 ) && PathBank.allLocations.get(freewayIndex).get(coordinateIndex).milesToNext >= 2*milesPerTimeDiv && !slopeDone) {
						//slope = tempY / tempX;
						counter = 1;
						double rawDiv = Math.floor(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).milesToNext / milesPerTimeDiv);
						divisions = (int)(Math.min(rawDiv, Math.max(Math.abs(tempX), Math.abs(tempY))));
						slopeX = tempX / (double)divisions;
						slopeY = tempY / (double)divisions;
						//System.out.println("Divisions: " + divisions + " rawdiv " + rawDiv + " slope: " + slopeX +", " + slopeY + " total to move: " + tempX + ", " + tempY);
						
						//temp = point;
						sloped = true;
					}
					else {
					
						coordinateIndex++;
						increased = true;
					}
					//increased = true;
				}
				else if (freewayIndex == 0) {
					System.out.println(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).milesToPrev);
					tempX = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.x - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).previous.point.x;
					tempY = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.y - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).previous.point.y;
					if ((Math.abs(tempX) > 1 || Math.abs(tempY) > 1 ) && PathBank.allLocations.get(freewayIndex).get(coordinateIndex).milesToNext >= 2*milesPerTimeDiv && !slopeDone) {
						//slope = tempY / tempX;
						counter = 1;
						double rawDiv = Math.floor(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).milesToPrev / milesPerTimeDiv);
						divisions = (int)(Math.min(rawDiv, Math.max(Math.abs(tempX), Math.abs(tempY))));
						slopeX = tempX / (double)divisions;
						slopeY = tempY / (double)divisions;
						//System.out.println("Divisions: " + divisions + " rawdiv " + rawDiv + " slope: " + slopeX +", " + slopeY + " total to move: " + tempX + ", " + tempY);
						
						//temp = point;
						sloped = true;
					}
					
					else {
						coordinateIndex--;
					}
					
				}
			}
			else if (direction.equals(WEST)) { 
				if (freewayIndex == 1 || freewayIndex == 2) { // If on the 101, 105, or 10
					System.out.println(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).milesToPrev);
					tempX = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.x - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).previous.point.x;
					tempY = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.y - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).previous.point.y;

					if ((Math.abs(tempX) > 1 || Math.abs(tempY) > 1 ) && PathBank.allLocations.get(freewayIndex).get(coordinateIndex).milesToNext >= 2*milesPerTimeDiv && !slopeDone) {
						//slope = tempY / tempX;
						counter = 1;
						double rawDiv = Math.floor(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).milesToPrev / milesPerTimeDiv);
						divisions = (int)(Math.min(rawDiv, Math.max(Math.abs(tempX), Math.abs(tempY))));
						slopeX = tempX / (double)divisions;
						slopeY = tempY / (double)divisions;
						//System.out.println("Divisions: " + divisions + " rawdiv " + rawDiv + " slope: " + slopeX +", " + slopeY + " total to move: " + tempX + ", " + tempY);
						
						//temp = point;
						sloped = true;
					}
					
					else {
						coordinateIndex--;
					}
				}
				else if (freewayIndex == 0) {
					System.out.println(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).milesToNext);
					tempX = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).next.point.x - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.x;
					tempY = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).next.point.y - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.y;
					if ((Math.abs(tempX) > 1 || Math.abs(tempY) > 1 ) && PathBank.allLocations.get(freewayIndex).get(coordinateIndex).milesToNext >= 2*milesPerTimeDiv && !slopeDone) {
						//slope = tempY / tempX;
						counter = 1;
						double rawDiv = Math.floor(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).milesToNext / milesPerTimeDiv);
						divisions = (int)(Math.min(rawDiv, Math.max(Math.abs(tempX), Math.abs(tempY))));
						slopeX = tempX / (double)divisions;
						slopeY = tempY / (double)divisions;
						//System.out.println("Divisions: " + divisions + " rawdiv " + rawDiv + " slope: " + slopeX +", " + slopeY + " total to move: " + tempX + ", " + tempY);
						
						//temp = point;
						sloped = true;
					}
					else {
					
						coordinateIndex++;
						increased = true;
					}
//					increased = true;
				}
			}
			// Not going beyond the bounds of the path
			if (((increased && coordinateIndex < PathBank.allLocations.get(freewayIndex).size()) || (!increased && coordinateIndex >= 0)) && !sloped) {
				this.point = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point;
				System.out.println("Delta X, Y: " + tempX + ", " + tempY);
			}
			slopeDone = false;
		}


	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getRamp() {
		return ramp;
	}

	public void setRamp(String ramp) {
		this.ramp = ramp;
	}

	public String getFreeway() {
		return freeway;
	}

	public void setFreeway(String freeway) {
		this.freeway = freeway;
	}

	public String toString() {
		return (id + "," + speed + "," + direction + "," + ramp + "," + freeway);
	}

	public String insertString() {
		return (id + "," + speed + ",'" + direction + "','" + ramp + "'," + freeway);
	}
}
