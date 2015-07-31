package com.weedai.ptp.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.weedai.ptp.model.InvestList;
import com.weedai.ptp.model.StandInsideLetterList;
import com.weedai.ptp.ui.activity.AboutActivity;
import com.weedai.ptp.ui.activity.AboutCompanyActivity;
import com.weedai.ptp.ui.activity.AccountActivity;
import com.weedai.ptp.ui.activity.AccountAvatarsActivity;
import com.weedai.ptp.ui.activity.AccountEmailActivity;
import com.weedai.ptp.ui.activity.ArticleActivity;
import com.weedai.ptp.ui.activity.ArticleDetailActivity;
import com.weedai.ptp.ui.activity.CalculatorInterestActivity;
import com.weedai.ptp.ui.activity.CalculatorNetCreditActivity;
import com.weedai.ptp.ui.activity.ChangePasswordActivity;
import com.weedai.ptp.ui.activity.ChangePaymentPasswordActivity;
import com.weedai.ptp.ui.activity.FinanceInvestmentActivity;
import com.weedai.ptp.ui.activity.FinancialActivity;
import com.weedai.ptp.ui.activity.FinancialDetailActivity;
import com.weedai.ptp.ui.activity.GestureClearActivity;
import com.weedai.ptp.ui.activity.GestureEditActivity;
import com.weedai.ptp.ui.activity.GesturePasswordActivity;
import com.weedai.ptp.ui.activity.GestureVerifyActivity;
import com.weedai.ptp.ui.activity.HeroActivity;
import com.weedai.ptp.ui.activity.HomeVideoActivity;
import com.weedai.ptp.ui.activity.InvestorActivity;
import com.weedai.ptp.ui.activity.LiCaiRecommendActivity;
import com.weedai.ptp.ui.activity.LoginActivity;
import com.weedai.ptp.ui.activity.LuckyDrawActivity;
import com.weedai.ptp.ui.activity.MainActivity;
import com.weedai.ptp.ui.activity.MoneyRecordActivity;
import com.weedai.ptp.ui.activity.MyBankCardActivity;
import com.weedai.ptp.ui.activity.MyBankCardChangeActivity;
import com.weedai.ptp.ui.activity.MyFinancialManagementActivity;
import com.weedai.ptp.ui.activity.MyMicroCurrencyHistoryActivity;
import com.weedai.ptp.ui.activity.MyRechargeActivity;
import com.weedai.ptp.ui.activity.MyStandInsideLetterActivity;
import com.weedai.ptp.ui.activity.MyStandInsideLetterDetailActivity;
import com.weedai.ptp.ui.activity.MyWealthActivity;
import com.weedai.ptp.ui.activity.MyWithdrawalActivity;
import com.weedai.ptp.ui.activity.MyWithdrawalRecordActivity;
import com.weedai.ptp.ui.activity.PartnerActivity;
import com.weedai.ptp.ui.activity.ReceivableSearchActivity;
import com.weedai.ptp.ui.activity.RechargeOnlineActivity;
import com.weedai.ptp.ui.activity.RegisterActivity;
import com.weedai.ptp.ui.activity.SecurityLevelActivity;
import com.weedai.ptp.ui.activity.SecurityPhoneActivity;
import com.weedai.ptp.ui.activity.TransferActivity;

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

    /**
     * 提现记录
     */
    public static void showMyWithdrawalRecord(Context context) {
        Intent intent = new Intent(context, MyWithdrawalRecordActivity.class);
        context.startActivity(intent);
    }

    /**
     * 提现
     */
    public static void showMyWithdrawal(Context context) {
        Intent intent = new Intent(context, MyWithdrawalActivity.class);
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

    public static void showMyBankCardChange(Context context, String bank) {
        Intent intent = new Intent(context, MyBankCardChangeActivity.class);
        intent.putExtra("bank", bank);
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
     * 站内信详情
     */
    public static void showMyStandInsideLetterDetail(Context context, StandInsideLetterList data, int requestCode) {
        Intent intent = new Intent(context, MyStandInsideLetterDetailActivity.class);
        intent.putExtra("data", data);
        ((Activity) context).startActivityForResult(intent, requestCode);
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
     * 邮箱验证
     */
    public static void showSecurityEmail(Context context) {
        Intent intent = new Intent(context, AccountEmailActivity.class);
        context.startActivity(intent);
    }

    /**
     * 我的账号
     */
    public static void showAccount(Context context) {
        Intent intent = new Intent(context, AccountActivity.class);
        context.startActivity(intent);
    }

    /**
     * 账户信息
     */
    public static void showAccountAvatars(Context context) {
        Intent intent = new Intent(context, AccountAvatarsActivity.class);
        context.startActivity(intent);
    }

    /**
     * 修改登录密码
     */
    public static void showChangePassword(Context context) {
        Intent intent = new Intent(context, ChangePasswordActivity.class);
        context.startActivity(intent);
    }

    /**
     * 修改提现密码
     */
    public static void showChangePaymentPassword(Context context) {
        Intent intent = new Intent(context, ChangePaymentPasswordActivity.class);
        context.startActivity(intent);
    }

    /**
     * 抽奖
     */
    public static void showLuckyDraw(Context context) {
        Intent intent = new Intent(context, LuckyDrawActivity.class);
        context.startActivity(intent);
    }

    /**
     * 优选理财
     */
    public static void showOptimizingFinancial(Context context) {
        Intent intent = new Intent(context, FinancialActivity.class);
        context.startActivity(intent);
    }

    /**
     * 项目推荐
     */
    public static void showLiCaiRecommend(Context context) {
        Intent intent = new Intent(context, LiCaiRecommendActivity.class);
        context.startActivity(intent);
    }

    /**
     * 债权转让
     */
    public static void showTransfer(Context context) {
        Intent intent = new Intent(context, TransferActivity.class);
        context.startActivity(intent);
    }

    public static void showFinancialDetail(Context context, InvestList data) {
        Intent intent = new Intent(context, FinancialDetailActivity.class);
//        intent.putExtra("id", bid);
        intent.putExtra("data", data);
        context.startActivity(intent);
    }

    /**
     * 投资理财
     */
    public static void showFinanceInvestment(Context context, InvestList data) {
        Intent intent = new Intent(context, FinanceInvestmentActivity.class);
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

    /**
     * 关于我们
     */
    public static void showAbout(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    /**
     * 公司介绍
     */
    public static void showAboutCompany(Context context) {
        Intent intent = new Intent(context, AboutCompanyActivity.class);
        context.startActivity(intent);
    }

    /**
     * 合作伙伴
     */
    public static void showPartner(Context context) {
        Intent intent = new Intent(context, PartnerActivity.class);
        context.startActivity(intent);
    }

    /**
     * 利息计算器
     */
    public static void showCalculatorInterest(Context context) {
        Intent intent = new Intent(context, CalculatorInterestActivity.class);
        context.startActivity(intent);
    }

    /**
     * 网贷计算器
     */
    public static void showCalculatorNetCredit(Context context) {
        Intent intent = new Intent(context, CalculatorNetCreditActivity.class);
        context.startActivity(intent);
    }

    /**
     * 首页视频
     */
    public static void showHomeVideo(Context context) {
        Intent intent = new Intent(context, HomeVideoActivity.class);
        context.startActivity(intent);
    }

    /**
     * 投资人列表
     */
    public static void showInvestor(Context context, String bid) {
        Intent intent = new Intent(context, InvestorActivity.class);
        intent.putExtra("bid", bid);
        context.startActivity(intent);
    }

    /**
     * 手势密码
     */
    public static void showPasswordGestures(Context context) {
        Intent intent = new Intent(context, GesturePasswordActivity.class);
        context.startActivity(intent);
    }

    /**
     * 编辑手势密码
     */
    public static void showLock9View(Context context) {
        Intent intent = new Intent(context, GestureEditActivity.class);
        context.startActivity(intent);
    }

    /**
     * 手势绘制/校验界面
     */
    public static void showGestureVerify(Context context) {
        Intent intent = new Intent(context, GestureVerifyActivity.class);
        context.startActivity(intent);
    }

    /**
     * 手势绘制/校验界面
     */
    public static void showGestureClear(Context context) {
        Intent intent = new Intent(context, GestureClearActivity.class);
        context.startActivity(intent);
    }


    /**
     * 在线充值
     */
    public static void showRechargeOnline(Context context,String url) {
        Intent intent = new Intent(context, RechargeOnlineActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
}
