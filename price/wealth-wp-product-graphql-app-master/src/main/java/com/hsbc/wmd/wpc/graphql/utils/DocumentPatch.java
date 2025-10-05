package com.dummy.wmd.wpc.graphql.utils;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.Operation;
import com.dummy.wmd.wpc.graphql.model.OperationInput;

import java.util.*;
import java.util.stream.Collectors;

public class DocumentPatch {
    private static final String LIST_PATH_TEMPLATE = "%s[?(@.rowid=='%s')]";

    private static final String FULL_PATH_TEMPLATE = "%s.%s";

    /**
     * Calculate the operation from docLeft to docRight
     *
     * @param docLeft
     * @param docRight
     * @return an OperationInput list
     */
    public List<OperationInput> patch(Map<String, Object> docLeft, Map<String, Object> docRight) {
        return documentPatch("$", docLeft, docRight);
    }

    private List<OperationInput> documentPatch(String parentPath, Map<String, Object> docLeft, Map<String, Object> docRight) {
        List<OperationInput> opList = new ArrayList<>();
        Set<String> lKeys = docLeft.entrySet().stream().filter(entry -> null != entry.getValue()).map(Map.Entry::getKey).collect(Collectors.toSet());
        Set<String> rKeys = docRight.entrySet().stream().filter(entry -> null != entry.getValue()).map(Map.Entry::getKey).collect(Collectors.toSet());
        // a key exists in both left and right, changed
        Set<String> bothKeys = new HashSet<>(lKeys);
        bothKeys.retainAll(rKeys);
        bothKeys.forEach(key -> {
            String path = String.format(FULL_PATH_TEMPLATE, parentPath, key);
            Object leftObj = docLeft.get(key);
            Object rightObj = docRight.get(key);
            if (leftObj instanceof List && rightObj instanceof List) {
                // handle list
                // cast the types to avoid sonarlint issues
                List<Object> leftList = (List) leftObj;
                List<Object> rightList = (List) rightObj;
                if ((!leftList.isEmpty() && leftList.get(0) instanceof String)
                        || (!rightList.isEmpty() && rightList.get(0) instanceof String)) {
                    opList.addAll(stringListPatch(path, (List) leftList, (List) rightList));
                } else {
                    opList.addAll(documentListPatch(path, (List) leftList, (List) rightList));
                }
            } else if (leftObj instanceof Map && rightObj instanceof Map) {
                opList.addAll(documentPatch(path, (Map) leftObj, (Map) rightObj));
            } else if (!Objects.equals(leftObj, rightObj)) {
                opList.add(new OperationInput(Operation.set, path, rightObj));
            }
        });

        // a key exists in left only, deleted
        Set<String> leftOnlyKeys = new HashSet<>(lKeys);
        leftOnlyKeys.removeAll(rKeys);
        leftOnlyKeys.forEach(key -> {
            String path = String.format(FULL_PATH_TEMPLATE, parentPath, key);
            Object leftObj = docLeft.get(key);
            opList.add(new OperationInput(Operation.delete, path, leftObj));
        });

        // a key exists in right only, added
        Set<String> rightOnlyKeys = new HashSet<>(rKeys);
        rightOnlyKeys.removeAll(lKeys);
        rightOnlyKeys.forEach(key -> {
            String path = String.format(FULL_PATH_TEMPLATE, parentPath, key);
            Object rightObj = docRight.get(key);
            opList.add(new OperationInput(Operation.put, path, rightObj));
        });

        return opList;
    }

    private List<OperationInput> stringListPatch(String parentPath, List<String> leftList, List<String> rightList) {
        List<OperationInput> opList = new ArrayList<>();
        // change [String] to [Document], for ctryProdTradeCde
        if (!Objects.deepEquals(leftList, rightList)) {
            opList.add(new OperationInput(Operation.set, parentPath, rightList));
        }
        return opList;
    }

    private List<OperationInput> documentListPatch(String parentPath, List<Map<String, Object>> leftList, List<Map<String, Object>> rightList) {
        List<OperationInput> opList = new ArrayList<>();

        Map<String, Map<String, Object>> leftMap = buildRowidMap(leftList);
        Map<String, Map<String, Object>> rightMap = buildRowidMap(rightList);

        Set<String> lKeys = leftMap.keySet();
        Set<String> rKeys = rightMap.keySet();
        // a key exists in both left and right, changed
        Set<String> bothKeys = new HashSet<>(lKeys);
        bothKeys.retainAll(rKeys);
        bothKeys.forEach(key -> {
            // sample path $.store.book[?(@.price < 10)]
            String path = String.format(LIST_PATH_TEMPLATE, parentPath, key);
            Map<String, Object> leftObj = leftMap.get(key);
            Map<String, Object> rightObj = rightMap.get(key);
            if (leftObj instanceof List && rightObj instanceof List) {
                // handle list
                opList.addAll(documentListPatch(path, (List) leftObj, (List) rightObj));
            } else if (leftObj instanceof Map && rightObj instanceof Map) {
                opList.addAll(documentPatch(path, leftObj, rightObj));
            } else if (!Objects.equals(leftObj, rightObj)) {
                opList.add(new OperationInput(Operation.set, path, rightObj));
            }
        });

        // a key exists in left only, deleted
        Set<String> leftOnlyKeys = new HashSet<>(lKeys);
        leftOnlyKeys.removeAll(rKeys);
        leftOnlyKeys.forEach(key -> {
            String path = String.format(LIST_PATH_TEMPLATE, parentPath, key);
            Object leftObj = leftMap.get(key);
            opList.add(new OperationInput(Operation.delete, path, leftObj));
        });

        // a key exists in right only, added
        Set<String> rightOnlyKeys = new HashSet<>(rKeys);
        rightOnlyKeys.removeAll(lKeys);
        rightOnlyKeys.forEach(key -> {
            Object rightObj = rightMap.get(key);
            opList.add(new OperationInput(Operation.add, parentPath, rightObj));
        });

        return opList;
    }

    private Map<String, Map<String, Object>> buildRowidMap(List<Map<String, Object>> list) {
        Map<String, Map<String, Object>> map = new LinkedHashMap<>();
        list.forEach(doc -> {
            if (null == doc) {
                return;
            }
            String rowid = (String) doc.get(Field.rowid);
            if (null == rowid) {
                throw new IllegalArgumentException("List item has no rowid: " + doc);
            }
            map.put(rowid, doc);
        });
        return map;
    }
}
