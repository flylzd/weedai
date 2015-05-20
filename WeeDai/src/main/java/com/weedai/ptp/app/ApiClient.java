package com.weedai.ptp.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.error.VolleyErrorHelper;
import com.weedai.ptp.constant.Urls;
import com.weedai.ptp.model.About;
import com.weedai.ptp.model.Article;
import com.weedai.ptp.model.ArticleDetail;
import com.weedai.ptp.model.ArticleRelated;
import com.weedai.ptp.model.Award;
import com.weedai.ptp.model.Bank;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.model.Calculator;
import com.weedai.ptp.model.Comment;
import com.weedai.ptp.model.FinancialManager;
import com.weedai.ptp.model.Invest;
import com.weedai.ptp.model.Micro;
import com.weedai.ptp.model.Money;
import com.weedai.ptp.model.MyWeallth;
import com.weedai.ptp.model.ReceivableSearch;
import com.weedai.ptp.model.RotationImage;
import com.weedai.ptp.model.SecurityLevel;
import com.weedai.ptp.model.SecurityPhone;
import com.weedai.ptp.model.SignIn;
import com.weedai.ptp.model.StandInsideLetter;
import com.weedai.ptp.model.User;
import com.weedai.ptp.model.Valicode;
import com.weedai.ptp.model.WithdrawalRecord;
import com.weedai.ptp.utils.AppUtil;
import com.weedai.ptp.utils.Logger;
import com.weedai.ptp.volley.GsonGetRequest;
import com.weedai.ptp.volley.GsonPostRequest;
import com.weedai.ptp.volley.MultiPartGsonPostRequest;
import com.weedai.ptp.volley.MultipartEntity;
import com.weedai.ptp.volley.ResponseListener;
import com.weedai.ptp.volley.SimpleMultipartRequest;
import com.weedai.ptp.volley.VolleySingleton;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
        GsonPostRequest request = createGsonPostRequest(url, requestParams, BaseModel.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 安全退出
     */
    public static void logout(String tag, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("q", "action/logout");
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, BaseModel.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 验证用户名是否存在
     */
    public static void checkUsername(String tag, String username, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("username", username);
        requestParams.put("q", "action/check_username");
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, BaseModel.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 注册
     */
    public static void register(String tag, String email, String username, String password, String realname, int sex, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("email", email);
        requestParams.put("username", username);
        requestParams.put("password", password);
        requestParams.put("realname", realname);
        requestParams.put("sex", String.valueOf(sex));
        requestParams.put("q", "action/reg");
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, BaseModel.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }


    /**
     * 每天签到
     */
    public static void signIn(String tag, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("t", String.valueOf((int) Math.random() * 10 + 1));
        requestParams.put("act", "ajaxbaidu");
        requestParams.put(Urls.ACTION, "dayqiandao");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, SignIn.class, listener);
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

    public static void getInvestList(String tag, int page, String xmtype, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("page", String.valueOf(page));
        requestParams.put("xmtype", xmtype);
//        requestParams.put("timelimit", null);
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
     * 投标操作
     */
    public static void tender(String tag, String id, String money, String paypassword, String valicode,  ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("id", id);
        requestParams.put("money", money);
        requestParams.put("paypassword", paypassword);
        requestParams.put("valicode", valicode);
        requestParams.put("q", "code/borrow/tender");
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, BaseModel.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }


    /**
     * 回款查询
     */
    public static void searchReceiable(String tag, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("q", "action/checkhuikuan");
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, ReceivableSearch.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }


    /**
     * 资金记录
     */
    public static void getMoneyRecord(String tag, int page, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("q", "code/account/log");
        requestParams.put("page", String.valueOf(page));
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, Money.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 我的财富
     */
    public static void getMyWealth(String tag, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("q", "action/checkcaifu");
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, MyWeallth.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 提现记录
     */
    public static void getWithdrawalRecord(String tag, int page, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("q", "code/account/cash");
        requestParams.put("page", String.valueOf(page));
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, WithdrawalRecord.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }


    /**
     * 理财管理-成功投资的借款
     */
    public static void getFinancialSuccess(String tag, int page, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("q", "code/borrow/succes");
        requestParams.put("page", String.valueOf(page));
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, FinancialManager.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 理财管理-未收款明细
     */
    public static void getFinancialGathering(String tag, int page, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("q", "code/borrow/gathering");
        requestParams.put("page", String.valueOf(page));
        requestParams.put("status", "0");
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, FinancialManager.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }


    /**
     * 微币记录
     */
    public static void getMicroHistory(String tag, int page, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("q", "code/account/wblog");
        requestParams.put("page", String.valueOf(page));
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, Micro.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 站内信
     */
    public static void getStandInsideLetter(String tag, int page, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("q", "code/message");
        requestParams.put("page", String.valueOf(page));
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, StandInsideLetter.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }


    /**
     * 账户安全
     */
    public static void getSecurityLevel(String tag, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, User.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 获得银行卡信息
     */
    public static void getBank(String tag, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("q", "code/account/bank");
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, Bank.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 修改银行卡信息
     */
    public static void changeBank(String tag, String account, String bank, String branch, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("account", account);
        requestParams.put("bank", bank);
        requestParams.put("branch", branch);
        requestParams.put("q", "code/account/bank");
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, Bank.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }


    /**
     * 获取手机验证码
     */
    public static void getPhoneVerificationCode(String tag, String phonenum, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("phonenum", phonenum);
        requestParams.put(Urls.ACTION, "ajaxphonecheck");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, SecurityPhone.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 绑定手机验证
     */
    public static void bindingPhone(String tag, String phone, String valicodes, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("phone", phone);
        requestParams.put("valicodes", valicodes);
        requestParams.put("q", "code/user/phone_status");
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, SecurityPhone.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 邮箱验证
     */
    public static void bindingEmail(String tag, String email, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("email", email);
        requestParams.put("q", "code/user/email_status");
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, SecurityPhone.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 修改登录密码
     */
    public static void changePassword(String tag, String oldpassword, String newpassword, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("oldpassword", oldpassword);
        requestParams.put("newpassword", newpassword);
        requestParams.put("q", "code/user/userpwd");
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, SecurityPhone.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 修改支付密码
     */
    public static void changeaymentPassword(String tag, String oldpassword, String newpassword, String valicode, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("oldpassword", oldpassword);
        requestParams.put("newpassword", newpassword);
        requestParams.put("valicode", valicode);
        requestParams.put("q", "code/user/paypwd");
        requestParams.put(Urls.ACTION, "users");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, SecurityPhone.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 利息计算器
     */
    public static void calculatorInterest(String tag, String account, String lilv, String times, int type, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("account", account);
        requestParams.put("lilv", lilv);
        requestParams.put("times", times);
        requestParams.put("type", String.valueOf(type));
        requestParams.put(Urls.ACTION, "lixitools");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, Calculator.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 网贷计算器
     */
    public static void calculatorNetCredit(String tag, String nianxi, String times, int type, String account, String addtime, String award, String manage, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("nianxi", nianxi);
        requestParams.put("qixian", times);
        requestParams.put("type", String.valueOf(type));
        requestParams.put("account", account);
        requestParams.put("addtime", addtime);
        requestParams.put("award", award);
        requestParams.put("manage", manage);
        requestParams.put(Urls.ACTION, "ttools");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, Calculator.class, listener);
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
     * 相关文章
     */
    public static void getRelatedArticleList(String tag, String siteid, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("siteid", siteid);
        requestParams.put(Urls.ACTION, "article/related");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, ArticleRelated.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 评论列表
     */
    public static void getCommentList(String tag, String id, int page, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("id", id);
        requestParams.put("page", String.valueOf(page));
        requestParams.put("type", "list");
        requestParams.put("code", "article");
        requestParams.put(Urls.ACTION, "comments");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, Comment.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 添加评论
     */
    public static void addComment(String tag, String id, String comment, String valicode, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("id", id);
        requestParams.put("comment", comment);
        requestParams.put("valicode", valicode);
        requestParams.put("type", "add");
        requestParams.put("code", "article");
        requestParams.put(Urls.ACTION, "comments");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, BaseModel.class, listener);
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

    /**
     * 微币转换
     */
    public static void changeWb(String tag, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put(Urls.ACTION, "changewbs");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, BaseModel.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 轮换图
     */
    public static void scrollPic(String tag, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put(Urls.ACTION, "scrollpic");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, RotationImage.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }


    /**
     * 获得抽奖次数
     */
    public static void getAwardNumber(String tag, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("checkt", "1");
        requestParams.put(Urls.ACTION, "priceget");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, Award.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 抽奖
     */
    public static void award(String tag, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("checkt", "0");
        requestParams.put(Urls.ACTION, "priceget");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, Award.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 合作伙伴
     */
    public static void getPartner(String tag, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put(Urls.ACTION, "friends/list");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, About.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 公司介绍
     */
    public static void getAboutCompany(String tag, ResponseListener listener) {
        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put(Urls.ACTION, "aboutus/list");

        String url = Urls.ACTION_INDEX;
        GsonPostRequest request = createGsonPostRequest(url, requestParams, About.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }


    /**
     * 头像获得
     */
    public static void getAvatars(String tag, ResponseListener listener) {

        listener.onStarted();

        Map<String, String> requestParams = getSignatureMap();
        requestParams.put("t", String.valueOf((int) Math.random() * 10 + 1));
        requestParams.put(Urls.ACTION, "getavatars");

        String url = Urls.ACTION_INDEX;
        GsonGetRequest request = createGsonGetRequest(url, requestParams, User.class, listener);
        request.setTag(tag);
        requestQueue.add(request);
    }


    /**
     * 上传头像
     */
    public static void uploadAvatars(String tag, String filePath, final ResponseListener listener) {

        listener.onStarted();

        long time = System.currentTimeMillis();
        String timestamp = String.valueOf(time);
        String signature = AppUtil.getSignature(timestamp);

        String url = Urls.ACTION_INDEX;
        MultiPartGsonPostRequest multiPartRequest = createMultiPartGsonPostRequest(url, BaseModel.class, listener);
        multiPartRequest.setTag(tag);
        multiPartRequest.addMultipartParam(TIMESTAMP, "text/plain", timestamp);
        multiPartRequest.addMultipartParam(SIGNATURE, "text/plain", signature);
        multiPartRequest.addMultipartParam(Urls.ACTION, "text/plain", "upavatars");
//        multiPartRequest.addMultipartParam("avatarpic", "image/jpg", String.valueOf(new File(filePath)));
        multiPartRequest.addFile("avatarpic", filePath);


//        SimpleMultipartRequest multiPartRequest = createSimpleMultipartRequest(url, BaseModel.class, listener);
//        multiPartRequest.setTag(tag);

//        MultipartEntity multipartEntity = multiPartRequest.getMultiPartEntity();
//        multipartEntity.addStringPart(TIMESTAMP, timestamp);
//        multipartEntity.addStringPart(SIGNATURE, signature);
//        multipartEntity.addStringPart(Urls.ACTION, "upavatars");
//        File file = new File(filePath);
//        multipartEntity.addFilePart("avatarpic", new File(filePath));

        requestQueue.add(multiPartRequest);
    }

    /**
     * 上传头像
     */
    public static void uploadAvatars(String tag, Bitmap bitmap, final ResponseListener listener) {

        listener.onStarted();

        long time = System.currentTimeMillis();
        String timestamp = String.valueOf(time);
        String signature = AppUtil.getSignature(timestamp);

        String url = Urls.ACTION_INDEX;
//        MultiPartGsonPostRequest multiPartRequest = createMultiPartGsonPostRequest(url, BaseModel.class, listener);
//        multiPartRequest.setTag(tag);
//        multiPartRequest.addMultipartParam(TIMESTAMP, "text/plain", timestamp);
//        multiPartRequest.addMultipartParam(SIGNATURE, "text/plain", signature);
//        multiPartRequest.addMultipartParam(Urls.ACTION, "text/plain", "upavatars");
//        multiPartRequest.addMultipartParam("avatarpic", "images", String.valueOf(new File(filePath)));
////        multiPartRequest.addFile("avatarpic", filePath);

        SimpleMultipartRequest multiPartRequest = createSimpleMultipartRequest(url, BaseModel.class, listener);
        multiPartRequest.setTag(tag);

        MultipartEntity multipartEntity = multiPartRequest.getMultiPartEntity();
        multipartEntity.addStringPart(TIMESTAMP, timestamp);
        multipartEntity.addStringPart(SIGNATURE, signature);
        multipartEntity.addStringPart(Urls.ACTION, "upavatars");
////        bitmap参数
        multipartEntity.addBinaryPart("avatarpic", bitmapToBytes(bitmap));

        requestQueue.add(multiPartRequest);
    }

    private static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
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

    private static <T> MultiPartGsonPostRequest createMultiPartGsonPostRequest(String url, Class<T> clazz, final ResponseListener listener) {

        MultiPartGsonPostRequest request = new MultiPartGsonPostRequest(url, clazz, new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                Log.d(TAG, "onResponse = " + response.toString());
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "volleyError = " + volleyError.toString());
                Toast.makeText(AppContext.getInstance().getApplicationContext(), VolleyErrorHelper.getMessage(volleyError, AppContext.getInstance()), Toast.LENGTH_SHORT).show();
                listener.onErrorResponse(volleyError);
            }
        });

        try {
            Log.d(TAG, "request Headers = " + request.getHeaders().toString());
            //        String params = requestParamsMap.toString().substring(1, requestParamsMap.toString().length() - 1);
            Log.d(TAG, "request Url-Params = " + request.getUrl());
            Log.d(TAG, "request getFilesToUpload = " + request.getFilesToUpload().toString());
            Log.d(TAG, "request getEncodedUrlParams = " + request.getEncodedUrlParams().toString());
            Log.d(TAG, "request getMultipartParams = " + request.getMultipartParams().toString());
            Log.d(TAG, "request getBody = " + request.getBody());
            Log.d(TAG, "request getBodyContentType = " + request.getBodyContentType());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        return request;
    }

    private static <T> SimpleMultipartRequest createSimpleMultipartRequest(String url, Class<T> clazz, final ResponseListener listener) {

        SimpleMultipartRequest request = new SimpleMultipartRequest(url, clazz, new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                Log.d(TAG, "onResponse = " + response.toString());
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "volleyError = " + volleyError.toString());
                Toast.makeText(AppContext.getInstance().getApplicationContext(), VolleyErrorHelper.getMessage(volleyError, AppContext.getInstance()), Toast.LENGTH_SHORT).show();
                listener.onErrorResponse(volleyError);
            }
        });

        try {
            Log.d(TAG, "request Headers = " + request.getHeaders().toString());
            //        String params = requestParamsMap.toString().substring(1, requestParamsMap.toString().length() - 1);
            Log.d(TAG, "request Url-Params = " + request.getUrl());
//            Log.d(TAG, "request getFilesToUpload = " + request.getFilesToUpload().toString());
            Log.d(TAG, "request getEncodedUrlParams = " + request.getEncodedUrlParams().toString());
//            Log.d(TAG, "request getMultipartParams = " + request.getMultipartParams().toString());
            Log.d(TAG, "request getBody = " + request.getBody());
            Log.d(TAG, "request getBodyContentType = " + request.getBodyContentType());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        return request;
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
