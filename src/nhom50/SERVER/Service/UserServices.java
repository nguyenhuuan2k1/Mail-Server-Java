package nhom50.SERVER.Service;

import nhom50.DTO.Response;
import nhom50.DTO.User;
import nhom50.Encrypt.Hybrid_Encryption;
import nhom50.HELPERS.GlobalHelper;
import nhom50.SERVER.BUS.UserBUS;
import nhom50.SERVER.IOFile.FileFunc;
import nhom50.SERVER.MyHandle.HandleServer;
import org.json.JSONObject;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class UserServices {
    public static UserServices gI;

    public static UserServices gI() {
        if (gI == null) {
            gI = new UserServices();
        }
        return gI;
    }

    public boolean doLogin(String clientKey, String valueData, ObjectOutputStream out) {
        // dữ liệu đầu vào sẽ là json gồm email & password
        org.json.JSONObject object = new JSONObject(valueData);
        String email = object.getString("email");
        String password = object.getString("password");
        // gọi hàm login ở BUS
        Response response = UserBUS.getInstance().login(email, password);
        String output = Hybrid_Encryption.encryptAES(response.toString(), clientKey);
        GlobalHelper.sendMessage(out, output);
        if (response.isSuccess()) {
            HandleServer.log_server.add(email + " login success at " + new Date(System.currentTimeMillis()));
            FileFunc.ghiFile(HandleServer.log_server, "log.txt");
        }
        return response.isSuccess() ? true : false;
    }

    // lấy user hiện tại đang login vào, sử dụng cho hàm doCommand ở HandleCLient
    public User getCurrentUser(String valueData) {
        try {
            org.json.JSONObject object = new JSONObject(valueData);
            String email = object.getString("email");
            return UserBUS.getInstance().findUserByEmail(email);
        } catch (Exception e) {
            return null;
        }
    }

    public void doRegister(String clientKey, String valueData, ObjectOutputStream out) {
        try {
            org.json.JSONObject object = new JSONObject(valueData);
            String email = object.getString("email");
            String password = object.getString("password");
            String lastName = object.getString("lastName");
            String firstName = object.getString("firstName");
            String confirmPassword = object.getString("confirmPassword");
            Response response = UserBUS.getInstance().createUser(email, firstName, lastName, password, confirmPassword);
            String output = Hybrid_Encryption.encryptAES(response.toString(), clientKey);
            GlobalHelper.sendMessage(out, output);
            System.out.println("output in server" + output);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Response response = new Response(false, "Đã có lỗi xảy ra vui lòng khởi động lại ứng dụng", null);
            String output = Hybrid_Encryption.encryptAES(response.toString(), clientKey);
            GlobalHelper.sendMessage(out, output);
        }
    }

    public ArrayList<User> getAllUsers() {
        return UserBUS.getInstance().getAllUsers();
    }

    public Response changeStatusUser(String email, String status) {
        return UserBUS.getInstance().changeStatusUser(email, status);
    }

    public Response editStorage(float storage) {
        return UserBUS.getInstance().editStorage(storage);
    }

    public Response editStorageUser(String email, float storageUser) {
        return UserBUS.getInstance().editStorageUser(email, storageUser);
    }

    public void setOfflineforUserWhenServerClosed() {
        System.out.println("Đang đóng server");
        UserBUS.getInstance().setOffline();
    }
}