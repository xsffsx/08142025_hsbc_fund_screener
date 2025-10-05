
package com.hhhh.group.secwealth.mktdata.fund;

import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.service.AbstractService;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;



@Component("abstractMdsbeService")
public abstract class AbstractMdsbeService extends AbstractService {

    @Override
    public Object execute(final Object object) throws Exception {
        try {
            return execute(object);
        } catch (Exception e) {
            LogUtil.error(AbstractMdsbeService.class,
                "SystemException: AbstractMdsbeService, doService, execute " + object.toString(), e);
            throw e;
        }
    }
}
