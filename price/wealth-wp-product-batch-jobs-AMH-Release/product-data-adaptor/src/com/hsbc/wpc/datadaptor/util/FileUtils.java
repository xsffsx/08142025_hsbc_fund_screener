package com.dummy.wpc.datadaptor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 
 * @author WMDHKG0007
 * 
 */
public class FileUtils {
	/*
	 * whether the path is a directory
	 */
	public static boolean isFolder(String path) {
		File file = new File(path);
		if (file.exists()) {
			if (file.isDirectory()) {
				return true;
			} else {
				return false;
			}
		} else {
			// if(path.endsWith(File.separator)){
			if (path.endsWith("/") || path.endsWith("\\")) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public static void copyFile(String oldPath, String newPath) {
        try {  
            int bytesum = 0;  
            int byteread = 0;  
            File oldfile = new File(oldPath);  
            if (oldfile.exists()) { 
                InputStream inStream = new FileInputStream(oldPath); 
                FileOutputStream fs = new FileOutputStream(newPath);  
                byte[] buffer = new byte[1444];  
                while ( (byteread = inStream.read(buffer)) != -1) {  
                    bytesum += byteread;
                    System.out.println(bytesum);  
                    fs.write(buffer, 0, byteread);  
                }  
                inStream.close();  
                fs.close();  
            }  
        }  
        catch (Exception e) {  
            System.out.println("copy file error");  
            e.printStackTrace();  
        }  
    }
}
