package com.dummy.wpb.wpc.utils;

import com.google.common.io.ByteStreams;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {
    public static String readResource(String classpath) {
        URL url = CommonUtils.class.getResource(classpath);
        if(null == url){
            throw new IllegalArgumentException("Resource not found: " + classpath);
        }
        try {
            return StreamUtils.copyToString(new ClassPathResource(classpath).getInputStream(), Charset.defaultCharset());
        } catch (Exception e){
            throw new IllegalStateException("Error reading resource: " + classpath, e);
        }
    }

    public static String exceptionInfo(Exception e){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(os));
        return e.getMessage() + "\n" + os.toString();
    }

    /**
     * Retrieve zip entries into a map of path -> content
     *
     * @param zis
     * @return
     */
    public static Map<String, String> getZipEntryMap(ZipInputStream zis) throws IOException {
        Map<String, String> entryMap = new LinkedHashMap<>();
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            if (zipEntry.isDirectory()) {
                zipEntry = zis.getNextEntry();   // ignore directory
                continue;
            }

            // write file content
            try(ByteArrayOutputStream fos = new ByteArrayOutputStream()) {
                ByteStreams.copy(zis, fos);
                String path = zipEntry.getName();
                String content = fos.toString("UTF-8");
                entryMap.put(path, content);
            }

            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        return entryMap;
    }

    public static Map<String, String> readSchemaFile(Map<String, String> fileMap) throws IOException {
        URL url = CommonUtils.class.getResource("/schema");
        File schema = new File(url.getFile());
        File[] schemaFileList = schema.listFiles();
        for (File schemaFile : schemaFileList){
            try(ByteArrayOutputStream fos = new ByteArrayOutputStream()) {
                ByteStreams.copy(new FileInputStream(schemaFile), fos);
                String content = fos.toString("UTF-8");
                fileMap.put("schema/"+schemaFile.getName(), content);
            }
        }

        return fileMap;
    }

    public static String getHostname() {
        String hostName = System.getenv("HOSTNAME");
        if(null != hostName) return hostName;

        hostName = System.getenv("COMPUTERNAME");

        return null == hostName? "" : hostName;
    }
}
