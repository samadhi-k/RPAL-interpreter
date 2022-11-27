package tree_generator;

import java.util.ArrayList;

public class Node {
	
	private ArrayList<Node> children = new ArrayList<>();
	private Node parent;
	private String content;
	private int depth;
	
	public Node( String content, Node parent, int depth) {
		this.content = content;
		this.parent = parent;
		this.depth = depth;
		this.children = new ArrayList<>();
			
	}
	
	//Getters and Setters 
	
	public ArrayList<Node> getChildren() {
		return children;
	}
	
	public void setChildren(ArrayList<Node> nextNodes) {
		this.children = nextNodes;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	//add children to nodes
	
	public void addChild( Node child ) {
		this.children.add(child);
	}

	
	
	
}
