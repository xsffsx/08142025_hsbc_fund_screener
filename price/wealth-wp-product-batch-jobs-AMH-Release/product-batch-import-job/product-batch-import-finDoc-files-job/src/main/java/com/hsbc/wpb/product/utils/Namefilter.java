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

    public boolean accept(File dir, String inputName) {
        for (int i=0; i < filename.length; i++){
            if (matching(inputName, filename[i], prefix, allUpperCase))
                return true;
        }
        return false;
    }

    public boolean matching (String inputName, String filename, boolean prefix, boolean allUpperCase){
        if (allUpperCase){
            if (prefix){
                return inputName.toUpperCase().startsWith(filename);
            } 
                return inputName.toUpperCase().endsWith(filename);
        }
        if (prefix){
            return inputName.startsWith(filename);
        } 
            return inputName.endsWith(filename);
        
    }
}