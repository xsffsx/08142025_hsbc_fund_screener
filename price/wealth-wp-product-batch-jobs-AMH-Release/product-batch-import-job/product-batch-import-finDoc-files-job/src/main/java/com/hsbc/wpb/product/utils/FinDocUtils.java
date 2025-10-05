package com.dummy.wpb.product.utils;


import com.dummy.wpb.product.constant.FinDocConstants;

import java.io.*;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class FinDocUtils {
    
    public final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    public final SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");

    public Date toDate(String str) {
        try {
            if (str == null || str.equals(" "))
                return null;
            else {
                dateFormat.setLenient(false);
                return new Date(dateFormat.parse(str)
                    .getTime());}
            
        } catch (ParseException e) {
            return null;
        }
    }

    public Time toTime(String str) {
        try {
            if (str == null || str.equals(" ")) {
                return null;
            } else {
                dateFormat.setLenient(false);
                return new Time(timeFormat.parse(str)
                        .getTime());
            }
        } catch (ParseException e) {
            return null;
        }
    }
    
    public static File chkAndCreate(File file) throws IOException{
        File rtrn = null;
        if (file.exists()){
            rtrn = copyAndRenameFile(file);
        } 
        return rtrn;
    }
    
    public static String curTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cal = new GregorianCalendar();
        return df.format(cal.getTime());
    }
    
    public static File copyAndRenameFile(File inputFile) throws IOException {
        String temp = curTime()+".tmp";
        boolean success;
        File output = new File(inputFile.getParent(), temp);
        try (FileInputStream in = new FileInputStream(inputFile); FileOutputStream out = new FileOutputStream(output)) {

            success = false;

            byte[] buf = new byte[1024];
            int c;
            while ((c = in.read(buf)) != -1)
                out.write(buf, 0, c);
        } catch (IOException e) {
            throw new IOException(e);
        }
        if (output.length() == inputFile.length()) {
            Files.delete(inputFile.toPath());
            success = output.renameTo(new File(inputFile.getParent(), inputFile.getName()));
        }

        if (success) {
            return new File(inputFile.getParent(), inputFile.getName());
        }
        return null;
    }

    public static   String replaceName(String name, String originPattern, String newPattern) {
        String newName = null;
        int loc = name.toLowerCase().lastIndexOf(originPattern);
        if (loc >= 0) {
            newName = name.substring(0, loc) + newPattern;
        } else {
            newName = name;
        }
        return newName;
    }

    public static  String getcurTimeDDMMMYY() {
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yy");
        return df.format(cal.getTime()) + " on "
                + df2.format(cal.getTime());
    }

    public static boolean checkAck(File file) throws IOException {
        String name = "";
        if (file.getName().toLowerCase().endsWith(FinDocConstants.SUFFIX_CSV)) {
            name = FinDocUtils.replaceName(file.getName(), FinDocConstants.SUFFIX_CSV, FinDocConstants.SUFFIX_ACK);

        } else if (file.getName().toLowerCase().endsWith(FinDocConstants.SUFFIX_XLS)) {
            name = FinDocUtils.replaceName(file.getName(), FinDocConstants.SUFFIX_XLS, FinDocConstants.SUFFIX_ACK);

        } else if (file.getName().toLowerCase().endsWith(FinDocConstants.SUFFIX_PDF)) {
            name = FinDocUtils.replaceName(file.getName(), FinDocConstants.SUFFIX_PDF, FinDocConstants.SUFFIX_ACK);
        } else {
            return false;
        }
        File ackfile = new File(file.getParent(), name);
        if (ackfile.exists()) {
            Files.delete(ackfile.toPath());
            return true;
        }
        return false;
    }
}
