package com.dummy.wpb.wpc.utils;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.ClusterConnectionMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Configuration
public class ApplicationConfig {
    @Value("${spring.data.mongodb.host}")
    private String mongoHost;
    @Value("#{${spring.data.mongodb.port}}")
    private int mongoPort;
    @Value("${spring.data.mongodb.database}")
    private String mongoDatabase;
    @Value("${spring.data.mongodb.username:}")
    private String mongoUsername;
    @Value("${spring.data.mongodb.password:}")
    private String mongoPassword;
    @Value("${spring.data.mongodb.enable-replica}")
    private boolean enableReplica;

    @Bean
    public BlockingQueue<String> getBlockingQueue() {
        return new LinkedBlockingDeque<>();
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix("Task Thread ");
        return threadPoolTaskScheduler;
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(ThreadPoolTaskExecutorConfiguration threadPoolTaskExecutorConfiguration) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(threadPoolTaskExecutorConfiguration.getCorePoolSize());
        threadPoolTaskExecutor.setMaxPoolSize(threadPoolTaskExecutorConfiguration.getMaxPoolSize());
        threadPoolTaskExecutor.setQueueCapacity(Integer.MAX_VALUE);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Bean
    public MongoClient mongoClient(){
        if("localhost".equals(mongoHost)){
            return MongoClients.create();
        }

        MongoCredential credential = MongoCredential.createCredential(mongoUsername, mongoDatabase, mongoPassword.toCharArray());
        ClusterConnectionMode connectionMode = enableReplica
                ? ClusterConnectionMode.MULTIPLE
                : ClusterConnectionMode.SINGLE;
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToSslSettings(builder -> {
                    builder.enabled(true);
                    builder.invalidHostNameAllowed(true);
                })
                .retryWrites(false)
                .applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(new ServerAddress(mongoHost, mongoPort))).mode(connectionMode))
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoDatabase mongoDatabase(MongoClient mongoClient){
        return mongoClient.getDatabase(mongoDatabase);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient){
        return new MongoTemplate(mongoClient, mongoDatabase);
    }
}