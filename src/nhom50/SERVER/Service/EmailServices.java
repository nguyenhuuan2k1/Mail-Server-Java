package nhom50.SERVER.Service;

import nhom50.DTO.Email;
import nhom50.DTO.Response;
import nhom50.DTO.User;
import nhom50.Encrypt.Hybrid_Encryption;
import nhom50.HELPERS.GlobalHelper;
import nhom50.SERVER.BUS.EmailBUS;
import org.json.JSONObject;

import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.UUID;

public class EmailServices {
    public static EmailServices instance;


    public static EmailServices getInstance() {
        if (instance == null) {
            instance = new EmailServices();

        }
        return instance;
    }

    public Response sendMail(Email email) {
        return EmailBUS.getInstance().sendMail(email);
    }

    /*
    *  valuedata  = json của email sẽ có dạng
    * {
          "CC": "",
          "BCC": "",
          "filename": "",
          "size": ,
          "receiver_email": "",
          "subject": "",
          "sender_name": "",
          "content": "",
          "sender_email": "",
          "status": ""
        }
    * */
    public void sendMail(String clientKey, String valueData, ObjectOutputStream out) {
        // valuedata đọc ở trên
        org.json.JSONObject object = new JSONObject(valueData);
        String uuid = UUID.randomUUID().toString();
        String sender_name = object.getString("sender_name");
        String sender_email = object.getString("sender_email");
        String receiver_email = object.getString("receiver_email");
        String subject = object.getString("subject");
        String CC = object.getString("CC");
        String BCC = object.getString("BCC");
        String content = object.getString("content");
        String status = "SENT";
        Email email = null;
        email = new Email(uuid, sender_email, sender_name, receiver_email, subject, CC, BCC, content, status);
        // tuỳ vào email có gửi kèm file thì filename sẽ != rỗng hoặc != null
        if (object.has("filename")) {
            // nếu có file name thì tạo 1 instance email có theme filename & size
            String filename = object.getString("filename");
            Float size = object.getFloat("size");
            email = new Email(uuid, sender_email, sender_name, receiver_email, subject, CC, BCC, content, status, filename, size);

        }
        // gửi mail lên bus và đợi response trả về
        Response response = EmailBUS.getInstance().sendMail(email);
        // reponse trả về sẽ có dạng json. Ta mã hoá AES với clientKey để đưojc output
        String output = Hybrid_Encryption.encryptAES(response.toString(), clientKey);
        // gửi output về cho client
        GlobalHelper.sendMessage(out, output);
    }

    // gửi email theo lịch hẹn
    public void sendMailSchedule(String clientKey, String valueData, ObjectOutputStream out) {
        try {
            // value data kéo lên dòng 34 xem
            org.json.JSONObject object = new JSONObject(valueData);
            // lấy thông tin của email
            String uuid = UUID.randomUUID().toString();
            String sender_name = object.getString("sender_name");
            String sender_email = object.getString("sender_email");
            String receiver_email = object.getString("receiver_email");
            String subject = object.getString("subject");
            String CC = object.getString("CC");
            String BCC = object.getString("BCC");
            String content = object.getString("content");
            String status = "SENT";
            String time = "";
            Email email;
            String filename = "";
            float size = 0;
            // kiểm tra email có gửi kèm file hay không
            if (object.has("filename")) {
                // lấy file name & size của file
                filename = object.getString("filename");
                size = object.getFloat("size");
            }
            if (object.has("schedule")) {
                // kiểm tra email có hẹn lịch hông
                org.json.JSONObject schedule = object.getJSONObject("schedule");
                System.out.println("time: " + object.get("schedule"));
                // lấy ngày giờ phút của lịch ra để hẹn giờ
                Calendar cal = Calendar.getInstance();
                cal.clear();
                cal.set(Calendar.YEAR, schedule.getInt("year"));
                cal.set(Calendar.MONTH, schedule.getInt("month"));
                cal.set(Calendar.DATE, schedule.getInt("dayOfMonth"));
                cal.set(Calendar.HOUR, schedule.getInt("hourOfDay"));
                cal.set(Calendar.MINUTE, schedule.getInt("minute"));
                // tạo reponse gửi thành công về client
                Response response = new Response(true, "Gửi email thành công", null);
                // Giả mã dữ liệu với clientKey ( client là đối số truyền vào hàm )
                String output = Hybrid_Encryption.encryptAES(response.toString(), clientKey);
                // gủi dữ liệu về client
                GlobalHelper.sendMessage(out, output);
                // tạo instance email gồm các thuộc tính lấy được ở trên
                email = new Email(uuid, sender_email, sender_name, receiver_email, subject, CC, BCC, content, status, filename, size);
                // tạo hẹn giờ
                Date timeSend = cal.getTime();
                // khởi tạo ScheduleServices với email cần gửi
                ScheduleServices s = new ScheduleServices(email);
                // hẹn giờ thông qua Timer
                // timer giống thread là xử lý bất đồng bộ,
                Timer timer = new Timer();
                // hẹn giờ cho timer
                timer.schedule(s, timeSend);
                // hẹn giờ thành công
                System.out.println("timer thành công");
            } else {
                // trường hợp ngược lại gửi mail như bình thường
                email = new Email(uuid, sender_email, sender_name, receiver_email, subject, CC, BCC, content, status, filename, size);
                Response response = EmailBUS.getInstance().sendMail(email);
                String output = Hybrid_Encryption.encryptAES(response.toString(), clientKey);
                GlobalHelper.sendMessage(out, output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStatusMail(String clientKey, String valueData, ObjectOutputStream out, String status) {
        // chuyển qua lại giữa các status của email
        // READ , SENT, INBOX, DELETED
        String uuid = valueData;
        Response response = EmailBUS.getInstance().changeStatusEmail(uuid, status);
        String output = Hybrid_Encryption.encryptAES(response.toString(), clientKey);
        GlobalHelper.sendMessage(out, output);
    }

    // hàm get tin nhắn mới nhận vào client key , user mà thread đang phục vụ ,
    // valuedata ở hàm này không dùng , truyền thừa ~~
    public void getInboxMail(String clientKey, String valueData, ObjectOutputStream out, User user) {
        Response response = EmailBUS.getInstance().getInboxMail(user);
        String output = Hybrid_Encryption.encryptAES(response.toString(), clientKey);
        GlobalHelper.sendMessage(out, output);
    }


    public void getAllMail(String clientKey, String valueData, ObjectOutputStream out) {

    }

    // hàm get tin nhắn đã gửi dựa vào client key , user mà thread đang phục vụ ,
    // valuedata ở hàm này không dùng , truyền thừa ~~
    public void getSentMail(String clientKey, String valueData, ObjectOutputStream out, User currentUser) {
        Response response = EmailBUS.getInstance().getSentMail(currentUser);
        // mã hoá dữ liệu
        String output = Hybrid_Encryption.encryptAES(response.toString(), clientKey);
        // gửi dữ liệu đã mã hoá về client
        GlobalHelper.sendMessage(out, output);
    }

    // hàm này tương tự hàm các hàm trên
    public void getReadMail(String clientKey, String valueData, ObjectOutputStream out, User currentUser) {
        Response response = EmailBUS.getInstance().getReadMail(currentUser);
        // mã hoá dữ liệu
        String output = Hybrid_Encryption.encryptAES(response.toString(), clientKey);
        GlobalHelper.sendMessage(out, output);
    }

    // hàm này tương tự hàm các hàm trên
    public void getSpamMail(String clientKey, String valueData, ObjectOutputStream out, User currentUser) {
        Response response = EmailBUS.getInstance().getSpamMail(currentUser);
        // mã hoá dữ liệu
        String output = Hybrid_Encryption.encryptAES(response.toString(), clientKey);
        GlobalHelper.sendMessage(out, output);
    }

    // hàm này tương tự hàm các hàm trên
    public void getDeleteMail(String clientKey, String valueData, ObjectOutputStream out, User currentUser) {
        Response response = EmailBUS.getInstance().getDeleteMail(currentUser);
        // mã hoá dữ liệu
        String output = Hybrid_Encryption.encryptAES(response.toString(), clientKey);
        GlobalHelper.sendMessage(out, output);
    }
}
