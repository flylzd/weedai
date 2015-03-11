package com.weedai.p2p.utils;


import android.content.Context;
import android.content.Intent;

import com.weedai.p2p.ui.activity.OptimizingFinancialActivity;

public class UIHelper {

    public static void showOptimizingFinancial(Context context) {
        Intent intent = new Intent(context, OptimizingFinancialActivity.class);
        context.startActivity(intent);
    }
}
