package com.itheima.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    /**
     * 使用md5的算法进行加密
     */
    public static String md5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public static void main(String[] args) {
        /**
         * MD5 保证安全
         * 1.加盐 （&N#@H@#H
         * 2.多次加密
         */
        String salt = "123";
        System.out.println(md5(md5(md5("12345"))));

        /**
         * 推荐使用：bcrypt加密算法
         * 每一次加密后的秘钥都不一样 随机加盐 + 默认加密10次
         */
    }

}