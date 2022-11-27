package standardize;

import tree_generator.*;

public class Standardizer {
	
	public void standardize(ASTree ast) {
		
		//navigate to bottom
		
		//change
	
	}
	
	private void stdWhere(Node node) {
		
		Node p = node.getChildren().get(0);
		Node eq = node.getChildren().get(1);
		Node x = eq.getChildren().get(0);
		Node e = eq.getChildren().get(1);
		
		int headDepth = node.getDepth();
		Node lambda = new Node("lambda", node, headDepth+1);
		
		node.setContent("gamma");
		node.addChild(lambda);
		node.addChild(e);
		
		e.setDepth(headDepth+1);
		e.setParent(node);
		
		lambda.addChild(x);
		lambda.addChild(p);
		
		x.setParent(lambda);
		x.setDepth(headDepth+2);
		
		p.setParent(lambda);
		p.setDepth(headDepth+2);
	}
	
	private void stdLet(Node node) {
		
		Node eq = node.getChildren().get(0);
		Node p = node.getChildren().get(1);
		Node x = eq.getChildren().get(0);
		Node e = eq.getChildren().get(1);
		
		int headDepth = node.getDepth();
		
		Node lambda = new Node("lambda", node, headDepth+1);
		
		node.setContent("gamma");
		node.addChild(lambda);
		node.addChild(e);
		
		e.setDepth(headDepth+1);
		e.setParent(node);
		
		lambda.addChild(x);
		lambda.addChild(p);
		
		x.setParent(lambda);
		x.setDepth(headDepth+2);
		
		p.setParent(lambda);
		p.setDepth(headDepth+2);
		
	}
	
	private void stdRec(Node node) {
		
		Node eq = node.getChildren().get(0);
		Node x1 = eq.getChildren().get(0);
		Node x2 = eq.getChildren().get(0);
		Node e = eq.getChildren().get(1);
		
		int headDepth = node.getDepth();
		
		Node gamma = new Node("gamma", node, headDepth+1);
		Node ystar = new Node("ystar", gamma, headDepth+2);
		Node lambda = new Node("lambda", gamma, headDepth+2);
		
		node.setContent("=");
		node.addChild(x1);
		node.addChild(gamma);
		
		x1.setDepth(headDepth+1);
		x1.setParent(node);
		
		gamma.addChild(ystar);
		gamma.addChild(lambda);
		
		lambda.addChild(x2);
		lambda.addChild(e);
		
		x2.setDepth(headDepth+3);
		x2.setParent(lambda);
		
		e.setDepth(headDepth+3);
		e.setParent(lambda);
		
	}
	
	private void stdLambda(Node node) {
		
	}
	
	private void stdWithin(Node node) {
		
	}

	private void stdFcnForm(Node node) {
	
	}

	private void stdAt(Node node) {
	
	}

	private void stdAnd(Node node) {
	
	}

	
 
}
