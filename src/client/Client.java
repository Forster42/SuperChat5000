package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	
	private static final String DISCONNECT_MESSAGE="disconnect";
	private static Socket server;
	private static boolean shutdown = false;

	public static void main(String[] args) throws UnknownHostException,
			IOException {

		server = new Socket("localhost", 4242);

		new Thread(new Runnable() {

			@Override
			public void run() {

				Scanner scanner = new Scanner(System.in);

				boolean stopScanner = false;
				while (!stopScanner) {
					stopScanner = sendMessage(scanner.nextLine());
				}
			}
		}).start();

		System.out.println("Client running");
		
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(
				server.getInputStream()));
		
		while (!shutdown) {
			String message = inputReader.readLine();
		
			if(message.equals(DISCONNECT_MESSAGE))
			{
				sendMessage("DISCONNECT_EVENT");
				shutdown = true;
			}
			else
			{
				System.out.println(message);
			}
		}
	}
	
	private static boolean sendMessage(String message)
	{
		boolean shutdownMessage = message.equals("shutdown");
		message = message
				+ System.lineSeparator();
		try {
			server.getOutputStream().write(message.getBytes());
			server.getOutputStream().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return shutdownMessage;
	}
	}

	


