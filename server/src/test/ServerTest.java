import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
public class ServerTest {
    @Test
    public void TestServerLog(){
        String lastLine = "";
        ServerThread server = new ServerThread(8888);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        server.serverLog(timestamp,1,2,"ServerLog Test2");
        server.serverLog(timestamp,1,2,"ServerLog Test2");
        try {
            File file = new File("serverLog.txt");
            Scanner fileReader = new Scanner(file);
            while(fileReader.hasNext()){
                lastLine = fileReader.nextLine();
            }
            assertEquals(timestamp+" - Action : 1 - CLIENT2 - ServerLog Test2",lastLine);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
