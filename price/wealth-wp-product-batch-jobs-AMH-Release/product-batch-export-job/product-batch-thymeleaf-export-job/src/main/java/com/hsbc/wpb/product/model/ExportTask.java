package com.dummy.wpb.product.model;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import lombok.Data;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExportTask {
    private CollectionName collection;
    private Document filter = new Document();
    private Document sort = new Document();
    private String className;
    private String deltaKey;
    private String deltaField = Field.recUpdtDtTm;
    private List<ExportFile> files = new ArrayList<>();
}
