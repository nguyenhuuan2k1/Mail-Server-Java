package nhom50.SERVER.IOFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileFunc {
    public static byte[] convertFileToByte(String filestr) throws IOException {

        File file = new File(filestr);
        /*FileInputStream fileInput = new FileInputStream(file.getAbsolutePath());

        String filename = file.getName();
        byte[] fileByte = filename.getBytes();
        byte[] fileContent = new byte[(int)file.length()];

        return fileContent;*/
        return Files.readAllBytes(file.toPath());
    }

    public static void WriteFile(String filename, byte[] data) {
        FileOutputStream fout = null;
        try {
            File file = new File(filename);
            fout = new FileOutputStream(file);

            fout.write(data);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileFunc.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileFunc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fout.close();
            } catch (IOException ex) {
                Logger.getLogger(FileFunc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static HashMap<String, String> getServerKey(String path) {
        try {
            HashMap<String, String> keyServer = new HashMap<String, String>();
            System.out.println("Loading server Keys");
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String a = br.readLine();
            while (a != null) {
                String[] tempArrray = a.split("=");
                keyServer.put(tempArrray[0], tempArrray[1]);
                a = br.readLine();
            }
            System.out.println("Loading server Keys success");
            return keyServer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized boolean ghiFile(ArrayList<String> map, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter os = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(os);
            for (String item : map) {
                String line = item;
                bw.write(line);
                bw.newLine();
            }
            bw.close();
            os.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static synchronized ArrayList<String> docfile(String path) {
        try {
            System.out.println("Loading file");
            ArrayList<String> map = new ArrayList<>();
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String a = br.readLine();
            while (a != null) {
                map.add(a);
                a = br.readLine();
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}