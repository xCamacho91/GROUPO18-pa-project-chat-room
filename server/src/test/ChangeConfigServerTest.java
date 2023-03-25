import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import org.junit.jupiter.api.Test;

public class ChangeConfigServerTest {

    private ArrayList<String> filterWords= new ArrayList<>();

    private Semaphore numberOfConcurrentRequests = new Semaphore(4);
    @Test
    public void testRunMethod() throws IOException {
        File profanity = new File ("filtro.txt");
        Scanner readerFile = new Scanner(profanity);
        while (readerFile.hasNextLine()){
            filterWords.add(readerFile.nextLine());
        }
        readerFile.close();
        String promtUser = "9";
        String promtFilter = "red";
        ChangeConfigServer change = new ChangeConfigServer(numberOfConcurrentRequests, filterWords);
        change.handleFilter(promtFilter);
        assertEquals(filterWords.contains("red"), true);
        change.handleFilter(promtFilter);
        assertEquals(filterWords.contains("red"), false);
        change.chageNumberOfConcurrentRequests(promtUser);
        assertEquals(numberOfConcurrentRequests.availablePermits(), 9);
        }
    }
