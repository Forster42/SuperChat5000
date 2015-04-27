package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;

public class Server {

	private static final int PORT = 4242;

	private Collection<Connection> connections = new LinkedList<>();

	public Server() {

		try {

			ServerSocket serverSocket = new ServerSocket(PORT);

			while (true) {

				Socket client = serverSocket.accept();

				Connection serverClientConnection = new Connection(this, client);

				new Thread(serverClientConnection).start();
				
				connections.add(serverClientConnection);

				System.out.println("Connected");

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void broadcasts(String message) throws IOException {

		for (Connection c : connections) {

			c.sendMessage(message);

		}

	}

	public static void main(String[] args) {

		System.out.println("Server");

		Server server = new Server();

	}
}
