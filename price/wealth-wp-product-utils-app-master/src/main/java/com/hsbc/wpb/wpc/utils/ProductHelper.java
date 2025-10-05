package com.dummy.wpb.wpc.utils;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductHelper {
    private static Configuration conf = Configuration.defaultConfiguration()
            .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
            .addOptions(Option.SUPPRESS_EXCEPTIONS);

    public static void remodelViaConfig(Map<String, Object> prod, Document config) {
        DocumentContext doc = JsonPath.using(conf).parse(prod);
        // handle "copy" fields
        config.keySet().stream().forEach(configKey -> {
            List<Map<String, String>> copyList = (List<Map<String, String>>) config.get(configKey);
            copyList.forEach(item -> {
                String fieldCde = item.get("fieldCde");
                // only udf field will to camel case
                if ("udf".equalsIgnoreCase(configKey)) {
                    fieldCde = CodeUtils.toCamelCase(fieldCde);
                }
                moveAndDeleteDuplicate(doc, configKey, fieldCde, item.get("jsonPath"));
            });
        });
    }

    /**
     * Move value from one path to another and delete duplicate data
     * if cant find data in specific path, try to find other path
     * 
     * @param doc
     * @param configKey
     * @param fieldCde
     * @param to
     */
    private static void moveAndDeleteDuplicate(DocumentContext doc, String configKey, String fieldCde, String to) {
        String from = configKey + "." + fieldCde;
        List<String> otherKeys = new ArrayList<>(Arrays.asList("ext", "extOp", "extEg", "udf"));
        otherKeys.remove(configKey);
        Object value = doc.read(from, Object.class);
        if (null != value) {
            // if exists, delete duplicate data
            otherKeys.forEach(node -> doc.delete(node + "." + fieldCde));
            // move to
            if (!StringUtils.equals(from, to)) {
                createParents(doc, to);
                doc.set(to, value);
                doc.delete(from);
            }
        } else {
            doc.delete(from);
            // if not exists, find in other path
            for (String node : otherKeys) {
                String otherPath = node + "." + fieldCde;
                // move to
                Object otherValue = doc.read(otherPath, Object.class);
                if (null != otherValue && !StringUtils.equals(otherPath, to)) {
                    createParents(doc, to);
                    doc.set(to, otherValue);
                    doc.delete(otherPath);
                    break;
                }
            }
        }
    }

    public static void copy(DocumentContext doc, String from, String to) {
        Object value = doc.read(from, Object.class);
        if (null != value) { // copy only not null value
            createParents(doc, to);
            doc.set(to, value);
        }
    }

    /**
     * Make sure parents exist, create one if no
     * TO_DO: no requirement to handle array at the moment, if there is, need to
     * consider how to handle array parent.
     * 
     * @param to
     */
    private static void createParents(DocumentContext doc, String to) {
        int idx = -1;
        while (-1 != (idx = to.indexOf(".", idx + 1))) {
            String path = to.substring(0, idx);
            if (path.endsWith("]")) {
                throw new IllegalArgumentException("List parent is not yet support: " + path);
            }
            Object obj = doc.read(path, Object.class);
            if (null == obj) {
                doc.set(path, new LinkedHashMap<>());
            }
        }
    }
}
