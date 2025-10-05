package com.dummy.wmd.wpc.graphql;

import graphql.execution.instrumentation.InstrumentationState;

public class LogState implements InstrumentationState {
    private final long startTimeMillis;

    public LogState(){
        startTimeMillis = System.currentTimeMillis();
    }

    public long getMillisCost(){
        return System.currentTimeMillis() - startTimeMillis;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }
}
