/* 
 * @(#)ProgressDialogUtils.java    Created on 2012-11-16
 * Copyright (c) 2012 ZDSoft Networks, Inc. All rights reserved.
 * $Id: ProgressDialogUtils.java 34344 2013-01-21 07:40:03Z xuan $
 */
package com.xuan.xutils.android.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

/**
 * 简化一些加载提示框的操作
 * 
 * @author xuan
 * @version $Revision: 34344 $, $Date: 2013-01-21 15:40:03 +0800 (周一, 21 一月 2013) $
 */
public class ProgressDialogUtils {

    private final static ProgressDialogUtils instance = new ProgressDialogUtils();
    private Context context;
    private ProgressDialog progressDialog;

    private ProgressDialogUtils() {
    }

    public static ProgressDialogUtils instance(Context context) {
        if (instance.context != context) {
            instance.context = context;
            instance.progressDialog = new ProgressDialog(context);
        }

        return instance;
    }

    /**
     * 单例的progressDialog显示
     * 
     * @param title
     */
    public void show(String title) {
        ProgressDialogUtils.show(title, progressDialog);
    }

    /**
     * 单例的progressDialog隐藏
     */
    public void dismiss() {
        ProgressDialogUtils.dismiss(progressDialog);
    }

    /**
     * 单例的progressDialog影藏，在线程中使用
     */
    public void dismiss(Handler handler) {
        ProgressDialogUtils.dismiss(handler, progressDialog);
    }

    /**
     * 显示（在UI线程中使用）
     * 
     * @param title
     */
    public static void show(String title, ProgressDialog progressDialog) {
        progressDialog.setTitle(title);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * 隐藏（在线程中使用）
     * 
     * @param handler
     */
    public static void dismiss(Handler handler, final ProgressDialog progressDialog) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
    }

    /**
     * 隐藏（在UI线程中使用）
     */
    public static void dismiss(ProgressDialog progressDialog) {
        progressDialog.dismiss();
    }

}
