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
		Server s = new Server(8881); // gonna use 8881 as the port for now
		s.listen();
	}

	private ServerSocket accepter;

	public Server(int port) throws IOException {
		accepter = new ServerSocket(port);
		System.out.println("Server: IP Address: " + accepter.getInetAddress() + " (" + port + ")");
	}

	public void listen() throws IOException {
		for (;;) {
			Socket s = accepter.accept(); // waiting and waiting
			SocketCommunicationThread communicator = new SocketCommunicationThread(s);
			System.out.println("Server: Connection accepted from " + s.getInetAddress());
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
				sendGreeting(writer);
				String msg = getMessage();
				System.out.println("Server: Received [" + msg + "]");
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		private void sendGreeting(PrintWriter writer) {
			writer.println("Connection open");
		}

		private String getMessage() throws IOException {
			BufferedReader responses = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String toReturn = "";
			while(!responses.ready()) {}
			while(responses.ready()) {
				toReturn += responses.readLine() + "\n";
			}
			return toReturn;
		}
	}

}
