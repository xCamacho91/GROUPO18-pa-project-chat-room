import java.util.ArrayList;

public class FilterMessage implements Runnable {
    /**
     * list of profanity words
     */
    private static ArrayList<String> filterWords = new ArrayList<String>();
    /**
     * unfiltered message
     */
    private String message;

    /**
     * @param message - unfilterd message
     */
    public FilterMessage(ArrayList<String> filterWords, String message) {
        this.filterWords = filterWords;
        this.message = message;
    }

    /**
     *
     * @return filtered message
     */
    public String filter (){
        for (String word : filterWords) {
            message = message.replace(word, "****");
        }
        return message;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "Processing message: " + message);
        filter();
        System.out.println(Thread.currentThread().getName() + "Message Processed " + message);
    }

}
