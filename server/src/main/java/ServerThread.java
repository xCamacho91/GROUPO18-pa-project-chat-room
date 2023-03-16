import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import static java.lang.Integer.parseInt;

public class ServerThread extends Thread {
    private final int port;
    private final String serverLogFileName = "serverLog.txt";
    private DataInputStream in;
    private PrintWriter out;
    private ServerSocket server;
    private Socket socket;
    private static Semaphore semaphore;


    /**
     * The server's configuration, imported from the configuration file when the server started.
     */
    private static Properties serverConfig;

    /**
     * The semaphore responsible for the number of requests that can be served simultaneously.
     */
    private static Semaphore numberOfConcurrentRequests;


    public ServerThread ( int port) {
        this.port = port;

        try {
            server = new ServerSocket ( this.port );
        } catch ( IOException e ) {
            e.printStackTrace ( );
        }

        createFile(serverLogFileName);
    }


    /**
     * Constructor for the thread responsible for accepting the clients.
     *
     * @param configPath path of the configuration file used by the HTTP server.
     * @throws IOException if an I/O error occurs when creating the output stream or if the socket is not connected.
     **/
    private static void initializeSettings(String configPath) {
        Thread t = new Thread( ()-> {
            while (true) {
                try {
                    serverConfig = new Properties();
                    InputStream configPathInputStream = new FileInputStream(configPath);
                    serverConfig.load(configPathInputStream);
                } catch (IOException e) {
                    System.out.println("Settings config path not found.");
                    throw new RuntimeException(e);
                }
            }
        });
        t.start();
    }

    /**
     * The thread's run method.
     * Accepts clients and creates a new thread to serve each individual client.
     */
    public void run ( ) {
        initializeSettings("C:/Users/joaoc/source/PA/GROUPO18-pa-project-chat-room/server/server.config");

        processRequests();
    }

    private void processRequests() {
        //Thread t = new Thread( ()-> {
            while ( true ) {
                try {
                    System.out.println ( "Accepting Data" );
                    socket = server.accept ( );
                    in = new DataInputStream ( socket.getInputStream ( ) );
                    out = new PrintWriter ( socket.getOutputStream ( ) , true );
                    String message = in.readUTF ( );

                    //thread para filtrar o conteudo das mensagens antes de apresentar
                    System.out.println ( "***** " + message + " *****" );
                    // escrita para o ficheiro de log das mensagens

                    out.println ( message.toUpperCase ( ) );

                } catch ( IOException e ) {
                    e.printStackTrace ( );
                }
            }
        //});
        //t.start();
    }

    /**
     *
     * @param timestamp - the time that the message was sent
     * @param action - code for the action WIP TODO:change to use constants
     * @param clientID - ID of the client that performed the action
     * @param message - message sent by the client
     */
    public void serverLog(Timestamp timestamp, int action, int clientID, String message) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(timestamp).append(" - Action : ").append(action).append(" - CLIENT").append(clientID).append(" - ").append(message).append("\n");
        writeFile(serverLogFileName,stringBuilder.toString());
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
        }catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
