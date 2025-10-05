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
 * Class Name		Namefilter
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
package com.dummy.wpb.product.utils;

import java.io.File;
import java.io.FilenameFilter;

public class Namefilter implements FilenameFilter {

    private String[] filename;
    private boolean prefix; 
    private boolean allUpperCase = false;
    public Namefilter(String filename, boolean prefix) {       
        this.filename = new String[]{filename};
        this.prefix = prefix;
    }

    public Namefilter(String[] filename, boolean prefix){
        this.filename = filename;
        this.prefix = prefix;
    }


    public Namefilter(String[] filename, boolean prefix, boolean allUpperCase){
        this.filename = filename;
        this.prefix = prefix;
        this.allUpperCase = allUpperCase;
    }

    public boolean accept(File dir, String name) {
        if (filename == null)
            return false;
        for (int i=0; i < filename.length; i++){
            if (matching(name, filename[i], prefix, allUpperCase))
                return true;
        }
        return false;
    }

    public boolean matching (String name, String filename, boolean prefix, boolean allUpperCase){
        if (allUpperCase){
            if (prefix){
                return name.toUpperCase().startsWith(filename);
            } 
                return name.toUpperCase().endsWith(filename);            
        }
        if (prefix){
            return name.startsWith(filename);
        } 
            return name.endsWith(filename);
        
    }

}