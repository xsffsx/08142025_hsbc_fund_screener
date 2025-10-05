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
 * Class Name		EmailFormation
 *
 * Creation Date	Mar 7, 2006
 * 
 * Abstract			This class provides email content formation functions.
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Mar 7, 2006
 *    CMM/PPCR No.		
 *    Programmer		Andy Man
 *    Description
 * 
 */
package com.dummy.wpb.product.email;


import com.dummy.wpb.product.constant.EmailConstants;
import com.dummy.wpb.product.constant.EmailContent;
import com.dummy.wpb.product.script.ChmodScript;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


//This class provides email content formation functions.
@Slf4j
public class EmailFormation {

    //This method creates the email content.
    public void emailFormation(String sender, EmailContent[] emCnts, String chmodScriptPath, String rejLogPath) {
        EmailUtils eu = new EmailUtils();
        String curtime = EmailUtils.curTime();
        headerFormation(eu.fileList(), curtime, rejLogPath);
        emailCntFormation(sender, emCnts, curtime, rejLogPath);
        trailerFormation(eu.fileList(), curtime, emCnts, chmodScriptPath, rejLogPath);
    }

    //This method creates the email header content.
    public void headerFormation(List<String> aList, String curtime, String rejLogPath) {
        try {
            EmailUtils eu = new EmailUtils();

            for (int i = 0; i < aList.size(); i++) {
                String path = rejLogPath + aList.get(i) + curtime;

                eu.checkDir(path);

                String header = null;
                if (aList.get(i).equals(EmailConstants.HFIK)) {
                    header = EmailConstants.HEAD1
                        + EmailConstants.KEYFILENAME + EmailConstants.HEAD2
                        + curtime + "P"; //"T" - testing 
                } else {
                    header = EmailConstants.HEAD1
                        + EmailConstants.MESSAGEFILENAME + EmailConstants.HEAD2
                        + curtime + "P";

                }
                String ctxt = header
                    + EmailUtils.printSpace(80 - header.length());
                writeLine(ctxt, path);
            }
        } catch (IOException e) {
            log.error("Method headerFormation error: " + e.getMessage());
        }
    }

    //This method creates the content of the email.
    public void emailCntFormation(String sender, EmailContent[] emCnts,
        String curtime, String rejLogPath) {
        try {
            String path = rejLogPath + EmailConstants.HFIK + curtime;

            writeLine(EmailConstants.TYPE1COD + sender, path);
            for (int i = 0; i < emCnts.length; i++) {
                //limit the subject length <=60
                char[] subject = new char[60];
                String subjectStr;
                if(emCnts[i].getSubject().length()>60){
                    emCnts[i].getSubject().getChars(0,59,subject,0);
                    subjectStr = Arrays.toString(subject);
                }else{
                    subjectStr = emCnts[i].getSubject();
                }
                
                String add = EmailUtils.chkRvcrAdd(emCnts[i].getRecptAdr());
                String ctxt = EmailConstants.TYPE2COD
                    + add
                    + EmailUtils.printSpace(50 - add.length())
                    + subjectStr
                    + EmailUtils.printSpace(60 - subjectStr.length());

                if (emCnts[i].getContent() == null
                    || emCnts[i].getContent().equals("")) {
                    ctxt = ctxt + EmailUtils.printSpace(100);
                } else {
                    ctxt = ctxt
                        + emCnts[i].getContent()
                        + EmailUtils.printSpace(100 - emCnts[i].getContent()
                            .length());
                }

                writeLine(ctxt, path);
            }
        } catch (IOException e) {
            log.error("Method emailCntFormation error: " + e.getMessage());
        }
    }

    //This method creates the email trailer.
    public void trailerFormation(List<String> aList, String curtime, EmailContent[] emCnts,
                                 String chmodScriptPath, String rejLogPath) {
        final int REC_COUNT = emCnts.length + 1;

        try {
            for (int i = 0; i < aList.size(); i++) {
                String path = rejLogPath + aList.get(i) + curtime;

                String trailer = null;
                if (aList.get(i).equals(EmailConstants.HFIK)) {
                    trailer = EmailConstants.TRAILER1
                        + EmailConstants.KEYFILENAME
                        + EmailConstants.TRAILER2
                        + EmailUtils.fillZero(REC_COUNT, 7)
                        + EmailUtils.fillZero(REC_COUNT, 15) + "+";
                } else {
                    trailer = EmailConstants.TRAILER1
                        + EmailConstants.MESSAGEFILENAME
                        + EmailConstants.TRAILER2 + EmailUtils.fillZero(0, 7)
                        + EmailUtils.fillZero(0, 15) + "+";
                }

                String ctxt = trailer
                    + EmailUtils.printSpace(80 - trailer.length());

                writeLine(ctxt, path);

                ChmodScript.chmodScript(path, "777", chmodScriptPath);
            }
        } catch (IOException e) {
            log.error("Method trailerFormation error: " + e.getMessage());
        }
    }

    //This method writes content to the output.
    public void writeLine(String str, String path) throws IOException {

        try(FileOutputStream fo2 = new FileOutputStream(path, true); DataOutputStream dos = new DataOutputStream(fo2)) {
            dos.writeBytes(str);
            dos.writeBytes("\n");
        }
    }
}