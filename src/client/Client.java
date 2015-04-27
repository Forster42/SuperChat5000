package client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws UnknownHostException,
			IOException {

		System.out.println("Client running");

		Socket server = new Socket("localhost", 4242);

		new Thread(new Runnable() {

			@Override
			public void run() {

				Scanner scanner = new Scanner(System.in);

				while (true) {

					try {
						String message = scanner.nextLine()+System.lineSeparator();
						server.getOutputStream().write(
								message.getBytes());
						server.getOutputStream().flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}).start();

		InputStreamReader in = new InputStreamReader(server.getInputStream());

		while (true) {

			System.out.println(in.read());

		}

	}

}
