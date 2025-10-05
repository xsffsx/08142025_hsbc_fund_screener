/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;

public class MD5Util {

    private static final String HASH_TYPE = "MD5";
    private static final char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String getMd5ByFile(final File file) throws Exception {
        FileInputStream in = null;
        FileChannel channel = null;
        String hash = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance(MD5Util.HASH_TYPE);
            in = new FileInputStream(file);
            channel = in.getChannel();
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
            LogUtil.error(MD5Util.class, "getMd5ByFile error, file=" + file + ", error message=" + e.getMessage(), e);
            throw e;
        } finally {
            try {
                if (channel != null) {
                    channel.close();
                }
            } catch (IOException e) {
                LogUtil.error(MD5Util.class, "close FileChannel error", e);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                LogUtil.error(MD5Util.class, "close FileInputStream error", e);
            }
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
