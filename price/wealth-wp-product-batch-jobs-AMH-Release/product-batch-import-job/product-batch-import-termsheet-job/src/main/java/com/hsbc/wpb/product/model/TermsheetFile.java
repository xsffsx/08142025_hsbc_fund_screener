package com.dummy.wpb.product.model;

import lombok.Data;

import java.io.File;

@Data
public class TermsheetFile {

    /** raw term sheet file */
    private File rawFile;

    /** term sheet file type, e.g. pdf */
    private String type;

    private String docFinTypeCde;

    private String langFinDocCde;

    private String ctryRecCde;

    private String grpMembrRecCde;

    private String prodTypeCde;

    private String prodAltPrimNum;

    public TermsheetFile(File file,
                         String type,
                         String docFinTypeCde,
                         String langFinDocCde,
                         String ctryRecCde,
                         String grpMembrRecCde,
                         String prodTypeCde,
                         String prodAltPrimNum) {
        // term sheet info
        this.rawFile = file;
        this.type = type;
        this.docFinTypeCde = docFinTypeCde;
        this.langFinDocCde = langFinDocCde;
        // product info
        this.ctryRecCde = ctryRecCde;
        this.grpMembrRecCde = grpMembrRecCde;
        this.prodTypeCde = prodTypeCde;
        this.prodAltPrimNum = prodAltPrimNum;
    }
}
