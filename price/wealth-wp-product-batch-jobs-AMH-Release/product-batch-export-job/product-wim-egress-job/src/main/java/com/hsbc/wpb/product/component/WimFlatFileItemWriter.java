package com.dummy.wpb.product.component;

import com.dummy.wpb.product.configuration.ExportFieldConfig;
import com.dummy.wpb.product.model.TableInfo;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.joda.time.DateTime;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.core.io.FileSystemResource;

import java.nio.file.Paths;
import java.util.List;

public class WimFlatFileItemWriter extends FlatFileItemWriter<Document> {

    private static final String CSV_DELIMITER = "|";

    private String outPutPath;

    public WimFlatFileItemWriter(String tableName,String outPutPath) {
        super();

        this.outPutPath = outPutPath;
        TableInfo tableInfo = ExportFieldConfig.getTableInfo(tableName);
        this.setName(tableName);
        this.setResource(new FileSystemResource(getOutPutPath(tableName)));
        this.setLineAggregator(new JsonPathLineAggregator(tableInfo));
        this.setHeaderCallback(writer -> {
            // Add bom flag to avoid gibberish
            writer.write('\ufeff');
            writer.write(StringUtils.join(tableInfo.getFieldMapping().keySet(), CSV_DELIMITER));
        });
    }

    @Override
    public String doWrite(List<? extends Document> items) {
        return super.doWrite(items).replaceAll("(?m)^[ \t]*\r?\n", "");
    }

    private String getOutPutPath(String tableName) {
        String fileName = String.format("%s_%s.csv", tableName, new DateTime().toString("yyyyMMdd"));
        return Paths.get(outPutPath, fileName).toString();
    }
}
