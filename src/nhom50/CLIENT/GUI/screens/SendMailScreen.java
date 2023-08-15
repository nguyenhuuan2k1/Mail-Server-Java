package nhom50.CLIENT.GUI.screens;


import com.google.gson.Gson;
import nhom50.CLIENT.ClientMain;
import nhom50.CLIENT.GUI.custom.DateTimePicker;
import nhom50.CLIENT.helpers.Helper;
import nhom50.CLIENT.utils.Constants;
import nhom50.CLIENT.utils.GlobalState;
import nhom50.Encrypt.Hybrid_Encryption;
import nhom50.HELPERS.FileHandle;
import nhom50.HELPERS.GlobalHelper;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.StyledEditorKit.BoldAction;
import javax.swing.text.StyledEditorKit.ItalicAction;
import javax.swing.text.StyledEditorKit.UnderlineAction;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLWriter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class SendMailScreen {
    private static final String DEFAULT_FONT_FAMILY = "SansSerif";
    private static final int DEFAULT_FONT_SIZE = 18;
    private static String MAIN_TITLE = null;
    private static JFrame frame;
    private static JTextPane textEdit;
    private static JTextField txtReceiver;
    private static JTextField txtCc;
    private static JTextField txtBcc;
    private static JTextField txtTitle;
    private static JLabel lblFileName;
    public static Date timeSend = null;
    public static File file__ = null;
    public static String new_file_name = null;

    public static JTextField getTxtReceiver() {
        return txtReceiver;
    }

    public static JTextField getTxtCc() {
        return txtCc;
    }

    public static JTextField getTxtBcc() {
        return txtBcc;
    }

    public SendMailScreen() throws IOException {
        // giao diện
        MAIN_TITLE = GlobalState.currentUser.getEmail();
        frame = new JFrame(MAIN_TITLE);
        textEdit = new JTextPane();
        JScrollPane editorScrollPane = new JScrollPane(textEdit);
        textEdit.setDocument(new DefaultStyledDocument());
        textEdit.setContentType("text/html;charset=UTF-8");

        Helper.changeLNF("Windows");
        EditButtonActionListener editButtonActionListener = new EditButtonActionListener();

        JLabel receiver = new JLabel("To: ");
        receiver.setFont(Constants.font.deriveFont(Constants.fontSize));
        receiver.setPreferredSize(new Dimension(40, 15));

        JLabel cc = new JLabel("Cc: ");
        cc.setFont(Constants.font.deriveFont(Constants.fontSize));
        cc.setPreferredSize(new Dimension(30, 15));

        JLabel bcc = new JLabel("Bcc: ");
        bcc.setFont(Constants.font.deriveFont(Constants.fontSize));
        bcc.setPreferredSize(new Dimension(30, 15));

        JLabel lbTitle = new JLabel("Title: ");
        lbTitle.setFont(Constants.font.deriveFont(Constants.fontSize));
        lbTitle.setPreferredSize(new Dimension(45, 15));

        txtReceiver = new JTextField(20);
        txtCc = new JTextField(20);
        txtBcc = new JTextField(20);
        txtTitle = new JTextField(74);


        JButton btnBold = new JButton(new BoldAction());
        btnBold.setIcon(new ImageIcon("images/bold_16px.png"));
        btnBold.setFont(Constants.font.deriveFont(Constants.fontSize));
        btnBold.setHideActionText(true);
        btnBold.setText("Bold");
        btnBold.setFocusPainted(false);
        btnBold.addActionListener(editButtonActionListener);

        JButton btnItalic = new JButton(new ItalicAction());
        btnItalic.setIcon(new ImageIcon("images/italic_16px.png"));
        btnItalic.setFont(Constants.font.deriveFont(Constants.fontSize));
        btnItalic.setHideActionText(true);
        btnItalic.setText("Italic");
        btnItalic.setFocusPainted(false);
        btnItalic.addActionListener(editButtonActionListener);

        JButton btnUnderline = new JButton(new UnderlineAction());
        btnUnderline.setIcon(new ImageIcon("images/underline_16px.png"));
        btnUnderline.setFont(Constants.font.deriveFont(Constants.fontSize));
        btnUnderline.setHideActionText(true);
        btnUnderline.setText("Underline");
        btnUnderline.setFocusPainted(false);
        btnUnderline.addActionListener(editButtonActionListener);

        JButton btnColor = new JButton("Color");
        btnColor.setIcon(new ImageIcon("images/color_16px.png"));
        btnColor.setFont(Constants.font.deriveFont(Constants.fontSize));
        btnColor.setFocusPainted(false);
        btnColor.addActionListener(new ColorActionListener());

        JButton btnFile = new JButton("File");
        btnFile.setIcon(new ImageIcon("images/Attach_16px.png"));
        btnFile.setFont(Constants.font.deriveFont(Constants.fontSize));
        btnFile.setFocusPainted(false);
        btnFile.addActionListener(e -> btnFileMouseClicked(e));

        JButton btnSend = new JButton("Send");
        btnSend.setIcon(new ImageIcon("images/imac_mail_16px.png"));
        btnSend.setFont(Constants.font.deriveFont(Constants.fontSize));
        btnSend.setFocusPainted(false);
        btnSend.addActionListener(e -> {
            try {
                btnSendMouseClicked(e);
            } catch (Exception exception) {

            }
        });

        JButton btnSchedule = new JButton("Schedule");
        btnSchedule.setIcon(new ImageIcon("images/schedule_16px.png"));
        btnSchedule.setFont(Constants.font.deriveFont(Constants.fontSize));
        btnSchedule.setFocusPainted(false);
        btnSchedule.addActionListener(e -> btnScheduleMouseClicked(e));

        JLabel title = new JLabel("Compose");
        title.setFont(Constants.font.deriveFont(Constants.fontSizeTitle));
        title.setForeground(new Color(0, 90, 233, 255));
        title.setHorizontalAlignment(JLabel.CENTER);

        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel2.setBackground(Color.WHITE);
        panel2.add(receiver);
        panel2.add(txtReceiver);

        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel3.setBackground(Color.WHITE);
        panel3.add(cc);
        panel3.add(txtCc);

        JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel4.setBackground(Color.WHITE);
        panel4.add(bcc);
        panel4.add(txtBcc);

        JPanel panel6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel6.setBackground(Color.WHITE);
        panel6.add(lbTitle);
        panel6.add(txtTitle);

        JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel5.setBackground(Color.WHITE);
        panel5.add(panel2);
        panel5.add(panel3);
        panel5.add(panel4);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        panel.add(btnSend);
        panel.add(btnSchedule);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(btnBold);
        panel.add(btnItalic);
        panel.add(btnUnderline);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(btnColor);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(btnFile);

        JPanel toolBarPanel = new JPanel();
        toolBarPanel.setBackground(Color.WHITE);
        toolBarPanel.setLayout(new GridLayout(4, 1));
        toolBarPanel.add(title);
        toolBarPanel.add(panel5);
        toolBarPanel.add(panel6);
        toolBarPanel.add(panel);
        JPanel panel_1 = new JPanel();
        panel_1.setPreferredSize(new Dimension(50, 50));
        frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
        panel_1.setLayout(new BorderLayout(0, 0));

        lblFileName = new JLabel("Tệp đính kèm : Không");
        lblFileName.setIcon(new ImageIcon("images/Attach_16px.png"));
        lblFileName.setFont(new Font("Tahoma", Font.BOLD, 13));
        panel_1.add(lblFileName, BorderLayout.EAST);
        frame.getContentPane().add(toolBarPanel, BorderLayout.NORTH);
        frame.getContentPane().add(editorScrollPane, BorderLayout.CENTER);


        frame.setIconImage(new ImageIcon("images/logo.png").getImage());
        frame.setSize(900, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        textEdit.requestFocusInWindow();

    }

    private String writeSelection() {
        StringWriter buf = new StringWriter();
        HTMLWriter writer = new HTMLWriter(buf,
                (HTMLDocument) textEdit.getDocument(), 0, textEdit.getDocument().getLength());
        try {
            writer.write();
        } catch (IOException | BadLocationException ex) {
            ex.printStackTrace();
        }
        return buf.toString();
    }

    private void btnSendMouseClicked(ActionEvent e) {
        try {
            // Tạo hashMap lưu dữ liệu email
            Map<String, Object> email = new HashMap<String, Object>();
            email.put("sender_name", GlobalState.currentUser.getFirstName() + " " + GlobalState.currentUser.getLastName());
            email.put("sender_email", GlobalState.currentUser.getEmail());
            email.put("receiver_email", txtReceiver.getText());
            email.put("subject", txtTitle.getText());
            email.put("CC", txtCc.getText());
            email.put("content", writeSelection());
            email.put("BCC", txtBcc.getText());
            email.put("status", "SENT");
            email.put("size", 0f);
            if (file__ != null) {
                // nếu có chọn file thì kèm thêm filename và size\
                String filename = file__.getName();
                new_file_name = renameFile(filename);
                email.put("filename", new_file_name);
                email.put("size", file__.length() / (1024f * 1024f));// // đổi từ byte sang MB
            }
            String emailInfo = new Gson().toJson(email); // Hashmap chuyển sang json
            System.out.println("Email info :" + emailInfo);
            // mã hoá dữ liệu
            String data = Hybrid_Encryption.encryptAES(emailInfo, ClientMain.randomKey);
            String encryptRandomKey = Hybrid_Encryption.encryptRSA(ClientMain.randomKey, ClientMain.pKey);
            // Tạo hashMap để gửi dữ liệu cho server
            HashMap<String, String> sendServer = new HashMap<>();
            sendServer.put("cmd", "cmd_sendEmail");
            sendServer.put("key", encryptRandomKey);
            sendServer.put("value", data);
            String strSend = new Gson().toJson(sendServer);
            // đóng gói thành json
            System.out.println("Client sent encrypt String" + strSend);
            GlobalHelper.sendMessage(ClientMain.out, strSend);
            if (file__ != null) {
                // có file thì tạo thread gửi file
                SendFiletoServer send = new SendFiletoServer();
                new Thread(send).start();
            }
            try {
                String message = (String) ClientMain.in.readObject();
                String input = Hybrid_Encryption.decryptAES(message, ClientMain.randomKey);
                JSONObject reponse = new JSONObject(input);
                JOptionPane.showMessageDialog(null, reponse.getString("message"));
                if (reponse.getBoolean("isSuccess")) {
                    this.frame.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Mất kết nối tới máy chủ");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Mất kết nối tới máy chủ");
        }
    }

    public void btnFileMouseClicked(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            System.out.println(chooser.getSelectedFile().getAbsolutePath());
            file__ = chooser.getSelectedFile();
            lblFileName.setText(String.format(file__.getAbsolutePath() + " / " + String.format("%.2f", file__.length() / (1024f * 1024f)) + " MB"));
        }
    }


    public void btnScheduleMouseClicked(ActionEvent e) {
        // Tạo hashMap lưu dữ liệu email
        Map<String, Object> email = new HashMap<String, Object>();
        email.put("sender_name", GlobalState.currentUser.getFirstName() + " " + GlobalState.currentUser.getLastName());
        email.put("sender_email", GlobalState.currentUser.getEmail());
        email.put("receiver_email", txtReceiver.getText());
        email.put("subject", txtTitle.getText());
        email.put("CC", txtCc.getText());
        email.put("content", writeSelection());
        email.put("BCC", txtBcc.getText());
        email.put("status", "SENT");
        email.put("size", 0f);
        if (file__ != null) {
            String filename = file__.getName();
            new_file_name = renameFile(filename);
            email.put("filename", new_file_name);
            email.put("filepath", file__.getAbsolutePath());
            email.put("size", file__.length() / (1024f * 1024f));// đổi từ byte sang MB
            System.out.println("file size" + file__.length() / (1024 * 1024));
        }
        // gửi hashmap qua màn hình datetimepicker
        new DateTimePicker(email).setVisible(true);
    }

    private StyledDocument getEditorDocument() {
        StyledDocument doc = (DefaultStyledDocument) textEdit.getDocument();
        return doc;
    }

    // rename file tránh lỗi trùng file
    public String renameFile(String oldName) {
        return System.currentTimeMillis() + "_" + oldName;
    }

    private class EditButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            textEdit.requestFocusInWindow();
        }
    }

    private class ColorActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(frame, "Choose a color", Color.BLACK);
            if (newColor == null) {
                textEdit.requestFocusInWindow();
                return;
            }
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setForeground(attr, newColor);
            textEdit.setCharacterAttributes(attr, false);
            textEdit.requestFocusInWindow();
        }
    }

    class SendFiletoServer implements Runnable {

        @Override
        public void run() {
            try {
                String filename = file__.getName();
                System.out.println("đang gửi file lên server");
                String fileUrl = file__.getAbsolutePath();
                byte[] datafile = FileHandle.convertFileToByte(fileUrl);
                HashMap<String, String> maps = new HashMap<>();
                maps.put("filename", new_file_name);
                maps.put("data", new Gson().toJson(datafile));
                String data = new Gson().toJson(maps);
                data = Hybrid_Encryption.encryptAES(data, ClientMain.randomKey);
                String encryptRandomKey = Hybrid_Encryption.encryptRSA(ClientMain.randomKey, ClientMain.pKey);
                HashMap<String, String> sendServer = new HashMap<>();
                sendServer.put("cmd", "cmd_AddFileToServer");
                sendServer.put("key", encryptRandomKey);
                sendServer.put("value", data);
                String strSend = new Gson().toJson(sendServer);
                GlobalHelper.sendMessage(ClientMain.out, strSend);
                new_file_name = null;
                file__ = null;
            } catch (Exception e) {

            }
        }
    }
}
