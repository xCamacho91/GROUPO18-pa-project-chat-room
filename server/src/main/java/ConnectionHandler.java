import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;


public class ConnectionHandler implements Runnable {

    /**
     * The client socket.
     */
    private Socket client;
    /**
     * The input stream.
     */
    private BufferedReader in;
    /**
     * The output stream.
     */
    private PrintWriter out;
    /**
     * The boolean that indicates if the client is connected or not.
     */
    private boolean done;
    /**
     * The id of the client.
     */
    private int id;
    /**
     * The list of clients connected to the server.
     */
    private ArrayList<ConnectionHandler> clients;
    /**
     * The message received from the client.
     */
    private static String message;

    /**
     * list of profanity words
     */
    private static ArrayList<String> filterWords = new ArrayList<String>();
    /**
     * The semaphore responsible for the number of requests that can be served simultaneously.
     */
    private Semaphore numberOfConcurrentRequests;
    /**
     * The type of message received from the client.
     */
    private final static int TYPE_BROADCAST_MESSAGE = 1;
    /**
     * The type of message received from the client.
     */
    private final static int TYPE_BROADCAST_CONNECT = 2;
    /**
     * The type of message received from the client.
     */
    private final static int TYPE_BROADCAST_DISCONNECT = 3;


    /**
     * Constructor for the thread responsible for handling client connections.
     *
     * @param clientSocket
     * @param clients
     * @param id
     * @throws IOException
     */
    public ConnectionHandler(Socket clientSocket, ArrayList<ConnectionHandler> clients, int id, Semaphore numberOfConcurrentRequests, ArrayList<String> filterWords ) throws IOException {
        this.client = clientSocket;
        this.clients = clients;
        this.id= id;
        this.filterWords = filterWords;
        this.numberOfConcurrentRequests = numberOfConcurrentRequests;
        in = new BufferedReader(new InputStreamReader((client.getInputStream())));
        out = new PrintWriter(client.getOutputStream(), true);
    }

    /**
     * The thread's run method.
     * Accepts clients and creates a new thread to serve each individual client.
     */
    @Override
    public void run ( ) {
        System.out.println("Client " + id + " connected.");
        Broadcast("", id, TYPE_BROADCAST_CONNECT);

        try {
            OutputStream outputStream = client.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeInt(id); // Send the ID to the client
            dataOutputStream.writeInt(numberOfConcurrentRequests.availablePermits()); // Send the number of concurrent requests to the client
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            numberOfConcurrentRequests.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        processClientRequests();
    }

    /**
     * Receives and Broadcasts a message to all clients connected to the server.
     */
    private void processClientRequests() {
        try{
            while (true){
                System.out.println(numberOfConcurrentRequests);

                String request = in.readLine();
                if (request.contains("")){
                    int firstSpace = request.indexOf("");
                    if (request.startsWith("/quit")){
                        shutdown();
                        System.out.println("Client" + id +" disconnected.");
                        Broadcast("", id, TYPE_BROADCAST_DISCONNECT);
                        numberOfConcurrentRequests.release();
                    } else {
                        message = request.substring(firstSpace+0);
                        FilterMessage filterMessage = new FilterMessage(filterWords, message); // TODO tocar isto por uma thread pool
                        message = filterMessage.filter();

                        Broadcast(message, id, TYPE_BROADCAST_MESSAGE);

                    }
                } else {
                    out.println("...");
                }
                System.out.println("Client" + id +": "  + request);
            }
        }  catch (IOException e) {
            shutdown();
            numberOfConcurrentRequests.release();
        } catch ( NullPointerException e){
            shutdown();
            numberOfConcurrentRequests.release();
        }
    }

    private void Broadcast(String message, int id, int type) {
        for (ConnectionHandler aClient : clients ){
            if (aClient.id!=id) {
                switch (type) {
                    case TYPE_BROADCAST_MESSAGE:
                        aClient.out.println("Client " + id + ": " + message);
                        break;
                    case TYPE_BROADCAST_CONNECT:
                        aClient.out.println("Client "+ id + " " +"connected to chat");
                        break;
                    case TYPE_BROADCAST_DISCONNECT:
                        aClient.out.println("Client "+ id + " " +"disconnected from chat");
                        break;
                }
            }
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

