package com.weedai.p2p.utils;


import android.content.Context;
import android.content.Intent;

import com.weedai.p2p.ui.activity.MainActivity;
import com.weedai.p2p.ui.activity.OptimizingFinancialActivity;
import com.weedai.p2p.ui.activity.RegisterActivity;

public class UIHelper {

    public static void showMain(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void showRegister(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    public static void showOptimizingFinancial(Context context) {
        Intent intent = new Intent(context, OptimizingFinancialActivity.class);
        context.startActivity(intent);
    }
}
