package com.dummy.wmd.wpc.graphql.validator;

import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.utils.CodeUtils;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.utils.JsonPathUtils;
import com.dummy.wmd.wpc.graphql.utils.NullableMapAccessor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeConverter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Slf4j
public class ValidationContext {
    private final Deque<String> nestedPathStack = new ArrayDeque<>();
    private final Map<String, Object> product;
    private final List<Error> violations = new ArrayList<>();
    private final StandardEvaluationContext evaluationContext;
    private final SpelExpressionParser spelExpressionParser;

    public ValidationContext(Map<String, Object> product){
        this.product = product;
        evaluationContext = new StandardEvaluationContext(product);
        evaluationContext.addPropertyAccessor(new NullableMapAccessor());
        evaluationContext.setTypeConverter(new StandardTypeConverter(new DefaultFormattingConversionService()));
        try {
            evaluationContext.registerFunction("sum", SpELUtils.class.getDeclaredMethod("sum", List.class, String.class));
            evaluationContext.registerFunction("localDate", SpELUtils.class.getDeclaredMethod("localDate", Object.class));
            evaluationContext.registerFunction("validatePrecisionScale",
                    SpELUtils.class.getDeclaredMethod("validatePrecisionScale", Object.class, Integer.class, Integer.class));
        } catch (Exception e){
            throw new productErrorException(productErrors.RuntimeException, "Register function error: " + e.getMessage());
        }
        spelExpressionParser = new SpelExpressionParser();
    }

    public String getCtryRecCde() {
        return (String)product.get(Field.ctryRecCde);
    }

    public String getGrpMembrRecCde() {
        return (String)product.get(Field.grpMembrRecCde);
    }

    public void addError(String rule, String defaultMessage) {
        String jsonPath = getNestedPath();
        String code = String.format("%s@%s", CodeUtils.normalizedJsonPath(jsonPath), rule);
        // get customized error message, otherwise use the default one
        Error error = new Error(jsonPath, code, defaultMessage);
        violations.add(error);
        log.debug("add {}", error);
    }

    public String getNestedPath() {
        return nestedPathStack.getFirst();
    }

    public void pushNestedPath(String subPath) {
        String current = subPath;
        if(!current.contains(".") && nestedPathStack.size() > 1) {  // in case it's relative path and not the root one
            String parent = nestedPathStack.getFirst();
            current = parent + "." + subPath;
        }
        this.nestedPathStack.push(current);
        log.debug("push: {}", getNestedPath());
    }

    public void pushNestedPath(int index) {
        String parent = nestedPathStack.getFirst();
        String current = String.format("%s[%d]", parent, index);
        this.nestedPathStack.push(current);
        log.debug("push: {}", getNestedPath());
    }

    public String popNestedPath() throws IllegalStateException {
        try {
            String path = this.nestedPathStack.pop();
            log.debug(" pop: {}", path);
            return path;
        } catch (NoSuchElementException ex) {
            throw new IllegalStateException("Cannot pop nested path: no nested path on stack");
        }
    }

    public boolean evaluateCondition(String condition) {
        evaluationContext.setVariable("now", LocalDate.now());  // so we can use #now variable in the expression
        boolean apply = !StringUtils.hasText(condition);
        if(!apply) {
            try {
                apply = spelExpressionParser.parseExpression(condition).getValue(evaluationContext, Boolean.class);
            } catch (SpelEvaluationException e) {
                log.warn("SpEL error: {}, {}", condition, e.getMessage());
                apply = true;
            }
        }
        return apply;
    }
    private static Pattern expPattern = Pattern.compile("^\\s*\\$\\{(.+)}\\s*$");

    /**
     * Resolve param place holder into value, eg. ${ctryRecCde} --> SG
     * @param variable eg. ${ctryRecCde}
     * @return
     */
    public Object resolveVariable(String variable) {
        Matcher matcher = expPattern.matcher(variable);
        if(matcher.find()){
            String exp = matcher.group(1);
            if(exp.contains("[*]")) {
                exp = JsonPathUtils.realizeListElement(exp, nestedPathStack.peekFirst());
            }
            return spelExpressionParser.parseExpression(exp).getValue(evaluationContext);
        } else {
            log.warn("Can not resolve variable: " + variable);
            return variable;
        }
    }

    /**
     * Parse template variables ${xxx}
     */
    private static ParserContext parserContext = new ParserContext(){
        @Override
        public boolean isTemplate() {
            return true;
        }

        @Override
        public String getExpressionPrefix() {
            return "${";
        }

        @Override
        public String getExpressionSuffix() {
            return "}";
        }
    };

    public String evaluateTemplate(String template) {
        return spelExpressionParser.parseExpression(template, parserContext).getValue(evaluationContext, String.class);
    }
}
