package com.dummy.wpb.product.writer;

import com.dummy.wpb.product.config.loader.DBConfigLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TableFileItemWriterFactory {

    private TableFileItemWriterFactory() {
    }

    public static TableFileItemWriter createProductTableWriter(String tableName, String outputPath) {

        if (null != DBConfigLoader.getMetadata(tableName)) {
            return new ProductTableFileItemWriter(tableName, outputPath);
        }

        if (null != DBConfigLoader.getUserDefinedFieldConfig(tableName)) {
            return new ExtendFieldTableItemWriter(tableName, outputPath);
        }

        log.error("Invalid tableName: " + tableName);
        return null;
    }
}
