package nhom50.SERVER.MyHandle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nhom50.DTO.User;
import nhom50.Encrypt.Hybrid_Encryption;
import nhom50.HELPERS.GlobalHelper;
import nhom50.SERVER.BUS.UserBUS;
import nhom50.SERVER.IOFile.FileFunc;
import nhom50.SERVER.ServerMain;
import nhom50.SERVER.Service.EmailServices;
import nhom50.SERVER.Service.FileServices;
import nhom50.SERVER.Service.UserServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;

public class HandleClient extends Thread {
    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String ClientName = "";
    private User currentUser = null;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public HandleClient(Socket socket, String clientName) throws IOException {
        this.socket = socket;
        this.ClientName = clientName;
        this.publicKey = ServerMain.publicKey;
        this.privateKey = ServerMain.privateKey;
    }

    public String getClientName() {
        return ClientName;
    }


    @Override
    public void run() {
        String input = null;
        try {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());

            while (true) {
                input = (String) in.readObject();
                System.out.println(input);
                // khi client khởi động, client sẽ gửi cái lệnh này lên server
                // server sẽ gửi publicKey Server về cho client
                if (input.equals("cmd_sendMePublicKey")) {
                    GlobalHelper.sendMessage(out, ServerMain.PUBLIC_KEY_STRING);
                } else {
                    // chuyển json mà client gửi lên thành HashMap
                    HashMap<String, Object> readClient = new Gson().fromJson(input, new TypeToken<HashMap<String, Object>>() {
                    }.getType());
                    if (readClient.containsKey("cmd")) {
                        String encryptKey = (String) readClient.get("key");
                        String valueData = (String) readClient.get("value");
                        // giải mã key của client
                        String clientKey = Hybrid_Encryption.decryptRSA(encryptKey, privateKey);
                        //  giải mã data mà client gửi lên
                        valueData = Hybrid_Encryption.decryptAES(valueData, clientKey);
                        String cmd = (String) readClient.get("cmd");
                        // thực hiện command
                        doCommand(clientKey, cmd, valueData);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(getClass() + ": " + e.getMessage());
            // khi mà client thoát, thì sẽ có exception connection reset
            // bắt trường hợp này ta sẽ xoá client khỏi list user
            for (HandleClient user : ServerMain.listUser) {
                if (user.getClientName().equals(this.ClientName)) {
                    ServerMain.listUser.remove(user);
                    break;
                }
            }
            // client THOÁT có 2 trường hợp
            // đã đăng nhập thì sẽ ghi logout, ghi log login ở  HandleClient.doLogin();
            if (this.currentUser != null) {
                UserBUS.getInstance().changeStatusOnline(currentUser, 0);
                System.out.println(this.currentUser.getEmail() + " is logout");
                HandleServer.log_server.add(this.currentUser.getEmail() + " is logout" + new Date(System.currentTimeMillis()));
                FileFunc.ghiFile(HandleServer.log_server, "log.txt");
            } else {
                // TH2 chưa đăng nhập thì không ghi log
                System.out.println("Anonymous user is close windows");
            }
            // đóng socket
            doCloseSocket();
        }

    }

    // đóng socket
    public void doCloseSocket() {
        try {
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doCommand(String clientKey, String cmd, String valueData) {
        // valuedata đã được giải mã ở line 69
        switch (cmd) {
            case "cmd_login":
                if (UserServices.gI().doLogin(clientKey, valueData, out)) {
                    // trường hợp đăng nhập thành công thì gán user này vào cho Thread để thread làm các công việc khác
                    this.currentUser = UserServices.gI().getCurrentUser(valueData);
                }
                break;
            // đăng ký
            case "cmd_register":
                UserServices.gI().doRegister(clientKey, valueData, out);
                break;
            // không dùng, quên xoá, báo cáo xong rồi xoá
            case "cmd_getAllMail":
                EmailServices.getInstance().getAllMail(clientKey, valueData, out);
                break;
            // get inbox mail theo user đang đăng nhập trong Thread
            case "cmd_getInboxMail":
                EmailServices.getInstance().getInboxMail(clientKey, valueData, out, currentUser);
                break;
            // get sent mail theo user đang đăng nhập trong Thread
            case "cmd_getSentMail":
                EmailServices.getInstance().getSentMail(clientKey, valueData, out, currentUser);
                break;
            // get read mail theo user đang đăng nhập trong Thread
            case "cmd_getReadMail":
                EmailServices.getInstance().getReadMail(clientKey, valueData, out, currentUser);
                break;
            // get spam mail theo user đang đăng nhập trong Thread
            case "cmd_getSpamMail":
                EmailServices.getInstance().getSpamMail(clientKey, valueData, out, currentUser);
                break;
            // get delete mail theo user đang đăng nhập trong Thread
            case "cmd_getDeleteMail":
                EmailServices.getInstance().getDeleteMail(clientKey, valueData, out, currentUser);
                break;
            // gửi mail theo data mà client gửi lene
            case "cmd_sendEmail":
                EmailServices.getInstance().sendMail(clientKey, valueData, out);
                break;
            // gửi email theo lịch+++++++++ 
            case "cmd_sendEmailSchedule":
                EmailServices.getInstance().sendMailSchedule(clientKey, valueData, out);
                break;
            // đánh dấu đã đọc
            case "cmd_read":
                EmailServices.getInstance().setStatusMail(clientKey, valueData, out, "READ");
                break;
            // đánh dấu spam
            case "cmd_spam":
                EmailServices.getInstance().setStatusMail(clientKey, valueData, out, "SPAM");
                break;
            // đánh dấu delete
            case "cmd_delete":
                EmailServices.getInstance().setStatusMail(clientKey, valueData, out, "DELETED");
                break;
            // thêm file váo server
            case "cmd_AddFileToServer":
                FileServices.getInstance().addFile(clientKey, valueData, out);
                break;
            // tải file về clinet
            case "cmd_downFiletoClient":
                FileServices.getInstance().downloadFile(clientKey, valueData, out);
                break;
            default:
                break;
        }
    }


}