package falezza.fabio.ransomoid.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import falezza.fabio.ransomoid.R;
import falezza.fabio.ransomoid.services.DecryptService;

public class DecryptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        Intent decryptService = new Intent(this, DecryptService.class);
        //decryptService.putExtra("Key", key);
        decryptService.setData(Uri.parse(key));
        startService(decryptService);
    }

}
