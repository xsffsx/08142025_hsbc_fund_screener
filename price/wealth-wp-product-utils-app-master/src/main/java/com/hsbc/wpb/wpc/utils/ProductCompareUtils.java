package com.dummy.wpb.wpc.utils;

import com.dummy.wpb.wpc.utils.model.Difference;
import com.dummy.wpb.wpc.utils.model.ProductTableInfo;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductCompareUtils {

    public static ProductCompareUtils getInstance(){
        return new ProductCompareUtils();
    }
    private static final Map<String, List<String>> keys = new HashMap<>();

    private static final Set<String> ignoreFields = new HashSet<>(Arrays.asList("recCreatDtTm", "recUpdtDtTm", "revision", "rowid",
            "createdBy", "lastUpdatedBy", "lastSyncTime", "fieldGroupCtoffDtTm"));

    static {
        List<ProductTableInfo> tableInfoList = ProductMapping.getTableInfoList();
        tableInfoList.stream().forEach(tableInfo -> keys.put(tableInfo.getToAttribute(), tableInfo.getKeyFields()));
    }

    public static Set<Difference> compare(Map<String, Object> leftProd, Map<String, Object> rightProd) {
        return compare("$.", leftProd, rightProd);
    }


    public static Set<Difference> compare(String path,
                                          Map<String, Object> leftProd,
                                          Map<String, Object> rightProd) {
        Set<Difference> diffs = doCompare("", leftProd, rightProd);
        diffs.forEach(diff -> diff.setPath(path + diff.getPath()));
        return diffs;
    }

    /**
     * Compare two {@link Map} objects and return a set of {@link Difference} objects.
     * <p>
     * See MapDiffUtilsTest for a usage example.
     *
     * @param path      the current path in the map. Different levels are separated by a period.
     * @param leftProd  the expected {@link Map} object
     * @param rightProd the actual {@link Map} object
     * @return the set of {@link Difference} objects.
     */
    private static Set<Difference> doCompare(String path,
                                             Map<String, Object> leftProd,
                                             Map<String, Object> rightProd) {

        if (null == leftProd || null == rightProd) {
            return getResultWhenEitherIsNull(path, leftProd, rightProd);
        }
        Map<String, Object> rightProdCopy = new HashMap<>(rightProd);
        Set<Difference> result = new HashSet<>();
        leftProd.forEach((key, leftValue) -> compareLeftProd(path, rightProdCopy, result, key, leftValue));
        if (!rightProdCopy.isEmpty()) {
            rightProdCopy.forEach((key, value) -> {
                if (!ignoreFields.contains(key) && value != null) {
                    String diffPath = StringUtils.isBlank(path) ? key : path + "." + key;
                    result.add(new Difference(diffPath, null, value));
                }
            });
        }
        return result;
    }

    private static void compareLeftProd(String path, Map<String, Object> rightProdCopy, Set<Difference> result, String key, Object leftValue) {
        Object rightValue = rightProdCopy.remove(key);
        if (ignoreFields.contains(key)) {
            return;
        }

        String diffPath = StringUtils.isBlank(path) ? key : path + "." + key;
        if (leftValue instanceof Map && rightValue instanceof Map) {
            result.addAll(doCompare(
                    diffPath,
                    toMap(leftValue),
                    toMap(rightValue)
            ));
            return;
        }
        if (leftValue instanceof List && rightValue instanceof List) {
            result.addAll(compareList(
                    diffPath,
                    toList(leftValue),
                    toList(rightValue),
                    keys.getOrDefault(diffPath, Collections.emptyList())
            ));
            return;
        }
        if (!equals(leftValue, rightValue)) {
            result.add(new Difference(diffPath, leftValue, rightValue));
        }
    }

    private static Set<Difference> compareList(String path,
                                               List<Object> leftList,
                                               List<Object> rightList,
                                               List<String> keys) {
        if (null == leftList || null == rightList) {
            return getResultWhenEitherIsNull(path, leftList, rightList);
        }
        Set<Difference> result = new HashSet<>();
        if (keys.isEmpty()) {
            if (!equals(leftList, rightList)) {
                result.add(new Difference(path, leftList, rightList));
            }
            return result;
        }

        List<Object> rightListCopy = new ArrayList<>(rightList);
        // When the keys param is provided, the elements in the lists are maps.
        for (Object leftValue : leftList) {
            Map<String, Object> leftProd = toMap(leftValue);
            Object rightValue = rightListCopy.stream()
                    .filter(item -> haveSameKeys(leftProd, toMap(item), keys))
                    .findFirst()
                    .orElse(null);
            Map<String, Object> rightProd = toMap(rightValue);
            result.addAll(doCompare(
                    path + getPathInList(leftValue, keys),
                    leftProd,
                    rightProd
            ));
            rightListCopy.remove(rightValue);
        }
        if (!rightListCopy.isEmpty()) {
            rightListCopy.forEach(actual ->
                    result.add(new Difference(path + getPathInList(actual, keys), null, actual))
            );
        }
        return result;
    }

    private static Set<Difference> getResultWhenEitherIsNull(String path, Object expected, Object actual) {
        if (expected == actual) {
            return Collections.emptySet();
        } else {
            return Collections.singleton(new Difference(path, expected, actual));
        }
    }

    private static String getPathInList(Object map, List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try {
            sb.append('[');

            keys.forEach(key -> sb.append(String.format("%s=%s,", key, JsonPath.read(map, key))));
            sb.setCharAt(sb.length() - 1, ']');
        } catch (PathNotFoundException e) {
            //ignore
            return "";
        }
        return sb.toString();
    }

    private static boolean equals(Object oracleValue, Object mongoValue) {
        if (oracleValue == mongoValue) {
            return true;
        }

        if (oracleValue == null || mongoValue == null) {
            return false;
        }

        if (oracleValue instanceof LocalDateTime && mongoValue instanceof Date) {
            oracleValue = Date.from(((LocalDateTime) oracleValue).atZone(ZoneId.systemDefault()).toInstant());
            return mongoValue.equals(oracleValue);
        }

        return oracleValue.equals(mongoValue);
    }

    private static boolean haveSameKeys(Map<String, Object> a, Map<String, Object> b, List<String> keys) {
        for (String key : keys) {
            Object obj1 = null;
            Object obj2 = null;
            try {
                obj1 = JsonPath.read(a, key);
            } catch (PathNotFoundException e) {
                //ignore
            }
            try {
                obj2 = JsonPath.read(b, key);
            } catch (PathNotFoundException e) {
                //ignore
            }
            if (!equals(obj1, obj2)) {
                return false;
            }
        }
        return true;
    }

    private static Map<String, Object> toMap(Object fromValue) {
        return (Map<String, Object>) fromValue;
    }

    private static List<Object> toList(Object fromValue) {
        return (List<Object>) fromValue;
    }
}
