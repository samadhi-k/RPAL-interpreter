package tree_generator;

public class ASTree {
	
	private Node root;
	
	//construct AS tree with given array
	ASTree (Node root ){
		this.setRoot(root);
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}
	
	
	
}
