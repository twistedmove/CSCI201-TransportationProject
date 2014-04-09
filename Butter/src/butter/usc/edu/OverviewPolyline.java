package butter.usc.edu;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Holds the polyline string extracted from Google Directions API
 * @author LorraineSposto
 *
 */
public class OverviewPolyline {
	String points;
	
	public OverviewPolyline(String points) {
		this.points = points;
	}
	
	public String toString() {
		return points;
	}
	
	/**
	 * Takes a string containing entire Directions JSON and extracts the points object from the JSON.
	 * {points: "..." }
	 * It would be a lot of work to construct objects for the whole Directions API Json structure, so since we only need
	 * the overview_polyline (for now), this just searches the string of the JSON to extract the string of the polyline.
	 * @param jsonString
	 * @return
	 */

	public static String getPolylineObjectFromString(String jsonString) {
		/*
		 * Extracts the overview_polyline chunk of the JSON
		 */
		String key = "\"overview_polyline\" : ";
		if(jsonString.contains(key)) {
			int keyIndex = jsonString.indexOf(key);
			String polylineObject = jsonString.substring(keyIndex + key.length(), jsonString.indexOf(",", keyIndex));
			return polylineObject;
		} 
		return null;
	}
	
	/**
	 * Utilizes Gson to just take the overview_polyline object from the JSON and deserialize it into the OverviewPolyline class.
	 * @param polyline - The string of the JSONObject points obtained from getPolylineObjectFromString(String) method, places into an OverviewPolyline object
	 * @return An OverviewPolyline object
	 * @throws JsonSyntaxException
	 */

	public static OverviewPolyline toOverviewPolyline(String polyline) throws JsonSyntaxException {
		return new Gson().fromJson(polyline, OverviewPolyline.class);
	}
}
