package com.weedai.ptp.utils;


import android.content.Context;
import android.content.Intent;

import com.weedai.ptp.model.InvestList;
import com.weedai.ptp.ui.activity.AboutActivity;
import com.weedai.ptp.ui.activity.AccountActivity;
import com.weedai.ptp.ui.activity.ArticleActivity;
import com.weedai.ptp.ui.activity.ArticleDetailActivity;
import com.weedai.ptp.ui.activity.FinancialActivity;
import com.weedai.ptp.ui.activity.FinancialDetailActivity;
import com.weedai.ptp.ui.activity.HeroActivity;
import com.weedai.ptp.ui.activity.LoginActivity;
import com.weedai.ptp.ui.activity.MainActivity;
import com.weedai.ptp.ui.activity.MoneyRecordActivity;
import com.weedai.ptp.ui.activity.MyBankCardActivity;
import com.weedai.ptp.ui.activity.MyFinancialManagementActivity;
import com.weedai.ptp.ui.activity.MyMicroCurrencyHistoryActivity;
import com.weedai.ptp.ui.activity.MyRechargeActivity;
import com.weedai.ptp.ui.activity.MyStandInsideLetterActivity;
import com.weedai.ptp.ui.activity.MyWealthActivity;
import com.weedai.ptp.ui.activity.ReceivableSearchActivity;
import com.weedai.ptp.ui.activity.RegisterActivity;
import com.weedai.ptp.ui.activity.SecurityLevelActivity;
import com.weedai.ptp.ui.activity.SecurityPhoneActivity;

public class UIHelper {


    public static void showMain(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void showLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void showRegister(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    public static void showMyWealth(Context context, String amount, String wb) {
        Intent intent = new Intent(context, MyWealthActivity.class);
        intent.putExtra("amount", amount);
        intent.putExtra("wb", wb);
        context.startActivity(intent);
    }

    public static void showMyMicroCurrencyHistory(Context context, String wb) {
        Intent intent = new Intent(context, MyMicroCurrencyHistoryActivity.class);
        intent.putExtra("wb", wb);
        context.startActivity(intent);
    }

    public static void showMyBankCard(Context context) {
        Intent intent = new Intent(context, MyBankCardActivity.class);
        context.startActivity(intent);
    }

    public static void showMyRecharge(Context context) {
        Intent intent = new Intent(context, MyRechargeActivity.class);
        context.startActivity(intent);
    }

    /**
     * 回款查询
     */
    public static void showMyReceivableSearch(Context context) {
        Intent intent = new Intent(context, ReceivableSearchActivity.class);
        context.startActivity(intent);
    }

    /**
     * 资金记录
     */
    public static void showMyMoneyRecord(Context context) {
        Intent intent = new Intent(context, MoneyRecordActivity.class);
        context.startActivity(intent);
    }

    /**
     * 理财管理
     */
    public static void showMyFinancialManagemen(Context context) {
        Intent intent = new Intent(context, MyFinancialManagementActivity.class);
        context.startActivity(intent);
    }

    /**
     * 站内信
     */
    public static void showMyStandInsideLetter(Context context) {
        Intent intent = new Intent(context, MyStandInsideLetterActivity.class);
        context.startActivity(intent);
    }

    /**
     * 账户安全
     */
    public static void showSecurityLevel(Context context) {
        Intent intent = new Intent(context, SecurityLevelActivity.class);
        context.startActivity(intent);
    }

    /**
     * 绑定手机
     */
    public static void showSecurityPhone(Context context) {
        Intent intent = new Intent(context, SecurityPhoneActivity.class);
        context.startActivity(intent);
    }

    /**
     * 我的账号
     */
    public static void showAccount(Context context) {
        Intent intent = new Intent(context, AccountActivity.class);
        context.startActivity(intent);
    }

    public static void showOptimizingFinancial(Context context) {
        Intent intent = new Intent(context, FinancialActivity.class);
        context.startActivity(intent);
    }

    public static void showFinancialDetail(Context context, InvestList data) {
        Intent intent = new Intent(context, FinancialDetailActivity.class);
//        intent.putExtra("id", bid);
        intent.putExtra("data", data);
        context.startActivity(intent);
    }

    public static void showArticle(Context context) {
        Intent intent = new Intent(context, ArticleActivity.class);
        context.startActivity(intent);
    }

    public static void showArticleDetail(Context context, String aid) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra("aid", aid);
        context.startActivity(intent);
    }


    public static void showHero(Context context) {
        Intent intent = new Intent(context, HeroActivity.class);
        context.startActivity(intent);
    }

    public static void showAbout(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }
}
