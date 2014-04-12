package butter.usc.edu;

import java.util.Vector;

public class RampBank {
	// In order: 101, 105, 110, 405
	String rampNames[][] = {{"I-5 south (Santa Ana Freeway) Santa Ana",
			"Euclid Avenue",
			"SR 60 east (Pomona Freeway) / Soto Street Pomona",
			"Seventh Street",
			"Fourth Street",
			"First Street",
			"Cesar Chavez Avenue",
			"I-10 east (San Bernardino Freeway) San Bernardino",
			"Mission Road",
			"Vignes Street",
			"Alameda Street Union Station",
			"Los Angeles Street",
			"Spring Street",
			"Broadway",
			"Grand Avenue, Temple Street",
			"SR 110 north (Pasadena Freeway) / I-110 south (Harbor Freeway) Pasadena, San Pedro",
			"Glendale Boulevard, Echo Park Avenue, Union Avenue, Belmont Avenue",
			"SR 2 east (Alvarado Street)",
			"Rampart Boulevard, Benton Way",
			"Silver Lake Boulevard",
			"Vermont Avenue",
			"Melrose Avenue, Normandie Avenue",
			"SR 2 west (Santa Monica Boulevard) / Western Avenue",
			"Sunset Boulevard",
			"Hollywood Boulevard",
			"Gower Street",
			"Vine Street",
			"Cahuenga Boulevard Hollywood Bowl",
			"Highland Avenue (SR 170 south) Hollywood Bowl",
			"Barham Boulevard Burbank",
			"Universal Studios Boulevard",
			"Lankershim Boulevard Universal City",
			"Ventura Boulevard",
			"Vineland Avenue",
			"SR 134 east (Ventura Freeway) Pasadena",
			"SR 170 north (Hollywood Freeway) Sacramento",
			"Tujunga Avenue",
			"Laurel Canyon Boulevard Studio City",
			"Coldwater Canyon Avenue",
			"Woodman Avenue",
			"Van Nuys Boulevard",
			"Sepulveda Boulevard",
			"I-405 (San Diego Freeway) Santa Monica, Sacramento",
			"Haskell Avenue",
			"Hayvenhurst Avenue",
			"Balboa Boulevard Encino",
			"White Oak Avenue",
			"Reseda Boulevard",
			"Tampa Avenue",
			"Winnetka Avenue Woodland Hills",
			"De Soto Avenue, Serrania Avenue",
			"Canoga Avenue",
			"SR 27 (Topanga Canyon Boulevard) / Ventura Boulevard",
			"Shoup Avenue",
			"Fallbrook Avenue",
			"Woodlake Avenue",
	"Mulholland Drive, Valley Circle Boulevard"},
{"Imperial Highway west", "SR 1 (Sepulveda Boulevard) / Imperial Highway east LAX Airport",
			"Nash Street LAX Airport",
			"La Cienega Boulevard, Aviation Boulevard",
			"I-405 (San Diego Freeway) Santa Monica, Long Beach",
			"Hawthorne Boulevard",
			"Prairie Avenue",
			"Crenshaw Boulevard",
			"Vermont Avenue",
			"I-110 (Harbor Freeway) Los Angeles, San Pedro",
			"Central Avenue",
			"Wilmington Avenue",
			"Long Beach Boulevard",
			"I-710 (Long Beach Freeway) Pasadena, Long Beach",
			"Garfield Avenue",
			"Paramount Boulevard",
			"SR 19 (Lakewood Boulevard)",
			"Bellflower Boulevard",
			"I-605 (San Gabriel River Freeway)",
			"Hoxie Avenue Norwalk Metro Station",
	"Studebaker Road"},
		{"SR 1 north (Pacific Coast Highway) Oxnard",
			"4th Street, 5th Street",
			"SR 1 south (Lincoln Boulevard) to SR 2 east",
			"20th Street",
			"Cloverfield Boulevard",
			"Centinela Avenue",
			"Bundy Drive",
			"I-405 (San Diego Freeway) Sacramento, LAX Airport, Long Beach",
			"National Boulevard, Overland Avenue",
			"National Boulevard",
			"Robertson Boulevard Culver City",
			"La Cienega Boulevard, Venice Boulevard (SR 187 west)",
			"Fairfax Avenue, Washington Boulevard",
			"La Brea Avenue",
			"Crenshaw Boulevard",
			"Arlington Avenue",
			"Western Avenue, Normandie Avenue",
			"Hoover Street, Vermont Avenue",
			"Hoover Street",
			"I-110 south (Harbor Freeway) / SR 110 north (Harbor Freeway) / Pico Boulevard San Pedro, Pasadena, Downtown Los Angeles",
			"Grand Avenue",
			"Los Angeles Street Convention Center",
			"Maple Avenue",
			"San Pedro Street",
			"Central Avenue",
			"Alameda Street",
			"Mateo Street, Santa Fe Avenue",
			"I-5 south (Santa Ana Freeway) / SR 60 east (Pomona Freeway) Santa Ana, Pomona",
			"Boyle Avenue",
			"Fourth Street",
			"Cesar Chavez Avenue",
			"I-5 north (Golden State Freeway) Sacramento",
			"US 101 north (Santa Ana Freeway via San Bernardino Freeway) Los Angeles, Hollywood",
			"State Street",
			"Soto Street",
			"City Terrace Drive",
	"Eastern Avenue"},
{"Manchester Boulevard, La Cienega Boulevard, Florence Avenue",
			"La Tijera Boulevard",
			"Howard Hughes Parkway, Sepulveda Boulevard",
			"Sepulveda Boulevard, Slauson Avenue (SR 90 east)",
			"Jefferson Boulevard",
			"SR 90 west (Marina Freeway)   Marina del Rey",
			"Culver Boulevard, Washington Boulevard   Culver City",
			"Venice Boulevard (SR 187), Washington Boulevard",
			"National Boulevard",
			"I-10 (Santa Monica Freeway)   Santa Monica, Los Angeles",
			"Olympic Boulevard, Pico Boulevard",
			"SR 2 (Santa Monica Boulevard)",
			"Wilshire Boulevard",
			"Montana Avenue",
			"Sunset Boulevard",
			"Moraga Drive",
			"Getty Center Drive",
			"Mulholland Drive, Skirball Center Drive",
			"Ventura Boulevard, Sepulveda Boulevard, Valley Vista Boulevard",
			"US 101 (Ventura Freeway)   Ventura, Los Angeles",
			"Burbank Boulevard",
			"Victory Boulevard   Van Nuys",
			"Sherman Way",
			"Roscoe Boulevard   Panorama City",
			"Nordhoff Street",
			"Devonshire Street   Granada Hills",
			"SR 118 (Ronald Reagan Freeway)   Simi Valley",
			"San Fernando Mission Boulevard   San Fernando",
			"Rinaldi Street   Mission Hills",
			"I-5 north (Golden State Freeway)   Sacramento"
	}};
	Vector<Vector<Ramp>> allRamps;
	public static final int num101ramps = 57;
	public static final int num105ramps = 20;
	public static final int num110ramps = 37;
	public static final int num405ramps = 30;

	public RampBank() {
		allRamps = new Vector<Vector<Ramp>>();
		Vector<Ramp> ramps101 = new Vector<Ramp>();
		Vector<Ramp> ramps105 = new Vector<Ramp>();
		Vector<Ramp> ramps110 = new Vector<Ramp>();
		Vector<Ramp> ramps405 = new Vector<Ramp>();

		for (int i = 0; i < num101ramps; i++) {
			ramps101.add(new Ramp(rampNames[0][i], 101, 0, 0));
		}

		for (int i = 0; i < num105ramps; i++) {
			ramps105.add(new Ramp(rampNames[1][i], 105, 0, 0));
		}

		for (int i = 0; i < num110ramps; i++) {
			ramps110.add(new Ramp(rampNames[2][i], 110, 0, 0));
		}

		for (int i = 0; i < num405ramps; i++) {
			ramps405.add(new Ramp(rampNames[3][i], 405, 0, 0));
		}
		
		allRamps.add(ramps101);
		allRamps.add(ramps105);
		allRamps.add(ramps110);
		allRamps.add(ramps405);
	}
}