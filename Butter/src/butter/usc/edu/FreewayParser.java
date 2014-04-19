package butter.usc.edu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 * Thrown when there is an error in the text files containing the static game data
 * @author LorraineSposto
 *
 */
class TxtFormatException extends Exception {
	private static final long serialVersionUID = 1L;

	public TxtFormatException(String message) {
		super(message);
	}
}

/**
 * Reads in coordinates from CSV and links the coordinates.
 * @author LorraineSposto
 *
 */
public class FreewayParser {
	
	static Vector<Location> parseFreeway(File filename, int freeway) throws TxtFormatException {
		try {
			Vector<Location> coordinates = new Vector<Location>();
			
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String[] fields = br.readLine().split("[,]");
			
//			for(int i=0; i < fields.length; ++i) System.out.println(fields[i]);
			
			String line = br.readLine();
			double lat=0, lon=0; int branch =0 ;
			while(line != null) {
				fields = line.split("[,]");
				lat = new Double(fields[0]).doubleValue();
				lon = new Double(fields[1]).doubleValue();
				branch = new Integer(fields[2]).intValue();
				
				coordinates.add(new Location(lat, lon, freeway, branch));
				line = br.readLine();
			}
			br.close();
			/* Linking spaces */
			for(int i=1; i < coordinates.size(); ++i) {
				if(i == 0) {
					coordinates.get(i).setPrev(coordinates.get(coordinates.size()-1));
				}
				else if(i == coordinates.size()-1) {
					coordinates.get(i).setPrev(coordinates.get(i-1));
				}
				else {
					coordinates.get(i).setPrev(coordinates.get(i-1));
				}
			}
			return coordinates;
		} catch (NumberFormatException e) {
//			e.printStackTrace();
			throw new TxtFormatException("parseSpaces");
		} catch(IOException e) {
			throw new TxtFormatException("parseSpaces");
//			e.printStackTrace();
		}	
	}
}
