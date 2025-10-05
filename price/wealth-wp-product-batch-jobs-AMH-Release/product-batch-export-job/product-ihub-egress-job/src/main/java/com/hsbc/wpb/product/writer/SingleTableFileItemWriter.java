package com.dummy.wpb.product.writer;

import com.google.common.base.CaseFormat;
import com.dummy.wpb.product.config.loader.ExportConfigLoader;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class SingleTableFileItemWriter extends TableFileItemWriter {

    private final List<String> headers;

    private final List<String> attrNames;

    public SingleTableFileItemWriter(String tableName, String outputPath) {
        super(tableName, outputPath);

        this.headers = ExportConfigLoader.getSingleTableFields(tableName);

        this.attrNames = headers.stream().map(header -> CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, header)).collect(Collectors.toList());
    }

    @Override
    protected List<String> aggregate(Document document) {
        List<String> values = new ArrayList<>();
        for (String attrName : attrNames) {
            Object value = document.get(attrName);
            values.add(convertValueToStr(value, attrName.endsWith("DtTm")));
        }
        return Collections.singletonList(StringUtils.join(values, CSV_DELIMITER));
    }

    @Override
    protected List<String> getHeaders() {
        return headers;
    }

}
