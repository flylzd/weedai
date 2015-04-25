package com.weedai.ptp.constant;


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

    public final static class CodeResult {
        public final static int SUCCESS = 200;
        public final static int FAILURE = 400;
    }

    public final static class ArticleType {
        public final static int NOTICE = 22;   //资讯
        public final static int INFORMATION = 59;   //网站公告
        public final static int AD = 133;  //投资英雄榜公告
    }

}
