package everythingElse;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

public class NetworkData implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String tag; // instruction for the server/client
	private String msg;
	private String username;
	private String filename;
	private File file;
	private byte[] fileContents;
	static String USERNAME_TAG = "USERNAME";
	static String FILE_TAG = "FILE";
	static String MSG_TAG = "MSG";


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
			this.filename = data;
			convertToBytes(data);
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

	public String getFilename() {
		return this.filename;
	}

	public File getFile() {
		return this.file;
	}

	public byte[] getFileContents() {
		return this.fileContents;
	}

	private void convertToBytes(String data) {
		try {
			this.fileContents = Files.readAllBytes(this.file.toPath());
		} catch (IOException e) {
			// TODO run an alert
			e.printStackTrace();
		}
	}

}
