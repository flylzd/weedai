package com.weedai.ptp.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AppUtil {

    public static String TOKEN = "f6499a6bba1";

    public static String getSignature(String timestamp) {

        String[] str = { TOKEN, timestamp };
        Arrays.sort(str); // 字典序排序
        String decript = str[0] + str[1];
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("SHA-1");
            byte messageDigest[] =  digest.digest(decript.getBytes());
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
