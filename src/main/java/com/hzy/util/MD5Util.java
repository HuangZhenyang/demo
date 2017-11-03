package com.hzy.util;


/**
 * Created by huangzhenyang on 2017/11/2.
 * md5加密解密工具类
 */
public class MD5Util {

    // 构造器设置成private
    private MD5Util() {

    }

    /**
     * MD5加码 生成32位md5码
     */
/*    public String string2MD5(String inStr) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }*/

    /**
     * 加密
     * 参数是明文
     */
    public static String encrypt(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        return new String(a);
    }

    /**
     * 解密
     * 参数是encrypt()加密过的
    * */
    public static String decrypt(String str){
        return encrypt( str);
    }


}
