package nhom50.CLIENT.GUI.screens;

import com.google.gson.Gson;
import nhom50.CLIENT.ClientMain;
import nhom50.CLIENT.GUI.custom.ImagePanel;
import nhom50.CLIENT.utils.Constants;
import nhom50.HELPERS.GlobalHelper;
import nhom50.Encrypt.Hybrid_Encryption;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;
import java.util.HashMap;

public class SignupScreen extends JFrame {
    private JTextField txtFirstName, txtLastName, txtEmail, txtPassword, txtConfirmPassword;
    private JLabel lblClose, lblLogin, lblSignup;

    public SignupScreen() {
        addControls();
        addEvents();
    }

    public SignupScreen(Socket socket, PublicKey publicKey, String randomKey) {
        try {

            System.out.println("Random key in " + getClass() + " is " + randomKey);
            addControls();
            addEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addControls() {
        Container con = getContentPane();

        JPanel pnMain = new ImagePanel("images/background-signup.png");
        pnMain.setLayout(null);

        lblClose = new JLabel(new ImageIcon("images/btn_close_blue.png"));
        lblClose.setBounds(876, 20, 25, 25);
        lblClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        pnMain.add(lblClose);

        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtEmail = new JTextField();
        txtPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();

        txtFirstName.setBounds(544, 122, 126, 32);
        txtLastName.setBounds(709, 122, 126, 32);
        txtEmail.setBounds(544, 197, 291, 32);
        txtPassword.setBounds(544, 269, 291, 34);
        txtConfirmPassword.setBounds(544, 342, 291, 34);

        txtFirstName.setFont(Constants.font.deriveFont(Constants.fontSize));
        txtLastName.setFont(Constants.font.deriveFont(Constants.fontSize));
        txtEmail.setFont(Constants.font.deriveFont(Constants.fontSize));

        txtFirstName.setBorder(BorderFactory.createEmptyBorder());
        txtLastName.setBorder(BorderFactory.createEmptyBorder());
        txtEmail.setBorder(BorderFactory.createEmptyBorder());
        txtPassword.setBorder(BorderFactory.createEmptyBorder());
        txtConfirmPassword.setBorder(BorderFactory.createEmptyBorder());

        pnMain.add(txtFirstName);
        pnMain.add(txtLastName);
        pnMain.add(txtEmail);
        pnMain.add(txtPassword);
        pnMain.add(txtConfirmPassword);

        lblSignup = new JLabel(new ImageIcon("images/btn_signup"));
        lblSignup.setBounds(537, 399, 305, 36);
        lblSignup.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pnMain.add(lblSignup);

        lblLogin = new JLabel();
        lblLogin.setBounds(617, 462, 144, 20);
        lblLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pnMain.add(lblLogin);

        con.add(pnMain);
    }

    private void addEvents() {
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
                onClickLogin();
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
                lblSignup.setIcon(new ImageIcon("images/btn_signup_hover.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblSignup.setIcon(new ImageIcon("images/btn_signup.png"));
            }
        });
    }

    private void onClickSignUp() {
        try {
            String email = txtEmail.getText();
            String firstName = txtFirstName.getText();
            String lastName = txtLastName.getText();
            String password = txtPassword.getText();
            String confirmPassword = txtConfirmPassword.getText();


            if (!Constants.validate(email)) {
                JOptionPane.showMessageDialog(this,
                        "Địa chỉ email không đúng định dạng",
                        "Thông báo",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            HashMap<String, String> infoUsers = new HashMap<>();
            infoUsers.put("firstName", firstName);
            infoUsers.put("lastName", lastName);
            infoUsers.put("email", email);
            infoUsers.put("password", password);
            infoUsers.put("confirmPassword", confirmPassword);

            String users = new Gson().toJson(infoUsers);
            System.out.println(users);
            // mã hoá dữ liệu
            String data = Hybrid_Encryption.encryptAES(users, ClientMain.randomKey);
            String encryptRandomKey = Hybrid_Encryption.encryptRSA(ClientMain.randomKey, ClientMain.pKey);
            // lệnh login -> server
            HashMap<String, String> sendServer = new HashMap<>();
            sendServer.put("cmd", "cmd_register");
            sendServer.put("key", encryptRandomKey);
            sendServer.put("value", data);
            String strSend = new Gson().toJson(sendServer);
            // đóng gói thành json
            //System.out.println("Client sent encrypt String" + strSend);
            GlobalHelper.sendMessage(ClientMain.out, strSend);
            String message = (String) ClientMain.in.readObject();
            String input = Hybrid_Encryption.decryptAES(message, ClientMain.randomKey);
            JSONObject reponse = new JSONObject(input);
            if (reponse.getBoolean("isSuccess")) {
                JOptionPane.showMessageDialog(null, reponse.getString("message"));
                closeWindows();
                new LoginScreen().showWindow();
            } else {
                JOptionPane.showMessageDialog(null, reponse.getString("message"));
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Máy chủ tắt hoặc mất sóng");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeWindows() {
        this.dispose();
        this.setVisible(false);
    }

    private void onClickLogin() {
        this.dispose();
        new LoginScreen().showWindow();
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
