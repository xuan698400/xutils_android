/* 
 * @(#)AlertDialogUtils.java    Created on 2011-6-2
 * Copyright (c) 2011 ZDSoft Networks, Inc. All rights reserved.
 * $Id: AlertDialogUtils.java 36102 2013-03-20 04:33:56Z xuan $
 */
package com.xuan.xutils.android.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;

/**
 * AlertDialog工具类
 * 
 * @author xuan
 * @version $Revision: 36102 $, $Date: 2013-03-20 12:33:56 +0800 (Wed, 20 Mar 2013) $
 */
public class AlertDialogUtils {

    /**
     * 展现简单的一个按钮的alert框，类似网页alert
     * 
     * @param title
     * @param message
     * @param buttonText
     */
    public static void displayAlert(Context context, String title, String message, String buttonText) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setTitle(title).setMessage(message).create();

        alertDialog.show();
    }

    /**
     * 展现简单的一个按钮的alert框，类似网页alert(可在线程中使用)
     * 
     * @param context
     * @param title
     * @param message
     * @param buttonText
     * @param handler
     */
    public static void displayAlert2Handler(final Context context, final String title, final String message,
            final String buttonText, Handler handler) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                displayAlert(context, title, message, buttonText);
            }
        });
    }

    /**
     * 供用户选择，然后触发事件的提示框
     * 
     * @param context
     * @param title
     *            标题
     * @param message
     *            提示文本
     * @param positiveBtnText
     *            确定按钮文本
     * @param positionOnclick
     *            确定按钮事件
     * @param negativeBtnText
     *            取消按钮文本
     * @param negativeOnclick
     *            取消按钮事件
     */
    public static void displayAlert4Choice(Context context, String title, String message, String positiveBtnText,
            DialogInterface.OnClickListener positionOnclick, String negativeBtnText,
            DialogInterface.OnClickListener negativeOnclick) {

        Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);

        if (null == positionOnclick) {
            positionOnclick = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
        }
        builder.setPositiveButton(positiveBtnText, positionOnclick);

        if (null == negativeOnclick) {
            negativeOnclick = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
        }
        builder.setNegativeButton(negativeBtnText, negativeOnclick);

        builder.create().show();
    }

    /**
     * 多个选择选一个
     * 
     * @param context
     * @param title
     * @param cancelable
     * @param selectNames
     * @param OnClickListener
     */
    public static void displayAlert4SingleChoice(Context context, String title, boolean cancelable,
            String[] selectNames, final DialogInterface.OnClickListener onClickListener) {
        AlertDialog accountDlg = new AlertDialog.Builder(context).setTitle(title).setCancelable(cancelable)
                .setSingleChoiceItems(selectNames, -1, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (null != onClickListener) {
                            onClickListener.onClick(dialog, which);
                        }

                        dialog.dismiss();
                    }
                }).create();
        accountDlg.show();
    }

}
