package everythingElse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

// This class is heavily based on Dr. Ferrer's Server class from his network
// programming tutorial.
public class Server {

	public static void main(String[] args) throws IOException {

	}

	private ServerSocket accepter;
	private String username;

	public Server(int port, String username) throws IOException {
		this.username = username;
		accepter = new ServerSocket(port);
		System.out.println("Server: IP Address: " + accepter.getInetAddress() + " (" + port + ")");
	}

	public void listen() throws IOException {
		for (;;) {
			Socket s = accepter.accept(); // waiting and waiting
			SocketCommunicationThread communicator = new SocketCommunicationThread(s);
			System.out.println("Server: Request from " + s.getInetAddress());
			communicator.start();
		}
	}

	private class SocketCommunicationThread extends Thread {

		private Socket socket;

		public SocketCommunicationThread(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				ObjectOutputStream sockout = new ObjectOutputStream(socket.getOutputStream());
				NetworkData data = getData();
				printToConsole("Received", data);
				unpackageData(sockout, data);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		private void unpackageData(ObjectOutputStream sockout, NetworkData justReceived) throws IOException {
			if (justReceived.getTag().equals(NetworkData.MSG_TAG)) {
				NetworkData toSend = new NetworkData(NetworkData.USERNAME_TAG, username, "connection open");
				sockout.writeObject(toSend);
				printToConsole("Sent", toSend);
			} else if (justReceived.getTag().equals(NetworkData.FILE_TAG)) {
				saveFile(justReceived);
			}
			sockout.flush();
		}

		private void saveFile(NetworkData justReceived) {
			try {
				String path = System.getProperty("user.dir") + "/receivedFiles/" + justReceived.getFilename();
				FileOutputStream writer = new FileOutputStream(path);
				writer.write(justReceived.getFileContents());
				writer.close();
//				Path path = Paths.get(System.getProperty("user.dir"), "/receivedFiles/");
//				Files.write(path, justReceived.getFileContents(), StandardOpenOption.CREATE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void printToConsole(String action, NetworkData data) {
			String toPrint = "Server: " + action;
			toPrint += ": [" + data.getTag() + "]";
			System.out.println(toPrint);
		}

		private NetworkData getData() throws IOException, ClassNotFoundException {
			ObjectInputStream sockin = new ObjectInputStream(socket.getInputStream());
			NetworkData data = (NetworkData) sockin.readObject();
			return data;
//			BufferedReader responses = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			String toReturn = "";
//			while(!responses.ready()) {}
//			while(responses.ready()) {
//				toReturn += responses.readLine() + "\n";
//			}
//			return toReturn.trim(); // to get rid of the last newline
		}
	}

}
