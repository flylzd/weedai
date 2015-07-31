package com.weedai.ptp.model;


import java.io.Serializable;

public class Result extends BaseModel {

    public ResultData data;

    public class ResultData implements Serializable {
        public String url;
    }
}
