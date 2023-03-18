import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

public class ServerClientThread extends Thread {


    /**
     * The server's configuration, imported from the configuration file when the server started.
     */
    private final Properties serverConfig;

    /**
     * The client's socket, created when the client requested some route.
     */
    private final Socket clientSocket;

    /**
     * The lock responsible for the client sockets array,
     * which contains all the clients that are making requests at a given point in time.
     */
    private final ReentrantLock clientSocketsLock;
    /**
     * The server's array that contains the sockets of each request being made at a given point in time.
     */
    private final ArrayList<Socket> clientSockets;

    private DataInputStream in;
    private PrintWriter out;


    public ServerClientThread(Properties serverConfig, Socket clientSocket, ReentrantLock clientSocketsLock, ArrayList<Socket> clientSockets) {
        this.serverConfig = serverConfig;
        this.clientSocket = clientSocket;
        this.clientSocketsLock = clientSocketsLock;
        this.clientSockets = clientSockets;
    }

    public void run() {
        System.out.println("Client accepted");


        try {
            in = new DataInputStream( clientSocket.getInputStream ( ) );
            out = new PrintWriter( clientSocket.getOutputStream ( ) , true );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String message = null;
        try {
            message = in.readUTF ( );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //thread para filtrar o conteudo das mensagens antes de apresentar
        System.out.println ( "***** " + message + " *****" );
        // escrita para o ficheiro de log das mensagens

        out.println ( message.toUpperCase ( ) );

    }

}
