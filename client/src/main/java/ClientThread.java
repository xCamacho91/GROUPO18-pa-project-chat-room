
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.concurrent.locks.ReentrantLock;

public class ClientThread extends Thread {
    private int port;
    private int id;
    private int freq;
    private final String MESSAGE = "MESSAGE";
    private final String DISCONNECT = "DISCONNECT";
    private final String CONNECT = "CONNECT";
    private final String WAITING = "WAITING";
    private DataOutputStream out;
    private BufferedReader in;
    private Socket server;
    private ReentrantLock lockWriteFile;

    public ClientThread(Socket s, ReentrantLock lockWriteFile, int id) throws IOException {
        this.id=id;
        server = s;
        in = new BufferedReader(new InputStreamReader((server.getInputStream())));
        this.lockWriteFile = lockWriteFile;
    }

    @Override
    public void run ( ) {
        String RespostaServer = null;
        try {
            while (true) {
                RespostaServer = in.readLine();

                if (RespostaServer == null) break;

                System.out.println(RespostaServer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
