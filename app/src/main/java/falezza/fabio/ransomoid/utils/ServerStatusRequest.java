package falezza.fabio.ransomoid.utils;

import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fabio on 16/02/18.
 */
public class ServerStatusRequest extends Request {

    private final Response.Listener mListener;
    private String mBody;
    private String mContentType;
    private HashMap mCustomHeaders;

    public ServerStatusRequest(int method,
                               String url,
                               HashMap customHeaders,
                               String body,
                               Response.Listener listener,
                               Response.ErrorListener errorListener) {

        super(method, url, errorListener);
        mCustomHeaders = customHeaders;
        mBody = body;
        mListener = listener;
        mContentType = "application/json";

        if (method == Request.Method.POST) {
            RetryPolicy policy = new DefaultRetryPolicy(5000, 0, 5);
            setRetryPolicy(policy);
        }
    }

    public ServerStatusRequest(String url,
                                     HashMap customHeaders,
                                     Response.Listener listener,
                                     Response.ErrorListener errorListener) {

        super(Method.GET, url, errorListener);
        mCustomHeaders = customHeaders;
        mListener = listener;
        mContentType = "application/x-www-form-urlencoded";
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.statusCode, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(Object response) {
        mListener.onResponse(response);
    }

    @Override
    public Map getHeaders() throws AuthFailureError {
        if (mCustomHeaders != null) {
            return mCustomHeaders;
        }
        return super.getHeaders();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return mBody.getBytes();
    }

    @Override
    public String getBodyContentType() {
        return mContentType;
    }

    public String getContentType() {
        return mContentType;
    }

    public void setContentType(String mContentType) {
        this.mContentType = mContentType;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return 0;
    }
}
