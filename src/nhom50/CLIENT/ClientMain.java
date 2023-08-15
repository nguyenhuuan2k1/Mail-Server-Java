package nhom50.CLIENT;

import nhom50.CLIENT.GUI.screens.LoginScreen;
import nhom50.HELPERS.GlobalHelper;
import nhom50.Encrypt.Hybrid_Encryption;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;

public class ClientMain {
    public static Socket socket;
    public static ObjectInputStream in = null;
    public static ObjectOutputStream out = null;
    public static String PUBLIC_KEY_SERVER = "";
    public static String randomKey = "";
    public static PublicKey pKey;

    public static void main(String[] args) {
        changeLNF("Windows");
        try {
            socket = new Socket("localhost", 5000);
            socket.setReuseAddress(true);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            getKeyFromServer(); // lấy key từ server
            initKey(); // tạo pkey và randomkey
            new LoginScreen().showWindow();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Mất kết nối tới máy chủ");
        }

    }
    private static void getKeyFromServer() throws IOException, ClassNotFoundException {
        GlobalHelper.sendMessage(out, "cmd_sendMePublicKey");
        PUBLIC_KEY_SERVER = (String) in.readObject();
    }

    private static void initKey() throws NoSuchAlgorithmException {
        pKey = Hybrid_Encryption.getPublicKeyRSA(PUBLIC_KEY_SERVER);
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();
        randomKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }


    public static void changeLNF(String nameLNF) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (nameLNF.equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        }

    }
}
