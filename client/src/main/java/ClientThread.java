import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.concurrent.locks.ReentrantLock;

public class ClientThread extends Thread {
    private final int port;
    private final int id;
    private final int freq;
    private final String serverLogFileName = "server/server.log";
    private final String MESSAGE = "MESSAGE";
    private final String DISCONNECT = "DISCONNECT";
    private final String CONNECT = "CONNECT";
    private final String WAITING = "WAITING";
    private DataOutputStream out;
    private BufferedReader in;
    private Socket socket;
    private ReentrantLock lockWriteFile;

    public ClientThread ( int port , int id , int freq ) {
        this.port = port;
        this.id = id;
        this.freq = freq;

        lockWriteFile = new ReentrantLock();

        createFile(serverLogFileName);
    }

    public void run ( ) {
        parseRequest ( );
    }

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
    }

    /**
     *
     * @param timestamp - the time that the message was sent
     * @param action - code for the action WIP
     * @param clientID - ID of the client that performed the action
     * @param message - message sent by the client
     * @throws InterruptedException
     */
    public void serverLog(Timestamp timestamp, String action, int clientID, String message) throws InterruptedException {
        Thread t = new Thread( ()-> {
            lockWriteFile.lock();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(timestamp).append(" - Action : ").append(action).append(" - CLIENT").append(clientID);
            if (!message.isEmpty()) {
                stringBuilder.append(" - \"").append(message).append("\"\n");
            }
            writeFile(serverLogFileName, stringBuilder.toString());
            lockWriteFile.unlock();
        });
        t.start();
    }

    /**
     *
     * @param fileName - name of the file that will be created
     */
    private static void createFile(String fileName){
        try {
            //create file
            File file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        }catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     *
     * @param fileName - name of the file that will be used
     * @param message - message to write
     */
    private void writeFile(String fileName,String message){
        try {
            FileWriter writer = new FileWriter(fileName,true);
            writer.append(message);
            writer.close();
            System.out.println(message);
        }catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


}
