import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageTest {
    @Test
    public void teste() throws IOException {

        // inicia thread do servidor
        new Thread(() -> {
            try {
                ServerSocket socketServer = new ServerSocket(1999); //cria ligaçao
                Socket socketClient = socketServer.accept();
                PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));

                String mensagemCliente = in.readLine(); //mensagem do cliente, vem na linha 36
                out.println("Oi, " + mensagemCliente); // resposta ao cliente

                out.close();
                in.close();
                socketClient.close();
                socketServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        //cliente conecta ao server
        Socket socket = new Socket("localhost", 1999);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.println("io"); //mensagem cliente -> servidor

        String mensagemServer = in.readLine(); //resposta do servidor

        assertEquals("Oi, io", mensagemServer);

        out.close();
        in.close();
        socket.close();
    }

}

