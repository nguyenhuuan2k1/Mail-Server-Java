package nhom50.CLIENT.Test;

import com.google.gson.Gson;
import nhom50.Encrypt.Hybrid_Encryption;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Scanner;

public class Client {
    private static Socket socket;
    private static ServerSocket serverSocket;
    private static BufferedReader in = null;
    private static BufferedWriter out = null;

    private static final String PUBLIC_KEY_SERVER = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbPndbAp25koChNaXO9XfZHLBEVKWedG5c2Inio657AePBaYzYISc2ucXwHDzn+xJsFbthGzyt+CYsnVdrtwpVB3Pv7TpWnj2W2l0yG5vrOjsUERVBaC+6Mk1+RNXRimqxCJDtJTtXeB9/bZGXBe4WcPXUhwIB563JPyAGTyeVnwIDAQAB";
    private static String randomKey = "";
    private static PublicKey pKey;

    public static void main(String[] args) {
        try {
            pKey = Hybrid_Encryption.getPublicKeyRSA(PUBLIC_KEY_SERVER);

            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256); // for example

            SecretKey secretKey = keyGen.generateKey();
            randomKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());


            socket = new Socket("localhost", 5000);
            socket.setReuseAddress(true);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            Scanner sc = new Scanner(System.in);
            String line = "";
            String recived = "";
            new Listener().start();
            new Sender().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Listener extends Thread {
        public void run() {
            while (true) {
                try {
                    String line = in.readLine();
                    System.out.println("Client recived encrypt String :" + line);
                    if (line != null) {
                        // client nhận được string mã hoá từ server và tiến hành giải mã
                        String decrypt = Hybrid_Encryption.decryptAES(line, randomKey);
                        System.out.println("Client recived decrypt String :" + decrypt);
                    }
                } catch (Exception e) {
                }

            }
        }
    }

    static class Sender extends Thread {
        @Override
        public void run() {
            try {
                Scanner sc = new Scanner(System.in);
                String line = "";
                // ví dụ về client gửi request lên server để đọc hết dữ liệu trong file
                String command = "cmd_docfile";
                String data = Hybrid_Encryption.encryptAES(command, randomKey);
                String encryptRandomKey = Hybrid_Encryption.encryptRSA(randomKey, pKey);
                HashMap<String, String> sendServer = new HashMap<>();
                sendServer.put("key", encryptRandomKey);
                sendServer.put("value", data);
                String strSend = new Gson().toJson(sendServer); // output mã hoá
                System.out.println("Client sent encrypt String" + strSend);
                out.write(strSend);
                out.newLine();
                out.flush();
//                while ((line = sc.nextLine()) != null) {
//                    System.out.println("Input:");
//                    // mã hoá
//                    String data = Hybrid_Encryption.encryptAES(line, randomKey);
//                    String encryptRandomKey = Hybrid_Encryption.encryptRSA(randomKey, pKey);
//
//                    HashMap<String, String> sendServer = new HashMap<>();
//                    sendServer.put("key", encryptRandomKey);
//                    sendServer.put("value", data);
//                    String strSend = new Gson().toJson(sendServer); // output mã hoá
//                    System.out.println("Client sent encrypt String" + strSend);
//                    out.write(strSend);
//                    out.newLine();
//                    out.flush();
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
