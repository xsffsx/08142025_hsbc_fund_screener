package com.hhhh.group.secwealth.mktdata.api.equity.quotes.scheduled.tradingsession;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.rbpTradingHour.RetrieveTradeDateInfoResponse;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TradingSessionSEHK {
    private static RetrieveTradeDateInfoResponse tradingSession;

    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    public static void setTradingSessionWithLock(RetrieveTradeDateInfoResponse tradingSessionSEHK) {
        try {
            lock.writeLock().lock();
            tradingSession = tradingSessionSEHK;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static RetrieveTradeDateInfoResponse getTradingSessionWithLock() {
        try {
            lock.readLock().lock();
            return tradingSession;
        } finally {
            lock.readLock().unlock();
        }
    }
}
