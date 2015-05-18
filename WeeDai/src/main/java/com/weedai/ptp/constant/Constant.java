package com.weedai.ptp.constant;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constant {

    public final static class InvestType {
        public final static int TYPE_DY = 1;   //1.抵押标
        public final static int TYPE_ZQ = 2;   //2.债权转让
        public final static int TYPE_XY = 3;   //3.信用标
        public final static int TYPE_YX = 4;   //4.优选理财
    }

//    public final static class FinancialProgress {
//        public final static int PROGRESS_JOIN = 1;   //1.抵押标
//        public final static int PROGRESS_HAVE_FULL = 2;   //2.债权转让
//        public final static int PROGRESS_COMPLETED = 3;   //3.信用标
//        public final static int PROGRESS_PAYMENT = 4;   //4.优选理财
//    }

    public final static class XMTYPE {
        public final static String Borrow = "Borrow";  //:正在招标
        public final static String Now = "Now";  //正在还款
        public final static String Yes = "Yes";  //已还完

    }

    public final static class CodeResult {
        public final static int SUCCESS = 200;
        public final static int FAILURE = 400;
    }

    public final static class ArticleType {
        public final static int NOTICE = 22;   //资讯
        public final static int INFORMATION = 59;   //网站公告
        public final static int AD = 133;  //投资英雄榜公告
    }


    public final static Map<String, String> bankMap = new HashMap<String, String>();
    public final static List<String> bankNameList =  new ArrayList<String>();

    static {
        bankMap.put("300", "工商银行");
        bankMap.put("301", "中国银行");
        bankMap.put("302", "建设银行");
        bankMap.put("303", "农业银行");
        bankMap.put("465", "广发银行");
        bankMap.put("463", "交通银行");
        bankMap.put("466", "招商银行");
        bankMap.put("467", "平安银行");
        bankMap.put("468", "兴业银行");
        bankMap.put("469", "民生银行");
        bankMap.put("470", "华夏银行");
        bankMap.put("471", "上海浦东发展银行");
        bankMap.put("472", "中信银行");

        bankNameList.add("工商银行");
        bankNameList.add("中国银行");
        bankNameList.add("建设银行");
        bankNameList.add("农业银行");
        bankNameList.add("广发银行");
        bankNameList.add("交通银行");
        bankNameList.add("招商银行");
        bankNameList.add("平安银行");
        bankNameList.add("兴业银行");
        bankNameList.add("民生银行");
        bankNameList.add("华夏银行");
        bankNameList.add("上海浦东发展银行");
        bankNameList.add("中信银行");
    }


}
