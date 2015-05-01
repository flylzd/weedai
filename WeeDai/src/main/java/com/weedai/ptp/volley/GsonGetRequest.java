package com.weedai.ptp.volley;


import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.GsonRequest;
import com.weedai.ptp.constant.Config;

import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class GsonGetRequest<T> extends GsonRequest<T> {

    private static final String TAG = "GsonGetRequest";

    //    private static final String DEFAULT_PARAMS_ENCODING = "GBK";
    private static final String DEFAULT_PARAMS_ENCODING = HTTP.ISO_8859_1;

    /**
     * Content type for request.
     */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", HTTP.UTF_8);

    public GsonGetRequest(String url, Class<T> clazz, Map<String, String> params, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, clazz, null, params, listener, errorListener);
    }

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
        response.headers.put(HTTP.CONTENT_TYPE, PROTOCOL_CONTENT_TYPE);
        Log.d(TAG, new String(response.data));
        return super.parseNetworkResponse(response);
    }

    protected String getParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }

    public String getEncodedUrlParams() throws AuthFailureError {

        StringBuilder encodedParams = new StringBuilder();
        String paramsEncoding = getParamsEncoding();
        Map<String, String> params = getParams();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (null == entry.getValue()) {
                continue;
            }
//                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
//            encodedParams.append(URLEncoder.encode(entry.getKey()));
            encodedParams.append(entry.getKey());
            encodedParams.append('=');
//            encodedParams.append(URLEncoder.encode(entry.getValue()));
            encodedParams.append(entry.getValue());
            encodedParams.append('&');
        }
        return encodedParams.toString();
//        try {
//            for (Map.Entry<String, String> entry : params.entrySet()) {
//                if (null == entry.getValue()) {
//                    continue;
//                }
//                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
//                encodedParams.append('=');
//                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
//                encodedParams.append('&');
//            }
//            Log.d(TAG, encodedParams.toString());
//            return encodedParams.toString();
//        } catch (UnsupportedEncodingException uee) {
//            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
//        }
    }

}
