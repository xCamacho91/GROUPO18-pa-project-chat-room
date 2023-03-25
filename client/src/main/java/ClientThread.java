
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

    /*
    public ClientThread ( int port , int id , int freq ) {
        this.port = port;
        this.id = id;
        this.freq = freq;

        lockWriteFile = new ReentrantLock();

        createFile(serverLogFileName);
    }*/

    @Override
    public void run ( ) {
        //parseRequest ( );
        String RespostaServer = null;
        try {
            while (true) {
                RespostaServer = in.readLine();

                if (RespostaServer == null) break;

                System.out.println("Client" + id + ": " + RespostaServer);
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
    /*
    private void parseRequest() {
        Thread t = new Thread( ()-> {
            int i = 0;
            StringBuilder stringBuilder = new StringBuilder();
            while ( true ) {
                System.out.println ( "Sending Data" );
                try {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    // if(sem.tryAcquire(1, TimeUnit.SECONDS)) {
                    socket = new Socket ( "localhost" , port );
                    serverLog(timestamp,CONNECT,id,stringBuilder.toString());
                    out = new DataOutputStream ( socket.getOutputStream ( ) );
                    in = new BufferedReader ( new InputStreamReader ( socket.getInputStream ( ) ) );
                    stringBuilder.append("My message number " + i + " to the server " + "I'm " + id );
                    out.writeUTF ( stringBuilder.toString());
                    serverLog(timestamp,MESSAGE,id,stringBuilder.toString());
                    stringBuilder.setLength(0);
                    String response;
                    response = in.readLine ( );
                    System.out.println ( "From Server " + response );
                    out.flush ( );
                    socket.close ( );
                    serverLog(timestamp,DISCONNECT,id,stringBuilder.toString());
                    sleep ( freq );
                    i++;
                } catch ( IOException | InterruptedException e ) {
                    e.printStackTrace ( );
                }
            }
        });
        t.start();
    } */


}
