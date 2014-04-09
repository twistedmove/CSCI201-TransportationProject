package butter.usc.edu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

/**
 * Deserializes a JSONArray of car objects.
 * Contains two static methods: deserializeArrayFromFile(String filename), deserializeArrayFromURL(String url). The latter is probably
 * more practical for our purposes, the former was mostly for testing.
 * @author LorraineSposto
 *
 */

public class CarDeserializer {

	/**
	 * Reads a JSON file containing an array of cars and deserializes it into a Vector.
	 * Currently the on/off ramps don't work and just produce null, maybe because of the question mark thing?
	 * @param filename - The filename of the JSON file
	 * @return A vector of Cars
	 * @throws IOException
	 */

	public static Vector<Car> deserializeArrayFromFile(String filename) throws IOException {
		JsonReader reader = null;
		try {
			Vector<Car> cars = new Vector<Car>();
			Gson gson = new Gson();
	
			FileInputStream in = new FileInputStream(new File(filename));
			reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
	
			Type collectionType = new TypeToken<Vector<Car>>(){}.getType();
			cars = gson.fromJson(reader, collectionType);
			return cars;
		} finally {
			if (reader != null) reader.close();
		}
		
	}
	
	/**
	 * Reads a JSON from a URL stream containing an array of cars and deserializes it into a Vector.
	 * Currently the on/off ramps don't work and just produce null, maybe because of the question mark thing?
	 * @param urlString - The url of the JSON 
	 * @return A vector of Cars
	 * @throws IOException
	 */
	
	public static Vector<Car> deserializeArrayFromURL(String urlString) throws IOException {
		JsonReader reader = null;
		try {
			Vector<Car> cars = new Vector<Car>();
			Gson gson = new Gson();
	
	        URL url = new URL(urlString);
	        reader = new JsonReader(new InputStreamReader(url.openStream(), "UTF-8"));
	
			Type collectionType = new TypeToken<Vector<Car>>(){}.getType();
			cars = gson.fromJson(reader, collectionType);
			return cars;
		} finally {
	        if (reader != null)
	            reader.close();
	    }
	}
	
//	public static void main(String[] args) {
//		try {
////			Vector<Car> cars = CarDeserializer.deserializeArrayFromURL("http://www-scf.usc.edu/~csci201/mahdi_project/test.json");
//			Vector<Car> cars = CarDeserializer.deserializeArrayFromFile("test-formatted.json");
//			for(Car c : cars) {
//				System.out.println(c);
//			}
//		} catch (IOException e) {
//			System.out.println(e.getMessage());
//		}
//	}
	
}
