package butter.usc.edu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 * Thrown when there is an error in the text files containing the static game data
 */
class TxtFormatException extends Exception {
	private static final long serialVersionUID = 1L;

	public TxtFormatException(String message) {
		super(message);
	}
}

/**
 * Reads in coordinates from CSV and links the coordinates.
 */
public class FreewayParser {
	
	static Vector<Ramp> parseRamps(File filename, int freeway) throws TxtFormatException {
		try {
			Vector<Ramp> ramps = new Vector<Ramp>();
			
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String[] fields = br.readLine().split("[,]");
			
			String rampName = "", branchName = "";
			int index = 0;
			String line = br.readLine();
			while(line != null) {
				for(int i=0; i < fields.length; ++i) fields[i] = "";
				fields = line.split("[;]");
				rampName = fields[0];
				index = new Integer(fields[1]).intValue();
				branchName = fields[2];
				
				ramps.add(new Ramp(rampName, branchName, index, freeway));
				line = br.readLine();
			}
			br.close();
			/** 
			 * Linking spaces 
			 */
			for(int i=1; i < ramps.size(); ++i) {
				ramps.get(i).setPreviousRamp(ramps.get(i-1));
			}
			return ramps;
		} catch (NumberFormatException e) {
			throw new TxtFormatException("NFE: Error parsing ramps.");
		} catch(IOException e) {
			throw new TxtFormatException("IOE: Error parsing ramps.");
		}	
	}
	
	static Vector<Location> parseFreeway(File filename, int freeway) throws TxtFormatException {
		try {
			Vector<Location> coordinates = new Vector<Location>();
			
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String[] fields = br.readLine().split("[,]");
			
			String line = br.readLine();
			double lat=0, lon=0; int branch =0 ;
			while(line != null) {
				for(int i=0; i < fields.length; ++i) fields[i] = "";
				fields = line.split("[,]");
				lat = new Double(fields[0]).doubleValue();
				lon = new Double(fields[1]).doubleValue();
				branch = new Integer(fields[2]).intValue();
				
				coordinates.add(new Location(lat, lon, freeway, branch));
				line = br.readLine();
			}
			br.close();
			/** 
			 * Linking spaces 
			 */
			for(int i=1; i < coordinates.size(); ++i) {
				coordinates.get(i).setPrev(coordinates.get(i-1));
			}
			return coordinates;
		} catch (NumberFormatException e) {
			throw new TxtFormatException("Error parsing freeways.");
		} catch(IOException e) {
			throw new TxtFormatException("Error parsing freeways.");
		}	
	}
}
