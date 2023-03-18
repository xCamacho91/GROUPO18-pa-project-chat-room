import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Integer.parseInt;

public class ServerThread extends Thread {
    private final int port;
    private final ServerSocket serverSocket;
    private final ReentrantLock clientSocketsLock;
    private final ArrayList<Socket> clientSockets;
    private final Properties serverConfig;
    private DataInputStream in;
    private PrintWriter out;


    public ServerThread (Properties serverConfig, ServerSocket serverSocket, ReentrantLock clientSocketsLock, ArrayList<Socket> clientSockets, int port) {
        this.serverConfig = serverConfig;
        this.serverSocket = serverSocket;

        this.clientSocketsLock = clientSocketsLock;
        this.clientSockets = clientSockets;

        this.port = port;
    }


    /**
     * The thread's run method.
     * Accepts clients and creates a new thread to serve each individual client.
     */
    public void run ( ) {
        try {
            while ( true ) {
                //System.out.println ( "Accepting Data" );
                Socket newClientSocket  = serverSocket.accept ( );

                clientSocketsLock.lock();
                clientSockets.add(newClientSocket);
                System.out.println("Client added to the list of sockets"+ newClientSocket.getPort());

                Socket clientAdded = clientSockets.get(clientSockets.size()-1);
                clientSocketsLock.unlock();

                ServerClientThread newClient = new ServerClientThread(serverConfig, clientAdded, clientSocketsLock, clientSockets);
                newClient.start();

            }
        } catch ( IOException e ) {
            e.printStackTrace ( );
        }
    }
}
