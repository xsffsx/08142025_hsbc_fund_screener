package com.dummy.wpb.product.writer;

import com.dummy.wpb.product.thymeleaf.productProcessorDialect;
import com.dummy.wpb.product.thymeleaf.VersionClassLoaderTemplateResolver;
import com.dummy.wpb.product.writer.support.AbstractTemplateWriter;
import lombok.SneakyThrows;
import org.bson.Document;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Collections;
import java.util.List;

public class CsvTemplateWriter extends AbstractTemplateWriter {
    protected static final TemplateEngine csvTemplateEngine;

    static {
        csvTemplateEngine = new SpringTemplateEngine();
        VersionClassLoaderTemplateResolver templateResolver = new VersionClassLoaderTemplateResolver();
        templateResolver.setPrefix("template/");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        csvTemplateEngine.setTemplateResolver(templateResolver);
        csvTemplateEngine.addDialect(productProcessorDialect.getInstance());
    }

    @SneakyThrows
    @Override
    public void close() {
        thymeleafContext.setVariable("isLast", true);
        getOutputState().write(doWrite(Collections.emptyList()));
        super.close();
    }

    @Override
    protected String doWrite(List<? extends Document> documents) {
        thymeleafContext.setVariable("documents", documents);
        thymeleafContext.setVariable("isFirst", getOutputState().getLinesWritten() == 0);
        return csvTemplateEngine.process(templateName, thymeleafContext)
                .replaceAll("(?m)^[ \t]*\r?\n", "")
                .replace("${lineBreak}", "");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // do nothing
    }
}
