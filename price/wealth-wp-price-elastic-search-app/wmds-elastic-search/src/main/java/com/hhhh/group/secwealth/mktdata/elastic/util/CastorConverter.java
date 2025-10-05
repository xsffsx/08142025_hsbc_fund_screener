/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

public final class CastorConverter {

    private CastorConverter() {}

    public static Object convertXMLToBean(URL mappingURL, String content, Class<?> targetClass, boolean blankPreserve) throws IOException, MappingException, MarshalException, ValidationException {
        final StringReader reader = new StringReader(content);
        final Mapping mapping = new Mapping();
        mapping.loadMapping(mappingURL);
        final Unmarshaller unMarshaller = new Unmarshaller(targetClass);
        unMarshaller.setIgnoreExtraElements(true);
        unMarshaller.setWhitespacePreserve(blankPreserve);
        unMarshaller.setMapping(mapping);
        return unMarshaller.unmarshal(reader);
    }

    public static Object convertXMLToBean(URL mappingURL, URL configURL, Class<?> targetClass, boolean blankPreserve) throws MarshalException, MappingException, ValidationException, IOException {
        InputStream configStream = null;
        String content;
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
