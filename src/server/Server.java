package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;

public class Server {

	private static final int PORT = 4242;
	private Collection<Connection> connections = new LinkedList<>();
	private boolean shutdown = false;
	private String SHUTDOWN_MESSAGE = "shutdown";
	private ServerSocket serverSocket;

	public static void main(String[] args) {
		
		System.out.println("Server running...");
		new Server();
	}
	
	public Server() {
		try {

			serverSocket = new ServerSocket(PORT);

			while(!shutdown) {

				Socket client = serverSocket.accept();
				Connection serverClientConnection = new Connection(this, client);
				new Thread(serverClientConnection).start();
				connections.add(serverClientConnection);
				System.out.println("...connection established");
			}
			
			System.out.println("shutting down server...");
			//serverSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("...done");

	}

	public void broadcasts(String message) throws IOException {

		if(message.equals(SHUTDOWN_MESSAGE))
		{
			//unblock remaining serverSocket.accept line by opening a dummy connection.
			shutdown = true;
			Socket unblockSocket = new Socket("localhost", 4242);
			connections.add(new Connection(this, unblockSocket));
			
			//close all client sockets, then close server socket
			System.out.println("disconnecting all clients...");
			for(Connection c: connections)
			{
				c.disconnect();
			}
			
			//send dummy message to unblock own dummy socket
			unblockSocket.getOutputStream().write("DISCONNECT_EVENT".getBytes());
			unblockSocket.getOutputStream().flush();
			
			serverSocket.close();
			
			return;
		}
		
		for (Connection c : connections) {
			c.sendMessage(message+"\n");
		}
	}
}
