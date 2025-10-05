/*
 */
package com.hhhh.group.secwealth.mktdata.wmds_entitlement.interceptor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;

/**
 * <p>
 * <b> Entitlement Service. </b>
 * </p>
 */
@Service
public class EntitlementService {

    /**
     * <p>
     * <b>Check if have authorized access to service.</b>
     * </p>
     *
     * @param headerMap
     * @param whileListRules
     * @return
     */
    @Cacheable(cacheNames = "entitlementCache", keyGenerator = "entitlementCacheKeyGenerator", unless = "#result == null")
    public boolean hasEntitlement(final String serviceName, final List<String> checklist, final Map<String, String> headerMap,
        final LinkedHashMap<String, List<String>> whiteListRules) {

        if (whiteListRules == null || whiteListRules.isEmpty()) {
            return true;
        }

        String headerValue = "";
        List<String> valueList = new ArrayList<>();
        for (String key : whiteListRules.keySet()) {
            valueList = whiteListRules.get(key);
            if (key.indexOf(SymbolConstant.SYMBOL_UNDERLINE) == -1) {
                headerValue = headerMap.get(key);
            } else {
                headerValue = key;
                String[] keyArray = key.split(SymbolConstant.SYMBOL_UNDERLINE);
                for (int index = 0; index < keyArray.length; index++) {
                    String tempStr = headerMap.get(keyArray[index]);
                    if (tempStr != null) {
                        headerValue = headerValue.replace(keyArray[index], tempStr);
                    }
                }
            }
            if (!valueList.contains(headerValue)) {
                return false;
            }
        }
        return true;
    }
}
