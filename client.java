import java.net.*;
import java.io.*;

class ioClient {
    public static void main(String args[]) throws Exception {
        int PORT = Integer.parseInt(args[0]);
        String NAME = args[1];
        // connecting to a server
        Socket socket = new Socket("localhost", PORT);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));

        output.writeUTF(NAME);
        String status = input.readUTF();
        System.out.println("Connected to: " + status + "@" + PORT);

        String sendString = "", receivedString = "";
        while (!sendString.equals("stop")) {
            // sending new data
            System.out.print(NAME + "@" + PORT + "=> ");
            sendString = bufferReader.readLine();
            output.writeUTF(NAME + ": " + sendString);
            output.flush();
            // reading and printing the received data
            receivedString = input.readUTF();
            System.out.println(receivedString);
        }
        System.out.println("Closing connection: " + NAME + "@" + PORT);
        output.close();
        socket.close();
    }
}