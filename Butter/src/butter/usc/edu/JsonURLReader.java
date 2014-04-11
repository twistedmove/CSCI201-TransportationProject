package butter.usc.edu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;
//import java.io.FileWriter;
//import java.io.Reader;
//import java.io.InputStream;
//import java.net.MalformedURLException;
//import java.text.ParseException;
//import java.util.Vector;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonSyntaxException;
//import com.google.gson.stream.JsonReader;
//import com.google.gson.stream.JsonWriter;


/**
 * Reads JSON stream from Google Directions API and extracts only the overview polyline. 
 * EDIT: polyline extraction moved to static methods in OverviewPolyline class.
 * In the future, if we need the entire data structure, we can create it, but the only information we need from this API is the 
 * polyline, so I've skipped the effort of mapping out the whole JSON structure into a Directions object.
 * @author LorraineSposto
 *
 */

public class JsonURLReader {
	
	/**
	 * Gets the entire JSON from the Google Directions API as a String.
	 * @param urlString - URL of the JSON from Directions API
	 * @return The entire Directions journey as a String
	 * @throws IOException
	 */

	public static String urlToString(String urlString) throws IOException {
	    BufferedReader reader = null;
	    try {
	        URL url = new URL(urlString);
	        URLConnection urlConnect = url.openConnection();
	        reader = new BufferedReader(new InputStreamReader(urlConnect.getInputStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 

	        return buffer.toString();
	    } finally {
	        if (reader != null)
	            reader.close();
	    }
	}

//
//	public static void main(String[] args) {
//		String urlString = "https://maps.googleapis.com/maps/api/directions/json?origin=34.027485,-118.210868&destination=34.159475,-118.637349&sensor=false&key=AIzaSyDY1BXusaVvEci4wLy4Xkw4LBJqOpIpxEo";
//		try {
//			String jsonString = JsonURLReader.urlToString(urlString);
//			String polyline = OverviewPolyline.getPolylineObjectFromString(jsonString);
//			OverviewPolyline op = OverviewPolyline.toOverviewPolyline(polyline);
//			
//			System.out.println("POLYLINE: ");
//			System.out.println(op);
//			System.out.println("---------");
//			
//			System.out.print("COORDINATES: ");
//			Vector<Location> polylineCoordinates = PolylineDecoder.decodePoly(op.toString());
//			System.out.println(polylineCoordinates.size());
//			
//			FileWriter fw = new FileWriter(new File("101-path.csv"));
//			PrintWriter pw = new PrintWriter(fw);
//			
//			try {
//				pw.println("latitude,longitude");
//
//				for(Location loc : polylineCoordinates) {
//					pw.println(loc);
//					System.out.println(loc);
//				}
//			} finally {
//				pw.flush();
//				pw.close();
//				fw.close();
//			}
//			
////			for(int i=0; i < polylineCoordinates.size(); ++i) {
////				System.out.println(polylineCoordinates.get(i));
////			}
//		} catch (IOException e) {
////			 TODO Auto-generated catch block
//			System.out.println(e.getMessage());
//		}
//	}

}

