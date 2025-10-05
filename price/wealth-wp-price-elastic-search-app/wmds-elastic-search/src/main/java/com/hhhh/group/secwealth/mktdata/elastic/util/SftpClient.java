package com.hhhh.group.secwealth.mktdata.elastic.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.surefire.shared.compress.utils.Lists;
import org.jboss.logging.Logger;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.ChannelSftp.LsEntrySelector;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
//dev
public class SftpClient {
	private static final Logger LOGGER = Logger.getLogger(SftpClient.class);
	private String ip = "";
	private int port = 22;
	private String user = "";
	private String pwd = "";
	private ChannelSftp sftpChannel = null;
	private String privateKeyPath = null;
	private String keyPwd = null;

	private static final String CHARSET = "GBK";
	private static final String BANK_SFTP_IP = "127.0.0.4";
	private static final int BANK_SFTP_PORT = 22;
	private static final String BANK_SFTP_USER = "sftpusername";
	private static final String BANK_SFTP_PWD = "sftppassword";

	public static void main(String[] args) {
		SftpClient sftpClient = new SftpClient(BANK_SFTP_IP, BANK_SFTP_PORT);
		sftpClient.setUser(BANK_SFTP_USER);
		sftpClient.setPwd(BANK_SFTP_PWD);
		String remotePath = "./";
		String remoteFileName = "remoteFile.txt";

		String rspData = null;
		try {
			rspData = sftpClient.download(remotePath, remoteFileName);
		} catch (Exception e) {
			LOGGER.error("the exception file:" + remoteFileName, e);
		} finally {
			sftpClient.disconnect();
		}

		LOGGER.info(remoteFileName + " content:" + rspData);
	}

	public SftpClient(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public String download(String directory, String tFile) throws JSchException {
		if (null == sftpChannel) {
			sftpChannel = connect();
		}

		if (null == sftpChannel) {
			throw new JSchException("connect sftp server exception");
		}

		StringBuilder sb = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(sftpChannel.get(tFile), CHARSET))) {
			sftpChannel.cd(directory);
			String temp;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
		} catch (Exception e) {
			LOGGER.error("下载文件异常", e);
		} finally {
			disconnect();
		}
		return sb.toString();
	}

	/**
	 * 按指定规则获取文件名称列表
	 */
	public List<LsEntry> pullRptFiles(String remoteDir, String fileType) {
		SftpClient sftpClient = new SftpClient(BANK_SFTP_IP, BANK_SFTP_PORT);
		sftpClient.setUser(BANK_SFTP_USER);
		sftpClient.setPwd(BANK_SFTP_PWD);
		String latestMTime = "2018-11-18 23:34:34";
		try {
			sftpClient.connect();
			ChannelSftp client = sftpClient.getSftpClient();
			/**
			 * 目标文件名称：CAM52XX.<shortID>.<datestamp>.<extension>
			 * 目标文件名称：XMLSTXX.<shortID>.<datestamp>.<extension>
			 */
			// 定义远程文件选择器
			final Pattern fileTypeMatch = Pattern.compile("^" + fileType + "XX.*(.txt|.TXT)$");
			final List<LsEntry> rptFiles = new ArrayList<>();
			final String index = latestMTime;
			LsEntrySelector selector = entry -> {
				Matcher mtc = fileTypeMatch.matcher(entry.getFilename());
				SftpATTRS attrs = entry.getAttrs();
				boolean isMatch = mtc.find() && !attrs.isDir() && !attrs.isLink();
				boolean isNewFile = index.compareTo(String.valueOf(attrs.getMTime())) < 0;
				if (isMatch && isNewFile) {
					rptFiles.add(entry);
				}
				return LsEntrySelector.CONTINUE;
			};

			client.ls(remoteDir, selector);

			LOGGER.info("增量文件数量：" + rptFiles.size());
			return rptFiles;
		} catch (SftpException|JSchException e) {
			LOGGER.error("获取文件列表异常：", e);
		} finally {
			sftpClient.disconnect();
		}

		return Lists.newArrayList();
	}

	public void download(String directory, String downloadFile, String saveFile) throws JSchException {
		if (null == sftpChannel) {
			sftpChannel = connect();
		}
		if (null == sftpChannel) {
			throw new JSchException("connect sftp server exception");
		}
		try (FileOutputStream fos = new FileOutputStream(new File(saveFile))) {
			sftpChannel.cd(directory);
			sftpChannel.get(downloadFile, fos);
		} catch (Exception e) {
			LOGGER.error("", e);
		} finally {
			disconnect();
		}
	}

	public ChannelSftp connect() throws JSchException {
		if (null == user || user.length() == 0) {
			return null;
		}
		if ((null == pwd || pwd.length() == 0) && null == privateKeyPath) {
			return null;
		}

		JSch jsch = new JSch();
		Session session = null;

		if (null != privateKeyPath) {
			// 设置密钥和密码 支持密钥的方式登陆，只需在jsch.getSession之前设置一下密钥的相关信息就可以了
			try {
				if (null != keyPwd) {
					jsch.addIdentity(privateKeyPath, keyPwd);
				} else {
					jsch.addIdentity(privateKeyPath);
				}
			} catch (JSchException e) {
				LOGGER.error("设置证书异常", e);
				throw e;
			}
		}

		// 连接服务器，采用默认22端口
		session = jsch.getSession(user, ip, port);

		if (null != pwd && pwd.length() != 0) {
			session.setPassword(pwd);
		}

		// 如果服务器连接不上，则抛出异常
		if (session == null) {
			throw new JSchException("session is null");
		}

		// 设置第一次登陆的时候提示，可选值：(ask | yes | no)
		session.setConfig("StrictHostKeyChecking", "no");
		// 设置登陆超时时间
		session.connect(60 * 1000);
		sftpChannel = (ChannelSftp) session.openChannel("sftp");
		sftpChannel.connect(60 * 1000);

		return sftpChannel;
	}

	/**
	 * Disconnect with server
	 */
	public void disconnect() {
		if (this.sftpChannel != null) {
			if (this.sftpChannel.isConnected()) {
				this.sftpChannel.disconnect();
				this.sftpChannel.exit();
			} else if (this.sftpChannel.isClosed()) {
				LOGGER.info("sftp server connection is already closed ");
			}
			this.sftpChannel = null;
		}
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	// 默认22端口
	public void setPort(int port) {
		if (port < 1) {
			this.port = 22;
		}
		this.port = port;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setPrivateKeyPath(String privateKeyPath) {
		this.privateKeyPath = privateKeyPath;
	}

	public void setKeyPwd(String keyPwd) {
		this.keyPwd = keyPwd;
	}

	public ChannelSftp getSftpClient() {
		return sftpChannel;
	}

}
