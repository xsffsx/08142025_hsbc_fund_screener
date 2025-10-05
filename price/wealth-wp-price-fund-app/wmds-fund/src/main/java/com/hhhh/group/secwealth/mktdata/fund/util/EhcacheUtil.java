package com.hhhh.group.secwealth.mktdata.fund.util;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheUtil {

    public CacheManager manager;

    public static String resourceName;

    public static volatile EhcacheUtil ehCache;

    private EhcacheUtil(final String resourceName) {

        LogUtil.info(EhcacheUtil.class, "Instantiating MDSBECacheManager with resourceName: {}", resourceName);
        this.manager = CacheManager.create(this.getClass().getResource(resourceName));
        String[] cacheNames = this.manager.getCacheNames();
        LogUtil.info(EhcacheUtil.class, "Available Cache: {}", Arrays.toString(cacheNames));
        this.resourceName = resourceName;
    }


    public static EhcacheUtil getInstance(final String resourceName) {
        synchronized (EhcacheUtil.class) {
            if (ehCache == null) {
                LogUtil.info(EhcacheUtil.class, "manager not found again for resourceName: {}! going to instance", resourceName);
                ehCache = new EhcacheUtil(resourceName);
            }
        }
        return ehCache;
    }



    public int getcacheSize(final Cache cache) {
        List<String> keys = cache.getKeys();
        for (String key : keys) {
            cache.get(key);
        }
        return cache.getSize();
    }


    public void put(String cacheName, String key, Object value, boolean eternal, int time) {
        Cache cache = manager.getCache(cacheName);
        Element element = new Element(key, value);
        element.setEternal(eternal);
        element.setTimeToLive(time);
        cache.put(element);
    }


    public Object get(String cacheName, String key) {
        Cache cache = manager.getCache(cacheName);
        Element element = cache.get(key);
        return element == null ? null : element.getObjectValue();

    }


    public Cache get(String cacheName) {
        return manager.getCache(cacheName);

    }


    public String getMemory(String cacheName) {
        long memory = manager.getCache(cacheName).calculateInMemorySize();
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (memory < 1024) {
            fileSizeString = df.format((double) memory) + "B";
        } else if (memory < 1048576) {
            fileSizeString = df.format((double) memory / 1024) + "K";
        } else if (memory < 1073741824) {
            fileSizeString = df.format((double) memory / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) memory / 1073741824) + "G";
        }
        return fileSizeString;
    }


    public void remove(String cacheName, String key) {
        Cache cache = manager.getCache(cacheName);
        cache.removeAll();
    }
}
