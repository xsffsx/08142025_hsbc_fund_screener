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
 * Class Name		Utils
 *
 * Creation Date	Feb 4, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Feb 4, 2006
 *    CMM/PPCR No.		
 *    Programmer		Anthony Chan
 *    Description
 * 
 */
package com.dummy.wpb.product.utils;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class FinDocUtils {

    private FinDocUtils() {
        throw new IllegalStateException("FinDocUtils is Utility class");
    }

    public static File chkAndCreate(File fm) throws IOException{
        File rtrn = null;
        if (fm.exists()){
            rtrn = copyAndRenameFile(fm);          
        } 
        return rtrn;
    }
    
    public static String curTime() {
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(cal.getTime());
    }
    
    public static File copyAndRenameFile(File input) throws IOException{
        String temp = curTime()+".tmp";
        File output = new File(input.getParent(), temp);

        try (FileInputStream in = new FileInputStream(input);
             FileOutputStream out = new FileOutputStream(output)) {

            boolean success = false;

            byte[] buf = new byte[1024];
            int c;
            while ((c = in.read(buf)) != -1)
                out.write(buf, 0, c);

            if (output.length() == input.length()) {
                Files.delete(Paths.get(input.getPath()));
                success = output.renameTo(new File(input.getParent(), input.getName()));
            }

            if (success)
                return new File(input.getParent(), input.getName());
            return null;
        }
    }

}
