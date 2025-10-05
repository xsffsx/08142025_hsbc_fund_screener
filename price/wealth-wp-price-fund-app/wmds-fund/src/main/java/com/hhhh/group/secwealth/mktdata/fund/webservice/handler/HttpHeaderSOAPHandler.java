
package com.hhhh.group.secwealth.mktdata.fund.webservice.handler;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.lang.StringUtils;

import com.hhhh.group.secwealth.mktdata.common.dao.impl.CustomerContextHolder;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;


public class HttpHeaderSOAPHandler implements SOAPHandler<SOAPMessageContext> {

    public static boolean inbound = false;

    public HttpHeaderSOAPHandler(final boolean inbound){
        HttpHeaderSOAPHandler.inbound = inbound;
    }

    public boolean handleMessage(final SOAPMessageContext context) {

        Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outbound != null && outbound.booleanValue()) {
            @SuppressWarnings("unchecked")
            Map<String, List<String>> requestHeaders = (Map<String, List<String>>) context.get(MessageContext.HTTP_REQUEST_HEADERS);
            if (requestHeaders == null) {
                requestHeaders = new HashMap<String, List<String>>();
                context.put(MessageContext.HTTP_REQUEST_HEADERS, requestHeaders);
            }

            String hhhhSamlToken = null;
            if(null != CustomerContextHolder.getHeaderMap()){
                hhhhSamlToken = CustomerContextHolder.getHeaderMap().get("X-hhhh-Saml");
            }
            LogUtil.debug(HttpHeaderSOAPHandler.class, "[CustomerContextHolder]-hhhhSamlToken is : " + hhhhSamlToken);

            // set e2e trust token in request header
            if (StringUtils.isNotBlank(hhhhSamlToken)) {
                requestHeaders.put("X-hhhh-Saml", Collections.singletonList(hhhhSamlToken));
            }

            LogUtil.debug(HttpHeaderSOAPHandler.class, "\nOutbound message:");
            logMessage(context, "SOAP Message is : ");

        }else{
            if(HttpHeaderSOAPHandler.inbound){
                LogUtil.debug(HttpHeaderSOAPHandler.class,"\nInbound message:");
                logMessage(context, "SOAP Message is : ");
            }
        }
        return true;
    }

    public boolean handleFault(final SOAPMessageContext context) {
        LogUtil.debug(HttpHeaderSOAPHandler.class, "Client : handleFault()......");
        logMessage(context, "SOAP Error is : ");
        return false;
    }

    public void close(final MessageContext context) {
        LogUtil.debug(HttpHeaderSOAPHandler.class, "Client : close()......");

    }

    public Set<QName> getHeaders() {
        LogUtil.debug(HttpHeaderSOAPHandler.class, "Client : getHeaders()......");
        return null;
    }

    private void logMessage(final SOAPMessageContext context, final String type) {
        try {
            Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
            if (outboundProperty) {
                LogUtil.debug(HttpHeaderSOAPHandler.class, "Outbound " + type);
            } else {
                LogUtil.debug(HttpHeaderSOAPHandler.class, "Inbound " + type);
            }

            SOAPMessage message = context.getMessage();

            // Print out the MIME headers
            MimeHeaders headers = message.getMimeHeaders();
            Iterator<MimeHeader> headersIterator = headers.getAllHeaders();
            MimeHeader mimeHeader;
            LogUtil.debug(HttpHeaderSOAPHandler.class, "  Mime headers :");
            while (headersIterator.hasNext()) {
                mimeHeader = headersIterator.next();
                LogUtil.debug(HttpHeaderSOAPHandler.class, "    " + mimeHeader.getName() + " : " + mimeHeader.getValue());
            }

            // Print out the message body
            LogUtil.debug(HttpHeaderSOAPHandler.class, "  Message body :");
            try (OutputStream outStream = new ByteArrayOutputStream()) {
                message.writeTo(outStream);
                LogUtil.debug(HttpHeaderSOAPHandler.class, "Message Stream is : " + outStream.toString());
            }
        } catch (Exception e) {
            LogUtil.error(HttpHeaderSOAPHandler.class, "Error logging SOAP message", e);
        }
    }
}
