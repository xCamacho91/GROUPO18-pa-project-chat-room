import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogClientTest {

    private static ReentrantLock lockWriteFile = new ReentrantLock();
    @Test
    public void testRunMethod() throws FileNotFoundException {

        LogClient cli = new LogClient ("MESSAGE", 2, "ServerLog Test2", lockWriteFile);
        cli.run();
        String lastLine = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        File file = new File("server.log");
        Scanner fileReader = new Scanner(file);
        while(fileReader.hasNext()){
            lastLine = fileReader.nextLine();
        }
        assertEquals(timestamp+" - Action : MESSAGE - CLIENT2 - \"ServerLog Test2\"",lastLine);
        // is working but the timestamp is not the same as the one in the file so the test fails
        // the path for the server.log file is different for the directory in the method itself
    }

}
