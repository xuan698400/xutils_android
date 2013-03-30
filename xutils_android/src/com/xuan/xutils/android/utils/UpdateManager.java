/* 
 * @(#)UpdateManager.java    Created on 2011-12-20
 * Copyright (c) 2011 ZDSoft Networks, Inc. All rights reserved.
 * $Id: UpdateManager.java 35745 2013-03-12 01:20:28Z xuan $
 */
package com.xuan.xutils.android.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.xuan.xutils.android.common.Constants;

/**
 * 更新应用的工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-3-25 上午9:28:20 $
 */
public class UpdateManager {
    private final Context mContext;

    // 提示语
    private final String updateMsg;

    // 返回的安装包url
    private final String apkUrl;

    private Dialog noticeDialog;// 提示是否下载的对话框

    // 下载包安装文件夹路径
    private static final String savePath = Constants.UPDATE_APK_PATH;
    private static final String saveFileName = savePath + Constants.APK_NAME;// apk文件名

    private ProgressDialog updateProgress;// 更新进度条
    private int progress;// 进度值

    private static final int DOWN_UPDATE = 1;// 正在下载标识
    private static final int DOWN_OVER = 2;// 下载完成标识

    private Thread downLoadThread;// 下载apk的线程

    private final boolean interceptFlag = false;// 是否取消下载

    private final Handler mHandler = new Handler() {// 更新下载进度条的handler
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case DOWN_UPDATE:
                updateProgress.setProgress(progress);
                break;
            case DOWN_OVER:
                updateProgress.dismiss();
                installApk();
                break;
            default:
                break;
            }
        };
    };

    public UpdateManager(Context context, String apkUrl, String updateText) {
        this.mContext = context;
        this.apkUrl = apkUrl;
        this.updateMsg = updateText;
    }

    /**
     * 外部接口让主Activity调用-进行更新
     */
    public void checkUpdateInfo() {
        showNoticeDialog();
    }

    /**
     * 外部接口让主Activity调用-进行下载安装
     */
    public void downloadAndInstallApk() {
        showIsDownloadAndInstallDialog();
    }

    // 更新前让用户选择是否更新的对话框
    private void showNoticeDialog() {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle("软件版本更新");
        builder.setMessage(updateMsg);
        builder.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        builder.setPositiveButton("现在更新", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                update();
            }
        }).setNegativeButton("以后再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        noticeDialog = builder.create();
        noticeDialog.show();
    }

    // 更新操作
    private void update() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // sdcard 不可用
            ToastUtils.displayTextShort(mContext, "SD卡不可用，无法下载更新，请安装SD卡后再试。");
            return;
        }

        // 显示更新进度
        updateProgress = new ProgressDialog(mContext);
        updateProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        updateProgress.setTitle("软件更新");
        updateProgress.setCancelable(false);
        updateProgress.show();
        downloadApk();
    }

    // 下载apk任务
    private final Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                // 为什么呢，因为有些apk的下载获取不到conn.getContentLength();
                // URL url = new URL(apkUrl);
                //
                // HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // conn.connect();
                // int length = conn.getContentLength();
                // InputStream is = conn.getInputStream();

                HttpGet getMethod = new HttpGet(apkUrl);
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(getMethod);
                HttpEntity httpEntity = response.getEntity();
                InputStream is = httpEntity.getContent();
                long length = httpEntity.getContentLength();

                // 创建件文件夹
                File file = new File(savePath);
                if (!file.exists()) {
                    boolean success = file.mkdirs();
                    if (!success) {
                        Log.e(Constants.TAG, "mkdirs failed");
                    }
                }

                // 创建文件
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                if (!ApkFile.exists()) {
                    ApkFile.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(ApkFile);

                // 从输入流中读取字节数据，写到文件中
                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                }
                while (!interceptFlag);// 点击取消就停止下载.

                fos.close();
                is.close();
            }
            catch (Exception e) {
                Log.e(Constants.TAG, "", e);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.displayTextShort(mContext, "下载包时发生错误。");
                        updateProgress.dismiss();
                    }
                });
            }
        }
    };

    // 下载apk
    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    // 安装apk
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    // -------------------------------------以下是下载安装的方法---------------------------------------------------

    // 更新前让用户选择是否更新的对话框
    private void showIsDownloadAndInstallDialog() {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle("组件安装");
        builder.setMessage("系统还没安装该组件，请安装！");

        builder.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("现在安装", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // 判断sd是否存在
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    ToastUtils.displayTextShort(mContext, "SD卡不可用，无法下载安装，请安装SD卡后再试。");
                    return;
                }

                // 显示更新进度
                updateProgress = new ProgressDialog(mContext);
                updateProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                updateProgress.setTitle("软件下载安装");
                updateProgress.setCancelable(false);
                updateProgress.show();
                downloadApk();
            }
        }).setNegativeButton("以后再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        noticeDialog = builder.create();
        noticeDialog.show();
    }

}
