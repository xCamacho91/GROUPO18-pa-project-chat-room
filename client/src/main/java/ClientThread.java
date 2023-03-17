import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable {
    //private final int port;
    //private final int id;
    //private final int freq;
    //private DataOutputStream out;
    private BufferedReader in;
    private Socket server;

    public ClientThread(Socket s) throws IOException {
        server = s;
        in = new BufferedReader(new InputStreamReader((server.getInputStream())));
    }

    /*public ClientThread ( int port , int id , int freq ) {
        this.port = port;
        this.id = id;
        this.freq = freq;
    }

     */

    @Override
    public void run ( ) {
        //parseRequest ( );
        String RespostaServer = null;
        try {
            while (true) {
                RespostaServer = in.readLine();

                if (RespostaServer == null) break;

                System.out.println("Client: " + RespostaServer);
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
            while ( true ) {
                System.out.println ( "Sending Data" );
                try {
                    // if(sem.tryAcquire(1, TimeUnit.SECONDS)) {
                    socket = new Socket ( "localhost" , port );
                    out = new DataOutputStream ( socket.getOutputStream ( ) );
                    in = new BufferedReader ( new InputStreamReader ( socket.getInputStream ( ) ) );
                    out.writeUTF ( "My message number " + i + " to the server " + "I'm " + id );
                    String response;
                    response = in.readLine ( );
                    System.out.println ( "From Server " + response );
                    out.flush ( );
                    socket.close ( );
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
