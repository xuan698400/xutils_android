/* 
 * @(#)ImageUtils.java    Created on 2006-4-13
 * Copyright (c) 2005 ZDSoft.net, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.xutils.android.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.xuan.autils.R;
import com.xuan.xutils.android.common.Constants;

/**
 * 图片工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-1-15 下午5:16:43 $
 */
public abstract class ImageUtils {

    private static Paint textPaintWhite;
    static {
        textPaintWhite = new Paint();
        textPaintWhite.setColor(Color.WHITE);
        textPaintWhite.setTextAlign(Align.CENTER);
        textPaintWhite.setTextSize(18);
    }

    /**
     * 等比例修改图片大小，目标图片的大小不会超过指定的宽、高，以小的为准
     * 
     * @param src
     *            源图片的路径
     * @param dest
     *            目标图片的路径
     * @param width
     *            宽
     * @param height
     *            高
     * @throws IOException
     */
    public static void changeOppositeSize(InputStream src, String dest, int newWidth, int newHeight) {
        Bitmap bitmapSrc = BitmapFactory.decodeStream(src);
        try {
            src.close();
        }
        catch (IOException e) {
            // Ignore
        }
        int w = bitmapSrc.getWidth();
        int h = bitmapSrc.getHeight();

        // 若宽高小于指定最大值，不需重新绘制
        Bitmap bitmap = null;
        if (w <= newWidth && h <= newHeight) {
            bitmap = bitmapSrc;
        }
        else {
            float scale = ((float) newWidth / w) > ((float) newHeight / h) ? ((float) newHeight / h)
                    : ((float) newWidth / w);
            newWidth = (int) (w * scale);
            newHeight = (int) (h * scale);
            if (newWidth <= 0) {
                newWidth = 1;
            }
            if (newHeight <= 0) {
                newHeight = 1;
            }

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            bitmap = Bitmap.createBitmap(bitmapSrc, 0, 0, w, h, matrix, true);
        }

        OutputStream out = null;
        try {
            File file = new File(dest);
            createParentDirs(file);
            out = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(CompressFormat.JPEG, 70, out);
        }
        catch (IOException e) {
            Log.e(Constants.TAG, "", e);
        }
        finally {
            try {
                out.flush();
                out.close();
            }
            catch (IOException e) {
                // Ignore
            }
        }
    }

    /**
     * 在图片上绘制未读消息数
     * 
     * @param baseDrawable
     * @param unreadedNum
     * @return
     */
    public static Drawable getUnreadedDrawable(Drawable baseDrawable, Resources resources, int unreadedNum) {
        Bitmap bitmap = Bitmap.createBitmap(baseDrawable.getIntrinsicWidth(), baseDrawable.getIntrinsicHeight(),
                baseDrawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        baseDrawable.setBounds(0, 0, baseDrawable.getIntrinsicWidth(), baseDrawable.getIntrinsicHeight());
        baseDrawable.draw(canvas);

        Bitmap newBitmap = BitmapFactory.decodeResource(resources, R.drawable.msg_new);
        int left = bitmap.getWidth() - newBitmap.getWidth();// left表示从左边那个位置开始画起

        canvas.drawBitmap(newBitmap, left, 0, new Paint());// 画上红色的圈圈

        int x = (int) (bitmap.getWidth() - newBitmap.getWidth() / 2d);
        int y = (int) (newBitmap.getWidth() * 0.8d);
        String text = unreadedNum > 9 ? "n" : String.valueOf(unreadedNum);
        canvas.drawText(text, x, y, textPaintWhite);

        BitmapDrawable ret = new BitmapDrawable(resources, bitmap);

        return ret;
    }

    /**
     * 在Bitmap图片上画未读消息
     * 
     * @param bitmap
     * @param resources
     * @param unreadedNum
     * @return
     */
    public static Bitmap getUnreadedBitmap(Bitmap bitmap, Resources resources, int unreadedNum) {
        BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, bitmap);
        BitmapDrawable ret = (BitmapDrawable) getUnreadedDrawable(bitmapDrawable, resources, unreadedNum);
        return ret.getBitmap();
    }

    /**
     * 如果父目录不存在，则创建之。
     * 
     * @param file
     *            文件
     */
    private static void createParentDirs(File file) {
        File parentPath = file.getParentFile();
        if (!parentPath.exists() || !parentPath.isDirectory()) {
            parentPath.mkdirs();
        }
    }

}
