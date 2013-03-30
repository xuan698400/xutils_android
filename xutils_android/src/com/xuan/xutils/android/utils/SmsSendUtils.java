/* 
 * @(#)SmsSendUtils.java    Created on 2013-2-21
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.xutils.android.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 用本手机发送短信功能
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-2-21 上午11:40:25 $
 */
public abstract class SmsSendUtils {

    /**
     * 根据手机号发送短信
     * 
     * @param context
     * @param phone
     */
    public static void sendSmsByPhone(Context context, String phone) {
        if (Validators.isEmpty(phone)) {
            return;
        }
        Uri uri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        // intent.putExtra("sms_body", "");
        context.startActivity(intent);
    }

    /**
     * 根据内容调用手机通讯录
     * 
     * @param context
     * @param content
     */
    public static void sendSmsByContent(Context context, String content) {
        Uri uri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        context.startActivity(intent);
    }

}
