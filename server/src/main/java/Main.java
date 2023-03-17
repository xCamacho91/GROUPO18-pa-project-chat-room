import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static sun.management.jmxremote.ConnectorBootstrap.PropertyNames.PORT;

public class Main {

    private static final int PORT = 1999;
    private static ArrayList<ServerThread> clients = new ArrayList<>();
    private static int id;
    private static ExecutorService pool = Executors.newFixedThreadPool(4);
    public static void main ( String[] args ) throws IOException {

        ServerSocket listener = new ServerSocket(PORT);
        System.out.println("Chat room abriu!!!");
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
