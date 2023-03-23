import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
public class ServerTest {

    private static ArrayList<String> filterWords = new ArrayList<String>();

    @Test
    public void TestServerLog() throws FileNotFoundException {
        File profanity = new File ("filtro.txt");
        Scanner readerFile = new Scanner(profanity);
        while (readerFile.hasNextLine()){
            filterWords.add(readerFile.nextLine());
        }
        readerFile.close();

        String message = "youre a dumbass";
        FilterMessage server = new FilterMessage(filterWords, message);
        String filterMessage = server.filter();
        assertEquals("youre a dumb****",filterMessage);
    }

}
