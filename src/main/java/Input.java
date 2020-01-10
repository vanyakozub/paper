import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Input  extends Thread {

    private Socket socket;
    public Input(Socket s){this.socket = s;}

    public void run(){
        try {
            while (true){
                act();
            }

        }
        catch (Throwable t) {

        }

    }
    public void act() throws Exception {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromUser;
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
}
