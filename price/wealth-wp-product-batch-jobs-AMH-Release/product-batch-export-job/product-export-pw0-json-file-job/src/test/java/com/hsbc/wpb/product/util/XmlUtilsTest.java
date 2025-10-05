package com.dummy.wpb.product.util;

import com.dummy.wpb.product.utils.CommonUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.soap.MessageFactory;

import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class XmlUtilsTest {

    @Test
    void testUnmarshalException() {
        try (
                MockedStatic<MessageFactory> messageFactoryUtilsMockedStatic = Mockito.mockStatic(
                        MessageFactory.class)
        ) {
            messageFactoryUtilsMockedStatic.when(() -> MessageFactory.newInstance(anyString()))
                    .thenThrow(RuntimeException.class);

        }
        Assertions.assertNotNull(XmlUtils.unmarshal(CommonUtils.readResource("/response/pw0-resp.xml"), Object.class));
    }

    @Test
    void testUnmarshal() {
        Assertions.assertNotNull(XmlUtils.unmarshal(CommonUtils.readResource("/response/pw0-resp.xml"), Object.class));
    }
}