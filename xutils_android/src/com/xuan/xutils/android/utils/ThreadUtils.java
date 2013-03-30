/* 
 * @(#)ThreadUtils.java    Created on 2012-12-4
 * Copyright (c) 2012 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.xutils.android.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 用来执行任务的线程工具，内包含了一个线程池
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2012-12-4 上午10:05:03 $
 */
public abstract class ThreadUtils {

    /**
     * 初始化的线程数，有待历史的验证，暂时弄4个
     */
    public static ExecutorService threadPool = Executors.newFixedThreadPool(4);

    /**
     * 执行延迟任务，类似Timer的效果
     */
    public static ScheduledExecutorService scheduleThreadPool = Executors.newScheduledThreadPool(2);

    /**
     * 立即执行任务
     * 
     * @param task
     */
    public static void excute(Runnable task) {
        threadPool.execute(task);
    }

    /**
     * 延后执行任务
     * 
     * @param task
     * @param delay
     */
    public static void schedule(Runnable task, long delay) {
        scheduleThreadPool.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    public static void shutdownThreadPool() {
        threadPool.shutdown();
    }

    public static void shutdownScheduleThreadPool() {
        scheduleThreadPool.shutdown();
    }

}
