/*
 */
package com.hhhh.group.secwealth.mktdata.common.convertor;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;

@Component("serviceKeyToProdCdeAltClassCdeConvertor")
public class ServiceKeyToProdCdeAltClassCdeConvertor {

    @Resource(name="venderProdCdeAltClassCdeMap")
    private Map<String, String> venderProdCdeAltClassCdeMap = new HashMap<String, String>();

    public String doConvert(final String venderCode) {
        String out = null;
        if (null != venderCode) {
            out = this.venderProdCdeAltClassCdeMap.get(venderCode.toString());
            if (null == out) {
                out = this.venderProdCdeAltClassCdeMap.get(CommonConstants.DEFAULT);
            }
        }
        return out;
    }
}
