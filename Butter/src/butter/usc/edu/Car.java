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
	private int pixToMove;
	private String direction;
	private String ramp;
	private String freeway;
	private int freewayIndex;
	private int rampIndex;
	public Point location;
	public static final String EAST = "East";
	public static final String WEST = "West";
	public static final String NORTH = "North";
	public static final String SOUTH = "South";
	public static final int pixelsPerMile = 57;
	public static final int updatePerSec = 4;
	public static final double secPerHour = 360.00;
	
	public Car() {
		
	}

	public Car(int id, double speed, String direction, String ramp, String freeway) {
		this.id = id;
		this.speed = speed;
		this.direction = direction;
		this.ramp = ramp;
		this.freeway = freeway;
		location = new Point(100,100);
		pixToMove = (int)((speed / secPerHour) * pixelsPerMile / updatePerSec);
		this.freewayIndex = getFreewayIndex(this);
		this.rampIndex = getRampIndex(this);
		//this.location = PathBank.allLocations.get(freewayIndex).get(rampIndex).
		
		//System.out.println("Starting position is: ");
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
	//	System.out.println("update");
		if (direction.equals(NORTH) ){
			location.y += pixToMove;
		}
		if (direction.equals(SOUTH)) {
			location.y -= pixToMove;
		}
		if (direction.equals(EAST)) {
			location.x += pixToMove;
		}
		if (direction.equals(WEST)) {
			location.x -= pixToMove;
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
