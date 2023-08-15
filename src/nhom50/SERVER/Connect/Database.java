package nhom50.SERVER.Connect;

import com.mysql.jdbc.DatabaseMetaData;
import com.mysql.jdbc.Driver;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class Database {

    private static Database instance = null;
    private Connection conn = null;
    private String severName = "";
    private String dbName = "";
    private String userName = "";
    private String password = "";
    private boolean autoMigrate = false;

    private Database() {
        try {
            // Đầu tiên read config database trước
            readConfig();

            // Dùng value config để thực hiện connect
            String strConnect = "jdbc:mysql://" + severName + "/" + dbName + "?useUnicode=true&characterEncoding=utf-8";
            Properties pro = new Properties();
            pro.put("user", userName);
            pro.put("password", password);

//            System.out.print("Thực hiện kết nối database: ");
//            System.out.println(severName + " | " + dbName + " | " + userName + " | " + password + " | ");
            // Bắt đầu kết nối
            com.mysql.jdbc.Driver driver = new Driver();
            this.conn = driver.connect(strConnect, pro);

            // Nếu connect thành công thì thực hiện migrate tạo table
            if (this.autoMigrate) {
                System.out.println("Kết nối thành công!");
                migrate();
            }
        } catch (SQLException ex) {
            System.err.println("Không kết nối được tới CSDL! " + ex.getMessage());
            System.exit(0);
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        return conn;
    }

    // Xử lý đọc file để lấy ra 4 tham số
    private void readConfig() {
        try {
            FileInputStream fis = new FileInputStream(".env");
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
                    case "DB_HOST":
                        this.severName = value;
                        break;
                    case "DB_NAME":
                        this.dbName = value;
                        break;
                    case "DB_USER":
                        this.userName = value;
                        break;
                    case "DB_PASSWORD":
                        this.password = value;
                        break;
                    case "MIGRATE":
                        this.autoMigrate = value.equalsIgnoreCase("true");
                        break;
                }
            }
        } catch (Exception ex) {
        }
    }

    private void migrate() {
        System.out.println("Thực hiện migrate tạo các bảng...");
        if (this.conn != null) {
            try {
                // Tạo statement
                Statement stmt = this.conn.createStatement();

                // Kiểm tra tồn tại bảng `users`
                if (!isTableExists("users")) {
                    // Tạo bảng `users`
                    System.out.println("Đang tạo bảng `users`...");
                    String sqlCreateTblUsers =
                            "CREATE TABLE `users` (" +
                                    "  `id` varchar(36) NOT NULL PRIMARY KEY," +
                                    "  `email` varchar(255) NOT NULL," +
                                    "  `first_name` varchar(50) NOT NULL," +
                                    "  `last_name` varchar(50) NOT NULL," +
                                    "  `password` varchar(32) NOT NULL," +
                                    "  `is_online` TINYINT(1) NOT NULL," +
                                    "  `created_at` timestamp NOT NULL DEFAULT current_timestamp()," +
                                    "  `updated_at` timestamp NULL DEFAULT NULL" +
                                    ")ENGINE=INNODB;";
                    stmt.execute(sqlCreateTblUsers);
                    System.out.println("Tạo bảng `users` thành công!");
                } else {
                    System.out.println("Bảng `users` đã có sẵn!");
                }

                // Kiểm tra tồn tại bảng `emails`
                if (!isTableExists("emails")) {
                    // Tạo bảng `emails`
                    System.out.println("Đang tạo bảng `emails`...");
                    String sqlCreateTblEmails =
                            "CREATE TABLE `emails` (" +
                                    "  `id` varchar(36) NOT NULL PRIMARY KEY," +
                                    "  `sender_id` varchar(36) NOT NULL," +
                                    "  `receiver_id` varchar(36) NOT NULL," +
                                    "  `subject` varchar(255) NOT NULL," +
                                    "  `CC` varchar(255) NULL," +
                                    "  `BCC` varchar(255) NULL," +
                                    "  `content` text NULL," +
                                    "  `status` varchar(10) NOT NULL," +
                                    "  `created_at` timestamp NOT NULL DEFAULT current_timestamp()," +
                                    "  `deleted_at` timestamp NULL DEFAULT NULL" +
                                    ")ENGINE=INNODB;";
                    stmt.execute(sqlCreateTblEmails);
                    System.out.println("Tạo bảng `emails` thành công!");
                } else {
                    System.out.println("Bảng `emails` đã có sẵn!");
                }

                // Kiểm tra tồn tại bảng `attachments`
                if (!isTableExists("attachments")) {
                    // Tạo bảng `attachments`
                    System.out.println("Đang tạo bảng `attachments`...");
                    String sqlCreateTblAttachments =
                            "CREATE TABLE `attachments` (" +
                                    "`id` VARCHAR(36) NOT NULL PRIMARY KEY, " +
                                    "`email_id` VARCHAR(36) NOT NULL, " +
                                    "`file_path` VARCHAR(255) NOT NULL, " +
                                    "`file_name` VARCHAR(255) NOT NULL, " +
                                    "`file_type` VARCHAR(50) NOT NULL, " +
                                    "`created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                                    ");";
                    stmt.execute(sqlCreateTblAttachments);
                    System.out.println("Tạo bảng `attachments` thành công!");
                } else {
                    System.out.println("Bảng `attachments` đã có sẵn!");
                }

                System.out.println("Đã migrate thành công các bảng!");
            } catch (Exception ex) {
                System.out.println("Lỗi khi migrate... " + ex.getMessage());
                System.exit(0);
            }
        } else {
            System.out.println("Không kết nối được tới database!");
            System.exit(0);
        }
    }

    private boolean isTableExists(String tableName) {
        try {
            DatabaseMetaData dbm = (DatabaseMetaData) this.conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName, null);
            return tables.next();
        } catch (Exception ex) {
        }
        return false;
    }

    public void closeConnect() {
        try {
            if (this.conn != null) {
                this.conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đóng kết nối CSDL! " + e.getMessage());
        }
    }

}