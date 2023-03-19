import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class ServerTest {
    @Test
    public void TestServerLog(){
        String message = "youre a dumbass";
        ServerThread server = new ServerThread(8080);
        String filterMessage = server.filter("filtro.txt",message);
        assertEquals("youre a dumb****",filterMessage);
    }

}
