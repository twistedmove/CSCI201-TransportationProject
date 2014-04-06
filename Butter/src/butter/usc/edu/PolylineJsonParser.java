import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

// For reference:
// http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/stream/JsonReader.html

/**
 * Contains a parser to parse a JSON returned by Google Directions API to extract the polyline of a journey.
 * @author LorraineSposto
 *
 */
class PolylineJsonParser {
	public static String getPolylineString(String filename) {
		String pl = "";
		FileInputStream fileIn;
		try {
			fileIn = new FileInputStream(new File("directions.JSON"));
			JsonReader reader = new JsonReader(new InputStreamReader(fileIn, "UTF-8"));
			
			Vector<String> polylines = new Vector<String>();

			reader.beginObject();
			reader.nextName();
			reader.beginArray();
			reader.beginObject();
			while(reader.hasNext()) {
				while(reader.hasNext() && !reader.peek().equals(JsonToken.NAME)) {
					reader.skipValue();
				}
				if(reader.hasNext() && reader.peek().equals(JsonToken.NAME) && reader.nextName().equals("overview_polyline")) {
					System.out.println("polyline");
					reader.beginObject();
					reader.nextName();
					pl = reader.nextString();
					reader.endObject();
				}
				if(reader.peek().equals(JsonToken.END_ARRAY)) {
					reader.endArray();
				} else if(reader.peek().equals(JsonToken.END_OBJECT)) {
					reader.endObject();
				}
			}
			System.out.println(pl);
			reader.close();
			return pl;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	public static void main(String[] args) {
		String urlString = "https://maps.googleapis.com/maps/api/directions/json?origin=Burbank,CA&destination=Los+Angeles,CA&sensor=false&key=AIzaSyDY1BXusaVvEci4wLy4Xkw4LBJqOpIpxEo";
		try {
			String jsonString = JsonURLPolylineReader.fetchJsonFromUrl(urlString);
			String polyline = JsonURLPolylineReader.getPolylineObjectFromString(jsonString);
			OverviewPolyline op = JsonURLPolylineReader.toOverviewPolyline(polyline);
			System.out.println("POLYLINE: ");
			System.out.println(op);
			System.out.println("---------");
			System.out.print("COORDINATES: ");
			Vector<Location> polylineCoordinates = PolylineDecoder.decodePoly(op.toString());
			System.out.println(polylineCoordinates.size());
			for(int i=0; i < polylineCoordinates.size(); ++i) {
				System.out.println(polylineCoordinates.get(i));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}

}