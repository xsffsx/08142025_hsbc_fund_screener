package com.hhhh.group.secwealth.mktdata.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.JSONUtils;
import net.sf.json.util.PropertyFilter;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;

public final class JsonUtil {
    public static JSONObject beanToJsonOjbect(final Object obj) throws Exception {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        return JSONObject.fromObject(obj, jsonConfig);
    }

    public static JSONArray beanToJsonArray(final Object obj) throws Exception {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        return JSONArray.fromObject(obj, jsonConfig);
    }

    public static Map<?, ?> beanToMap(final Object obj) throws Exception {
        JSONObject jsonObject = null;
        if (obj instanceof JSONObject) {
            jsonObject = (JSONObject) obj;
        } else {
            jsonObject = JSONObject.fromObject(obj);
        }

        Map<Object, Object> map = new HashMap<Object, Object>(jsonObject.size());
        Iterator<?> it = jsonObject.keys();
        while (it.hasNext()) {
            Object key = it.next();
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                map.put(key, beanToMap(value));
            } else if (value instanceof JSONArray) {
                map.put(key, jsonArrayToList(value));
            } else {
                map.put(key, value);
            }
        }

        return map;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<?> jsonArrayToList(final Object obj) throws Exception {

        JSONArray jsonArray = null;
        if (obj instanceof JSONArray) {
            jsonArray = (JSONArray) obj;
        } else {
            jsonArray = JSONArray.fromObject(obj);
        }

        List list = new ArrayList(jsonArray.size());
        Iterator<?> it = jsonArray.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next instanceof JSONObject) {
                list.add(beanToMap(next));
            } else if (next instanceof JSONArray) {
                list.add(beanToMap(next));
            } else {
                list.add(next);
            }
        }
        return list;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T jsonToBean(final String jsonStr, final Class<T> clazz, final Map<String, Class> subClassMap)
        throws Exception {
        JsonConfig filterNullConfig = new JsonConfig();
        filterNullConfig.setJsonPropertyFilter(new PropertyFilter() {
            @Override
            public boolean apply(final Object clazz, final String name, final Object value) {
                boolean isFilter = false;
                if (value == null || "".equals(value)) {
                    isFilter = true;
                }
                return isFilter;
            }
        });
        JSONObject jsonObject = JSONObject.fromObject(jsonStr, filterNullConfig);
        String[] dateFormats = new String[] {DateConstants.DateFormat_yyyyMMddHHmmss_withSymbol,
            DateConstants.DateFormat_yyyyMMdd_withHyphen, DateConstants.DateFormat_yyyyMMddHHmm_withSymbol};
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFormats));

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(clazz);
        if (subClassMap != null && subClassMap.size() > 0) {
            jsonConfig.setClassMap(subClassMap);
        }
        return (T) JSONSerializer.toJava(jsonObject, jsonConfig);
    }

    @SuppressWarnings("unchecked")
    public static <T> T jsonToBean(final String jsonString, final Class<T> clazz) throws Exception {
        try {
            JSONObject jsonObject = JSONObject.fromObject(jsonString);
            return (T) JSONObject.toBean(jsonObject, clazz);
        } catch (Exception e) {
            String message = "jsonToBean error: jsonString: " + jsonString + " clazz: " + clazz.getName();
            LogUtil.error(JsonUtil.class, message, e);
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, message);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T jsonObjectToBean(final JSONObject jsonObject, final Class<T> clazz) throws Exception {
        try {
            return (T) JSONObject.toBean(jsonObject, clazz);
        } catch (Exception e) {
            String message = "jsonObjectToBean error: jsonObject: " + jsonObject.toString() + " clazz: " + clazz.getName();
            LogUtil.error(JsonUtil.class, message, e);
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, message);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> T jsonObjectToBean(final JSONObject jsonObject, final Class<T> clazz, final Map<String, Class> classMap)
        throws Exception {
        try {
            return (T) JSONObject.toBean(jsonObject, clazz, classMap);
        } catch (Exception e) {
            String message = "jsonObjectToBean error: jsonObject: " + jsonObject.toString() + " clazz: " + clazz.getName();
            LogUtil.error(JsonUtil.class, message, e);
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, message);
        }
    }

    public static Object[] jsonArrayToArray(final JSONArray jsonArray, final Class<?> clazz) throws Exception {
        Object[] array = new Object[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            array[i] = JSONObject.toBean(jsonArray.getJSONObject(i), clazz);
        }
        return array;
    }

    public static Object[] jsonArrayToArray(final JSONArray jsonArray, final Class<?> clazz, final Map<?, ?> classMap)
        throws Exception {
        Object[] array = new Object[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            array[i] = JSONObject.toBean(jsonArray.getJSONObject(i), clazz, classMap);
        }
        return array;
    }

    public static List<Object> jsonArrayToList(final JSONArray jsonArray) throws Exception {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i));
        }
        return list;
    }

    public static List<Object> jsonArrayToList(final JSONArray jsonArray, final Class<?> clazz) throws Exception {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(JSONObject.toBean(jsonArray.getJSONObject(i), clazz));
        }
        return list;
    }

    public static List<Object> jsonArrayToList(final JSONArray JsonArray, final Class<?> clazz, final Map<?, ?> classMap)
        throws Exception {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < JsonArray.size(); i++) {
            list.add(JSONObject.toBean(JsonArray.getJSONObject(i), clazz, classMap));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonObjectToMap(final JSONObject jsonObject) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        for (Iterator<String> iter = jsonObject.keys(); iter.hasNext();) {
            String key = (String) iter.next();
            map.put(key, jsonObject.get(key));
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonObjectToMap(final JSONObject jsonObject, final Class<?> clazz) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        for (Iterator<String> iter = jsonObject.keys(); iter.hasNext();) {
            String key = (String) iter.next();
            map.put(key, JSONObject.toBean(jsonObject.getJSONObject(key), clazz));
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonObjectToMap(final JSONObject jsonObject, final Class<?> clazz, final Map<?, ?> classMap)
        throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        for (Iterator<String> iter = jsonObject.keys(); iter.hasNext();) {
            String key = (String) iter.next();
            map.put(key, JSONObject.toBean(jsonObject.getJSONObject(key), clazz, classMap));
        }
        return map;
    }
}
