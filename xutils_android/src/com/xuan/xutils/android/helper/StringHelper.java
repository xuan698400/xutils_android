/* 
 * @(#)StringHelper.java    Created on 2013-3-14
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.xutils.android.helper;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * 字符串一些工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-3-14 下午8:20:55 $
 */
public class StringHelper {

    /**
     * 是否正常的字符串
     * 
     * @param text
     * @return
     */
    public static boolean isText(String text) {
        if (text == null || text.length() == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * bytes[]转换成Hex字符串,可用于URL转换，IP地址转换
     * 
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 字节转换成合适的单位
     * 
     * @param value
     * @return
     */
    public static String prettyBytes(long value) {
        String args[] = { "B", "KB", "MB", "GB", "TB" };
        StringBuilder sb = new StringBuilder();
        int i;
        if (value < 1024L) {
            sb.append(String.valueOf(value));
            i = 0;
        }
        else if (value < 1048576L) {
            sb.append(String.format("%.1f", value / 1024.0));
            i = 1;
        }
        else if (value < 1073741824L) {
            sb.append(String.format("%.2f", value / 1048576.0));
            i = 2;
        }
        else if (value < 1099511627776L) {
            sb.append(String.format("%.3f", value / 1073741824.0));
            i = 3;
        }
        else {
            sb.append(String.format("%.4f", value / 1099511627776.0));
            i = 4;
        }
        sb.append(' ');
        sb.append(args[i]);
        return sb.toString();
    }

    /**
     * 将Excepiton信息转换成String字符串.
     * 
     * @param t
     * @return
     */
    public static String exceptionToString(Throwable throwable) {
        if (throwable == null) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            throwable.printStackTrace(new PrintStream(baos));
        }
        finally {
            try {
                baos.close();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                System.gc();
            }
        }
        return baos.toString();
    }

}
