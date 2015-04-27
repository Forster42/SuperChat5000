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

	public Connection(Server server, Socket client) {

		this.server = server;
		this.client = client;

		try {
			this.os = client.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {

		try {
			InputStreamReader in = new InputStreamReader(
					client.getInputStream());
			BufferedReader reader = new BufferedReader(in);

			String bla;
			while (true) {

				bla = reader.readLine();
				server.broadcasts(bla);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendMessage(String message) throws IOException {
		os.write(message.getBytes());
		os.flush();

	}

}
