/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.provider.sftp.IdentityInfo;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;

/**
 * <p>
 * <b> The Helper which wraps the Apache Common VFS for file access </b>
 * </p>
 */
public class FileAccessHelper {

    private static final int TIMEOUT = 6 * 1000;

    public static boolean isConnect(final String privateKeyPath, final String filePath, final int retryTime) throws Exception {
        boolean isConnect = false;
        for (int i = 0; i < 5; i++) {
            if (i > 0) {
                if (i < 4) {
                    Thread.sleep(10 * 1000);
                } else {
                    Thread.sleep(retryTime * 1000);
                }
            }
            isConnect = connect(privateKeyPath, filePath);
            if (isConnect) {
                return isConnect;
            }
            LogUtil.debug(FileAccessHelper.class, "SFTP is Retry Connect times={}, isConnect sfpt={}", i + 1, isConnect);
        }
        return isConnect;
    }

    public static boolean connect(final String privateKeyPath, final String filePath) {
        boolean isConnect = false;
        FileSystemManager fsm = null;
        FileObject fo = null;
        try {
            if (StringUtil.isValid(privateKeyPath) && StringUtil.isValid(filePath)) {
                fsm = VFS.getManager();
                FileSystemOptions options = createDefaultOptions(privateKeyPath, filePath);
                fo = fsm.resolveFile(filePath, options);
                if (fo != null) {
                    isConnect = true;
                    LogUtil.debug(FileAccessHelper.class, "SFTP is Connect...privateKeyPath: {}, remoteFilePath: {}",
                        privateKeyPath, filePath);
                }
            }
        } catch (FileSystemException e) {
            LogUtil.error(FileAccessHelper.class, "SFTP can not Connect...privateKeyPath: {}, remoteFilePath: {}", privateKeyPath,
                filePath, e);
        } finally {
            if (fsm != null && fo != null) {
                try {
                    fsm.closeFileSystem(fo.getParent().getFileSystem());
                    fo.close();
                } catch (FileSystemException e) {
                    LogUtil.error(FileAccessHelper.class, "connect, Cannot close SFPT connect after processing, privateKeyPath="
                        + privateKeyPath + ", filePath=" + filePath, e);
                }

            }
        }
        return isConnect;
    }

    /**
     *
     * <p>
     * <b> Get the filename list </b>
     * </p>
     *
     * @param privateKeyPath
     * @param destFolder
     *            - destination folder
     * @param regPattern
     *            - regular expression for query
     * @return array of filenames
     * @throws FileSystemException
     */
    public static String[] getChildren(final String privateKeyPath, final String destFolder, final String regPattern)
        throws FileSystemException {
        String[] filenames = null;
        FileSystemManager fsm = null;
        FileObject fo = null;
        try {
            fsm = VFS.getManager();
            FileSystemOptions options = createDefaultOptions(privateKeyPath, destFolder);
            fo = fsm.resolveFile(destFolder, options);
            Pattern pattern = Pattern.compile(regPattern);
            // Search the folder with regular expression pattern
            if (fo.isFolder()) {
                FileObject[] listOfFileObject = fo.getChildren();

                List<String> al = new ArrayList<String>();
                for (FileObject aFileObject : listOfFileObject) {
                    String uri = aFileObject.getName().getFriendlyURI();
                    if (pattern.matcher(uri).matches()) {
                        al.add(uri);
                    }
                }
                filenames = (String[]) al.toArray(new String[] {});
            }
        } catch (FileSystemException e) {
            LogUtil.error(FileAccessHelper.class,
                "getChildren error: destFolder: " + destFolder + ", regPattern:" + regPattern + ", privateKeyPath" + privateKeyPath,
                e);
            throw e;
        } finally {
            if (fsm != null && fo != null) {
                try {
                    fsm.closeFileSystem(fo.getParent().getFileSystem());
                    fo.close();
                } catch (FileSystemException e) {
                    LogUtil.error(FileAccessHelper.class, "getChildren, Cannot close SFPT connect after processing, privateKeyPath="
                        + privateKeyPath + ", destFolder=" + destFolder, e);
                }

            }
        }
        return filenames;
    }

    /**
     *
     * <p>
     * <b> To copy the file (as in FileInfo.filePath) to the path
     * (localFilePath) </b>
     * </p>
     *
     * @param privateKeyPath
     * @param filePath
     * @param localFilePath
     *            (Full filepath with filename, e.g.: /appvol/abc.txt)
     * @throws FileSystemException
     */
    public static void copyToLocal(final String privateKeyPath, final String remoteFilePath, final String localFilePath)
        throws FileSystemException {
        FileSystemManager fsm = null;
        FileObject fo = null;
        try {
            fsm = VFS.getManager();
            FileSystemOptions options = createDefaultOptions(privateKeyPath, remoteFilePath);
            fo = fsm.resolveFile(remoteFilePath, options);
            FileObject localFo = VFS.getManager().resolveFile(localFilePath);

            localFo.copyFrom(fo, Selectors.SELECT_SELF);
        } catch (FileSystemException e) {
            LogUtil.error(FileAccessHelper.class, "copyToLocal error: remoteFilePath: " + remoteFilePath + ", localFilePath:"
                + localFilePath + ", privateKeyPath" + privateKeyPath, e);
            throw e;
        } finally {
            if (fsm != null && fo != null) {
                try {
                    fsm.closeFileSystem(fo.getParent().getFileSystem());
                    fo.close();
                } catch (FileSystemException e) {
                    LogUtil.error(FileAccessHelper.class, "copyToLocal, Cannot close SFPT connect after processing, privateKeyPath="
                        + privateKeyPath + ", remoteFilePath=" + remoteFilePath, e);
                }

            }
        }
    }

    /**
     *
     * <p>
     * <b> FileSystemOptions for Apache VFS </b>
     * </p>
     *
     * @return FileSystemOptions
     * @throws FileSystemException
     */
    private static FileSystemOptions createDefaultOptions(final String privateKeyPath, final String filePath)
        throws FileSystemException {
        // create options for vfs
        FileSystemOptions options = new FileSystemOptions();

        if (filePath.startsWith("sftp://") && StringUtil.isValid(privateKeyPath)) {
            // ssh key
            SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(options, "no");
            // set root directory to user home
            SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(options, false);
            // timeout
            SftpFileSystemConfigBuilder.getInstance().setTimeout(options, FileAccessHelper.TIMEOUT);
            // To prevent Kerberos username prompt
            SftpFileSystemConfigBuilder.getInstance().setPreferredAuthentications(options,
                "publickey,keyboard-interactive,password");
            // Private KeyPath
            IdentityInfo ii = new IdentityInfo(new File(privateKeyPath));
            SftpFileSystemConfigBuilder.getInstance().setIdentityInfo(options, ii);

//            SftpFileSystemConfigBuilder.getInstance().setProxyPort(options, 10022);
        }
        return options;
    }
}
