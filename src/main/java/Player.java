import java.io.*;
import java.net.*;
public class Player {

    private String hostName = "localhost";//args[0];

    private int portNumber = 9876;//Integer.parseInt(args[1]);
    public void begin() {
        try {
            Socket kkSocket = new Socket(hostName, portNumber);
            BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
            String fromServer;
            Input input = new Input(kkSocket);
            input.start();
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
        catch (
    UnknownHostException e) {  }
        catch (
    IOException e) {  }
}
}
