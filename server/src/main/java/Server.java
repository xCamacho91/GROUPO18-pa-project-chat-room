import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import static java.lang.Integer.parseInt;

public class Server {



    /**
     * The port where the server is going to run in.
     */
    private static int PORT;

    /**
     * The server's configuration, imported from the configuration file when the server started.
     */
    private static Properties serverConfig;

    /**
     * The number of concurrent requests that the server can handle.
     */
    private static Semaphore numberOfConcurrentRequests;

    /**
     * The list of clients connected to the server.
     */
    private static ArrayList<ConnectionHandler> clients = new ArrayList<>();
    /**
     * The id of the client.
     */
    private static int id;
    /**
     * The thread pool.
     */
    private static ExecutorService pool = Executors.newFixedThreadPool(4);

    /**
     * path to the file with the profanity words
     */
    private static final String fileProfanity = "server/filtro.txt";

    /**
     * list of profanity words
     */
    private static ArrayList<String> filterWords = new ArrayList<String>();

    /**
     * Constructor for the thread responsible for accepting the clients.
     *
     * @throws IOException if an I/O error occurs when creating the output stream or if the socket is not connected.
     **/
    private static void initializeSettings() {
        try {
            serverConfig = new Properties();
            InputStream configPathInputStream = new FileInputStream("server/server.config");
            serverConfig.load(configPathInputStream);
            PORT = parseInt(serverConfig.getProperty("server.port"));
            numberOfConcurrentRequests = new Semaphore(parseInt(serverConfig.getProperty("server.maximum.users"), 10));
        } catch (IOException e) {
            System.out.println("Config file not found.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads the file with the profanity words and adds them to the arraylist filterWords
     * @throws FileNotFoundException
     */
    private static void readFilterFile() throws FileNotFoundException {
        File profanity = new File (fileProfanity);
        Scanner readerFile = new Scanner(profanity);
        while (readerFile.hasNextLine()){
            filterWords.add(readerFile.nextLine());
        }
        readerFile.close();
    }

    public static void main ( String[] args ) throws IOException, InterruptedException {
        initializeSettings();
        readFilterFile();
        ServerSocket listener = new ServerSocket(PORT);
        System.out.println("Server is now available");

        while (true){

            Socket client =  listener.accept();
            System.out.println("Client" + id + " connected.");
            ConnectionHandler connectHandle = new ConnectionHandler(client, clients, id, numberOfConcurrentRequests, filterWords);
            clients.add(connectHandle);
            pool.execute(connectHandle);
            id++;


        }

    }
}
