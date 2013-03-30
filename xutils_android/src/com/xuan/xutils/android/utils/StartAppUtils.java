/* 
 * @(#)StartAppUtils.java    Created on 2013-3-28
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.xutils.android.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import com.xuan.xutils.android.common.Constants;

/**
 * 启动应用工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-3-28 下午5:46:31 $
 */
public class StartAppUtils {

    /**
     * 获取pkg应用中第一个Activity的名字，冒失没什么意义哦
     * 
     * @param context
     * @param pkg
     * @return
     */
    public static String getFirstActivityName(Context context, String pkg) {
        String activityName = null;
        // 得到所有应用的包信息
        List<PackageInfo> list = context.getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES);

        int length = list.size();
        Log.d("pkg length", length + "");

        for (int i = 0; i < length; i++) {
            String name = list.get(i).packageName;
            Log.d("pkg " + i, name);

            if (name.equalsIgnoreCase(pkg)) {
                ActivityInfo activityinfo = list.get(i).activities[0];// 得到入口activity
                activityName = activityinfo.name;
                Log.d("activity " + i, activityName);
                break;
            }
        }

        return activityName;
    }

    /**
     * 判断应用是否存在
     * 
     * @param pkg
     * @param context
     * @return
     */
    public static boolean isInstalledApp(String pkg, Context context) {
        if (Validators.isEmpty(pkg)) {
            return false;
        }

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(pkg,
                    PackageManager.GET_ACTIVITIES + Context.CONTEXT_IGNORE_SECURITY);

            return !(null == packageInfo);
        }
        catch (Exception e) {
            Log.e(Constants.TAG, "", e);
        }

        return false;
    }

    /**
     * 判断应用是否存在
     * 
     * @param pkg
     * @param context
     * @return
     */
    public static boolean isInstalledApp2(String pkg, Context context) {
        if (Validators.isEmpty(pkg)) {
            return false;
        }

        List<PackageInfo> packageInfoList = context.getPackageManager().getInstalledPackages(0);

        for (int i = 0, n = packageInfoList.size(); i < n; i++) {
            PackageInfo packageInfo = packageInfoList.get(i);
            Log.d(Constants.TAG, packageInfo.packageName);
            if (pkg.equals(packageInfo.packageName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 启动App
     * 
     * @param context
     * @param pkg
     * @param paramsMap
     */
    public static void startApp4LaunchIntent(Context context, String pkg, HashMap<String, String> paramsMap) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkg);
        try {
            if (null != paramsMap) {
                for (Entry<String, String> entry : paramsMap.entrySet()) {
                    intent.putExtra(entry.getKey(), entry.getValue());
                }
            }

            context.startActivity(intent);
        }
        catch (Exception e) {
            Log.d(Constants.TAG, "", e);
        }
    }

    /**
     * 启动App
     * 
     * @param context
     * @param pkg
     * @param activity
     * @param paramsMap
     */
    public static void startApp4ComponentName(Context context, String pkg, String activity,
            HashMap<String, String> paramsMap) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(pkg, activity));
            intent.setAction("android.intent.action.MAIN");

            if (null != paramsMap) {
                for (Entry<String, String> entry : paramsMap.entrySet()) {
                    intent.putExtra(entry.getKey(), entry.getValue());
                }
            }

            context.startActivity(intent);
        }
        catch (Exception e) {
            Log.d(Constants.TAG, "", e);
        }
    }

    /**
     * 启动App
     * 
     * @param context
     * @param url
     * @param paramsMap
     */
    public static void startApp4Url(final Context context, String url, HashMap<String, String> paramsMap) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            if (null != paramsMap) {
                for (Entry<String, String> entry : paramsMap.entrySet()) {
                    intent.putExtra(entry.getKey(), entry.getValue());
                }
            }

            context.startActivity(intent);
        }
        catch (Exception e) {
            Log.d(Constants.TAG, "", e);
        }
    }

    /**
     * 启动App
     * 
     * @param context
     * @param intentStr
     * @param categoryStr
     * @param paramsMap
     */
    public static void startApp4Category(final Context context, String intentStr, String categoryStr,
            HashMap<String, String> paramsMap) {
        try {
            Intent intent = new Intent(intentStr);
            intent.addCategory(categoryStr);

            List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(intent, 0);

            if (activities.size() > 0) {
                if (null != paramsMap) {
                    for (Entry<String, String> entry : paramsMap.entrySet()) {
                        intent.putExtra(entry.getKey(), entry.getValue());
                    }
                }
                context.startActivity(intent);
            }
        }
        catch (Exception e) {
            Log.d(Constants.TAG, "", e);
        }

    }
}
