package cse_machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import tree_generator.Node;

public class CSEMachine {

	ArrayList<ControlStructure> deltaArr = new ArrayList<>();
	ArrayList<ControlStructure> conditionalDelta = new ArrayList<>();
	ControlStructure currentcs;
	Environment currentenv;
	Stack<Node> stack = new Stack<Node>();
	ArrayList<Environment> env = new ArrayList<>();
	HashMap<String, String> rators = new HashMap<>();
	
	public CSEMachine() {
		
		this.deltaArr = new ArrayList<ControlStructure>();
		this.currentcs = new ControlStructure(0);
		deltaArr.add(currentcs);
		
		rators.put("+", "bop");
		rators.put("-", "bop");
		rators.put("*", "bop");
		rators.put("/", "bop");
		rators.put("**", "bop");
		rators.put("eq", "bop");
		rators.put("ne", "bop");
		rators.put("ls", "bop");
		rators.put("le", "bop");
		rators.put("gr", "bop");
		rators.put("ge", "bop");
		rators.put("<", "bop");
		rators.put(">", "bop");
		rators.put("<=", "bop");
		rators.put(">=", "bop");
		rators.put("or", "bop");
		rators.put("&", "bop");
		rators.put("not", "uop");
		rators.put("neg", "uop");
	}
	
	public void execute(Node node) {
 		
		this.buildControlStructure(node, currentcs);
		
		PrimitiveEnvironment pe = new PrimitiveEnvironment();
		env.add(pe);
		currentenv = pe ;
		Node p = new Node("pe", null, 0);
		stack.push(p);
		
		ControlStructure controlstructure = deltaArr.get(0);
		ArrayList<Node> cs = controlstructure.getDelta();

		cs.add(0, p);
		
		while (!controlstructure.getDelta().isEmpty()) {
			
			Node current_node = cs.get(cs.size()-1);
			cs.remove(cs.size()-1);
			
			//rule 1
			if(current_node.getContent().startsWith("<ID:")){
				
				
				String x = current_node.getContent().substring(4, current_node.getContent().length()-1);
				String o = LookUp(x, currentenv);
				if(o == null) {
					System.out.println("Error: undefined variable " + x);
					break;
				}
				else if (o.contentEquals("defined")) {
					
					stack.push(current_node);					
							
				}
				else {
					Node n = new Node(o, null, 0);
					stack.push(n);
				}
			}
			
			//rule 2
			else if (current_node.getContent().contains("lambda") ) {
				
				int x = currentenv.getIndex();
				current_node.setCe(x);
				stack.push(current_node);				
			}
			
			//rule 3 and 4
			else if(current_node.getContent().equalsIgnoreCase("gamma")) {
				
				
				Node rt = (Node) stack.pop();
				Node rd = (Node) stack.pop();
				String content = rd.getContent();
				String rand1;
				
				if(rt.getContent().startsWith("<ID:")) {
					String x = rt.getContent().substring(4, rt.getContent().length()-1);
					String op = carryInBuilt(x,content);
					Node new_node = new Node(op, null, 0);
					stack.push(new_node);
					continue;
				}
				
				if(content.startsWith("<INT:")) {
					//integer
					rand1 = content.substring(5, content.length()-1);
				} else if(content.startsWith("<STR:")) {
					//string
					rand1 = content.substring(6, content.length()-2);
				}else if (content.startsWith("<")){
					//boolean
					rand1 = content.substring(1, content.length()-1);
				}
				else {
					//lambda
					rand1 = content;
				}
				
				//rule 4
				if(rt.getContent().contains("lambda")) {
					
					
					Environment e = new Environment(currentenv);
					currentenv = e;
					env.add(e);
					
					Node n = new Node("e", null, 0);
					
					cs.add(n);
					
					if(rt.getIndex() != 0) {						
						cs.addAll(deltaArr.get(rt.getIndex()).getDelta());
					}else {
						char x = rt.getContent().charAt(rt.getContent().length()-1);
						int y = Character.getNumericValue(x);
						cs.addAll(deltaArr.get(y).getDelta());
					}
					
					stack.push(n);
					String v = rt.getVar() != null ? rt.getVar() : rt.getContent().substring(10, rt.getContent().indexOf(">"));
					e.addName(v, content) ;
					
						
				}
				
				else if (rt.getContent() == "ystar") {
					String i = rd.getContent();
					if(i.contains("lambda")) {
						String j = "eta<ID:" + rd.getVar() + ">-" + rd.getIndex(); 
						rd.setContent(j);
						stack.push(rd);
					}
				}
				else if(rt.getContent().contains("eta")) {
					cs.add(new Node("gamma", null, 0));
					cs.add(new Node("gamma", null, 0));
					stack.push(rd);
					stack.push(rt);
					String j = "lambda<ID:" + rt.getVar() + ">-" + rt.getIndex(); 
					stack.push(new Node(j, null, 0));
				}
				
				//rule 3
				else{
					String rator = rt.getContent();
					
					if(rators.get(rt.getContent()) == null ) {
						System.out.println("Error: Illeagal operation");
						break;
					}
					
					if(rators.get(rt.getContent()).equalsIgnoreCase("bop" )) {

						Node rd2 = (Node) stack.pop();
						String content2 = rd2.getContent();
						
						String x = carryBOP(rator, rand1, content2);
						if(x == null) break;
						Node n = new Node(x,null, 0 );
						stack.push(n);
					}
					
					else if (rators.get(rt.getContent()).equalsIgnoreCase("uop")) {
						
						String x = carryUOP(rator, content);
						if(x == null) break;
						Node n = new Node(x,null, 0 );
						stack.push(n);
						
					}
					
					else {
						
					}
				}
				
			}
			
			//rule 5
			else if(current_node.getContent().equalsIgnoreCase("e") || current_node.getContent().equalsIgnoreCase("pe")) {
				Node e = (Node) stack.get(stack.size()-2);
				
				if(e.getContent().equalsIgnoreCase("e" )) {
					stack.remove(e);
					cs.remove(current_node);
				}
				else if (e.getContent().equalsIgnoreCase("pe")) {
					stack.remove(e);
					cs.remove(current_node);
					String output = stack.get(0).getContent();
					String o;
					if( output == null) {
						System.out.println("Error: stack dump");
						break;
					}
					else if(output.startsWith("<STR:") || output.startsWith("<INT:"))  o = output.substring(5, output.length()-1);
					else o = output.substring(1, output.length()-1);
					System.out.println(o);
					break;
				}
				
			}
			
			else if (rators.containsKey(current_node.getContent())) {
				String rator = current_node.getContent();
				
				if(rators.get(current_node.getContent()).equalsIgnoreCase("bop")) {
					
					Node rd1 = (Node) stack.pop();
					String content1 = rd1.getContent();
					
					Node rd2 = (Node) stack.pop();
					String content2 = rd2.getContent();
					
					String x = carryBOP(rator, content1, content2);
					if(x == null) break;
					Node n = new Node(x,null, 0 );
					stack.push(n);
				}
				
				else if (rators.get(current_node.getContent()).equalsIgnoreCase("uop")) {
					Node rd1 = (Node) stack.pop();
					String content1 = rd1.getContent();
										
					String x = carryUOP(rator, content1);
					if(x == null) break;
					Node n = new Node(x,null, 0 );
					stack.push(n);
					
				}
				
				else {
					System.out.println("Error: Illeagal operation");
				}
			}
			
			else if(current_node.getContent().equalsIgnoreCase("b")){
				String t = stack.pop().getContent();
				int i = current_node.getIndex();
				
				if(t.equalsIgnoreCase("<true>" )) {
					cs.addAll(conditionalDelta.get(i).getDelta());
				}
				else if (t.equalsIgnoreCase("<false>")) {
					cs.addAll(conditionalDelta.get(i+1).getDelta());
				}
				else {
					System.out.println("Error: illeagal condition");
					break;
				}
			}
			
			else {
				stack.push(current_node);
			}
			
			
		}
		
		
		
	}
	
	/*
	 * carry in built functions of RPAl
	 */
	
	private String carryInBuilt(String func_name, String input) {
		
		if( func_name.equalsIgnoreCase("Print")) {
			return input;
		}
		else if(func_name.contentEquals("Isstring")) {
			if(input.startsWith("<STR:")) return "<true>";
			else return "<false>";
		}
		else if(func_name.contentEquals("Isinteger")) {
			if(input.startsWith("<INT:")) return "<true>";
			else return "<false>";
		}
		else if(func_name.contentEquals("Stern")) {
			if(input.startsWith("<STR:")) {
				String xy = input.substring(5, input.length()-1);
				String op = "<STR:"+xy.substring(2,xy.length()-1)+">";
				return op;
			}
			return null;
		}
		else if(func_name.contentEquals("Stem")) {
			if(input.startsWith("<STR:")) {
				String xy = input.substring(5, input.length()-1);
				String op = "<STR:"+xy.substring(1, 2)+">";
				return op;
			}
			return null;
		}
		
		else {
			return null;
		}
		
	}
	
	/*
	 * carry out binary operations
	 */

	private String carryBOP (String rator, String content1, String content2){
		
		if(content2.contains("<INT:")) {
			//integer
			String rand1 = content1.substring(5, content1.length()-1);
			String rand2 = content2.substring(5, content2.length()-1);
	
			try {
				
				int r1 = Integer.parseInt(rand1);
				int r2 = Integer.parseInt(rand2);
				String x;
				if(rator.equalsIgnoreCase("+")) {
					Integer result  = r1 + r2;	
					x = "<INT:" + result.toString() + ">";
				}
				else if(rator.equalsIgnoreCase("-")) {
					Integer result  = r1 - r2;	
					x = "<INT:" + result.toString() + ">";
				}
				else if(rator.equalsIgnoreCase("*")) {
					Integer result  = r1 * r2;	
					x = "<INT:" + result.toString() + ">";
				}
				else if(rator.equalsIgnoreCase("/")) {
					Integer result  = (int)(r1 / r2);	
					x = "<INT:" + result.toString() + ">";
				}
				else if(rator.equalsIgnoreCase("**")) {
					Integer result  = (int) Math.pow(r1, r2);
					x = "<INT:" + result.toString() + ">";
				}
				else if(rator.equalsIgnoreCase("eq")) {
					Boolean result = (r1 == r2);
					x = "<" + result.toString() + ">";
				}
				else if(rator.equalsIgnoreCase("ne")) {
					Boolean result = (r1 != r2);
					x = "<" + result.toString() + ">";
				}
				else if(rator.equalsIgnoreCase("ls") || rator.equalsIgnoreCase("<")) {
					Boolean result = (r1 < r2);
					x = "<" + result.toString() + ">";
				}
				else if(rator.equalsIgnoreCase("gr") || rator.equalsIgnoreCase(">")) {
					Boolean result = (r1 > r2);
					x = "<" + result.toString() + ">";
				}
				else if(rator.equalsIgnoreCase("le") || rator.equalsIgnoreCase("<=")) {
					Boolean result = (r1 <= r2);
					x = "<" + result.toString() + ">";
				}
				else if(rator.equalsIgnoreCase("ge") || rator.equalsIgnoreCase(">=")) {
					Boolean result = (r1 >= r2);
					x = "<" + result.toString() + ">";
				}
				else {
					System.out.println("Error: Illeagal operation");
					return null;
				}
				
				
				return x;
				
			} catch (Exception e) {
				System.out.println("Error: Illeagal operation");
				return null;
			}
			
		} else if(content2.contains("<STR:")) {
			
			//string
			String rand1 = content1.substring(6, content1.length()-2);
			String rand2 = content2.substring(6, content2.length()-2);
			try {
				String x;
				if(rator.equalsIgnoreCase("eq")) {
					Boolean result = (rand1 == rand2);
					x = "<" + result.toString() + ">";
				}
				else if(rator.equalsIgnoreCase("ne")) {
					Boolean result = (rand1 != rand2);
					x = "<" + result.toString() + ">";
				}
				else if(rator.equalsIgnoreCase("ls") || rator.equalsIgnoreCase("<")) {
					Boolean result = (rand1.compareTo(rand1) < 0);
					x = "<" + result.toString() + ">";
				}
				else if(rator.equalsIgnoreCase("gr") || rator.equalsIgnoreCase(">")) {
					Boolean result = (rand1.compareTo(rand1) > 0);
					x = "<" + result.toString() + ">";
				}
				else if(rator.equalsIgnoreCase("le") || rator.equalsIgnoreCase("<=")) {
					Boolean result = (rand1.compareTo(rand1) < 0 || rand1.compareTo(rand1) == 0 );
					x = "<" + result.toString() + ">";
				}
				else if(rator.equalsIgnoreCase("ge") || rator.equalsIgnoreCase(">=")) {
					Boolean result = (rand1.compareTo(rand1) > 0 || rand1.compareTo(rand1) == 0);
					x = "<" + result.toString() + ">";
				}
				else {
					System.out.println("Error: Illeagal operation");
					return null;
				}
				
				return x;
				
			} catch (Exception e) {
				System.out.println("Error: Illeagal operation");
				return null;
			}
		}else if(content2.startsWith("<") && content2.endsWith(">")){
			//boolean
			String rand1 = content1.substring(1, content1.length()-1);
			String rand2 = content2.substring(1, content2.length()-1);
			
			try {
				Boolean r1 = Boolean.parseBoolean(rand1);
				Boolean r2 = Boolean.parseBoolean(rand2);
				
				String x;
				
				if (rator.equalsIgnoreCase("eq")) {
					Boolean result = r1 == r2;
					x = "<" + result.toString() + ">";
				}
				else if (rator.equalsIgnoreCase("ne")) {
					Boolean result = r1 != r2;
					x = "<" + result.toString() + ">";
				}
				else if(rator.equalsIgnoreCase("or")) {
					Boolean result = (r1 == false && r2 == false ) ? false : true ;
					x = "<" + result.toString() + ">";
				}
				else if(rator.equalsIgnoreCase("&")) {
					Boolean result = (r1 == true && r2 == true ) ? true : false ;
					x = "<" + result.toString() + ">";
				}
				else {
					System.out.println("Error: Illeagal operation");
					return null;
				}
				return x;
				
			} catch (Exception e) {
				System.out.println("Error: Illeagal operation");
				return null;
			}
		}
		else {
			
		}
		return null;
	}

	/*
	 * carry out unary operations
	 */
	
	private String carryUOP (String rator, String content){
		if(content.contains("<INT:")) {
			//integer
			String rand1 = content.substring(5, content.length()-1);
			
			try {
				int r1 = Integer.parseInt(rand1);
				
				String x;
				
				if (rator.equalsIgnoreCase("neg")) {
					Integer result = r1 * (-1);
					x = "<INT:" + result.toString() + ">";
				}
				else {
					System.out.println("Error: Illeagal operation");
					return null;
				}
				return x;
				
				
			} catch (Exception e) {
				System.out.println("Error: Illeagal operation");
				return null;
			}
			
		} else if(content.contains("<STR:")) {
			System.out.println("Error: Illeagal operation");
			return null;
			
		}else if(content.startsWith("<") && content.endsWith(">")){
			//boolean
			String rand1 = content.substring(1, content.length()-1);
			
			try {
				Boolean r1 = Boolean.parseBoolean(rand1);
				
				String x;
				
				if (rator.equalsIgnoreCase("not")) {
					Boolean result = !r1;
					x = "<" + result.toString() + ">";
				}
				else {
					System.out.println("Error: Illeagal operation");
					return null;
				}
				return x;
				
			} catch (Exception e) {
				System.out.println("Error: Illeagal operation");
				return null;
			}
		}else {
			
		}
		return null;
	}
	
	/*
	 * print stack to the console
	 */
	
	private void printStack(Stack<Node> s) {
		System.out.print("Stack :");
		for (Node n : s) {
			
			System.out.print(((Node)n).getContent());
			System.out.print(" , ");
			
			
		}
		System.out.println();
		System.out.println();

	}
	
	/*
	 * print control structure
	 */
	
	private void printCS(ControlStructure cs) {
		System.out.print("CS :");
		ArrayList<Node> x = cs.getDelta();
		for (Node y: x) {
			System.out.print(y.getContent());
			System.out.print(" , ");
		}
		System.out.println();
	}
	
	/*
	 * search for previously defined variables in enviroments
	 */
	
	private String LookUp(String content, Environment e) {
		
		for (Environment en: env) {
			if(en.getNames().containsKey(content)) {
				return en.getNames().get(content);
			}
		}
		return null;
		
	}
	
	/*
	 * build the control structure
	 */

	private void buildControlStructure(Node node,ControlStructure cs) {
		
		if(node == null) return;
		
		if (node.getContent().contains("lambda")) {
            setLambda(node,cs);
        } else if (node.getContent().equalsIgnoreCase("->")) {
            setConditional(node,cs);
        } else if (node.getContent().equalsIgnoreCase("tau")) {
            setTau(node, cs);
        } else {
            cs.addtoDelta(node);
            for (Node n: node.getChildren()) {
            	buildControlStructure(n, cs);
            }
        }		
		
	}
	
	/*
	 * set up tau node while building the control structure
	 */

	private void setTau(Node node, ControlStructure cs) {
		int l = node.getChildren().size();
		node.setLen(l);
		buildControlStructure(node, cs);
		
	}
	
	/*
	 * set conditional node while buildinf the control structure
	 */

	private void setConditional(Node node, ControlStructure cs) {
		Node condition = node.getChildren().get(0);
		Node n = new Node("b", null, 0);
		n.setConditionIndex(conditionalDelta.size());
		cs.addtoDelta(n);
		buildControlStructure(condition, cs);
		Node if_true = node.getChildren().get(1);
		ControlStructure delta_true = new ControlStructure(conditionalDelta.size());
		conditionalDelta.add(delta_true);
		buildControlStructure(if_true, delta_true);
		Node if_false = node.getChildren().get(2);
		ControlStructure delta_false = new ControlStructure(conditionalDelta.size());
		conditionalDelta.add(delta_false);
		buildControlStructure(if_false, delta_false);
	
	}
	
	/*
	 * set lambda nose while building the control structure
	 */

	private void setLambda(Node node,ControlStructure cs) {
		
		//get bound variable for lambda
		Node x = node.getChildren().get(0);
		String v = x.getContent().substring(4, x.getContent().length()-1);
		
		//modify lambda node
		node.setIndex(this.deltaArr.size());
		node.setVar(v);
		
		node.setContent("lambda<ID:"+v+">-"+(this.deltaArr.size()));
		
		//add lambda to current control structure
		cs.addtoDelta(node);

		ControlStructure newcs = new ControlStructure(this.deltaArr.size());
		
		this.deltaArr.add(newcs);
		
		Node y = node.getChildren().get(1);
		buildControlStructure(y,newcs);
		
		
	}
	
	
	
}
