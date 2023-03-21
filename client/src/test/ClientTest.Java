import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class ClientTest {

    @Test
    public void LogClientTest() {

        LogClient cli = new LogClient ("MESSAGE", 1, "ServerLog Test2", new ReentrantLock());
        cli.run();
        String lastLine = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
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
