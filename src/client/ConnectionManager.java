package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

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
    private boolean encryptionEnabled = true;

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
                    chatPanel.extendHistory(decrypt(message));
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
                        ((encryptionEnabled?encrypt(message):message) + System.lineSeparator()).getBytes());
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

    /**
     * reverts the message (ignoring the message origin prelude)
     */
    private String encrypt(String message)
    {
        //checking for a collon, since this indicates a username exists, therfore it is not DISCONNECT_REQ / DISCONNECT_ACKF
        if(message.contains(":"))
        {
            //split at fors colon
            String[] messageSplice = message.split(":", 2);
            char[]  choppedMessageContent = messageSplice[1].toCharArray();
            char[] invertedMessageContent = new char[choppedMessageContent.length];
            for(int i = 0 ; i < choppedMessageContent.length; i++)
            {
                invertedMessageContent[invertedMessageContent.length-i-1] = choppedMessageContent[i];
            }
            return messageSplice[0]+":"+new String(invertedMessageContent);
        }
        return message;
    }
    
    /**
     * Using a symmetric encryption. Decryption method may internally use encryption method.
     */
    private String decrypt(String message)
    {
        return encrypt(message);
    }
}
