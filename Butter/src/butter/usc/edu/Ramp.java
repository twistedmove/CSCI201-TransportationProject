package butter.usc.edu;

/*
 *  Ramp class, creates a ramp given the name, freeway number, and the index of the coordinate in the respective array of path coordinates. 
 *  @author Kaitlyn
 */
public class Ramp {
	String name, branchName;
	int freeway;
	int indexOfCoordinate;
	Location l;
	Ramp previous;
	Ramp next;
	Ramp branch;
	
	public Ramp(String name, int freeway, int indexOfCoordinate, Location l) {
		this.name = name;
		this.freeway = freeway;
		this.indexOfCoordinate = indexOfCoordinate;
		this.l = l;
		this.previous = null;
		this.next = null;
	}
	
	public Ramp(String name, String branchName, int index, int freeway) {
		this.name = name;
		this.branchName = branchName;
		this.freeway = freeway;
		this.indexOfCoordinate = index;
		this.l = null;
		this.previous = null;
		this.next = null;
	}

	public int getIndexOfCoordinate() {
		return indexOfCoordinate;
	}
	
	public void setIndexOfCoordinate(int index) {
		this.indexOfCoordinate = index;
	}
	
	public void setPreviousRamp(Ramp prev) {
		this.previous = prev;
		if(prev != null) prev.next = this;
	}
	
	public void setBranchRamp(Ramp branch) {
		this.branch = branch;
	}
	
	public Ramp getNext() {
		return next;
	}
	
	public Ramp getPrevious() {
		return previous;
	}
	
	public Location getLocation(){
		return l;
	}
	
	public void setLocation(Location l) {
		this.l = l;
	}
	
}
