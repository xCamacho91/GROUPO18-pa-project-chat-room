import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    /**
     * The path of the file to save the requests' information to.
     */
    private final static String serverLogFileName = "server/server.log";
    private final static String IP = "127.0.0.1";
    private final static int PORT = 8080;
    private final static String MESSAGE = "MESSAGE";
    private final static String DISCONNECT = "DISCONNECT";
    private final static String CONNECT = "CONNECT";
    private final static String WAITING = "WAITING";
    private final static ReentrantLock lockWriteFile = new ReentrantLock();
    public static void main ( String[] args ) throws IOException {

        //System.out.println("Enter the IP address:");
        //BufferedReader keyboardIP = new BufferedReader(new InputStreamReader(System.in));
        //IP = keyboardIP.readLine();
        //System.out.println("Enter the port:");
        //BufferedReader keyboardPORT = new BufferedReader(new InputStreamReader(System.in));
        //PORT = Integer.parseInt(keyboardPORT.readLine());

        Socket socket = new Socket(IP,PORT);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        InputStream inputStream = socket.getInputStream(); // Get the input stream of the server socket
        DataInputStream dataInputStream = new DataInputStream(inputStream); // Wrap the input stream with a DataInputStream
        int id = dataInputStream.readInt(); // Receive the ID from the server
        System.out.println("Your ID is: " + id);
        int sem;
        sem = dataInputStream.readInt(); // Receive the ID from the server
        System.out.println("Your ID is: " + sem);
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
                LogClient logClients = new LogClient(timestamp, DISCONNECT, id, comando, lockWriteFile, serverLogFileName);
                logClients.start();
                break; //mudar aqui
            }
            out.println(comando);
        }
        socket.close();
        System.exit(0);
    }
}
