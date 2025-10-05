package com.dummy.wpb.product.thymeleaf.processor;

import org.bson.Document;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.Each;
import org.thymeleaf.standard.expression.EachUtils;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDefinedFieldEachTagProcessor extends AbstractAttributeTagProcessor {
    public UserDefinedFieldEachTagProcessor(String dialectPrefix) {
        super(
                TemplateMode.XML, // This processor will apply only to XML mode
                dialectPrefix,     // Prefix to be applied to name for matching
                null,              // No tag name: match any tag name
                false,             // No prefix to be applied to tag name
                "udfEach",         // Name of the attribute that will be matched
                true,              // Apply dialect prefix to attribute name
                200,        // Precedence (inside dialect's precedence)
                true);
    }

    /**
     * This function splits the list of user-defined field values into individual user-defined field values, aligning with WPC.
     * <p>Example of list user defined field value:</p>
     * <pre>{@code
     * <prodUserDefExtSeg>
     *     <fieldCde>tradeAccList</fieldCde>
     *     <fieldValue>[8CO, 8CR]</fieldValue>
     * <prodUserDefExtSeg>
     * }</pre>
     *
     * <p>Convert to:</p>
     * <pre>{@code
     * <prodUserDefExtSeg>
     *     <fieldCde>tradeAccList</fieldCde>
     *     <fieldValue>8CO</fieldValue>
     * <prodUserDefExtSeg>
     * <prodUserDefExtSeg>
     *     <fieldCde>tradeAccList</fieldCde>
     *     <fieldValue>8CR</fieldValue>
     * <prodUserDefExtSeg>
     * }</pre>
     */
    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, IElementTagStructureHandler structureHandler) {
        final Each each = EachUtils.parseEach(context, attributeValue);
        final String iterVarName = Objects.toString(each.getIterVar().execute(context), null);

        Object iteratedValue = each.getIterable().execute(context);

        if (iteratedValue instanceof List) {
            List<Document> userDefinedFields = (List<Document>) iteratedValue;
            List<Document> splitUserDefinedFields = new ArrayList<>();
            for (Document userDefinedField : userDefinedFields) {
                Object value = userDefinedField.get("value");
                if (value instanceof List) {
                    ((List<?>) value).forEach(v -> splitUserDefinedFields.add(new Document(userDefinedField).append("value", v)));
                } else {
                    splitUserDefinedFields.add(userDefinedField);
                }
            }

            iteratedValue = splitUserDefinedFields;
        }

        structureHandler.iterateElement(iterVarName, null, iteratedValue);
    }
}