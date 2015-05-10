package com.weedai.ptp.volley;


import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.ParseError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.weedai.ptp.constant.Config;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SimpleMultipartRequest<T> extends Request<T> {

    public static final String TAG = "SimpleMultipartRequest";

    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Response.Listener<T> listener;

    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", HTTP.UTF_8);

    MultipartEntity mMultiPartEntity = new MultipartEntity();

    public SimpleMultipartRequest(String url,Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.listener = listener;
        this.clazz = clazz;
    }

    public MultipartEntity getMultiPartEntity() {
        return mMultiPartEntity;
    }

    @Override
    public String getBodyContentType() {
        return mMultiPartEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // 将mMultiPartEntity中的参数写入到bos中
            mMultiPartEntity.writeTo(bos);
        } catch (IOException e) {
            Log.e("", "IOException writing to ByteArrayOutputStream");
        }
        System.out.println("bos " + String.valueOf(bos.toString()));
        return bos.toByteArray();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        Map<String, String> map = new HashMap<String, String>();
        if (Config.cookie != null) {
            map.put("Cookie", Config.cookie);
            return map;
        }
        map.put("Cookie", Config.cookie);
        return super.getHeaders();
    }


    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        response.headers.put(HTTP.CONTENT_TYPE, PROTOCOL_CONTENT_TYPE);
        Log.d(TAG, response.headers.toString());
        Log.d(TAG, "data" + new String(response.data));
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
}
