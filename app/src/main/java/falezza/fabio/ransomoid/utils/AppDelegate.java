package falezza.fabio.ransomoid.utils;

import android.content.Context;
import android.content.SharedPreferences;

import falezza.fabio.ransomoid.AppState;

/**
 * Created by fabio on 16/02/18.
 */

public class AppDelegate {

    private static AppDelegate instance;
    private SharedPreferences storage;

    public static final String userID = "UserID";
    public static final String appState = "AppState";

    private AppDelegate(Context context) {
        this.storage = context.getSharedPreferences("preferences", 0);
    }

    public static AppDelegate getInstance(Context context) {
        if (instance == null) {
            instance = new AppDelegate(context);
        }
        return instance;
    }

    public void saveUserID(String id) {
        SharedPreferences.Editor editor = this.storage.edit();
        editor.putString(userID, id);
        editor.apply();
    }

    public void saveAppState(AppState state) {
        SharedPreferences.Editor editor = this.storage.edit();
        editor.putString(appState, state.toString());
        editor.apply();
    }

    public String getByTag(String tag) {
        return storage.getString(tag, "");
    }
}
