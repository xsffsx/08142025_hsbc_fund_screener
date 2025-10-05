package com.dummy.wpb.product.thymeleaf.expression;

import org.bson.Document;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Csvs {


    public List<Document> parse(String... keyValuePairs) {
        if (keyValuePairs == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(keyValuePairs)
                .map(keyValuePair -> keyValuePair.split(":", 2))
                .map(arr -> new Document().append("header", arr[0]).append("expr", arr[1]))
                .collect(Collectors.toList());
    }

    public List<Document> merge(List<Document>... csvInfos) {
        return Arrays.stream(csvInfos)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
