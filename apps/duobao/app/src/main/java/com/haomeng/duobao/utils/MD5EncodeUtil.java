package com.haomeng.duobao.utils;

import android.util.Log;

import java.security.MessageDigest;

/**
 * MD5加密类
 * @author xujj1
 *
 */
public class MD5EncodeUtil {
	
	 private static final String TAG = "MD5EncodeUtil";

	 /**
      * MD5加密
      * @param data:数据源
      * @return 返回加密的字符串数据
      */

	public static String encryptData2MD5(String data) {
        Log.d(TAG, "encryptionData2MD5");
        String result = null;
        try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(data.getBytes("UTF-8"));
                result = byte2hex(md5.digest());
        } catch (Exception e) {
                e.printStackTrace();
        }
        return result;
	}
	
	/**
     * byte数据转十六进制数值字符串
     * @param b:byte数据源
     * @return 返回转换字符串数据
     */
    public static String byte2hex(byte[] b) {
            Log.d(TAG, "byte2hex");
            StringBuffer hs = new StringBuffer(b.length);
            String stmp = "";
            int len = b.length;
            for (int n = 0; n < len; n++) {
                    stmp = Integer.toHexString(b[n] & 0xFF);
                    if (stmp.length() == 1)
                            hs = hs.append("0").append(stmp);
                    else {
                            hs = hs.append(stmp);
                    }
            }
            return String.valueOf(hs);
    }
}
