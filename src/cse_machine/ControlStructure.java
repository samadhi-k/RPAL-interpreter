package cse_machine;

import java.util.ArrayList;
import tree_generator.ASTree;
import tree_generator.Node;

public class ControlStructure {
	
	private ArrayList<Node> arr;
	private int index;
	
	public ControlStructure(int index) {
		this.setIndex(index);
		this.arr = new ArrayList<Node>();
	}
		
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public ArrayList<Node> getDelta() {
		return arr;
	}

	public void setDelta(ArrayList<Node> arr) {
		this.arr = arr;
	}
	
	public void addtoDelta(Node node) {
		this.arr.add(node);
	}
	
	
}
