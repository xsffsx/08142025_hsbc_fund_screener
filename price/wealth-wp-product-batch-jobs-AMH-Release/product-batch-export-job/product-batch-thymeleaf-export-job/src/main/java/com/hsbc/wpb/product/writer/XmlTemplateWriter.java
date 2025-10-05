package com.dummy.wpb.product.writer;

import com.dummy.wpb.product.exception.FileResourceException;
import com.dummy.wpb.product.thymeleaf.productProcessorDialect;
import com.dummy.wpb.product.thymeleaf.VersionClassLoaderTemplateResolver;
import com.dummy.wpb.product.writer.support.AbstractTemplateWriter;
import lombok.SneakyThrows;
import org.bson.Document;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

public class XmlTemplateWriter extends AbstractTemplateWriter {

    protected static final TemplateEngine xmlTemplateEngine;

    protected String rootStartTag;

    protected String rootEndTag = "";

    protected String xmlDeclaration;

    static {
        xmlTemplateEngine = new SpringTemplateEngine();
        VersionClassLoaderTemplateResolver templateResolver = new VersionClassLoaderTemplateResolver();
        templateResolver.setPrefix("template/");
        templateResolver.setSuffix(".xml"); // set the template file suffix
        templateResolver.setTemplateMode(TemplateMode.XML);
        templateResolver.setCharacterEncoding("UTF-8");
        xmlTemplateEngine.setTemplateResolver(templateResolver);
        xmlTemplateEngine.addDialect(productProcessorDialect.getInstance());
    }

    @SneakyThrows
    @Override
    public void close() {
        // If there are no products to be written, write an empty root tag
        if (null == xmlDeclaration) {
            getOutputState().write(doWrite(Collections.emptyList()));
        }
        getOutputState().write(rootEndTag);
        super.close();
    }

    @Override
    protected String doWrite(List<? extends Document> items) {
        thymeleafContext.setVariable("documents", items);
        String xmlData = xmlTemplateEngine.process(templateName, thymeleafContext);

        if (null == xmlDeclaration) {
            readXmlDeclarationAndRoot(xmlData);
            return xmlData.replace(rootEndTag, "")
                    .replaceAll("(?m)^[ \t]*\r?\n", "");
        }

        return xmlData.replace(xmlDeclaration, "")
                .replaceAll("(?m)^[ \t]*\r?\n", "") //remove empty lines
                .replace(rootStartTag + "\n", "")
                .replace(rootEndTag, "");
    }

    private void readXmlDeclarationAndRoot(String xmlData) {
        try (BufferedReader reader = new BufferedReader(new StringReader(xmlData))) {
            xmlDeclaration = reader.readLine() + "\n";
            rootStartTag = reader.readLine();
            rootEndTag = rootStartTag.replace("<", "</");
        } catch (IOException e) {
            throw new FileResourceException("Failed to read Xml declaration and root tag", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // do nothing
    }
}
