
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

    @PostConstruct
    public void init() throws Exception {
        URL errResponseURL = this.getClass().getClassLoader().getResource(this.configPath + this.errorResponseConfig);
        URL mappingFileURL = this.getClass().getClassLoader().getResource(this.configPath + this.errorResponseMapping);


        try (InputStream configIn = errResponseURL.openStream();
             InputStream mappingIn = mappingFileURL.openStream()){
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


    public ErrResponseHolder getErrResponseHolder() {
        return this.errResponseHolder;
    }


    public void setErrResponseHolder(final ErrResponseHolder errResponseHolder) {
        this.errResponseHolder = errResponseHolder;
    }
}
