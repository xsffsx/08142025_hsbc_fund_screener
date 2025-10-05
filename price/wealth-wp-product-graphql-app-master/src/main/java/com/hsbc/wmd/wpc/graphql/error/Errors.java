package com.dummy.wmd.wpc.graphql.error;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

@Slf4j
public class Errors {

    private Errors() {}

    public static void log(ErrorCode code, String message) {
        log.error("{}: {}", code, message);
    }
    
    public static void log(ErrorCode code, String message, Throwable e) {
        log.error(String.format("%s: %s", code, message), e);
    }
    
    public static void log(ErrorCode code, String message, Logger logger) {
        logger.error("{}: {}", code, message);
    }

    public static void log(ErrorCode code, String message, Throwable e, Logger logger) {
        String msg = String.format("%s: %s", code, message);
        logger.error(msg, e);
    }
}
