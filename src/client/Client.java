package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client
{

    private static final String LOGOUT_MESSAGE = "quit";
    private static final String DISCONNECT_REQ = "DISCONNECT_REQ";
    private static final String DISCONNECT_ACK = "DISCONNECT_ACK";
    private static boolean disconnected = false;
    private static Socket server;

    public static void main(String[] args) throws UnknownHostException,
            IOException
    {

        //launch extra read reading from stdin until LOGOUT_MESSAGE was entered
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Scanner scanner = new Scanner(System.in);
                while (!sendMessage(scanner.nextLine())) {
                }
                scanner.close();
            }

        }).start();

        // connect to server socket and listen for incoming messages
        server = new Socket("localhost", 4242);
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                server.getInputStream()));
        String message = "***SuperChat5000***";
        while (!(message.equals(DISCONNECT_REQ) || message.equals(DISCONNECT_ACK))) {
            System.out.println(message);
            message = inputReader.readLine();
        }
        //in case the server still needs to be unblocked, send unblock ACK
        if (message.equals(DISCONNECT_REQ)) {
            sendMessage(DISCONNECT_ACK);
        }

        disconnected = true;
        System.out.println("[Connection closed, type any key to exit]");
    }

    /**
     * Sends message through socket connection. Returns true if the message sent
     * was the disconnect message.
     *
     * @param message as the content to be transmitted
     * @return whether the message matches the disconnect sequence
     */
    private static boolean sendMessage(String message)
    {
        if (!disconnected) {
            try {
                server.getOutputStream().write(
                        new String((message.equals(LOGOUT_MESSAGE) ? DISCONNECT_REQ : message) + System.lineSeparator()).getBytes());
                server.getOutputStream().flush();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return message.equals(LOGOUT_MESSAGE);
        }
        return true;
    }

}
