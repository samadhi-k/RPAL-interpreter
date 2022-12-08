package tree_generator;

import java.util.ArrayList;

public class Node{
	
	private ArrayList<Node> children = new ArrayList<>();
	private Node parent;
	private String content;
	private int depth;
	
	//used only for lambda
	private int index = 0;
	private String var= null;
	private int ce = 0;
	
	//used only for tau
	private int len;
	
	//used for conditional 
	private int conditionIndex;
	
	
	public Node( String content, Node parent, int depth) {
		this.content = content;
		this.parent = parent;
		this.depth = depth;
		this.children = new ArrayList<>();		
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
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
	
	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public void addChild( Node child ) {
		this.children.add(child);
	}
	
	public void addChildtoPosition(Node child, int position) {
		this.children.set(position, child);
	}

	public int getCe() {
		return ce;
	}

	public void setCe(int ce) {
		this.ce = ce;
	}

	public int getConditionIndex() {
		return conditionIndex;
	}

	public void setConditionIndex(int conditionIndex) {
		this.conditionIndex = conditionIndex;
	}
	

	
	
	
}
