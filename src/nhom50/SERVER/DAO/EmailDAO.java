package nhom50.SERVER.DAO;

import nhom50.DTO.Email;
import nhom50.DTO.Response;
import nhom50.DTO.User;
import nhom50.SERVER.Connect.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class EmailDAO {

    public static EmailDAO instance;

    public static EmailDAO getInstance() {
        if (instance == null) {
            instance = new EmailDAO();
            conn = Database.getInstance().getConnection();
        }
        return instance;
    }

    public static Connection conn = null;

    public void insertFile(String emailID, String filePath) {
        try {
            String sql = "INSERT INTO `attachments`(`id`, `email_id`, `filepath`, `active`, `created_at`) VALUES (?,?,?,?,?)";
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, UUID.randomUUID().toString());
            pre.setString(2, emailID);
            pre.setString(3, filePath);
            pre.setInt(4, 1);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            pre.setTimestamp(5, timestamp);
            pre.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Response sendMail(Email email) {
        try {
            boolean result = false;
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (email.getReceiverEmail() != null && !email.getReceiverEmail().isEmpty()) {
                String receiverArr[] = email.getReceiverEmail().split(",");
                for (String receiver : receiverArr) {
                    String emailid = UUID.randomUUID().toString();
                    String sql =
                            "INSERT INTO `emails`(`id`,`sender_email`, `receiver_email`, `subject`, `CC`, `BCC`, `content`, `status`,`created_at`)" +
                                    "VALUES (?,?,?,?,?,?,?,?,?)";
                    PreparedStatement pre = this.conn.prepareStatement(sql);
                    pre.setString(1, emailid);
                    pre.setString(2, email.getSenderEmail());
                    pre.setString(3, receiver);
                    pre.setString(4, email.getSubject());
                    pre.setString(5, email.getCc());
                    pre.setString(6, email.getBcc());
                    pre.setString(7, email.getContent());
                    pre.setString(8, "INBOX");
                    pre.setTimestamp(9, timestamp);
                    result = pre.executeUpdate() > 0;
                    if (email.getFileName() != null && !email.getFileName().isEmpty()) {
                        insertFile(emailid, email.getFileName());
                    }
                }
            }
            if (email.getBcc() != null && !email.getBcc().isEmpty()) {
                String BCCArr[] = email.getBcc().split(",");
                System.out.println("Length BCC " + BCCArr.length);
                for (String BCC : BCCArr) {
                    String emailID = UUID.randomUUID().toString();
                    String sql =
                            "INSERT INTO `emails`(`id`,`sender_email`, `receiver_email`, `subject`, `CC`, `BCC`, `content`, `status`,`created_at`)" +
                                    "VALUES (?,?,?,?,?,?,?,?,?)";
                    PreparedStatement pre = this.conn.prepareStatement(sql);
                    pre.setString(1, emailID);
                    pre.setString(2, email.getSenderEmail());
                    pre.setString(3, BCC);
                    pre.setString(4, email.getSubject());
                    pre.setString(5, email.getCc());
                    pre.setString(6, email.getBcc());
                    pre.setString(7, email.getContent());
                    pre.setString(8, "INBOX");
                    pre.setTimestamp(9, timestamp);
                    result = pre.executeUpdate() > 0;
                    if (email.getFileName() != null && !email.getFileName().isEmpty()) {
                        insertFile(emailID, email.getFileName());
                    }
                }
            }
            if (email.getCc() != null && !email.getCc().isEmpty()) {
                String CCArr[] = email.getCc().split(",");
                System.out.println("Length  CC" + CCArr.length);
                for (String CC : CCArr) {
                    String emailID = UUID.randomUUID().toString();
                    String sql =
                            "INSERT INTO `emails`(`id`,`sender_email`, `receiver_email`, `subject`, `CC`, `BCC`, `content`, `status`,`created_at`)" +
                                    "VALUES (?,?,?,?,?,?,?,?,?)";
                    PreparedStatement pre = this.conn.prepareStatement(sql);
                    pre.setString(1, UUID.randomUUID().toString());
                    pre.setString(2, email.getSenderEmail());
                    pre.setString(3, CC);
                    pre.setString(4, email.getSubject());
                    pre.setString(5, email.getCc());
                    pre.setString(6, email.getBcc());
                    pre.setString(7, email.getContent());
                    pre.setString(8, "INBOX");
                    pre.setTimestamp(9, timestamp);
                    result = pre.executeUpdate() > 0;
                    if (email.getFileName() != null && !email.getFileName().isEmpty()) {
                        insertFile(emailID, email.getFileName());
                    }
                }
            }
            if (result) {
                return new Response(true, "Gửi email thành công", email);
            } else {
                return new Response(false, "Gửi email thất bại ", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response(false, "Gửi email thất bại ", null);
    }

    public Response changeStatusEmail(String uuid, String status) {
        try {
            String sql = "UPDATE `emails` SET status = ? WHERE id = ?";
            PreparedStatement pre = this.conn.prepareStatement(sql);
            pre.setString(1, status);
            pre.setString(2, uuid);
            return pre.executeUpdate() > 0 ? new Response(true, "Đã đánh dấu :" + status, null)
                    : new Response(false, "Status not updated", null);
        } catch (Exception e) {
            return new Response(false, "Đã có lỗi xảy ra", null);
        }
    }

    // hàm get all mail không sử dụng nữa nhé
    public Response getAllMail(User currentUser) {
        try {
            ArrayList<Email> allMail = new ArrayList<Email>();
            String uuid = currentUser.getId();
            String sql = "SELECT *, u.email from emails e, users u WHERE e.sender_email = ? or e.receiver_email = ?;";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, uuid);
            pstm.setString(2, uuid);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);
                String sender_email = rs.getString(2);
                String receiver_email = rs.getString(3);
                String subject = rs.getString(4);
                String CC = rs.getString(5);
                String BCC = rs.getString(6);
                String content = rs.getString(7);
                String status = rs.getString(8);
                Date created_at = rs.getTimestamp(9);
                Date deleted_at = rs.getTimestamp(10);
                float size = rs.getFloat(11);
                User user = UserDAO.getInstance().findUserByID(id);
                String sender_name = user.getFirstName() + " " + user.getLastName();
                Email email = new Email(id, sender_email, sender_name, receiver_email, subject, CC, BCC, content, status, created_at, deleted_at, size);
                allMail.add(email);

            }
            return new Response(true, "success", allMail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response(false, "fails", null);
    }

    public String getEmailFilePath(String emailId) {
        try {
            String sql = "SELECT * FROM `attachments` WHERE email_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, emailId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String path = rs.getString("filepath");
                return path;
            }
            return "";
        } catch (Exception e) {
            return "";
        }

    }

    public Response getInboxMail(User currentUser) {
        try {
            ArrayList<Email> allMail = new ArrayList<Email>();
            String sql = "SELECT * FROM `emails` WHERE receiver_email = ? and status = 'INBOX' ORDER BY DATE(created_at) DESC";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, currentUser.getEmail());
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);
                String sender_email = rs.getString(2);
                String receiver_email = rs.getString(3);
                String subject = rs.getString(4);
                String CC = rs.getString(5);
                String BCC = rs.getString(6);
                String content = rs.getString(7);
                String status = rs.getString(8);
                Date created_at = rs.getTimestamp(9);
                Date deleted_at = rs.getTimestamp(10);
                float size = rs.getFloat(11);
                User user = UserDAO.getInstance().findUserByEmail(sender_email);
                String sender_name = user.getFirstName() + " " + user.getLastName();
                String file_path = getEmailFilePath(id);
                Email email = new Email(id, sender_email, sender_name, receiver_email, subject, CC, BCC, content, status, created_at, deleted_at, file_path);
                allMail.add(email);
            }
            return new Response(true, "success", allMail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response(false, "fails", null);
    }

    public Response getSentMail(User currentUser) {
        try {
            ArrayList<Email> allMail = new ArrayList<Email>();
            String sql = "SELECT * FROM `emails` WHERE sender_email = ? and status != 'DELETED' GROUP BY `created_at` ORDER BY DATE(created_at) DESC ";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, currentUser.getEmail());
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);
                String sender_email = rs.getString(2);
                String receiver_email = rs.getString(3);
                String subject = rs.getString(4);
                String CC = rs.getString(5);
                String BCC = rs.getString(6);
                String content = rs.getString(7);
                String status = rs.getString(8);
                Date created_at = rs.getTimestamp(9);
                Date deleted_at = rs.getTimestamp(10);
                float size = rs.getFloat(11);
                User user = UserDAO.getInstance().findUserByEmail(sender_email);
                String sender_name = user.getFirstName() + " " + user.getLastName();
                String file_path = getEmailFilePath(id);
                Email email = new Email(id, sender_email, sender_name, receiver_email, subject, CC, BCC, content, status, created_at, deleted_at, file_path);
                allMail.add(email);
            }
            return new Response(true, "success", allMail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response(false, "fails", null);
    }

    public Response getReadMail(User currentUser) {
        try {
            ArrayList<Email> allMail = new ArrayList<Email>();
            String sql = "SELECT * FROM `emails` WHERE receiver_email = ? and status = 'READ'";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, currentUser.getEmail());
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);
                String sender_email = rs.getString(2);
                String receiver_email = rs.getString(3);
                String subject = rs.getString(4);
                String CC = rs.getString(5);
                String BCC = rs.getString(6);
                String content = rs.getString(7);
                String status = rs.getString(8);
                Date created_at = rs.getTimestamp(9);
                Date deleted_at = rs.getTimestamp(10);
                float size = rs.getFloat(11);
                User user = UserDAO.getInstance().findUserByEmail(sender_email);
                String sender_name = user.getFirstName() + " " + user.getLastName();
                String file_path = getEmailFilePath(id);
                Email email = new Email(id, sender_email, sender_name, receiver_email, subject, CC, BCC, content, status, created_at, deleted_at, file_path);
                allMail.add(email);
            }
            return new Response(true, "success", allMail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response(false, "fails", null);
    }

    public Response getSpamMail(User currentUser) {
        try {
            ArrayList<Email> allMail = new ArrayList<Email>();
            String sql = "SELECT * FROM `emails` WHERE receiver_email = ? and status = 'SPAM'";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, currentUser.getEmail());
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);
                String sender_email = rs.getString(2);
                String receiver_email = rs.getString(3);
                String subject = rs.getString(4);
                String CC = rs.getString(5);
                String BCC = rs.getString(6);
                String content = rs.getString(7);
                String status = rs.getString(8);
                Date created_at = rs.getTimestamp(9);
                Date deleted_at = rs.getTimestamp(10);
                float size = rs.getFloat(11);
                User user = UserDAO.getInstance().findUserByEmail(sender_email);
                String sender_name = user.getFirstName() + " " + user.getLastName();
                String file_path = getEmailFilePath(id);
                Email email = new Email(id, sender_email, sender_name, receiver_email, subject, CC, BCC, content, status, created_at, deleted_at, file_path);
                allMail.add(email);
            }
            return new Response(true, "success", allMail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response(false, "fails", null);
    }

    public Response getDeleteMail(User currentUser) {
        try {
            ArrayList<Email> allMail = new ArrayList<Email>();
            String sql = "SELECT * FROM `emails` WHERE (receiver_email = ? or sender_email = ?) and status = 'DELETED' ORDER BY DATE(created_at) DESC";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, currentUser.getEmail());
            pstm.setString(2, currentUser.getEmail());
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);
                String sender_email = rs.getString(2);
                String receiver_email = rs.getString(3);
                String subject = rs.getString(4);
                String CC = rs.getString(5);
                String BCC = rs.getString(6);
                String content = rs.getString(7);
                String status = rs.getString(8);
                Date created_at = rs.getTimestamp(9);
                Date deleted_at = rs.getTimestamp(10);
                float size = rs.getFloat(11);
                User user = UserDAO.getInstance().findUserByEmail(currentUser.getEmail());
                String sender_name = user.getFirstName() + " " + user.getLastName();
                String file_path = getEmailFilePath(id);
                Email email = new Email(id, sender_email, sender_name, receiver_email, subject, CC, BCC, content, status, created_at, deleted_at, file_path);
                allMail.add(email);
            }
            return new Response(true, "success", allMail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response(false, "fails", null);
    }
}
