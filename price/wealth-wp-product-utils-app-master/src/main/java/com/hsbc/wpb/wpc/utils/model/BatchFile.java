package com.dummy.wpb.wpc.utils.model;

import lombok.Data;
import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;

@Data
public class BatchFile {
    private String name;
    private String parentName;
    private String absolutePath;
    private String type;
    private long size;
    private String updateTime;

    public  BatchFile(File file) {
        this.name= file.getName();
        this.parentName = file.getParentFile().getName();
        this.absolutePath= file.getAbsolutePath();
        this.type = file.isDirectory() ? "directory" : "file";
        this.size = file.isDirectory()? 0 : file.length();
        this.updateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(file.lastModified()));
    }
}