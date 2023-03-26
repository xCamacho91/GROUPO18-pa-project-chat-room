import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChangeConfigServerTest {
    private final ArrayList<String> filterWords= new ArrayList<>();
    private final Semaphore numberOfConcurrentRequests = new Semaphore(4);

    /**
     * Test the methods of the class ChangeConfigServer
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
        assertTrue(filterWords.contains("red"));
        change.handleFilter("red");
        assertFalse(filterWords.contains("red"));
        change.chageNumberOfConcurrentRequests("9");
        assertEquals(numberOfConcurrentRequests.availablePermits(), 9);
        }
    }
