package butter.usc.edu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

/**
 * Deserializes a JSONArray of car objects.
 * @author LorraineSposto
 *
 */
public class CarDeserializer {

	/**
	 * Reads a JSON file containing an array of cars and deserializes it into a Vector
	 * @param filename - The filename of the JSON file
	 * @return A vector of Cars
	 * @throws IOException
	 */
	public static Vector<Car> deserializeArrayOfCars(String filename) throws IOException {
		FileInputStream in;
		Vector<Car> cars = new Vector<Car>();
		Gson gson = new Gson();

		in = new FileInputStream(new File(filename));
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

		Type collectionType = new TypeToken<Vector<Car>>(){}.getType();
		cars = gson.fromJson(reader, collectionType);
		return cars;
	}
	
//	public static void main(String[] args) {
//		try {
//			Vector<Car> cars = CarDeserializer.deserializeArrayOfCars("test-formatted.json");
//			for(Car c : cars) {
//				System.out.println(c);
//			}
//		} catch (IOException e) {
//			System.out.println(e.getMessage());
//		}
//	}
	
}
