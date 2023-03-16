import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
public class ClientTest {
    @Test
    public void TestServerLog(){
        String lastLine = "";
        ClientThread client = new ClientThread(8888,1,222);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            client.serverLog(timestamp,"MESSAGE",2,"ServerLog Test2");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            File file = new File("serverLog.txt");
            Scanner fileReader = new Scanner(file);
            while(fileReader.hasNext()){
                lastLine = fileReader.nextLine();
            }
            assertEquals(timestamp+" - Action : MESSAGE - CLIENT2 - \"ServerLog Test2\"",lastLine);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
