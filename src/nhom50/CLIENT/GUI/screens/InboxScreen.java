package nhom50.CLIENT.GUI.screens;

import com.google.gson.Gson;
import nhom50.CLIENT.ClientMain;
import nhom50.CLIENT.GUI.custom.ItemMail;
import nhom50.CLIENT.utils.GlobalState;
import nhom50.DTO.Email;
import nhom50.DTO.User;
import nhom50.Encrypt.Hybrid_Encryption;
import nhom50.HELPERS.GlobalHelper;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class InboxScreen extends JFrame {

    private ArrayList<Email> inboxMail = new ArrayList<>();
    private ArrayList<Email> sentMail = new ArrayList<>();
    private ArrayList<Email> deletedMail = new ArrayList<>();
    private ArrayList<Email> readMail = new ArrayList<>();
    private ArrayList<Email> spamMail = new ArrayList<>();
    private CardLayout cardLayout;
    private JPanel btnBin, btnCompose, btnInbox, btnRead, btnSend, btnSpam, indBin, indCompose, indSpam, indSend, indRead, indInbox;
    private JLabel jLabel1, jLabel11, jLabel12, jLabel14, jLabel5, jLabel6, jLabel7, jLabel9;
    private JPanel pnlInbox, pnlParent, pnlRead, pnlRight, sidePanel;
    private JScrollPane scrollInbox, scrollRead, scrollSend, scrollSpam, scrollBin;
    private Loading loadingScreen = new Loading();

    public InboxScreen() {
        initComponents();
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        //custom card
        JPanel pnlSend = new JPanel();
        JPanel pnlSpam = new JPanel();
        JPanel pnlBin = new JPanel();

        pnlSend.setPreferredSize(pnlRight.getPreferredSize());
        pnlSpam.setPreferredSize(pnlRight.getPreferredSize());
        pnlBin.setPreferredSize(pnlRight.getPreferredSize());

        scrollSend = new JScrollPane();
        scrollSpam = new JScrollPane();
        scrollBin = new JScrollPane();

        scrollSend.setPreferredSize(new Dimension(830, 629));
        scrollSpam.setPreferredSize(new Dimension(830, 629));
        scrollBin.setPreferredSize(new Dimension(830, 629));

        pnlSend.add(scrollSend);
        pnlBin.add(scrollBin);
        pnlSpam.add(scrollSpam);

        pnlRight.add(pnlSend, "card3");
        pnlRight.add(pnlSpam, "card4");
        pnlRight.add(pnlBin, "card5");

        this.getRootPane().setBorder(new LineBorder(new Color(76, 41, 211)));
        cardLayout = (CardLayout) pnlRight.getLayout();

        setColor(btnInbox);
        indInbox.setOpaque(true);

        getInboxMail(GlobalState.currentUser);
        updateListInbox();

        this.setTitle(GlobalState.currentUser.getEmail() + " - Inbox");
    }

    private void getInboxMail(User currentUser) {
        try {
            inboxMail.removeAll(inboxMail);
            // mã hoá dữ liệu gửi cho server  // chỗ này thừa vì trên server đã lấy đưojc user đang hoạt động trong thread
            String emails = Hybrid_Encryption.encryptAES(currentUser.getEmail(), ClientMain.randomKey);
            // mã hoá key của client
            String encryptRandomKey = Hybrid_Encryption.encryptRSA(ClientMain.randomKey, ClientMain.pKey);
            // đẩy dữ liệu vào HashMap => để convert sang json cho dễ
            HashMap<String, String> sendServer = new HashMap<>();
            sendServer.put("cmd", "cmd_getInboxMail");
            sendServer.put("key", encryptRandomKey);
            sendServer.put("value", emails);
            // convert HashMap thành json
            String strSend = new Gson().toJson(sendServer);
            // gửi cho server
            GlobalHelper.sendMessage(ClientMain.out, strSend);

            // đợi nhập dữ liệu từ server trả về
            // nếu server lỗi và không trả về hoặc đợi lâu quá thì nó văng exception ở dưới
            String message = (String) ClientMain.in.readObject();

            // nếu đã nhận được dữ liệu từ server thì bây giờ giải mã
            String input = Hybrid_Encryption.decryptAES(message, ClientMain.randomKey);
            // dữ liệu sau khi giải mã có dạng
            /* {
                  "isSuccess": true,
                  "message": "success",
                  "data": [
                    {
                      "id": "",
                      "senderEmail": "",
                      "senderName": "",
                      "receiverEmail": "",
                      "subject": "",
                      "cc": "",
                      "bcc": "",
                      "content": "",
                      "status": "",
                      "createdAt": "",
                      "size": 0,
                      "fileName": ""
                    },
                  ]
                }
            * */
            org.json.JSONObject response = new org.json.JSONObject(input);
            if (response.has("data")) {
                // tách dữ liệu từ json
                org.json.JSONArray emails_array = response.getJSONArray("data");
                System.out.println(emails_array.length());
                for (int i = 0; i < emails_array.length(); i++) {
                    org.json.JSONObject object = emails_array.getJSONObject(i);
                    Email email = new Email();
                    email.setId(object.getString("id"));
                    email.setSenderEmail(object.getString("senderEmail"));
                    email.setSenderName(object.getString("senderName"));
                    email.setReceiverEmail(object.getString("receiverEmail"));
                    email.setSubject(object.getString("subject"));
                    email.setCc(object.getString("cc"));
                    email.setBcc(object.getString("bcc"));
                    email.setContent(object.getString("content"));
                    email.setCreatedAt(new Date(object.getString("createdAt")));
                    email.setStatus(object.getString("status"));
                    email.setFileName(object.getString("fileName"));
                    // thêm vào array inboxMail
                    inboxMail.add(email);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Mất kết nối tới máy chủ");
        }
    }

    private void getSentMail(User currentUser) {
        try {
            sentMail.removeAll(sentMail);
            // mã hoá dữ liệu gửi cho server  // chỗ này hơi thừa vì trên server đã lấy đưojc user đang hoạt động trong thread
            String emails = Hybrid_Encryption.encryptAES(currentUser.getEmail(), ClientMain.randomKey);
            // mã hoá key của client
            String encryptRandomKey = Hybrid_Encryption.encryptRSA(ClientMain.randomKey, ClientMain.pKey);
            // đẩy dữ liệu vào HashMap => để convert sang json cho dễ
            HashMap<String, String> sendServer = new HashMap<>();
            sendServer.put("cmd", "cmd_getSentMail");
            sendServer.put("key", encryptRandomKey);
            sendServer.put("value", emails);
            // convert HashMap thành json
            String strSend = new Gson().toJson(sendServer);
            // gửi dữ liệu lên server
            GlobalHelper.sendMessage(ClientMain.out, strSend);
            // đợi nhập dữ liệu từ server trả về
            // nếu server lỗi và không trả về hoặc đợi lâu quá thì nó văng exception ở dưới
            String message = (String) ClientMain.in.readObject();
            // nếu đã nhận được dữ liệu từ server thì bây giờ giải mã
            String input = Hybrid_Encryption.decryptAES(message, ClientMain.randomKey);
            // dữ liệu sau khi giải mã có dạng
            /* {
                  "isSuccess": true,
                  "message": "success",
                  "data": [
                    {
                      "id": "",
                      "senderEmail": "",
                      "senderName": "",
                      "receiverEmail": "",
                      "subject": "",
                      "cc": "",
                      "bcc": "",
                      "content": "",
                      "status": "",
                      "createdAt": "",
                      "size": 0,
                      "fileName": ""
                    },
                  ]
                }
            * */


            // tách dữ liệu và gắn vào arraylist
            org.json.JSONObject response = new org.json.JSONObject(input);
            if (response.has("data")) {
                org.json.JSONArray emails_array = response.getJSONArray("data");
                System.out.println(emails_array.length());
                for (int i = 0; i < emails_array.length(); i++) {
                    org.json.JSONObject object = emails_array.getJSONObject(i);
                    Email email = new Email();
                    email.setId(object.getString("id"));
                    email.setSenderEmail(object.getString("senderEmail"));
                    email.setSenderName(object.getString("senderName"));
                    email.setReceiverEmail(object.getString("receiverEmail"));
                    email.setSubject(object.getString("subject"));
                    email.setCc(object.getString("cc"));
                    email.setBcc(object.getString("bcc"));
                    email.setContent(object.getString("content"));
                    email.setCreatedAt(new Date(object.getString("createdAt")));
                    email.setStatus(object.getString("status"));
                    email.setFileName(object.getString("fileName"));
                    sentMail.add(email);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Mất kết nối tới máy chủ");
        }
    }

    // tương tự các hàm trên
    private void getReadMail(User currentUser) {
        try {
            readMail.removeAll(readMail);
            String emails = Hybrid_Encryption.encryptAES(currentUser.getEmail(), ClientMain.randomKey);
            String encryptRandomKey = Hybrid_Encryption.encryptRSA(ClientMain.randomKey, ClientMain.pKey);
            HashMap<String, String> sendServer = new HashMap<>();
            sendServer.put("cmd", "cmd_getReadMail");
            sendServer.put("key", encryptRandomKey);
            sendServer.put("value", emails);
            String strSend = new Gson().toJson(sendServer);
            GlobalHelper.sendMessage(ClientMain.out, strSend);
            String message = (String) ClientMain.in.readObject();
            String input = Hybrid_Encryption.decryptAES(message, ClientMain.randomKey);
            org.json.JSONObject response = new org.json.JSONObject(input);
            if (response.has("data")) {
                org.json.JSONArray emails_array = response.getJSONArray("data");
                System.out.println(emails_array.length());
                for (int i = 0; i < emails_array.length(); i++) {
                    org.json.JSONObject object = emails_array.getJSONObject(i);
                    Email email = new Email();
                    email.setId(object.getString("id"));
                    email.setSenderEmail(object.getString("senderEmail"));
                    email.setSenderName(object.getString("senderName"));
                    email.setReceiverEmail(object.getString("receiverEmail"));
                    email.setSubject(object.getString("subject"));
                    email.setCc(object.getString("cc"));
                    email.setBcc(object.getString("bcc"));
                    email.setContent(object.getString("content"));
                    email.setCreatedAt(new Date(object.getString("createdAt")));
                    email.setStatus(object.getString("status"));
                    email.setFileName(object.getString("fileName"));
                    readMail.add(email);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Mất kết nối tới máy chủ");
        }
    }

    // tương tự ở trên
    private void getSpamMail(User currentUser) {
        try {
            spamMail.removeAll(spamMail);
            String emails = Hybrid_Encryption.encryptAES(currentUser.getEmail(), ClientMain.randomKey);
            String encryptRandomKey = Hybrid_Encryption.encryptRSA(ClientMain.randomKey, ClientMain.pKey);
            HashMap<String, String> sendServer = new HashMap<>();
            sendServer.put("cmd", "cmd_getSpamMail");
            sendServer.put("key", encryptRandomKey);
            sendServer.put("value", emails);
            String strSend = new Gson().toJson(sendServer);
            GlobalHelper.sendMessage(ClientMain.out, strSend);
            String message = (String) ClientMain.in.readObject();
            String input = Hybrid_Encryption.decryptAES(message, ClientMain.randomKey);
            org.json.JSONObject response = new org.json.JSONObject(input);
            if (response.has("data")) {
                org.json.JSONArray emails_array = response.getJSONArray("data");
                System.out.println(emails_array.length());
                for (int i = 0; i < emails_array.length(); i++) {
                    org.json.JSONObject object = emails_array.getJSONObject(i);
                    Email email = new Email();
                    email.setId(object.getString("id"));
                    email.setSenderEmail(object.getString("senderEmail"));
                    email.setSenderName(object.getString("senderName"));
                    email.setReceiverEmail(object.getString("receiverEmail"));
                    email.setSubject(object.getString("subject"));
                    email.setCc(object.getString("cc"));
                    email.setBcc(object.getString("bcc"));
                    email.setContent(object.getString("content"));
                    email.setCreatedAt(new Date(object.getString("createdAt")));
                    email.setStatus(object.getString("status"));
                    email.setFileName(object.getString("fileName"));
                    spamMail.add(email);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Mất kết nối tới máy chủ");
        }
    }

    // tương tự ở trên
    private void getDeletedMail(User currentUser) {
        try {
            deletedMail.removeAll(deletedMail);
            String emails = Hybrid_Encryption.encryptAES(currentUser.getEmail(), ClientMain.randomKey);
            String encryptRandomKey = Hybrid_Encryption.encryptRSA(ClientMain.randomKey, ClientMain.pKey);
            HashMap<String, String> sendServer = new HashMap<>();
            sendServer.put("cmd", "cmd_getDeleteMail");
            sendServer.put("key", encryptRandomKey);
            sendServer.put("value", emails);
            String strSend = new Gson().toJson(sendServer);
            GlobalHelper.sendMessage(ClientMain.out, strSend);
            String message = (String) ClientMain.in.readObject();
            String input = Hybrid_Encryption.decryptAES(message, ClientMain.randomKey);
            org.json.JSONObject response = new org.json.JSONObject(input);
            if (response.has("data")) {
                org.json.JSONArray emails_array = response.getJSONArray("data");
                System.out.println(emails_array.length());
                for (int i = 0; i < emails_array.length(); i++) {
                    org.json.JSONObject object = emails_array.getJSONObject(i);
                    Email email = new Email();
                    email.setId(object.getString("id"));
                    email.setSenderEmail(object.getString("senderEmail"));
                    email.setSenderName(object.getString("senderName"));
                    email.setReceiverEmail(object.getString("receiverEmail"));
                    email.setSubject(object.getString("subject"));
                    email.setCc(object.getString("cc"));
                    email.setBcc(object.getString("bcc"));
                    email.setContent(object.getString("content"));
                    email.setCreatedAt(new Date(object.getString("createdAt")));
                    email.setStatus(object.getString("status"));
                    email.setFileName(object.getString("fileName"));
                    deletedMail.add(email);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Mất kết nối tới máy chủ");
        }
    }

    private void initComponents() {
        pnlParent = new JPanel();
        sidePanel = new JPanel();
        btnInbox = new JPanel();
        indInbox = new JPanel();
        jLabel12 = new JLabel();
        jLabel1 = new JLabel();
        btnRead = new JPanel();
        indRead = new JPanel();
        jLabel5 = new JLabel();
        btnSend = new JPanel();
        indSend = new JPanel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        btnCompose = new JPanel();
        indCompose = new JPanel();
        jLabel9 = new JLabel();
        btnSpam = new JPanel();
        indSpam = new JPanel();
        jLabel11 = new JLabel();
        btnBin = new JPanel();
        indBin = new JPanel();
        jLabel14 = new JLabel();
        pnlRight = new JPanel();
        pnlInbox = new JPanel();
        scrollInbox = new JScrollPane();
        pnlRead = new JPanel();
        scrollRead = new JScrollPane();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(GlobalState.currentUser.getEmail());
        setLocationByPlatform(true);

        pnlParent.setLayout(new java.awt.BorderLayout());

        sidePanel.setBackground(new java.awt.Color(0, 90, 233));
        sidePanel.setForeground(new java.awt.Color(51, 51, 51));
        sidePanel.setPreferredSize(new java.awt.Dimension(250, 200));
        sidePanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                sidepaneMouseDragged(evt);
            }
        });
        sidePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                sidepaneMousePressed(evt);
            }
        });

        btnInbox.setBackground(new java.awt.Color(0, 90, 233));
        btnInbox.setForeground(new java.awt.Color(255, 255, 255));
        btnInbox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnInboxMousePressed(evt);
            }
        });
        btnInbox.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        indInbox.setOpaque(false);
        indInbox.setPreferredSize(new java.awt.Dimension(4, 40));

        GroupLayout indInboxLayout = new GroupLayout(indInbox);
        indInbox.setLayout(indInboxLayout);
        indInboxLayout.setHorizontalGroup(
                indInboxLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 4, Short.MAX_VALUE)
        );
        indInboxLayout.setVerticalGroup(
                indInboxLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 40, Short.MAX_VALUE)
        );

        btnInbox.add(indInbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 40));

        jLabel12.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Inbox");
        btnInbox.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Mail Server");

        btnRead.setBackground(new java.awt.Color(0, 90, 233));
        btnRead.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnReadMousePressed(evt);
            }
        });
        btnRead.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        indRead.setOpaque(false);
        indRead.setPreferredSize(new java.awt.Dimension(4, 40));

        GroupLayout indReadLayout = new GroupLayout(indRead);
        indRead.setLayout(indReadLayout);
        indReadLayout.setHorizontalGroup(
                indReadLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 4, Short.MAX_VALUE)
        );
        indReadLayout.setVerticalGroup(
                indReadLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 40, Short.MAX_VALUE)
        );

        btnRead.add(indRead, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 40));

        jLabel5.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Read");
        btnRead.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        btnSend.setBackground(new java.awt.Color(0, 90, 233));
        btnSend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnSendMousePressed(evt);
            }
        });
        btnSend.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        indSend.setOpaque(false);
        indSend.setPreferredSize(new java.awt.Dimension(4, 40));

        GroupLayout indSendLayout = new GroupLayout(indSend);
        indSend.setLayout(indSendLayout);
        indSendLayout.setHorizontalGroup(
                indSendLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 4, Short.MAX_VALUE)
        );
        indSendLayout.setVerticalGroup(
                indSendLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 40, Short.MAX_VALUE)
        );

        btnSend.add(indSend, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 40));

        jLabel6.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Send");
        btnSend.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        jLabel7.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("MAIL");

        btnCompose.setBackground(new java.awt.Color(0, 90, 233));
        btnCompose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                try {
                    btnComposeMousePressed(evt);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btnCompose.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        indCompose.setOpaque(false);
        indCompose.setPreferredSize(new java.awt.Dimension(4, 40));

        GroupLayout indComposeLayout = new GroupLayout(indCompose);
        indCompose.setLayout(indComposeLayout);
        indComposeLayout.setHorizontalGroup(
                indComposeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 4, Short.MAX_VALUE)
        );
        indComposeLayout.setVerticalGroup(
                indComposeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 40, Short.MAX_VALUE)
        );

        btnCompose.add(indCompose, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 40));

        jLabel9.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Compose");
        btnCompose.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        btnSpam.setBackground(new java.awt.Color(0, 90, 233));
        btnSpam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnSpamMousePressed(evt);
            }
        });
        btnSpam.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        indSpam.setOpaque(false);
        indSpam.setPreferredSize(new java.awt.Dimension(4, 40));

        GroupLayout indSpamLayout = new GroupLayout(indSpam);
        indSpam.setLayout(indSpamLayout);
        indSpamLayout.setHorizontalGroup(
                indSpamLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 4, Short.MAX_VALUE)
        );
        indSpamLayout.setVerticalGroup(
                indSpamLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 40, Short.MAX_VALUE)
        );

        btnSpam.add(indSpam, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 40));

        jLabel11.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Spam");
        btnSpam.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        btnBin.setBackground(new java.awt.Color(0, 90, 233));
        btnBin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnBinMousePressed(evt);
            }
        });
        btnBin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        indBin.setOpaque(false);
        indBin.setPreferredSize(new java.awt.Dimension(4, 40));

        GroupLayout indBinLayout = new GroupLayout(indBin);
        indBin.setLayout(indBinLayout);
        indBinLayout.setHorizontalGroup(
                indBinLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 4, Short.MAX_VALUE)
        );
        indBinLayout.setVerticalGroup(
                indBinLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 40, Short.MAX_VALUE)
        );

        btnBin.add(indBin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 40));

        jLabel14.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Bin");
        btnBin.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        GroupLayout sidepaneLayout = new GroupLayout(sidePanel);
        sidePanel.setLayout(sidepaneLayout);
        sidepaneLayout.setHorizontalGroup(
                sidepaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(btnInbox, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRead, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSend, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCompose, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSpam, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(sidepaneLayout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addGroup(sidepaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel1))
                                .addContainerGap(60, Short.MAX_VALUE))
        );
        sidepaneLayout.setVerticalGroup(
                sidepaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(sidepaneLayout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jLabel1)
                                .addGap(61, 61, 61)
                                .addComponent(jLabel7)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnInbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRead, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSend, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSpam, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCompose, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(206, Short.MAX_VALUE))
        );

        pnlParent.add(sidePanel, java.awt.BorderLayout.LINE_START);

        pnlRight.setBackground(new java.awt.Color(255, 255, 255));
        pnlRight.setLayout(new java.awt.CardLayout());

        pnlInbox.setBackground(new java.awt.Color(255, 255, 255));

        scrollInbox.setBorder(null);

        GroupLayout pnlInboxLayout = new GroupLayout(pnlInbox);
        pnlInbox.setLayout(pnlInboxLayout);
        pnlInboxLayout.setHorizontalGroup(
                pnlInboxLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scrollInbox, GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE)
        );
        pnlInboxLayout.setVerticalGroup(
                pnlInboxLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scrollInbox, GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
        );

        pnlRight.add(pnlInbox, "card1");

        pnlRead.setBackground(new java.awt.Color(255, 255, 255));

        scrollRead.setBorder(null);

        GroupLayout pnlReadLayout = new GroupLayout(pnlRead);
        pnlRead.setLayout(pnlReadLayout);
        pnlReadLayout.setHorizontalGroup(
                pnlReadLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scrollRead, GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE)
        );
        pnlReadLayout.setVerticalGroup(
                pnlReadLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scrollRead, GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
        );

        pnlRight.add(pnlRead, "card2");

        pnlParent.add(pnlRight, java.awt.BorderLayout.CENTER);

        getContentPane().add(pnlParent, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>

    private void sidepaneMousePressed(MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void sidepaneMouseDragged(MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void btnBinMousePressed(MouseEvent evt) {
        setColor(btnBin);
        resetColor(btnInbox);
        resetColor(btnCompose);
        resetColor(btnRead);
        resetColor(btnSend);
        resetColor(btnSpam);

        indInbox.setOpaque(false);
        indCompose.setOpaque(false);
        indRead.setOpaque(false);
        indSend.setOpaque(false);
        indBin.setOpaque(true);
        indSpam.setOpaque(false);

        cardLayout.show(pnlRight, "card5");
        this.setTitle(GlobalState.currentUser.getEmail() + " - Bin");
        getDeletedMail(GlobalState.currentUser);
        updateListBin();
    }

    private void btnSpamMousePressed(java.awt.event.MouseEvent evt) {
        setColor(btnSpam);
        resetColor(btnInbox);
        resetColor(btnCompose);
        resetColor(btnRead);
        resetColor(btnSend);
        resetColor(btnBin);

        indInbox.setOpaque(false);
        indCompose.setOpaque(false);
        indRead.setOpaque(false);
        indSend.setOpaque(false);
        indBin.setOpaque(false);
        indSpam.setOpaque(true);

        cardLayout.show(pnlRight, "card4");
        this.setTitle(GlobalState.currentUser.getEmail() + " - Spam");

        getSpamMail(GlobalState.currentUser);
        updateListSpam();
    }

    private void btnComposeMousePressed(MouseEvent evt) throws IOException {
        new SendMailScreen();
    }

    private void btnSendMousePressed(MouseEvent evt) {
        openloading();
        setColor(btnSend);
        resetColor(btnCompose);
        resetColor(btnRead);
        resetColor(btnInbox);
        resetColor(btnBin);
        resetColor(btnSpam);

        indInbox.setOpaque(false);
        indCompose.setOpaque(false);
        indRead.setOpaque(false);
        indSend.setOpaque(true);
        indBin.setOpaque(false);
        indSpam.setOpaque(false);
        getSentMail(GlobalState.currentUser);
        updateListSend();
        cardLayout.show(pnlRight, "card3");
        this.setTitle(GlobalState.currentUser.getEmail() + " - Send");
        closeLoading();
    }

    private void btnReadMousePressed(MouseEvent evt) {
        openloading();
        setColor(btnRead);
        resetColor(btnCompose);
        resetColor(btnInbox);
        resetColor(btnSend);
        resetColor(btnBin);
        resetColor(btnSpam);

        //indicators
        indInbox.setOpaque(false);
        indCompose.setOpaque(false);
        indRead.setOpaque(true);
        indSend.setOpaque(false);
        indBin.setOpaque(false);
        indSpam.setOpaque(false);

        cardLayout.show(pnlRight, "card2");
        this.setTitle(GlobalState.currentUser.getEmail() + " - Read");
        // gọi hàm
        getReadMail(GlobalState.currentUser);
        //
        updateListRead();
        closeLoading();
    }

    private void btnInboxMousePressed(MouseEvent evt) {
        openloading();
        setColor(btnInbox);
        resetColor(btnCompose);
        resetColor(btnRead);
        resetColor(btnSend);
        resetColor(btnSpam);
        resetColor(btnBin);

        indInbox.setOpaque(true);
        indCompose.setOpaque(false);
        indRead.setOpaque(false);
        indSend.setOpaque(false);
        indBin.setOpaque(false);
        indSpam.setOpaque(false);
        cardLayout.show(pnlRight, "card1");
        this.setTitle(GlobalState.currentUser.getEmail() + " - Inbox");

        getInboxMail(GlobalState.currentUser);
        updateListInbox();
        closeLoading();
    }

    void openloading() {
        loadingScreen.setVisible(true);
    }

    void closeLoading() {
        loadingScreen.setVisible(false);
    }

    // update giao diện inbox
    private void updateListInbox() {
        openloading();
        JPanel pnInbox = new JPanel();
        pnInbox.setLayout(new BoxLayout(pnInbox, BoxLayout.Y_AXIS));
        pnInbox.setBackground(Color.white);
        System.out.println("Length of inboxMail" + inboxMail.size());
        for (Email email : inboxMail) {
            ItemMail i = new ItemMail(email);
            i.setPreferredSize(new Dimension(800, 100));
            pnInbox.add(i);
        }
        scrollInbox.setViewportView(pnInbox);
        closeLoading();
    }

    // update giao diện read
    private void updateListRead() {
        JPanel pnRead = new JPanel();
        pnRead.setLayout(new BoxLayout(pnRead, BoxLayout.Y_AXIS));
        pnRead.setBackground(Color.white);
        for (Email email : readMail) {
            ItemMail i = new ItemMail(email);
            i.setPreferredSize(new Dimension(800, 100));
            pnRead.add(i);
        }
        scrollRead.setViewportView(pnRead);
    }

    // update giao diện sent
    private void updateListSend() {
        JPanel pnSend = new JPanel();
        pnSend.setLayout(new BoxLayout(pnSend, BoxLayout.Y_AXIS));
        pnSend.setBackground(Color.white);
        for (Email email : sentMail) {
            ItemMail i = new ItemMail(email);
            i.setPreferredSize(new Dimension(800, 100));
            pnSend.add(i);
        }
        scrollSend.setViewportView(pnSend);
    }

    // update giao diện spam
    private void updateListSpam() {
        JPanel pnSpam = new JPanel();
        pnSpam.setLayout(new BoxLayout(pnSpam, BoxLayout.Y_AXIS));
        pnSpam.setBackground(Color.white);
        for (Email email : spamMail) {
            ItemMail i = new ItemMail(email);
            i.setPreferredSize(new Dimension(800, 100));
            pnSpam.add(i);
        }
        scrollSpam.setViewportView(pnSpam);
    }

    // update giao diện bin
    private void updateListBin() {

        JPanel pnBin = new JPanel();
        pnBin.setLayout(new BoxLayout(pnBin, BoxLayout.Y_AXIS));
        pnBin.setBackground(Color.white);
        for (Email email : deletedMail) {
            ItemMail i = new ItemMail(email);
            i.setPreferredSize(new Dimension(800, 100));
            pnBin.add(i);
        }
        scrollBin.setViewportView(pnBin);
    }

    private void updateListCompose() {
    }

    int xy, xx;

    // set and reset color
    void setColor(JPanel panel) {
        panel.setBackground(new Color(135, 112, 225));
    }

    void resetColor(JPanel panel) {
        panel.setBackground(new Color(0, 90, 233));
    }


}