package com.weedai.ptp.model;

import java.io.Serializable;

public class SecurityLevelData implements Serializable {

    public String paypassword; // null为没设置 其他为已设置
    public boolean phone_status; //  1为已经手机认证
    public boolean real_status;  //1为已经实名认证
}
