package com.hhhh.group.secwealth.mktdata.predsrch.svc.cache;

import com.hhhh.group.secwealth.mktdata.predsrch.pres.beans.PredSrchResponse;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

public class LabciSymbolSearchCache {

    private static LabciSymbolSearchCache INSTANCE;

    private final ConcurrentHashMap<String, PredSrchResponse> cachedSymbols;

    private final DelayQueue<LabciSymbol> cleaningUpQueue;

    private LabciSymbolSearchCache() {
        cachedSymbols = new ConcurrentHashMap<>();
        cleaningUpQueue = new DelayQueue<>();
        initCleanerThread();
    }

    public static LabciSymbolSearchCache getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new LabciSymbolSearchCache();
        }

        return INSTANCE;
    }

    private void initCleanerThread() {
        Thread cleanerThread = new Thread(() -> {
            LabciSymbol labciSymbol;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    labciSymbol = this.cleaningUpQueue.take();
                    this.cachedSymbols.remove(labciSymbol.getSymbol(), labciSymbol.getPredSrchResponse());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        cleanerThread.setDaemon(true);
        cleanerThread.start();
    }

    public void add(String symbol, PredSrchResponse predSrchResponse, long time, TimeUnit timeUnit) {
        if (symbol == null) {
            return;
        }
        if (predSrchResponse == null) {
            cachedSymbols.remove(symbol);
        } else {
            long expiryTime = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(time, timeUnit);
            cachedSymbols.put(symbol, predSrchResponse);
            cleaningUpQueue.put(new LabciSymbol(symbol, predSrchResponse, expiryTime));
        }
    }

    public void add(String symbol, PredSrchResponse predSrchResponse) {
        add(symbol, predSrchResponse, 4L, TimeUnit.HOURS);
    }

    public void removeAll() {
        cachedSymbols.clear();
        cleaningUpQueue.clear();
    }

    public int getCacheSize() {
        return cachedSymbols.size();
    }

    public PredSrchResponse get(String symbol) {
        return cachedSymbols.get(symbol);
    }
}
