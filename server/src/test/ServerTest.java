import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
public class ServerTest {
    @Test
    public void TestServerLog() {
        String message = "youre a dumbass";
        FilterMessage server = new FilterMessage(message);
        String filterMessage = server.filter();
        assertEquals("youre a dumb****",filterMessage);
    }

}
