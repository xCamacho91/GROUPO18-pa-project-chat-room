import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    /**
     * The path of the file to save the requests' information to.
     */
    private final static String serverLogFileName = "server/server.log";
    /**
     * The IP of the server.
     */
    private final static String IP = "127.0.0.1";
    /**
     * The port of the server.
     */
    private final static int PORT = 8080;
    /**
     * The type of message received from the client.
     */
    private final static String MESSAGE = "MESSAGE";
    /**
     * The type of message received from the client.
     */
    private final static String DISCONNECT = "DISCONNECT";
    /**
     * The type of message received from the client.
     */
    private final static String CONNECT = "CONNECT";
    /**
     * The type of message received from the client.
     */
    private final static String WAITING = "WAITING";
    /**
     * The lock to write to the file.
     */
    private final static ReentrantLock lockWriteFile = new ReentrantLock();

    public static void main ( String[] args ) throws IOException {

        Socket socket = new Socket(IP,PORT);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        InputStream inputStream = socket.getInputStream(); // Get the input stream of the server socket
        DataInputStream dataInputStream = new DataInputStream(inputStream); // Wrap the input stream with a DataInputStream
        int id = dataInputStream.readInt(); // Receive the ID from the server
        System.out.println("Your ID is: " + id);
        int sem;
        sem = dataInputStream.readInt(); // Receive the ID from the server
        System.out.println("Available entries: " + sem);
        if ( sem != 0) {
            LogClient logClient = new LogClient(timestamp, CONNECT, id, "", lockWriteFile, serverLogFileName);
            logClient.start();
        } else {
            LogClient logClient = new LogClient(timestamp, WAITING, id, "", lockWriteFile, serverLogFileName);
            logClient.start();
        }
        ClientThread servercon = new ClientThread (socket, lockWriteFile);
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        new Thread(servercon).start();

        while (true){
            String comando = keyboard.readLine();
            LogClient logClient = new LogClient(timestamp, MESSAGE, id, comando, lockWriteFile, serverLogFileName);
            logClient.start();
            if(comando.equals("/quit")) {
                out.println(comando);
                LogClient logClients = new LogClient(timestamp, DISCONNECT, id, comando, lockWriteFile, serverLogFileName);
                logClients.start();
                break;
            }
            out.println(comando);
        }
        socket.close();
        System.exit(0);
    }
}
