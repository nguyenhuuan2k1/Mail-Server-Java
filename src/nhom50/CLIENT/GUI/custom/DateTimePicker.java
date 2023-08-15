package nhom50.CLIENT.GUI.custom;

import com.google.gson.Gson;
import nhom50.CLIENT.ClientMain;
import nhom50.CLIENT.GUI.screens.SendMailScreen;
import nhom50.CLIENT.helpers.Helper;
import nhom50.Encrypt.Hybrid_Encryption;
import nhom50.HELPERS.FileHandle;
import nhom50.HELPERS.GlobalHelper;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class DateTimePicker extends JFrame {
    private JPanel pickDate, pick;
    private JComboBox cbHours, cbMin;
    private JButton sendButton;
    private JLabel label;
    private String date;
    Map<String, Object> email = null;

    public DateTimePicker(Map<String, Object> email) {
        this.email = email;
        System.out.println(getClass() + " " + email);
        sendButton = new JButton();
        sendButton.setText("Send");
        sendButton.setFocusPainted(false);

        label = new JLabel();
        label.setText(":");

        setTitle("Schedule");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int HEIGHT = 80;
        final int WIDTH = 450;
        setBounds(((screenSize.width / 2) - (WIDTH / 2)),
                ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);


        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        pick = new JPanel();
        pick.add(datePicker);
        pickDate = new JPanel();
        pickDate.add(pick);

        cbHours = new JComboBox();
        for (int i = 0; i < 24; i++) {
            cbHours.addItem(i);
        }

        cbMin = new JComboBox();
        for (int i = 0; i < 60; i++) {
            cbMin.addItem(i);
        }

        pickDate.add(cbHours);
        pickDate.add(label);
        pickDate.add(cbMin);
        pickDate.add(sendButton);
        add(pickDate);
        sendButton.addActionListener(e -> {
            sendMail(e);
        });
        Helper.changeLNF("Windows");
        setResizable(false);
    }

    private void sendMail(ActionEvent e) {
        try {
            // tạo lịch hẹn giờ
            Calendar calendar = Calendar.getInstance();
            String temp[] = date.split("-");
            calendar.set(Calendar.DATE, Integer.parseInt(temp[0]));
            calendar.set(Calendar.MONTH, Integer.parseInt(temp[1]) - 1);
            calendar.set(Calendar.YEAR, Integer.parseInt(temp[2]));
            int hour = Integer.parseInt(String.valueOf(cbHours.getItemAt(cbHours.getSelectedIndex())));
            int minute = Integer.parseInt(String.valueOf(cbMin.getItemAt(cbMin.getSelectedIndex())));
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            // thêm lịch hẹn giờ vào hashmap ( dữ liệu này sẽ gửi lên server )
            email.put("schedule", calendar);
            String emailInfo = new Gson().toJson(email); // chuyển dữ liệu sang json
            System.out.println("Email info: " + emailInfo);
            // mã hoá json dữ liệu emails ở trên
            String data = Hybrid_Encryption.encryptAES(emailInfo, ClientMain.randomKey);
            // mã hoá client key bằng public key server
            String encryptRandomKey = Hybrid_Encryption.encryptRSA(ClientMain.randomKey, ClientMain.pKey);
            HashMap<String, String> sendServer = new HashMap<>();
            sendServer.put("cmd", "cmd_sendEmailSchedule");
            sendServer.put("key", encryptRandomKey);
            sendServer.put("value", data);
            String strSend = new Gson().toJson(sendServer);
            GlobalHelper.sendMessage(ClientMain.out, strSend);
            if (SendMailScreen.file__ != null) {
                // nếu có file thì gửui file cho server ~~
                new Thread(new SendFiletoServer()).start();
            }
            try {
                // đợi kết quả trả về từ server
                String message = (String) ClientMain.in.readObject();
                String input = Hybrid_Encryption.decryptAES(message, ClientMain.randomKey);
                JSONObject reponse = new JSONObject(input);
                JOptionPane.showMessageDialog(null, reponse.getString("message"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Mất kết nối tới máy chủ");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Đã có lỗi xảy ra");
        }
    }

    class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private String format = "dd-MM-yyyy";
        private SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormat.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                date = dateFormat.format(cal.getTime());
                return dateFormat.format(cal.getTime());
            }
            return "";
        }
    }

    class SendFiletoServer implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("đang gửi file lên server");
                // lấy url của file đã được chọn
                String fileUrl = (String) email.get("filepath");
                // chuyển file thành mảng byte
                byte[] datafile = FileHandle.convertFileToByte(fileUrl);
                // Tạo hashmapchứa dữ liệu của file
                HashMap<String, String> maps = new HashMap<>();
                maps.put("filename", (String) email.get("filename"));
                maps.put("data", new Gson().toJson(datafile));
                // chuyển hashmap thành json
                String data = new Gson().toJson(maps);
                // mã hoá json vừa tạo được ở trene
                data = Hybrid_Encryption.encryptAES(data, ClientMain.randomKey);
                // mã hoá key
                String encryptRandomKey = Hybrid_Encryption.encryptRSA(ClientMain.randomKey, ClientMain.pKey);
                // tạo hashmap gửi lên server
                HashMap<String, String> sendServer = new HashMap<>();
                sendServer.put("cmd", "cmd_AddFileToServer");
                sendServer.put("key", encryptRandomKey);
                sendServer.put("value", data);
                // chuyển hashmap thành json
                String strSend = new Gson().toJson(sendServer);
                // gửi lên server
                GlobalHelper.sendMessage(ClientMain.out, strSend);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}