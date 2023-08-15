package nhom50.SERVER.DAO;

import nhom50.DTO.Response;
import nhom50.DTO.User;
import nhom50.SERVER.Connect.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UserDAO {

    private static UserDAO instance = null;
    private final Connection conn = Database.getInstance().getConnection();

    private UserDAO() {
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    public Response createUser(User user) {
        try {
            User userFind = findUserByEmail(user.getEmail());
            if (userFind != null) {
                return new Response(false, "Tài khoản đã tồn tại!", userFind);
            }
            String sql =
                    "INSERT INTO `users` ( `email`, `first_name`, `last_name`, `password`,`status`, `is_online`, `created_at`, `updated_at`) " +
                            "VALUES ( ?, ?, ?,?,?, 0, current_timestamp(), current_timestamp());";

            PreparedStatement pre = this.conn.prepareStatement(sql);

            pre.setString(1, user.getEmail());
            pre.setString(2, user.getFirstName());
            pre.setString(3, user.getLastName());
            pre.setString(4, user.getPassword());
            pre.setString(5, "UNLOCK");
            boolean result = pre.executeUpdate() > 0;

            if (result) {
                return new Response(true, "Tạo tài khoản thành công!", user);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi đăng ký tài khoản");
            e.printStackTrace();
        }

        return new Response(false, "Không tạo được tài khoản", null);
    }

    public User findUserByEmail(String email) {
        try {
            String sql = "SELECT * FROM `users` WHERE `email`=?";
            PreparedStatement pre = this.conn.prepareStatement(sql);
            pre.setString(1, email);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setPassword(rs.getString("password"));
                user.setOnline(rs.getBoolean("is_online"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
                user.setStorage(rs.getFloat("storage"));
                user.setStatus(rs.getString("status"));
                user.setStorage(rs.getFloat("storage"));
                return user;
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tìm kiếm user");
            e.printStackTrace();
        }
        return null;
    }

    public User findUserByID(String id) {
        try {
            String sql = "SELECT * FROM `users` WHERE `id`=?";
            PreparedStatement pre = this.conn.prepareStatement(sql);
            pre.setString(1, id);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setPassword(rs.getString("password"));
                user.setOnline(rs.getBoolean("is_online"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
                user.setStorage(rs.getFloat("storage"));
                return user;
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tìm kiếm user");
            e.printStackTrace();
        }
        return null;
    }

    public void changeStatusOnline(User user, int status) {
        try {
            String sql = "UPDATE `users` SET is_online = ? WHERE email = ?";
            PreparedStatement pre = this.conn.prepareStatement(sql);
            pre.setInt(1, status);
            pre.setString(2, user.getEmail());
            pre.executeUpdate();
        } catch (Exception e) {
            System.out.println("Lỗi changeStatusOnline");
            e.printStackTrace();
        }
    }

    public Response loginUser(String email, String password) {
        try {
            User user = findUserByEmail(email);
            // kiểm tra tài khoản block
            if (user.getStatus().equals("BLOCK")) {
                return new Response(false, "Tài khoản đã bị khoá, vui lòng liên hệ admin", null);
            }
            // kiểm tra tài khoản đang online
            if (user != null && user.isOnline()) {
                return new Response(false, "Bạn đang đăng nhập ở máy khác", null);
            }
            // kiểm tra tài khoản không tồn tại
            if (user == null) {
                return new Response(false, "Tài khoản không tồn tại", null);
            } else if (user.getPassword().equals(password) && user.getEmail().equals(email)) {
                // đăng nhập thành công
                changeStatusOnline(user, 1); // chuyển trạng thái user thành đang online
                return new Response(true, "Đăng nhập thành công", user);
                // trườmg hợp sai tài khoản
            } else if (!user.getPassword().equals(password) || !user.getEmail().equals(email)) {
                return new Response(false, "Sai tài khoản hoặc mật khẩu", null);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi login user " + email + " " + password);
        }
        return new Response(false, "Không login được", null);
    }

    public Response changeStatusUser(String email, String status) {
        try {
            User user = findUserByEmail(email);
            if (user == null) {
                return new Response(false, "Địa chỉ email này không tồn tại trên server", null);
            }
            String sql = "UPDATE `users` SET status = ? WHERE email = ?";
            PreparedStatement pre = this.conn.prepareStatement(sql);
            pre.setString(1, status);
            pre.setString(2, email);
            int i = pre.executeUpdate();
            if (i > 0) {
                return new Response(true, "Đã " + status + " " + email + " thành công", null);
            }
        } catch (Exception e) {
            return new Response(false, "Đã có lỗi xảy ra", null);
        }
        return new Response(false, "Đã có lỗi xảy ra", null);
    }

    public Response editStorage(float storage) {
        try {
            String sql = "ALTER TABLE `users` CHANGE `storage` `storage` FLOAT NOT NULL DEFAULT ?";
            PreparedStatement pre = this.conn.prepareStatement(sql);
            pre.setFloat(1, storage);
            pre.executeUpdate();
            return new Response(true, "Đã thay đổi dung lượng mặc định thành công, dung lượng mặc định hiện tại là : " + storage, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response(false, "Đã có lỗi xảy ra khi thay đổi dung lượng", null);
    }

    public Response editStorageUser(String email, float storageUser) {
        try {
            String sql = "UPDATE `users` SET `storage`= ? WHERE email = ?";
            PreparedStatement pre = this.conn.prepareStatement(sql);
            pre.setFloat(1, storageUser);
            pre.setString(2, email);
            int i = pre.executeUpdate();
            if (i > 0) {
                return new Response(true, "Đã thay đổi dung lượng của " + email + ", dung lượng mặc định hiện tại là : " + storageUser, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // update storage user sender when send mail if it has file
    public boolean updateStorageUser(String email, float user_storage, float file_size) {
        try {
            User user = findUserByEmail(email);
            String sql = "UPDATE `users` SET storage = ? where email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            float new_size = user_storage - file_size;
            ps.setFloat(1, new_size);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(" Lỗi ở hàm : " + getClass());
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<User> getAllUsers() {
        try {
            ArrayList<User> users = new ArrayList<User>();
            String sql = "SELECT * FROM users";
            PreparedStatement pre = conn.prepareStatement(sql);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setPassword(rs.getString("password"));
                user.setOnline(rs.getBoolean("is_online"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
                user.setStorage(rs.getFloat("storage"));
                users.add(user);
            }
            return users;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void setOffline() {
        try {
            String sql = "UPDATE `users` SET is_online = 0 ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            System.out.println("Đóng server thành công");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
