package everythingElse;

import java.io.File;
import java.io.Serializable;

public class NetworkData implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String tag; // instruction for the server/client
	private String msg;
	private String username;
	private File file;
	static String USERNAME_TAG = "USERNAME";
	static String FILE_TAG = "FILE";
	static String MSG_TAG = "MSG";

	//private byte[] file; // byte array of sound file


	public NetworkData(String tag, String username, String data) {
		this.tag = tag;
		this.username = username;
		placeData(data);
	}

	private void placeData(String data) {
		if (tag.equals(NetworkData.USERNAME_TAG)) {
			this.msg = data;
		} else if (tag.equals(NetworkData.FILE_TAG)) {
			this.file = new File(System.getProperty("user.dir") + "/new_folder/" + data);
		}
	}


	public String getTag() {
		return this.tag;
	}

	public String getMsg() {
		return this.msg;
	}

	public String getUsername() {
		return this.username;
	}

	public File getFile() {
		return this.file;
	}

//	private void convertToBytes(String data) {
//		Path path = (Path) Paths.get(System.getProperty("user.dir"), "/new_folder/" + data);
//		try {
//			this.file = Files.readAllBytes(path);
//		} catch (IOException e) {
//			// TODO run an alert
//			e.printStackTrace();
//		}
//	}

}
