package nhom50.SERVER.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nhom50.Encrypt.Hybrid_Encryption;
import nhom50.HELPERS.FileHandle;
import nhom50.HELPERS.GlobalHelper;
import org.json.JSONObject;

import java.io.File;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class FileServices {
    public static FileServices gI;

    public static FileServices getInstance() {
        if (gI == null) {
            gI = new FileServices();
        }
        return gI;
    }

    // hàm thêm file vào server
    public void addFile(String clientKey, String valueData, ObjectOutputStream out) {
        // valueData đã được giải mã ở HandleClient có
        // json dạng { filename: "",
        //              data: ""}
        try {
            org.json.JSONObject object = new JSONObject(valueData);
            String filename = object.getString("filename");
            String data = object.getString("data");
            // chuyển string thành mảng bytee
            byte[] dataFile = new Gson().fromJson(data, new TypeToken<byte[]>() {
            }.getType());
            // ghi fileeee
            FileHandle.WriteFile("./src/nhom50/SERVER/FileServer/" + filename, dataFile);
        } catch (Exception e) {
            System.out.println(getClass() + ": " + e.getMessage());
        }
    }

    public void downloadFile(String clientKey, String valueData, ObjectOutputStream out) {
        try {
            System.out.println(getClass() + ": " + valueData);
            File f = new File("./src/nhom50/SERVER/FileServer/" + valueData);
            if (f.exists()) {
                String filepath = f.getAbsolutePath();
                byte[] datafile = FileHandle.convertFileToByte(filepath);
                HashMap<String, String> maps = new HashMap<>();
                maps.put("filename", f.getName());
                maps.put("data", new Gson().toJson(datafile));
                String data = new Gson().toJson(maps);
                System.out.println(getClass() + "." + data);
                data = Hybrid_Encryption.encryptAES(data, clientKey);
                System.out.println("File data" + data);
                GlobalHelper.sendMessage(out, data);
                System.out.println("Đã gửi ngược lại client");
            }
            else{
                System.out.println("không có file");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
