package com.dummy.wpb.wpc.utils.service;

import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({"java:S3010"})
public class ProdReservedAttrUtils {

    private static List<String> reservedAttrs = Collections.emptyList();


    public ProdReservedAttrUtils(MongoDatabase mongodb){
        Document reservedAttrDoc = mongodb.getCollection(CollectionName.configuration).find(Filters.eq(Field._id, "ALL/product-reserved-attrs")).first();
        if (MapUtils.isNotEmpty(reservedAttrDoc)){
            reservedAttrs =  reservedAttrDoc.get("config", List.class);
        }
    }
    
    /**
     * Extract reserve attributes from oldProd and put into the newProd document
     *
     * @param oldProd
     * @param newProd
     */
    public static void copyReservedAttrs(Document oldProd, Document newProd) {
        DocumentContext newContext = JsonPath.parse(newProd);
        DocumentContext oldContext = JsonPath.parse(oldProd);

        for (String reservedAttr : reservedAttrs) {

            Object value = readValue(oldContext, reservedAttr);

            if (null == value){
                continue;
            }

            try {
                if (reservedAttr.matches("(.*)(\\[(.*?)])(.*)")) {
                    setObjectListAttr(reservedAttr, newContext, value);
                } else {
                    setFieldAttr(reservedAttr, newContext, value);
                }
            } catch (Exception e) {
                log.error("An error occurred while copy reserve attribute {}. Cause:{} ", reservedAttr, e.getMessage());
            }
        }
    }

    private static void setObjectListAttr(String reservedAttr, DocumentContext context, Object value) {
        List<Map<String,Object>> valueList = (List<Map<String,Object>>) value;
        if (CollectionUtils.isNotEmpty(valueList)) {
            String parent = reservedAttr.substring(0, reservedAttr.indexOf('['));
            context.add(parent, valueList.get(0));
        }
    }

    private static void setFieldAttr(String reservedAttr, DocumentContext context, Object value) {
        if (reservedAttr.contains(".")) {
            String parent = reservedAttr.substring(0, reservedAttr.lastIndexOf("."));

            if (Objects.isNull(readValue(context, parent))) {
                setFieldAttr(parent, context, new LinkedHashMap<>()); // make sure parent exist
            }

            String attr = reservedAttr.substring(reservedAttr.lastIndexOf(".") + 1);
            context.put(parent, attr, value);
        } else {
            context.put("$", reservedAttr, value);
        }
    }

    /**
     * Read a value from the DocumentContext with given jsonPath
     *
     * @param context
     * @param jsonPath
     * @return
     * @param <T>
     */
    private static <T> T readValue(DocumentContext context, String jsonPath) {
        T result = null;
        try {
            result = context.read(jsonPath);
        } catch (PathNotFoundException e) {
            // ignore, return null in this case
        }
        return result;
    }
}
