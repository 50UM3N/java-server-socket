import java.net.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
class ioClient {
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
            sendString = affineCipher.encryptMessage(sendString.toUpperCase().toCharArray());
            String hash = sha256(sendString);
            output.writeUTF(NAME + "::" + sendString+"::"+hash);
            output.flush();
            // reading and printing the received data
            receivedString = input.readUTF();
            String message[] = receivedString.split("::",3);
            if(message[2].equals(sha256(message[1])))
                System.out.println(message[0]+": "+affineCipher.decryptCipher(message[1]).toLowerCase());
            else 
                System.out.println("Error: Hash not matched");
        }
        System.out.println("Closing connection: " + NAME + "@" + PORT);
        output.close();
        socket.close();
    }
}