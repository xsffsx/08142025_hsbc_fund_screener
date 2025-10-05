
package com.hhhh.group.secwealth.mktdata.fund.util;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;

public final class CreateUnmarshallerUtil {

    public static Unmarshaller getUnmarshaller(final String dataClass) throws Exception {
        try {
            JAXBContext jc = JAXBContext.newInstance(Class.forName(dataClass));
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            return unmarshaller;
        } catch (ClassNotFoundException e) {
            LogUtil
                .error(CreateUnmarshallerUtil.class, "CreateUnmarshallerUtil ClassNotFoundException, dataClass: " + dataClass, e);
            throw new SystemException(ErrTypeConstants.SYSTEM_INIT_ERROR, e);
        } catch (JAXBException e) {
            LogUtil.error(CreateUnmarshallerUtil.class, "CreateUnmarshallerUtil JAXBException, dataClass: " + dataClass, e);
            throw new SystemException(ErrTypeConstants.SYSTEM_INIT_ERROR, e);
        }
    }
}
