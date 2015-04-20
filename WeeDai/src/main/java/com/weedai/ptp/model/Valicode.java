package com.weedai.ptp.model;


import java.io.Serializable;

public class Valicode extends BaseModel {

    public ValicodeData data;

    public class ValicodeData implements Serializable {
        public String code;
    }
}
