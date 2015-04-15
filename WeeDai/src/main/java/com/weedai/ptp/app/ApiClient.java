package com.weedai.ptp.app;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.weedai.ptp.constant.Urls;
import com.weedai.ptp.model.Article;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.utils.AppUtil;
import com.weedai.ptp.utils.Logger;
import com.weedai.ptp.volley.GsonGetRequest;
import com.weedai.ptp.volley.GsonPostRequest;
import com.weedai.ptp.volley.ResponseListener;
import com.weedai.ptp.volley.VolleySingleton;

import java.util.HashMap;
import java.util.Map;


public class ApiClient {

    private static Logger logger = Logger.getLogger(Logger.class);
    public static final String TAG = "ApiClient";

    public static final RequestQueue requestQueue = VolleySingleton.getRequestQueue();

    private static final String TIMESTAMP = "timestamp";
    private static final String SIGNATURE = "signature";

    private static final int PAGE_LIMIT = 10;

    /**
     * 接口验证
     */
    public static void verifyInterface(String tag, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, BaseModel.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 投资信息获取
     *
     * @param tag
     * @param page
     * @param type
     * @param timelimit
     * @param listener
     */
    public static void getInvestList(String tag, int page, int type, int timelimit, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("page", String.valueOf(page));
        requestParams.put("type", String.valueOf(type));
        requestParams.put("timelimit", String.valueOf(timelimit));
        requestParams.put(Urls.ACTION, "invest/list");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, BaseModel.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }


    public static void getArticleList(String tag,int page,  ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("site_id", "59");
        requestParams.put("status", "1");
        requestParams.put("page", String.valueOf(page));
        requestParams.put("limit", String.valueOf(PAGE_LIMIT));
        requestParams.put(Urls.ACTION, "article/list");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, Article.class, listener);
        request.setTag(tag);
        requestQueue.add(request);

    }

    private static Map<String, String> getSignatureMap() {

        long time = System.currentTimeMillis();
        String timestamp = String.valueOf(time);
        String signature = AppUtil.getSignature(timestamp);

        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put(TIMESTAMP, timestamp);
        requestParams.put(SIGNATURE, signature);

        return requestParams;
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
//        Log.d(TAG, "request Url-Params = " + request.getUrl() + "?" + params);
        Log.d(TAG, "request Url-Params = " + request.getUrl());

        return request;
    }



    private static <T> GsonGetRequest createGsonGetRequest(String url, Map<String, String> requestParamsMap, Class<T> clazz, final ResponseListener listener) {

        GsonGetRequest request = new GsonGetRequest(url, clazz, requestParamsMap, new Response.Listener<T>() {
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
            String params = requestParamsMap.toString().substring(1, requestParamsMap.toString().length() - 1);
            //        Log.d(TAG, "request Url-Params = " + request.getUrl() + "?" + params);
            Log.d(TAG, "request Url-Params = " + request.getUrl());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        return request;
    }

}
