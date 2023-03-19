import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class ServerTest {
    @Test
    public void TestServerLog() throws IOException {
        ServerSocket listener = new ServerSocket(8080);
        Socket client = listener.accept();
        String message = "youre a dumbass";
        ConnectionHandler server = new ConnectionHandler(client, new ArrayList<>(), 8080);
        String filterMessage = server.filter("filtro.txt",message);
        assertEquals("youre a dumb****",filterMessage);
    }

}
