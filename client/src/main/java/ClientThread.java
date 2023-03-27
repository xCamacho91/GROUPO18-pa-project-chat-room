
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class ClientThread extends Thread {

    /**
     * The input stream.
     */
    private final BufferedReader in;

    /**
     * The lock responsible for the log document, which contains a list of requests information.
     */
    private final ReentrantLock lockWriteFile;

    /**
     *Constructor for the thread responsible for handling client connections. In this case, for client side.
     *
     * @param s - socket that will receive the inputStream from the buffered reader, from the server
     * @param lockWriteFile - The lock responsible for the log document, which contains a list of requests information.
     * @throws IOException
     */
    public ClientThread(Socket s, ReentrantLock lockWriteFile) throws IOException {
        this.in = new BufferedReader(new InputStreamReader((s.getInputStream())));
        this.lockWriteFile = lockWriteFile;
    }

    /**
     * The thread's run method.
     * Receive the message from server and shows it to the client.
     */
    @Override
    public void run ( ) {
        String RespostaServer;
        try {
            while (true) {
                RespostaServer = in.readLine();

                if (RespostaServer == null) break;

                System.out.println(RespostaServer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
