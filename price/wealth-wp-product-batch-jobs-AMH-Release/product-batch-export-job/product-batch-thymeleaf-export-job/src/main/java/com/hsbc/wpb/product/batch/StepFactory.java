package com.dummy.wpb.product.batch;

import com.dummy.wpb.product.model.ExportFile;
import com.dummy.wpb.product.model.ExportRequest;
import com.dummy.wpb.product.model.ExportTask;
import com.dummy.wpb.product.reader.ReaderFactoryBean;
import com.dummy.wpb.product.writer.WriterFactoryBean;
import org.bson.Document;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StepFactory {

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    DefaultListableBeanFactory beanFactory;

    @Autowired
    private ExportRequest exportRequest;

    @Value("${batch.chunk-size}")
    private Integer chunkSize;

    public List<Step> createStep() {
        List<Step> stepList = new ArrayList<>();

        for (int i = 1; i <= exportRequest.getTasks().size(); i++) {
            ExportTask task = exportRequest.getTasks().get(i - 1);
            String name = task.getFiles().stream().map(ExportFile::getName).collect(Collectors.joining(", "));
            String stepName = String.format("Step %d: Generate %s %s", i, exportRequest.getSystemCde(), name);

            BeanDefinition readerBeanDef = BeanDefinitionBuilder
                    .rootBeanDefinition(ReaderFactoryBean.class)
                    .addPropertyValue("task", task)
                    .setScope("step")
                    .getBeanDefinition();
            ItemReader<Document> reader = registerAndGetBean(String.format("Reader for %s", stepName), readerBeanDef);

            BeanDefinition writerDef = BeanDefinitionBuilder
                    .rootBeanDefinition(WriterFactoryBean.class)
                    .addPropertyValue("files", task.getFiles())
                    .setScope("step")
                    .getBeanDefinition();
            ItemWriter<Document> writer = registerAndGetBean(String.format("Writer for %s", stepName), writerDef);


            SimpleStepBuilder<Document, Document> builder = stepBuilderFactory.get(stepName)
                    .<Document, Document>chunk(chunkSize)
                    .reader(reader)
                    .writer(writer);

            stepList.add(builder.build());
        }

        return stepList;
    }

    protected <T> T registerAndGetBean(String beanName, BeanDefinition beanDef) {
        BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDef, beanName);
        BeanDefinitionHolder scopedProxy = ScopedProxyUtils.createScopedProxy(beanDefinitionHolder, this.beanFactory, true);
        beanFactory.registerBeanDefinition(beanName, scopedProxy.getBeanDefinition());
        return (T) beanFactory.getBean(beanName);
    }
}
