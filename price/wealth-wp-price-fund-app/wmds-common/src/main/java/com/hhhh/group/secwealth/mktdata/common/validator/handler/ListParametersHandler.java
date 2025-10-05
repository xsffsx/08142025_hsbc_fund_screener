package com.hhhh.group.secwealth.mktdata.common.validator.handler;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.ServiceEntity;

/**
 * Custom Address Handler
 * 
 * @author Unmi Qiu CreateTime: Apr 23, 2011
 */
public class ListParametersHandler implements FieldHandler {
    @Override
    public Object getValue(final Object object) throws IllegalStateException {
        ServiceEntity entity = (ServiceEntity) object;
        if (entity.getReqListParameters() == null) {
            return null;
        }
        return entity.getReqListParameters();
    }

    @Override
    public void setValue(final Object object, final Object value) throws IllegalStateException, IllegalArgumentException {
        ServiceEntity entity = (ServiceEntity) object;
        String[] reqListParameters = ((String) value).split(CommonConstants.SYMBOL_SEPARATOR);
        entity.setReqListParameters(reqListParameters);
    }

    @Override
    public void checkValidity(final Object arg0) throws ValidityException, IllegalStateException {}

    @Override
    public Object newInstance(final Object arg0) throws IllegalStateException {
        return null;
    }

    @Override
    public void resetValue(final Object arg0) throws IllegalStateException, IllegalArgumentException {}

}