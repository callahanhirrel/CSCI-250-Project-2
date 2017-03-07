package everythingElse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class NetworkData {

	private String tag; // instruction for the server/client
	private String msg; // data being sent
	private byte[] file; // byte array of sound file

	public NetworkData(String tag, String data) {
		this.tag = tag;
		placeData(data);
	}

	public NetworkData(String packet) {

	}

	private void placeData(String data) {
		if (tag.equals("USERNAME")) {
			this.msg = data;
		} else if (tag.equals("FILE")) {
			convertToBytes(data);
		}
	}

	private void convertToBytes(String data) {
		Path path = (Path) Paths.get(System.getProperty("user.dir"), "/new_folder/" + data);
		try {
			this.file = Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO run an alert
			e.printStackTrace();
		}
	}

}
