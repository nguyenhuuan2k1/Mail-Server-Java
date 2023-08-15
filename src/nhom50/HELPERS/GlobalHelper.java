package nhom50.HELPERS;

import java.io.ObjectOutputStream;

public class GlobalHelper {
    public static void sendMessage(ObjectOutputStream out, String message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
