package com.dummy.wpb.product.model;

import lombok.Data;

@Data
public class ExportFile {
    private InterfaceType interfaceType;
    private String fileName = "${ctryRecCde}_${grpMembrRecCde}_${systemCde}_${interface}_${dateTime}_${sequence}.{fileType}";
    private String templateName;
    private String className;
    private String name = "";
    private String sequenceKey;
}
