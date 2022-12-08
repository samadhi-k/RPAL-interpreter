package standardize;

import tree_generator.*;
import java.util.ArrayList;
import java.util.List;


public class Standardizer {
	
	/*
	 * standardize the AST and generate ST
	 */
	
	public void standardize(Node node) {
		
		ArrayList<Node> children = node.getChildren();
		for (int i = 0; i < children.size(); i++) {
				standardize(children.get(i));
		}
		
		String content = node.getContent();
		switch (content) {
	
		case "where" :{
			this.stdWhere(node);
			break;
		}
		case "let" : {
			this.stdLet(node);
			break;
		}	
		case "rec" : {
			this.stdRec(node);
			break;
		}
		case "within" : {
			this.stdWithin(node);
			break;
		}
		case "function_form" : {
			this.stdFcnForm(node);
			break;
		}
		case "lambda" : {
			this.stdLambda(node);
			break;
		}
		case "@" : {
			this.stdAt(node);
			break;
		}
		case "and" : {
			this.stdAnd(node);
			break;
		}
		}
	
}
	
	/*
	 @param Node node
	 
	 standardize where clause
	 */
	
	private void stdWhere(Node node) {
		
		Node p = node.getChildren().get(0);
		Node eq = node.getChildren().get(1);
		
		Node x = eq.getChildren().get(0);
		Node e = eq.getChildren().get(1);
		
		int headDepth = node.getDepth();
		Node lambda = new Node("lambda", node, headDepth+1);
		
		node.setContent("gamma");
		node.addChildtoPosition(lambda, 0);
		node.addChildtoPosition(e,1);
		
		e.setDepth(headDepth+1);
		e.setParent(node);
		
		lambda.addChild(x);
		lambda.addChild(p);
		
		x.setParent(lambda);
		x.setDepth(headDepth+2);
		
		p.setParent(lambda);
		p.setDepth(headDepth+2);
		
	}
	
	/*
	 * standardize let node
	 */
	
	private void stdLet(Node node) {
		
		Node eq = node.getChildren().get(0);
		Node p = node.getChildren().get(1);
		
		Node x = eq.getChildren().get(0);
		Node e = eq.getChildren().get(1);
		
		int headDepth = node.getDepth();
		
		Node lambda = new Node("lambda", node, headDepth+1);
		
		node.setContent("gamma");
		node.addChildtoPosition(lambda, 0);
		node.addChildtoPosition(e, 1);
		
		e.setDepth(headDepth+1);
		e.setParent(node);
		
		lambda.addChild(x);
		lambda.addChild(p);
		
		x.setParent(lambda);
		x.setDepth(headDepth+2);
		
		p.setParent(lambda);
		p.setDepth(headDepth+2);
		
		
	}
	
	/*
	 * standardize rec node
	 */
	private void stdRec(Node node) {
		
		ArrayList<Node> children = node.getChildren();
		
		Node eq = children.get(0);
		
		node.setChildren(new ArrayList<Node>());
		
		Node x = eq.getChildren().get(0);
		Node e = eq.getChildren().get(1);
		
		int headDepth = node.getDepth();
		
		Node gamma = new Node("gamma", node, headDepth+1);
		Node ystar = new Node("ystar", gamma, headDepth+2);
		Node lambda = new Node("lambda", gamma, headDepth+2);
		
		node.setContent("=");
		
		Node x1 = new Node(x.getContent(), node, headDepth+1);
		x1.setChildren(x.getChildren());
		
		node.addChild(x1);
		node.addChild(gamma);
		
		gamma.addChild(ystar);
		gamma.addChild(lambda);
		
		Node x2 = new Node(x.getContent(), lambda, headDepth+3);
		x2.setChildren(x.getChildren());
		
		lambda.addChild(x2);
		lambda.addChild(e);
		
		e.setDepth(headDepth+3);
		e.setParent(lambda);
		
	}
	
	/*
	 * standardize lambda node
	 */
	private void stdLambda(Node node) {
		
		ArrayList<Node> children = node.getChildren();
		
		Node e = children.get(children.size()-1);
		
		ArrayList<Node> arguments = new ArrayList<>();
		for (int j = 0; j < children.size() - 1; j++) {
			arguments.add(children.get(j));
		}
		
		node.setChildren(new ArrayList<Node>());
		
		Node n = node;
		
		for (int i = 0; i < arguments.size()-1; i++) {
			
			Node v = arguments.get(i);
			Node lambda = new Node("lambda", n, n.getDepth()+1 );
			n.addChild(v);
			n.addChild(lambda);
			
			v.setDepth(n.getDepth()+1);
			v.setParent(n);
			
			n = lambda;
			
		}
		Node x = arguments.get(arguments.size()-1);
		n.addChild(x);
		n.addChild(e);
		
		x.setParent(n);
		x.setDepth(n.getDepth()+1);
		e.setDepth(n.getDepth()+1);
		e.setParent(n);
		
		
	}
	
	/*
	 * standardize within node
	 */
	private void stdWithin(Node node) {
		
		ArrayList<Node> children = node.getChildren();
		
		Node eq1 = children.get(0);
		Node eq2 = children.get(1);
		
		Node x1 = eq1.getChildren().get(0);
		Node e1 = eq1.getChildren().get(1);
		
		Node x2 = eq2.getChildren().get(0);
		Node e2 = eq2.getChildren().get(1);
		
		node.setChildren(new ArrayList<Node>());
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
		
		x1.setParent(lambda);
		x1.setDepth(lambda.getDepth()+1);
		
		e2.setParent(lambda);
		e2.setDepth(lambda.getDepth()+1);
		
	}

	/*
	 * standardize function form
	 */
	private void stdFcnForm(Node node) {
		
		ArrayList<Node> children = node.getChildren();
		
		Node f = children.get(0);
		Node e = children.get(children.size()-1);
		
		ArrayList<Node> arguments = new ArrayList<>();
		for (int j = 1; j < children.size() - 1; j++) {
			arguments.add(children.get(j));
		}
		
		node.setChildren(new ArrayList<Node>());
		node.addChild(f);
		node.setContent("=");
		
		Node n = node;
		
		for (int i = 0; i < arguments.size() ; i++) {
			
			Node v = arguments.get(i);
			Node lambda = new Node("lambda", n, n.getDepth()+1 );
			n.addChild(lambda);
			
			lambda.addChild(v);
			v.setDepth(lambda.getDepth()+1);
			v.setParent(lambda);
			
			n = lambda;
			
		}
		
		n.addChild(e);
		
		e.setDepth(n.getDepth()+1);
		e.setParent(n);
	
	}

	/*
	 * standardize @ node
	 */
	private void stdAt(Node node) {
		
		ArrayList<Node> children = node.getChildren();
	
		Node e1 = children.get(0);
		Node n = children.get(1);
		Node e2 = children.get(2);
		
		node.setChildren(new ArrayList<Node>());
		int headDepth = node.getDepth();
		
		Node gamma = new Node("gamma", node, headDepth+1);
		
		node.setContent("gamma");
		node.addChild(gamma);
		node.addChild(e2);
		
		e2.setDepth(headDepth+1);
		e2.setParent(node);
		
		gamma.addChild(n);
		gamma.addChild(e1);
		
		n.setDepth(headDepth+2);
		n.setParent(gamma);
		
		e1.setDepth(headDepth+2);
		e1.setParent(gamma);
		
	}

	/*
	 * standardize and node
	 */
	private void stdAnd(Node node) {
		
		ArrayList<Node> children = node.getChildren();
		int headDepth = node.getDepth();
		
		node.setChildren(new ArrayList<Node>());
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
