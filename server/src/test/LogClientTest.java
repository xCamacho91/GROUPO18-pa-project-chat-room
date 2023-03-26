import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogClientTest {

    private String serverLogFileName = "server.log";
    private static ReentrantLock lockWriteFile = new ReentrantLock();
    @Test
    public void testRunMethod() throws FileNotFoundException {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        LogClient cli = new LogClient (timestamp, "MESSAGE", 2, "ServerLog Test2", lockWriteFile, serverLogFileName);
        cli.run();
        String lastLine = "";

        File file = new File("server.log");
        Scanner fileReader = new Scanner(file);
        while(fileReader.hasNext()){
            lastLine = fileReader.nextLine();
        }
        assertEquals(timestamp+" - Action : MESSAGE - CLIENT 2 - \"ServerLog Test2\"",lastLine);
    }

}
