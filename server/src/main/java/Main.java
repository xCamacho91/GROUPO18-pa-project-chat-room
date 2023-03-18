import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import static java.lang.Integer.parseInt;

public class Main {

    /**
     * The port where the server is going to run in.
     */
    private static int PORT;

    /**
     * The server's configuration, imported from the configuration file when the server started.
     */
    private static Properties serverConfig;

    /**
     * The semaphore responsible for the number of requests that can be served simultaneously.
     */
    private static Semaphore numberOfConcurrentRequests;
    private static ArrayList<ServerThread> clients = new ArrayList<>();
    private static int id;
    private static ExecutorService pool = Executors.newFixedThreadPool(4);


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
            PORT = Integer.parseInt(serverConfig.getProperty("server.port"));
            numberOfConcurrentRequests = new Semaphore( parseInt(serverConfig.getProperty("server.maximum.users")));
        } catch (IOException e) {
            System.out.println("Config file not found.");
            throw new RuntimeException(e);
        }
    }

    public static void main ( String[] args ) throws IOException {

        initializeSettings();

        ServerSocket listener = new ServerSocket(PORT);
        System.out.println("Server is now available");
        while (true){

            Socket client =  listener.accept();
            System.out.println("Client" + id + " connected.");
            ServerThread clientThread = new ServerThread(client, clients, id);
            clients.add(clientThread);

            pool.execute(clientThread);
            id++;
        }
    }
}
