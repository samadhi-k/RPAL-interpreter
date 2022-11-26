package tree_generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ASTMaker {
	
	public static ASTree buildTree() {
		
		ArrayList<String> ast = readFromFile();
		String head_content = ast.get(0);
		Node root = new Node(head_content, null, 0);
		
		ASTree astree = new ASTree(root);
		
		Node current_node = root;
		
		//iterate over the array from file
		for (int i = 1; i < ast.size(); i++) {
			
			String str = ast.get(i);
			
			//iterate over a string to find the depth using '.' count
			int depth_in_array = 0;
			String content;
			
			for (int j = 0; j < str.length(); j++) {
				if(str.charAt(j) != '.') {
					depth_in_array = j;
					break;
				}	
			}
			
			content = str.substring(depth_in_array);
			Node new_node = new Node(content, null, depth_in_array);
			
			insertNewNode(current_node, new_node);
			
			current_node = new_node;
		}
		
		
		System.out.println(astree);
		return astree;
		
	}
	
	
	public static void main(String[] args) {
		ASTree x = buildTree();
	}
	
	private static Boolean insertNewNode( Node current_node, Node new_node) {
		
		int current_tree_depth = current_node.getDepth();
		int depth_in_array = new_node.getDepth();

		if(depth_in_array - current_tree_depth == 1) {
			current_node.addChild(new_node);
			new_node.setParent(current_node);
		}
		else {
			current_node = current_node.getParent();
			insertNewNode(current_node, new_node);
		}
				
		return false;	
	}
	
	private static ArrayList<String> readFromFile(){
		
		try {
			File inputFile = new File("C:\\Users\\Samadhi\\eclipse-workspace\\RPAL interpreter\\src\\main\\input.txt");
			Scanner reader = new Scanner(inputFile);
			ArrayList<String> ast = new ArrayList<>();
			while(reader.hasNextLine()) {
				ast.add(reader.nextLine());
			}
			System.out.println(ast);
			reader.close();
			return ast;
			
		} catch (FileNotFoundException e) {
			System.out.println(e);
			
		}
		return null;
		
	}
}
