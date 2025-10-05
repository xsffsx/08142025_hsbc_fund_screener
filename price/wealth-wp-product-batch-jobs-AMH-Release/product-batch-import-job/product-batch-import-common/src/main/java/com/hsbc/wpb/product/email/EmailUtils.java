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
 * Class Name		EmailUtils
 *
 * Creation Date	Mar 7, 2006
 * 
 * Abstract			This class provides email utility.
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	
 *    CMM/PPCR No.		
 *    Programmer		
 *    Description
 * 
 */
package com.dummy.wpb.product.email;

import com.dummy.wpb.product.constant.EmailConstants;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * This class provides email utility.
 */
@Slf4j
public class EmailUtils {

    public List<String> fileList() {
        List<String> mail = new ArrayList<>();
        mail.add(EmailConstants.HFIK);
        mail.add(EmailConstants.HFIM);
        return mail;
    }

    public static String chkRvcrAdd(String add) {
        if (add == null)
            return EmailConstants.FDDFLTRCVR;
        return add;
    }

    public static String fillZero(int k, int length) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(k);

        int lh = stringBuilder.length();

        for (int i = 1; i <= (length - lh); i++) {
            stringBuilder.insert(0, "0");
        }
        return stringBuilder.toString();
    }

    public static String printSpace(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        if (i > 0)
            for (int j = 0; j < i; j++)
                stringBuilder.append(" ");
        return stringBuilder.toString();
    }

    public void checkDir(String path) throws IOException {
        File dir = new File(path);
        if (!dir.exists()) {
            try {
                boolean done = dir.createNewFile();
                if (done) {
                    log.info(dir.getPath() + " created");
                }
            } catch (IOException ioe) {
                log.info(dir.getPath() + " Could not make and exist");
            }
        }

    }

    public static String curTime() {
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(cal.getTime());
    }

}