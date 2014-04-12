package butter.usc.edu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
	private final String CURRENT_TABLE = "current_traffic_data";
	private final String HISTORICAL_TABLE = "historical_traffic_data";
	
	private Connection connection = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	private final String SELECT_CURRENT_STMT = "SELECT * FROM " + CURRENT_TABLE; // TO SELECT ALL FROM CURRENT TABLE (1 ROW)
	private final String SELECT_HISTORICAL_STMT = "SELECT * FROM " + HISTORICAL_TABLE; // TO SELECT ALL FROM CURRENT TABLE (1 ROW)
	private final String INSERT_HISTORICAL_STMT = "INSERT INTO " + HISTORICAL_TABLE + " VALUES (?,?,?,?,?,?)"; // INSERT ID, SPEED, DIR, RAMP, FREEWAY, DATETIME
	
	private final String ID_LABEL = "id";
	private final String SPEED_LABEL = "speed";
	private final String DIRECTION_LABEL = "direction";
	private final String RAMP_LABEL = "ramp";
	private final String FREEWAY_LABEL = "freeway";
	private final String DATETIME_LABEL = "timestamp";
	
	private int id = 0;
	private double speed = 0;
	private String direction = null;
	private String ramp = null;
	private String freeway = null;
	private Timestamp datetime = null;
	
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
	 * Creates the current data table if it does not exist.
	 * @throws SQLException
	 */
	private void createCurrentTrafficTable() throws SQLException {
		try {
			String sql = "CREATE TABLE IF NOT EXISTS " + CURRENT_TABLE + " ("
					+ ID_LABEL + " INT NOT NULL,"
					+ SPEED_LABEL + " DOUBLE NOT NULL,"
					+ DIRECTION_LABEL + " VARCHAR(255),"
					+ RAMP_LABEL + " TEXT,"
					+ FREEWAY_LABEL + " INT NOT NULL,"
					+ DATETIME_LABEL + " TIMESTAMP"
					+ ")";
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			System.out.println("Created current data table (if not exists)!");
		} finally {
			if(statement != null) statement.close();
		}
	}
	
	/**
	 * Creates the table if it does not exist.
	 * @throws SQLException
	 */
	private void createHistoricalTrafficTable() throws SQLException {
		try {
			String sql = "CREATE TABLE IF NOT EXISTS " + HISTORICAL_TABLE + " ("
					+ ID_LABEL + " INT NOT NULL,"
					+ SPEED_LABEL + " DOUBLE NOT NULL,"
					+ DIRECTION_LABEL + " VARCHAR(255),"
					+ RAMP_LABEL + " TEXT,"
					+ FREEWAY_LABEL + " INT NOT NULL,"
					+ DATETIME_LABEL + " TIMESTAMP"
					+ ")";
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			System.out.println("Created historical table (if not exists)!");
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
			createCurrentTrafficTable();
			createHistoricalTrafficTable();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}
	
	/**
	 * Moves the data in the current data table to the historical table
	 */
	private void moveCurrentToHistorical() {
		try {
			preparedStatement = connection.prepareStatement(SELECT_CURRENT_STMT);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				extractData();
				
				preparedStatement = connection.prepareStatement(INSERT_HISTORICAL_STMT);
				preparedStatement.setInt(1, id);
				preparedStatement.setDouble(2, speed);
				preparedStatement.setString(3, direction);
				preparedStatement.setString(4, ramp);
				preparedStatement.setString(5, freeway);
				preparedStatement.setTimestamp(6, datetime);
				
				preparedStatement.executeUpdate();
			}
			System.out.println("Data moved to historical table");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Extracts data from result set.
	 * @throws SQLException 
	 */
	private void extractData() throws SQLException {
		id = resultSet.getInt(ID_LABEL);
		speed = resultSet.getDouble(SPEED_LABEL);
		direction = resultSet.getString(DIRECTION_LABEL);
		ramp = resultSet.getString(RAMP_LABEL);
		freeway = resultSet.getString(FREEWAY_LABEL);
		datetime = resultSet.getTimestamp(DATETIME_LABEL);
	}
	
	/**
	 * Whenever we get a new wave of cars:
	 * (1) Inserts the cars from current table into historical table 
	 * (2) Inserts new data into current table
	 * @param cars - a vector of cars
	 */
	public void addNewCarData(Vector<Car> cars) {
		try {
			String sql = "";
			moveCurrentToHistorical();
			for(Car c : cars) {
				sql = "INSERT INTO " + CURRENT_TABLE + " VALUES (" + c.insertString() + ",?)";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
				preparedStatement.executeUpdate();
//				System.out.println("Inserted a new car.");
			}
			System.out.println("Inserted new cars into current table.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void dropDatabase() {
//		Drop the table & database
		try {
			statement = connection.createStatement();
		    String dropString = "DROP TABLE " + CURRENT_TABLE;
			statement.executeUpdate(dropString);
			System.out.println("Dropped current table.");
			
		    dropString = "DROP TABLE " + HISTORICAL_TABLE;
			statement.executeUpdate(dropString);
			System.out.println("Dropped historical table.");
			
			dropString = "DROP DATABASE " + DATABASE;
			statement.executeUpdate(dropString);
		    System.out.println("Database deleted successfully...");
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not drop the table");
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * 
	 * @param filename
	 * @throws IOException 
	 */
	public void exportToCSV(String filename) throws IOException {
		FileWriter fw = new FileWriter(new File(filename));
		PrintWriter pw = new PrintWriter(fw);
		pw.println(ID_LABEL + "," + SPEED_LABEL + "," + DIRECTION_LABEL + "," + RAMP_LABEL + "," + FREEWAY_LABEL + "," + DATETIME_LABEL);
		try {
			preparedStatement = connection.prepareStatement(SELECT_HISTORICAL_STMT);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				extractData();
				ramp = escapeCommas(ramp);
				
				pw.println(id + "," + speed + "," + direction + ", "
						+ "" + ramp + ""
								+ "," + freeway
										+ "," + datetime + "\"");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
			fw.close();
		}
	}
	
	private String escapeCommas(String s) {
		int index = s.indexOf(", ");
//		System.out.println("NOT ESCAPED: " + s);
		if(index > 0) {
//			System.out.println("YES");
			s = s.replaceAll(",\\s+", " ");
		}
//		System.out.println("ESCAPED: " + s);
		return s;
	}
	
	public static void main(String[] args) {
		TrafficHistoryDatabase t = new TrafficHistoryDatabase();
		try {
			Vector<Car> cars = CarDeserializer.deserializeArrayFromURL("http://www-scf.usc.edu/~csci201/mahdi_project/test.json");
//			for(Car c : cars) {
//				System.out.println(c);
//			}
			t.run();
			t.addNewCarData(cars);
			t.addNewCarData(cars);
			t.addNewCarData(cars);
			t.exportToCSV("historical_data.csv");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			t.dropDatabase();
		}
	}
}
