package com.dummy.wpb.product.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.Document;

import java.util.List;

@Data
@NoArgsConstructor
public class ExportRequest {
    private String ctryRecCde;
    private String grpMembrRecCde;
    private String systemCde;
    private String path;
    private List<ExportTask> tasks;
    private ExportAck ack = new ExportAck();
    private String timeZone;
    private Document params = new Document();
}
