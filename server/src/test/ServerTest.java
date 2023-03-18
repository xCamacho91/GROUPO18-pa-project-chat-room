import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class ServerTest {
    @Test
    public void TestServerLog(){

        ServerThread server = new ServerThread(1999);
        String filterMessage = server.filter("..\\profanity_words.txt","youre a dumbass");
        assertEquals("youre a dumb****",filterMessage);
        //:TODO erro teste
    }

}
