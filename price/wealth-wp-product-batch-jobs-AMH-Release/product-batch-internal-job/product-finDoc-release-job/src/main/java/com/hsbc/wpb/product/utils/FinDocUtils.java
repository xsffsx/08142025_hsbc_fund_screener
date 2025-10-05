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


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FinDocUtils {

    private FinDocUtils() {
        throw new IllegalStateException("FinDocUtils is Utility class");
    }

    public static boolean createList(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            Files.delete(Paths.get(path));
        }
        if (!file.exists()) {
            return file.createNewFile();
        }
        return true;
    }

    public static String replaceName(String name,
                                     String oPat,
                                     String nPat) {
        String newName = null;
        int loc = name.toLowerCase().lastIndexOf(oPat);
        if (loc >= 0) {
            newName = name.substring(0, loc) + nPat;
        } else {
            newName = name;
        }
        return newName;
    }
}
