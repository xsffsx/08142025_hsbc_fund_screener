package com.dummy.wpb.product.reader;


import com.dummy.wpb.product.model.WrtsUndlStockRecord;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.dummy.wpb.product.constant.WrtsUndlStockRecordColumnName.*;
import static com.dummy.wpb.product.constant.WrtsUndlStockRecordType.DETAIL_RECORD_TYPE;


@Configuration
public class WrtsUndlStockReaderConfiguration {

    @Value("${batch.file-pattern}")
    private String filePattern;

    private static final String DEFAULT_RECORD_PATTERN = "*";

    private static final String DETAIL_RECORD_PATTERN = DETAIL_RECORD_TYPE + "*";

    @Bean
    @StepScope
    public ItemStreamReader<WrtsUndlStockRecord> wrtsUndlStockReader(
            @Value("#{jobParameters['incomingPath']}") String incomingPath) {
        FlatFileItemReader<WrtsUndlStockRecord> itemReader = new FlatFileItemReader<>();
        itemReader.setEncoding("Big5-HKSCS");
        itemReader.setLineMapper(wrtsUndlStockLineMapper());
        return new ImportFileItemReader<>(incomingPath, filePattern, itemReader);
    }

    @Bean
    public PatternMatchingCompositeLineMapper<WrtsUndlStockRecord> wrtsUndlStockLineMapper() {
        PatternMatchingCompositeLineMapper<WrtsUndlStockRecord> lineMapper = new PatternMatchingCompositeLineMapper<>();

        FixedLengthTokenizer headerAndTrailerTokenizer = new FixedLengthTokenizer();
        headerAndTrailerTokenizer.setNames(RECORD_TYPE, HEADER_OR_TRAILER_CONTENT);
        headerAndTrailerTokenizer.setColumns(new Range(1, 1), new Range(2));

        FixedLengthTokenizer detailTokenizer = new FixedLengthTokenizer();
        detailTokenizer.setNames(RECORD_TYPE, SECURITY_CODE, SECURITY_TYPE, UNDERLYING_CODE);
        detailTokenizer.setColumns(new Range(1, 1), new Range(4, 15), new Range(244, 247), new Range(402, 406));

        Map<String, LineTokenizer> tokenizers = new LinkedHashMap<>(2);
        tokenizers.put(DEFAULT_RECORD_PATTERN, headerAndTrailerTokenizer);
        tokenizers.put(DETAIL_RECORD_PATTERN, detailTokenizer);

        lineMapper.setTokenizers(tokenizers);

        Map<String, FieldSetMapper<WrtsUndlStockRecord>> mappers = new LinkedHashMap<>(2);
        mappers.put(DEFAULT_RECORD_PATTERN, new WrtsUndlStockHeaderAndTrailerFieldSetMapper());
        mappers.put(DETAIL_RECORD_PATTERN, new WrtsUndlStockDetailFieldSetMapper());

        lineMapper.setFieldSetMappers(mappers);

        return lineMapper;
    }
}
