/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ServerLauncher {

    public static void main(final String[] args) {
        SpringApplication.run(ServerLauncher.class, args);
    }

}