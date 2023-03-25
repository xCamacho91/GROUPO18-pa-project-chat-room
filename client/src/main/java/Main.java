import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Main {

    private static final  String IP = "127.0.0.1";
    private static final int PORT = 8080;
    private static String MESSAGE = "MESSAGE";
    private static String DISCONNECT = "DISCONNECT";
    private static String CONNECT = "CONNECT";
    private static String WAITING = "WAITING";
    private static ReentrantLock lockWriteFile = new ReentrantLock();
    public static void main ( String[] args ) throws IOException, InterruptedException {
        Socket socket = new Socket(IP,PORT);
        ClientThread servercon = new ClientThread (socket, lockWriteFile);
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        new Thread(servercon).start();

        while (true){
            String comando = keyboard.readLine();
            LogClient logClient = new LogClient(MESSAGE, 1, comando.toString(), lockWriteFile);
            logClient.run();
            if(comando.equals("/quit")) {
                LogClient logClients = new LogClient(DISCONNECT, 1, comando.toString(), lockWriteFile);
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
