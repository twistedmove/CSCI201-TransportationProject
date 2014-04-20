package butter.usc.edu;

import java.io.File;
import java.util.Vector;
/*
 * PathBank holds all Locations on the paths for each highway in a vector.
 */
public class PathBank {
	public static final int FREEWAY101 = 101;
	public static final int FREEWAY10 = 10;
	public static final int FREEWAY105 = 105;
	public static final int FREEWAY405 = 405;
	
	public static Vector<Location> locations101;
	public static Vector<Location> locations105;
	public static Vector<Location> locations10;
	public static Vector<Location> locations405;
	public static Vector<Vector<Location>> allLocations;
	
	public PathBank() throws TxtFormatException {
		
		locations101 = FreewayParser.parseFreeway(new File("assets/CSV/101-path.csv"), FREEWAY101);
		locations105 = FreewayParser.parseFreeway(new File("assets/CSV/105-path.csv"), FREEWAY105);
		locations10 = FreewayParser.parseFreeway(new File("assets/CSV/10-path.csv"), FREEWAY10);
		locations405 = FreewayParser.parseFreeway(new File("assets/CSV/405-path.csv"), FREEWAY405);
		
		findIntersections();
		System.out.println("Successfully read in all freeway coordinates.");
		
		locations405.get(0).setFirst(); 
		locations101.get(0).setFirst();
		locations105.get(0).setFirst();
		locations10.get(0).setFirst();
		
		locations405.get(locations405.size() - 1).setLast();
		locations101.get(locations101.size() - 1).setLast();
		locations105.get(locations105.size() - 1).setLast();
		locations10.get(locations10.size() - 1).setLast();
		
		allLocations = new Vector<Vector<Location>>();
		allLocations.add(locations101);
		allLocations.add(locations105);
		allLocations.add(locations10);
		allLocations.add(locations405);
	}
	
	/**
	 * Goes through each locations vector an sets the intersections
	 */
	private void findIntersections() {
		for(Location loc : locations10) {
			if(loc.branchNum == FREEWAY101) {
				intersect(loc, locations101);
			}
			else if(loc.branchNum == FREEWAY105) {
				intersect(loc, locations105);
			}
			else if(loc.branchNum == FREEWAY405) {
				intersect(loc, locations405);
			}
		}
		
		for(Location loc : locations101) {
			if(loc.branchNum == FREEWAY10) {
				intersect(loc, locations10);
			}
			else if(loc.branchNum == FREEWAY105) {
				intersect(loc, locations105);
			}
			else if(loc.branchNum == FREEWAY405) {
				intersect(loc, locations405);
			}
		}
		
		for(Location loc : locations405) {
			if(loc.branchNum == FREEWAY101) {
				intersect(loc, locations101);
			}
			else if(loc.branchNum == FREEWAY105) {
				intersect(loc, locations105);
			}
			else if(loc.branchNum == FREEWAY10) {
				intersect(loc, locations10);
			}
		}
		
		for(Location loc : locations105) {
			if(loc.branchNum == FREEWAY101) {
				intersect(loc, locations101);
			}
			else if(loc.branchNum == FREEWAY10) {
				intersect(loc, locations10);
			}
			else if(loc.branchNum == FREEWAY405) {
				intersect(loc, locations405);
			}
		}
	}
	
	private static void intersect(Location loc, Vector<Location> v2) {
		for(Location loc2 : v2) {
			if(loc2.branchNum == loc.freeway) {
				loc.setBranch(loc2);
			}
		}
	}
	
}

//	public static Double coordinates101 [][] = {{34.02624,-118.20833},
//			{34.02605,-118.20785},
//			{34.02574,-118.20698},
//			{34.02526,-118.20548},
//			{34.02481,-118.20436},
//			{34.02429,-118.20316},
//			{34.02218,-118.19828},
//			{34.02119,-118.19604},
//			{34.02111,-118.19603},
//			{34.02088,-118.1957},
//			{34.02069,-118.1956},
//			{34.02059,-118.19563},
//			{34.02048,-118.19571},
//			{34.02039,-118.19535},
//			{34.02029,-118.19455},
//			{34.02006,-118.19348},
//			{34.01978,-118.19227},
//			{34.02028,-118.19226},
//			{34.02063,-118.19226},
//			{34.02087,-118.19227},
//			{34.02112,-118.1928},
//			{34.02173,-118.19422},
//			{34.02195,-118.19469},
//			{34.0217,-118.19485},
//			{34.02128,-118.19516},
//			{34.02124,-118.19522},
//			{34.02123,-118.19538},
//			{34.02124,-118.1955},
//			{34.02121,-118.19559},
//			{34.0218,-118.19696},
//			{34.02216,-118.19781},
//			{34.02349,-118.20088},
//			{34.02474,-118.20376},
//			{34.02535,-118.20532},
//			{34.02598,-118.20724},
//			{34.02816,-118.21216},
//			{34.02921,-118.21454},
//			{34.02936,-118.21492},
//			{34.02986,-118.21595},
//			{34.0308,-118.21776},
//			{34.03091,-118.21793},
//			{34.03146,-118.21871},
//			{34.03218,-118.21957},
//			{34.03251,-118.21986},
//			{34.0329,-118.22026},
//			{34.03358,-118.22082},
//			{34.03414,-118.22119},
//			{34.03455,-118.22136}, // intersect with 10
//			{34.03506,-118.22152},
//			{34.03578,-118.22164},
//			{34.03632,-118.22165},
//			{34.03686,-118.22155},
//			{34.03792,-118.22143},
//			{34.03838,-118.22145},
//			{34.03934,-118.22155},
//			{34.03997,-118.22152},
//			{34.04064,-118.2214},
//			{34.04109,-118.22128},
//			{34.04265,-118.22102},
//			{34.04418,-118.22102},
//			{34.0458,-118.22107},
//			{34.04666,-118.2212},
//			{34.04757,-118.22144},
//			{34.04828,-118.22171},
//			{34.04931,-118.2222},
//			{34.05085,-118.22299},
//			{34.0513,-118.22324},
//			{34.05177,-118.22359},
//			{34.0521,-118.22402},
//			{34.05239,-118.22457},
//			{34.05253,-118.22496},
//			{34.05259,-118.2253},
//			{34.05268,-118.22603},
//			{34.05279,-118.22654},
//			{34.05309,-118.2283},
//			{34.05331,-118.22972},
//			{34.05358,-118.23163},
//			{34.0537,-118.23275},
//			{34.05372,-118.23297},
//			{34.05393,-118.23492},
//			{34.0539,-118.23551},
//			{34.05387,-118.23582},
//			{34.05386,-118.23619},
//			{34.05393,-118.23693},
//			{34.05427,-118.23786},
//			{34.05509,-118.23927},
//			{34.05679,-118.24199},
//			{34.05814,-118.24405},
//			{34.05934,-118.24562},
//			{34.06028,-118.24662},
//			{34.06277,-118.24874},
//			{34.06369,-118.2496},
//			{34.06507,-118.25147},
//			{34.0668,-118.25388},
//			{34.06749,-118.25506},
//			{34.06781,-118.25575},
//			{34.06825,-118.25688},
//			{34.06936,-118.25999},
//			{34.07045,-118.26304},
//			{34.07113,-118.26463},
//			{34.07211,-118.26683},
//			{34.07298,-118.26896},
//			{34.07317,-118.26943},
//			{34.07424,-118.27166},
//			{34.07461,-118.27252},
//			{34.07518,-118.27402},
//			{34.07661,-118.27901},
//			{34.07721,-118.28138},
//			{34.07793,-118.28453},
//			{34.07863,-118.28805},
//			{34.0792,-118.28972},
//			{34.08021,-118.29169},
//			{34.08185,-118.295},
//			{34.08271,-118.29691},
//			{34.08355,-118.29846},
//			{34.08504,-118.30068},
//			{34.08701,-118.30348},
//			{34.08785,-118.30438},
//			{34.08859,-118.30501},
//			{34.09123,-118.30665},
//			{34.09211,-118.30723},
//			{34.09296,-118.30801},
//			{34.09329,-118.30842},
//			{34.0938,-118.30922},
//			{34.09517,-118.31138},
//			{34.09607,-118.31263},
//			{34.09654,-118.31314},
//			{34.09727,-118.31381},
//			{34.09813,-118.31443},
//			{34.10075,-118.31594},
//			{34.10196,-118.31667},
//			{34.10291,-118.31759},
//			{34.10322,-118.31799},
//			{34.10364,-118.31863},
//			{34.10391,-118.31926},
//			{34.10415,-118.32002},
//			{34.10431,-118.32095},
//			{34.1044,-118.32277},
//			{34.10449,-118.32397},
//			{34.10471,-118.32487},
//			{34.10516,-118.32594},
//			{34.10616,-118.3276},
//			{34.10807,-118.33082},
//			{34.10934,-118.33292},
//			{34.1097,-118.33348},
//			{34.11031,-118.33423},
//			{34.11063,-118.33454},
//			{34.11141,-118.33512},
//			{34.11191,-118.33538},
//			{34.1132,-118.33583},
//			{34.11489,-118.3364},
//			{34.11719,-118.33707},
//			{34.11864,-118.33763},
//			{34.11983,-118.3383},
//			{34.12114,-118.3393},
//			{34.12211,-118.34027},
//			{34.12465,-118.34323},
//			{34.12525,-118.3438},
//			{34.12667,-118.34478},
//			{34.12709,-118.34511},
//			{34.12864,-118.34667},
//			{34.13143,-118.34955},
//			{34.13225,-118.35045},
//			{34.13284,-118.35133},
//			{34.13331,-118.35231},
//			{34.13349,-118.35281},
//			{34.1342,-118.35576},
//			{34.1351,-118.35859},
//			{34.13553,-118.3595},
//			{34.1363,-118.36065},
//			{34.13785,-118.36284},
//			{34.13866,-118.36394},
//			{34.13966,-118.36522},
//			{34.1399,-118.36545},
//			{34.1406,-118.36599},
//			{34.14103,-118.36626},
//			{34.14262,-118.36696},
//			{34.14378,-118.36752},
//			{34.14514,-118.36822},
//			{34.14587,-118.36883},
//			{34.14664,-118.36973},
//			{34.1471,-118.3704},
//			{34.14815,-118.3719},
//			{34.14867,-118.37262},
//			{34.14903,-118.37301},
//			{34.14928,-118.37319},
//			{34.1501,-118.3738},
//			{34.15143,-118.37468},
//			{34.15242,-118.37524},
//			{34.153,-118.37559},
//			{34.15363,-118.37616},
//			{34.1541,-118.37689},
//			{34.15428,-118.37715},
//			{34.15445,-118.37768},
//			{34.1546,-118.37864},
//			{34.15458,-118.38112},
//			{34.15455,-118.39204},
//			{34.15456,-118.39648},
//			{34.15458,-118.39756},
//			{34.15479,-118.39879},
//			{34.15502,-118.39953},
//			{34.15528,-118.40011},
//			{34.1561,-118.40176},
//			{34.15656,-118.40305},
//			{34.1567,-118.40372},
//			{34.15681,-118.40494},
//			{34.15681,-118.40511},
//			{34.15681,-118.40552},
//			{34.15684,-118.418},
//			{34.15683,-118.42153},
//			{34.15672,-118.4225},
//			{34.15657,-118.42334},
//			{34.15584,-118.42669},
//			{34.15571,-118.42801},
//			{34.15573,-118.43189},
//			{34.15575,-118.43578},
//			{34.15575,-118.43888},
//			{34.1557,-118.43951},
//			{34.15556,-118.44042},
//			{34.1553,-118.44228},
//			{34.15529,-118.44354},
//			{34.15551,-118.44482},
//			{34.15582,-118.44581},
//			{34.15675,-118.4478},
//			{34.15694,-118.44824},
//			{34.15718,-118.449},
//			{34.15753,-118.45027},
//			{34.15768,-118.45109},
//			{34.15783,-118.45259},
//			{34.15806,-118.45518},
//			{34.15861,-118.4572},
//			{34.15891,-118.45813},
//			{34.15922,-118.45917},
//			{34.15943,-118.46013},
//			{34.15953,-118.46127},
//			{34.15952,-118.46361},
//			{34.15952,-118.46488},
//			{34.1596,-118.46573},
//			{34.15971,-118.46638},
//			{34.16013,-118.46781},
//			{34.16036,-118.46836},
//			{34.16097,-118.46942}, // intersect with 405
//			{34.16109,-118.46961}, 
//			{34.16128,-118.4699},
//			{34.16196,-118.47067},
//			{34.16225,-118.47094},
//			{34.16317,-118.4717},
//			{34.1637,-118.47215},
//			{34.16427,-118.47278},
//			{34.16444,-118.47301},
//			{34.16462,-118.47333},
//			{34.16477,-118.47363},
//			{34.16499,-118.47416},
//			{34.16529,-118.4752},
//			{34.16546,-118.47638},
//			{34.16547,-118.47695},
//			{34.16532,-118.48957},
//			{34.1653,-118.49093},
//			{34.16539,-118.49166},
//			{34.16553,-118.49236},
//			{34.16577,-118.49308},
//			{34.16603,-118.4937},
//			{34.16648,-118.49451},
//			{34.16845,-118.49754},
//			{34.17006,-118.50007},
//			{34.17048,-118.50093},
//			{34.1707,-118.50151},
//			{34.17102,-118.5026},
//			{34.17115,-118.50323},
//			{34.17129,-118.50458},
//			{34.17129,-118.50568},
//			{34.17128,-118.50975},
//			{34.17128,-118.51855},
//			{34.17132,-118.52142},
//			{34.17128,-118.52727},
//			{34.17127,-118.52875},
//			{34.1714,-118.52992},
//			{34.17163,-118.53089},
//			{34.172,-118.5319},
//			{34.17233,-118.53271},
//			{34.17296,-118.53425},
//			{34.17316,-118.53492},
//			{34.17333,-118.536},
//			{34.17341,-118.53849},
//			{34.17344,-118.54063},
//			{34.17361,-118.55083},
//			{34.17367,-118.5548},
//			{34.17374,-118.5588},
//			{34.17357,-118.56174},
//			{34.17348,-118.56267},
//			{34.173,-118.56789},
//			{34.17265,-118.56966},
//			{34.17221,-118.57118},
//			{34.17135,-118.57362},
//			{34.1701,-118.57688},
//			{34.16899,-118.57966},
//			{34.1685,-118.58161},
//			{34.16821,-118.58302},
//			{34.16801,-118.58518},
//			{34.16802,-118.58654},
//			{34.16813,-118.58792},
//			{34.16817,-118.58838},
//			{34.1696,-118.59782},
//			{34.17077,-118.60543},
//			{34.17087,-118.60657},
//			{34.17087,-118.6076},
//			{34.17075,-118.60862},
//			{34.17073,-118.60892},
//			{34.17046,-118.6099},
//			{34.17008,-118.61097},
//			{34.16984,-118.61147},
//			{34.16921,-118.61245},
//			{34.16815,-118.61399},
//			{34.1679,-118.61433},
//			{34.16746,-118.61499},
//			{34.16708,-118.61572},
//			{34.16682,-118.61636},
//			{34.16627,-118.61809},
//			{34.16464,-118.62328},
//			{34.1634,-118.62689},
//			{34.1621,-118.63049},
//			{34.16075,-118.63409},
//			{34.16073,-118.63443},
//			{34.16023,-118.6362},
//			{34.16015,-118.63682},
//			{34.16015,-118.63725},
//			{34.15999,-118.63774},
//			{34.15946,-118.63738}};
//	Double coordinates105 [][] = {{33.93122,-118.40398},
//			{33.93131,-118.40399},
//			{33.93131,-118.40338},
//			{33.9313,-118.40307},
//			{33.93127,-118.40257},
//			{33.93126,-118.40106},
//			{33.93128,-118.39973},
//			{33.93127,-118.39916},
//			{33.93122,-118.39769},
//			{33.93119,-118.39629},
//			{33.9312,-118.39528},
//			{33.93126,-118.39236},
//			{33.9312,-118.39085},
//			{33.93108,-118.38866},
//			{33.93102,-118.38764},
//			{33.93097,-118.38678},
//			{33.93096,-118.38561},
//			{33.93097,-118.38532},
//			{33.93104,-118.3827},
//			{33.93102,-118.38147},
//			{33.93098,-118.38089},
//			{33.93085,-118.38009},
//			{33.93064,-118.37922},
//			{33.93,-118.3766},
//			{33.92988,-118.37591},
//			{33.92981,-118.37519},
//			{33.92978,-118.37464},
//			{33.92977,-118.37413},
//			{33.92979,-118.37099},
//			{33.9298,-118.37002},
//			{33.9298,-118.36948},
//			{33.9298,-118.36925},
//			{33.92983,-118.36867},
//			{33.92984,-118.36856},
//			{33.92986,-118.36844},
//			{33.93,-118.36747},
//			{33.93015,-118.36672},
//			{33.93033,-118.36608},
//			{33.93073,-118.36479},
//			{33.93086,-118.36435},
//			{33.93139,-118.36253},
//			{33.93196,-118.36073},
//			{33.93248,-118.35895},
//			{33.93305,-118.35704},
//			{33.93325,-118.3561},
//			{33.93338,-118.35524},
//			{33.93345,-118.35453},
//			{33.93346,-118.35372},
//			{33.93342,-118.35272},
//			{33.93337,-118.35248},
//			{33.93305,-118.3508},
//			{33.93278,-118.34981},
//			{33.93232,-118.34848},
//			{33.93185,-118.34738},
//			{33.93119,-118.34607},
//			{33.93052,-118.34498},
//			{33.92978,-118.34388},
//			{33.92668,-118.3396},
//			{33.92609,-118.33866},
//			{33.92574,-118.33799},
//			{33.92531,-118.33692},
//			{33.92515,-118.33642},
//			{33.92494,-118.33564},
//			{33.92481,-118.33484},
//			{33.9247,-118.33369},
//			{33.92467,-118.33274},
//			{33.92475,-118.33118},
//			{33.92501,-118.32642},
//			{33.92509,-118.32526},
//			{33.92528,-118.32208},
//			{33.92529,-118.32009},
//			{33.92526,-118.31656},
//			{33.92523,-118.31374},
//			{33.92525,-118.31252},
//			{33.92528,-118.31147},
//			{33.92537,-118.30998},
//			{33.9255,-118.30873},
//			{33.92577,-118.30709},
//			{33.92597,-118.30616},
//			{33.92719,-118.30069},
//			{33.92728,-118.3003},
//			{33.92791,-118.29745},
//			{33.92819,-118.29606},
//			{33.9284,-118.29444},
//			{33.92847,-118.29344},
//			{33.92849,-118.2921},
//			{33.92849,-118.29183},
//			{33.92848,-118.29167},
//			{33.92848,-118.2913},
//			{33.9285,-118.28943},
//			{33.92847,-118.28743},
//			{33.92846,-118.28696},
//			{33.92848,-118.28273},
//			{33.92848,-118.28147},
//			{33.92848,-118.2814},
//			{33.92848,-118.28109},
//			{33.92849,-118.28026},
//			{33.92849,-118.28009},
//			{33.92846,-118.27831},
//			{33.92839,-118.27723},
//			{33.92829,-118.27632},
//			{33.9282,-118.27561},
//			{33.92803,-118.2747},
//			{33.92784,-118.27363},
//			{33.92754,-118.27232},
//			{33.92734,-118.2707},
//			{33.9273,-118.26969},
//			{33.92729,-118.26528},
//			{33.92729,-118.26032},
//			{33.92731,-118.2594},
//			{33.92735,-118.25875},
//			{33.92744,-118.2578},
//			{33.92752,-118.25723},
//			{33.92769,-118.25618},
//			{33.92783,-118.25517},
//			{33.92809,-118.25303},
//			{33.92825,-118.25188},
//			{33.92838,-118.25082},
//			{33.92849,-118.24977},
//			{33.92852,-118.24933},
//			{33.92858,-118.24841},
//			{33.92859,-118.24713},
//			{33.92854,-118.24605},
//			{33.92822,-118.24052},
//			{33.92814,-118.23914},
//			{33.92808,-118.23835},
//			{33.92801,-118.23734},
//			{33.92786,-118.23469},
//			{33.92784,-118.23325},
//			{33.92785,-118.23275},
//			{33.92796,-118.23171},
//			{33.92811,-118.2308},
//			{33.92822,-118.23031},
//			{33.92827,-118.23011},
//			{33.92828,-118.2301},
//			{33.92873,-118.22833},
//			{33.9292,-118.22645},
//			{33.92934,-118.22578},
//			{33.92937,-118.22558},
//			{33.92946,-118.22468},
//			{33.92949,-118.22404},
//			{33.9295,-118.22349},
//			{33.92949,-118.22303},
//			{33.92944,-118.2222},
//			{33.92934,-118.22154},
//			{33.92926,-118.22105},
//			{33.9291,-118.22034},
//			{33.92896,-118.21979},
//			{33.92877,-118.21921},
//			{33.92846,-118.21842},
//			{33.92761,-118.21649},
//			{33.92655,-118.21412},
//			{33.92624,-118.21348},
//			{33.92492,-118.21043},
//			{33.92471,-118.20997},
//			{33.92312,-118.20637},
//			{33.92239,-118.20469},
//			{33.92191,-118.20358},
//			{33.92163,-118.20299},
//			{33.9213,-118.20222},
//			{33.91965,-118.19845},
//			{33.91801,-118.19474},
//			{33.9174,-118.19338},
//			{33.91697,-118.19244},
//			{33.91622,-118.19055},
//			{33.91588,-118.18957},
//			{33.91565,-118.18894},
//			{33.91469,-118.186},
//			{33.91415,-118.18443},
//			{33.91357,-118.18286},
//			{33.91279,-118.1805},
//			{33.91261,-118.18},
//			{33.91244,-118.1795},
//			{33.9123,-118.1791},
//			{33.91193,-118.17792},
//			{33.91176,-118.1774},
//			{33.91141,-118.17628},
//			{33.91122,-118.17548},
//			{33.91107,-118.1745},
//			{33.91098,-118.17366},
//			{33.91092,-118.17228},
//			{33.91089,-118.17106},
//			{33.91087,-118.17072},
//			{33.9109,-118.16935},
//			{33.91092,-118.1689},
//			{33.91093,-118.16865},
//			{33.91123,-118.16524},
//			{33.91134,-118.16388},
//			{33.9117,-118.16022},
//			{33.91192,-118.15806},
//			{33.91232,-118.15374},
//			{33.91263,-118.15008},
//			{33.9127,-118.14902},
//			{33.91277,-118.14818},
//			{33.91283,-118.14693},
//			{33.91289,-118.14497},
//			{33.91291,-118.14112},
//			{33.91294,-118.13431},
//			{33.91297,-118.12715},
//			{33.91303,-118.12545},
//			{33.91314,-118.12328},
//			{33.91338,-118.12005},
//			{33.91359,-118.11695},
//			{33.91368,-118.11428},
//			{33.91368,-118.11376},
//			{33.91374,-118.11353},
//			{33.91375,-118.11347},
//			{33.91377,-118.11229},
//			{33.91378,-118.11055},
//			{33.91378,-118.10855},
//			{33.91378,-118.10634},
//			{33.91382,-118.10561},
//			{33.91411,-118.10312},
//			{33.91426,-118.10165},
//			{33.91425,-118.10015},
//			{33.91424,-118.09917}};
//	Double coordinates10 [][] = {
//			{34.06067,-118.17366},
//			{34.06034,-118.17432},
//			{34.06022,-118.17451},
//			{34.06014,-118.17456},
//			{34.05975,-118.17548},
//			{34.05848,-118.17833},
//			{34.05787,-118.1797},
//			{34.05715,-118.18127},
//			{34.05616,-118.18336},
//			{34.05588,-118.18412},
//			{34.05564,-118.18493},
//			{34.05549,-118.18573},
//			{34.05541,-118.18648},
//			{34.05538,-118.18706},
//			{34.0554,-118.18798},
//			{34.05552,-118.1927},
//			{34.05561,-118.19615},
//			{34.05561,-118.19708},
//			{34.05556,-118.19788},
//			{34.0555,-118.19849},
//			{34.05557,-118.19868},
//			{34.05556,-118.19883},
//			{34.05529,-118.20067},
//			{34.05519,-118.20137},
//			{34.05507,-118.20206},
//			{34.05485,-118.20291},
//			{34.05473,-118.20329},
//			{34.05416,-118.20477},
//			{34.05406,-118.2051},
//			{34.05398,-118.20543},
//			{34.05393,-118.20577},
//			{34.0539,-118.20654},
//			{34.05393,-118.20704},
//			{34.054,-118.20756},
//			{34.05413,-118.20817},
//			{34.05422,-118.20848},
//			{34.0546,-118.20959},
//			{34.05541,-118.21151},
//			{34.05547,-118.21166},
//			{34.05628,-118.21342},
//			{34.0563,-118.21359},
//			{34.05639,-118.21392},
//			{34.05642,-118.21414},
//			{34.05643,-118.21443},
//			{34.05642,-118.21463},
//			{34.05636,-118.215},
//			{34.05627,-118.21523},
//			{34.05608,-118.21553},
//			{34.05585,-118.21573},
//			{34.05562,-118.21585},
//			{34.05536,-118.21592},
//			{34.05511,-118.2159},
//			{34.05502,-118.21588},
//			{34.05469,-118.2157},
//			{34.05426,-118.2154},
//			{34.05363,-118.21491},
//			{34.0533,-118.21466},
//			{34.05289,-118.21436},
//			{34.05274,-118.2143},
//			{34.05248,-118.21427},
//			{34.05237,-118.21425},
//			{34.05226,-118.21423},
//			{34.05214,-118.21419},
//			{34.05198,-118.21412},
//			{34.05145,-118.2141},
//			{34.05072,-118.21409},
//			{34.04985,-118.21416},
//			{34.04968,-118.21419},
//			{34.04907,-118.21427},
//			{34.04744,-118.21472},
//			{34.04676,-118.21498},
//			{34.04611,-118.21529},
//			{34.0457,-118.21552},
//			{34.04555,-118.2156},
//			{34.0448,-118.21607},
//			{34.04338,-118.21701},
//			{34.04247,-118.2176},
//			{34.04077,-118.21874},
//			{34.0402,-118.21913},
//			{34.03983,-118.21936},
//			{34.0396,-118.21949},
//			{34.03926,-118.21961},
//			{34.03873,-118.21978},
//			{34.03788,-118.22002},
//			{34.03726,-118.22024},
//			{34.03648,-118.22057},
//			{34.03587,-118.22082},
//			{34.03575,-118.22094},
//			{34.03506,-118.22124},
//			{34.03461,-118.2215}, // intersect with 101
//			{34.03431,-118.22164},
//			{34.03393,-118.22179},
//			{34.03387,-118.22182},
//			{34.03182,-118.22265},
//			{34.03134,-118.22291}, 
//			{34.0312,-118.22294}, 
//			{34.03098,-118.22308},
//			{34.03063,-118.22336},
//			{34.03039,-118.22361},
//			{34.03027,-118.22378},
//			{34.03018,-118.22392},
//			{34.02993,-118.22435},
//			{34.02972,-118.22481},
//			{34.02957,-118.22526},
//			{34.02942,-118.22593},
//			{34.02892,-118.22831},
//			{34.02864,-118.22948},
//			{34.02847,-118.22978},
//			{34.02826,-118.23053},
//			{34.02803,-118.23125},
//			{34.02766,-118.23221},
//			{34.02709,-118.23366},
//			{34.02647,-118.23516},
//			{34.0262,-118.23577},
//			{34.02523,-118.23814},
//			{34.02479,-118.23923},
//			{34.02432,-118.24037},
//			{34.02405,-118.24111},
//			{34.024,-118.24129},
//			{34.02396,-118.24146},
//			{34.02384,-118.24212},
//			{34.0238,-118.2425},
//			{34.02377,-118.24323},
//			{34.02382,-118.24399},
//			{34.02391,-118.24468},
//			{34.02397,-118.24488},
//			{34.02423,-118.24576},
//			{34.02443,-118.24634},
//			{34.02456,-118.2466},
//			{34.02464,-118.24676},
//			{34.02496,-118.24735},
//			{34.02597,-118.24916},
//			{34.02687,-118.2508},
//			{34.02777,-118.2525},
//			{34.0297,-118.25609},
//			{34.03001,-118.25666},
//			{34.03048,-118.25766},
//			{34.03095,-118.25883},
//			{34.03129,-118.25988},
//			{34.03158,-118.26084},
//			{34.03183,-118.26163},
//			{34.03201,-118.2621},
//			{34.03215,-118.26244},
//			{34.03224,-118.26264},
//			{34.03261,-118.26338},
//			{34.03298,-118.26404},
//			{34.03309,-118.26426},
//			{34.03371,-118.26536},
//			{34.03429,-118.26638},
//			{34.03434,-118.26649},
//			{34.03435,-118.26653},
//			{34.03442,-118.26667},
//			{34.03475,-118.26727},
//			{34.03549,-118.26853},
//			{34.03603,-118.26932},
//			{34.03609,-118.26941},
//			{34.03646,-118.26992},
//			{34.0369,-118.27054},
//			{34.03721,-118.27105},
//			{34.0378,-118.27211},
//			{34.03808,-118.2728},
//			{34.03826,-118.27336},
//			{34.03833,-118.2737},
//			{34.03836,-118.27388},
//			{34.03842,-118.27426},
//			{34.03847,-118.2749},
//			{34.03845,-118.27546},
//			{34.03843,-118.27567},
//			{34.03818,-118.27895},
//			{34.03813,-118.2794},
//			{34.03805,-118.27983},
//			{34.038,-118.28013},
//			{34.03767,-118.28167},
//			{34.03726,-118.2837},
//			{34.03715,-118.28432},
//			{34.03704,-118.28518},
//			{34.03698,-118.28637},
//			{34.03698,-118.28739},
//			{34.03704,-118.29112},
//			{34.03708,-118.29529},
//			{34.03708,-118.29655},
//			{34.03715,-118.30018},
//			{34.03716,-118.30072},
//			{34.03719,-118.30278},
//			{34.03721,-118.30394},
//			{34.03721,-118.30497},
//			{34.03723,-118.3089},
//			{34.03724,-118.30909},
//			{34.03727,-118.30961},
//			{34.03724,-118.31072},
//			{34.03715,-118.31335},
//			{34.03701,-118.31571},
//			{34.03681,-118.31764},
//			{34.03661,-118.31891},
//			{34.03595,-118.32364},
//			{34.03565,-118.32565},
//			{34.03553,-118.32661},
//			{34.03547,-118.32732},
//			{34.03541,-118.32842},
//			{34.03535,-118.32923},
//			{34.03517,-118.33335},
//			{34.035,-118.33645},
//			{34.03495,-118.338},
//			{34.03487,-118.33991},
//			{34.03472,-118.34318},
//			{34.03463,-118.34507},
//			{34.03438,-118.35016},
//			{34.0341,-118.35589},
//			{34.03396,-118.35849},
//			{34.03388,-118.36014},
//			{34.03388,-118.36093},
//			{34.03393,-118.36194},
//			{34.03395,-118.36215},
//			{34.03407,-118.36332},
//			{34.03446,-118.36538},
//			{34.03471,-118.36656},
//			{34.03496,-118.36772},
//			{34.03527,-118.3692},
//			{34.03587,-118.37194},
//			{34.03589,-118.37203},
//			{34.03636,-118.37422},
//			{34.03691,-118.37687},
//			{34.03707,-118.37794},
//			{34.03707,-118.37896},
//			{34.03699,-118.37962},
//			{34.03675,-118.38072},
//			{34.03666,-118.38105},
//			{34.03634,-118.3818},
//			{34.03591,-118.38258},
//			{34.03415,-118.38553},
//			{34.03389,-118.386},
//			{34.03346,-118.38672},
//			{34.03327,-118.38701},
//			{34.03288,-118.38767},
//			{34.03162,-118.38985},
//			{34.03112,-118.39065},
//			{34.03091,-118.391},
//			{34.03012,-118.39241},
//			{34.02994,-118.39282},
//			{34.02966,-118.39352},
//			{34.02939,-118.39457},
//			{34.0293,-118.39506},
//			{34.02923,-118.39572},
//			{34.02918,-118.39624},
//			{34.02918,-118.39678},
//			{34.02921,-118.39723},
//			{34.02932,-118.3985},
//			{34.02972,-118.4031},
//			{34.03013,-118.40767},
//			{34.03023,-118.4088},
//			{34.03035,-118.40956},
//			{34.0305,-118.4102},
//			{34.03076,-118.41101},
//			{34.0312,-118.41222},
//			{34.03151,-118.41313},
//			{34.03168,-118.41381},
//			{34.03177,-118.4143},
//			{34.03187,-118.41512},
//			{34.03189,-118.4158},
//			{34.03189,-118.41752},
//			{34.03185,-118.41956},
//			{34.03184,-118.42186},
//			{34.03185,-118.42395},
//			{34.03185,-118.4246},
//			{34.03184,-118.42705},
//			{34.03183,-118.42853},
//			{34.03176,-118.43021},
//			{34.03164,-118.43138},
//			{34.03144,-118.4329},
//			{34.03134,-118.43362}, // intersect with 405
//			{34.03111,-118.43525},
//			{34.03094,-118.43656},
//			{34.0305,-118.43978},
//			{34.03037,-118.44071},
//			{34.03019,-118.44192},
//			{34.03004,-118.44272},
//			{34.03001,-118.44289},
//			{34.02971,-118.44417},
//			{34.02925,-118.44593},
//			{34.0288,-118.44758},
//			{34.02834,-118.44933},
//			{34.02796,-118.45074},
//			{34.02784,-118.4514},
//			{34.02779,-118.45189},
//			{34.02778,-118.45258},
//			{34.02785,-118.45418},
//			{34.02788,-118.4547},
//			{34.02788,-118.45515},
//			{34.02784,-118.45564},
//			{34.02778,-118.45605},
//			{34.02761,-118.45696},
//			{34.0272,-118.45896},
//			{34.02683,-118.46076},
//			{34.02641,-118.46255},
//			{34.0263,-118.46293},
//			{34.02613,-118.46358},
//			{34.02549,-118.46597},
//			{34.02457,-118.4694},
//			{34.02367,-118.47275},
//			{34.02326,-118.47413},
//			{34.02312,-118.47451},
//			{34.02304,-118.4747},
//			{34.02289,-118.47497},
//			{34.02239,-118.47591},
//			{34.02223,-118.47616},
//			{34.02152,-118.47714},
//			{34.02143,-118.47725},
//			{34.01958,-118.47948},
//			{34.01919,-118.47998},
//			{34.01882,-118.48044},
//			{34.01716,-118.48245},
//			{34.01687,-118.48283},
//			{34.01608,-118.48399},
//			{34.01461,-118.48633},
//			{34.01308,-118.48882},
//			{34.01284,-118.48936},
//			{34.01268,-118.48992},
//			{34.01264,-118.49016},
//			{34.01259,-118.49056},
//			{34.01252,-118.49183},
//			{34.01249,-118.4921},
//			{34.01243,-118.49238},
//			{34.01231,-118.49273},
//			{34.01221,-118.49295},
//			{34.01177,-118.49366},
//			{34.01164,-118.4939},
//			{34.01153,-118.49416},
//			{34.01147,-118.49438},
//			{34.01143,-118.49463},
//			{34.01142,-118.49519},
//			{34.01146,-118.49553},
//			{34.01156,-118.49598},
//			{34.01161,-118.49617},
//			{34.01174,-118.49642},
//			{34.0119,-118.49667},
//			{34.01277,-118.49764},
//			{34.013,-118.49788}};
//	Double coordinates405 [][] = {{33.96353,-118.37017},
//			{33.96336,-118.3706},
//			{33.96258,-118.37027},
//			{33.96225,-118.37018},
//			{33.96185,-118.37014},
//			{33.96158,-118.37013},
//			{33.96158,-118.37003},
//			{33.96158,-118.36963},
//			{33.96159,-118.36946},
//			{33.96159,-118.36924},
//			{33.9616,-118.3688},
//			{33.96154,-118.36857},
//			{33.96142,-118.36841},
//			{33.96133,-118.36837},
//			{33.96112,-118.36839},
//			{33.96092,-118.36847},
//			{33.96083,-118.36862},
//			{33.96081,-118.36882},
//			{33.9609,-118.36899},
//			{33.96095,-118.36903},
//			{33.96108,-118.36907},
//			{33.96156,-118.36913},
//			{33.96189,-118.36919},
//			{33.96241,-118.36931},
//			{33.9637,-118.36969},
//			{33.96385,-118.36973},
//			{33.96389,-118.36973},
//			{33.96451,-118.36993},
//			{33.96602,-118.37041},
//			{33.96607,-118.37043},
//			{33.96648,-118.37055},
//			{33.96688,-118.37079},
//			{33.96689,-118.3708},
//			{33.96695,-118.37084},
//			{33.9674,-118.37112},
//			{33.96775,-118.37133},
//			{33.96814,-118.37162},
//			{33.96858,-118.37201},
//			{33.96914,-118.37264},
//			{33.9697,-118.37351},
//			{33.97026,-118.37462},
//			{33.97192,-118.37797},
//			{33.97219,-118.37847},
//			{33.97392,-118.38147},
//			{33.97473,-118.38306},
//			{33.97517,-118.38396},
//			{33.97656,-118.38687},
//			{33.97842,-118.39081},
//			{33.97877,-118.39142},
//			{33.97909,-118.39191},
//			{33.9797,-118.39258},
//			{33.97983,-118.39268},
//			{33.98056,-118.39329},
//			{33.98212,-118.39454},
//			{33.9832,-118.39539},
//			{33.98731,-118.39866},
//			{33.98775,-118.39899},
//			{33.98874,-118.39976},
//			{33.9895,-118.40036},
//			{33.99228,-118.40249},
//			{33.99431,-118.40408},
//			{33.99498,-118.40464},
//			{33.99643,-118.40578},
//			{33.9987,-118.40758},
//			{34.00088,-118.4093},
//			{34.00153,-118.40979},
//			{34.00302,-118.41098},
//			{34.00338,-118.41126},
//			{34.00816,-118.41503},
//			{34.01219,-118.41823},
//			{34.01294,-118.41884},
//			{34.01639,-118.4215},
//			{34.01755,-118.42245},
//			{34.0184,-118.42313},
//			{34.02013,-118.42452},
//			{34.02466,-118.42812},
//			{34.02682,-118.4299},
//			{34.02832,-118.43107},
//			{34.02916,-118.43174},
//			{34.0311,-118.4333}, // intersect with 10
//			{34.0317,-118.43382}, // intersect with 10
//			{34.03232,-118.43452},
//			{34.03354,-118.43617},
//			{34.03384,-118.43654},
//			{34.03401,-118.43672},
//			{34.03433,-118.43702},
//			{34.03468,-118.43729},
//			{34.03494,-118.43747},
//			{34.03561,-118.43786},
//			{34.03589,-118.43803},
//			{34.03663,-118.43838},
//			{34.03768,-118.43889},
//			{34.03853,-118.43936},
//			{34.03908,-118.43975},
//			{34.04275,-118.44289},
//			{34.04475,-118.4446},
//			{34.04562,-118.44539},
//			{34.047,-118.44658},
//			{34.04851,-118.44784},
//			{34.04901,-118.44815},
//			{34.04919,-118.44831},
//			{34.04934,-118.4484},
//			{34.04981,-118.44863},
//			{34.05012,-118.44876},
//			{34.05103,-118.44905},
//			{34.05227,-118.44946},
//			{34.05286,-118.44973},
//			{34.05332,-118.45},
//			{34.05382,-118.45035},
//			{34.05553,-118.45181},
//			{34.05585,-118.45208},
//			{34.05696,-118.45302},
//			{34.05846,-118.45419},
//			{34.05997,-118.45536},
//			{34.06132,-118.45649},
//			{34.06457,-118.45928},
//			{34.06511,-118.45978},
//			{34.06574,-118.46032},
//			{34.06684,-118.46131},
//			{34.06943,-118.4636},
//			{34.0707,-118.46473},
//			{34.07167,-118.46555},
//			{34.07229,-118.46604},
//			{34.07289,-118.46652},
//			{34.0751,-118.46824},
//			{34.07547,-118.46853},
//			{34.07618,-118.46905},
//			{34.07734,-118.46997},
//			{34.07813,-118.47064},
//			{34.0793,-118.47179},
//			{34.08018,-118.4726},
//			{34.08067,-118.473},
//			{34.08124,-118.4734},
//			{34.08184,-118.47374},
//			{34.08246,-118.47404},
//			{34.08309,-118.47428},
//			{34.08373,-118.47447},
//			{34.08438,-118.4746},
//			{34.08712,-118.47499},
//			{34.09228,-118.47574},
//			{34.0938,-118.47593},
//			{34.09396,-118.47594},
//			{34.09463,-118.47603},
//			{34.0951,-118.47615},
//			{34.09543,-118.47626},
//			{34.09584,-118.47644},
//			{34.09675,-118.47698},
//			{34.09713,-118.47725},
//			{34.09774,-118.47756},
//			{34.09824,-118.47777},
//			{34.09913,-118.47794},
//			{34.09959,-118.47795},
//			{34.1001,-118.4779},
//			{34.10091,-118.47769},
//			{34.10197,-118.47737},
//			{34.10257,-118.47725},
//			{34.10295,-118.47721},
//			{34.10338,-118.47722},
//			{34.10385,-118.47726},
//			{34.10415,-118.47734},
//			{34.10452,-118.47745},
//			{34.105,-118.47765},
//			{34.10557,-118.47798},
//			{34.10998,-118.48123},
//			{34.11056,-118.48158},
//			{34.111,-118.48178},
//			{34.11136,-118.4819},
//			{34.112,-118.48202},
//			{34.11231,-118.48205},
//			{34.11282,-118.48203},
//			{34.11391,-118.48196},
//			{34.11656,-118.48177},
//			{34.11781,-118.48166},
//			{34.11834,-118.48154},
//			{34.11865,-118.48142},
//			{34.11901,-118.48125},
//			{34.11936,-118.48106},
//			{34.11969,-118.48082},
//			{34.12016,-118.48044},
//			{34.12067,-118.48003},
//			{34.1224,-118.47855},
//			{34.12406,-118.47725},
//			{34.12611,-118.47563},
//			{34.12637,-118.47539},
//			{34.12639,-118.47537},
//			{34.12712,-118.47479},
//			{34.12765,-118.47446},
//			{34.12855,-118.47401},
//			{34.12912,-118.4738},
//			{34.13102,-118.4732},
//			{34.13305,-118.47255},
//			{34.13494,-118.47192},
//			{34.13731,-118.47122},
//			{34.13787,-118.47111},
//			{34.13844,-118.47104},
//			{34.13901,-118.47102},
//			{34.13936,-118.47103},
//			{34.1412,-118.47127},
//			{34.14195,-118.47132},
//			{34.14269,-118.47132},
//			{34.14343,-118.47124},
//			{34.14404,-118.47114},
//			{34.14439,-118.47106},
//			{34.14511,-118.47086},
//			{34.14572,-118.47063},
//			{34.14621,-118.47041},
//			{34.14835,-118.4693},
//			{34.14921,-118.46883},
//			{34.14971,-118.46858},
//			{34.15014,-118.46842},
//			{34.15071,-118.46821},
//			{34.15092,-118.46816},
//			{34.15147,-118.46804},
//			{34.15227,-118.46794},
//			{34.1527,-118.4679},
//			{34.15326,-118.4679},
//			{34.15369,-118.46793},
//			{34.15429,-118.46801},
//			{34.15521,-118.46825},
//			{34.15752,-118.46907},
//			{34.15836,-118.46928},
//			{34.15893,-118.46935},
//			{34.15977,-118.4694},
//			{34.16092,-118.46942}, // intersect with 101
//			{34.16304,-118.46943},
//			{34.16482,-118.46948},
//			{34.1655,-118.46949},
//			{34.16606,-118.46942},
//			{34.16652,-118.46931},
//			{34.1668,-118.46922},
//			{34.16733,-118.46899},
//			{34.1689,-118.46807},
//			{34.16973,-118.46774},
//			{34.17022,-118.4676},
//			{34.1708,-118.46752},
//			{34.17126,-118.46751},
//			{34.17212,-118.46752},
//			{34.17463,-118.46759},
//			{34.17535,-118.46768},
//			{34.17593,-118.46782},
//			{34.17688,-118.46814},
//			{34.17757,-118.46845},
//			{34.17816,-118.46878},
//			{34.17846,-118.46896},
//			{34.17926,-118.46954},
//			{34.18404,-118.47306},
//			{34.18447,-118.47331},
//			{34.1849,-118.47353},
//			{34.18531,-118.4737},
//			{34.18555,-118.47378},
//			{34.18582,-118.47387},
//			{34.18664,-118.47402},
//			{34.18692,-118.47405},
//			{34.19359,-118.4742},
//			{34.19444,-118.4742},
//			{34.19628,-118.47421},
//			{34.19707,-118.47417},
//			{34.19747,-118.47413},
//			{34.19802,-118.47402},
//			{34.20049,-118.47349},
//			{34.20112,-118.47339},
//			{34.20147,-118.47334},
//			{34.20206,-118.4733},
//			{34.20422,-118.47327},
//			{34.20824,-118.4732},
//			{34.21623,-118.47313},
//			{34.22169,-118.47306},
//			{34.22831,-118.47302},
//			{34.23254,-118.47297},
//			{34.23575,-118.47294},
//			{34.23741,-118.47291},
//			{34.23938,-118.47289},
//			{34.24303,-118.47287},
//			{34.24509,-118.47286},
//			{34.2458,-118.4728},
//			{34.24633,-118.47272},
//			{34.24715,-118.47254},
//			{34.24843,-118.47225},
//			{34.24914,-118.47216},
//			{34.2501,-118.47214},
//			{34.25615,-118.47212},
//			{34.25911,-118.4721},
//			{34.26221,-118.4721},
//			{34.26377,-118.47206},
//			{34.26458,-118.47203},
//			{34.26587,-118.47204},
//			{34.26614,-118.47204},
//			{34.26807,-118.472},
//			{34.26932,-118.47201},
//			{34.27119,-118.472},
//			{34.27184,-118.47194},
//			{34.27239,-118.47185},
//			{34.27338,-118.47162},
//			{34.27434,-118.47131},
//			{34.27543,-118.4709},
//			{34.27597,-118.47071},
//			{34.27862,-118.46979},
//			{34.28084,-118.46914},
//			{34.28341,-118.46854},
//			{34.28636,-118.46787},
//			{34.28835,-118.46744},
//			{34.28881,-118.46739},
//			{34.28927,-118.46738},
//			{34.28953,-118.46739},
//			{34.28997,-118.46743},
//			{34.2905,-118.46755},
//			{34.29113,-118.46773},
//			{34.29148,-118.46784},
//			{34.29184,-118.46803},
//			{34.29229,-118.4683},
//			{34.2929,-118.46876},
//			{34.29336,-118.46918},
//			{34.29359,-118.46941},
//			{34.29588,-118.47197}};

