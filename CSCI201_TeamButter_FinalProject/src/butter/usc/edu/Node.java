package butter.usc.edu;

public class Node {
	public Location loc;
	public boolean visited = false;
	public Node(Location location){
		this.loc = location;
	}
	
	public Location getLocation(){
		return loc;
	}
	
	public boolean getVisited(){
		return visited;
	}
}
