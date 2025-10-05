package com.dummy.wpb.product.utils;

import com.dummy.wpb.product.exception.productBatchException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class CommonUtils {

    public static String readResource(String classpath) {
        try {
            return StreamUtils.copyToString(new ClassPathResource(classpath).getInputStream(), Charset.defaultCharset());
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Resource not found: " + classpath);
        } catch (IOException e) {
            throw new productBatchException("Error reading resource: " + classpath, e);
        }
    }

    public static List<File> scanFileInPath(File path, String fileNamePattern, String suffix) {
        List<File> fileList = new ArrayList<>();
        if (path.isDirectory()) {
            FilenameFilter filter = (dir, name) -> name.contains(fileNamePattern);
            File[] files = path.listFiles(filter);
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(suffix)) {
                        fileList.add(file);
                    }
                }
            }
        }
        return fileList;
    }

    public static List<File> scanFileInPathWithPattern(String incomingPath, String fileNamePattern) {
        File filePath = new File(incomingPath);
        List<File> fileList = new ArrayList<>();
        if (filePath.isDirectory()) {
            FilenameFilter filter = (dir, name) -> name.matches(fileNamePattern);
            File[] files = filePath.listFiles(filter);
            if (files != null) {
                fileList.addAll(Arrays.asList(files));
            }
        }
        return fileList;
    }

    /**
     * Add file suffix
     * @param file
     * @param suffix
     * @return [original filename] + [.suffix] 
     */
    public static boolean addFileSuffix(File file, String suffix) {
        File newFile = new File(file.getAbsolutePath() + "." + suffix);
        return file.renameTo(newFile);
    }

    public static  boolean isEmptyList(List<?> list) {
        return list == null || list.isEmpty() || list.stream().allMatch(Objects::isNull);
    }
}
