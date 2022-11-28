package standardize;

import tree_generator.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Standardizer {
	
	
	public void standardize(ASTree ast) {
		
		//navigate to bottom
		
		//change
	
	}
	
	private void stdWhere(Node node) {
		
		Node p = node.getChildren().get(0);
		Node eq = node.getChildren().get(1);
		node.removeChildren();
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
		node.removeChildren();
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
		node.removeChildren();
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
		
		ArrayList<Node> children = node.getChildren();
		node.removeChildren();
		
		Node e = children.get(children.size()-1);
		
		ArrayList<Node> arguments = (ArrayList<Node>) children.subList(0, children.size()-1);
		
		Node n = node;
		
		for (int i = 0; i < arguments.size(); i++) {
			
			Node v = arguments.get(i);
			Node lambda = new Node("lambda", n, n.getDepth()+1 );
			n.addChild(lambda);
			
			v.setDepth(lambda.getDepth()+1);
			v.setParent(lambda);
			
			lambda.addChild(v);
			
			n = lambda;
			
		}
		
		n.addChild(e);
		
		e.setDepth(n.getDepth()+1);
		e.setParent(n);
		
	}
	
	private void stdWithin(Node node) {
		
		Node eq1 = node.getChildren().get(0);
		Node eq2 = node.getChildren().get(1);
		
		Node x1 = eq1.getChildren().get(0);
		Node e1 = eq1.getChildren().get(1);
		
		Node x2 = eq2.getChildren().get(0);
		Node e2 = eq2.getChildren().get(1);
		
		node.removeChildren();
		node.setContent("=");
		node.addChild(x2);
		x2.setParent(node);
		x2.setDepth(node.getDepth()+1);
		
		Node gamma = new Node("gamma", node, node.getDepth()+1);
		node.addChild(gamma);
		
		Node lambda = new Node("lambda", gamma, gamma.getDepth()+1);
		gamma.addChild(lambda);
		gamma.addChild(e1);
		e1.setParent(gamma);
		e1.setDepth(gamma.getDepth()+ 1);
		
		lambda.addChild(x1);
		lambda.addChild(e2);
		
	}

	private void stdFcnForm(Node node) {
		
		ArrayList<Node> children = node.getChildren();
		node.removeChildren();
	
		Node p = children.get(0);
		Node e = children.get(children.size()-1);
		
		ArrayList<Node> arguments = (ArrayList<Node>) children.subList(1, children.size()-1);
		
		node.setContent("=");
		
		Node n = node;
		
		for (int i = 0; i < arguments.size(); i++) {
			
			Node v = arguments.get(i);
			Node lambda = new Node("lambda", n, n.getDepth()+1 );
			n.addChild(lambda);
			
			v.setDepth(lambda.getDepth()+1);
			v.setParent(lambda);
			
			lambda.addChild(v);
			
			n = lambda;
			
		}
		
		n.addChild(e);
		
		e.setDepth(n.getDepth()+1);
		e.setParent(n);
		
	}

	private void stdAt(Node node) {
	
		Node e1 = node.getChildren().get(0);
		Node n = node.getChildren().get(1);
		Node e2 = node.getChildren().get(2);
		
		node.removeChildren();
		int headDepth = node.getDepth();
		
		Node gamma2 = new Node("gamma", node, headDepth+1);
		
		node.setContent("gamma");
		node.addChild(gamma2);
		node.addChild(e2);
		
		e2.setDepth(headDepth+1);
		e2.setParent(node);
		
		gamma2.addChild(n);
		gamma2.addChild(e1);
		
		n.setDepth(headDepth+2);
		n.setParent(gamma2);
		
		e1.setDepth(headDepth+2);
		e1.setParent(gamma2);
		
	}

	private void stdAnd(Node node) {
		
		ArrayList<Node> children = node.getChildren();
		int headDepth = node.getDepth();
		node.removeChildren();
		node.setContent("=");
		
		Node comma = new Node(",", node, headDepth+1);
		Node tau = new Node("tau", node, headDepth+1);
		
		node.addChild(comma);
		node.addChild(tau);
		
		for (int i = 0; i < children.size(); i++) {
			
			Node eq = children.get(i);
			Node x = eq.getChildren().get(0);
			Node e = eq.getChildren().get(1);
			
			x.setParent(comma);
			x.setDepth(headDepth+2);
			e.setParent(tau);
			e.setDepth(headDepth+2);
			
			comma.addChild(x);
			tau.addChild(e);
		}
	
		
	}
	
	

	
 
}
