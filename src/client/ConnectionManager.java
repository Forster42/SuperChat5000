package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Max Schiedermeier
 */
public class ConnectionManager
{

    private static final int port = 4242;
    private ChatPanel chatPanel;
    private final String DISCONNECT_REQ = "DISCONNECT_REQ";
    private final String DISCONNECT_ACK = "DISCONNECT_ACK";
    private boolean connected = false;
    private Socket server;

    private static ConnectionManager singletonReference = null;

    public static ConnectionManager getInstance()
    {
        if (singletonReference == null) {
            singletonReference = new ConnectionManager();
        }
        return singletonReference;
    }

    private ConnectionManager()
    {
    }

    public void setOutputWindow(ChatPanel chatPanel)
    {
        this.chatPanel = chatPanel;
    }

    public void connect(String serverIp) throws IOException
    {
        // connect to server socket and listen for incoming messages
        server = new Socket(serverIp, port);
        connected = true;
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                server.getInputStream()));

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String message = "";
                while (!(message.equals(DISCONNECT_REQ) || message.equals(DISCONNECT_ACK))) {
                    chatPanel.extendHistory(message);
                    try {
                        message = inputReader.readLine();
                    }
                    catch (IOException ex) {
                        throw new RuntimeException(ex.getMessage());
                    }
                }
                //in case the server still needs to be unblocked, send unblock ACK
                if (message.equals(DISCONNECT_REQ)) {
                    sendMessage(DISCONNECT_ACK);
                }

                connected = false;
                System.out.println("[Connection closed]");
            }

        }).start();

    }

    public void disconnect() throws IOException
    {
        sendMessage(DISCONNECT_REQ);
    }

    /**
     * Sends message through socket connection.
     *
     * @param message as the content to be transmitted
     */
    public void sendMessage(String message)
    {
        if (connected) {
            try {
                server.getOutputStream().write(
                        (message + System.lineSeparator()).getBytes());
                server.getOutputStream().flush();
            }
            catch (IOException ex) {
                throw new RuntimeException("Unable to send message. Broken Pipe possible.\n" + ex.getMessage());
            }

        }
    }

    public boolean isConnected()
    {
        return connected;
    }

}
