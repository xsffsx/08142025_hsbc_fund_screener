/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import com.hhhh.group.secwealth.mktdata.elastic.properties.VolumeServiceProperties;
import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.handler.BMCComponent;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.exception.ExTraceCodeGenerator;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.jcraft.jsch.ChannelSftp.LsEntry;

@Component
public class FileAccessUtil {

	private static Logger logger = LoggerFactory.getLogger(FileAccessUtil.class);

	private ChannelSftp sftpClient = null;

	@Autowired
	private VolumeServiceProperties volumeServiceProperty;
	
	@Autowired
    private BMCComponent bmcComponent;

	@Value("${aws.sftp.port:22}")
	private int port;

	private static final int TIMEOUT = 5 * 1000;

	public boolean isConnect(final String privateKeyPath, final String filePath, final int retryTime) throws InterruptedException, JSchException {
		try {
			boolean isConnect = false;
			for (int i = 0; i < 5; i++) {
				if (i > 0) {
					if (i < 4) {
						Thread.sleep(10 * 1000L);
					} else {
						Thread.sleep(retryTime * 1000L);
					}
				}
				if (connect(privateKeyPath, filePath) != null) {
					return true;
				}
				logger.debug("SFTP is Retry Connect times={}, isConnect sfpt={}", i + 1, isConnect);
			}
		} finally {
			disconnect();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private ChannelSftp connect(String privateKeyPath, String filePath) {
		try {
			if (StringUtil.isValid(privateKeyPath) && StringUtil.isValid(filePath)) {
				JSch jsch = new JSch();
				String siteKey = "HK_HBAP";
				String privateKeyId = volumeServiceProperty.getValue(siteKey, VolumeServiceProperties.PRIVATEKEY_ID_KEY);
				String host = volumeServiceProperty.getValue(siteKey, "host");
				String user = volumeServiceProperty.getValue(siteKey, "user");
				jsch.addIdentity(privateKeyId, readFile(privateKeyPath), null, null);
				Session session = jsch.getSession(user, host, port);
				session.setConfig("StrictHostKeyChecking", "no");
				session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
				session.connect(FileAccessUtil.TIMEOUT);
				this.sftpClient = (ChannelSftp) session.openChannel("sftp");
				this.sftpClient.connect(FileAccessUtil.TIMEOUT);
				List<LsEntry> ls = this.sftpClient.ls(filePath);
				if (!ls.isEmpty()) {
					return this.sftpClient;
				}
			}
		} catch (Exception e) {
			logger.error("SFTP can not Connect...privateKeyPath: {}, remoteFilePath: {}", privateKeyPath, filePath, e);
			String traceCode = ExTraceCodeGenerator.generate();
			try {
				this.bmcComponent.doBMC(new VendorException(ExCodeConstant.EX_CODE_SFTPCONNECTERR), traceCode);
			} catch (IOException e1) {
				logger.error("Write BMC encounter error", e1);
			}
		}
		return this.sftpClient;
	}

	@SuppressWarnings("unchecked")
	public String[] getChildren(final String privateKeyPath, final String destFolder, final String regPattern)
			throws SftpException, JSchException {
		String[] filenames;
		try {
			connect(privateKeyPath, destFolder);
			List<LsEntry> ls = this.sftpClient.ls(destFolder);
			Iterator<LsEntry> iterator = ls.iterator();
			Pattern pattern = Pattern.compile(regPattern);
			List<String> al = new ArrayList<>();

			while (iterator.hasNext()) {
				String filename = iterator.next().getFilename();
				if (pattern.matcher(filename).matches()) {
					al.add(filename);
				}
			}
			filenames = al.toArray(new String[] {});
		} catch (SftpException e) {
			logger.error("getChildren error: destFolder: " + destFolder + ", regPattern:" + regPattern
					+ ", privateKeyPath" + privateKeyPath, e);
			throw new SftpException(500, "Sftp error");
		} finally {
			disconnect();
		}

		return filenames;
	}

	public void copyToLocal(final String privateKeyPath, final String remoteFilePath,final String remoteOutFile, final String localFilePath)
			throws JSchException {
		try {
			connect(privateKeyPath, remoteFilePath);
			this.sftpClient.cd(remoteFilePath);
			this.sftpClient.get(remoteOutFile, localFilePath);
		} catch (SftpException e) {
			logger.error("copyToLocal error: remoteFilePath: {}, localFilePath: {}, privateKeyPath: {}", remoteFilePath, localFilePath, privateKeyPath);
			throw new ApplicationException(ExCodeConstant.EX_CODE_COPYTOLOCAL, e);
		} finally {
			disconnect();
		}

	}

	public void disconnect() throws JSchException {
		if (this.sftpClient != null) {
			if (this.sftpClient.isConnected()) {
				this.sftpClient.getSession().disconnect();
				this.sftpClient.disconnect();
				this.sftpClient.exit();
			} else if (this.sftpClient.isClosed()) {
				logger.info("sftp server connection is already closed ");
			}
			this.sftpClient = null;
		}
	}

	public byte[] readFile(String privateKeyPath) throws IOException {
		InputStream in = this.getClass().getResourceAsStream(privateKeyPath);
		return IOUtils.toByteArray(in);
	}

}
