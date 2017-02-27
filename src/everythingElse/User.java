package everythingElse;

import java.util.HashMap;

public class User {
	
	HashMap<String, String> users;
	
	public User() {
		this.users = new HashMap<String, String>();
	}
	
	public void add(String username, String password) {
		users.put(username, password);
	}
	
	public HashMap<String, String> getHashMap() {
		return users;
	}
	
	public boolean containsUser(String username) {
		return users.containsKey(username);
	}
}
