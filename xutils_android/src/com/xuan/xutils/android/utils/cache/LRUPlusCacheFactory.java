/* 
 * @(#)LRUPlusCacheFactory.java    Created on 2013-3-14
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.xutils.android.utils.cache;

import java.util.HashMap;

/**
 * LRUPlusCache的工厂类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-3-14 下午6:23:07 $
 */
public abstract class LRUPlusCacheFactory {
    private static final int DEFAULT_CACHE_SIZE = 20;// 默认的缓存容量

    private static final LRUPlusCache defaultCache = new LRUPlusCache(DEFAULT_CACHE_SIZE);
    private static final HashMap<String, LRUPlusCache> cachePool = new HashMap<String, LRUPlusCache>();

    /**
     * 获取默认容量的缓存
     * 
     * @return
     */
    public static LRUPlusCache getDefaultCache() {
        return defaultCache;
    }

    /**
     * 初始化一个缓存到缓存池中
     * 
     * @param size
     * @param cacheId
     */
    public static void initCache(int size, String cacheId) {
        cachePool.put(cacheId, new LRUPlusCache(size));
    }

    /**
     * 从缓存池中获取一个缓存，如果之前没有，那返回null
     * 
     * @param cacheId
     */
    public static void getCache(String cacheId) {
        cachePool.get(cacheId);
    }

    /**
     * 判断缓存是否存在缓存池中
     * 
     * @param cacheId
     * @return
     */
    public static boolean isCacheExits(String cacheId) {
        return cachePool.containsKey(cacheId);
    }

}
