package nhom50.SERVER.MyHandle;

import nhom50.DTO.Email;
import nhom50.DTO.Response;
import nhom50.DTO.User;
import nhom50.SERVER.IOFile.FileFunc;
import nhom50.SERVER.ServerMain;
import nhom50.SERVER.Service.EmailServices;
import nhom50.SERVER.Service.UserServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class HandleServer extends Thread {
    Scanner sc = new Scanner(System.in);
    public static ArrayList<String> log_server = null;

    @Override
    public void run() {
        while (true) {
            doCommand();
        }
    }

    public void menu() {
        System.out.println("============================================");
        System.out.println("1. Lock User");
        System.out.println("2. Read Log Server");
        System.out.println("3. Edit Storage User");
        System.out.println("4. Send email notification");
        System.out.println("5. Close server");
    }

    public void lockMenu() {
        boolean subMenu1 = true;
        while (subMenu1) {
            System.out.println("============================================");
            System.out.println("1. Lock User");
            System.out.println("2. Unlock User");
            System.out.println("3. Exit");
            Response response = null;
            String command = sc.nextLine();
            int case_ = Integer.parseInt(command);
            switch (case_) {
                case 1:
                    System.out.println("Nhập email user cần khoá : ");
                    String emailblock = sc.nextLine();
                    response = UserServices.gI().changeStatusUser(emailblock, "BLOCK");
                    System.out.println(response.getMessage());
                    if (response.isSuccess()) {
                        for (HandleClient client : ServerMain.listUser) {
                            if (client.getCurrentUser().getEmail().equals(emailblock.trim())) {
                                //client.doCloseSocket(); // kich socket
                                try {
                                    client.getOut().close();
                                    client.getSocket().close();
                                } catch (Exception e) {

                                }
                            }
                        }
                    }

                    break;
                case 2:
                    System.out.println("Nhập email user cần mở khoá : ");
                    String emailunlock = sc.nextLine();
                    response = UserServices.gI().changeStatusUser(emailunlock, "UNLOCK");
                    System.out.println(response.getMessage());
                    break;
                case 3:
                    subMenu1 = false;
                default:
                    System.out.println("Không có lựa chọn này");
                    break;
            }
        }

    }

    public void doCommand() {
        while (true) {
            try {
                menu();
                log_server = FileFunc.docfile("log.txt");
                System.out.println("Nhập lệnh cần thực hiện :");
                String command = sc.nextLine();
                int case_ = Integer.parseInt(command);
                switch (case_) {
                    case 1:
                        // menu lock user
                        lockMenu();
                        break;
                    case 2:
                        // coi lock đăng nhập, đăng xuất
                        showLog();
                        break;
                    case 3:
                        storageMenu();
                        break;
                    case 4:
                        // chỉ gửi tiêu đề và nội dung, không đính kèm file
                        sendMailtoAllUsers();
                        break;
                    case 5:
                        try {
                            if (ServerMain.server != null) {
                                ServerMain.server.close();
                            }
                            System.exit(0);
                        } catch (IOException e) {

                        }
                        break;
                    default:
                        System.out.println("Không có lựa chọn này");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại");
            }

        }
    }

    private void sendMailtoAllUsers() {
        try {
            System.out.println("Nhập tiêu đề :");
            String title = sc.nextLine();
            System.out.println("Nhập nội dung cần gửi :");
            String content = sc.nextLine();
            System.out.println("Bạn có muốn gửi không (Y/N)");
            String choose = sc.nextLine().trim();
            if (choose.equalsIgnoreCase("y")) {
                // lấy danh sách user từ database về
                ArrayList<User> allUsers = UserServices.gI().getAllUsers();
                for (User user : allUsers) {
                    // gửi email cho từng user
                    // email mặc định của admin là admin@nhom50.com
                    // name mặc định của admin là ADMIN
                    Email email = new Email(UUID.randomUUID().toString(), "admin@nhom50.com", "ADMIN", user.getEmail(), title, "", "", content, "SENT");
                    // gửi email và in kết quả trả về
                    Response response = EmailServices.getInstance().sendMail(email);
                    System.out.println("Gửi mail tới + " + email.getReceiverEmail() + response.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void storageMenu() {
        boolean subMenu3 = true;
        while (subMenu3) {
            try {
                System.out.println("============================================");
//                System.out.println("1. Chỉnh sửa dung lượng mặc đinh");
                System.out.println("1. Chỉnh sủa dung lượng của user");
                System.out.println("2. Exit");
                Response response = null;
                String command = sc.nextLine();
                int case_ = Integer.parseInt(command);
                switch (case_) {
                    case 1:
//                        System.out.println("Chỉnh sửa dung lượng mặc đinh  : dung lượng >= 0");
//                        float storage = Float.parseFloat(sc.nextLine());
//                        response = UserServices.gI().editStorage(storage);
//                        System.out.println(response.getMessage());
//                        break;
                        System.out.println("Nhập email user cần tăng dung lượng : ");
                        String email = sc.nextLine();
                        System.out.println("Nhập dung lượng cần tăng : dung lượng >= 0 ");
                        float storageUser = Float.parseFloat(sc.nextLine());
                        response = UserServices.gI().editStorageUser(email, storageUser);
                        System.out.println(response.getMessage());
                        break;
//                    case 2:
//                        System.out.println("Nhập email user cần tăng dung lượng : ");
//                        String email = sc.nextLine();
//                        System.out.println("Nhập dung lượng cần tăng : dung lượng >= 0 ");
//                        float storageUser = Float.parseFloat(sc.nextLine());
//                        response = UserServices.gI().editStorageUser(email, storageUser);
//                        System.out.println(response.getMessage());
//                        break;
                    case 2:
                        subMenu3 = false;
                    default:
                        System.out.println("Không có lựa chọn này");
                        break;
                }
            } catch (Exception e) {
                System.out.println(getClass() + ": " + e.getMessage());
                System.out.println("Dữ liệu nhập vào không hợp lệ, vui lòng nhập lại");
            }
        }
    }


    private void showLog() {
        for (String line : log_server) {
            System.out.println(line);
        }
    }
}
