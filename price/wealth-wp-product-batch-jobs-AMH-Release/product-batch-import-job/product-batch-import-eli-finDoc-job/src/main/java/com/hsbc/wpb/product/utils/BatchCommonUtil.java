package com.dummy.wpb.product.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class BatchCommonUtil {

	private BatchCommonUtil() {
		throw new IllegalStateException("BatchCommonUtil class");
	}

	@SneakyThrows
	public static boolean renameFile(File file, String suffix) {
		File newFile = new File(file.getCanonicalPath() + suffix);
		return file.renameTo(newFile);
	}

	public static boolean executeOperationSystemCommand(String command) {
		// 1. process system command
		Process proc = null;
		int status = -1;
		log.info("Execute Operation System Command -> " + command);
		try {
			proc = Runtime.getRuntime().exec(command);//NOSONAR
			status = proc.waitFor();
		} catch (IOException e) {
			log.error("Execute Operation System Command ERROR ->  " + command, e);
		} catch (InterruptedException e) {
			log.error("Error InterruptedException, Command ->  " + command, e);
			Thread.currentThread().interrupt();
		}
		// 2. handle return result
        if (proc != null) {
            try (BufferedReader standardReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()))){
                String noramalMsg = null;
                String errorMsg = null;
                StringBuilder noramalMsgBuilder = new StringBuilder("Execute Operation System Command Successfully. ");
                StringBuilder errorMsgBuilder = new StringBuilder("Execute Operation System Command Failed, ");
                if (status == 0) {
                    while (null != (noramalMsg = standardReader.readLine())) {
                        noramalMsgBuilder.append(noramalMsg).append("; ");
                    }
                    log.info(noramalMsgBuilder.toString());
                } else {
                    while (null != (errorMsg = errorReader.readLine())) {
                        errorMsgBuilder.append(errorMsg).append("; ");
                    }
                    log.error(errorMsgBuilder.toString());
                }
            } catch (IOException e) {
                log.error("Execute Operation System Command ERROR ->  " + command, e);
            }
        }
        return status == 0;
	}

}
