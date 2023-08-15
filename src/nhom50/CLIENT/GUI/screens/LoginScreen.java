package nhom50.CLIENT.GUI.screens;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nhom50.CLIENT.ClientMain;
import nhom50.CLIENT.GUI.custom.ImagePanel;
import nhom50.CLIENT.utils.Constants;
import nhom50.CLIENT.utils.GlobalState;
import nhom50.DTO.User;
import nhom50.HELPERS.GlobalHelper;
import nhom50.Encrypt.Hybrid_Encryption;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends JFrame {
    private boolean isRemember = false;
    private JTextField txtEmail, txtPassword;
    private JLabel lblCheckBox, lblClose, lblLogin, lblSignup, lblForgot;

    public LoginScreen() {
        addControls();
        addEvents();
        readDataFromLocal();
    }

    private void addControls() {
        Container con = getContentPane();

        JPanel pnMain = new ImagePanel("images/background-login.png");
        pnMain.setLayout(null);

        lblClose = new JLabel(new ImageIcon("images/btn_close.png"));
        lblClose.setBounds(876, 20, 25, 25);
        lblClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        pnMain.add(lblClose);

        txtEmail = new JTextField();
        txtPassword = new JPasswordField();

        txtEmail.setBounds(80, 193, 291, 32);
        txtPassword.setBounds(80, 264, 291, 34);

        txtEmail.setFont(Constants.font.deriveFont(Constants.fontSize));

        txtEmail.setBorder(BorderFactory.createEmptyBorder());
        txtPassword.setBorder(BorderFactory.createEmptyBorder());

        pnMain.add(txtEmail);
        pnMain.add(txtPassword);

        lblCheckBox = new JLabel(new ImageIcon("images/check_remember.png"));
        lblCheckBox.setBounds(73, 310, 113, 23);
        lblCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblForgot = new JLabel();
        lblForgot.setBounds(276, 310, 100, 23);
        lblForgot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        pnMain.add(lblCheckBox);
        pnMain.add(lblForgot);

        lblLogin = new JLabel(new ImageIcon("images/btn_login"));
        lblLogin.setBounds(73, 355, 305, 36);
        lblLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pnMain.add(lblLogin);

        lblSignup = new JLabel();
        lblSignup.setBounds(153, 418, 144, 20);
        lblSignup.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pnMain.add(lblSignup);

        con.add(pnMain);
    }

    private void addEvents() {
        lblCheckBox.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isRemember = !isRemember;
                if (isRemember) {
                    lblCheckBox.setIcon(new ImageIcon("images/check_remember_checked.png"));
                } else {
                    lblCheckBox.setIcon(new ImageIcon("images/check_remember.png"));
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        lblForgot.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClickForgot();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        lblClose.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        lblLogin.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    onClickLogin();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lblLogin.setIcon(new ImageIcon("images/btn_login_hover.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblLogin.setIcon(new ImageIcon("images/btn_login.png"));
            }
        });
        lblSignup.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClickSignUp();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        txtPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickLogin();
            }
        });
    }


    private void onClickSignUp() {
        this.dispose();
        new SignupScreen().showWindow();
    }

    private void onClickForgot() {

    }

    private void onClickLogin() {
        try {
            String email = txtEmail.getText();
            String password = txtPassword.getText();

            if (!Constants.validate(email)) {
                JOptionPane.showMessageDialog(this,
                        "Địa chỉ email không đúng định dạng",
                        "Thông báo",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Map<String, String> infoUsers = new HashMap<String, String>();
            infoUsers.put("email", email);
            infoUsers.put("password", password);
            String user = new Gson().toJson(infoUsers); // chuyển sang json

            // mã hoá dữ liệu
            String data = Hybrid_Encryption.encryptAES(user, ClientMain.randomKey);

            String encryptRandomKey = Hybrid_Encryption.encryptRSA(ClientMain.randomKey, ClientMain.pKey);
            // lệnh login -> server
            HashMap<String, String> sendServer = new HashMap<>();
            sendServer.put("cmd", "cmd_login");
            sendServer.put("key", encryptRandomKey);
            sendServer.put("value", data);
            String strSend = new Gson().toJson(sendServer);
            // đóng gói thành json
            //System.out.println("Client sent encrypt String" + strSend);
            GlobalHelper.sendMessage(ClientMain.out, strSend);

            String message = (String) ClientMain.in.readObject();
            String input = Hybrid_Encryption.decryptAES(message, ClientMain.randomKey);

            // đợi nhận dữ liệu
            JSONObject reponse = new JSONObject(input);
            if (reponse.getBoolean("isSuccess")) {
                JOptionPane.showMessageDialog(null, reponse.getString("message"));
                JSONObject reponse_data = reponse.getJSONObject("data");
                // chuyển data user trong json thành hashmap
                HashMap<String, String> mapUser = new Gson().fromJson(reponse_data.toString(), new TypeToken<HashMap<String, String>>() {
                }.getType());
                // String id, String email, String firstName, String lastName, String password, boolean isOnline, Date createdAt, Date updatedAt
                GlobalState.currentUser = new User(mapUser.get("id"), mapUser.get("email"),
                        mapUser.get("firstName"), mapUser.get("lastName"),
                        mapUser.get("password"), Boolean.parseBoolean(mapUser.get("isOnline")), new Date(mapUser.get("createdAt")), new Date(mapUser.get("updatedAt")));
                closeWindow();

                // Login thành công lưu thông tin vào local để autoFill
                if (isRemember) {
                    writeDataToLocal();
                }
                new InboxScreen().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, reponse.getString("message"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Máy chủ đã tắt hoặc mất sóng");
        }
    }

    private void readDataFromLocal() {
        try {
            FileInputStream fis = new FileInputStream(".local");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            ArrayList<String> configs = new ArrayList<>();

            String line = br.readLine();
            while (line != null) {
                configs.add(line);
                line = br.readLine();
            }

            for (String str : configs) {
                String[] config = str.split("=");
                String key = config[0];
                String value = config.length == 2 ? config[1] : "";
                switch (key) {
                    case "USER_NAME":
                        txtEmail.setText(value);
                        break;
                    case "PASSWORD":
                        txtPassword.setText(value);
                        break;
                }
            }
        } catch (Exception ex) {
        }
    }

    private void writeDataToLocal() {
        try {
            String email = txtEmail.getText();
            String password = txtPassword.getText();
            FileWriter myWriter = new FileWriter(".local");
            myWriter.write("USER_NAME="+email);
            myWriter.write("\n");
            myWriter.write("PASSWORD="+password);
            myWriter.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeWindow() {
        this.setVisible(false); //you can't see me!
        this.dispose();
    }

    public void showWindow() {
        this.setTitle("Đăng nhập");
        this.setUndecorated(true);
        this.setSize(920, 550);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setBackground(new Color(0, 0, 0, 0));
    }
}

