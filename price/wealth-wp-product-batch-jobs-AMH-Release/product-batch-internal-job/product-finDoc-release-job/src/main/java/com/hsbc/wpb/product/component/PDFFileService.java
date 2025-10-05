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

import com.dummy.wpb.product.exception.RecordNotFoundException;
import com.dummy.wpb.product.model.FinDocSmry;
import com.dummy.wpb.product.utils.Namefilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;


@Slf4j
public class PDFFileService {

    @Value("${finDoc.sysPath}")
    private static String finDocSysPath;

    @Value("${finDoc.http.pdf}")
    private static String finDocHttpPath;

    private FinDocSmry fds;

    public PDFFileService(FinDocSmry fds) {
        this.fds = fds;
    }

    public File fileToAprvToDone(String src, String desc) throws IOException {
        File reqFile = null;
        FilenameFilter fnf;
        boolean needRename =  false;
        if (desc.toLowerCase().contains("aprv")) { //original name from chk folder to aprv
            fnf = new Namefilter(fds.getDocIncmName()
                    .trim(), false);
            needRename = true;
        } else { //name with resource item id from aprv/done/arch folder to done/arch/del
            fnf = new Namefilter(fillzero(fds.getDocSerNum(), 17) + "."
                    + fds.getDocIncmName().trim(), false);
            needRename =  false;
        }

        File[] match = lsFileInFolder(src, fnf);

        if (match.length > 0) {
            reqFile = match[0];
            File move = movePDF2AprvToDone(reqFile, new File(desc),needRename);
            reqFile = move;
        } else {
            throw new RecordNotFoundException("Document not found: " + fds.getDocIncmName());
        }

        log.info(reqFile != null ? reqFile.getPath()
            : ("File Not exist: " + fds.getDocIncmName()));

        return reqFile;
    }

    public File movePDF2AprvToDone(File fm, File dsc, boolean needRename) throws IOException {
        String fileName;
        if(needRename){
            fileName = fillzero(fds.getDocSerNum(), 17) + "."
                    + fm.getName();
        }else {
            fileName = fm.getName();
        }
        boolean success = fm.renameTo(new File(dsc, fileName));
        if (!success) {
        	log.error("PDF file can not move " + fm);
        }
        return new File(dsc, fileName);
    }
    
    public File fileToRej(String src, String desc, FinDocSmry to) throws IOException {
        File reqFile = null;
        FilenameFilter fnf = new Namefilter(fds.getDocIncmName()
            .trim(), false);
        File[] match = lsFileInFolder(src, fnf);
        if (match.length > 0) {
            reqFile = match[0];
            File move = movePDF2Rej(to, reqFile, new File(desc));
            reqFile = move;
        } else {
            throw new RecordNotFoundException("Document not found: " + fds.getDocIncmName());
        }
        return reqFile;
    }

    public File movePDF2Rej(FinDocSmry to, File fm, File dsc) {
        String newName = null;
        newName = to.getCreatDt().toString()
            + to.getCreatTm().toString();        
        newName = newName.replace('-', 'n');
        newName = newName.replace(':', 'n');
        newName = newName + "." + fm.getName();
        boolean success = fm.renameTo(new File(dsc, newName));
        if (!success) {
            log.error("PDF file can not move " + fm);
        }
        return new File(dsc, newName);
    }

    public File[] lsFileInFolder(String src, FilenameFilter fnf) throws IOException {
        File srcDir = new File(src);
        checkDir(srcDir);
        return srcDir.listFiles(fnf);
    }

    public static void checkDir(File dir) throws IOException {
        if (!dir.exists()) {
            try {
                boolean done = dir.createNewFile();
                if (done) {
                    log.info(dir.getPath() + " created");
                }
            } catch (IOException ioe) {
                log.error(dir.getPath()
                    + " Could not make and exist");
            }
        }
    }

    private String fillzero(Long k, int length) {

        String text = "";
        if (k < 0L)
            k = 0L;
        text = text + k;
        int lh = text.length();

        StringBuilder stringBuilder = new StringBuilder(text);
        for (int i = 1; i <= (length - lh); i++) {
            stringBuilder.insert(0, "0");
        }
        return stringBuilder.toString();
    }

    public static String setFileUrl(File file, boolean toHttp) {
        String filename = file.getName();
        File par = file.getParentFile();

        String foldername = par.getName();
        String url = null;
        if (toHttp) {
            url = StringUtils.isEmpty(finDocHttpPath) ? foldername + "/" + filename : finDocHttpPath + foldername + "/" + filename;
        } else {
            url = StringUtils.isEmpty(finDocSysPath) ? foldername + "/" + filename : finDocSysPath + foldername + "/" + filename;
        }
        return url;
    }
}