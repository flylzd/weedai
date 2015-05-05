package com.weedai.ptp.model;


import java.io.Serializable;

public class UserData implements Serializable {

    public String username;
    public String email;
    public int sex;
    public String wb;
    public String use_money;
    public String phone;
    public int avatar_status;
//    public boolean phone_status;
    public int email_status;
    public int vip_status;
    public int credit;
    public String touxiang;

    public String paypassword; // null为没设置 其他为已设置
    public int phone_status; //  1为已经手机认证
    public int real_status;  //1为已经实名认证

}
