/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.VerifyFilesService;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.VolumeService;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.MD5Util;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.VolumeServiceConfig;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.WpcFileUtil;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.WpcFileUtil.OutFileFilter;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.WpcFileUtil.WpcFilenameFilter;

@Service("verifyFilesService")
public class VerifyFilesServiceImpl implements VerifyFilesService {

    /** The support sites. */
    @Value("#{systemConfig['app.supportSites']}")
    private String[] supportSites;

    /** The site converter rule. */
    @Value("#{systemConfig['predsrch.siteLoadInterfaceRule']}")
    private String[] siteConverterRule;

    /** The file path. */
    @Value("#{systemConfig['predsrch.filePath']}")
    private String filePath;

    @Value("#{systemConfig['predsrch.sourcePath']}")
    private String sourceFilePath;

    @Value("#{systemConfig['predsrch.rejectPath']}")
    private String rejectFilePath;

    private File sourceFile;

    @Autowired
    @Qualifier("volumeService")
    private VolumeService volumeService;

    @Autowired
    @Qualifier("volumeServiceConfig")
    private VolumeServiceConfig volumeServiceConfig;

    @Override
    public void verifyFiles() throws Exception {
        LogUtil.debug(VerifyFilesServiceImpl.class, "verifyFilesService is start={}", System.currentTimeMillis());
        boolean isVerify = false;
        for (String siteKey : this.supportSites) {
            if (CommonConstants.DEFAULT.equalsIgnoreCase(siteKey)) {
                continue;
            }

            String verify = this.volumeServiceConfig.getValue(siteKey, VolumeServiceConfig.PREDSRCH_IS_VERIFY);
            isVerify = verify != null ? Boolean.parseBoolean(verify) : false;
            if (isVerify) {
                LogUtil.info(VerifyFilesServiceImpl.class, "verifyFilesService siteKey: {} MD5 verify file is Enabled", siteKey);
                break;
            }
        }

        if (!isVerify) {
            LogUtil.info(VerifyFilesServiceImpl.class, "verifyFilesService MD5 verify file is Disabled");
            return;
        }

        try {
            this.verifyFiles(true);
            LogUtil.debug(VerifyFilesServiceImpl.class, "verifyFilesService is end={}", System.currentTimeMillis());
        } catch (Exception e) {
            LogUtil.error(VerifyFilesServiceImpl.class, "Verify wpc Files error=" + e.getMessage(), e);
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_DATAVERIFYERR, e.getMessage());
        }
    }

    public void verifyFiles(final boolean reTry) throws Exception {
        this.sourceFile = new File(this.sourceFilePath);
        Set<String> siteSet = this.getSiteSet();
        List<File> invalidFiles = new ArrayList<File>();
        for (String site : siteSet) {
            if (CommonConstants.DEFAULT.equalsIgnoreCase(site)) {
                continue;
            }

            String verify = this.volumeServiceConfig.getValue(site, VolumeServiceConfig.PREDSRCH_IS_VERIFY);
            boolean isVerify = verify != null ? Boolean.parseBoolean(verify) : false;
            if (!isVerify) {
                continue;
            }
            LogUtil.info(VerifyFilesServiceImpl.class, "verifyFilesService siteKey: {} start MD5 verify file is Enabled", site);
            File[] outFiles = this.getOutFilesBySite(site);
            if (outFiles != null && outFiles.length > 0) {
                for (File outFile : outFiles) {
                    File[] validXmlFiles = this.validXmlFiles(site, outFile);
                    if (validXmlFiles == null || validXmlFiles.length == 0) {
                        invalidFiles.add(outFile);
                    }
                }
            }
        }

        if (ListUtil.isValid(invalidFiles)) {
            boolean isVolumeServiceEnabled = false;
            for (String site : siteSet) {
                String volumeServiceEnabled = this.volumeServiceConfig.getValue(site, VolumeServiceConfig.VOLUME_SERVICE_ENABLED);
                isVolumeServiceEnabled = volumeServiceEnabled != null ? Boolean.parseBoolean(volumeServiceEnabled) : false;
                if (isVolumeServiceEnabled) {
                    break;
                }
            }

            if (reTry && isVolumeServiceEnabled) {
                LogUtil.warn(VerifyFilesServiceImpl.class, "retry start copy wpc files to local");
                this.volumeService.copyToLocalData();
                this.verifyFiles(false);
            } else {
                for (File file : invalidFiles) {
                    LogUtil.error(VerifyFilesServiceImpl.class, "Verify wpc Files error, out file=" + file.getAbsolutePath());
                }
                throw new CommonException(PredictiveSearchConstant.ERRORMSG_DATAVERIFYERR);
            }
        }
    }

    private File[] validXmlFiles(final String site, final File outFile) throws Exception {
        File[] xmlFilesArray = null;
        String outFileName = outFile.getName();
        String pattern = WpcFileUtil.XML_FILE_NAME_PATTERN.replace(WpcFileUtil.SITE, site + CommonConstants.SYMBOL_UNDERLINE)
            .replace(WpcFileUtil.FILE_TIME, WpcFileUtil.getTime(outFileName));
        File[] xmlFiles = this.sourceFile.listFiles(new WpcFilenameFilter(pattern));
        if (this.validWpcFile(xmlFiles)) {
            this.moveWpcFiles(outFile, xmlFiles, this.filePath);
            xmlFilesArray = xmlFiles;
        } else {
            LogUtil.error(VerifyFilesServiceImpl.class,
                "Verify wpc Files error wpc files is invalid, site=" + site + ", outFile=" + outFile.getAbsolutePath());
            this.moveWpcFiles(outFile, xmlFiles, this.rejectFilePath);

        }
        return xmlFilesArray;
    }

    private boolean validWpcFile(final File[] xmlFiles) throws Exception {
        boolean isValid = false;
        if (xmlFiles != null && xmlFiles.length > 0) {
            for (File xmlFile : xmlFiles) {
                String md5 = MD5Util.getMd5ByFile(xmlFile);
                String md5Value = this.readFile(xmlFile.getAbsolutePath() + WpcFileUtil.MD5_FILE_EXTENSION);
                if (StringUtil.isValid(md5) && StringUtil.isValid(md5Value) && md5Value.equals(md5)) {
                    isValid = true;
                } else {
                    isValid = false;
                    LogUtil.error(VerifyFilesServiceImpl.class, "Verify wpc Files error wpc files is invalid, the md5 of xml file= "
                        + md5 + " , the path of xml File= " + xmlFile.getAbsolutePath() + " , the value of md5 = " + md5Value);
                    break;
                }
            }
        }
        return isValid;
    }

    private File[] getOutFilesBySite(final String site) {
        File file = new File(this.sourceFilePath);
        return file.listFiles(new OutFileFilter(site));
    }

    private void moveWpcFiles(final File outFile, final File[] xmlFiles, final String targetPath) throws Exception {
        for (File xmlFile : xmlFiles) {
            this.moveFile(targetPath, new File(xmlFile.getAbsolutePath() + WpcFileUtil.MD5_FILE_EXTENSION));
            this.moveFile(targetPath, xmlFile);
        }
        this.moveFile(targetPath, outFile);
    }

    private void moveFile(final String targetFolder, final File sourceFile) throws Exception {
        try {
            if (null != sourceFile && !sourceFile.isDirectory()) {
                moveFile(sourceFile, new File(targetFolder, sourceFile.getName()));
            }
        } catch (IOException e) {
            LogUtil.error(VerifyFilesServiceImpl.class, "copyFile error, targetFolder=" + targetFolder + ", sourceFile="
                + sourceFile.getAbsolutePath() + ", error message=" + e.getMessage());
            throw e;
        }
    }

    private void moveFile(final File srcFile, final File destFile) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' is a directory");
        }
        if (destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' is a directory");
        }
        final boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            FileUtils.copyFile(srcFile, destFile);
            srcFile.delete();
        }
    }

    private Set<String> getSiteSet() {
        Set<String> siteSet = new HashSet<String>();
        for (int i = 0; null != this.supportSites && i < this.supportSites.length; i++) {
            siteSet.add(this.supportSites[i]);
            for (int j = 0; null != this.siteConverterRule && j < this.siteConverterRule.length; j++) {
                if (this.siteConverterRule[j].contains(this.supportSites[i])) {
                    String[] convertSites = this.siteConverterRule[j].split(CommonConstants.SYMBOL_SEPARATOR);
                    for (String site : convertSites) {
                        siteSet.add(site);
                    }
                }
            }
        }
        return siteSet;
    }

    private String readFile(final String fileName) throws Exception {
        StringBuffer bf = new StringBuffer();
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            File file = new File(fileName);
            String content;
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            while ((content = bufferedReader.readLine()) != null) {
                bf.append(content);
            }
        } catch (Exception e) {
            LogUtil.error(PredictiveSearchServiceImpl.class, "readFile error, file name=" + fileName + ", error=" + e.getMessage(),
                e);
            throw e;
        } finally {
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    LogUtil.error(PredictiveSearchServiceImpl.class, "Can't close BufferedReader, error=" + e.getMessage(), e);
                }
            }
            if (null != fileReader) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    LogUtil.error(PredictiveSearchServiceImpl.class, "Can't close FileReader, error=" + e.getMessage(), e);
                }
            }
        }
        return bf.toString();
    }
}
