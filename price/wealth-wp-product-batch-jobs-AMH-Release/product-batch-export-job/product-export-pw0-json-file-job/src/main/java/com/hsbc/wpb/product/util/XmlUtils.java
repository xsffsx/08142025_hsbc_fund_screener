package com.dummy.wpb.product.util;

import com.dummy.wpb.product.exception.productBatchException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Slf4j
@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class XmlUtils {


    public static <T> T unmarshal(String xml, Class<T> toValueType) {
        try {
            MessageFactory msgFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
            SOAPMessage message = msgFactory.createMessage(null, new ByteArrayInputStream(xml.getBytes()));
            Document document = message.getSOAPBody().extractContentAsDocument();
            Unmarshaller unmarshaller = JAXBContext.newInstance(toValueType).createUnmarshaller();
            TransformerFactory transformerFactory = createSecureTransformerFactory();
            Transformer transformer = transformerFactory.newTransformer();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            transformer.transform(new DOMSource(document), new StreamResult(outputStream));
            XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
            xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            XMLStreamReader xsr = xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(outputStream.toByteArray()));
            return unmarshaller.unmarshal(xsr, toValueType).getValue();
        } catch (Exception e){
            throw new productBatchException(e);
        }
    }

    @SneakyThrows
    private static TransformerFactory createSecureTransformerFactory() {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        return transformerFactory;
    }
}
