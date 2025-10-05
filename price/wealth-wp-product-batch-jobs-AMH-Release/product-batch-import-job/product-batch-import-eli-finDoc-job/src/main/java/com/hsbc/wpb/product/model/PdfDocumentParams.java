package com.dummy.wpb.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;

@Data
@AllArgsConstructor
public class PdfDocumentParams {
    private File file;
    private String pdfName;
    private String prodSubtpCde;
    private boolean flag;
    private boolean isrInfoDoc;
    private String eliPayoffTypeCde;
    private Double distbrFeePct;
    private String prodOrig;
    private String tProdAltPrimNum;
    private String eliIssuer;
    private String custTier;
}
