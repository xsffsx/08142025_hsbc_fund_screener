package com.hhhh.group.secwealth.mktdata.elastic.configuration;

import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import com.hhhh.group.secwealth.mktdata.elastic.properties.VolumeServiceProperties;
import com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants;
import com.hhhh.group.secwealth.mktdata.elastic.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.IllegalInitializationException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Data
@Slf4j
public class VolumeConfig {

    @Autowired
    private AppProperties appProperty;

    @Autowired
    private VolumeServiceProperties volumeServiceProperty;

    @Value("${predsrch.filePath}")
    private String filePath;

    @Value("${volumeService.retryTime}")
    private Integer retryTime;

    @Value("${predsrch.sourcePath}")
    private String sourceFilePath;

    private Map<String, String[]> privateKeyIdMap = new HashMap<>();

    private Map<String, String[]> privateKeyPathMap = new HashMap<>();

    private Map<String, String[]> remoteFilePathMap = new HashMap<>();

    private Map<String, String[]> remoteFileDirMap = new HashMap<>();

    private Map<String, String> hostMap = new HashMap<>();

    private Map<String, String> userMap = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            // set up remote File Path and privateKey Path
            for (String siteKey : appProperty.getSupportSites()) {
                if (CommonConstants.DEFAULT.equalsIgnoreCase(siteKey)) {
                    continue;
                }
                String remoteFilePathStr = this.volumeServiceProperty.getValue(siteKey,
                        VolumeServiceProperties.REMOTE_FILE_PATH_KEY);
                String privateKeyIdStr = this.volumeServiceProperty.getValue(siteKey,
                        VolumeServiceProperties.PRIVATEKEY_ID_KEY);
                String privateKeyPathStr = this.volumeServiceProperty.getValue(siteKey,
                        VolumeServiceProperties.PRIVATEKEY_PATH_KEY);
                String remoteFileDirStr = this.volumeServiceProperty.getValue(siteKey,
                        VolumeServiceProperties.REMOTE_FILE_DIR_KEY);
                String host = this.volumeServiceProperty.getValue(siteKey, "host");
                String user = this.volumeServiceProperty.getValue(siteKey, "user");
                if (StringUtil.isValid(remoteFilePathStr) && StringUtil.isValid(privateKeyIdStr)&& StringUtil.isValid(privateKeyPathStr)) {
                    this.remoteFilePathMap.put(siteKey, remoteFilePathStr.split(CommonConstants.SYMBOL_SEMICOLON));
                    this.privateKeyIdMap.put(siteKey, privateKeyIdStr.split(CommonConstants.SYMBOL_SEMICOLON));
                    this.privateKeyPathMap.put(siteKey, privateKeyPathStr.split(CommonConstants.SYMBOL_SEMICOLON));
                    this.remoteFileDirMap.put(siteKey, remoteFileDirStr.split(CommonConstants.SYMBOL_SEMICOLON));
                    this.hostMap.put(siteKey, host);
                    this.userMap.put(siteKey, user);
                }
            }
            // set up retryTime if can not Connect to wp sfpt server
            if (this.retryTime == null || this.retryTime.intValue() < 60) {
                this.retryTime = 60;
            }
            // create local wpc file save Path
            this.createFolder(this.filePath);
            if (StringUtil.isValid(this.sourceFilePath) && !this.sourceFilePath.equals(this.filePath)) {
                this.createFolder(this.sourceFilePath);
            }
        } catch (Exception e) {
            log.error("Can't init VolumeServiceImpl|exception=" + e.getMessage(), e);
            throw new IllegalInitializationException(ExCodeConstant.EX_CODE_ILLEGAL_INITIALIZATION);
        }
    }

    private void createFolder(final String path) {
        File fileFolder = new File(path);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
    }
}
