package nhom50.DTO;

import java.io.Serializable;
import java.util.Date;

public class Email implements Serializable {

    private String id;
    private String senderEmail;
    private String senderName;
    private String receiverEmail;
    private String subject;
    private String cc;
    private String bcc;
    private String content;
    private String status;
    private Date createdAt;
    private Date deletedAt;
    private float size;
    private String fileName;
    public static final String STATUS_SENT = "SENT";
    public static final String STATUS_READ = "READ";
    public static final String STATUS_SPAM = "SPAM";
    public static final String STATUS_DELETED = "DELETED";

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Email() {
    }

    public Email(String id, String senderEmail, String senderName, String receiverEmail, String subject, String cc, String bcc, String content, String status, Date createdAt, Date deletedAt) {
        this.id = id;
        this.senderEmail = senderEmail;
        this.senderName = senderName;
        this.receiverEmail = receiverEmail;
        this.subject = subject;
        this.cc = cc;
        this.bcc = bcc;
        this.content = content;
        this.status = status;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }
    public Email(String id, String senderEmail, String senderName, String receiverEmail, String subject, String cc, String bcc, String content, String status, Date createdAt, Date deletedAt,String fileName) {
        this.id = id;
        this.senderEmail = senderEmail;
        this.senderName = senderName;
        this.receiverEmail = receiverEmail;
        this.subject = subject;
        this.cc = cc;
        this.bcc = bcc;
        this.content = content;
        this.status = status;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.fileName = fileName;
    }

    public Email(String id, String senderEmail, String senderName, String receiverEmail, String subject, String cc, String bcc, String content, String status, Date createdAt, Date deletedAt, float size) {
        this.id = id;
        this.senderEmail = senderEmail;
        this.senderName = senderName;
        this.receiverEmail = receiverEmail;
        this.subject = subject;
        this.cc = cc;
        this.bcc = bcc;
        this.content = content;
        this.status = status;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.size = size;
    }

    public Email(String id, String senderEmail, String senderName, String receiverEmail, String subject, String cc, String bcc, String content, String status) {
        this.id = id;
        this.senderEmail = senderEmail;
        this.senderName = senderName;
        this.receiverEmail = receiverEmail;
        this.subject = subject;
        this.cc = cc;
        this.bcc = bcc;
        this.content = content;
        this.status = status;
    }

    public Email(String id, String senderEmail, String senderName, String receiverEmail, String subject, String cc, String bcc, String content, String status,String fileName) {
        this.id = id;
        this.senderEmail = senderEmail;
        this.senderName = senderName;
        this.receiverEmail = receiverEmail;
        this.subject = subject;
        this.cc = cc;
        this.bcc = bcc;
        this.content = content;
        this.status = status;
        this.fileName = fileName;
    }
    public Email(String id, String senderEmail, String senderName, String receiverEmail, String subject, String cc, String bcc, String content, String status,String fileName, float size) {
        this.id = id;
        this.senderEmail = senderEmail;
        this.senderName = senderName;
        this.receiverEmail = receiverEmail;
        this.subject = subject;
        this.cc = cc;
        this.bcc = bcc;
        this.content = content;
        this.status = status;
        this.fileName = fileName;
        this.size = size;
    }
    @Override
    public String toString() {
        return "Email{" +
                "id='" + id + '\'' +
                ", senderEmail='" + senderEmail + '\'' +
                ", senderName='" + senderName + '\'' +
                ", receiverEmail='" + receiverEmail + '\'' +
                ", subject='" + subject + '\'' +
                ", cc='" + cc + '\'' +
                ", bcc='" + bcc + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", deletedAt=" + deletedAt +
                ", size=" + size +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }
}
