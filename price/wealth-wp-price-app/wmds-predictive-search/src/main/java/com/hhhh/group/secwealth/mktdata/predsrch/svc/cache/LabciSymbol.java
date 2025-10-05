package com.hhhh.group.secwealth.mktdata.predsrch.svc.cache;

import com.hhhh.group.secwealth.mktdata.predsrch.pres.beans.PredSrchResponse;
import lombok.Getter;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class LabciSymbol implements Delayed {

    private final long expiryTime;

    @Getter
    private final String symbol;

    @Getter
    private final PredSrchResponse predSrchResponse;

    public LabciSymbol(String symbol, PredSrchResponse predSrchResponse, long expiryTime) {
        this.symbol = symbol;
        this.predSrchResponse = predSrchResponse;
        this.expiryTime = expiryTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(expiryTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(expiryTime, ((LabciSymbol) o).expiryTime);
    }

}
