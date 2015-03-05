package com.weedai.p2p.volley;


import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.GsonRequest;

import org.apache.http.protocol.HTTP;

import java.util.HashMap;
import java.util.Map;

public class GsonPostRequest<T> extends GsonRequest<T> {


    public static final String TAG = "GsonPostRequest";

    /** Charset for request. */
    private static final String PROTOCOL_CHARSET = HTTP.UTF_8;

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", HTTP.UTF_8);

    private static String mCookie;
    private Listener<T> mListener;


    public GsonPostRequest(String url, Class<T> clazz, Map<String, String> params, Listener<T> listener, ErrorListener errorListener) {
        this(url, clazz, null, params, listener, errorListener);
    }


    public GsonPostRequest(String url, Class<T> clazz, Map<String, String> headers, Map<String, String> params, Listener<T> listener, ErrorListener errorListener) {
        super(Method.POST, url, clazz, headers, params, listener, errorListener);
        this.mListener = listener;
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        if (mCookie != null) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("Cookie", mCookie);
            return map;
        }
        return super.getHeaders();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        if (mCookie == null) {
            for (String s : response.headers.keySet()) {
                if (s.contains("Set-Cookie")) {
                    mCookie = response.headers.get(s);
                    break;
                }
            }
        }
        response.headers.put(HTTP.CONTENT_TYPE, PROTOCOL_CONTENT_TYPE);
        Log.d(TAG, response.headers.toString());
        Log.d(TAG, new String(response.data));
        return super.parseNetworkResponse(response);
    }


    @Override
    protected void deliverResponse(T response) {
        if (null != mListener) {
            mListener.onResponse(response);
        }
    }


}
