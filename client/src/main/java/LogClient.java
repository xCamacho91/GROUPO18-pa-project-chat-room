import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.locks.ReentrantLock;

public class LogClient extends Thread {

    private final String serverLogFileName = "server/server.log";

    private String action;
    private int clientID;
    private String message;
    private ReentrantLock lockWriteFile;

    /**
     *
     * @param action - code for the action WIP
     * @param clientID - ID of the client that performed the action
     * @param message - message sent by the client
     */
    public LogClient(String action, int clientID, String message, ReentrantLock lockWriteFile) {
        this.action = action;
        this.clientID = clientID;
        this.message = message;
        this.lockWriteFile = lockWriteFile;
    }

    public void run () {
        lockWriteFile.lock();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        createFile(serverLogFileName);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(timestamp).append(" - Action : ").append(action).append(" - CLIENT").append(clientID);
        if (!message.isEmpty()) {
            stringBuilder.append(" - \"").append(message).append("\"\n");
        }
        writeFile(serverLogFileName, stringBuilder.toString());
        lockWriteFile.unlock();
    }


    /**
     *
     * @param fileName - name of the file that will be created
     */
    private static void createFile(String fileName){
        try {
            //create file
            File file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            }
        }catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     *
     * @param fileName - name of the file that will be used
     * @param message - message to write
     */
    private void writeFile(String fileName,String message){
        try {
            FileWriter writer = new FileWriter(fileName,true);
            writer.append(message);
            writer.close();
        }catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
