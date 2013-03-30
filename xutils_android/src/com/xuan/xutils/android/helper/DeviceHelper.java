/* 
 * @(#)DeviceHelper.java    Created on 2013-3-14
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.xutils.android.helper;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;

/**
 * 获取设备信息的工具类http://www.oschina.net/p/andutils
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-3-14 下午7:43:11 $
 */
public class DeviceHelper {
    public String UA = Build.MODEL;
    private String mIMEI;// 唯一的设备ID，GSM手机的 IMEI 和 CDMA手机的 MEID
    private String mSIM;// SIM卡的序列号：需要权限：READ_PHONE_STATE
    private String mMobileVersion;// 设置软件的版本号：需要权限：READ_PHONE_STATE
    private String mNetwrokIso;// 当前注册的国家环境代码
    private String mNetType;// 当前的连网类型
    private String mDeviceID;// 唯一设备号
    private List<NeighboringCellInfo> mCellinfos;
    Context context;

    private TelephonyManager telephonyManager = null;// 很多关于手机的信息可以用此类得到

    private static DeviceHelper instance = null;// 单例模式

    /**
     * 最好用全局的context获取实例
     * 
     * @param context
     * @return
     */
    public static synchronized DeviceHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DeviceHelper(context);
        }

        return instance;
    }

    private DeviceHelper(Context context) {
        this.context = context;
        findData();
    }

    /**
     * 设置手机立刻震动
     * 
     * @param context
     * @param milliseconds
     *            milliseconds/1000(S)
     */
    public void Vibrate(Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    /**
     * 刷新基本信息
     */
    public void onRefresh() {
        findData();
    }

    /**
     * 获得android设备-唯一标识，android2.2 之前无法稳定运行
     * 
     * @param mCm
     * @return
     */
    public static String getDeviceId(Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    /**
     * 打印基本信息串
     * 
     * @return
     */
    public String getDeviceInfo() {
        StringBuffer info = new StringBuffer();
        info.append("IMEI:").append(getImei());
        info.append("\n");
        info.append("SIM:").append(getSIM());
        info.append("\n");
        info.append("UA:").append(getUA());
        info.append("\n");
        info.append("MobileVersion:").append(mMobileVersion);
        info.append("\n");
        info.append(getCallState());
        info.append("\n");
        info.append("SIM_STATE: ").append(getSimState());
        info.append("\n");
        info.append("SIM: ").append(getSIM());
        info.append("\n");
        info.append(getSimOpertorName());
        info.append("\n");
        info.append(getPhoneType());
        info.append("\n");
        info.append(getPhoneSettings());
        info.append("\n");
        return info.toString();
    }

    /**
     * 获取sim卡的状态
     * 
     * @return
     */
    public String getSimState() {
        switch (telephonyManager.getSimState()) {
        case android.telephony.TelephonyManager.SIM_STATE_UNKNOWN:
            return "未知SIM状态_" + android.telephony.TelephonyManager.SIM_STATE_UNKNOWN;
        case android.telephony.TelephonyManager.SIM_STATE_ABSENT:
            return "没插SIM卡_" + android.telephony.TelephonyManager.SIM_STATE_ABSENT;
        case android.telephony.TelephonyManager.SIM_STATE_PIN_REQUIRED:
            return "锁定SIM状态_需要用户的PIN码解锁_" + android.telephony.TelephonyManager.SIM_STATE_PIN_REQUIRED;
        case android.telephony.TelephonyManager.SIM_STATE_PUK_REQUIRED:
            return "锁定SIM状态_需要用户的PUK码解锁_" + android.telephony.TelephonyManager.SIM_STATE_PUK_REQUIRED;
        case android.telephony.TelephonyManager.SIM_STATE_NETWORK_LOCKED:
            return "锁定SIM状态_需要网络的PIN码解锁_" + android.telephony.TelephonyManager.SIM_STATE_NETWORK_LOCKED;
        case android.telephony.TelephonyManager.SIM_STATE_READY:
            return "就绪SIM状态_" + android.telephony.TelephonyManager.SIM_STATE_READY;
        default:
            return "未知SIM状态_" + android.telephony.TelephonyManager.SIM_STATE_UNKNOWN;
        }
    }

    /**
     * 获取手机信号类型
     * 
     * @return
     */
    public String getPhoneType() {
        switch (telephonyManager.getPhoneType()) {
        case android.telephony.TelephonyManager.PHONE_TYPE_NONE:
            return "PhoneType: 无信号_" + android.telephony.TelephonyManager.PHONE_TYPE_NONE;
        case android.telephony.TelephonyManager.PHONE_TYPE_GSM:
            return "PhoneType: GSM信号_" + android.telephony.TelephonyManager.PHONE_TYPE_GSM;
        case android.telephony.TelephonyManager.PHONE_TYPE_CDMA:
            return "PhoneType: CDMA信号_" + android.telephony.TelephonyManager.PHONE_TYPE_CDMA;
        default:
            return "PhoneType: 无信号_" + android.telephony.TelephonyManager.PHONE_TYPE_NONE;
        }
    }

    /**
     * 服务商名称：例如：中国移动、联通 　　 SIM卡的状态必须是 SIM_STATE_READY就绪状态(使用getSimState()判断). 　　
     */
    public String getSimOpertorName() {
        if (telephonyManager.getSimState() == android.telephony.TelephonyManager.SIM_STATE_READY) {
            StringBuffer sb = new StringBuffer();
            sb.append("SimOperatorName: ").append(telephonyManager.getSimOperatorName());
            sb.append("\n");
            sb.append("SimOperator: ").append(telephonyManager.getSimOperator());
            sb.append("\n");
            sb.append("Phone:").append(telephonyManager.getLine1Number());
            return sb.toString();
        }
        else {
            StringBuffer sb = new StringBuffer();
            sb.append("SimOperatorName: ").append("未知");
            sb.append("\n");
            sb.append("SimOperator: ").append("未知");
            return sb.toString();
        }
    }

    /**
     * 获取手机的基本设置
     * 
     * @return
     */
    public String getPhoneSettings() {
        StringBuffer buf = new StringBuffer();
        String str = Secure.getString(context.getContentResolver(), Secure.BLUETOOTH_ON);
        buf.append("蓝牙:");
        if (str.equals("0")) {
            buf.append("禁用");
        }
        else {
            buf.append("开启");
        }

        str = Secure.getString(context.getContentResolver(), Secure.BLUETOOTH_ON);
        buf.append("WIFI:");
        buf.append(str);

        str = Secure.getString(context.getContentResolver(), Secure.INSTALL_NON_MARKET_APPS);
        buf.append("APP位置来源:");
        buf.append(str);

        return buf.toString();
    }

    /**
     * 电话活动的状态
     * 
     * @return
     */
    public String getCallState() {
        switch (telephonyManager.getCallState()) {
        case android.telephony.TelephonyManager.CALL_STATE_IDLE:
            return "电话状态[CallState]: 挂断";
        case android.telephony.TelephonyManager.CALL_STATE_OFFHOOK:
            return "电话状态[CallState]: 接听";
        case android.telephony.TelephonyManager.CALL_STATE_RINGING:
            return "电话状态[CallState]: 来电";
        default:
            return "电话状态[CallState]: 未知";
        }
    }

    // 设置基本信息
    private void findData() {
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mIMEI = telephonyManager.getDeviceId();
        mMobileVersion = telephonyManager.getDeviceSoftwareVersion();
        mCellinfos = telephonyManager.getNeighboringCellInfo();
        mNetwrokIso = telephonyManager.getNetworkCountryIso();
        mSIM = telephonyManager.getSimSerialNumber();
        mDeviceID = getDeviceId();

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            // WIFI/MOBILE
            mNetType = info.getTypeName();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 获取设备号
    private String getDeviceId() {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    public String getNetwrokIso() {
        return mNetwrokIso;
    }

    public String getmDeviceID() {
        return mDeviceID;
    }

    public String getNetType() {
        return mNetType;
    }

    public String getImei() {
        return mIMEI;
    }

    public String getSIM() {
        return mSIM;
    }

    public String getUA() {
        return UA;
    }

}
