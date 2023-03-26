import java.util.ArrayList;

public class FilterMessage extends Thread {
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

}
