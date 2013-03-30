/* 
 * @(#)ToastUtils.java    Created on 2011-5-31
 * Copyright (c) 2011 ZDSoft Networks, Inc. All rights reserved.
 * $Id: ToastUtils.java 31799 2012-10-25 04:59:34Z xuan $
 */
package com.xuan.xutils.android.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * 吐司信息工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-3-25 下午7:40:05 $
 */
public class ToastUtils {
    /**
     * 显示吐司信息（较长时间）
     * 
     * @param context
     * @param text
     */
    public static void displayTextLong(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示吐司信息（较短时间）
     * 
     * @param context
     * @param text
     */
    public static void displayTextShort(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示吐司信息交给handler处理（较长时间）
     * 
     * @param context
     * @param text
     * @param handler
     */
    public static void displayTextLong2Handler(final Context context, final String text, Handler handler) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.displayTextLong(context, text);
            }
        });
    }

    /**
     * 显示吐司信息交给handler处理（较短时间）
     * 
     * @param context
     * @param text
     * @param handler
     */
    public static void displayTextShort2Handler(final Context context, final String text, Handler handler) {

        handler.post(new Runnable() {

            @Override
            public void run() {
                ToastUtils.displayTextShort(context, text);
            }
        });
    }
}
