package com.dummy.wpb.wpc.utils.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Slf4j
@Component
public class WatchTask implements Runnable {
    @Autowired
    private BlockingQueue<String> blockingQueue;
    @Autowired
    private SyncTask deltaSyncTask;
    private boolean stopWatch;
    public WatchTask(){ this.stopWatch = false;}

    public void setStopWatch(boolean stopWatch){
        this.stopWatch = stopWatch;
        log.info("stop watch = {}", stopWatch);
    }

    public Object getStopWatch() {
        return this.stopWatch;
    }

    @Override
    public void run() {
        if(!stopWatch) {
            blockingQueue.add(deltaSyncTask.getTaskName());
            log.debug("queue {}", deltaSyncTask.getTaskName());
        }
    }
}
