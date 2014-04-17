package butter.usc.edu;

import java.util.ArrayList;
import java.util.Stack;

public class DFSGraph {
	public Node root;
	public ArrayList<Node> nodeList;
	public int[][] adjacent;
	public int size;
	
	DFSGraph(Node n){
		setRoot(n);
		nodeList = new ArrayList<Node>();
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
		if (index != -1){
			for (int j=0; j < size; j++){	
				if(adjacent[index][j] == 1 && nodeList.get(j).visited == false){
					return nodeList.get(j);
				}
			}
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
			Node n = stack.peek();
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
