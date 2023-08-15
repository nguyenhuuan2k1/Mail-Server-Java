package nhom50.SERVER.BUS;

import nhom50.DTO.Email;
import nhom50.DTO.Response;
import nhom50.DTO.User;
import nhom50.SERVER.DAO.EmailDAO;

public class EmailBUS {
    public static EmailBUS instance;

    public static EmailBUS getInstance() {
        if (instance == null) {
            instance = new EmailBUS();
        }
        return instance;
    }

    public Response sendMail(Email email) {
        if (email.getSenderEmail().isEmpty() || email.getSenderEmail().isBlank()) {
            return new Response(false, "Người gửi không được để trống", null);
        }
        if (email.getReceiverEmail().isEmpty() || email.getReceiverEmail().isBlank()) {
            return new Response(false, "Không được để trống người nhận", null);
        }
        if (email.getSubject().isEmpty() || email.getSubject().isBlank()) {
            return new Response(false, "Tiêu đề không được để trống", null);
        }
        if (!email.getReceiverEmail().isEmpty()) {
            String receiverEmail[] = email.getReceiverEmail().split(",");
            for (String reciverUser : receiverEmail) {
                User reciver = UserBUS.getInstance().findUserByEmail(reciverUser.trim());
                if (reciver == null) {
                    return new Response(false, "Người nhận không hợp lệ", null);
                }
                if (reciver != null && reciver.getStorage() - email.getSize() <= 0) {
                    return new Response(false, reciver.getEmail() + " không đủ dung lượng trống", null);
                }
            }
        }
        User sender = UserBUS.getInstance().findUserByEmail(email.getSenderEmail());

        if (!email.getSenderEmail().isEmpty()) {
            if (sender.getStorage() - email.getSize() <= 0) {
                return new Response(false, "Bạn không đủ dung lượng trống", null);
            }
        }
        if (!email.getBcc().isEmpty()) {
            String[] arrBCC = email.getBcc().split(",");
            for (String bcc : arrBCC) {
                User user = UserBUS.getInstance().findUserByEmail(bcc.trim());
                if (user == null) {
                    return new Response(false, "Email " + bcc + " không tồn tại", null);
                }
                if (user.getStorage() - email.getSize() <= 0) {
                    return new Response(false, "Email " + bcc + " không đủ dung lượng trống", null);
                }

            }
        }
        if (!email.getCc().isEmpty()) {
            String[] cc = email.getCc().split(",");
            for (String item : cc) {
                User user = UserBUS.getInstance().findUserByEmail(item.trim());
                if (UserBUS.getInstance().findUserByEmail(item) == null) {
                    return new Response(false, "Email " + item + " không tồn tại", null);
                }
                if (user.getStorage() - email.getSize() <= 0) {
                    return new Response(false, "Email " + item + " không đủ dung lượng trống", null);
                }

            }
        }
        if (sender == null) {
            return new Response(false, "Người gửi không tồn tại", null);
        }
        // check file in email and update storage in user sender
        Response res = EmailDAO.getInstance().sendMail(email);
        if (res.isSuccess()) {
            System.out.println("respone send mail is success");
            System.out.println("email size is " + email.getSize());
            if (email.getSize() > 0f) {
                if (UserBUS.getInstance().updateStorageUser(email.getSenderEmail(), sender.getStorage(), email.getSize())) {
                    System.out.println("Updated storage success :" + email.getSenderEmail());
                }
            }
        }
        return res;
    }

    public Response changeStatusEmail(String uuid, String status) {
        return EmailDAO.getInstance().changeStatusEmail(uuid, status);
    }

    public Response getInboxMail(User user) {
        return EmailDAO.getInstance().getInboxMail(user);
    }

    public Response getAllMail(User currentUser) {
        return EmailDAO.getInstance().getAllMail(currentUser);
    }

    public Response getSentMail(User currentUser) {
        return EmailDAO.getInstance().getSentMail(currentUser);
    }

    public Response getReadMail(User currentUser) {
        return EmailDAO.getInstance().getReadMail(currentUser);
    }

    public Response getSpamMail(User currentUser) {
        return EmailDAO.getInstance().getSpamMail(currentUser);
    }

    public Response getDeleteMail(User currentUser) {
        return EmailDAO.getInstance().getDeleteMail(currentUser);
    }
}
