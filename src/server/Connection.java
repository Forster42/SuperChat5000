package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Connection implements Runnable {

	private Server server;
	private Socket client;
	private OutputStream os;
	private BufferedReader inputReader;
	private boolean disconnect = false;

	static final String DISCONNECT_REQ = "DISCONNECT_REQ";
	private static final String DISCONNECT_ACK = "DISCONNECT_ACK";

	public Connection(Server server, Socket client) {

		this.server = server;
		this.client = client;

		try {
			os = client.getOutputStream();
			inputReader = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handle all incoming connections until disconnect message unblocks the
	 * loop
	 */
	@Override
	public void run() {
		try {
			String message;
			while (!disconnect) {
				message = inputReader.readLine();
				if (!((message.equals(DISCONNECT_ACK) || message
						.equals(DISCONNECT_REQ))))
					server.broadcasts(message);
				else
					disconnect(message.equals(DISCONNECT_REQ));
			}

			// close connection
			client.close();
			System.out.println("[Closed a connection]");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a given message to the remote client
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException {
		os.write(message.getBytes());
		os.flush();
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void disconnect(boolean sendAck) throws IOException {
		disconnect = true;
		if (sendAck)
			sendMessage(DISCONNECT_ACK);
		//server.removeConnection(this);
	}
}
