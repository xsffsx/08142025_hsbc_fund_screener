package com.dummy.wpb.product.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.common.base.Equivalence;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.model.graphql.Operation;
import com.dummy.wpb.product.service.OperationService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class DefaultOperationService implements OperationService {
    private final ObjectMapper objectMapper;

    private final BatchEquivalence equivalence;

    public DefaultOperationService() {
        SimpleModule mapToDocumentModule = new SimpleModule();
        mapToDocumentModule.addAbstractTypeMapping(Map.class, Document.class);
        objectMapper = JsonMapper.builder()
                .addModule(new ParameterNamesModule())
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .addModule(mapToDocumentModule)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();
        equivalence = new BatchEquivalence();
    }

    private static final List<String> IGNORE_FIELDS = Arrays.asList(Field.recCreatDtTm, Field.recUpdtDtTm, Field._id, Field.rowid);

    @Override
    public <T> List<Operation> calcOperations(T originalProduct, T updatedProduct) {
        return calcOperations(decorate(originalProduct), decorate(updatedProduct), "");
    }

    private List<Operation> calcOperations(Document originalProduct, Document updatedProduct, String parent) {
        List<Operation> operations = new ArrayList<>();

        MapDifference<String, Object> result = Maps.difference(originalProduct, updatedProduct, equivalence);

        operations.addAll(setOperationsForValueDiff(result.entriesDiffering(), parent));
        operations.addAll(setOptionsForOnlyOneSide(result.entriesOnlyOnRight(), parent, false));
        operations.addAll(setOptionsForOnlyOneSide(result.entriesOnlyOnLeft(), parent, true));
        return operations;
    }

    private List<Operation> setOperationsForValueDiff(Map<String, ? extends MapDifference.ValueDifference<?>> valueDifferences, String parent) {
        List<Operation> operations = new ArrayList<>();
        valueDifferences.forEach((key, difference) -> {
            if (IGNORE_FIELDS.contains(key)) {
                return;
            }

            String path = StringUtils.isBlank(parent) ? key : parent + "." + key;

            Object leftValue = difference.leftValue();
            Object rightValue = difference.rightValue();
            if (allInstancesOf(Document.class, leftValue, rightValue)) {
                operations.addAll(calcOperations((Document) leftValue, (Document) rightValue, path));
            } else {
                operations.add(new Operation("put", path, ObjectUtils.isEmpty(rightValue) ? null : rightValue));
            }

        });
        return operations;
    }

    private List<Operation> setOptionsForOnlyOneSide(Map<String, ?> map, String parent, boolean onlyOnLeft) {
        List<Operation> operations = new ArrayList<>();
        map.forEach((key, value) -> {
            if (IGNORE_FIELDS.contains(key)) {
                return;
            }
            String path = StringUtils.isBlank(parent) ? key : parent + "." + key;
            if (value instanceof Map) {
                operations.addAll(setOptionsForOnlyOneSide((Map<String, ?>) value, path, onlyOnLeft));
            } else {
                operations.add(new Operation("put", path, onlyOnLeft ? null : value));
            }

        });
        return operations;
    }

    private boolean allInstancesOf(Class<?> clazz, Object... obj) {
        return Stream.of(obj).allMatch(clazz::isInstance);
    }

    @Override
    public Document decorate(Object product) {
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(product), Document.class);
        } catch (JsonProcessingException e) {
            throw new productBatchException("Failed to decorate product", e);
        }
    }

    private class BatchEquivalence extends Equivalence<Object> {

        Equivalence<Iterable<Object>> pairwise = super.pairwise();


        @Override
        protected boolean doEquivalent(Object leftValue, Object rightValue) {
            if (allInstancesOf(Number.class, leftValue, rightValue)) {
                return Objects.equals(((Number) leftValue).doubleValue(), ((Number) rightValue).doubleValue());
            }

            if (allInstancesOf(Document.class, leftValue, rightValue)) {
                Document leftDocument = (Document) leftValue;
                Document rightDocument = (Document) rightValue;
                return calcOperations(leftDocument, rightDocument, "").isEmpty();
            }

            if (allInstancesOf(Iterable.class, leftValue, rightValue)) {
                return pairwise.equivalent((Iterable<Object>) leftValue, (Iterable<Object>) rightValue);
            }

            return leftValue.equals(rightValue);
        }

        @Override
        protected int doHash(Object o) {
            return o.hashCode();
        }
    }

}
