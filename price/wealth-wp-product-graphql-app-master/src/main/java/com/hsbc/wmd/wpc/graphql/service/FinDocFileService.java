package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FinDocFileService {

    @Value("${product.finDoc.path}")
    private String finDocPath;

    public Path getPath(String filename) {
        if(filename.contains("/") || filename.contains("\\") || !StringUtils.endsWithIgnoreCase(filename,".pdf")) {
            throw new IllegalArgumentException("Invalid filename: " + filename);
        }
        return CommonUtils.toCanonicalPath(Paths.get(finDocPath, filename));
    }
}
