package com.dummy.wpb.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;

@Data
@AllArgsConstructor
public class MergeFileParams {
    private File file;
    private String prodOrig;
    private String importantFileApendix;
    private String issueInfoDoc;
    private String mergeFile;
    private String newFileName;
    private String importantFile;
    private String prodTypeCde;
    private String importantFileEnd;
    private String tProdAltPrimNum;
    private String eliIssuer;

}
