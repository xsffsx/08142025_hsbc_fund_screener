/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.service;

import java.io.*;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import com.hhhh.group.secwealth.mktdata.elastic.properties.VolumeServiceProperties;
import com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants;
import com.hhhh.group.secwealth.mktdata.elastic.util.MD5Util;
import com.hhhh.group.secwealth.mktdata.elastic.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.elastic.util.WpcFileUtil;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import static com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants.OUT_FILE_EXTENSION;

@Service
@Slf4j
public class VerifyFilesService {
	
	private static Logger logger = LoggerFactory.getLogger(VerifyFilesService.class);
	
	@Autowired
	private AppProperties appProperty;
	
	@Autowired
	private VolumeServiceProperties volumeServiceProperty;
	
	@Autowired
	private VolumeService volumeService;
	
	@Value("${predsrch.siteLoadInterfaceRule}")
	private List<String> siteConverterRule;
	
	@Value("${predsrch.sourcePath}")
	private String sourceFilePath;
	
	@Value("${predsrch.filePath}")
	private String filePath;
	
	@Value("${predsrch.rejectPath}")
	private String rejectFilePath;
	
	public static final String ERRORMSG_DATAVERIFYERR = "DataVerifyErr";
	
	public void verifyFiles() throws Exception{
		logger.debug("verifyFilesService is start={}", System.currentTimeMillis());
        boolean isVerify = false;
        for (String siteKey : appProperty.getSupportSites()) {
            String verify = this.volumeServiceProperty.getValue(siteKey, VolumeServiceProperties.PREDSRCH_IS_VERIFY);
            if (verify != null) isVerify = Boolean.parseBoolean(verify);
            if (CommonConstants.DEFAULT.equalsIgnoreCase(siteKey) || isVerify) {
            	logger.info("verifyFilesService siteKey: {} MD5 verify file is Enabled", siteKey);
                break;
            }
        }
        if (!isVerify) {
        	logger.info("verifyFilesService MD5 verify file is Disabled");
            return;
        }
        try {
            this.verifyFiles(true);
            logger.debug("verifyFilesService is end={}", System.currentTimeMillis());
        } catch (Exception e) {
            logger.error("Verify wpc Files error=" + e.getMessage(), e);
            throw new ApplicationException(VerifyFilesService.ERRORMSG_DATAVERIFYERR);
        }
	}
	
	public void verifyFiles(final boolean reTry) throws Exception {
        File sourceFile = new File(this.sourceFilePath);
        Set<String> siteSet = this.getSiteSet();
        List<File> invalidFiles = new ArrayList<>();
        for (String site : siteSet) {
            String verify = this.volumeServiceProperty.getValue(site, VolumeServiceProperties.PREDSRCH_IS_VERIFY);
            boolean isVerify = false;
            if (verify != null) isVerify = Boolean.parseBoolean(verify);
            if (CommonConstants.DEFAULT.equalsIgnoreCase(site) || !isVerify) {
                continue;
            }
            logger.info("verifyFilesService siteKey: {} start MD5 verify file is Enabled", site);
            this.getInvalidFiles(invalidFiles, sourceFile, site);
        }

        if (!invalidFiles.isEmpty()) {
            boolean isVolumeServiceEnabled = this.isVolumeServiceEnabled(siteSet);
            //re download file from WPC
            if (reTry && isVolumeServiceEnabled) {
            	logger.warn("retry start copy wpc files to local");
                this.volumeService.copyToLocalData();
                this.verifyFiles(false);
            } else {
                for (File file : invalidFiles) {
                   logger.error("Verify wpc Files error, out file: {}", file.getAbsolutePath());
                }
                throw new ApplicationException(VerifyFilesService.ERRORMSG_DATAVERIFYERR);
            }
        }
    }

    private boolean isVolumeServiceEnabled(Set<String> siteSet) {
        boolean isVolumeServiceEnabled = false;
        for (String site : siteSet) {
            String volumeServiceEnabled = this.volumeServiceProperty.getValue(site, VolumeServiceProperties.VOLUME_SERVICE_ENABLED);
            if(volumeServiceEnabled != null) isVolumeServiceEnabled = Boolean.parseBoolean(volumeServiceEnabled);
            if (isVolumeServiceEnabled) {
                break;
            }
        }
        return isVolumeServiceEnabled;
    }

    private void getInvalidFiles(List<File> invalidFiles, File sourceFile, String site) throws IOException, NoSuchAlgorithmException {
        File[] outFiles = this.getOutFilesBySite(site);
        if (outFiles != null && outFiles.length > 0) {
            for (File outFile : outFiles) {
                File[] validXmlFiles = this.validXmlFiles(site, outFile, sourceFile);
                if (validXmlFiles == null || validXmlFiles.length == 0) {
                    invalidFiles.add(outFile);
                }
            }
        }
    }

    private Set<String> getSiteSet() {
        Set<String> siteSet = new HashSet<>();
        for (String supportSite: appProperty.getSupportSites()) {
            siteSet.add(supportSite);
            for (String siteConvert : this.siteConverterRule) {
                if (siteConvert.contains(supportSite)) {
                    siteSet.addAll(Arrays.asList(siteConvert.split(CommonConstants.SYMBOL_SEPARATOR)));
                }
            }
        }
        return siteSet;
    }
	
	private File[] getOutFilesBySite(final String site) {
        File file = new File(this.sourceFilePath);
        return file.listFiles((dir, name) -> (name.endsWith(OUT_FILE_EXTENSION) && name.startsWith(site)));
    }
	
	private File[] validXmlFiles(final String site, final File outFile, File sourceFile) throws IOException, NoSuchAlgorithmException {
        File[] xmlFilesArray = null;
        String outFileName = outFile.getName();
        String pattern = WpcFileUtil.XML_FILE_NAME_PATTERN.replace(WpcFileUtil.SITE, site + CommonConstants.SYMBOL_UNDERLINE)
            .replace(WpcFileUtil.FILE_TIME, WpcFileUtil.getTime(outFileName));
        File[] xmlFiles = sourceFile.listFiles((dir, name) -> Pattern.compile(pattern).matcher(name).matches());
        if (this.validWpcFile(xmlFiles)) {
            this.moveWpcFiles(outFile, xmlFiles, this.filePath);
            xmlFilesArray = xmlFiles;
        } else {
            logger.error("Verify wpc Files error wpc files is invalid, site: {},  outFile: {}", site, outFile.getAbsolutePath());
            this.moveWpcFiles(outFile, xmlFiles, this.rejectFilePath);
        }
        return xmlFilesArray;
    }
	
	private boolean validWpcFile(final File[] xmlFiles) throws IOException, NoSuchAlgorithmException {
        boolean isValid = false;
        if (xmlFiles != null && xmlFiles.length > 0) {
            for (File xmlFile : xmlFiles) {
                String md5 = MD5Util.getMd5ByFile(xmlFile);
                String md5Value = this.readFile(xmlFile.getAbsolutePath() + WpcFileUtil.MD5_FILE_EXTENSION);
                if (StringUtil.isValid(md5) && StringUtil.isValid(md5Value) && md5Value.equals(md5)) {
                    isValid = true;
                } else {
                    isValid = false;
                    logger.error("Verify wpc Files error wpc files is invalid, the md5 of xml file: {}, the path of xml File: {}, the value of md5: {} ", md5, xmlFile.getAbsolutePath(), md5Value);
                    break;
                }
            }
        }
        return isValid;
    }
	
	private String readFile(final String fileName) throws IOException {
        StringBuilder bf = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String content;
            while ((content = br.readLine()) != null) {
                bf.append(content);
            }
        }
        return bf.toString();
    }
	
	private void moveWpcFiles(final File outFile, final File[] xmlFiles, final String targetPath) throws IOException {
        for (File xmlFile : xmlFiles) {
            this.moveFile(targetPath, new File(xmlFile.getAbsolutePath() + WpcFileUtil.MD5_FILE_EXTENSION));
            this.moveFile(targetPath, xmlFile);
        }
        this.moveFile(targetPath, outFile);
    }
	
	private void moveFile(final String targetFolder, final File sourceFile) throws IOException {
        try {
            if (null != sourceFile && !sourceFile.isDirectory()) {
                moveFile(sourceFile, new File(targetFolder, sourceFile.getName()));
            }
        } catch (IOException e) {
            logger.error("copyFile error, targetFolder: {}, sourceFile: {}, error message: {}", targetFolder, sourceFile.getAbsolutePath(), e.getMessage());
            throw new ApplicationException();
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
            try {
                Files.delete(srcFile.toPath());
            } catch (Exception e) {
                log.error("Unable to delete the file: {}", srcFile.getName());
            }
        }
    }

}
