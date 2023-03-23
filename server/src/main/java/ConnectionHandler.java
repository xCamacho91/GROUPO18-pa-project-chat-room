import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Integer.parseInt;

public class ConnectionHandler implements Runnable {
    private static ArrayList<String> Filter_Words = new ArrayList<String>();
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;
    private int id;
    private ArrayList<ConnectionHandler> clients;
    /**
     * The message received from the client.
     */
    private static String message;

    private final ExecutorService executor;

    /**
     * list of profanity words
     */
    private static ArrayList<String> filterWords = new ArrayList<String>();

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
    }*/

    /**
     * Constructor for the thread responsible for handling client connections.
     *
     * @param clientSocket
     * @param clients
     * @param id
     * @throws IOException
     */
    public ConnectionHandler(Socket clientSocket, ArrayList<ConnectionHandler> clients, int id, int numberOfConcurrentRequests, ArrayList<String> filterWords ) throws IOException {
        this.client = clientSocket;
        this.clients = clients;
        this.id= id;
        this.filterWords = filterWords;
        this.executor = Executors.newFixedThreadPool(numberOfConcurrentRequests);
        in = new BufferedReader(new InputStreamReader((client.getInputStream())));
        out = new PrintWriter(client.getOutputStream(), true);
    }


    /**
     * The thread's run method.
     * Accepts clients and creates a new thread to serve each individual client.
     */
    @Override
    public void run ( ) {
        try{
            while (true){
                String request = in.readLine();
                if (request.contains("")){
                    int firstSpace = request.indexOf("");
                    if (request.startsWith("/sair")){
                        shutdown();
                        System.out.println("Client" + id +" disconnected.");
                    }else{
                        message = request.substring(firstSpace+0);
                        FilterMessage filterMessage = new FilterMessage(filterWords, message); // TODO tocar isto por uma thread pool
                        message = filterMessage.filter();

                        Broadcast(message);
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

    /*
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

                    out.println ( filter("profanity_words.txt", message) );

                } catch ( IOException e ) {
                    e.printStackTrace ( );
                }
            }
        //});
        //t.start();
    }*/

    private void Broadcast(String message) { //funcao que vai mandar mensagem para todos os clients
        for (ConnectionHandler aClient : clients ){
            aClient.out.println(message);
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

