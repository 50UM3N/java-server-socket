import java.net.*;
import java.io.*;

class ioServer {
    public static void main(String args[]) throws Exception {
        int PORT = Integer.parseInt(args[0]);
        String NAME = args[1];
        // creating server socket
        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket accept = serverSocket.accept();
        DataInputStream input = new DataInputStream(accept.getInputStream());
        DataOutputStream output = new DataOutputStream(accept.getOutputStream());
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
        String status = input.readUTF();
        System.out.println("Connected to: " + status + "@" + PORT);
        output.writeUTF(NAME);

        String receivedString = "", sendString = "";
        while (!receivedString.equals("end")) {
            // reading and printing the received data
            receivedString = input.readUTF();
            System.out.println(receivedString);
            // sending new data
            System.out.print(NAME + "@" + PORT + "=> ");
            sendString = bufferReader.readLine();
            output.writeUTF(NAME + ": " + sendString);
            output.flush();
        }
        System.out.println("Closing connection: " + NAME + "@" + PORT);
        input.close();
        accept.close();
        serverSocket.close();
    }
}