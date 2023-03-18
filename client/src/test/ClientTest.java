import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.DirectoryStream.Filter;
import java.sql.Time;
import java.io.IOException;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
public class ClientTest {
    @Test
    public void TestServerLog(){
        String lastLine = "";
        Socket socket = null;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            socket = new Socket("127.0.0.1",1999);
            ClientThread client = new ClientThread(socket);
            client.serverLog(timestamp,"MESSAGE",2,"ServerLog Test2");
        } catch (InterruptedException | IOException e) {
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
        
        String filterMessage = server.filter("..\\profanity_words.txt","youre a dumbass");
        assertEquals("youre a dumb****",filterMessage);

    }

}
