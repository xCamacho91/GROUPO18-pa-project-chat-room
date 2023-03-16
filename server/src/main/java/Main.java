import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static sun.management.jmxremote.ConnectorBootstrap.PropertyNames.PORT;

public class Main {

    private static final int PORT = 5001;
    private static ArrayList<Clientt> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(4);
    public static void main ( String[] args ) throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        while (true){
            System.out.println("Espera de clientes...");
            Socket client =  listener.accept();
            System.out.println("Utilizador xxx entrou no chat.");
            Clientt clientThread = new Clientt(client, clients);
            clients.add(clientThread);

            pool.execute(clientThread);


        }


    }
}
