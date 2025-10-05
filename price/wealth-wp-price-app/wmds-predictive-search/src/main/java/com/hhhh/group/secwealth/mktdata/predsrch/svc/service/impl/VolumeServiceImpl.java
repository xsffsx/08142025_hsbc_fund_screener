/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.service.impl;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.vfs2.FileSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.VolumeService;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.FileAccessHelper;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.VolumeServiceConfig;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.WpcFileUtil;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.WpcFileUtil.OutFileFilter;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.WpcFileUtil.WpcOutFileNameComprator;

@Service("volumeService")
public class VolumeServiceImpl implements VolumeService {


    /** The support sites. */
    @Value("#{systemConfig['app.supportSites']}")
    private String[] supportSites;

    /** The site converter rule. */
    @Value("#{systemConfig['predsrch.siteLoadInterfaceRule']}")
    private String[] siteConverterRule;

    /** The file path. */
    @Value("#{systemConfig['predsrch.filePath']}")
    private String filePath;

    @Value("#{systemConfig['volumeService.retryTime']}")
    private Integer retryTime;

    @Value("#{systemConfig['predsrch.sourcePath']}")
    private String sourceFilePath;

    private Map<String, String[]> privateKeyPathMap = new HashMap<String, String[]>();

    private Map<String, String[]> remoteFilePathMap = new HashMap<String, String[]>();

    @Autowired
    @Qualifier("volumeServiceConfig")
    private VolumeServiceConfig volumeServiceConfig;


    @PostConstruct
    public void init() throws Exception {
        try {
            // set up remote File Path and privateKey Path
            for (String siteKey : this.supportSites) {
                if (CommonConstants.DEFAULT.equalsIgnoreCase(siteKey)) {
                    continue;
                }
                String remoteFilePathStr = this.volumeServiceConfig.getValue(siteKey, VolumeServiceConfig.REMOTE_FILE_PATH_KEY);
                String privateKeyPathStr = this.volumeServiceConfig.getValue(siteKey, VolumeServiceConfig.PRIVATEKEY_PATH_KEY);
                if (StringUtil.isValid(remoteFilePathStr) && StringUtil.isValid(privateKeyPathStr)) {
                    this.remoteFilePathMap.put(siteKey, remoteFilePathStr.split(CommonConstants.SYMBOL_SEMICOLON));
                    this.privateKeyPathMap.put(siteKey, privateKeyPathStr.split(CommonConstants.SYMBOL_SEMICOLON));
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
            LogUtil.error(VolumeServiceImpl.class, "Can't init VolumeServiceImpl|exception=" + e.getMessage(), e);
            throw new SystemException(ErrTypeConstants.SYSTEM_INIT_ERROR, e);
        }
    }

    public void copyToLocalData() throws Exception {
        LogUtil.info(VolumeServiceImpl.class, "volumeService: copy to local data start={}", System.currentTimeMillis());
        boolean sucAllConnect = true;
        boolean sucAllDownload = true;
        for (String site : this.supportSites) {
            String remoteOutFile = null;

            try {
                // ignore default site
                if (CommonConstants.DEFAULT.equalsIgnoreCase(site)) {
                    LogUtil.debug(VolumeServiceImpl.class, "volumeService: skip the DEFAULT site", System.currentTimeMillis());
                    continue;
                }
                String volumeServiceEnabled = this.volumeServiceConfig.getValue(site, VolumeServiceConfig.VOLUME_SERVICE_ENABLED);
                boolean isVolumeServiceEnabled = volumeServiceEnabled != null ? Boolean.parseBoolean(volumeServiceEnabled) : false;
                LogUtil.info(VolumeServiceImpl.class, "Site: {}, volumeService is {}", site,
                    isVolumeServiceEnabled == true ? "Enabled" : "Disabled");
                if (!isVolumeServiceEnabled) {
                    continue;
                }

                if (this.privateKeyPathMap.size() == 0 || this.remoteFilePathMap.size() == 0) {
                    LogUtil.error(VolumeServiceImpl.class,
                        "VolumeService is not config SFPT Private Key Path and Remote File Path!");
                    throw new CommonException(PredictiveSearchConstant.ERRORMSG_VOLUMESERVICEERR);
                }

                String[] privateKeyPathArray = this.privateKeyPathMap.get(site);
                String[] remoteFilePathArray = this.remoteFilePathMap.get(site);
                if (privateKeyPathArray == null || privateKeyPathArray.length == 0 || remoteFilePathArray == null
                    || remoteFilePathArray.length == 0) {
                    LogUtil.error(VolumeServiceImpl.class,
                        "Site: {}, volumeService is not config SFPT Private Key Path and Remote File Path", site);
                    continue;
                }

                String privateKeyPath = null;
                String remoteFilePath = null;

                Set<String> siteSet = new HashSet<String>();
                siteSet.add(site);
                if (this.siteConverterRule != null && this.siteConverterRule.length > 0) {
                    for (String rule : this.siteConverterRule) {
                        String str = rule.substring(0, rule.indexOf(CommonConstants.SYMBOL_VERTICAL));
                        if (site.equals(str)) {
                            String[] array = rule.split(CommonConstants.SYMBOL_SEPARATOR);
                            for (int i = 1; i < array.length; i++) {
                                siteSet.add(array[i]);
                            }
                            break;
                        }
                    }
                }

                for (int i = 0; i < privateKeyPathArray.length; i++) {
                    try {
                        // privateKeyPath =
                        // this.getClass().getClassLoader().getResource(privateKeyPathArray[i]).getPath();
                        privateKeyPath = ResourceUtils.getFile(privateKeyPathArray[i]).getPath();
                    } catch (Exception e) {
                        LogUtil.error(VolumeServiceImpl.class,
                            "Can not find privateKey: " + privateKeyPathArray[i] + " siteKey: " + site, e);
                    }
                    boolean isConnect = FileAccessHelper.isConnect(privateKeyPath, remoteFilePathArray[i], this.retryTime);
                    if (isConnect) {
                        remoteFilePath = remoteFilePathArray[i];
                        break;
                    } else {
                        sucAllConnect = false;
                    }
                }

                if (StringUtil.isInvalid(privateKeyPath) || StringUtil.isInvalid(remoteFilePath)) {
                    continue;
                }
                for (String siteKey : siteSet) {
                    remoteOutFile = this.copyOutFile(siteKey, privateKeyPath, remoteFilePath);

                    if (StringUtil.isValid(remoteOutFile)) {
                        String pattern =
                            WpcFileUtil.XML_FILE_PATTERN.replace(WpcFileUtil.SITE, siteKey + CommonConstants.SYMBOL_UNDERLINE)
                                .replace(WpcFileUtil.FILE_TIME, WpcFileUtil.getTime(remoteOutFile));
                        String[] remoteXmlFileArray = FileAccessHelper.getChildren(privateKeyPath, remoteFilePath, pattern);
                        if (remoteXmlFileArray != null && remoteXmlFileArray.length > 0) {
                            String[] dataTypeArray = this.getDataTypeArray(siteKey);
                            // Download xml file
                            for (String remoteXmlFile : remoteXmlFileArray) {
                                if (dataTypeArray != null && dataTypeArray.length > 0) {
                                    for (String dataType : dataTypeArray) {
                                        if (remoteXmlFile.indexOf(dataType) != -1) {
                                            downloadXmlFile(remoteXmlFile, privateKeyPath, siteKey);
                                        }
                                    }
                                } else {
                                    downloadXmlFile(remoteXmlFile, privateKeyPath, siteKey);
                                }
                            }

                            // Download out file
                            String localOutFilePath =
                                this.getFileSavePath(siteKey) + remoteOutFile.substring(remoteOutFile.lastIndexOf("/"));
                            File outFile = new File(localOutFilePath);
                            FileAccessHelper.copyToLocal(privateKeyPath, remoteOutFile, outFile.getAbsolutePath());
                            LogUtil.warn(VolumeServiceImpl.class, "siteKey: {}, Download out file from: {} to :{}", siteKey,
                                remoteOutFile, outFile.getAbsolutePath());
                        }
                    }
                }
            } catch (Exception e) {
                LogUtil.error(VolumeServiceImpl.class, "copyToLocalData error: site= " + site + ", OutFileName: " + remoteOutFile,
                    e);
                sucAllDownload = false;
            }
        }
        if (!sucAllConnect || !sucAllDownload) {
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_VOLUMESERVICEERR);
        }
        LogUtil.info(VolumeServiceImpl.class, "volumeService: copy to local data end={}", System.currentTimeMillis());
    }

    private void downloadXmlFile(final String remoteXmlFile, final String privateKeyPath, final String siteKey) throws Exception {
        String localXmlFile = remoteXmlFile.substring(remoteXmlFile.lastIndexOf("/"));
        String localXmlFilePath = this.getFileSavePath(siteKey) + localXmlFile;
        File xmlFile = new File(localXmlFilePath);
        FileAccessHelper.copyToLocal(privateKeyPath, remoteXmlFile, xmlFile.getAbsolutePath());
        LogUtil.warn(VolumeServiceImpl.class, "siteKey: {}, Download xml file from: {} to :{}", siteKey, remoteXmlFile,
            xmlFile.getAbsolutePath());

        String verify = this.volumeServiceConfig.getValue(siteKey, VolumeServiceConfig.PREDSRCH_IS_VERIFY);
        boolean isVerify = verify != null ? Boolean.parseBoolean(verify) : false;
        if (isVerify) {
            String remoteMD5File = remoteXmlFile + WpcFileUtil.MD5_FILE_EXTENSION;
            String localMD5FilePath = this.getFileSavePath(siteKey) + remoteMD5File.substring(remoteMD5File.lastIndexOf("/"));
            File md5File = new File(localMD5FilePath);
            FileAccessHelper.copyToLocal(privateKeyPath, remoteMD5File, md5File.getAbsolutePath());
            LogUtil.warn(VolumeServiceImpl.class, "siteKey: {}, Download MD5 file from: {} to :{}", siteKey, remoteMD5File,
                md5File.getAbsolutePath());
        }
    }

    private String copyOutFile(final String siteKey, final String privateKeyPath, final String remoteFilePath) throws Exception {
        String copyOutFile = "";
        long localOutFileCreateTime = 0L;
        long sfptOutFileCreateTime = 0L;

        // local Out File
        File file = new File(this.getFileSavePath(siteKey));
        String[] localOutFileArray = file.list(new OutFileFilter(siteKey));
        if (localOutFileArray != null && localOutFileArray.length > 0) {
            if (localOutFileArray.length > 1) {
                Arrays.sort(localOutFileArray, new WpcOutFileNameComprator());
            }
            String localOutFile = localOutFileArray[0];
            LogUtil.debug(VolumeServiceImpl.class, "siteKey: {}, local Out File name: {}", siteKey, localOutFile);
            localOutFileCreateTime = WpcFileUtil.converOutFileNameToTimestamp(localOutFile);
        }
        LogUtil.debug(VolumeServiceImpl.class, "siteKey: {}, local Out File localOutFileCreateTime: {}", siteKey,
            localOutFileCreateTime);

        // remote Out File
        try {
            String[] remoteOutFileArray = FileAccessHelper.getChildren(privateKeyPath, remoteFilePath,
                WpcFileUtil.ALL_SITE_OUT_FILE_PATTERN.replace(WpcFileUtil.SITE, siteKey + CommonConstants.SYMBOL_UNDERLINE));
            if (remoteOutFileArray != null && remoteOutFileArray.length > 0) {
                if (remoteOutFileArray.length > 1) {
                    Arrays.sort(remoteOutFileArray, new WpcOutFileNameComprator());
                }
                String remoteOutFile = remoteOutFileArray[0];
                LogUtil.debug(VolumeServiceImpl.class, "siteKey: {}, remote Out File name: {}", siteKey, remoteOutFile);
                sfptOutFileCreateTime = WpcFileUtil.converOutFileNameToTimestamp(remoteOutFile);
                if (sfptOutFileCreateTime > localOutFileCreateTime) {
                    copyOutFile = remoteOutFile;
                }
            }
        } catch (FileSystemException e) {
            LogUtil.error(VolumeServiceImpl.class,
                "siteKey: " + siteKey + "Get sfpt out file list error: sftpFilePath: " + remoteFilePath, e);
            throw e;
        }
        LogUtil.debug(VolumeServiceImpl.class, "siteKey: {}, remote Out File remoteOutFileCreateTime: {}", siteKey,
            sfptOutFileCreateTime);

        return copyOutFile;
    }

    @Override
    public void isConnectBySite(final String siteKey) throws Exception {
        String[] privateKeyPathArray = this.privateKeyPathMap.get(siteKey);
        String[] remoteFilePathArray = this.remoteFilePathMap.get(siteKey);
        // String privateKeyPath =
        // this.getClass().getClassLoader().getResource(privateKeyPathArray[0]).getPath();
        String privateKeyPath = ResourceUtils.getFile(privateKeyPathArray[0]).getPath();
        FileAccessHelper.connect(privateKeyPath, remoteFilePathArray[0]);
    }

    /**
     * Get the xml file data Type Setting
     */
    private String[] getDataTypeArray(final String site) throws Exception {
        String[] dataTypeArray = null;
        String dataTypeStr = this.volumeServiceConfig.getValue(site, VolumeServiceConfig.VOLUME_SERVICE_DATATYPE);
        if (StringUtil.isValid(dataTypeStr)) {
            dataTypeArray = dataTypeStr.split(CommonConstants.SYMBOL_COMMA);
        }
        return dataTypeArray;
    }

    private void createFolder(final String path) {
        File fileFolder = new File(path);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
    }

    private String getFileSavePath(final String site) throws Exception {
        String verify = this.volumeServiceConfig.getValue(site, VolumeServiceConfig.PREDSRCH_IS_VERIFY);
        boolean isVerify = verify != null ? Boolean.parseBoolean(verify) : false;
        if (isVerify) {
            return this.sourceFilePath;
        }
        return this.filePath;
    }
}
