import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import javax.swing.plaf.basic.BasicListUI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import static java.lang.Integer.parseInt;

public class ServerThread implements Runnable {
    //private final int port;
    private ServerSocket server;

    private static Semaphore semaphore;
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;
    private int id;
    private ArrayList<ServerThread> clients;


    /**
     * The server's configuration, imported from the configuration file when the server started.
     */
    private static Properties serverConfig;

    /**
     * The semaphore responsible for the number of requests that can be served simultaneously.
     */
    private static Semaphore numberOfConcurrentRequests;


    /*public ServerThread ( int port) {
        this.port = port;

        try {
            server = new ServerSocket ( this.port );
        } catch ( IOException e ) {
            e.printStackTrace ( );
        }
    }
    */
    public ServerThread(Socket clientSocket, ArrayList<ServerThread> clients, int id) throws IOException {
        this.client = clientSocket;
        this.clients = clients;
        this.id= id;
        in = new BufferedReader(new InputStreamReader((client.getInputStream())));
        out = new PrintWriter(client.getOutputStream(), true);
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
    @Override
    public void run ( ) {
        initializeSettings("C:/Users/user/IdeaProjects/GROUPO18-pa-project-chat-room/server/server.config");

        try{
            while (true){
                String request = in.readLine();
                if (request.contains("")){
                    int firstSpace = request.indexOf("");
                    if (request.startsWith("/sair")){
                        shutdown();
                        System.out.println("Client" + id +" disconnected.");
                    }else{
                        Broadcast(request.substring(firstSpace+0));
                    }
                }
                else{
                    out.println("...");
                }
                System.out.println("Client" + id +": "  + request);
            }

        }  catch (IOException e) {
            shutdown();
        }
        //processRequests();
    }

    /*private void processRequests() {
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
     */

    private void Broadcast(String massage) { //funcao que vai mandar mensagem para todos os clients
        for (ServerThread aClient : clients ){
            aClient.out.println(massage);
        }
    }

    public void shutdown(){
        done =true;
        try{
            in.close();
            out.close();
            if(!client.isClosed()){
                client.close();
            }
        }catch (IOException e){

        }
    }


}
