package com.weedai.ptp.model;


import java.io.Serializable;

public class MicroList implements Serializable {

    public String uid;
    public String fen;
    public String addtime;
    public String addip;
    public int type;  //（0站岗微币 1签到  2分享 3投资 4抽奖活动 7罚息 8续投奖励）
    public String account_num;

}
