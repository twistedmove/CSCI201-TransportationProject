package butter.usc.edu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Create the database if it does not exist. Use for inserting data into database.
 * Extends Thread.
 * Calls to server using CarDeserializer every so often to fetch data.
 * @author LorraineSposto
 *
 */
public class TrafficHistoryDatabase extends Thread {
	
	private final String USERNAME = "root";
	private final String PASSWORD = "pass";
	private String CONNECTION_URL = "jdbc:mysql://localhost:3306/";

	/** The name of the database we are testing with (this default is installed with MySQL) */
	private final String DATABASE = "traffic_history";
	
	/** The name of the table we are testing with */
	private final static String CURRENT_TABLE = "current_traffic_data";
	private final static String HISTORICAL_TABLE = "historical_traffic_data";
	
	private static Connection connection = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	
	private final String SELECT_HISTORICAL_STMT = "SELECT * FROM " + HISTORICAL_TABLE; // TO SELECT ALL FROM CURRENT TABLE (1 ROW)
	
	private final static String ID_LABEL = "id";
	private final static String SPEED_LABEL = "speed";
	private final static String DIRECTION_LABEL = "direction";
	private final static String RAMP_LABEL = "ramp";
	private final static String FREEWAY_LABEL = "freeway";
	private final String CALLS_LABEL = "serverCalls";
	
	private int id = 0;
	private static double speed = 0;
	private static String direction = null;
	private static String ramp = null;
	private static String freeway = null;
	private int serverCall = 0;
	
	int serverCalls;
	static DataPullThread dataPullThread;
	
	public static final String SERVER_URL = "http://www-scf.usc.edu/~csci201/mahdi_project/project_data.json";
//	public static final String SERVER_URL = "http://www-scf.usc.edu/~csci201/mahdi_project/test.json";
	
	class DataPullThread extends Thread {
		private Lock lock = new ReentrantLock();
		private Condition updateCondition = lock.newCondition(); 
		
		public void run() {
			try {
				while(true) {
					updateCurrentRamps();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		/**
		 * Updates the car ramps in current table.
		 * @throws SQLException 
		 * @throws InterruptedException 
		 */
		private void updateCurrentRamps() throws SQLException, InterruptedException {
			this.lock.lock();
			this.updateCondition.await();
//			System.out.println("&&&& Attempting to update RAMPS &&&&");
			boolean gotLock = false;
			while(!gotLock) {
				gotLock = ButterGUI.allCarsWrapper.getLock().tryLock();
//				System.out.println("&&&& Waiting for lock &&&&");
			}
//			System.out.println("&&&& Got lock: " + gotLock + " &&&&");
			try {
				for(int i=0; i < ButterGUI.allCarsWrapper.allCars.size(); ++i) {
					String sql = "UPDATE " + CURRENT_TABLE + " SET " + RAMP_LABEL 
							+ "='" + ButterGUI.allCarsWrapper.allCars.get(i).getRamp() 
							+ "' WHERE " + ID_LABEL + "=" + i;
					
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.execute();
				}
			} finally {
				ButterGUI.allCarsWrapper.getLock().unlock();
//				System.out.println("&&&& Releasing lock &&&&");
			}
			this.lock.unlock();
		}
		
		public Lock getDataPullLock() {
			return lock;
		}
		
		public Condition getDataPullCondition() {
			return updateCondition;
		}
	}
	
	public TrafficHistoryDatabase() throws SQLException {
		super();
		createDatabase();
		createCurrentTrafficTable();
		createHistoricalTrafficTable();
		serverCalls = 0;
		dataPullThread = new DataPullThread();
		dataPullThread.start();
	}
	
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
					+ CALLS_LABEL + " INT NOT NULL"
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
					+ CALLS_LABEL + " INT NOT NULL"
					+ ")";
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			System.out.println("Created historical table (if not exists)!");
		} finally {
			if(statement != null) statement.close();
		}
	}
	
	/** 
	 * Acquires lock on cars and gets data
	 * @throws IOException
	 */
	private void getData() throws IOException {
//		System.out.println("*** Attempting to get data ***");
		boolean gotLock = false;
		while(!gotLock) {
			gotLock = ButterGUI.allCarsWrapper.getLock().tryLock();
//			System.out.println("*** Waiting for lock ***");
		}
//		System.out.println("*** Got lock: " + gotLock + " ***");
		try {
//			System.out.println("CALLING SERVER - 10 SECS.");
			serverCalls++;
//			System.out.println("CALL " + serverCalls);
			ButterGUI.allCarsWrapper.allCars = CarDeserializer.deserializeArrayFromURL(SERVER_URL);
			addNewCarData();
		} finally {
			ButterGUI.allCarsWrapper.getLock().unlock();
//			System.out.println("** Releasing lock **");
		}
	}
	
	/**
	 * Connects to SQL server, creates database and table if necessary.
	 */
	public void run() {
		try {
			while(true) {
				getData();
//				System.out.println("*** SLEEPING ***");
				Thread.sleep(180000); // 3 minutes = 180000 ms
//				System.out.println("*** AWAKE ***");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			System.out.println("TrafficHistoryDatabase.run(): InterruptedException: " + e.getMessage());
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
		serverCall = resultSet.getInt(CALLS_LABEL);
	}
	
	/**
	 * Whenever we get a new wave of cars:
	 * (1) Inserts the cars from current table into historical table 
	 * (2) Inserts new data into current table
	 * @param cars - a vector of cars
	 */
	private void addNewCarData() {
		try {
			String sql = "DELETE FROM " + CURRENT_TABLE;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.execute();
//			moveCurrentToHistorical();
//			System.out.println("DELETED");
			for(Car c : ButterGUI.allCarsWrapper.allCars) {
				sql = "INSERT INTO " + CURRENT_TABLE + " VALUES (" + c.insertString() + "," + serverCalls + ")";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.executeUpdate();
				
				sql = "INSERT INTO " + HISTORICAL_TABLE + " VALUES (" + c.insertString() + "," + serverCalls + ")";
				preparedStatement = connection.prepareStatement(sql);
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
		pw.println(ID_LABEL + "," + SPEED_LABEL + "," + DIRECTION_LABEL + "," + RAMP_LABEL + "," + FREEWAY_LABEL + "," + CALLS_LABEL);
		try {
			preparedStatement = connection.prepareStatement(SELECT_HISTORICAL_STMT);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				extractData();
				ramp = escapeCommas(ramp);
				
				pw.println(id + "," + speed + "," + direction + ", "
						+ "" + ramp + ""
								+ "," + freeway
										+ "," + serverCall);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
			fw.close();
		}
	}
	
	/**
	 * Calculates the average speed of a freeway for path edges.
	 * @param freeway - int representing freeway name (110, 10, 105, 405)
	 * @param direction - String representing direction of travel; use strings in Car class
	 * @param start
	 * @param end
	 * @return
	 * @throws SQLException 
	 */
	public double getEdgeAverageTime(String direction, Ramp ramp) throws SQLException, Exception {
		double distance = 0;
		int s = 0;
		Ramp nextRamp = null;
		
		String sql = "SELECT " + SPEED_LABEL
				+ " FROM " + CURRENT_TABLE + " WHERE " 
					+ FREEWAY_LABEL + "=" + ramp.freeway + " AND " 
					+ DIRECTION_LABEL + "='" + direction + "' AND "
					+ RAMP_LABEL + "='" + ramp.name + "'";

		
		preparedStatement = connection.prepareStatement(sql);
		resultSet = preparedStatement.executeQuery();
		
		int num = 0;
		while(resultSet.next()) {
			s += resultSet.getDouble(SPEED_LABEL);
			++num;
		}
//		System.out.println("************************");
//		System.out.println("S: " + s);
//		System.out.println("NUM: " + num);
		
		if(num == 0) {
			return 0;
		}
		else {
			double avgSpeed = s/num;
//			System.out.println("AVGSPEED: " + avgSpeed);
		
			if(ramp.freeway == 101) {
				if(direction.equals(Car.NORTH)) {
					nextRamp = ramp.getNext();
				}
				else if(direction.equals(Car.SOUTH)) {
					nextRamp = ramp.getPrevious();
				}
				else {
					System.out.println("101 wrong direction!");
					throw new Exception("101 Wrong direction!");
				}
				
				if(nextRamp != null) {
//					System.out.println("\t\t" + ramp.name + "(" + ramp.indexOfCoordinate + ")"
//							+ " --> " + nextRamp.name + "(" + nextRamp.indexOfCoordinate + ")");
					if(nextRamp.indexOfCoordinate > ramp.indexOfCoordinate) {
						for(int i=ramp.indexOfCoordinate; i < nextRamp.indexOfCoordinate; ++i) {
							distance += PathBank.locations101.get(i).milesToNext;
						}
					}
					else {
						for(int i=nextRamp.indexOfCoordinate; i < ramp.indexOfCoordinate; ++i) {
							distance += PathBank.locations101.get(i).milesToNext;
						}
					}
				}
			}
			else if(ramp.freeway == 405) {
				if(direction.equals(Car.NORTH)) {
					nextRamp = ramp.getNext();
				}
				else if(direction.equals(Car.SOUTH)) {
					nextRamp = ramp.getPrevious();
				}
				else {
					System.out.println("405 wrong direction!");
					throw new Exception("405 Wrong direction!");
				}
				
				if(nextRamp != null) {
//					System.out.println("\t\t" + ramp.name + "(" + ramp.indexOfCoordinate + ")"
//							+ " --> " + nextRamp.name + "(" + nextRamp.indexOfCoordinate + ")");
					if(nextRamp.indexOfCoordinate > ramp.indexOfCoordinate) {
						for(int i=ramp.indexOfCoordinate; i < nextRamp.indexOfCoordinate; ++i) {
							distance += PathBank.locations405.get(i).milesToNext;
						}
					}
					else {
						for(int i=nextRamp.indexOfCoordinate; i < ramp.indexOfCoordinate; ++i) {
							distance += PathBank.locations405.get(i).milesToNext;
						}
					}
				}
			}
			else if(ramp.freeway == 10) {
				if(direction.equals(Car.EAST)) {
					nextRamp = ramp.getPrevious();
				}
				else if(direction.equals(Car.WEST)) {
					nextRamp = ramp.getNext();
				}
				else {
					System.out.println("10 wrong direction!");
					throw new Exception("10 Wrong direction!");
				}
				
				if(nextRamp != null) {
//					System.out.println("\t\t" + ramp.name + "(" + ramp.indexOfCoordinate + ")"
//							+ " --> " + nextRamp.name + "(" + nextRamp.indexOfCoordinate + ")");
					if(nextRamp.indexOfCoordinate > ramp.indexOfCoordinate) {
						for(int i=ramp.indexOfCoordinate; i < nextRamp.indexOfCoordinate; ++i) {
							distance += PathBank.locations10.get(i).milesToNext;
						}
					}
					else {
						for(int i=nextRamp.indexOfCoordinate; i < ramp.indexOfCoordinate; ++i) {
							distance += PathBank.locations10.get(i).milesToNext;
						}
					}
				}
			}
			else if(ramp.freeway == 105) {
				if(direction.equals(Car.EAST)) {
					nextRamp = ramp.getNext();
				}
				else if(direction.equals(Car.WEST)) {
					nextRamp = ramp.getPrevious();
				}
				else {
					System.out.println("105 wrong direction!");
					throw new Exception("105 Wrong direction!");
				}
				
				if(nextRamp != null) {
//					System.out.println("\t\t" + ramp.name + "(" + ramp.indexOfCoordinate + ")"
//							+ " --> " + nextRamp.name + "(" + nextRamp.indexOfCoordinate + ")");
					if(nextRamp.indexOfCoordinate > ramp.indexOfCoordinate) {
						for(int i=ramp.indexOfCoordinate; i < nextRamp.indexOfCoordinate; ++i) {
							distance += PathBank.locations105.get(i).milesToNext;
						}
					}
					else {
						for(int i=nextRamp.indexOfCoordinate; i < ramp.indexOfCoordinate; ++i) {
							distance += PathBank.locations105.get(i).milesToNext;
						}
					}
				}
			}
//			System.out.println("DISTANCE: " + distance);
			double time = distance/avgSpeed;
//			System.out.println("TIME: " + time);
//			System.out.println("************************");
			return time;
		}
	}
	
	private String escapeCommas(String s) {
		int index = s.indexOf(", ");
		if(index > 0) {
			s = s.replaceAll(",\\s+", " ");
		}
		return s;
	}


//
//	public static void main(String[] args) {
//		Vector<Car> cars = new Vector<Car>();
//		TrafficHistoryDatabase t;
//		try {
//			t = new TrafficHistoryDatabase();
//			cars = CarDeserializer.deserializeArrayFromURL(TrafficHistoryDatabase.SERVER_URL);
//			t.addNewCarData(cars);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
