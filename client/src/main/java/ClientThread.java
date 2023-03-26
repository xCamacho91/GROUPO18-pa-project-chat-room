
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class ClientThread extends Thread {

    private final BufferedReader in;
    private final ReentrantLock lockWriteFile;

    public ClientThread(Socket s, ReentrantLock lockWriteFile) throws IOException {
        this.in = new BufferedReader(new InputStreamReader((s.getInputStream())));
        this.lockWriteFile = lockWriteFile;
    }

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
