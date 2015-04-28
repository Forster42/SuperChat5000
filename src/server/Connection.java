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
	private final String DISCONNECT_MESSAGE="disconnect";

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

	@Override
	public void run() {

		try {
			String message;
			while (!disconnect) {

				message = inputReader.readLine();
				//if(!message.equals("DISCONNECT_EVENT"))
				//{
				server.broadcasts(message);
				//}}
			}
			
			//close connection
			client.close();
			System.out.println("[Closed a connection]");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void sendMessage(String message) throws IOException {
		os.write(message.getBytes());
		os.flush();

	}

	public void disconnect() throws IOException {
		disconnect = true;
		os.write(DISCONNECT_MESSAGE.getBytes());
		os.flush();
		
	}
}
