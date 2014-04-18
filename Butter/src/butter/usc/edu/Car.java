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
	private String rampName;
	//private Ramp recentRamp;
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
	private int numPointsToNextRamp;
	public Boolean increaseOnPath = false;


	//	public Car() {
	//
	//	}

	public Car(int id, double speed, String direction, String ramp, String freeway) {
		this.id = id;
		this.speed = speed;
		this.direction = direction;
		this.rampName = ramp;
		this.freeway = freeway;
		this.freewayIndex = getFreewayIndex(this);
		this.rampIndex = getRampIndex(this);
		//recentRamp = RampBank.allRamps.get(freewayIndex).get(rampIndex);
		this.point = RampBank.allRamps.get(freewayIndex).get(rampIndex).l.point;// PathBank.allLocations.get(freewayIndex).get(rampIndex).point;
		this.coordinateIndex = RampBank.allRamps.get(freewayIndex).get(rampIndex).indexOfCoordinate;
		milesPerTimeDiv = speed / secPerHour / updatePerSec;
		findIfIncrease();
		System.out.println("Car " + id + " with speed: " + speed + " miles per time div: " + milesPerTimeDiv);
		System.out.println("Ramp index: " + rampIndex);
		updatePointsToNextRamp();
		System.out.println(RampBank.allRamps.get(freewayIndex).get(rampIndex).name + " index coordinate: " + RampBank.allRamps.get(freewayIndex).get(rampIndex).indexOfCoordinate);

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
			if (c.rampName.equals(RampBank.rampNames[c.freewayIndex][i])) {
				System.out.println("Ramp index: " + i);
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
			if (increaseOnPath && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).isLast){
				pointsToMove = 1;
				numPointsToNextRamp--;
				System.out.println("Car " + id + " direction " + direction);
				while (!PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).isLast && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove + 1).isLast && (milesPerTimeDiv - Math.abs(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove))) > .01) {
//					if (PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove+1).isLast) {
//						break;
//					}
//					else {
						pointsToMove++;
						coordinateIndex++;
						numPointsToNextRamp--;
					//}
				}
				tempX = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).point.x - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.x;
				tempY = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).point.y - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.y;

				if ((Math.abs(tempX) >= 1 || Math.abs(tempY) >= 1 ) && PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove) >= milesPerTimeDiv && !slopeDone) {
					counter = 1;
					double rawDiv = Math.floor(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove) / milesPerTimeDiv);
					divisions = (int)(rawDiv);
					slopeX = tempX / (double)divisions;
					slopeY = tempY / (double)divisions;
					sloped = true;
					System.out.println("Dividing into " + divisions);
				}
				else { // This includes slopeDone
					coordinateIndex++;
					increased = true;
				}
			}
			else if (!increaseOnPath && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).isFirst) { 
				pointsToMove = -1;
				numPointsToNextRamp--;
				while (!PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).isFirst && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove - 1).isFirst && (milesPerTimeDiv - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove)) > .01) {
						pointsToMove--;
						coordinateIndex--;
						numPointsToNextRamp--;
				}
				tempX = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).point.x - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.x;
				tempY = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).point.y - PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point.y;
				if ((Math.abs(tempX) >= 1 || Math.abs(tempY) >= 1 ) && PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove) >= 2*milesPerTimeDiv && !slopeDone) {
					counter = 1;
					double rawDiv = Math.floor(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove) / milesPerTimeDiv);
					divisions = (int)(rawDiv);
					slopeX = tempX / (double)divisions;
					slopeY = tempY / (double)divisions;
					sloped = true;
				}
				else {
					System.out.println("here5");
					coordinateIndex--;
				}

			}
			// Not going beyond the bounds of the path
			if (((increased && coordinateIndex < PathBank.allLocations.get(freewayIndex).size()) || (!increased && coordinateIndex >= 0)) && !sloped && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).isFirst && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).isLast) {
				System.out.println("here6");
				this.point = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point;
			}
			if (numPointsToNextRamp == 0) {
				System.out.println("here7");
				// fix the freeway index, 
				this.rampIndex++;
				this.rampName = RampBank.allRamps.get(freewayIndex).get(rampIndex).name;
				System.out.println("Updated to ramp: " + rampName);
				updatePointsToNextRamp();
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

	public void findIfIncrease() {
		if ((freewayIndex == 2 && direction.equals(WEST)) || (freewayIndex == 1 && direction.equals(EAST)) ||  ((freewayIndex == 0 || freewayIndex == 3)  && direction.equals(NORTH))) {
			increaseOnPath = true;
		}
		else {
			increaseOnPath = false;
		}
	}

	public void updatePointsToNextRamp() {
		System.out.println("Car " + id + " Ramp index: " + rampIndex);
		/*
		if (increaseOnPath) {
			System.out.println("ramp index: " + rampIndex);
			System.out.println("Total ramps: " + RampBank.allRamps.get(freewayIndex).size());
			System.out.println(RampBank.allRamps.get(freewayIndex).get(rampIndex).name);
			numPointsToNextRamp = RampBank.allIndices[freewayIndex][rampIndex+1] - this.rampIndex;
			//numPointsToNextRamp = RampBank.allRamps.get(freewayIndex).get(rampIndex + 1).indexOfCoordinate - this.rampIndex;
		}
		else {
			numPointsToNextRamp = RampBank.allRamps.get(freewayIndex).get(rampIndex - 1).indexOfCoordinate - this.rampIndex;
		}
		 */
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
		return rampName;
	}

	public void setRamp(String ramp) {
		this.rampName = ramp;
	}

	public String getFreeway() {
		return freeway;
	}

	public void setFreeway(String freeway) {
		this.freeway = freeway;
	}

	public String toString() {
		return (id + "," + speed + "," + direction + "," + rampName + "," + freeway);
	}

	public String insertString() {
		return (id + "," + speed + ",'" + direction + "','" + rampName + "'," + freeway);
	}
}
