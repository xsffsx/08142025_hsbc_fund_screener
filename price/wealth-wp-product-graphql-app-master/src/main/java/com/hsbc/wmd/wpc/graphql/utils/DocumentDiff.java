package com.dummy.wmd.wpc.graphql.utils;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.DiffType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.*;
import java.util.*;

@SuppressWarnings("java:S3740")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentDiff {
    private static final String LIST_PATH_TEMPLATE = "%s[?(@.rowid=='%s')]";

    private static final String FORMATER = "%s.%s";

    public static List<DiffType> diff(Map<String, Object> docLeft, Map<String, Object> docRight){
        return diffMap("$", docLeft, docRight);
    }

    private static List<DiffType> diffMap(String parentPath, Map<String, Object> docLeft, Map<String, Object> docRight) {
        List<DiffType> opList = new ArrayList<>();
        Set<String> lKeys = docLeft.keySet();
        Set<String> rKeys = docRight.keySet();
        // a key exists in both left and right, changed
        bothKeysChanged(parentPath, docLeft, docRight, lKeys, rKeys, opList);

        // a key exists in left only, deleted
        Set<String> leftOnlyKeys = new HashSet<>(lKeys);
        leftOnlyKeys.removeAll(rKeys);
        leftOnlyKeys.forEach(key -> {
            String path = String.format(FORMATER, parentPath, key);
            Object leftObj = docLeft.get(key);
            if(null != leftObj) {
                opList.add(new DiffType(path, leftObj, null));
            }
        });

        // a key exists in right only, added
        Set<String> rightOnlyKeys = new HashSet<>(rKeys);
        rightOnlyKeys.removeAll(lKeys);
        rightOnlyKeys.forEach(key -> {
            String path = String.format(FORMATER, parentPath, key);
            Object rightObj = docRight.get(key);
            if(null != rightObj) {
                opList.add(new DiffType(path, null, rightObj));
            }
        });

        return opList;
    }

    private static void bothKeysChanged(String parentPath, Map<String, Object> docLeft, Map<String, Object> docRight, Set<String> lKeys, Set<String> rKeys, List<DiffType> opList) {
        Set<String> bothKeys = new HashSet<>(lKeys);
        bothKeys.retainAll(rKeys);
        bothKeys.forEach(key -> {
            String path = String.format(FORMATER, parentPath, key);
            Object leftObj = docLeft.get(key);
            Object rightObj = docRight.get(key);
            if(leftObj instanceof List && rightObj instanceof List) {
                // change [String] to [Document], for ctryProdTradeCde
                if(isStringList(leftObj) || isStringList(rightObj)){    // handle string list
                    opList.addAll(diffStringList(path, (List)leftObj, (List)rightObj));
                } else {    // handle map list
                    opList.addAll(diffMapList(path, (List) leftObj, (List) rightObj));
                }
            } else if(leftObj instanceof Map && rightObj instanceof Map){
                opList.addAll(diffMap(path, (Map)leftObj, (Map)rightObj));
            } else {
                /* Avoid difference like this, they should be the same
                    "path": "$.prodStatUpdtDtTm",
                    "left": "2021-07-14T03:39:47.538Z",
                    "right": "2021-07-14T03:39:47.538+0000"
                 */
                leftObj = rectifyDateTimeType(leftObj);
                rightObj = rectifyDateTimeType(rightObj);
                if (!Objects.equals(leftObj, rightObj)) {
                    opList.add(new DiffType(path, leftObj, rightObj));
                }
            }
        });
    }

    /**
     * If the object is Date, OffsetDateTime, LocalDate, LocalDateTime change into Instant for comparison
     *
     * @param obj
     * @return
     */
    private static Object rectifyDateTimeType(Object obj) {
        if(obj instanceof Date) {
            return ((Date)obj).toInstant();
        }
        if(obj instanceof OffsetDateTime) {
            return ((OffsetDateTime)obj).toInstant();
        }
        if(obj instanceof LocalDateTime) {
            return ((LocalDateTime)obj).atZone(ZoneId.systemDefault()).toInstant();
        }
        if(obj instanceof LocalDate) {
            return ((LocalDate)obj).atStartOfDay(ZoneId.systemDefault()).toInstant();
        }

        // otherwise, return as is
        return obj;
    }

    /**
     * Check whether two string list has difference
     *
     * @param path
     * @param leftObj
     * @param rightObj
     * @return
     */
    private static List<DiffType> diffStringList(String path, List<String> leftObj, List<String> rightObj) {
        boolean hasDiff = false;
        if(null == leftObj && null == rightObj){
            hasDiff = false;
        } else if(null == leftObj || null == rightObj) {
            hasDiff = true;
        } else {
            hasDiff = !Arrays.equals(leftObj.toArray(), rightObj.toArray());
        }
        return hasDiff? Collections.singletonList(new DiffType(path, leftObj, rightObj)): Collections.emptyList();
    }

    private static boolean isStringList(Object obj) {
        if(obj instanceof List) {
            List list = (List)obj;
            return !list.isEmpty() && list.get(0) instanceof String;
        }
        return false;
    }

    private static List<DiffType> diffMapList(String parentPath, List<Map<String, Object>> leftList, List<Map<String, Object>> rightList) {
        List<DiffType> opList = new ArrayList<>();

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
            if(leftObj instanceof List && rightObj instanceof List) {
                // handle list
                opList.addAll(diffMapList(path, (List)leftObj, (List)rightObj));
            } else if(leftObj instanceof Map && rightObj instanceof Map){
                opList.addAll(diffMap(path, leftObj, rightObj));
            } else if(!Objects.equals(leftObj, rightObj)){
                opList.add(new DiffType(path, leftObj, rightObj));
            }
        });

        // a key exists in left only, deleted
        Set<String> leftOnlyKeys = new HashSet<>(lKeys);
        leftOnlyKeys.removeAll(rKeys);
        leftOnlyKeys.forEach(key -> {
            String path = String.format(LIST_PATH_TEMPLATE, parentPath, key);
            Object leftObj = leftMap.get(key);
            opList.add(new DiffType(path, leftObj, null));
        });

        // a key exists in right only, added
        Set<String> rightOnlyKeys = new HashSet<>(rKeys);
        rightOnlyKeys.removeAll(lKeys);
        rightOnlyKeys.forEach(key -> {
            String path = String.format(LIST_PATH_TEMPLATE, parentPath, key);
            Object rightObj = rightMap.get(key);
            opList.add(new DiffType(path, null, rightObj));
        });

        return opList;
    }

    private static Map<String, Map<String, Object>> buildRowidMap(List<Map<String, Object>> list) {
        Map<String, Map<String, Object>> map = new LinkedHashMap<>();
        list.forEach(doc -> {
            if (null == doc) {
                return;
            }
            String rowid = (String)doc.get(Field.rowid);
            if (null == rowid) {
                throw new IllegalArgumentException("List item has no rowid: " + doc);
            }
            map.put(rowid, doc);
        });
        return map;
    }
}
