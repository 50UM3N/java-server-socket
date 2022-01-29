

public class AffineCipher {
    private int a, b;

    public AffineCipher(int _a, int _b) {
        a = _a;
        b = _b;
    }

    public String encryptMessage(char[] msg) {
        String cipher = "";
        for (int i = 0; i < msg.length; i++) {
            if (msg[i] != ' ' && msg[i] != ':')
                cipher = cipher + (char) ((((a * (msg[i] - 'A')) + b) % 26) + 'A');
            else
                cipher += msg[i];
        }
        return cipher;
    }

    public String decryptCipher(String cipher) {
        String msg = "";
        int a_inv = 0;
        int flag = 0;
        for (int i = 0; i < 26; i++) {
            flag = (a * i) % 26;
            if (flag == 1)
                a_inv = i;
        }
        for (int i = 0; i < cipher.length(); i++) {
            if (cipher.charAt(i) != ' ' && cipher.charAt(i) != ':')
                msg = msg + (char) (((a_inv * ((cipher.charAt(i) + 'A' - b)) % 26)) + 'A');
            else
                msg += cipher.charAt(i);
        }
        return msg;
    }
}
