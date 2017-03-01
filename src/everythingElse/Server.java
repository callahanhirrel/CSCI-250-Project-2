package everythingElse;

import java.io.IOException;
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
			
		}
	}

}
