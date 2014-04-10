package butter.usc.edu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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
	 * Overrides default JsonDeserializer functionality so that variable names do not have to match.
	 * @author LorraineSposto
	 *
	 */
	static class CustomCarDeserializer implements JsonDeserializer<Car> {
		@Override
		public Car deserialize(final JsonElement json, final Type type,
				final JsonDeserializationContext context) throws JsonParseException {
			// TODO Auto-generated method stub
			
			final JsonObject jsonObject = json.getAsJsonObject();
			final int id = jsonObject.get("id").getAsInt();
			final double speed = jsonObject.get("speed").getAsDouble();
			final String direction = jsonObject.get("direction").getAsString();
			final String ramp = jsonObject.get("on/off ramp").getAsString();
			final String freeway = jsonObject.get("freeway").getAsString();
			
			final Car c = new Car();
			c.setId(id);
			c.setSpeed(speed);
			c.setDirection(direction);
			c.setRamp(ramp);
			c.setFreeway(freeway);
			return c;
		}
	}

	/**
	 * Reads a JSON file containing an array of cars and deserializes it into a Vector.
	 * Currently the on/off ramps don't work and just produce null.
	 * @param filename - The filename of the JSON file
	 * @return A vector of Cars
	 * @throws IOException
	 */

	public static Vector<Car> deserializeArrayFromFile(String filename) throws IOException {
		JsonReader reader = null;
		try {
			Vector<Car> cars = new Vector<Car>();
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Car.class, new CustomCarDeserializer());
			Gson gson = gsonBuilder.create();
	
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
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Car.class, new CustomCarDeserializer());
			Gson gson = gsonBuilder.create();
	
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
//			Vector<Car> cars = CarDeserializer.deserializeArrayFromURL("http://www-scf.usc.edu/~csci201/mahdi_project/test.json");
////			Vector<Car> cars = CarDeserializer.deserializeArrayFromFile("test-formatted.json");
//			for(Car c : cars) {
//				System.out.println(c);
//			}
//		} catch (IOException e) {
//			System.out.println(e.getMessage());
//		}
//	}
	
}
