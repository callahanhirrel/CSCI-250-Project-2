package everythingElse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
				PrintWriter writer = new PrintWriter(socket.getOutputStream());
				String data = getData();
				printToConsole(true, data);
				sendData(writer, data);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		private void sendData(PrintWriter writer, String justReceived) {
			if (justReceived.equals("requesting connection")) {
				writer.println("connection open");
				printToConsole(false, "connection open");

				writer.println(username);
				printToConsole(false, username);
			}
			writer.flush();
		}

		private void printToConsole(boolean received, String txt) {
			String toPrint = "Server: ";
			if (received) {
				toPrint += "Received ";
			} else {
				toPrint += "Sent ";
			}
			toPrint += "[" + txt + "]";
			System.out.println(toPrint);
		}

		private String getData() throws IOException {
			BufferedReader responses = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String toReturn = "";
			while(!responses.ready()) {}
			while(responses.ready()) {
				toReturn += responses.readLine() + "\n";
			}
			return toReturn.trim(); // to get rid of the last newline
		}
	}

}
