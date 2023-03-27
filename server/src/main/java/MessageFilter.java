import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageFilter {
    /**
     * The thread pool.
     */
    private final ExecutorService executor;
    /**
     * The queue of messages.
     */
    private final Queue<String> messageQueue;
    /**
     * The queue of filtered messages.
     */
    private final Queue<String> filteredQueue;

    /**
     * The list of words to filter.
     */
    private ArrayList<String> filterWords;

    /**
     * Constructor of the MessageFilter class.
     * @param numThreads the number of threads to use.
     * @param messageQueue the queue of messages.
     * @param filteredQueue the queue of filtered messages.
     * @param filterWords the list of words to filter.
     */
    public MessageFilter(int numThreads, Queue<String> messageQueue, Queue<String> filteredQueue, ArrayList<String> filterWords) {
        this.messageQueue = messageQueue;
        this.filteredQueue = filteredQueue;
        this.executor = Executors.newFixedThreadPool(numThreads);
        this.filterWords = filterWords;
    }
    /**
     *
     * @return filtered message
     */
    public String filter (String message) {
        for (String word : filterWords) {
            message = message.replace(" " + word + " ", "****");
            message = message.replace( word + " ", "****");
            message = message.replace( " " + word, "****");
            message = message.replace(word, "****");
        }
        return message;
    }

    /**
     * Starts the filter.
     */
    public void start() {
        while (!messageQueue.isEmpty()) {
            String message = messageQueue.poll();
            executor.execute(() -> {
                String filtedMessage = filter(message);
                filteredQueue.add(filtedMessage);
            });
        }
        executor.shutdown();
    }
}
