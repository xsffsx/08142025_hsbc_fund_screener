package com.dummy.wmd.wpc.graphql.utils;

import com.dummy.wmd.wpc.graphql.model.Operation;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import com.jayway.jsonpath.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonPathUtils {

    public static void applyChanges(Map<String, Object> prod, List<OperationInput> operations) {
        DocumentContext doc = JsonPath.parse(prod);
        for (OperationInput operation : operations) {
            try {
                switch (operation.getOp()) {
                    case put:
                        doPut(operation, doc);
                        break;
                    case set:   // set only apply to an existing field, will be ignored in case the field does not exist
                        doc.set(operation.getPath(), operation.getValue());
                        break;
                    case add:
                        doAdd(operation, doc);
                        break;
                    case delete:
                        doDelete(operation, doc);
                        break;
                }
            } catch (Exception e) {
                String message = String.format("Failed to apply change operation: %s, error: %s", operation, e.getMessage());
                log.error(message);
                throw new IllegalArgumentException(message, e);
            }
        }
    }

    private static void doPut(OperationInput operation, DocumentContext doc) {
        String path = operation.getPath();
        String key;
        // separate the full path into 2 parts, eg. $.a.b --> $.a and b, $ means ROOT of the object
        int idx = path.lastIndexOf(".");
        if (-1 == idx) {
            key = path;
            path = "$";
        } else {
            key = path.substring(idx + 1);
            path = path.substring(0, idx);

            // if the parent path does not exist, need to set it as a Map or List according to the input path
            try {
                doc.read(path, Map.class);
            } catch (PathNotFoundException e) {
                doc.put("$", path, new LinkedHashMap<>());
            }
        }
        doc.put(path, key, operation.getValue());
    }

    private static void doAdd(OperationInput operation, DocumentContext doc) {
        String path = operation.getPath();
        List<?> list = null;

        try {
            list = doc.read(path);
        } catch (PathNotFoundException e) {// ignored
        }

        if (CollectionUtils.isEmpty(list)) {
            doPut(new OperationInput(Operation.add, path, new ArrayList<>()), doc);
        }

        doc.add(path, operation.getValue());
    }

    private static void doDelete(OperationInput operation, DocumentContext doc) {
        try {
            doc.delete(operation.getPath());
        } catch (InvalidPathException e) {
            // ignore that when value is null
        }
    }

    private static Pattern idxPattern = Pattern.compile("\\[(\\d+)]");
    private static Pattern starPattern = Pattern.compile("\\[(\\*)]");

    /**
     * Realize the * in an expression, eg.
     * Given exp = debtInstm.credRtng[*].creditRtngAgcyCde and path = debtInstm.credRtng[0].creditRtngCde
     * result in debtInstm.credRtng[0].creditRtngAgcyCde
     *
     * @param exp
     * @param path
     * @return
     */
    public static String realizeListElement(String exp, String path) {
        // retrieve the index list
        List<String> idxList = new ArrayList<>();
        Matcher matcher = idxPattern.matcher(path);
        while (matcher.find()) {
            idxList.add(matcher.group());
        }

        // apply the index list to replace * respectively
        matcher = starPattern.matcher(exp);
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (matcher.find()) {
            matcher.appendReplacement(sb, idxList.get(i++));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
