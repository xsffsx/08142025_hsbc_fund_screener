/*
 */
package com.hhhh.group.secwealth.mktdata.common.exception;

import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;

/**
 * The Class ErrResponseAdapter
 */
@Component("errResponseAdapter")
public class ErrResponseAdapter {

    @Value("#{systemConfig['app.exception.configPath']}")
    private String configPath;

    @Value("#{systemConfig['app.exception.errorResponseConfig']}")
    private String errorResponseConfig;

    @Value("#{systemConfig['app.exception.errorResponseMapping']}")
    private String errorResponseMapping;

    /** The err response holder. */
    private ErrResponseHolder errResponseHolder;

    /**
     * Inits the.
     *
     * @throws Exception
     *             the exception
     */
    @PostConstruct
    public void init() throws Exception {
        URL errResponseURL = this.getClass().getClassLoader().getResource(this.configPath + this.errorResponseConfig);
        URL mappingFileURL = this.getClass().getClassLoader().getResource(this.configPath + this.errorResponseMapping);
        InputStream configIn = null;
        InputStream mappingIn = null;
        try {
            configIn = errResponseURL.openStream();
            mappingIn = mappingFileURL.openStream();
            String configStr = StringUtil.streamToStringConvert(configIn);
            String mappingStr = StringUtil.streamToStringConvert(mappingIn);

            Mapping mapping = new Mapping();
            mapping.loadMapping(new InputSource(new StringReader(mappingStr)));
            Unmarshaller unMarshaller = new Unmarshaller(ErrResponseHolder.class);
            unMarshaller.setIgnoreExtraElements(true);
            unMarshaller.setMapping(mapping);
            this.errResponseHolder = (ErrResponseHolder) unMarshaller.unmarshal(new StringReader(configStr));
        } catch (Exception e) {
            LogUtil.error(ErrResponseAdapter.class, "Can't init ErrResponseAdapter|exception=" + e.getMessage(), e);
            throw new SystemException(ErrTypeConstants.SYSTEM_INIT_ERROR, e);
        } finally {
            try {
                if (configIn != null) {
                    configIn.close();
                }
                if (mappingIn != null) {
                    mappingIn.close();
                }
            } catch (Exception e) {
                LogUtil.error(ErrResponseAdapter.class,
                    "Init ErrResponseAdapter, can not close java.io.InputStream|exception=" + e.getMessage(), e);
                // throw new SystemException(e);
            }
        }

    }

    /**
     * Gets the err response.
     *
     * @param key
     *            the key
     * @return the err response
     * @throws Exception
     *             the exception
     */
    public String[] getErrResponseArray(final String key) {
        ErrResponse err = this.errResponseHolder.getErrResponses().get(key);
        if (null != err) {
            return new String[] {err.getResponseCode(), err.getReasonCode(), err.getText()};
        } else {
            LogUtil.error(ErrResponseAdapter.class,
                "SystemException: ErrResponseAdapter, getErrResponseArray, Can't find the error key={}", key);
            err = this.errResponseHolder.getErrResponses().get(ErrTypeConstants.UNDEFINED);
            return new String[] {err.getResponseCode(), err.getReasonCode(), err.getText()};
        }
    }

    public ErrResponse getErrResponseObject(final String key) {
        ErrResponse err = this.errResponseHolder.getErrResponses().get(key);
        if (null == err) {
            LogUtil.error(ErrResponseAdapter.class,
                "SystemException: ErrResponseAdapter, getErrResponseObject, Can't find the error key={}", key);
            err = this.errResponseHolder.getErrResponses().get(ErrTypeConstants.UNDEFINED);
        }
        return err;
    }

    /**
     * Gets the err response holder.
     *
     * @return the err response holder
     */
    public ErrResponseHolder getErrResponseHolder() {
        return this.errResponseHolder;
    }

    /**
     * Sets the err response holder.
     *
     * @param errResponseHolder
     *            the new err response holder
     */
    public void setErrResponseHolder(final ErrResponseHolder errResponseHolder) {
        this.errResponseHolder = errResponseHolder;
    }
}
