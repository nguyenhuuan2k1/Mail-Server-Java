package nhom50.HELPERS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileHandle {
    public static byte[] convertFileToByte(String filestr) {
        try {
            File file = new File(filestr);
            return Files.readAllBytes(file.toPath());
        } catch (Exception e) {
          System.out.println("Đã có lỗi xảy ra khi đọc file");
        }
        return null;
    }

    public static void WriteFile(String filename, byte[] data) {
        FileOutputStream fout = null;
        try {
            File file = new File(filename);
            fout = new FileOutputStream(file);

            fout.write(data);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileHandle.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileHandle.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fout.close();
            } catch (IOException ex) {
                Logger.getLogger(FileHandle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
