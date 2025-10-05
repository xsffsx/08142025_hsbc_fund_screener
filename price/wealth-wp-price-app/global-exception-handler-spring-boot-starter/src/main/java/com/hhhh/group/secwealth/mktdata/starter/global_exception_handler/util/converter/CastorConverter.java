/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.util.converter;

import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;

public final class CastorConverter {

    private CastorConverter() {}

    public static Object convertXMLToBean(final URL mappingURL, final String content, final Class<?> targetClass,
        final boolean blankPreserve) throws Exception {
        final StringReader reader = new StringReader(content);
        final Mapping mapping = new Mapping();
        mapping.loadMapping(mappingURL);
        final Unmarshaller unMarshaller = new Unmarshaller(targetClass);
        unMarshaller.setIgnoreExtraElements(true);
        unMarshaller.setWhitespacePreserve(blankPreserve);
        unMarshaller.setMapping(mapping);
        return unMarshaller.unmarshal(reader);
    }

    public static Object convertXMLToBean(final URL mappingURL, final URL configURL, final Class<?> targetClass,
        final boolean blankPreserve) throws Exception {
        InputStream configStream = null;
        String content = null;
        try {
            configStream = configURL.openStream();
            content = IOUtils.toString(configStream, StandardCharsets.UTF_8);
        } finally {
            if (configStream != null) {
                configStream.close();
            }
        }
        return convertXMLToBean(mappingURL, content, targetClass, blankPreserve);
    }

}
