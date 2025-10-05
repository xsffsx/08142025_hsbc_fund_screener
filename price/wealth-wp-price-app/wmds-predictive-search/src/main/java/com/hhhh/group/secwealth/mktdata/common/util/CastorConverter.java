/*
 */
package com.hhhh.group.secwealth.mktdata.common.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;

public final class CastorConverter {

    private CastorConverter() {}

    /**
     * Convert bean object to xml string
     *
     * @return the String
     *
     */
    public static String castorBeanToXMLConvert(final Object obj, final String requestMapContent) throws Exception {
        Writer out = null;
        try {
            out = new StringWriter();
            Mapping mapping = new Mapping();
            mapping.loadMapping(new InputSource(new StringReader(requestMapContent)));

            Marshaller marshaller = new Marshaller(out);
            marshaller.setMapping(mapping);
            marshaller.marshal(obj);

            return out.toString();
        } catch (Exception e) {
            LogUtil.error(CastorConverter.class,
                "SystemException: CastorConverter, castorBeanToXMLConvert, Param: " + obj.toString(), e);
            throw new SystemException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                LogUtil.error(CastorConverter.class, "can not close java.io.Writer|exception=" + e.getMessage(), e);
                // throw new SystemException(e);
            }
        }

    }

    /**
     * Convert xml string to bean object.
     *
     * @return the object
     */
    @SuppressWarnings("rawtypes")
    public static Object castorXMLToBeanConvert(final String sourceStream, final String responseMapContent,
        final Class responseClass, final boolean blankPreserve) throws Exception {
        StringReader reader = null;
        try {
            reader = new StringReader(sourceStream);
            Mapping mapping = new Mapping();
            mapping.loadMapping(new InputSource(new StringReader(responseMapContent)));

            Unmarshaller unMarshaller = new Unmarshaller(responseClass);
            unMarshaller.setIgnoreExtraElements(true);
            // preserve the newline and whitespace
            unMarshaller.setWhitespacePreserve(blankPreserve);
            unMarshaller.setMapping(mapping);

            return unMarshaller.unmarshal(reader);
        } catch (Exception e) {
            LogUtil.error(CastorConverter.class, "SystemException: CastorConverter, castorXMLToBeanConvert", e);
            throw new SystemException(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                LogUtil.error(CastorConverter.class, "can not close java.io.StringReader|exception=" + e.getMessage(), e);
                // throw new SystemException(e);
            }
        }

    }

}