import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

/**
 * Reads JSON stream from Google Directions API and extracts only the overview polyline.
 * In the future, if we need the entire data structure, we can create it, but the only information we need from this API is the 
 * polyline, so I've skipped the effort of mapping out the whole JSON structure into a Directions object.
 * @author LorraineSposto
 *
 */
public class JsonURLPolylineReader {
	public static String fetchJsonFromUrl(String urlString) throws IOException {
	    BufferedReader reader = null;
	    try {
	        URL url = new URL(urlString);
	        reader = new BufferedReader(new InputStreamReader(url.openStream()));
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
	
	/**
	 * It would be a lot of work to construct objects for the whole Directions API Json structure, so since we only need
	 * the overview_polyline (for now), this just searches the string of the JSON to extract the string of the polyline.
	 * @param jsonString
	 * @return
	 */
	public static String getPolylineObjectFromString(String jsonString) {
		/*
		 * Extracts the overview_polyline chunk of the JSON
		 */
//		System.out.println(jsonString);
		String key = "\"overview_polyline\" : ";
		if(jsonString.contains(key)) {
			int keyIndex = jsonString.indexOf(key);
			String polylineObject = jsonString.substring(keyIndex + key.length(), jsonString.indexOf(",", keyIndex));
//			System.out.println(polylineObject);
			return polylineObject;
		} 
		return null;
	}
	
	public static OverviewPolyline toOverviewPolyline(String polyline) {
		return new Gson().fromJson(polyline, OverviewPolyline.class);
	}
}
