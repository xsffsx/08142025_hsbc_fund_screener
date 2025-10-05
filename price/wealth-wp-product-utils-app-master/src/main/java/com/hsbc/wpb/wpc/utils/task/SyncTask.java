package com.dummy.wpb.wpc.utils.task;

public interface SyncTask extends Runnable {
    String getTaskName();
    void setLockToken(String token);
}
