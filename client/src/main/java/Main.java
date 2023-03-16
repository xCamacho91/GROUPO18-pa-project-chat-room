import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;

public class Main {

    private static final  String IP = "127.0.0.1";
    private static final int PORT = 1999;

    public static void main ( String[] args ) throws IOException {
        Socket socket = new Socket(IP,PORT);
        Server servercon = new Server (socket);
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        new Thread(servercon).start();

        while (true){
            System.out.println("- ");
            String command = keyboard.readLine();

            if(command.equals("/sair")) break;
            out.println(command);

        }
        socket.close();
        System.exit(0);

    }
}
