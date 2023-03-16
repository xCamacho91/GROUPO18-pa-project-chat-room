import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Server implements Runnable {

    private Socket server;
    private BufferedReader in;

    public Server(Socket s) throws IOException {
        server = s;
        in = new BufferedReader(new InputStreamReader((server.getInputStream())));

    }

    @Override
    public void run() {

            String RespostaServer = null;
            try {
                while (true) {
                    RespostaServer = in.readLine();

                    if (RespostaServer == null) break;

                    System.out.println("Nome utilizador: " + RespostaServer);
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
