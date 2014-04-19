package butter.usc.edu;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

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
	private String freeway;
	private int freewayIndex;
	private int rampIndex;
	private int coordinateIndex;
	public Point point;
	private double radiansToRotate;
	public static final String EAST = "East";
	public static final String WEST = "West";
	public static final String NORTH = "North";
	public static final String SOUTH = "South";
	public static final int updatePerSec = 16; // 4 or 8?
	public static final double secPerHour = 360.00;

	public double milesPerTimeDiv;
	private Boolean sloped = false;
	private double slopeX, slopeY;
	private int counter;
	private int divisions;
	private Boolean slopeDone = false;
	private int numPointsToNextRamp;
	public Boolean increaseOnPath = false;
	public Boolean TextIsDisplayed = false;

	public Car(int id, double speed, String direction, String ramp, String freeway) {
		this.id = id;
		this.speed = speed;
		this.direction = direction;
		this.rampName = ramp;
		this.freeway = freeway;
		this.freewayIndex = getFreewayIndex(this);
		this.rampIndex = getRampIndex(this);
//		System.out.println("Rampindex: " + RampBank.allRamps.get(freewayIndex).get(rampIndex).l);
		this.point = RampBank.allRamps.get(freewayIndex).get(rampIndex).l.point;
		this.coordinateIndex = RampBank.allRamps.get(freewayIndex).get(rampIndex).indexOfCoordinate;
		milesPerTimeDiv = speed / secPerHour / updatePerSec;
		findIfIncrease();
		updatePointsToNextRamp();
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
//		for(Vector<Ramp> vr : RampBank.allRamps) {
//			for(Ramp r : vr) {
//				if(c.rampName.equals(r.name)) {
//					
//				}
//			}
//		}
		
		for (int i = 0; i < RampBank.rampNames[c.freewayIndex].length; i++) {
			if (c.rampName.equals(RampBank.rampNames[c.freewayIndex][i])) {
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
				while (!PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove).isLast && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).getLocationAway(pointsToMove + 1).isLast && (milesPerTimeDiv - Math.abs(PathBank.allLocations.get(freewayIndex).get(coordinateIndex).distToPointAway(pointsToMove))) > .01) {
					pointsToMove++;
					coordinateIndex++;
					numPointsToNextRamp--;
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
				}
				else {
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
					coordinateIndex--;
				}
			}
			if (((increased && coordinateIndex < PathBank.allLocations.get(freewayIndex).size()) || (!increased && coordinateIndex >= 0)) && !sloped && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).isFirst && !PathBank.allLocations.get(freewayIndex).get(coordinateIndex).isLast) {
				this.point = PathBank.allLocations.get(freewayIndex).get(coordinateIndex).point;
			}
			if (numPointsToNextRamp <= 0) {
				if (increaseOnPath) {
					if (rampIndex < RampBank.allIndices[freewayIndex].length - 1) {
						rampIndex++;
						rampName = RampBank.allRamps.get(freewayIndex).get(rampIndex).name;
						updatePointsToNextRamp();
						if (this.TextIsDisplayed) {
							TrafficHistoryDatabase.dataPullThread.getDataPullLock().lock();
							if (!ButterGUI.noCar){
								ButterGUI.updateTextCarInfo(this);
							}
							TrafficHistoryDatabase.dataPullThread.getDataPullCondition().signal();
							TrafficHistoryDatabase.dataPullThread.getDataPullLock().unlock();
						}
					}
					else {
						numPointsToNextRamp = 0;
					}
				}
				else {
					if (rampIndex > 0) {
						rampIndex--;
						rampName = RampBank.allRamps.get(freewayIndex).get(rampIndex).name;
						updatePointsToNextRamp();
						if (this.TextIsDisplayed) {
							TrafficHistoryDatabase.dataPullThread.getDataPullLock().lock();
							if (!ButterGUI.noCar){
								ButterGUI.updateTextCarInfo(this);
							}
							TrafficHistoryDatabase.dataPullThread.getDataPullCondition().signal();
							TrafficHistoryDatabase.dataPullThread.getDataPullLock().unlock();
						}
					}
					else {
						numPointsToNextRamp = 0;
					}
				}
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
		if (increaseOnPath) {
			if (rampIndex < RampBank.allIndices[freewayIndex].length - 1) {
				numPointsToNextRamp = RampBank.allIndices[freewayIndex][rampIndex + 1] - RampBank.allIndices[freewayIndex][rampIndex];
			}
		}
		else {
			if (rampIndex > 0) {
				numPointsToNextRamp = RampBank.allIndices[freewayIndex][rampIndex] - RampBank.allIndices[freewayIndex][rampIndex-1];
			}
		}
	}
	
	public double getRadiansToRotate(){
		Point aim = null;
		if (increaseOnPath) {
			if (rampIndex < RampBank.allIndices[freewayIndex].length - 1) {
				aim = RampBank.allRamps.get(freewayIndex).get(rampIndex + 1).l.point;
			}
		}
		else {
			if (rampIndex > 0) {
				aim = RampBank.allRamps.get(freewayIndex).get(rampIndex - 1).l.point;
			}
		}
		if (aim != null) {
			radiansToRotate = Math.atan2(aim.y - point.y, aim.x - point.x);
		}
		
		if (radiansToRotate < 0) {
			radiansToRotate += 2*Math.PI;
		}
		//System.out.println("Rads: " + radiansToRotate + " " + point + " " + aim);
		//System.out.println("Degrees: " + Math.toDegrees(radiansToRotate));

		return radiansToRotate;
	}
	public void startRadian() {
		if (direction.equals(WEST)) {
			radiansToRotate = Math.PI;
		}
		else if (direction.equals(EAST)) {
			radiansToRotate = 0;
		}
		else if (direction.equals(NORTH)) {
			radiansToRotate = Math.PI / 2.0;
		}
		if (direction.equals(SOUTH)) {
			radiansToRotate = Math.PI * (4.0/3.0);
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
