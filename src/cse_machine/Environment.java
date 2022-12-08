package cse_machine;

import java.util.HashMap;

public class Environment {

	private Environment previous_env;
	private HashMap<String, String> names;
	private int index;
	
	public Environment(Environment previous_env) {
		this.previous_env = previous_env;
		this.names = new HashMap<>();
		this.index = previous_env == null ? 0 : previous_env.getIndex()+1;
	}
	
	public Environment getPrevious_env() {
		return previous_env;
	}

	public void setPrevious_env(Environment previous_env) {
		this.previous_env = previous_env;
	}

	public HashMap<String,String> getNames() {
		return names;
	}

	public void setNames(HashMap<String,String> names) {
		this.names = names;
	}
	
	public void addName(String key, String value) {
		this.names.put(key, value);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
