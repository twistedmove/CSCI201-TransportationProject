package butter.usc.edu;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * Car class, so far only holding the essential values of a car as outlined in test.json
 * @author LorraineSposto
 *
 */
public class Car {
	private int id;
	private double speed;
	private String direction;
	private String ramp;
	private String freeway;
	private int freewayIndex;
	private int rampIndex;
	private int coordinateIndex;
	public Point point;
	public static final String EAST = "East";
	public static final String WEST = "West";
	public static final String NORTH = "North";
	public static final String SOUTH = "South";
	public static final int updatePerSec = 4; // 4 or 8?
	public static final double secPerHour = 360.00;

	public double milesPerTimeDiv;	// MPH
	private Boolean sloped = false;
	private double slopeX, slopeY;
	private int counter;
	private int divisions;
	private Boolean slopeDone = false;

	public Car() {

	}

	public Car(int id, double speed, String direction, String ramp, String freeway) {
		this.id = id;
		this.speed = speed;
		this.direction = direction;
		this.ramp = ramp;
		this.freeway = freeway;
		this.freewayIndex = getFreewayIndex(this);
		this.rampIndex = getRampIndex(this);
		this.point = RampBank.allRamps.get(freewayIndex).get(rampIndex).l.point;// PathBank.allLocations.get(freewayIndex).get(rampIndex).point;
		this.coordinateIndex = RampBank.allRamps.get(freewayIndex).get(rampIndex).indexOfCoordinate;
		milesPerTimeDiv = speed / secPerHour / updatePerSec;
		System.out.println("Car " + id + " with speed: " + speed + " miles per time div: " + milesPerTimeDiv);
		//System.out.println(RampBank.allRamps.get(freewayIndex).get(rampIndex).name + " index coordinate: " + RampBank.allRamps.get(freewayIndex).get(rampIndex).indexOfCoordinate);

		//System.out.println("lat, long: " + RampBank.allRamps.get(freewayIndex).get(rampIndex).l.getLatitude() + ", " + RampBank.allRamps.get(freewayIndex).get(rampIndex).l.getLongitude() + ". " + point);
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
			point.x += (int) (slopeX); 
			point.y += (int) (slopeY);
			if (counter >= (divisions - 1)) {
				sloped = false;
				slopeDone = true;
			}
			else {
				counter++;
			}
		}
		else {
			int pointsToMove = 0;
			if (direction.equals(NORTH) ){
				if (freewayIndex == 3 && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).isLast) { // If on the 405
					pointsToMove = 1;
					while ((milesPerTimeDiv - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove)) > .01 && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).isLast) {
						pointsToMove++;
						coordinateIndex++;
					}
					tempX = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).point.x - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.x;
					tempY = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).point.y - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.y;

					if ((Math.abs(tempX) > 1 || Math.abs(tempY) > 1 ) && PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove) >= 2*milesPerTimeDiv && !slopeDone) {
						counter = 1;
						double rawDiv = Math.floor(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove) / milesPerTimeDiv);
						divisions = (int)(Math.min(rawDiv, Math.max(Math.abs(tempX), Math.abs(tempY))));
						slopeX = tempX / (double)divisions;
						slopeY = tempY / (double)divisions;
						sloped = true;
					}
					else { // This includes slopeDone
						coordinateIndex++;
						increased = true;
					}
				}
			}
			else if (direction.equals(SOUTH)) { 
				if (freewayIndex == 3 && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).isFirst) { // If on the 405
					pointsToMove = -1;
					while ((milesPerTimeDiv - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove)) > .01 && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).isFirst) {
						pointsToMove--;
						coordinateIndex--;
					}
					tempX = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).point.x - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.x;
					tempY = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).point.y - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.y;
					if ((Math.abs(tempX) > 1 || Math.abs(tempY) > 1 ) && PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove) >= 2*milesPerTimeDiv && !slopeDone) {
						counter = 1;
						double rawDiv = Math.floor(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove) / milesPerTimeDiv);
						divisions = (int)(Math.min(rawDiv, Math.max(Math.abs(tempX), Math.abs(tempY))));
						slopeX = tempX / (double)divisions;
						slopeY = tempY / (double)divisions;
						sloped = true;
					}
					else {
						coordinateIndex--;
					}
				}
			}
			else if (direction.equals(EAST)) {
				if ((freewayIndex == 1) && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).isLast) { // If on the 101, 105, or 10
					pointsToMove = 1;
					while ((milesPerTimeDiv - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove)) > .01 && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).isLast) {
						pointsToMove++;
						coordinateIndex++;
					}
					tempX = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).point.x - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.x;
					tempY = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).point.y - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.y;
					if ((Math.abs(tempX) > 1 || Math.abs(tempY) > 1 ) && PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove) >= 2*milesPerTimeDiv && !slopeDone) {
						counter = 1;
						double rawDiv = Math.floor(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove) / milesPerTimeDiv);
						divisions = (int)(Math.min(rawDiv, Math.max(Math.abs(tempX), Math.abs(tempY))));
						slopeX = tempX / (double)divisions;
						slopeY = tempY / (double)divisions;
						sloped = true;
					}
					else {
						coordinateIndex++;
						increased = true;
					}
				}
				else if ((freewayIndex == 0 || freewayIndex == 2) && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).isFirst) {
					pointsToMove = -1;
					while (!PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).isFirst && (milesPerTimeDiv - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove)) > .01) {
						pointsToMove--;
						coordinateIndex--;
					}
					tempX = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).point.x - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.x;
					tempY = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).point.y - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.y;
					if ((Math.abs(tempX) > 1 || Math.abs(tempY) > 1 ) && PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove) >= 2*milesPerTimeDiv && !slopeDone) {
						counter = 1;
						double rawDiv = Math.floor(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove) / milesPerTimeDiv);
						divisions = (int)(Math.min(rawDiv, Math.max(Math.abs(tempX), Math.abs(tempY))));
						slopeX = tempX / (double)divisions;
						slopeY = tempY / (double)divisions;
						sloped = true;
					}
					else {
						coordinateIndex--;
					}
				}
			}
			else if (direction.equals(WEST)) { 			
				if ((freewayIndex == 1) && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).isFirst) { // If on the 105	
					pointsToMove = -1;
					while ((milesPerTimeDiv - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove)) > .01 && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).isFirst) {
						pointsToMove--;
						coordinateIndex--;
					}
					tempX = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).point.x - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.x;
					tempY = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).point.y - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.y;
					if ((Math.abs(tempX) > 1 || Math.abs(tempY) > 1 ) && PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove) >= 2*milesPerTimeDiv && !slopeDone) {
						counter = 1;
						double rawDiv = Math.floor(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove) / milesPerTimeDiv);
						divisions = (int)(Math.min(rawDiv, Math.max(Math.abs(tempX), Math.abs(tempY))));
						slopeX = tempX / (double)divisions;
						slopeY = tempY / (double)divisions;
						sloped = true;
					}
					else {
						coordinateIndex--;
					}
				}
				else if ((freewayIndex == 0 || freewayIndex == 2) && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).isLast) { // 101
					pointsToMove = 1;
					while ((milesPerTimeDiv - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove)) > .01 && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).isLast) {
						pointsToMove++;
						coordinateIndex++;
					}
					tempX = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).point.x - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.x;
					tempY = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).point.y - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.y;
					if ((Math.abs(tempX) > 1 || Math.abs(tempY) > 1 ) && PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove) >= 2*milesPerTimeDiv && !slopeDone) {
						counter = 1;
						double rawDiv = Math.floor(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove) / milesPerTimeDiv);
						divisions = (int)(Math.min(rawDiv, Math.max(Math.abs(tempX), Math.abs(tempY))));
						slopeX = tempX / (double)divisions;
						slopeY = tempY / (double)divisions;
						sloped = true;
					}
					else {
						coordinateIndex++;
						increased = true;
					}
				}
			}
			// Not going beyond the bounds of the path
			if (((increased && coordinateIndex < PathBank.allLocations.get(freewayIndex).size()) || (!increased && coordinateIndex >= 0)) && !sloped) {
				this.point = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point;
			}
			slopeDone = false;
		}
	}

	public boolean checkPoint(int xc, int yc){
		Rectangle2D r2d = null;
		if (direction.equals(WEST) || direction.equals(EAST)){
			r2d = new Rectangle2D.Double(point.getX(), point.getY(), 24, 12);
			if (r2d.contains(xc,yc)){
				return true;
			} else{
				return false;
			}
		} else if(direction.equals(NORTH) || direction.equals(SOUTH)){
			r2d = new Rectangle2D.Double(point.getX(), point.getY(), 12, 24);
			if (r2d.contains(xc,yc)){
				return true;
			} else{
				return false;
			}
		}
		return false;
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
