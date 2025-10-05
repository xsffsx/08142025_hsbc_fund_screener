package com.dummy.wpb.product.writer;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.item.support.AbstractFileItemWriter;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public abstract class TableFileItemWriter extends AbstractFileItemWriter<Document> {

    protected static final String CSV_DELIMITER = ",";

    protected final String tableName;

    protected TableFileItemWriter(String tableName, String outputPath) {

        this.tableName = tableName;

        this.setName(tableName);
        this.setResource(getResource(tableName, outputPath));

        this.setHeaderCallback(writer -> {
            // Add bom flag to avoid gibberish
            writer.write('\ufeff');
            writer.write(StringUtils.join(getHeaders(), CSV_DELIMITER));
        });
    }


    @Override
    protected String doWrite(List<? extends Document> products) {
        List<String> lines = products.stream()
                .map(this::aggregate)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        String content = StringUtils.join(lines, lineSeparator);
        if (!lines.isEmpty()) {
            content += lineSeparator;
        }

        return content;
    }

    protected abstract List<String> aggregate(Document item);

    protected abstract List<String> getHeaders();

    protected Resource getResource(String tableName, String outputPath) {
        String fileName = String.format("%s_%s.csv", tableName, ZonedDateTime.now(ZoneId.of("GMT+8")).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        Path path = Paths.get(outputPath, fileName);
        return new PathResource(path);
    }

    protected String convertValueToStr(Object object) {
        return this.convertValueToStr(object, false);
    }

    protected String convertValueToStr(Object object, boolean toDateTimeIfDate) {
        String result;
        if (object instanceof Date) {
            if (toDateTimeIfDate) {
                result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(object);
            } else {
                result = new SimpleDateFormat("yyyy-MM-dd").format(object);
            }
        } else if (object instanceof List) {
            result = StringUtils.join((List<Object>) object, ",");
        } else if (object instanceof String) {
            result = correctText((String) object);
        } else {
            result = Objects.toString(object, EMPTY);
        }


        if (StringUtils.isNotBlank(result) && !(object instanceof Number)) {
            //Avoid line breaks caused by commas in text
            result = String.format("\"%s\"", result);
        }

        return result;
    }

    protected String correctText(String content) {
        content = StringUtils.replaceChars(content, '"', '\'');
        // handle the case cause by Upload Form
        content = content.replaceAll("[\r\n]", " ");
        // handle the case cuase by Staff Maint Screen
        content = StringUtils.replace(content, "&#xd;", "");
        return content;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

}
