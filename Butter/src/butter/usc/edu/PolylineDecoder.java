package butter.usc.edu;

import java.util.Vector;

// Taken from http://www.geekyblogger.com/2010/12/decoding-polylines-from-google-maps.html

class PolylineDecoder {
	public static Vector<Location> decodePoly(String encoded) {
		Vector<Location> poly = new Vector<Location>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;
		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;
			Location p = new Location((((double) lat / 1E5)),
					(((double) lng / 1E5)));
			poly.add(p);
		}
		return poly;
	}
//
//	public static void main(String[] args) {
//		Vector<Location> loc = PolylineDecoder.decodePoly("gzynEfmupUu@nAQZk@bAeAjBMTw@xA");
//		System.out.println("Done");
//		for(int i=0; i < loc.size(); ++i) {
//			System.out.println(loc.get(i));
//		}
//	}
}