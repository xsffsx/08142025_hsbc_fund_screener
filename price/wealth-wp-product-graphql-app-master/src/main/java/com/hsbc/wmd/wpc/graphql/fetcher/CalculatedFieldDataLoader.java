package com.dummy.wmd.wpc.graphql.fetcher;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.model.CalculatedBy;
import com.dummy.wmd.wpc.graphql.utils.ObjectMapperUtils;
import com.dummy.wmd.wpc.graphql.validator.SpELUtils;
import com.jayway.jsonpath.JsonPath;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import graphql.GraphQLContext;
import graphql.execution.ResultPath;
import graphql.schema.DataFetchingEnvironment;
import org.bson.Document;
import org.dataloader.BatchLoader;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.ne;

@Component
public class CalculatedFieldDataLoader implements BatchLoader<DataFetchingEnvironment, Object> {

    private Map<String, CalculatedBy> fieldMap = new LinkedHashMap<>();

    public CalculatedFieldDataLoader(MongoDatabase mongoDatabase) {
        FindIterable<Document> iter = mongoDatabase.getCollection(CollectionName.metadata.toString()).find(ne("calculatedBy", null));
        // retrieve all readOnly field
        iter.forEach((Consumer<Document>) meta ->
                fieldMap.put(meta.getString("attrName"), ObjectMapperUtils.convertValue(meta.get("calculatedBy"), CalculatedBy.class))
        );
    }

    @Override
    public CompletionStage<List<Object>> load(List<DataFetchingEnvironment> environments) {
        return CompletableFuture.supplyAsync(() ->
                environments.stream().map(this::get).collect(Collectors.toList())
        );
    }

    public Object get(DataFetchingEnvironment environment) {
        GraphQLContext context = environment.getContext();
        ResultPath path = environment.getExecutionStepInfo().getPath();
        String fieldName = path.getSegmentName();
        CalculatedBy calculatedBy = fieldMap.get(fieldName);
        Document doc = getRootObject(context, path, calculatedBy.getRootObjectPath());
        return readValue(doc, calculatedBy.getExpression());
    }

    private Document getRootObject(GraphQLContext context, ResultPath executionPath, String rootObjectPath) {
        List<Object> paths = executionPath.toList();
        String rootName = (String) paths.get(0);
        Object rootObject = context.get(rootName);

        if (rootObject instanceof List) {
            List<Document> convertedObject1 = (List<Document>) rootObject;
            return convertedObject1.get((Integer) paths.get(1));
        }

        // read the root data with JsonPath, result expected to be a Map<String, Object> type object
        ResultPath path = executionPath;
        String[] parts = rootObjectPath.split("[\\\\/]");
        for (String part : parts) {
            if ("..".equals(part)) {
                path = path.dropSegment();
            }
        }

        String jsonPath = "$" + path.toString().replace("/", ".");
        Object convertedObject = ObjectMapperUtils.convertValue(rootObject, Map.class);
        Map<String, Object> rootData = Collections.singletonMap(rootName, convertedObject);
        Map<String, Object> value = JsonPath.read(rootData, jsonPath);

        return new Document(value);
    }

    private static List<Character> chars = Arrays.asList('.', ' ', '[', '(', '#');

    /**
     * for a.b, a[x].b form, need to read with JsonPath, or even work with SpEL to calculate a value
     *
     * @param doc
     * @param exp
     * @return
     */
    private Object readValue(Document doc, String exp) {
        Object result = null;
        boolean isSpel = chars.stream().anyMatch(ch -> -1 != exp.indexOf(ch));
        if (isSpel) {
            ExpressionParser parser = new SpelExpressionParser();
            StandardEvaluationContext context = new StandardEvaluationContext(doc);
            context.addPropertyAccessor(new MapAccessor());
            try {
                context.registerFunction("productById", SpELUtils.class.getDeclaredMethod("productById", Long.class));
            } catch (Exception e) {
                throw new productErrorException(productErrors.RuntimeException, "Register function error: " + e.getMessage());
            }
            result = parser.parseExpression(exp).getValue(context);
        } else {    // it's a simple key then, this approach will be faster
            result = doc.get(exp);
        }
        return result;
    }
}
