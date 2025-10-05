/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.handler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.constant.Constant;
import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.entity.DurationExCounter;

import lombok.Setter;

public class BMCHelper {

    private static final Logger logger = LoggerFactory.getLogger(BMCHelper.class);

    @Setter
    private String defaultExCode;

    public void removeExpiredExceptions(final List<DurationExCounter> exceptions, final int duration) {
        final GregorianCalendar currentTime = new GregorianCalendar();
        if (duration > 0) {
            currentTime.add(Calendar.SECOND, (-1) * duration);
            final Iterator<DurationExCounter> it = exceptions.iterator();
            while (it.hasNext()) {
                final DurationExCounter exception = it.next();
                final GregorianCalendar exTime = exception.getTime();
                if (currentTime.after(exTime)) {
                    it.remove();
                }
            }
        }
    }

    public void addCurrentException(final List<DurationExCounter> exceptions, final Throwable e) {
        exceptions.add(new DurationExCounter(e));
    }

    public void clearExceptions(final List<DurationExCounter> exceptions) {
        exceptions.clear();
    }

    public void genBMCFile(final String filePath, final String cmd) throws IOException {
        if (createFolderIfNotExist(filePath)) {
            final String timestamp = new SimpleDateFormat(Constant.BMC_FILE_DATE_PATTERN, Locale.ENGLISH).format(new Date());
            final String bmcFileName = Constant.BMC_FILE_PATTERN.replace(Constant.PALCEHOLDER_TIMESTAMP, timestamp);
            final StringBuilder sb = new StringBuilder();
            sb.append(filePath).append(bmcFileName);
            final String tmpFileName = sb.toString() + ".tmp";
            final String targetFileName = sb.toString() + ".RDY";
            try {
                FileUtils.writeStringToFile(new File(tmpFileName), cmd, StandardCharsets.UTF_8, true);
            } catch (IOException e) {
                BMCHelper.logger.error("Write BMC file encounter error", e);
                throw new IOException(this.defaultExCode);
            }
            final File tmpFile = new File(tmpFileName);
            final File targetFile = new File(targetFileName);
            if (tmpFile.isFile()) {
                if (!tmpFile.renameTo(targetFile)) {
                    BMCHelper.logger.error("Rename BMC file encounter error");
                    throw new IOException(this.defaultExCode);
                }
            } else {
                BMCHelper.logger.error(tmpFileName + " don't exist");
                throw new IOException(this.defaultExCode);
            }
        } else {
            BMCHelper.logger.error("Can't access BMC folder");
            throw new IOException(this.defaultExCode);
        }
    }

    private boolean createFolderIfNotExist(final String cmdFilePath) {
        final File file = new File(cmdFilePath);
        final boolean exists = file.exists();
        if (!exists) {
            return file.mkdirs();
        }
        return exists;
    }

    public String getBmcFilePath(final String bmcFilePath) {
        return bmcFilePath.replace(Constant.PLACEHOLDER_INSTANCE_NAME, getServerName());
    }

    private String getServerName() {
        String serverName;
        try {
            serverName = new InitialContext().lookup(Constant.SERVER_NAME).toString();
        } catch (NamingException e) {
            BMCHelper.logger.error("Can't get server name", e);
            serverName = "";
        }
        return serverName;
    }

}
