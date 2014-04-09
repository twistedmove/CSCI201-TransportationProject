package butter.usc.edu;

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
 * Contains a parser to parse a JSON returned by Google Directions API to extract the polyline string of a journey.
 * @author LorraineSposto
 *
 */

class PolylineJsonParser {
	
	/**
	 * I guess this gets the polyline from the Directions JSON as a file, but I don't use it anywhere so I don't remember if it works.
	 * For now use JsonURLPolylineReader to read directly from the call to the Directions API (using a URL).
	 * I think this was just an excessive method of doing it.
	 * @param filename
	 * @return
	 * @throws IOException
	 */

	public static String getPolylineString(String filename) throws IOException {
		String polyline = "";
		FileInputStream fileIn;

		fileIn = new FileInputStream(new File("directions.JSON"));
		JsonReader reader = new JsonReader(new InputStreamReader(fileIn, "UTF-8"));

		reader.beginObject();
		reader.nextName();
		reader.beginArray();
		reader.beginObject();
		while(reader.hasNext()) {
			while(reader.hasNext() && !reader.peek().equals(JsonToken.NAME)) {
				reader.skipValue();
			}
			if(reader.hasNext() && reader.peek().equals(JsonToken.NAME) && reader.nextName().equals("overview_polyline")) {
				
				reader.beginObject();
				reader.nextName();
				polyline = reader.nextString();
				reader.endObject();
			}
			if(reader.peek().equals(JsonToken.END_ARRAY)) {
				reader.endArray();
			} else if(reader.peek().equals(JsonToken.END_OBJECT)) {
				reader.endObject();
			}
		}
		System.out.println("The polyline is: " + polyline);
		reader.close();
		return polyline;

	}

}
