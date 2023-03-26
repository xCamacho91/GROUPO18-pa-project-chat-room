import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Main {

    /**
     * The path of the file to save the requests' information to.
     */
    private static String serverLogFileName = "server/server.log";
    private static final  String IP = "127.0.0.1";
    private static final int PORT = 8080;
    private static String MESSAGE = "MESSAGE";
    private static String DISCONNECT = "DISCONNECT";
    private static String CONNECT = "CONNECT";
    private static String WAITING = "WAITING";
    private static int id;
    private static ReentrantLock lockWriteFile = new ReentrantLock();
    public static void main ( String[] args ) throws IOException, InterruptedException {

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
        id = dataInputStream.readInt(); // Receive the ID from the server
        System.out.println("Your ID is: " + id);
        int sem = 0;
        sem = dataInputStream.readInt(); // Receive the ID from the server
        System.out.println("Your ID is: " + sem);
        if ( sem != 0) {
            LogClient logClient = new LogClient(timestamp, CONNECT, id, "", lockWriteFile, serverLogFileName);
            logClient.run();
        } else {
            LogClient logClient = new LogClient(timestamp, WAITING, id, "", lockWriteFile, serverLogFileName);
            logClient.run();
        }

        ClientThread servercon = new ClientThread (socket, lockWriteFile, id);
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);


        new Thread(servercon).start();

        while (true){
            String comando = keyboard.readLine();
            LogClient logClient = new LogClient(timestamp, MESSAGE, id, comando.toString(), lockWriteFile, serverLogFileName);
            logClient.run();
            if(comando.equals("/quit")) {
                LogClient logClients = new LogClient(timestamp, DISCONNECT, id, comando.toString(), lockWriteFile, serverLogFileName);
                logClients.run();
                sleep(1000);
                break; //mudar aqui
            }
            out.println(comando);
        }
        socket.close();
        System.exit(0);
    }
}
