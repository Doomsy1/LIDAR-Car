import java.io.*;
import java.net.*;
import java.util.Scanner;


//import threads

public class SocketClient {

    private Socket client;
    private PrintWriter out;
    private BufferedReader in;


    public SocketClient(String ip, int port) throws IOException {

        startConnection(ip, port);

    }
    public void startConnection(String ip, int port) throws IOException {
        
        client = new Socket(ip, port);
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));

    }

    public void sendMessage(String msg) {

        out.println(msg);

    }

    public String getMessage() throws IOException {
        String msg;
        if(in.ready()){

            msg = in.readLine();
            return msg;

        }
        else{

            return "No message";

        }
    }

    public void stopConnection() throws IOException {

        in.close();
        out.close();
        client.close();

    }

    public static void main(String[] args) throws IOException {

        
        Scanner kb = new Scanner(System.in);

        System.out.println("Enter the IP: ");
        String ip = kb.next();

        System.out.println("Enter the Port: ");
        int port = kb.nextInt();

        SocketClient client = new SocketClient(ip, port); 
        
        Thread messageListener = new Thread(() -> {
            try {
                while (true) {
                    String response = client.getMessage();
                    if (!response.equals("No message")) {
                        System.out.println(response);
                    }
                    // Add a small delay to prevent excessive CPU usage
                    Thread.sleep(2);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        messageListener.start(); // Start listening for messages in the background


        while(true){
            String msg = kb.next();
            client.sendMessage(msg);
        }

        // client.stopConnection();

}
}