/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5Util {

	private static Logger logger = LoggerFactory.getLogger(MD5Util.class);

	private MD5Util() {
	}

	private static final String HASH_TYPE = "MD5";
	private static final char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	public static String getMd5ByFile(final File file) throws IOException, NoSuchAlgorithmException {
		String hash;
		try (FileInputStream in = new FileInputStream(file);
			 FileChannel channel = in.getChannel()) {
			MessageDigest md5 = MessageDigest.getInstance(MD5Util.HASH_TYPE);
			ByteBuffer buffer = ByteBuffer.allocate(8 * 1024);
			for (int count = channel.read(buffer); count != -1; count = channel.read(buffer)) {
				buffer.flip();
				md5.update(buffer);
				if (!buffer.hasRemaining()) {
					buffer.clear();
				}
			}
			hash = toHexString(md5.digest());
		} catch (Exception e) {
			logger.error("getMd5ByFile error, file: {}, error message: {}", file, e.getMessage());
			throw new ApplicationException();
		}
		return hash;
	}

	private static String toHexString(final byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(MD5Util.hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(MD5Util.hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}
}
