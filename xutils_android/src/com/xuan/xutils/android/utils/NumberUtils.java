/* 
 * @(#)NumberUtils.java    Created on 2013-2-4
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.xutils.android.utils;

/**
 * 一个简单的数字工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-2-4 下午1:00:55 $
 */
public abstract class NumberUtils {

    private static int num = 0;

    /**
     * 获取一个相对不重复的数
     * 
     * @return
     */
    public static int getNum() {
        if (num >= Integer.MAX_VALUE) {
            num = 0;
        }

        return num++;
    }

}
