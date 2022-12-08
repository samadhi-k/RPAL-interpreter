package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import cse_machine.CSEMachine;
import tree_generator.ASTMaker;
import tree_generator.ASTree;
import tree_generator.Node;
import standardize.Standardizer;

public class Main {

	public static void main(String[] args) {
		
		//get array from  file
		ArrayList<String> ast = readFromFile(args[0]);
		
		//build the AST
		ASTree astree = ASTMaker.buildTree(ast);
		
		//standardize the tree
		Standardizer std = new Standardizer();
		std.standardize(astree.getRoot());	

		//execute the program 
		CSEMachine cse = new CSEMachine();
		cse.execute(astree.getRoot());
			
		
	}
	
	/*
	 * read the text file
	 */
	
	private static ArrayList<String> readFromFile(String filename){
		
		try {
			File inputFile = new File(filename);
			Scanner reader = new Scanner(inputFile);
			ArrayList<String> ast = new ArrayList<>();
			
			while(reader.hasNextLine()) {
				ast.add(reader.nextLine());
			}
			
			reader.close();
			return ast;
			
		} catch (FileNotFoundException e) {
			System.out.println(e);
			
		}
		return null;
		
	}

}
