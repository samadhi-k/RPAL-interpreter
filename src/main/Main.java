package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		try {
			File input_file = new File("C:\\Users\\Samadhi\\eclipse-workspace\\RPAL interpreter\\src\\main\\input.txt");
			Scanner reader = new Scanner(input_file);
			ArrayList<String> ast = new ArrayList<>();
			while(reader.hasNextLine()) {
				ast.add(reader.nextLine());
			}
			System.out.println(ast);
			reader.close();
			
		} catch (FileNotFoundException e) {
			System.out.println(e);
			
		}
	}

}
