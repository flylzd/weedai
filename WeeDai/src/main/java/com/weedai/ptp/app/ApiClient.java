package com.weedai.ptp.app;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.weedai.ptp.constant.Urls;
import com.weedai.ptp.model.Article;
import com.weedai.ptp.model.ArticleDetail;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.model.Invest;
import com.weedai.ptp.model.User;
import com.weedai.ptp.model.Valicode;
import com.weedai.ptp.utils.AppUtil;
import com.weedai.ptp.utils.Logger;
import com.weedai.ptp.volley.GsonGetRequest;
import com.weedai.ptp.volley.GsonPostRequest;
import com.weedai.ptp.volley.ResponseListener;
import com.weedai.ptp.volley.VolleySingleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


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
//    public static void verifyInterface(String tag, ResponseListener listener) {
//
//        listener.onStarted();
//
//        Map<String, String> requestParams = getSignatureMap();
//
//        String url = Urls.ACTION_INDEX;
//        GsonPostRequest request = createGsonPostRequest(url, requestParams, BaseModel.class, listener);
//        request.setTag(tag);
//        requestQueue.add(request);
//    }

    /**
     * 验证码获取
     *
     * @param tag
     * @param listener
     */
    public static void getImgcode(String tag, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("t", String.valueOf((int) Math.random() * 10 + 1));
        requestParams.put("q", "imgcode");

        String url = Urls.ACTION_INDEX_PLUGINS;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, Valicode.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 登陆
     */
    public static void login(String tag, String username, String passwrod, String valicode, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("keywords", username);
        requestParams.put("password", passwrod);
        requestParams.put("valicode", valicode);
        requestParams.put("q", "action/login");
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, Invest.class, listener);
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
    public static void getInvestList(String tag, int page, int type, String timelimit, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("page", String.valueOf(page));
        requestParams.put("type", String.valueOf(type));
        requestParams.put("timelimit", timelimit);
        requestParams.put(Urls.ACTION, "invest/list");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, Invest.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    public static void getInvestList(String tag, int page, int type, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("page", String.valueOf(page));
//        requestParams.put("type", String.valueOf(type));
        requestParams.put("timelimit", null);
        requestParams.put(Urls.ACTION, "invest/list");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, Invest.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 投资详情
     */
    public static void getFinancialDetail(String tag, String id, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("bid", id);
        requestParams.put(Urls.ACTION, "invest/content");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, Invest.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }


    /**
     * 获取英雄榜信息
     *
     * @param listener
     */
    public static void getHeroList(String tag, int dact, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("dact", String.valueOf(1));
        requestParams.put(Urls.ACTION, "heroact/list");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, Article.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }


    /**
     * 文章列表
     *
     * @param tag
     * @param page
     * @param type     文章类型
     * @param listener
     */
    public static void getArticleList(String tag, int page, int type, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("site_id", String.valueOf(type));
        requestParams.put("status", "1");
        requestParams.put("page", String.valueOf(page));
//        requestParams.put("limit", String.valueOf(PAGE_LIMIT));
        requestParams.put(Urls.ACTION, "article/list");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, Article.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 文章详情
     *
     * @param tag
     * @param aid
     * @param listener
     */
    public static void getArticleDetail(String tag, String aid, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("aid", aid);
        requestParams.put(Urls.ACTION, "article/content");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, ArticleDetail.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }


    /**
     * 用户中心
     */
    public static void getUser(String tag, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, User.class, listener);
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
        Log.d(TAG, "request Url-Params = " + request.getUrl() + "?" + params);
//        Log.d(TAG, "request Url-Params = " + request.getUrl());

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
