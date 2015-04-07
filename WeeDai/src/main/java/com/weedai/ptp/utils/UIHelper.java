package com.weedai.ptp.utils;


import android.content.Context;
import android.content.Intent;

import com.weedai.ptp.ui.activity.ArticleActivity;
import com.weedai.ptp.ui.activity.MainActivity;
import com.weedai.ptp.ui.activity.MyBankCardActivity;
import com.weedai.ptp.ui.activity.MyRechargeActivity;
import com.weedai.ptp.ui.activity.MyWealthActivity;
import com.weedai.ptp.ui.activity.OptimizingFinancialActivity;
import com.weedai.ptp.ui.activity.RegisterActivity;

public class UIHelper {

    public static void showMain(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void showRegister(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    public static void showMyWealth(Context context) {
        Intent intent = new Intent(context, MyWealthActivity.class);
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

    public static void showOptimizingFinancial(Context context) {
        Intent intent = new Intent(context, OptimizingFinancialActivity.class);
        context.startActivity(intent);
    }

    public static void showArticle(Context context) {
        Intent intent = new Intent(context, ArticleActivity.class);
        context.startActivity(intent);
    }
}
