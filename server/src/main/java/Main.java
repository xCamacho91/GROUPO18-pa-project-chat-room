import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Integer.parseInt;

public class Main {

    /**
     * The port where the server is going to run in.
     */
    private static int port;

    /**
     * The server's configuration, imported from the configuration file when the server started.
     */
    private static Properties serverConfig;


    /**
     * The lock responsible for the client sockets array,
     * which contains all the clients that are making requests at a given point in time.
     */
    private static final ReentrantLock clientSocketsLock = new ReentrantLock();

    /**
     * The server's array that contains the sockets of each request being made at a given point in time.
     */
    private static final ArrayList<Socket> clientSockets = new ArrayList<>();
    /**
     * Constructor for the thread responsible for accepting the clients.
     *
     * @throws IOException if an I/O error occurs when creating the output stream or if the socket is not connected.
     **/
    private static void initializeSettings() throws IOException {
        serverConfig = new Properties();
        InputStream configPathInputStream = new FileInputStream("server/server.config");
        serverConfig.load(configPathInputStream);
        port = parseInt(serverConfig.getProperty("server.port"), 10);
    }


    public static void main ( String[] args ) {

        // Initialize serverSocket settings
        try {
            initializeSettings();
        } catch (Exception exception) {
            System.out.println("Settings config path not found.");
        }


        // Start the server
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Started server on port: " + port);
        } catch (
                IOException exception) {
            System.out.println(exception.getMessage());
            return;
        }

        // Accept clients
        ServerThread server = new ServerThread(serverConfig, serverSocket, clientSocketsLock, clientSockets, port);
        server.start();


    }

}
