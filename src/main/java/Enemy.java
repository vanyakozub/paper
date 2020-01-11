import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Enemy  extends Thread {
    public String fromUser = new String();
    private Socket socket;
    private PrintWriter out;
    public Enemy(Socket s) throws Exception {
        this.socket = s;
        this.out = new PrintWriter(socket.getOutputStream(),true);
    }

    public void run(){
        /*try {
            while (true){
                act();
            }

        }
        catch (Throwable t) {

        }*/

    }
    public void act() throws Exception {
        out = new PrintWriter(socket.getOutputStream(), true);

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        {

            fromUser = stdIn.readLine();
            if (fromUser != null) {
                //System.out.println("Client: " + fromUser);
                out.println(fromUser);
                if(fromUser.equals("Bye.")){
                    System.exit(0);
                }
            }
        }
    }
    public void sendMesssage (String changes) {
        out.println(changes);
    }
}
