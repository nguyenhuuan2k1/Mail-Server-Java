package nhom50.CLIENT.GUI.screens;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nhom50.CLIENT.ClientMain;
import nhom50.CLIENT.helpers.Helper;
import nhom50.CLIENT.utils.Constants;
import nhom50.CLIENT.utils.GlobalState;
import nhom50.DTO.Email;
import nhom50.Encrypt.Hybrid_Encryption;
import nhom50.HELPERS.FileHandle;
import nhom50.HELPERS.GlobalHelper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;


public class Read2 extends JFrame {
    private JButton btnDelete, btnSpam, btnDownload, btnRep, btnRepAll, btnForward;
    private JPanel pnlMain;
    private JScrollPane scrollContent;
    private JLabel txtBcc, txtCc, txtToYou;
    private JEditorPane txtContent;
    private JLabel txtFileName, txtSender, txtTime, txtTitle, lblFileName;
    private Email currentEmail;
    private String clientFolder = "src/nhom50/CLIENT/FileClient/";
    private String reciver = "";
    private String bcc;
    private String cc;

    public Read2(Email email) {
        Helper.changeLNF("Windows");
        initComponents();
        this.currentEmail = email;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        txtContent.setContentType("text/html");
        String text = email.getContent();
        txtContent.setText(text);
        txtSender.setText(email.getSenderEmail());
        txtTitle.setText(email.getSubject());
        txtTime.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(email.getCreatedAt()));
        String myEmail = GlobalState.currentUser.getEmail();
        //txtToYou.setText("đến " + email.getReceiverEmail());
        if (!email.getBcc().isEmpty()) {
            // txtBcc.setText(" BBC: " + email.getBcc());
            if (currentEmail.getSenderEmail().equals(GlobalState.currentUser.getEmail())) {
                txtBcc.setText(" BBC: " + email.getBcc());
                this.bcc = myEmail;
            }
            if (email.getBcc().contains(myEmail)) {
                txtBcc.setText(" BBC: " + myEmail);
                this.bcc = myEmail;
                System.out.println(this.cc + " || " + this.bcc);
            }
        }
        if (!email.getCc().isEmpty()) {
            txtCc.setText(" CC: " + email.getCc());
//            if (email.getCc().contains(myEmail)) {
            this.cc = email.getCc();
            txtCc.setText(" CC: " + cc);
            System.out.println(this.cc + " || " + this.bcc);
//            }
        }
        if (!email.getFileName().equals("") || !email.getFileName().isEmpty()) {
            txtFileName.setText(email.getFileName());
            File f = new File("src/nhom50/CLIENT/FileClient/" + email.getFileName());
            if (f.exists()) {
                btnDownload.setText("Mở");
            } else {
                btnDownload.setText("Download");
                btnDownload.addActionListener(e -> {

                });
            }
        } else {
            txtFileName.setText("Không");
            btnDownload.setEnabled(false);
            ;
        }


        // trạng thái read
        // kiểm tra user hiện tại có phải là người nhận không
        // nếu là người nhận thì chuyển sang read
        // check này là cho trường hợp sent ủa user
        if (currentEmail.getReceiverEmail().equals(GlobalState.currentUser.getEmail()) && email.getStatus().equals("INBOX")) {
            readEmail(email.getId());
        }

        // Check xem current user có phải người gửi không => Nếu phải thì không cho đánh dấu SPAM
        if (GlobalState.currentUser.getEmail().equals(currentEmail.getSenderEmail())) {
            btnSpam.setEnabled(false);
        }

        System.out.println("Global: " + GlobalState.currentUser.getEmail());
        System.out.println("Current: " + currentEmail.getSenderEmail());

    }

    public void readEmail(String uuid) {
        try {
            uuid = Hybrid_Encryption.encryptAES(uuid, ClientMain.randomKey);
            String encryptRandomKey = Hybrid_Encryption.encryptRSA(ClientMain.randomKey, ClientMain.pKey);
            HashMap<String, String> sendServer = new HashMap<>();
            sendServer.put("cmd", "cmd_read");
            sendServer.put("key", encryptRandomKey);
            sendServer.put("value", uuid);
            String strSend = new Gson().toJson(sendServer);
            GlobalHelper.sendMessage(ClientMain.out, strSend);
            String message = (String) ClientMain.in.readObject();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Đã có lỗi xảy ra khi đọc email, vui lòng tắt chương trình và bật lại");
        }
    }

    public void deleteEmail(String uuid) {
        try {
            uuid = Hybrid_Encryption.encryptAES(uuid, ClientMain.randomKey);
            String encryptRandomKey = Hybrid_Encryption.encryptRSA(ClientMain.randomKey, ClientMain.pKey);
            HashMap<String, String> sendServer = new HashMap<>();
            sendServer.put("cmd", "cmd_delete");
            sendServer.put("key", encryptRandomKey);
            sendServer.put("value", uuid);
            String strSend = new Gson().toJson(sendServer);
            GlobalHelper.sendMessage(ClientMain.out, strSend);
            String message = (String) ClientMain.in.readObject();
            String input = Hybrid_Encryption.decryptAES(message, ClientMain.randomKey);
            org.json.JSONObject response = new org.json.JSONObject(input);
            JOptionPane.showMessageDialog(null, response.get("message"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Máy chủ tắt hoặc mất sóng");
        }
    }

    public void spamEmail(String uuid) {
        try {
            uuid = Hybrid_Encryption.encryptAES(uuid, ClientMain.randomKey);
            String encryptRandomKey = Hybrid_Encryption.encryptRSA(ClientMain.randomKey, ClientMain.pKey);
            HashMap<String, String> sendServer = new HashMap<>();
            sendServer.put("cmd", "cmd_spam");
            sendServer.put("key", encryptRandomKey);
            sendServer.put("value", uuid);
            String strSend = new Gson().toJson(sendServer);
            GlobalHelper.sendMessage(ClientMain.out, strSend);
            String message = (String) ClientMain.in.readObject();
            String input = Hybrid_Encryption.decryptAES(message, ClientMain.randomKey);
            org.json.JSONObject response = new org.json.JSONObject(input);
            JOptionPane.showMessageDialog(null, response.get("message"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Máy chủ tắt hoặc mất sóng");
        }
    }

    private void initComponents() {

        pnlMain = new JPanel();
        txtTitle = new JLabel();
        txtSender = new JLabel();
        txtTime = new JLabel();
        scrollContent = new JScrollPane();
        txtContent = new JEditorPane();
        btnRep = new JButton();
        btnRepAll = new JButton();
        btnDelete = new JButton();
        btnSpam = new JButton();
        btnForward = new JButton();
        txtToYou = new JLabel();
        txtCc = new JLabel();
        txtBcc = new JLabel();
        lblFileName = new JLabel();
        txtFileName = new JLabel();
        btnDownload = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 90, 233));
        setForeground(new java.awt.Color(255, 255, 255));

        pnlMain.setBackground(new java.awt.Color(255, 255, 255));

        txtTitle.setFont(Constants.font.deriveFont(Constants.fontSizeTitle));
        txtTitle.setText("Mail Title");

        txtSender.setFont(Constants.font.deriveFont(Constants.fontSize));
        txtSender.setText("sender@mail.com");

        txtBcc.setFont(Constants.font.deriveFont(Constants.fontSize));
        txtCc.setFont(Constants.font.deriveFont(Constants.fontSize));
        txtFileName.setFont(Constants.font.deriveFont(Constants.fontSize));
        txtToYou.setFont(Constants.font.deriveFont(Constants.fontSize));

        txtTime.setFont(Constants.font.deriveFont(Constants.fontSize));
        txtTime.setText("11:14, 11/11/2022 ");

//        txtContent.setColumns(20);
//        txtContent.setRows(5);
        scrollContent.setViewportView(txtContent);
        txtContent.setEditable(false);
        txtContent.setFont(Constants.font.deriveFont(Constants.fontSize));

        btnRep.setText("Reply");
        btnRep.setIcon(new ImageIcon("images/reply_16px.png"));
        btnRep.setFont(Constants.font.deriveFont(Constants.fontSize));
        btnRep.setFocusPainted(false);
        btnRep.addActionListener(e -> {
            try {
                SendMailScreen screen = new SendMailScreen();
                screen.getTxtReceiver().setText(currentEmail.getSenderEmail());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        btnRepAll.setText("Reply All");
        btnRepAll.setIcon(new ImageIcon("images/reply_all_16px.png"));
        btnRepAll.setFont(Constants.font.deriveFont(Constants.fontSize));
        btnRepAll.setFocusPainted(false);
        btnRepAll.addActionListener(e -> {
            try {
                System.out.println(cc + " || " + bcc);
                SendMailScreen screen = new SendMailScreen();
                screen.getTxtReceiver().setText(currentEmail.getSenderEmail());
                screen.getTxtCc().setText(cc);
                if (currentEmail.getSenderEmail().equals(GlobalState.currentUser.getEmail())) {
                    screen.getTxtBcc().setText(bcc);
                }
            } catch (Exception ex) {

            }
        });

        btnDelete.setText("Delete");
        btnDelete.setIcon(new ImageIcon("images/delete_16px.png"));
        btnDelete.setFont(Constants.font.deriveFont(Constants.fontSize));
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> {
            btnDeleteActionPerformed(e);
        });
        btnSpam.setText("Spam");
        btnSpam.setIcon(new ImageIcon("images/info_16px.png"));
        btnSpam.setFont(Constants.font.deriveFont(Constants.fontSize));
        btnSpam.setFocusPainted(false);
        btnSpam.addActionListener(e -> {
            String uuid = currentEmail.getId();
            spamEmail(uuid);
        });

        btnForward.setText("Forward");
        btnForward.setIcon(new ImageIcon("images/delete_16px.png"));
        btnForward.setFont(Constants.font.deriveFont(Constants.fontSize));
        btnForward.setFocusPainted(false);

        lblFileName.setText("File đính kèm:");
        lblFileName.setFont(Constants.font.deriveFont(Constants.fontSize));

        txtFileName.setText("file.name");

        btnDownload.setText("Download");
        btnDownload.setIcon(new ImageIcon("images/download_16px.png"));
        btnDownload.setFont(Constants.font.deriveFont(Constants.fontSize));
        btnDownload.setFocusPainted(false);
        btnDownload.addActionListener(e -> {
            if (!currentEmail.getFileName().isEmpty()) {
                File f = new File("src/nhom50/CLIENT/FileClient/" + currentEmail.getFileName());
                if (f.exists()) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.open(f);
                    } catch (IOException ioException) {
                        JOptionPane.showMessageDialog(null, "Could not find program to open file");
                    }
                } else {
                    DownLoadFile dl = new DownLoadFile();
                    Thread downloadThread = new Thread(dl);
                    downloadThread.start();
                }
            }
        });

        GroupLayout pnlMainLayout = new GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
                pnlMainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pnlMainLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlMainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                                                .addComponent(scrollContent)
                                                .addContainerGap())
                                        .addGroup(GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                                                .addGroup(pnlMainLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addGroup(pnlMainLayout.createSequentialGroup()
                                                                .addComponent(txtTitle)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(btnSpam)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(btnDelete))
                                                        .addGroup(pnlMainLayout.createSequentialGroup()
                                                                .addGroup(pnlMainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addGroup(pnlMainLayout.createSequentialGroup()
                                                                                .addComponent(txtToYou)
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(txtCc)
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(txtBcc))
                                                                        .addComponent(txtSender))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(txtTime)))
                                                .addGap(19, 19, 19))
                                        .addGroup(pnlMainLayout.createSequentialGroup()
                                                .addComponent(btnRep)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnRepAll)
                                                .addGap(18, 18, 18)
                                                //.addComponent(btnForward)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 510, Short.MAX_VALUE)
                                                .addComponent(lblFileName)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtFileName)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnDownload)
                                                .addContainerGap())))
        );
        pnlMainLayout.setVerticalGroup(
                pnlMainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pnlMainLayout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(pnlMainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtTitle)
                                        .addComponent(btnDelete)
                                        .addComponent(btnSpam))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlMainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlMainLayout.createSequentialGroup()
                                                .addComponent(txtSender)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(pnlMainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtToYou)
                                                        .addComponent(txtCc)
                                                        .addComponent(txtBcc)))
                                        .addGroup(pnlMainLayout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(txtTime)))
                                .addGap(39, 39, 39)
                                .addComponent(scrollContent, GroupLayout.PREFERRED_SIZE, 296, GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addGroup(pnlMainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnRep)
                                        .addComponent(btnRepAll)
                                       // .addComponent(btnForward)
                                        .addComponent(lblFileName)
                                        .addComponent(txtFileName)
                                        .addComponent(btnDownload))
                                .addContainerGap(100, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(pnlMain, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(pnlMain, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        String uuid = currentEmail.getId();
        deleteEmail(uuid);
    }

    private void btnForwardActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btnForward1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    class DownLoadFile implements Runnable {

        @Override
        public void run() {
            try {

                String fileName = currentEmail.getFileName();
                if (fileName.equals("") || fileName.isEmpty() || fileName.isBlank()) {
                    return;
                }
                System.out.println("DownLoadFile");
                String data = Hybrid_Encryption.encryptAES(fileName, ClientMain.randomKey);
                String encryptRandomKey = Hybrid_Encryption.encryptRSA(ClientMain.randomKey, ClientMain.pKey);
                HashMap<String, String> sendServer = new HashMap<>();
                sendServer.put("cmd", "cmd_downFiletoClient");
                sendServer.put("key", encryptRandomKey);
                sendServer.put("value", data);
                String strSend = new Gson().toJson(sendServer);
                GlobalHelper.sendMessage(ClientMain.out, strSend);
                Thread.sleep(3000);

                String message = (String) ClientMain.in.readObject();
                String input = Hybrid_Encryption.decryptAES(message, ClientMain.randomKey);
                System.out.println("Input: " + input);
                org.json.JSONObject object = new org.json.JSONObject(input);
                String filename = object.getString("filename");
                String dataObject = object.getString("data");
                byte[] dataFile = new Gson().fromJson(dataObject, new TypeToken<byte[]>() {
                }.getType());
                FileHandle.WriteFile(clientFolder + filename, dataFile);
                JOptionPane.showMessageDialog(null, "Xong");
                btnDownload.setText("Mở");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
