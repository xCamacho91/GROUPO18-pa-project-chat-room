import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FilterMessage extends Thread {
    /**
     * path to the file with the profanity words
     */
    private static final String fileProfanity = "server/filtro.txt";
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
    public FilterMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return filtered message
     */
    public String filter (){
        File profanity = new File (fileProfanity);
        try{
            Scanner readerFile = new Scanner(profanity);
            while (readerFile.hasNextLine()){
                filterWords.add(readerFile.nextLine());
            }
            readerFile.close();
            for (String word : filterWords) {
                message = message.replace(word, "****");
            }
        }catch(IOException e){
            System.out.println("Filter file not found.");
            e.printStackTrace();
        }
        return message;
    }

}
