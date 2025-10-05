package com.dummy.wpb.product.reader;


import com.dummy.wpb.product.constant.Field;
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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class BondCharacterIndReaderConfiguration {

    private static final String FILE_PATTERN = "^RSWPEIP.RSWPEIP";

    @Bean
    @StepScope
    public ItemStreamReader<Object> bondCharacterIndReader(@Value("#{jobParameters['incomingPath']}") String incomingPath) {
        FixedLengthTokenizer headerTokenizer = new FixedLengthTokenizer();
        headerTokenizer.setNames("headerIdentifier", "hederContent");
        headerTokenizer.setColumns(new Range(1, 7), new Range(8));

        FixedLengthTokenizer tailerTokenizer = new FixedLengthTokenizer();
        tailerTokenizer.setNames("trailerIdentifier", "trailerContent");
        tailerTokenizer.setColumns(new Range(1, 7), new Range(8));

        FixedLengthTokenizer bcCheckTokenizer = new FixedLengthTokenizer();
        bcCheckTokenizer.setStrict(false);
        bcCheckTokenizer.setNames(Field.prodAltPrimNum, "qtyTypeCde", Field.prodLocCde, "rbpMigrInd");
        bcCheckTokenizer.setColumns(new Range(1, 12), new Range(13, 15), new Range(16, 18), new Range(19));

        Map<String, LineTokenizer> tokenizers = new LinkedHashMap<>();
        tokenizers.put("HEADER*", headerTokenizer);
        tokenizers.put("TRAILER*", tailerTokenizer);
        tokenizers.put("*", bcCheckTokenizer);

        Map<String, FieldSetMapper<Object>> fieldSetMappers = Collections.singletonMap("*", new BondCharacterFieldSetMapper());

        PatternMatchingCompositeLineMapper<Object> lineMapper = new PatternMatchingCompositeLineMapper<>();
        lineMapper.setTokenizers(tokenizers);
        lineMapper.setFieldSetMappers(fieldSetMappers);

        FlatFileItemReader<Object> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setLineMapper(lineMapper);
        return new ImportFileItemReader<>(incomingPath, FILE_PATTERN, flatFileItemReader);
    }
}
