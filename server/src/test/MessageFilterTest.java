import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageFilterTest {

    private static ArrayList<String> filterWords = new ArrayList<String>();


    private static Queue<String> messageQueue = new ConcurrentLinkedQueue<>();

    private static Queue<String> filteredQueue = new ConcurrentLinkedQueue<>();

    @Test
    public void TestMessageFilter() throws FileNotFoundException {
        File profanity = new File ("filtro.txt");
        Scanner readerFile = new Scanner(profanity);
        while (readerFile.hasNextLine()){
            filterWords.add(readerFile.nextLine());
        }
        readerFile.close();

        String message = "youre a dumbass";

        messageQueue.add(message);
        MessageFilter filter = new MessageFilter(4, messageQueue, filteredQueue, filterWords);
        filter.start();

        while (!filteredQueue.isEmpty()) {
            String messageFiltered = filteredQueue.poll();
            assertEquals("youre a dumb****", messageFiltered);
        }

    }
}
