package com.weedai.ptp.utils;


import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class DataUtil {

    public static String urlDecode(String encode) {

        if (!TextUtils.isEmpty(encode)) {
            try {
                return URLDecoder.decode(encode, "utf8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
