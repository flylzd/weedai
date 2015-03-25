package com.weedai.ptp.app;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.weedai.ptp.constant.Urls;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.utils.Logger;
import com.weedai.ptp.volley.GsonPostRequest;
import com.weedai.ptp.volley.ResponseListener;
import com.weedai.ptp.volley.VolleySingleton;

import java.util.HashMap;
import java.util.Map;


public class ApiClient {

    private static Logger logger = Logger.getLogger(Logger.class);
    public static final String TAG = "ApiClient";

    public static final RequestQueue requestQueue = VolleySingleton.getRequestQueue();

    private static final String TIMESTAMP =  "timestamp";
    private static final String SIGNATURE =  "signature";

    /**
     * 接口验证
     */
    public static void verifyInterface(String tag, String timestamp, String signature, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = new HashMap<String,String>();
        requestParams.put(TIMESTAMP,timestamp);
        requestParams.put(SIGNATURE,signature);

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, BaseModel.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    private static <T> GsonPostRequest createGsonPostRequest(String url, Map<String, String> requestParamsMap, Class<T> clazz, final ResponseListener listener) {

        GsonPostRequest request = new GsonPostRequest(url, clazz, requestParamsMap, new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                Log.d(TAG, "onResponse = " + response.toString());
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "volleyError = " + volleyError.toString());
                listener.onErrorResponse(volleyError);
            }
        });

        try {
            Log.d(TAG, "request Headers = " + request.getHeaders().toString());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        String params = requestParamsMap.toString().substring(1, requestParamsMap.toString().length() - 1);
        Log.d(TAG, "request Url-Params = " + request.getUrl() + "?" + params);

        return request;
    }


}
