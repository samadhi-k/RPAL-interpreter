package tree_generator;

import java.util.ArrayList;

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
	
	public ArrayList<Node> preOrderTraverse(Node node, ArrayList<Node> pot) {
		node.getChildren().forEach((child) -> preOrderTraverse(child,pot));
		pot.add(node);
		return pot;
	}
	
	private void preOrderTraversetoPrint(Node node,int i) {
        for (int n = 0; n < i; n++) {System.out.print(".");}
        System.out.println(node.getContent());
        node.getChildren().forEach((child) -> preOrderTraversetoPrint(child, i+1));
    }
    
    public void printAst() {
        this.preOrderTraversetoPrint(root, 0);
    }
	
	
	
}
