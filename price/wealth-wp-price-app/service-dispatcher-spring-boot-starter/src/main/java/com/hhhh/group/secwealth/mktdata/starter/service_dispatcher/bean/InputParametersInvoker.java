/*
 */
package com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputParametersInvoker {

    private static final Logger logger = LoggerFactory.getLogger(InputParametersInvoker.class);

    /**
     * <p>
     * <b>Get LinkedHashMap of client input paramters. </b>
     * </p>
     *
     * @param configParamsList
     * @param header
     * @param request
     * @return
     */
    public LinkedHashMap<String, String> getClientInputParamsMap(final List<String> configParamsList, final Object header,
        final Object request) {

        LinkedHashMap<String, String> clientParamsMap = new LinkedHashMap<>();
        // Invoke Header
        invokeInputParameters(configParamsList, header, clientParamsMap);
        // Invoke Request
        invokeInputParameters(configParamsList, request, clientParamsMap);

        return clientParamsMap;
    }

    /**
     * <p>
     * <b> Get list of client input paramters. </b>
     * </p>
     *
     * @param map
     * @return
     */
    public List<String> getClientInputParamsList(final List<String> configParamsList, final Object header, final Object request) {

        LinkedHashMap<String, String> map = getClientInputParamsMap(configParamsList, header, request);

        List<String> paramsValuesList = null;
        if (map != null && !map.isEmpty()) {
            paramsValuesList = new ArrayList<String>(map.values());
        }
        return paramsValuesList;
    }

    /**
     * <p>
     * <b>Invoke client input parameters value. </b>
     * </p>
     *
     * @param list
     *            of client request paramters
     * @param object
     * @param map
     */
    private void invokeInputParameters(final List<String> list, final Object object, final Map<String, String> map) {

        if (list == null || list.isEmpty() || object == null) {
            return;
        }

        try {
            Class<? extends Object> clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                String fieldName = field.getName();
                if (list.contains(fieldName)) {
                    field.setAccessible(true);
                    String value = String.valueOf(field.get(object));
                    map.put(fieldName, value);
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            InputParametersInvoker.logger.error("Error to invoke client input paramters. Class:" + object.getClass(), ex);
        }
    }

}
