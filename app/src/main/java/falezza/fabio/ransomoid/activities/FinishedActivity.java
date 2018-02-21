package falezza.fabio.ransomoid.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import falezza.fabio.ransomoid.AppState;
import falezza.fabio.ransomoid.R;
import falezza.fabio.ransomoid.utils.AppDelegate;

public class FinishedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);

        String state = AppDelegate.getInstance(this).getByTag(AppDelegate.appState);
        if (!state.equals(AppState.FINISHED.toString())) {
            AppDelegate.getInstance(this).saveAppState(AppState.FINISHED);
        }
    }
}
