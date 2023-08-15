package nhom50.SERVER.BUS;


import nhom50.CLIENT.utils.Constants;
import nhom50.DTO.Response;
import nhom50.DTO.User;
import nhom50.SERVER.DAO.UserDAO;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.UUID;

public class UserBUS {

    private static UserBUS instance = null;
    private final UserDAO userDAO = UserDAO.getInstance();

    private UserBUS() {
    }

    public static UserBUS getInstance() {
        if (instance == null) {
            instance = new UserBUS();
        }
        return instance;
    }

    public Response createUser(String email, String firstName, String lastName, String password, String confirmPassword) {
        // validate
        if (!Constants.validate(email)) {
            return new Response(false, "Email sai định dạng!", null);
        }
        if (firstName.trim().length() == 0) {
            return new Response(false, "Không được để trống họ đệm!", null);
        }
        if (lastName.trim().length() == 0) {
            return new Response(false, "Không được để trống tên!", null);
        }
        if (password.trim().length() < 8) {
            return new Response(false, "Mật khẩu phải có ít nhất 8 ký tự!", null);
        }
        if (!password.equals(confirmPassword)) {
            return new Response(false, "Mật khẩu xác nhận không khớp!", null);
        }
        User user = new User(email.trim(), firstName.trim(), lastName.trim(), md5(password));
        user.setId(UUID.randomUUID().toString());
        return userDAO.createUser(user);
    }

    public Response login(String email, String password) {
        User user = userDAO.findUserByEmail(email);
        // kiểm tra user
        if (user == null) { // trường hợp không tồn tại
            return new Response(false, "Tài khoản không tồn tại", null);
        } else if (user != null && user.getStatus().equals("BLOCK")) { // trường hợp tồn tại nhưng tài khoản bị admin khoá
            return new Response(false, "Tài khoản đã bị khoá, vui lòng liên hệ admin", null);
        }
        // các trường hợp còn lại
        return userDAO.loginUser(email, md5(password));
    }

    public User findUserByEmail(String email) {
        return userDAO.findUserByEmail(email);
    }

    public void changeStatusOnline(User user, int status) {
        if (user == null) {
            System.out.println("User không tồn tại");
            return;
        }
        userDAO.changeStatusOnline(user, status);
    }

    public String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashText = no.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public Response changeStatusUser(String emailblock, String status) {
        return UserDAO.getInstance().changeStatusUser(emailblock, status);
    }

    public Response editStorage(float storage) {
        return UserDAO.getInstance().editStorage(storage);
    }

    public Response editStorageUser(String email, float storageUser) {
        if (findUserByEmail(email) == null) {
            return new Response(false, "Email không tồn tại", null);
        }
        return UserDAO.getInstance().editStorageUser(email, storageUser);
    }

    public boolean updateStorageUser(String email, float user_storage, float file_size) {
        return UserDAO.getInstance().updateStorageUser(email, user_storage, file_size);
    }

    public ArrayList<User> getAllUsers() {
        return UserDAO.getInstance().getAllUsers();
    }

    public void setOffline() {
        UserDAO.getInstance().setOffline();
    }
}
