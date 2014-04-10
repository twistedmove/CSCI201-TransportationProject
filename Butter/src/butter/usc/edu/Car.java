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
	private String onOffRamp;
	private String freeway;
	public Point location;
	public static final String EAST = "East";
	public static final String WEST = "West";
	public static final String NORTH = "North";
	public static final String SOUTH = "South";
	public static final int pixelsPerMile = 57;
	public static final int updatePerSec = 4;
	public static final double secPerHour = 360.00;
	
	public Car(int id, double speed, String direction, String onOffRamp, String freeway) {
		this.id = id;
		this.speed = speed;
		this.direction = direction;
		this.onOffRamp = onOffRamp;
		this.freeway = freeway;
		location = new Point(100,100);
		pixToMove = (int)((speed / secPerHour) * pixelsPerMile / updatePerSec);
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

	public String getOnOffRamp() {
		return onOffRamp;
	}

	public void setOnOffRamp(String onOffRamp) {
		this.onOffRamp = onOffRamp;
	}

	public String getFreeway() {
		return freeway;
	}

	public void setFreeway(String freeway) {
		this.freeway = freeway;
	}
	
	public String toString() {
		return (id + "," + speed + "," + direction + "," + onOffRamp + "," + freeway);
	}
	
	public String insertString() {
		return (id + "," + speed + ",'" + direction + "','" + onOffRamp + "'," + freeway);
	}
}
