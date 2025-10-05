package com.dummy.wmd.wpc.graphql.utils;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.MetadataService;
import com.jayway.jsonpath.JsonPath;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings({"java:S3010", "java:S1118"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class DataProcessingUtils {
    private static final int MAX_MESSAGE_LEN = 200;
    private static Map<String, String> businessNameMap = new LinkedHashMap<>();

    public DataProcessingUtils(MetadataService metadataService){
        businessNameMap = metadataService.getBusinessNameMapping();
    }

    /**
     * Convert the raw error message into a simplified one for better display
     *
     * @param message
     * @return
     */
    public static String simplifyErrorMessage(String message) {
        String error = null;
        if(message.startsWith("{") && message.endsWith("}") && message.contains("invalidProducts")) {
            error = productValidationError(message);
        } else if(message.contains("ProductNotFoundException:")) {
            error = productNotFoundException(message);
        } else if(message.contains("Exception:")) {
            error = normalException(message);
        } else {
            error = shortMessage(message);
        }
        return error;
    }

    /**
     * @param message
     * @return
     */
    private static String productNotFoundException(String message) {
        message = normalException(message);
        if(message.startsWith("com.dummy.wpb.product.error.ProductNotFoundException: ")){
            message = message.substring(53);
        }
        return message;
    }

    /**
     * Extract the 'code' field from product validation messages and the join as a string
     *
     * @param message
     * @return
     */
    private static String productValidationError(String message) {
        List<LinkedHashMap<String, String>> errorList = JsonPath.read(message, "$..errors[*]");
        List<String> list = errorList.stream().map(err -> {
            String jsonPath = err.get(Field.jsonPath);
            String businessName = businessNameMap.getOrDefault(jsonPath, jsonPath);
            String msg = err.get(Field.message);
            return businessName + ": " + msg;
        }).collect(Collectors.toList());
        return String.join(";\n", list);
    }

    /**
     * Convert normal exception stack, keep only the first line
     *
     * @param message
     * @return
     */
    private static String normalException(String message) {
        int idx = message.indexOf("\n");
        if (idx > 0) {
            return message.substring(0, idx).trim();
        }
        return shortMessage(message);
    }

    /**
     * Simply cut the message from begging and keep 100 chars only
     *
     * @param message
     * @return
     */
    private static String shortMessage(String message) {
        int len = message.length();
        if(len <= MAX_MESSAGE_LEN) {
            return message;
        } else {
            return message.substring(0, MAX_MESSAGE_LEN) + " ...";
        }
    }

    /**
     * Convert raw data JSON string into Map
     * @param data
     * @return
     */
    public static Map<String, Object> convertRawData(String data) {
        if(data.startsWith("{")){
            return ObjectMapperUtils.readValue(data, Map.class);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        if(data.startsWith("[")) {
            List<Map<String, Object>> list = ObjectMapperUtils.readValue(data, List.class);
            list.forEach(item -> result.put((String)item.get("name"), item.get("value")));
        }

        return result;
    }

    /**
     * Convert payload JSON string into Map
     * @param data
     * @return
     */
    public static List<Map<String, Object>> convertPayload(String data) {
        return ObjectMapperUtils.readValue(data, List.class);
    }
}
