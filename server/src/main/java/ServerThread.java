import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import javax.swing.plaf.basic.BasicListUI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import static java.lang.Integer.parseInt;

public class ServerThread implements Runnable {
    private int port;
    private ServerSocket server;
    private Socket socket;
    private static Semaphore semaphore;
    private static ArrayList<String> Filter_Words = new ArrayList<String>();
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;
    private int id;
    private ArrayList<ServerThread> clients;


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
    }

    public ServerThread(Socket clientSocket, ArrayList<ServerThread> clients, int id) throws IOException {
        this.client = clientSocket;
        this.clients = clients;
        this.id= id;
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

    
    /** 
     * @param FileProfanity  - name of the file with the words to filter
     * @param message - unfilterd message 
     * @return filtered message
     */
    public String filter (String FileProfanity,String message){
        File profanity = new File ( FileProfanity );
        try{
            Scanner reader_file = new Scanner(profanity);
            while (reader_file.hasNextLine()){
                Filter_Words.add(reader_file.nextLine());
            }
            reader_file.close();
            for (String word : Filter_Words) {
                message = message.replace(word, "****");

            }
        }catch(IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return message;
    }
}

