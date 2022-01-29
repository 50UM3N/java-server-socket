import java.net.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
class ioServer {
    public static String sha256(String source) {
        byte[] hash = null;
        String hashCode = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(source.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("sha256: Can't calculate SHA-256");
        }

        if (hash != null) {
            StringBuilder hashBuilder = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(hash[i]);
                if (hex.length() == 1) {
                    hashBuilder.append("0");
                    hashBuilder.append(hex.charAt(hex.length() - 1));
                } else {
                    hashBuilder.append(hex.substring(hex.length() - 2));
                }
            }
            hashCode = hashBuilder.toString();
        }

        return hashCode;
    }
    public static void main(String args[]) throws Exception {
        AffineCipher affineCipher = new AffineCipher(17,22);
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
            String message[] = receivedString.split("::",3);
            if(message[2].equals(sha256(message[1])))
                System.out.println(message[0]+": "+affineCipher.decryptCipher(message[1]).toLowerCase());
            else 
                System.out.println("Error: Hash not matched");
            // sending new data
            System.out.print(NAME + "@" + PORT + "=> ");
            sendString = bufferReader.readLine();
            sendString = affineCipher.encryptMessage(sendString.toUpperCase().toCharArray());
            String hash = sha256(sendString);
            output.writeUTF(NAME + "::" + sendString+"::"+hash);
            output.flush();
        }
        System.out.println("Closing connection: " + NAME + "@" + PORT);
        input.close();
        accept.close();
        serverSocket.close();
    }
}