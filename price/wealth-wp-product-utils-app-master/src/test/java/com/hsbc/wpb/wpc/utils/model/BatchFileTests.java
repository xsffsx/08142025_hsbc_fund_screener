package com.dummy.wpb.wpc.utils.model;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BatchFileTests {

    @Test
    void testBatchFileConstructorForFile() throws IOException {
       String currentPath = new ClassPathResource("/").getFile().getAbsolutePath();
        File testFile = new File(currentPath + "/config_index.yml");
        BatchFile batchFile = new BatchFile(testFile);

        assertEquals("config_index.yml", batchFile.getName());
        assertEquals(testFile.getParentFile().getName(), batchFile.getParentName());
        assertEquals(testFile.getAbsolutePath(), batchFile.getAbsolutePath());
        assertEquals("file", batchFile.getType());
        assertEquals(testFile.length(), batchFile.getSize());
        assertEquals(
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(testFile.lastModified())),
            batchFile.getUpdateTime()
        );
    }

    @Test
    void testBatchFileConstructorForDirectory() throws IOException {
       String currentPath = new ClassPathResource("/").getFile().getAbsolutePath();
        File folder = new File(currentPath + "/com");
        BatchFile batchFile = new BatchFile(folder);

        assertEquals("com", batchFile.getName());
        assertEquals(folder.getParentFile().getName(), batchFile.getParentName());
        assertEquals(folder.getAbsolutePath(), batchFile.getAbsolutePath());
        assertEquals("directory", batchFile.getType());
        assertEquals(0, batchFile.getSize());
        assertEquals(
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(folder.lastModified())),
            batchFile.getUpdateTime()
        );
    }

}