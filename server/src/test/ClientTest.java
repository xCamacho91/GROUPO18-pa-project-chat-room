import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class ClientTest {
    @Test
    public void TestServerLog() throws IOException {

        ServerSocket listener = new ServerSocket(8080);

        ClientThread cli = new ClientThread (new Socket("127.0.0.1",8080), new ReentrantLock());

        String lastLine = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            cli.serverLog(timestamp,"MESSAGE",2,"ServerLog Test2", new ReentrantLock());
            cli.serverLog(timestamp,"MESSAGE",2,"ServerLog Test2", new ReentrantLock());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            File file = new File("server.log");
            Scanner fileReader = new Scanner(file);
            while(fileReader.hasNext()){
                lastLine = fileReader.nextLine();
            }
            assertEquals(timestamp+" - Action : MESSAGE - CLIENT2 - \"ServerLog Test2\"",lastLine);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        //:TODO erro teste
    }

}
