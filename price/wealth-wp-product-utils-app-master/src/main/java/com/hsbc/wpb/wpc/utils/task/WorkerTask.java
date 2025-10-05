package com.dummy.wpb.wpc.utils.task;

import com.dummy.wpb.wpc.utils.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Component
public class WorkerTask implements Runnable {
    private static final String LOCK_NAME = "DATA_UTILS_TASK_LOCK";

    @Autowired
    private LockService lockService;

    @Autowired
    private BlockingQueue<String> blockingQueue;

    @Autowired
    private DeltaSyncTask deltaSyncTask;

    @Autowired
    private FullSyncTask fullSyncTask;
    @Autowired
    private CollectionSyncTask collectionSyncTask;

    @Autowired
    private CreateIndexesSyncTask createIndexesSyncTask;

    private static final Map<String, SyncTask> taskMap = new HashMap<>();

    @Value("#{${product.load.disable}}")
    private boolean disableDataLoad;

    @PostConstruct
    private void init(){
        taskMap.put(deltaSyncTask.getTaskName(), deltaSyncTask);
        taskMap.put(fullSyncTask.getTaskName(), fullSyncTask);
        taskMap.put(collectionSyncTask.getTaskName(), collectionSyncTask);
        taskMap.put(createIndexesSyncTask.getTaskName(), createIndexesSyncTask);
    }

    @Override
    public void run() {
        boolean exit = false;
        do {
            try {
                String taskName = blockingQueue.take();
                SyncTask task = taskMap.get(taskName);
                log.info("take {}, {} left", task.getTaskName(), blockingQueue.size());
                if(disableDataLoad&&taskName.equals(fullSyncTask.getTaskName())){
                    log.info("Data load is disabled");
                }else{
                    // a lock will be expired after 4 hour, since data load may take such a long time
                    String token = lockService.retrieveLock(LOCK_NAME);
                    if(null != token) {     // in case the lock is retrieved, there will be a token returned, which can be used in later release
                        try {
                            task.setLockToken(token);
                            task.run();
                        } finally {
                            lockService.releaseLock(LOCK_NAME);
                        }
                    }
                }
            }  catch (InterruptedException e) {
                log.warn("The WorkerTask has been interrupted");
                Thread.currentThread().interrupt();
            } catch (IllegalStateException e) {
                log.error("Exception: {}", e.getMessage());
                e.printStackTrace();
                exit = true;
            } catch (Exception e) {
                log.error("Exception: {}", e.getMessage());
                e.printStackTrace();
            }
        } while (!exit);
    }
}
