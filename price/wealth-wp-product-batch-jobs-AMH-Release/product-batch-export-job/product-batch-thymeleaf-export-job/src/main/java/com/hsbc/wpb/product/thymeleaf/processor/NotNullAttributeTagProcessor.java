package com.dummy.wpb.product.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.LazyEscapingCharSequence;
import org.unbescape.xml.XmlEscape;

public class NotNullAttributeTagProcessor extends AbstractStandardExpressionAttributeTagProcessor {

    private static final String ATTR_NAME = "notNull";
    private static final int PRECEDENCE = 2000;

    public NotNullAttributeTagProcessor(String dialectPrefix) {
        super(TemplateMode.XML, dialectPrefix, ATTR_NAME, PRECEDENCE,  true);
    }

    @Override
    protected void doProcess(
            final ITemplateContext context,
            final IProcessableElementTag tag,
            final AttributeName attributeName, final String attributeValue,
            final Object expressionResult,
            final IElementTagStructureHandler structureHandler) {
        if (expressionResult == null) {
            structureHandler.removeElement();
            return;
        }

        final String input = expressionResult.toString();
        final CharSequence text;
        if (input.length() > 100) {
            // Might be a large text -> Lazy escaping on the output Writer
            text = new LazyEscapingCharSequence(context.getConfiguration(), TemplateMode.XML, input);
        } else {
            // Not large -> better use a bit more of memory, but be faster
            text = XmlEscape.escapeXml10(input);
        }

        structureHandler.setBody(text, false);
    }
}
