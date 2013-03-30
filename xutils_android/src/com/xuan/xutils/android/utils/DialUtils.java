/* 
 * @(#)DialUtils.java    Created on 2013-2-6
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.xutils.android.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 拨打电话工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-2-6 下午3:00:48 $
 */
public abstract class DialUtils {

    /**
     * 根据手机好拨打电话
     * 
     * @param context
     * @param phone
     */
    public static void callByPhone(Context context, String phone) {
        if (Validators.isEmpty(phone)) {
            return;
        }

        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

}
