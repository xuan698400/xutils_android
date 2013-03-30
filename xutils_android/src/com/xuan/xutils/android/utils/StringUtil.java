/* 
 * @(#)StringUtil.java    Created on 2012-9-20
 * Copyright (c) 2012 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.xutils.android.utils;

import java.io.UnsupportedEncodingException;

import android.util.Log;

import com.xuan.xutils.android.common.Constants;

/**
 * 字符串处理工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-3-25 上午9:25:38 $
 */
public class StringUtil {
    private StringUtil() {
    }

    public static String newString(byte[] bs, String charset) {
        try {
            String str = new String(bs, charset);
            return str;
        }
        catch (UnsupportedEncodingException e) {
            Log.e(Constants.TAG, "", e);
        }
        return null;
    }

    public static byte[] getBytes(String str, String charsetName) {
        try {
            return str.getBytes(charsetName);
        }
        catch (UnsupportedEncodingException e) {
            Log.e(Constants.TAG, "", e);
        }
        return null;
    }

    /**
     * 截取固定长度的字符串，超长部分用suffix代替，最终字符串真实长度不会超过maxLength.
     * 
     * @param str
     * @param maxLength
     * @param suffix
     * @return
     */
    public static String cutOut(String str, int maxLength, String suffix) {
        if (Validators.isEmpty(str)) {
            return str;
        }

        int byteIndex = 0;
        int charIndex = 0;

        while (charIndex < str.length() && byteIndex <= maxLength) {
            char c = str.charAt(charIndex);
            if (c >= 256) {
                byteIndex += 2;
            }
            else {
                byteIndex++;
            }
            charIndex++;
        }

        if (byteIndex <= maxLength) {
            return str;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, charIndex));
        sb.append(suffix);

        while (getRealLength(sb.toString()) > maxLength) {
            sb.deleteCharAt(--charIndex);
        }

        return sb.toString();
    }

    /**
     * 取得字符串的真实长度，一个汉字长度为两个字节。
     * 
     * @param str
     *            字符串
     * @return 字符串的字节数
     */
    public static int getRealLength(String str) {
        if (str == null) {
            return 0;
        }

        char separator = 256;
        int realLength = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= separator) {
                realLength += 2;
            }
            else {
                realLength++;
            }
        }
        return realLength;
    }

    /**
     * 字符串过滤null
     * 
     * @param oldStr
     * @return
     */
    public static String filterNull(String oldStr) {
        if (null == oldStr || "null".equals(oldStr)) {
            return "";
        }

        return oldStr;
    }

}
