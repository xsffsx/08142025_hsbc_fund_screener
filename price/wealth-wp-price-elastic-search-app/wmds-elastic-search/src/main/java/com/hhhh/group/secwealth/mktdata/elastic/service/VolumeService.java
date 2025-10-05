/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.service;

import java.io.File;
import java.util.*;
import com.hhhh.group.secwealth.mktdata.elastic.configuration.VolumeConfig;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import com.hhhh.group.secwealth.mktdata.elastic.properties.VolumeServiceProperties;
import com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants;
import com.hhhh.group.secwealth.mktdata.elastic.util.FileAccessUtil;
import com.hhhh.group.secwealth.mktdata.elastic.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.elastic.util.WpcFileUtil;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;

@Service
public class VolumeService {

	private static Logger logger = LoggerFactory.getLogger(VolumeService.class);

	@Autowired
	private AppProperties appProperty;

	@Value("${predsrch.siteLoadInterfaceRule}")
	private List<String> siteConverterRule;

	@Value("${predsrch.filePath}")
	private String filePath;

	@Value("${volumeService.retryTime}")
	private Integer retryTime;

	@Value("${predsrch.sourcePath}")
	private String sourceFilePath;

	@Autowired
	private VolumeServiceProperties volumeServiceProperty;

	@Autowired
	private FileAccessUtil fileAccessHelper;

	@Autowired
	private VolumeConfig volumeConfig;

	public void copyToLocalData() throws ApplicationException {
		logger.info("volumeService: copy to local data start {}", System.currentTimeMillis());
		boolean sucAllConnect = true;
		boolean sucAllDownload = true;
		ApplicationException sucAllDownloadErr = null;
		for (String site : appProperty.getSupportSites()) {
			String remoteOutFile = null;
			try {
				// ignore default site
				if (checkVolumeServiceEnabled(site)) continue;
				this.checkPrivateKeyAndRemoteFile();
				String[] privateKeyIdArray = this.volumeConfig.getPrivateKeyIdMap().get(site);
				String[] privateKeyPathArray = this.volumeConfig.getPrivateKeyPathMap().get(site);
				String[] remoteFilePathArray = this.volumeConfig.getRemoteFileDirMap().get(site);
				if (ArrayUtils.isNotEmpty(privateKeyIdArray) && ArrayUtils.isNotEmpty(privateKeyPathArray) && ArrayUtils.isNotEmpty(remoteFilePathArray)) {
					List<String> volumeInfoList = this.getVolumeInfoList(sucAllConnect, privateKeyIdArray, privateKeyPathArray, remoteFilePathArray);
					String privateKeyPath = volumeInfoList.get(1);
					String remoteFilePath = volumeInfoList.get(2);

					Set<String> siteSet = this.getSiteSet(site);
					for (String siteKey : siteSet) {
						remoteOutFile = this.copyOutFile(siteKey, privateKeyPath, remoteFilePath);
						this.downloadXmlFiles(remoteOutFile, privateKeyPath, remoteFilePath, siteKey);
					}
				}
			} catch (Exception e) {
				// VolumeServiceProperties.getValue(),FileAccessUtil.isConnect(),FileAccessUtil.getChildren()
				logger.error("copyToLocalData error: site: {}, OutFileName: {}", site, remoteOutFile);
				sucAllDownload = false;
				sucAllDownloadErr = (ApplicationException) e;
				Thread.currentThread().interrupt();
			}
		}

		if (!sucAllDownload) {
			throw sucAllDownloadErr;
		}
		
		logger.info("volumeService: copy to local data end={}", System.currentTimeMillis());
	}

	private void checkPrivateKeyAndRemoteFile() {
		if (this.volumeConfig.getPrivateKeyIdMap().size() == 0 || this.volumeConfig.getPrivateKeyPathMap().size() == 0 || this.volumeConfig.getRemoteFileDirMap().size() == 0) {
			logger.error("VolumeService is not config SFTP Private Key Id or Path or Remote File Path!");
			throw new ApplicationException(ExCodeConstant.EX_CODE_SFTP_CONFIG_ERROR);
		}
	}

	/**
	 * download xml files from remote server
	 * @param remoteFilePath
	 * @param remoteXmlFile
	 * @param privateKeyPath
	 * @param siteKey
	 * @throws JSchException
	 */
	private void downloadXmlFiles(String remoteOutFile, String privateKeyPath, String remoteFilePath, String siteKey) throws JSchException, SftpException {
		if (StringUtil.isValid(remoteOutFile)) {
			// String pattern = WpcFileUtil.XML_FILE_PATTERN replace with name only
			String pattern = WpcFileUtil.XML_FILE_NAME_PATTERN
					.replace(WpcFileUtil.SITE, siteKey + CommonConstants.SYMBOL_UNDERLINE)
					.replace(WpcFileUtil.FILE_TIME, WpcFileUtil.getTime(remoteOutFile));
			String[] remoteXmlFileArray = fileAccessHelper.getChildren(privateKeyPath, remoteFilePath, pattern);
			if (ArrayUtils.isNotEmpty(remoteXmlFileArray)) {
				this.downloadXmlFilesByRemoteXmlFiles(privateKeyPath, remoteFilePath, siteKey, remoteXmlFileArray);
				// Download out file
				String localOutFilePath = this.getFileSavePath(siteKey) + remoteOutFile;
				File outFile = new File(localOutFilePath);
				fileAccessHelper.copyToLocal(privateKeyPath, remoteFilePath, remoteOutFile, outFile.getAbsolutePath());
				logger.warn("siteKey: {}, Download out file from: {} to :{}", siteKey, remoteOutFile, outFile.getAbsolutePath());
			}
		}
	}

	private List<String> getVolumeInfoList(boolean sucAllConnect, String[] privateKeyIdArray, String[] privateKeyPathArray,
									 String[] remoteFilePathArray) throws InterruptedException, JSchException {
		List<String> volumeInfoList = new ArrayList<>();
		String privateKeyId = null;
		String privateKeyPath = null;
		String remoteFilePath = null;
		for (int i = 0; i < privateKeyPathArray.length; i++) {
			privateKeyId = privateKeyIdArray[i];
			privateKeyPath = privateKeyPathArray[i];
			boolean isConnect = fileAccessHelper.isConnect(privateKeyPath, remoteFilePathArray[i], this.retryTime);
			if (isConnect) {
				remoteFilePath = remoteFilePathArray[i];
				break;
			} else {
				sucAllConnect = false;
			}
		}

		if (!sucAllConnect) {
			throw new ApplicationException(ExCodeConstant.EX_CODE_VOLUMESERVICEERR);
		}
		volumeInfoList.add(privateKeyId);
		volumeInfoList.add(privateKeyPath);
		volumeInfoList.add(remoteFilePath);
		return volumeInfoList;
	}

	private boolean checkVolumeServiceEnabled(String site) {
		if (CommonConstants.DEFAULT.equalsIgnoreCase(site)) {
			logger.debug("volumeService: skip the DEFAULT site: {}", System.currentTimeMillis());
			return true;
		}
		String volumeServiceEnabled = this.volumeServiceProperty.getValue(site,
				VolumeServiceProperties.VOLUME_SERVICE_ENABLED);
		boolean isVolumeServiceEnabled = false;
		if (volumeServiceEnabled != null) isVolumeServiceEnabled = Boolean.parseBoolean(volumeServiceEnabled);
		logger.info("Site: {}, volumeService is {}", site, isVolumeServiceEnabled ? "Enabled" : "Disabled");
		return !isVolumeServiceEnabled;
	}

	private void downloadXmlFilesByRemoteXmlFiles(String privateKeyPath, String remoteFilePath, String siteKey, String[] remoteXmlFileArray) throws JSchException {
		String[] dataTypeArray = this.getDataTypeArray(siteKey);// check which type to download
		// Download xml file
		for (String remoteXmlFile : remoteXmlFileArray) {
			// if volumeService.dataType was not set, all dataType will be handled
			if (ArrayUtils.isNotEmpty(dataTypeArray)) {
				for (String dataType : dataTypeArray) {
					if (remoteXmlFile.indexOf(dataType) != -1) {
						downloadXmlFile(remoteFilePath, remoteXmlFile, privateKeyPath, siteKey);
					}
				}
			} else {
				downloadXmlFile(remoteFilePath, remoteXmlFile, privateKeyPath, siteKey);
			}
		}
	}

	private Set<String> getSiteSet(String site) {
		Set<String> siteSet = new HashSet<>();
		siteSet.add(site);
		if (this.siteConverterRule != null && !this.siteConverterRule.isEmpty()) {
			for (String rule : this.siteConverterRule) {
				String str = rule.substring(0, rule.indexOf(CommonConstants.SYMBOL_VERTICAL));
				if (site.equals(str)) {
					String[] array = rule.split(CommonConstants.SYMBOL_SEPARATOR);
					String[] dest = new String[array.length];
					for (int i = 1; i < array.length; i++) {
						System.arraycopy(array, 1, dest, 0, array.length - 1);
						siteSet.addAll(Arrays.asList(dest));
					}
					break;
				}
			}
		}
		return siteSet;
	}

	private void downloadXmlFile(final String remoteFilePath, final String remoteXmlFile, final String privateKeyPath,
			final String siteKey) throws JSchException {
		String localXmlFile = remoteXmlFile;
		String localXmlFilePath = this.getFileSavePath(siteKey) + localXmlFile;
		File xmlFile = new File(localXmlFilePath);
		fileAccessHelper.copyToLocal(privateKeyPath, remoteFilePath, remoteXmlFile, xmlFile.getAbsolutePath());
		logger.warn("siteKey: {}, Download xml file from: {} to :{}", siteKey, remoteXmlFile,
				xmlFile.getAbsolutePath());

		String verify = this.volumeServiceProperty.getValue(siteKey, VolumeServiceProperties.PREDSRCH_IS_VERIFY);
		boolean isVerify = false;
		if (verify != null) isVerify = Boolean.parseBoolean(verify);
		if (isVerify) {
			String remoteMD5File = remoteXmlFile + WpcFileUtil.MD5_FILE_EXTENSION;
			String localMD5FilePath = this.getFileSavePath(siteKey) + remoteMD5File;
			File md5File = new File(localMD5FilePath);
			fileAccessHelper.copyToLocal(privateKeyPath, remoteFilePath, remoteMD5File, md5File.getAbsolutePath());
			logger.warn("siteKey: {}, Download MD5 file from: {} to :{}", siteKey, remoteMD5File,
					md5File.getAbsolutePath());
		}
	}

	private String copyOutFile(final String siteKey, final String privateKeyPath, final String remoteFilePath)
			throws SftpException, JSchException {
		String copyOutFile = "";
		long localOutFileCreateTime = 0L;
		long sfptOutFileCreateTime = 0L;

		File file = new File(this.filePath);
		String[] localOutFileArray = file.list((dir, name) -> (name.endsWith(CommonConstants.OUT_FILE_EXTENSION) && name.startsWith(siteKey)));
		if (localOutFileArray != null && localOutFileArray.length > 0) {
			if (localOutFileArray.length > 1) {
				this.sortOutFiles(localOutFileArray);
			}
			String localOutFile = localOutFileArray[0];// get latest .out file
			logger.debug("siteKey: {}, local Out File name: {}", siteKey, localOutFile);
			localOutFileCreateTime = WpcFileUtil.converOutFileNameToTimestamp(localOutFile);
		}
		logger.debug("siteKey: {}, local Out File localOutFileCreateTime: {}", siteKey, localOutFileCreateTime);

		String[] remoteOutFileArray = fileAccessHelper.getChildren(privateKeyPath, remoteFilePath,
				WpcFileUtil.ALL_SITE_OUT_FILE_PATTERN.replace(WpcFileUtil.SITE,
						siteKey + CommonConstants.SYMBOL_UNDERLINE));
		if (remoteOutFileArray != null && remoteOutFileArray.length > 0) {
			if (remoteOutFileArray.length > 1) {
				this.sortOutFiles(remoteOutFileArray);
			}
			String remoteOutFile = remoteOutFileArray[0];
			logger.debug("siteKey: {}, remote Out File name: {}", siteKey, remoteOutFile);
			sfptOutFileCreateTime = WpcFileUtil.converOutFileNameToTimestamp(remoteOutFile);
			if (sfptOutFileCreateTime > localOutFileCreateTime) {
				copyOutFile = remoteOutFile;
			}
		}
		logger.debug("siteKey: {}, remote Out File remoteOutFileCreateTime: {}", siteKey, sfptOutFileCreateTime);

		return copyOutFile;
	}

	private void sortOutFiles(String[] localOutFileArray) {
		Arrays.sort(localOutFileArray, (o1, o2) -> {
			long converterFileName1 = WpcFileUtil.converOutFileNameToTimestamp(o1);
			long converterFileName2 = WpcFileUtil.converOutFileNameToTimestamp(o2);
			if (converterFileName1 > converterFileName2) {
				return -1;
			} else if (converterFileName1 < converterFileName2) {
				return 1;
			} else {
				return 0;
			}
		});
	}

	/**
	 * Get the xml file data Type Setting
	 */
	private String[] getDataTypeArray(final String site) {
		String[] dataTypeArray = null;
		String dataTypeStr = this.volumeServiceProperty.getValue(site, VolumeServiceProperties.VOLUME_SERVICE_DATATYPE);
		if (StringUtil.isValid(dataTypeStr)) {
			dataTypeArray = dataTypeStr.split(CommonConstants.SYMBOL_COMMA);
		}
		return dataTypeArray;
	}

	private String getFileSavePath(final String site) {
		String verify = this.volumeServiceProperty.getValue(site, VolumeServiceProperties.PREDSRCH_IS_VERIFY);
		boolean isVerify = false;
		if (verify != null) isVerify = Boolean.parseBoolean(verify);
		if (isVerify) {
			return this.sourceFilePath;
		}
		return this.filePath;
	}

}
