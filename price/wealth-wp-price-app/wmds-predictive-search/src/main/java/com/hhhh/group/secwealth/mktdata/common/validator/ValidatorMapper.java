/*
 */
package com.hhhh.group.secwealth.mktdata.common.validator;

import java.io.InputStream;
import java.util.Properties;

import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;

/**
 * <p>
 * <b> ValidatorMapper. </b>
 * </p>
 */
public class ValidatorMapper {

    private static ValidatorMapper instance = null;

    private static Properties mapper = new Properties();

    public static void init(final InputStream commonfile, final InputStream customfile) throws Exception {
        try {
            ValidatorMapper.mapper.load(commonfile);
            ValidatorMapper.mapper.load(customfile);
        } catch (Exception e) {
            LogUtil.error(ValidatorMapper.class,
                "SystemException: ValidatorMapper, init, Can't init ValidatorMapper|exception=" + e.getMessage(), e);
            throw new SystemException(ErrTypeConstants.SYSTEM_INIT_ERROR, e);
        } finally {
            try {
                if (commonfile != null) {
                    commonfile.close();
                }
                if (customfile != null) {
                    customfile.close();
                }
            } catch (Exception e) {
                LogUtil.error(ValidatorMapper.class,
                    "Init ValidatorMapper, can not close java.io.InputStream|exception=" + e.getMessage(), e);
                // throw new SystemException(e);
            }
        }
    }

    public static void init(final InputStream commonfile) throws Exception {
        try {
            ValidatorMapper.mapper.load(commonfile);
        } catch (Exception e) {
            LogUtil.error(ValidatorMapper.class,
                "SystemException: ValidatorMapper, init, Can't init ValidatorMapper|exception=" + e.getMessage(), e);
            throw new SystemException(ErrTypeConstants.SYSTEM_INIT_ERROR, e);
        } finally {
            try {
                if (commonfile != null) {
                    commonfile.close();
                }
            } catch (Exception e) {
                LogUtil.error(ValidatorMapper.class,
                    "Init ValidatorMapper, can not close java.io.InputStream|exception=" + e.getMessage(), e);
                // throw new SystemException(e);
            }
        }
    }

    public static synchronized ValidatorMapper getInstance() {
        if (ValidatorMapper.instance == null) {
            ValidatorMapper.instance = new ValidatorMapper();
        }
        return ValidatorMapper.instance;
    }

    public String getValidatorByType(final String type) {
        return (String) ValidatorMapper.mapper.get(type);
    }

}
