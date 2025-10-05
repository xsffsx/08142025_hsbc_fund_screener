

package com.hhhh.group.secwealth.mktdata.fund.util;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorUtil {
private static final int DEFAULT_CORE_SIZE = 15;
    private static final int MAX_QUEUE_SIZE = 30;
    private volatile static ThreadPoolExecutor executor; 

    private ThreadPoolExecutorUtil() {}; 


    public static ThreadPoolExecutor getInstance() {
        if (executor == null) {
            synchronized (ThreadPoolExecutorUtil.class) {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(DEFAULT_CORE_SIZE,//core thread size
                    MAX_QUEUE_SIZE,
                    Integer.MAX_VALUE, 
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingDeque<Runnable>(Integer.MAX_VALUE),
                    Executors.defaultThreadFactory()
                    );
                }
            }
        }
        return executor;
    }
}