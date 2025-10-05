package com.dummy.wmd.wpc.graphql.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.bson.Document;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@SuppressWarnings("java:S3740")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentUtils {
    /**
     * Use java object persistence to clone object
     *
     * @param obj
     * @param <T>
     * @return
     */
    @SneakyThrows
    public static <T> T clone(T obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(obj);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream is = new ObjectInputStream(bis);
        Object result = is.readObject();
        bos.close();
        bis.close();
        os.close();
        is.close();
        return (T)result;
    }

    /**
     * Since getLong for an Integer will trigger a cast exception, we need this extra layer to make it smarter.
     * So that even an attribute in the db is Integer, it can still be returned as a Long value.
     *
     * @param doc
     * @param key
     * @return
     */
    public static Long getLong(Document doc, String key) {
        if(null == doc) {
            return null;
        }

        Object value = doc.get(key);
        if(value instanceof Integer) {
            return ((Integer) value).longValue();
        }

        return (Long)value;
    }

    public static Long getLong(Map<String, Object> doc, String key) {
        return getLong(new Document(doc), key);
    }

    public static LocalDate getLocalDate(Map<String, Object> doc, String key) {
        if(null == doc) {
            return null;
        }

        Object date = doc.get(key);
        if(null == date) {
            return null;
        }
        if(date instanceof LocalDate) {
            return (LocalDate) date;
        }
        if(date instanceof Date) {
            return ((Date)date).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }

        throw new IllegalArgumentException("Unexpected data type: " + date);
    }

    /**
     * If all attributes in a sub Document are null, make the attribute null
     *
     * @param doc
     * @return
     */
    public static Document compactDocument(Document doc){
        Document outDoc = new Document();
        doc.forEach((k,v) -> {
            if(v instanceof Document) {
                Document subDoc = (Document)v;
                boolean isNotEmpty = subDoc.values().stream().anyMatch(Objects::nonNull);
                if(isNotEmpty) {
                    outDoc.put(k, compactDocument(subDoc));
                } else {
                    outDoc.put(k, null);
                }
            } else if(v instanceof List) {
                outDoc.put(k, compactList((List)v));
            } else {
                outDoc.put(k, v);
            }
        });
        return outDoc;
    }

    private static List compactList(List list) {
        List out = new ArrayList();
        list.forEach(item -> {
            if(item instanceof Document) {
                out.add(compactDocument((Document)item));
            } else {
                out.add(item);
            }
        });
        return out;
    }

    public static String getString(Map<String, Object> doc, String key) {
        String s = null;
        if (null == doc) {
            return s;
        }

        Object value = doc.get(key);
        if (value instanceof String) {
            s = (String) value;
        }

        return s;
    }
    
    /**
     * Get BigDecimal value from Document.
     * @param doc
     * @param key
     * @return
     */
    public static BigDecimal getBigDecimal(Map<String, Object> doc, String key) {
        BigDecimal result = null;
        if (null == doc) {
            return result;
        }

        Object value = doc.get(key);
        if (null == value) {
            return result;
        }

        if (value instanceof Double) {
            result = BigDecimal.valueOf((Double) value);
        } else if (value instanceof Long) {
            result = BigDecimal.valueOf((Long) value);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> extractDocList(Document doc, String nodeName) {
        Object dataList = doc.get(nodeName);
        if (Objects.isNull(dataList) || ((List<?>) dataList).isEmpty()) return new ArrayList<>();
        return (List<Map<String, Object>>) dataList;
    }
}
