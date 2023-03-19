import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    private static final  String IP = "127.0.0.1";
    private static final int PORT = 8080;
    private static String MESSAGE = "MESSAGE";
    private final String DISCONNECT = "DISCONNECT";
    private final String CONNECT = "CONNECT";
    private final String WAITING = "WAITING";
    private ReentrantLock lockWriteFile;
    public static void main ( String[] args ) throws IOException {
        Socket socket = new Socket(IP,PORT);
        ReentrantLock lockWriteFile = new ReentrantLock();
        ClientThread servercon = new ClientThread (socket, lockWriteFile);
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        new Thread(servercon).start();

        while (true){
            //System.out.println(" -> ");
            String comando = keyboard.readLine();

            LogClient logClient = new LogClient(MESSAGE, 1, comando.toString(), lockWriteFile);
            logClient.run();
            if(comando.equals("/break")) break; //mudar aqui
            out.println(comando);

        }
        socket.close();
        System.exit(0);

    }
}
