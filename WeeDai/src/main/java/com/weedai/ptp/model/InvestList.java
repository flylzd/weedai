package com.weedai.ptp.model;


import java.io.Serializable;

public class InvestList implements Serializable {

    public String id;
    public String name;
    public String apr;
    public String account;
    public String time_limit;
    public float scale;
    public String addtime;
    public String is_fast;
    public String is_transfer;
    public String account_yes;
    public String award;   //奖励的类型
    public String funds;
    public int status;
    public float repayment_yesaccount;
    public float repayment_account;
    public long verify_time;     //距离审核时间
    public String success_time;    //复审时间

    public int style;  //0：按月分期还款； 3：按月付息，到期环本
    public int is_you;

    public String tender_times;

    public String other;


}
