package com.hhhh.group.secwealth.mktdata.api.equity.common.token;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EtnetToken {
    private static String etnetToken;

    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    public static void setTokenWithLock(String token) {
        try {
            lock.writeLock().lock();
            etnetToken = token;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static String getTokenWithLock() throws InterruptedException {
        try {
            lock.readLock().lock();
            return etnetToken;
        } finally {
            lock.readLock().unlock();
        }
    }
}
