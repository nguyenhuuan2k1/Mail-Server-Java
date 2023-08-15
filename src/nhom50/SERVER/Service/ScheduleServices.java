package nhom50.SERVER.Service;

import nhom50.DTO.Email;
import nhom50.DTO.Response;
import nhom50.SERVER.BUS.EmailBUS;

import java.util.TimerTask;

public class ScheduleServices extends TimerTask {
    public static ScheduleServices gI;

    public static ScheduleServices getInstance() {
        if (gI == null) {
            gI = new ScheduleServices();
        }
        return gI;
    }

    public ScheduleServices() {
    }

    public Email currentEmail;

    public ScheduleServices(Email email) {
        this.currentEmail = email;
    }

    @Override
    public void run() {
        Response response = EmailBUS.getInstance().sendMail(currentEmail);
        System.out.println(response.toString());
        System.out.println("Đã gửi mail theo lịch thành công");
    }
}
