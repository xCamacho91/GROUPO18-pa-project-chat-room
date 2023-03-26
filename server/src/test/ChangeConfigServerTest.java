import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import org.junit.jupiter.api.Test;

public class ChangeConfigServerTest {
    private ArrayList<String> filterWords= new ArrayList<>();
    private Semaphore numberOfConcurrentRequests = new Semaphore(4);

    /**
     * The test for the changing configurations
     * @throws IOException
     */
    @Test
    public void testRunMethod() throws IOException {
        File profanity = new File ("filtro.txt");
        Scanner readerFile = new Scanner(profanity);
        while (readerFile.hasNextLine()){
            filterWords.add(readerFile.nextLine());
        }
        readerFile.close();

        ChangeConfigServer change = new ChangeConfigServer(numberOfConcurrentRequests, filterWords);
        change.handleFilter("red");
        assertEquals(filterWords.contains("red"), true);
        change.handleFilter("red");
        assertEquals(filterWords.contains("red"), false);
        change.chageNumberOfConcurrentRequests("9");
        assertEquals(numberOfConcurrentRequests.availablePermits(), 9);
        }
    }
