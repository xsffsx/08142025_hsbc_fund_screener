package com.dummy.wpb.product.thymeleaf;

import com.dummy.wpb.product.thymeleaf.processor.NotNullAttributeTagProcessor;
import com.dummy.wpb.product.thymeleaf.processor.UserDefinedFieldEachTagProcessor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class productProcessorDialect extends AbstractProcessorDialect implements IExpressionObjectDialect {
    private static final productProcessorDialectHolder HOLDER = new productProcessorDialectHolder();

    @Autowired
    productExpressionFactory productExpressionFactory;

    public productProcessorDialect() {
        super("product Dialect", "product", 1000);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<>();
        processors.add(new NotNullAttributeTagProcessor(dialectPrefix));
        processors.add(new StandardXmlNsTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new UserDefinedFieldEachTagProcessor(dialectPrefix));
        return processors;
    }

    public static productProcessorDialect getInstance() {
        return HOLDER.instance;
    }

    @PostConstruct
    public void init() {
        HOLDER.setInstance(this);
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return productExpressionFactory;
    }

    @Setter
    private static class productProcessorDialectHolder {
        private productProcessorDialect instance = new productProcessorDialect();
    }
}
