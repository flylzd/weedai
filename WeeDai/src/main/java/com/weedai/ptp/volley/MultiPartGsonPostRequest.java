package com.weedai.ptp.volley;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.ParseError;
import com.android.volley.request.MultiPartRequest;
import com.android.volley.toolbox.HttpHeaderParser;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.weedai.ptp.constant.Config;

import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MultiPartGsonPostRequest<T> extends MultiPartRequest<T> {

    public static final String TAG = "MultiPartGsonPostRequest";

    private final Gson gson = new Gson();
    private final Class<T> clazz;
    //    private final Map<String, String> headers;
//    private final Map<String, String> params;params
    private final Listener<T> listener;

    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", HTTP.UTF_8);

    public MultiPartGsonPostRequest(int method, String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        super(method, url, listener, errorListener);

        this.clazz = clazz;
//        this.params = null;
        this.listener = listener;
    }


    public MultiPartGsonPostRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);

        this.clazz = clazz;
        this.listener = listener;
    }

//    @Override
//    protected Map<String, String> getParams() throws AuthFailureError {
//        return params != null ? params : super.getParams();
//    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        if (Config.cookie != null) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("Cookie", Config.cookie);
            return map;
        }
        return super.getHeaders();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        if (Config.cookie == null) {
            for (String s : response.headers.keySet()) {
                if (s.contains("Set-Cookie")) {
                    Config.cookie = response.headers.get(s);
                    break;
                }
            }
        }
        response.headers.put(HTTP.CONTENT_TYPE, PROTOCOL_CONTENT_TYPE);
        Log.d(TAG, response.headers.toString());
        Log.d(TAG, new String(response.data));
        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        if (null != listener) {
            listener.onResponse(response);
        }
    }
}
