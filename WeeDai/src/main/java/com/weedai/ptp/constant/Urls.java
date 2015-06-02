package com.weedai.ptp.constant;


public class Urls {

    private static String SERVER_IP = "112.124.22.39";

    private static String PROJECT_NAME = "/appptp"; //发布环境

//    private static String PROJECT_NAME = "/apptest";   //测试环境

    public static String SERVER_URL = "http://" + SERVER_IP + PROJECT_NAME;

    public static String SERVER_URL_AVATAR = "http://" + SERVER_IP + PROJECT_NAME + "/data/avatar";

    public static String ACTION_INDEX = SERVER_URL + "/index.php";

    public static String ACTION_INDEX_PLUGINS  = SERVER_URL + "/plugins/index.php";

    public static String ACTION = "actions";


    public static String APP_VERSION_URL = "http://fir.im/api/v2/app/version/556d0ee3546ca04275001be8?token=6c0d47a0080011e5a1009205912c89c04c24950b";
}
