import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.locks.ReentrantLock;

public class LogClient extends Thread {

    /**
     * The path of the file to save the requests' information to.
     */
    private final String serverLogFileName;

    /**
     * The time the action was performed
     */
    private final Timestamp timestamp;
    /**
     * code for the action WIP
     */
    private final String action;
    /**
     * ID of the client that performed the action
     */
    private final int clientID;
    /**
     * message sent by the client
     */
    private final String message;
    /**
     * The lock responsible for the log document, which contains a list of requests information.
     */
    private final ReentrantLock lockWriteFile;

    /**
     * The constructor of the thread that logs the requests' information.
     *
     * @param timestamp - The time the action was performed
     * @param action - code for the action WIP
     * @param clientID - ID of the client that performed the action
     * @param message - message sent by the client
     * @param lockWriteFile - The lock responsible for the log document, which contains a list of requests information.
     * @param serverLogFileName - The path of the file to save the requests' information to.
     */
    public LogClient(Timestamp timestamp, String action, int clientID, String message, ReentrantLock lockWriteFile, String serverLogFileName) {
        this.timestamp = timestamp;
        this.action = action;
        this.clientID = clientID;
        this.message = message;
        this.lockWriteFile = lockWriteFile;
        this.serverLogFileName = serverLogFileName;
    }

    @Override
    public void run () {
        lockWriteFile.lock();
        createFile(serverLogFileName);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(timestamp).append(" - Action : ").append(action).append(" - CLIENT ").append(clientID);
        if (!message.isEmpty()) {
            stringBuilder.append(" - \"").append(message).append("\"\n");
        } else {
            stringBuilder.append("\n");
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
