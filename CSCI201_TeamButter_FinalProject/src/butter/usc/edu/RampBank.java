package butter.usc.edu;

import java.io.File;
import java.util.Vector;
/*
 *  RampBank class: creates all Ramps for all 4 freeways and stores the Ramps in a 2-dimensional vector--a vector of a vector. 
 *  The String names of the ramps are stored in rampNames, a 2-dimension array
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
	
	public static String rampNames[][];
	
	public static int indices101 [];
	public static int indices105 [];
	public static int indices10 [];
	public static int indices405 [];
	public static int allIndices[][];

	public RampBank() {
		allRamps = new Vector<Vector<Ramp>>();
		try {
			ramps101 = FreewayParser.parseRamps(new File("assets/CSV/101-ramps-tabs.csv"), FREEWAY101);
			ramps105 = FreewayParser.parseRamps(new File("assets/CSV/105-ramps-tabs.csv"), FREEWAY105);
			ramps10 = FreewayParser.parseRamps(new File("assets/CSV/10-ramps-tabs.csv"), FREEWAY10);
			ramps405 = FreewayParser.parseRamps(new File("assets/CSV/405-ramps-tabs.csv"), FREEWAY405);
			System.out.println("Successfully read in ramps.");
		} catch (TxtFormatException e) {
			e.printStackTrace();
		}


		/**
		 * SETTING NEXT AND PREVS AND LOCATIONS
		 */
		
		for (int i = 0; i < ramps101.size(); i++) {
			if(i > 0 ) {
				ramps101.get(i).setPreviousRamp(ramps101.get(i-1));
			}
			ramps101.get(i).setLocation(PathBank.locations101.get(ramps101.get(i).getIndexOfCoordinate()));
		}

		for (int i = 0; i < ramps105.size(); i++) {
			if(i > 0 ) {
				ramps105.get(i).setPreviousRamp(ramps105.get(i-1));
			}
			ramps105.get(i).setLocation(PathBank.locations105.get(ramps105.get(i).getIndexOfCoordinate()));
		}

		for (int i = 0; i < ramps10.size(); i++) {
			if(i > 0 ) {
				ramps10.get(i).setPreviousRamp(ramps10.get(i-1));
			}
			ramps10.get(i).setLocation(PathBank.locations10.get(ramps10.get(i).getIndexOfCoordinate()));
		}

		for (int i = 0; i < ramps405.size(); i++) {
			if(i > 0 ) {
				ramps405.get(i).setPreviousRamp(ramps405.get(i-1));
			}
			ramps405.get(i).setLocation(PathBank.locations405.get(ramps405.get(i).getIndexOfCoordinate()));
		}
		
		allRamps.add(ramps101);
		allRamps.add(ramps105);
		allRamps.add(ramps10);
		allRamps.add(ramps405);
		
		setBranches();
		
		// In order: 101, 105, 10, 405
		rampNames = new String[allRamps.size()][];
		
		int index = 0;
		for(Vector<Ramp> vr : allRamps) {
			rampNames[index] = new String[vr.size()];
			for(int i=0; i < vr.size(); ++i) {
				rampNames[index][i] = vr.get(i).name;
			}
			index++;
		}
		
		indices10 = new int[ramps10.size()];
		for(int i=0; i < ramps10.size(); ++i) {
			indices10[i] = ramps10.get(i).indexOfCoordinate;
		}
		
		indices105 = new int[ramps105.size()];
		for(int i=0; i < ramps105.size(); ++i) {
			indices105[i] = ramps105.get(i).indexOfCoordinate;
		}
		
		indices101 = new int[ramps101.size()];
		for(int i=0; i < ramps101.size(); ++i) {
			indices101[i] = ramps101.get(i).indexOfCoordinate;
		}
		
		indices405 = new int[ramps405.size()];
		for(int i=0; i < ramps405.size(); ++i) {
			indices405[i] = ramps405.get(i).indexOfCoordinate;
		}
		
		allIndices = new int[allRamps.size()][];
		for(int i=0; i < allRamps.size(); ++i) {
			allIndices[i] = new int[allRamps.get(i).size()];
			for(int k = 0; k < allRamps.get(i).size(); ++k) {
				allIndices[i][k] = allRamps.get(i).get(k).indexOfCoordinate;
			}
		}
		
	}
	
	private void setBranches() {
		for (int i = 1; i < ramps101.size(); i++) {
			if(!ramps101.get(i).branchName.equals("NULL")) {
				for(Vector<Ramp> vr : allRamps) {
					for(Ramp r : vr) {
						if (r.name.equals(ramps101.get(i).branchName)) {
							ramps101.get(i).setBranchRamp(r);
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
						}
					}
				}
			}
		}
	}
}
