package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server
{

    private static final int PORT = 4242;
    private Collection<Connection> connections = new LinkedList<>();
    private boolean shutdown = false;
    private String SHUTDOWN_MESSAGE = "shutdown";
    private ServerSocket serverSocket;

    public static void main(String[] args)
    {

        System.out.println("Server running...");
        new Server();
    }

    public Server()
    {
        launchCommandLine();
        maintainConnections();
    }

    /**
     * Launched a scanner listening for the shutdown sequence in an extra
     * thread. Once entered all clients are logged out and the server will be
     * shut down.
     */
    private void launchCommandLine()
    {
        // launch extra read reading from stdin until SHUTDOWN_MESSAGE was typed
        new Thread(() -> {
            try (Scanner scanner = new Scanner(System.in)) {
                while (!scanner.nextLine().equals(SHUTDOWN_MESSAGE)) {
                }
            }
            
            //unblock thread waiting for new clients, using loopback socket
            shutdown = true;
            try {
                Socket loopbackSocket = new Socket("localhost", 4242);
            }
            catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

    /**
     * Blocking method, awaits new clients until shutdown is set and the method
     * is unblocked.
     */
    private void maintainConnections()
    {
        try {
            serverSocket = new ServerSocket(PORT);

            while (!shutdown) {
                Socket client = serverSocket.accept();
                Connection serverClientConnection = new Connection(this, client);
                new Thread(serverClientConnection).start();
                connections.add(serverClientConnection);
                System.out.println("...connection established");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //initialize shutdown, once the remaining opened socket was closed
        shutdown();
    }

    /**
     * Propagates a given message to all registered chat clients. Must not be
     * called in case closed/broken down connections persist.
     *
     * @param message
     * @throws IOException
     */
    public void broadcasts(String message) throws IOException
    {
        for (Connection c : connections) {
            c.sendMessage(message + "\n");
        }
    }

    /**
     * Removes a deactivated connection from the servers list. Needs to be
     * called before the next broadcast, whenever a connection is closed.
     *
     * @param connection
     */
    public void removeConnection(Connection connection)
    {
        connections.remove(connection);
        System.out.println("Removed a connection");
    }

    /**
     * Closes all active connections, stalls all threads. This is done by
     * setting the shutdown flag, then unblocking the maintainConnections()
     * method by a loopback connection.
     *
     * @throws IOException
     * @throws UnknownHostException
     */
    public void shutdown()
    {
        //shutdown = true;
        try {
            //	Socket loopbackSocket = new Socket("localhost", 4242);
            broadcasts(Connection.DISCONNECT_REQ);
			//for (Connection c : connections){
            //	removeConnection(c);}
            serverSocket.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to shutdown server!");
        }
        System.out.println("[Server shut down]");
    }

}
