package com.hhhh.group.secwealth.mktdata.common.validator.handler;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;

import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.validator.ValidatorMapper;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.RuleSet;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.ServiceEntity;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.Validator;

/**
 * Custom Address Handler
 * 
 * @author Unmi Qiu CreateTime: Apr 23, 2011
 */
public class ValidatorsHandler implements FieldHandler {

    @Override
    public Object getValue(final Object object) throws IllegalStateException {
        ServiceEntity entity = (ServiceEntity) object;
        if (entity.getValidators() == null) {
            return null;
        }
        return entity.getValidators();
    }

    @Override
    public void setValue(final Object object, final Object value) throws IllegalStateException, IllegalArgumentException {
        ServiceEntity entity = (ServiceEntity) object;
        entity.getValidators().add(convert((RuleSet) value));
    }

    private Validator convert(final RuleSet ruleSet) throws IllegalArgumentException {
        String type = ruleSet.getType();
        if (null != type && !type.equals("")) {
            String validatorString = ValidatorMapper.getInstance().getValidatorByType(type);
            if (null != validatorString && validatorString.length() > 0) {
                try {
                    RuleSet val = (RuleSet) Class.forName(validatorString).newInstance();
                    LogUtil.debug(ValidatorsHandler.class,
                        "Init Validator Instance: type {}, validatorString: {},  InstanceName: {}", type, validatorString,
                        val.toString());
                    val.preValidate(ruleSet.getFields());
                    val.setType(type);
                    val.setErrorKey(ruleSet.getErrorKey());
                    val.setAssociateFields(ruleSet.getAssociateFields());
                    return (Validator) val;
                } catch (Exception e) {
                    LogUtil.error(ValidatorsHandler.class, "convert error", e);
                    throw new IllegalArgumentException(e);
                }
            } else {
                LogUtil.error(ValidatorsHandler.class, "The validator type is error");
                throw new IllegalArgumentException("The validator type is error");
            }
        } else {
            LogUtil.error(ValidatorsHandler.class, "The validator type is null");
            throw new IllegalArgumentException("The validator type is null");
        }

    }

    @Override
    public void checkValidity(final Object arg0) throws ValidityException, IllegalStateException {

    }

    @Override
    public Object newInstance(final Object arg0) throws IllegalStateException {
        return null;
    }

    @Override
    public void resetValue(final Object arg0) throws IllegalStateException, IllegalArgumentException {

    }

}