package nhom50.SERVER;

import nhom50.Encrypt.Hybrid_Encryption;
import nhom50.SERVER.IOFile.FileFunc;
import nhom50.SERVER.MyHandle.HandleClient;
import nhom50.SERVER.MyHandle.HandleServer;
import nhom50.SERVER.Service.UserServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerMain {

    public static int NumThreads = 2;
    public static ArrayList<HandleClient> listUser = new ArrayList<HandleClient>();
    public static PrivateKey privateKey = null;
    public static PublicKey publicKey = null;
    public static String PUBLIC_KEY_STRING = "";
    public static String PRIVATE_KEY_STRING = "";
    public static ServerSocket server = null;
    public static ObjectInputStream in = null;
    public static ObjectOutputStream out = null;


    public static void main(String[] args) {
        // đọc keyServer
        readKey();
        try {
            // services chạy ngầm trước khi tắt server
            Runtime.getRuntime().addShutdownHook(new Thread(() -> UserServices.gI().setOfflineforUserWhenServerClosed()));
            // khởi tạo server socket
            ServerSocket server = new ServerSocket(5000);
            System.out.println("Server is running");
            int i = 1;
            new HandleServer().start();
            while (true) {
                // khởi tạo socket
                System.out.println("Start socket");
                Socket socket = server.accept();
                System.out.println("Socket accepted");
                HandleClient client = new HandleClient(socket, "Client" + i);
                listUser.add(client);
                System.out.println("added user  :" + "client " + i);
                client.start();
                System.out.println("User client" + client.getClientName() + " started");
                i++;
            }
        } catch (IOException e) {
            System.out.println("Đã có lỗi xảy ra khi start server");
        }
    }

    private static void readKey() {
        HashMap<String, String> keyServer = FileFunc.getServerKey("key.txt");
        PUBLIC_KEY_STRING = keyServer.get("publickey");
        PRIVATE_KEY_STRING = keyServer.get("privatekey");
        privateKey = Hybrid_Encryption.getPrivateKeyRSA(keyServer.get("privatekey"));
        publicKey = Hybrid_Encryption.getPublicKeyRSA(keyServer.get("publickey"));
    }
}
