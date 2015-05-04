package com.weedai.ptp.model;


import java.io.Serializable;

public class MoneyList implements Serializable {
    public String leixing;
    public String money;
    public String total;
    public String use_money;
    public String no_use_money;
    public String addtime;
    public String to_username;  //交易对象 空为admin
}
