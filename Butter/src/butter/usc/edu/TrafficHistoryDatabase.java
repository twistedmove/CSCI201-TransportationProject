package butter.usc.edu;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Vector;

/**
 * Create the database if it does not exist. Use for inserting data into database.
 * @author LorraineSposto
 *
 */
public class TrafficHistoryDatabase {
	
	private final String USERNAME = "root";
	private final String PASSWORD = "pass";
	private String CONNECTION_URL = "jdbc:mysql://localhost:3306/";

	/** The name of the database we are testing with (this default is installed with MySQL) */
	private final String DATABASE = "traffic_history";
	
	/** The name of the table we are testing with */
	private final String TABLE = "traffic";
	
	private Connection connection = null;
	private Statement statement = null;
//	private ResultSet resultset = null;
	
	/**
	 * Tries to establish connection with the database. If it cannot (the database doesn't exist), it connects to the host.
	 * @return True if the connection to the database was successful, false if not.
	 * @throws ClassNotFoundException
	 * @throws SQLException 
	 */
	private boolean getConnection() throws SQLException {
		try {
			connection = DriverManager.getConnection(CONNECTION_URL + DATABASE, USERNAME, PASSWORD);
			System.out.println("Connection to database acquired.");
			return true;
		} catch (SQLException e) {
			connection = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD);
			System.out.println("Connection to database not acquired. Connected to host.");
			return false;
		}
	}
	
	/**
	 * If connection to the database cannot be established, then it connects to the host, creates the database, and then uses said database.
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private void createDatabase() throws SQLException {
		try {
			if(!getConnection()) {
				// creates the database if connection failed, eg did not exists
				statement = connection.createStatement();
				statement.executeUpdate("CREATE DATABASE " + DATABASE);
				// uh now connecting to the database fur real
				connection = DriverManager.getConnection(CONNECTION_URL + DATABASE, USERNAME, PASSWORD);
				System.out.println("Database created! Connected to database!");
			}
		} catch (SQLException e) { //just in case
			if(e.getErrorCode() == 1007) {
				System.out.println("Liars it said database didn't exist.");
			} else {
				System.out.println("SQLException...");
				e.printStackTrace();
			}
		} finally {
			if (statement != null) statement.close();
		}
	}
	
	/**
	 * Creates the table if it does not exist.
	 * @throws SQLException
	 */
	private void createTrafficTable() throws SQLException {
		try {
			String sql = "CREATE TABLE IF NOT EXISTS " + TABLE + " ("
					+ "id INT NOT NULL,"
					+ "speed DOUBLE NOT NULL,"
					+ "direction VARCHAR(255),"
					+ "on_off_ramp TEXT,"
					+ "freeway INT NOT NULL"
					+ ")";
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			System.out.println("Created table (if not exists)!");
		} finally {
			if(statement != null) statement.close();
		}
		
	}
	
	/**
	 * Connects to SQL server, creates database and table if necessary.
	 */
	public void run() {
		try {
			createDatabase();
			createTrafficTable();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
		
//		Drop the table & database
//		try {
//			statement = connection.createStatement();
//		    String dropString = "DROP TABLE " + TABLE;
//			statement.executeUpdate(dropString);
//			System.out.println("Dropped the table.");
//			
//			dropString = "DROP DATABASE " + DATABASE;
//			statement.executeUpdate(dropString);
//		    System.out.println("Database deleted successfully...");
//	    } catch (SQLException e) {
//			System.out.println("ERROR: Could not drop the table");
//			e.printStackTrace();
//			return;
//		}
	}
	
	/**
	 * Whenever we get a new wave of cars, insert the cars into the traffic table.
	 * @param cars - a vector of cars
	 */
	public void insertCarsIntoTable(Vector<Car> cars) {
		try {
			String insertString = "";
			for(Car c : cars) {
				insertString = "INSERT INTO " + TABLE + " VALUES (" + c.insertString() + ")";
				statement = connection.createStatement();
				statement.executeUpdate(insertString);
				System.out.println("Inserted a car.");
			}
			System.out.println("Inserted all cars into table.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param filename
	 */
	public void exportToCSV(String filename) {
		
	}
	
	public static void main(String[] args) {
		
		try {
			Vector<Car> cars = CarDeserializer.deserializeArrayFromURL("http://www-scf.usc.edu/~csci201/mahdi_project/test.json");
			for(Car c : cars) {
				System.out.println(c);
			}
//			TrafficHistoryDatabase t = new TrafficHistoryDatabase();
//			t.run();
//			t.insertCarsIntoTable(cars);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
