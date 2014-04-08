package butter.usc.edu;

/**
 * Car class, so far only holding the essential values of a car as outlined in test.json
 * @author LorraineSposto
 *
 */
public class Car {
	private int id;
	private double speed;
	private String direction;
	private String onOffRamp;
	private String freeway;
	
	public static final String EAST = "East";
	public static final String WEST = "West";
	public static final String NORTH = "North";
	public static final String SOUTH = "South";
	
	public Car(int id, double speed, String direction, String onOffRamp, String freeway) {
		this.id = id;
		this.speed = speed;
		this.direction = direction;
		this.onOffRamp = onOffRamp;
		this.freeway = freeway;
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
		return ("id: " + id + 
				"; speed: " + speed + 
				"; direction: " + direction + 
				"; on/off ramp: " + onOffRamp +
				"; freeway: " + freeway);
	}
}
