package com.weedai.ptp.model;


import java.io.Serializable;

public class WithdrawalRecordList implements Serializable {

    public String account;
    public String total;
    public String credited;
    public String addtime;
    public int status;  //0为审核中 1为提现成功 2提现失败 3用户取消

}
