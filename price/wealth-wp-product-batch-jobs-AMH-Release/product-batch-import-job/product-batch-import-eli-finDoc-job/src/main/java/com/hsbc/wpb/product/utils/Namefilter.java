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

    public Namefilter(String filename, boolean prefix, boolean allUpperCase) {       
        this.filename = new String[]{filename};
        this.prefix = prefix;
        this.allUpperCase = allUpperCase;
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