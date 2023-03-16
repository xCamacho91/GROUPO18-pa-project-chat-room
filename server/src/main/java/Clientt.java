import javax.swing.plaf.basic.BasicListUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Clientt implements  Runnable{

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;
    private ArrayList<Clientt> clients;

    public Clientt(Socket clientSocket, ArrayList<Clientt> clients) throws IOException {
        this.client = clientSocket;
        this.clients = clients;
        in = new BufferedReader(new InputStreamReader((client.getInputStream())));
        out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run() {
        try{
            while (true){
                String request = in.readLine();
                if (request.contains("")){ //antes tava say e tinha de escrever say antes de falar
                    int firstSpace = request.indexOf(" ");
                    if (firstSpace != -1){
                        outToAll(request.substring(firstSpace+0)); //antes tava 1
                    }
                }
                else{
                    out.println("cena dos filtros...");
                }
            }

        }  catch (IOException e) {
            shutdown();
        }
    }

    private void outToAll(String massage) {
        for (Clientt aClient : clients ){
            aClient.out.println(massage);
        }
    }

    public void shutdown(){
        done =true;
        try{
            in.close();
            out.close();
            if(!client.isClosed()){
                client.close();
            }
        }catch (IOException e){

        }
    }

}