/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.util;

import com.hhhh.group.secwealth.mktdata.elastic.properties.VenderProdCdeAltClassCdeProp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceKeyToProdCdeAltClassCdeConvertor {

	@Autowired
   private VenderProdCdeAltClassCdeProp venderProdCdeAltClassCdeMap;

    public String doConvert(final String venderCode) {
        String out = null;
        if (null != venderCode) {
            out = venderProdCdeAltClassCdeMap.getVenderProdCdeAltClassCdeMap().get(venderCode);
            if (null == out) {
                out = venderProdCdeAltClassCdeMap.getVenderProdCdeAltClassCdeMap().get(CommonConstants.DEFAULT);
            }
        }
        return out;
    }
}
