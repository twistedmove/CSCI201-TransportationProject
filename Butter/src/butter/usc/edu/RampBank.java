package butter.usc.edu;

import java.io.File;
import java.util.Vector;
/*
 *  RampBank class: creates all Ramps for all 4 freeways and stores the Ramps in a 2-dimensional vector--a vector of a vector. The String names of the ramps are stored in rampNames, a 2-dimension array
 *  @author Kaitlyn
 */

public class RampBank {
	public static final int FREEWAY101 = 101;
	public static final int FREEWAY10 = 10;
	public static final int FREEWAY105 = 105;
	public static final int FREEWAY405 = 405;
	
	public static Vector<Vector<Ramp>> allRamps;
	public static Vector<Ramp> ramps101;
	public static Vector<Ramp> ramps105;
	public static Vector<Ramp> ramps10;
	public static Vector<Ramp> ramps405;
	public static String freeways [] = {"101", "105", "10", "405"};

	public static final int num101ramps = 57;
	public static final int num105ramps = 21;
	public static final int num10ramps = 37;
	public static final int num405ramps = 30;
	// See below for variables that hold more data

	public RampBank() {
		allRamps = new Vector<Vector<Ramp>>();
		try {
			ramps101 = FreewayParser.parseRamps(new File("CSV/101-ramps-tabs.txt"), FREEWAY101);
			ramps105 = FreewayParser.parseRamps(new File("CSV/105-ramps-tabs.txt"), FREEWAY105);
			ramps10 = FreewayParser.parseRamps(new File("CSV/10-ramps-tabs.txt"), FREEWAY10);
			ramps405 = FreewayParser.parseRamps(new File("CSV/405-ramps-tabs.txt"), FREEWAY405);
			System.out.println("Successfully read in ramps.");
		} catch (TxtFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		for (int i = 0; i < num101ramps; i++) {
//			ramps101.add(new Ramp(rampNames[0][i], 101, indices101[i], PathBank.locations101.get(indices101[i])));
//		}
//
//		for (int i = 0; i < num105ramps; i++) {
//			ramps105.add(new Ramp(rampNames[1][i], 105, indices105[i], PathBank.locations105.get(indices105[i])));
//		}
//
//		for (int i = 0; i < num10ramps; i++) {
//			ramps10.add(new Ramp(rampNames[2][i], 110, indices10[i], PathBank.locations10.get(indices10[i])));
//		}
//
//		for (int i = 0; i < num405ramps; i++) {
//			ramps405.add(new Ramp(rampNames[3][i], 405, indices405[i], PathBank.locations405.get(indices405[i])));
//		}
		
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		for (int i = 0; i < ramps101.size(); i++) {
			if(i != 0 ) ramps101.get(i).setPreviousRamp(ramps101.get(i-1));
			ramps101.get(i).setLocation(PathBank.locations101.get(ramps101.get(i).getIndexOfCoordinate()));
		}

		for (int i = 0; i < ramps105.size(); i++) {
			if(i != 0 )ramps105.get(i).setPreviousRamp(ramps105.get(i-1));
			ramps105.get(i).setLocation(PathBank.locations105.get(ramps105.get(i).getIndexOfCoordinate()));
		}

		for (int i = 0; i < ramps10.size(); i++) {
			if(i != 0 )ramps10.get(i).setPreviousRamp(ramps10.get(i-1));
			ramps10.get(i).setLocation(PathBank.locations10.get(ramps10.get(i).getIndexOfCoordinate()));
		}

		for (int i = 0; i < ramps405.size(); i++) {
			if(i != 0 )ramps405.get(i).setPreviousRamp(ramps405.get(i-1));
			ramps405.get(i).setLocation(PathBank.locations405.get(ramps405.get(i).getIndexOfCoordinate()));
		}
		
		allRamps.add(ramps101);
		allRamps.add(ramps105);
		allRamps.add(ramps10);
		allRamps.add(ramps405);
		
		setBranches();
	}
	
	private void setBranches() {
		System.out.println("-----------------------------");
		for (int i = 1; i < ramps101.size(); i++) {
			if(!ramps101.get(i).branchName.equals("NULL")) {
				for(Vector<Ramp> vr : allRamps) {
					for(Ramp r : vr) {
						if (r.name.equals(ramps101.get(i).branchName)) {
							ramps101.get(i).setBranchRamp(r);
							System.out.println("Branch set: " + ramps101.get(i).branchName + " --> " + r.name);
						}
					}
				}
			}
		}

		for (int i = 1; i < ramps10.size(); i++) {
			if(!ramps10.get(i).branchName.equals("NULL")) {
				for(Vector<Ramp> vr : allRamps) {
					for(Ramp r : vr) {
						if (r.name.equals(ramps10.get(i).branchName)) {
							ramps10.get(i).setBranchRamp(r);
							System.out.println("Branch set: " + ramps10.get(i).name + " --> " + r.name);
						}
					}
				}
			}
		}

		for (int i = 1; i < ramps405.size(); i++) {
			if(!ramps405.get(i).branchName.equals("NULL")) {
				for(Vector<Ramp> vr : allRamps) {
					for(Ramp r : vr) {
						if (r.name.equals(ramps405.get(i).branchName))  {
							ramps405.get(i).setBranchRamp(r);
							System.out.println("Branch set: " + ramps405.get(i).name + " --> " + r.name);
						}
					}
				}
			}
		}
		System.out.println("-----------------------------");
	}
	
	// In order: 101, 105, 110, 405
	public static String rampNames[][] = {{"I-5 south (Santa Ana Freeway) Santa Ana",
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
		{"Eastern Avenue", 
		"City Terrace Drive", 
		"Soto Street", 
		"State Street", 
		"US 101 north (Santa Ana Freeway via San Bernardino Freeway) Los Angeles, Hollywood", 
		"I-5 north (Golden State Freeway) Sacramento", 
		"Cesar Chavez Avenue", 
		"Fourth Street", 
		"Boyle Avenue", 
		"I-5 south (Santa Ana Freeway) / SR 60 east (Pomona Freeway) Santa Ana, Pomona", 
		"Mateo Street, Santa Fe Avenue", 
		"Alameda Street", 
		"Central Avenue", 
		"San Pedro Street", 
		"Maple Avenue", 
		"Los Angeles Street Convention Center", 
		"Grand Avenue", 
		"I-110 south (Harbor Freeway) / SR 110 north (Harbor Freeway) / Pico Boulevard San Pedro, Pasadena, Downtown Los Angeles", 
		"Hoover Street", 
		"Hoover Street, Vermont Avenue", 
		"Western Avenue, Normandie Avenue", 
		"Arlington Avenue", 
		"Crenshaw Boulevard", 
		"La Brea Avenue", 
		"Fairfax Avenue, Washington Boulevard", 
		"La Cienega Boulevard, Venice Boulevard (SR 187 west)", 
		"Robertson Boulevard Culver City", 
		"National Boulevard", 
		"National Boulevard, Overland Avenue", 
		"I-405 (San Diego Freeway) Sacramento, LAX Airport, Long Beach", 
		"Bundy Drive", 
		"Centinela Avenue", 
		"Cloverfield Boulevard", 
		"20th Street", 
		"SR 1 south (Lincoln Boulevard) to SR 2 east", 
		"4th Street, 5th Street", 
		"SR 1 north (Pacific Coast Highway) Oxnard"},
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
	
	public static int indices101 [] = {0, 35, 39, 46, 58, 62, 67, 72, 73, 78, 84, 85, 86, 87, 89, 90, 98, 100, 105, 107, 111, 115, 118, 128, 129, 137, 140, 142, 146, 160, 164, 169, 173, 181, 192, 193, 195, 197, 207, 215, 226, 235, 243, 252, 257, 265, 272, 279, 286, 292, 300, 302, 304, 311, 318, 322, 327};
	public static int indices105 [] = {0, 9, 14, 27, 32, 49, 55, 67, 83, 91, 114, 124, 154, 166, 176, 190, 196, 199, 205, 211, 215};
	public static int indices10 [] = {0, 10, 17, 36, 39, 63, 69, 75, 90, 106, 109, 118, 133, 135, 138, 144, 152, 164, 176, 178, 180, 192, 200, 206, 217, 224, 236, 247, 258, 269, 279, 287, 295, 305, 313, 314, 336};

	//public static int indices10 [] = {336, 314, 313, 305, 295, 287, 279, 269, 258, 247, 236, 224, 217, 206, 200, 192, 180, 178, 176, 164, 152, 144, 138, 135, 133, 118, 109, 106, 90, 75, 69, 63, 39, 36, 17, 10, 0};
	public static int indices405 [] = {0, 41, 45, 53, 56, 57, 66, 69, 76, 80, 91, 97, 110, 118, 123, 127, 143, 181, 212, 223, 237, 251, 260, 266, 269, 280, 285, 290, 296, 313};
	public static int allIndices[][] = {indices101, indices105, indices10, indices405};
}
