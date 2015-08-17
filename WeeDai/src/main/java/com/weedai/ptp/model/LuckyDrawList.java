package com.weedai.ptp.model;

import java.io.Serializable;

/**
 * 作者：lemon
 * 日期：2015-08-17
 */
public class LuckyDrawList implements Serializable {


    public String prize;  //礼品名称（需decode）
    public String addtime;
    public Integer if_fang;  //（1为已经发放，其他为没发放）

}
