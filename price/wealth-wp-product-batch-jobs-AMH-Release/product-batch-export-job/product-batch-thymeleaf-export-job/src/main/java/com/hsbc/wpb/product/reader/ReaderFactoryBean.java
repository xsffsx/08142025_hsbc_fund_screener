package com.dummy.wpb.product.reader;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.model.ExportTask;
import com.dummy.wpb.product.service.SystemParameterService;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ReaderFactoryBean implements FactoryBean<ItemStreamReader<Document>>, BeanNameAware {

    @Value("#{jobParameters['ctryRecCde']}")
    String ctryRecCde;

    @Value("#{jobParameters['grpMembrRecCde']}")
    String grpMembrRecCde;


    @Value("#{jobExecutionContext}")
    Map<String, Object> context;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    SystemParameterService sysParamService;

    @Setter
    ExportTask task;

    AbstractMongoReader reader;

    String name;
    @Value("${batch.chunk-size}")
    private Integer chunkSize;

    @PostConstruct
    public void buildReader() {
        if (StringUtils.isNotBlank(task.getClassName())) {
            try {
                reader = (AbstractMongoReader) Class.forName(task.getClassName()).getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new productBatchException("Failed to create reader instance: " + task.getClassName(), e);
            }
        } else {
            reader = new SingleCollectionReader();
        }
        reader.setCollection(task.getCollection());
        reader.setFilter(buildFilter());
        reader.setSort(task.getSort());
        reader.setPageSize(chunkSize);
        reader.setMongoTemplate(mongoTemplate);
        reader.setName(name);
    }

    @Override
    public ItemStreamReader<Document> getObject() {
        return reader;
    }

    @Override
    public Class<?> getObjectType() {
        return ItemStreamReader.class;
    }

    private Document buildFilter() {
        Document filter = task.getFilter()
                .append(Field.ctryRecCde, ctryRecCde)
                .append(Field.grpMembrRecCde, grpMembrRecCde);
        if (StringUtils.isBlank(task.getDeltaKey())) {
            return filter;
        }

        String deltaKey = task.getDeltaKey();
        String lastSuccessfulDateTime = sysParamService.getLastSuccessfulDateTime(ctryRecCde, grpMembrRecCde, deltaKey);
        LocalDateTime lastTime = LocalDateTime.parse(lastSuccessfulDateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        Document deltaFilter = new Document("$gte", lastTime).append("$lte", context.get("startTime"));
        filter.append(task.getDeltaField(), deltaFilter);
        return filter;
    }

    @Override
    public void setBeanName(String name) {
        this.name = name;
    }
}
