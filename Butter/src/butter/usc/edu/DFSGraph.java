package butter.usc.edu;

import java.util.ArrayList;
import java.util.Stack;

public class DFSGraph {
	public Node root;
	public ArrayList<Node> nodeList;
	public int[][] adjacent;
	public int size;
	
	DFSGraph(){
		nodeList = new ArrayList<Node>();
	}
	
	public void setInitialRoot(Node n){
		for (int i =0;i<nodeList.size(); i++){
			if (n.getLocation().getLatitude() == nodeList.get(i).getLocation().getLatitude() && n.getLocation().getLongitude() == nodeList.get(i).getLocation().getLongitude()){
				this.root = nodeList.get(i);
			}
		}
	}
	
	public void setRoot(Node n){
		this.root = n;
	}
	
	public Node getRoot(){
		return root;
	}
	
	public void addNode(Node n){
		nodeList.add(n);
	}
	
	public void connectNode(Node a, Node b){
		if (adjacent == null){
			size = nodeList.size();
			adjacent = new int[size][size];
		}
		
		int startIndex = nodeList.indexOf(a);
		int endIndex = nodeList.indexOf(b);
		adjacent[startIndex][endIndex] = 1;
		adjacent[endIndex][startIndex] = 1;
	}
	
	public Node getChildNode(Node n){
		int index = nodeList.indexOf(n);
		int j=0;
		if (index != -1){
			System.out.println("What");
			while(j<size){
				if (adjacent[index][j] == 1 && ((Node)nodeList.get(j)).visited == false){
					return (Node)nodeList.get(j);
				}
				j++;
			}
		} else if (index == -1){
			System.out.println("NEGATIVE ONE");
		}
		return null;
	}
	
	public void resetNodeVisited(){
		for (int i=0; i<size; i++){
			Node n = nodeList.get(i);
			n.visited = false;
		}
	}
	
	public void depthfirstsearch(){
		Stack<Node> stack = new Stack<Node>();
		stack.push(root);
		root.visited = true;
		while(!stack.isEmpty()){
			Node n = (Node)stack.peek();
			Node child = getChildNode(n);
			if(child != null){
				child.visited=true;
				stack.push(child);
			}
			else{
				stack.pop();
			}
		}
		resetNodeVisited();
	}
	
	
	
}
