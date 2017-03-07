package everythingElse;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class NetworkData implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String tag; // instruction for the server/client
	private String msg; // data being sent
	//private byte[] file; // byte array of sound file
	private File file;

	public NetworkData(String tag, String data) {
		this.tag = tag;
		placeData(data);
	}

	private void placeData(String data) {
		if (tag.equals("USERNAME")) {
			this.msg = data;
		} else if (tag.equals("FILE")) { // data should be a filename in this case
			// convertToBytes(data);
			this.file = new File(System.getProperty("user.dir") + "/new_folder/" + data);
		}
	}

	public String getTag() {
		return this.tag;
	}

	public String getMsg() {
		return this.msg;
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
