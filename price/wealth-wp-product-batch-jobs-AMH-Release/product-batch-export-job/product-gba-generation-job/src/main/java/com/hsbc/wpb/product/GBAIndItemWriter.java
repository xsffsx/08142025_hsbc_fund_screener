package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.Field;
import org.bson.Document;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.apache.commons.lang.StringUtils.EMPTY;

public class GBAIndItemWriter extends FlatFileItemWriter<Document> {

    @Value("#{jobParameters['ctryRecCde']}")
    String ctryRecCde;
    @Value("#{jobParameters['grpMembrRecCde']}")
    String grpMembrRecCde;
    @Value("#{jobParameters['systemCde']}")
    String systemCde;
    @Value("#{jobParameters['prodTypeCde']}")
    String prodTypeCde;
    @Value("#{jobParameters['outputPath']}")
    String outputPath;

    @Override
    public void afterPropertiesSet() throws Exception {
        setName("gbaIndWriter");
        setAppendAllowed(true);
        setLineAggregator(doc -> doc.getString(Field.prodAltPrimNum) + doc.get(Field.gbaAcctTrdb, EMPTY) + doc.get(Field.gnrAcctTrdb, EMPTY));
        String fileName = ctryRecCde + grpMembrRecCde + systemCde + prodTypeCde
                + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + ".txt";
        setResource(new FileSystemResource(Paths.get(outputPath, fileName).toString()));
        super.afterPropertiesSet();
    }

    @Override
    public String doWrite(List<? extends Document> items) {
        String currentTime = "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "]";
        return currentTime + lineSeparator + super.doWrite(items) + lineSeparator;
    }
}
