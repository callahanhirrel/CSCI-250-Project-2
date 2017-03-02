package everythingElse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;


public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String UserLoginFile = "User_Pass.txt";
	
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
	
	public static void saveUsers(User users) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(UserLoginFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(users);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static User loadUsers() {
		if (fileExists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(UserLoginFile);
				ObjectInputStream ois = new ObjectInputStream(fis);
				User tempUsers = (User) ois.readObject();
				ois.close();
				return tempUsers;
			} catch (ClassNotFoundException | IOException e){
				e.printStackTrace();
			}
		}			
		User tempUsers = new User();
		return tempUsers;
	}
	
	public static boolean fileExists() {
		return new File(UserLoginFile).isFile();
	}
}
