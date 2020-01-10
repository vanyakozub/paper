import java.io.*;
import java.net.*;
public class Player extends Thread {

    private String hostName = "localhost";//args[0];
    private static boolean isGreen = false;
    private int portNumber = 9876;//Integer.parseInt(args[1]);
    public String fromServer = new String();
    private Socket kkSocket;
    public Enemy enemy;

    public void run() {
        try {
            while (true){
                begin();
            }

        }
        catch (Throwable t) {

        }
    }
    public boolean getIsGreen() {
        return isGreen;
    }
    public void begin() {
        try {
            kkSocket = new Socket(hostName, portNumber);
            BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
            //String fromServer;
            enemy= new Enemy(kkSocket);
            enemy.start();
            fromServer = in.readLine();
            fromServer = fromServer.trim();
            int playerNumber = Integer.parseInt(fromServer)%2;
            if(fromServer.equals("0")) {
                isGreen = true;
            }
            else {
                isGreen = false;
            }
            System.out.println(isGreen);
            while (true) {
                if ((fromServer = in.readLine()) != null) {
                    System.out.println(fromServer);
                    if (fromServer.equals("Bye.")) {
                        System.exit(0);
                    }
                } else {
                    System.out.println("Server is closed");
                }
            }

    }
        catch (UnknownHostException e) {System.out.println(e.fillInStackTrace());  }
        catch (IOException e) { System.out.println(e.fillInStackTrace()); }
        catch (Exception ex) {System.out.println(ex.fillInStackTrace()); }
}

    public void sendChanges(String changes) {
        enemy.sendMesssage(changes);
}
}
