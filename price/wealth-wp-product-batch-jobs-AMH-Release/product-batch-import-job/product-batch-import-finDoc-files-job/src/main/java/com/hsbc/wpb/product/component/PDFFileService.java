/*
 * ***************************************************************
 * Copyright.  dummy Holdings plc 2006 ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it
 * has been provided.  No part of it is to be reproduced,
 * disassembled, transmitted, stored in a retrieval system or
 * translated in any human or computer language in any way or
 * for any other purposes whatsoever without the prior written
 * consent of dummy Holdings plc.
 * ***************************************************************
 *
 * Class Name		PDFFileService
 *
 * Creation Date	Mar 8, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Mar 8, 2006
 *    CMM/PPCR No.		
 *    Programmer		Anthony Chan
 *    Description
 * 
 */
package com.dummy.wpb.product.component;

import com.dummy.wpb.product.email.EmailUtils;
import com.dummy.wpb.product.exception.DuplicateCheckedPdfException;
import com.dummy.wpb.product.exception.RecordNotFoundException;
import com.dummy.wpb.product.model.FinDocSmry;
import com.dummy.wpb.product.utils.Namefilter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;


@Slf4j
@Service
@NoArgsConstructor
public class PDFFileService {

    @Value("${finDoc.sysPath}")
    private static String finDocSysPath;

    @Value("${finDoc.http.pdf}")
    private static String finDocHttpPath;

    private FinDocSmry fds;

    public PDFFileService(FinDocSmry fds) {
        this.fds = fds;
    }

    public File fileToChk(String src, String desc) throws  IOException {
        File reqFile = null;
        FilenameFilter fnf = new Namefilter(fds.getDocIncmName(), false);

        File[] match = lsFileInFolder(desc, fnf);
        if (match.length > 0) {
            log.error("Method: fileToChk; Duplicate Document in chk path: " + fds.getDocIncmName());
            throw new DuplicateCheckedPdfException("Duplicate Document: " + fds.getDocIncmName());
        } else {
            match = lsFileInFolder(src, fnf);
            if (match.length > 0) {
                reqFile = match[0];
                File move = movePDF2Chked(reqFile, new File(desc));
                reqFile = move;
            } else {
                log.error("Method: fileToChk; Document not found: " + fds.getDocIncmName());
                throw new RecordNotFoundException("Document not found: " + fds.getDocIncmName());
            }
        }

        log.info(reqFile != null ? reqFile.getCanonicalPath()
            : ("File Not exist: " + fds.getDocIncmName()));

        return reqFile;
    }

    public File movePDF2Chked(File fm, File dsc) {
        boolean success = fm.renameTo(new File(dsc, fm.getName()));

        if (!success) {
            log.error("PDF file can not move to chked path: " + fm);
        }
        return new File(dsc, fm.getName());
    }

    public File fileToAprv(String src, String desc) throws RecordNotFoundException {
        File reqFile = null;
        FilenameFilter fnf = new Namefilter(fds.getDocIncmName(), false);

        File[] match = lsFileInFolder(src, fnf);

        if (match.length > 0) {
            reqFile = match[0];
            reqFile = movePDF2Aprv(reqFile, new File(desc));
        } else {
            log.error("fileToAprv; Document not found: " + fds.getDocIncmName());
            throw new RecordNotFoundException("Document not found: " + fds.getDocIncmName());
        }

        log.info(reqFile != null ? reqFile.getPath() : ("File Not exist: " + fds.getDocIncmName()));

        return reqFile;
    }

    public File movePDF2Aprv(File fm, File dsc) {
        String newName = EmailUtils.fillZero(fds.getDocSerNum().intValue(), 17) + "." + fm.getName();
        boolean success = fm.renameTo(new File(dsc, newName));
        if (!success) {
            log.error("PDF file can not move to aprv path: " + fm);
        }
        return new File(dsc, newName);
    }
    
    public File fileToRej(String src, String desc, FinDocSmry to) {
        File reqFile = null;
        FilenameFilter fnf = new Namefilter(fds.getDocIncmName(), false);
        File[] match = lsFileInFolder(src, fnf);
        if (match.length > 0) {
            reqFile = match[0];
            reqFile = movePDF2Rej(to.getCreatDt().toString() + to.getCreatTm().toString(), reqFile, new File(desc));
        }
        return reqFile;
    }

    public File movePDF2Rej(String newName, File fm, File dsc) {
        newName = newName.replace('-', 'n');
        newName = newName.replace(':', 'n');
        newName = newName.replace("T", "");
        newName = newName + "." + fm.getName();
        boolean success = fm.renameTo(new File(dsc, newName));
        if (!success) {
            log.error("PDF file can not move to rej path: " + fm);
        }
        return new File(dsc, newName);
    }

    public File getFile(String src) throws IOException{
        FilenameFilter fnf = new Namefilter(fds.getDocIncmName(), false);
        File[] match = lsFileInFolder(src, fnf);

        if (match.length > 0)
            return match[0];
        else {
            throw new IOException("File not found: " + fds.getDocIncmName());
        }
    }

    public File[] lsFileInFolder(String src, FilenameFilter fnf) {
        File srcDir = new File(src);
        checkDir(srcDir);
        return srcDir.listFiles(fnf);
    }

    public static void checkDir(File dir) {
        if (!dir.exists()) {
            try {
                boolean done = dir.createNewFile();
                if (done) {
                    log.info(dir.getPath() + " created");
                }
            } catch (IOException ioe) {
                log.error(dir.getPath() + " Could not make and exist");
            }
        }
    }

    public static String setFileUrl(File file, boolean isToHttp) {
        String filename = file.getName();
        File par = file.getParentFile();

        String foldername = par.getName();
        String url = null;
        if (isToHttp) {
            //@url = FilePathConstants.HTTP_PATH + foldername + "/" + filename;@
            url = StringUtils.isEmpty(finDocHttpPath) ? foldername + "/" + filename : finDocHttpPath + foldername + "/" + filename;
        } else {
            //@url = FilePathConstants.SYS_PATH + foldername + "/" + filename;@
            url = StringUtils.isEmpty(finDocSysPath) ? foldername + "/" + filename : finDocSysPath + foldername + "/" + filename;
        }
        return url;
    }
}