package com.hhhh.group.secwealth.mktdata.elastic.processor;

import com.hhhh.group.secwealth.mktdata.elastic.bean.DataSiteEntity;
import com.hhhh.group.secwealth.mktdata.elastic.bean.Version;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.service.DataHandlerCommonService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileReader;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DebtInstmProductProcessorTest {
    @Mock
    private DataHandlerCommonService dataHandlerCommonService;
    @InjectMocks
    private DebtInstmProductProcessor underTest;

    @Nested
    class WhenProcessing {
        private final String SITE_KEY = "HK_HBAP";
        @Mock
        private DataSiteEntity entity;
        @Mock
        private DataSiteEntity.DataFileInfo info;

        @Test
        void test_process() throws Exception {
            String path = "src/test/resources/files/HK_HBAP_WMDS_BOND_20230530081158_35.xml";
            File file = new File(path);
            XMLInputFactory xmlif = XMLInputFactory.newInstance();
            XMLStreamReader xmlr = xmlif.createXMLStreamReader(new FileReader(file));
            Version version = new Version("20230530081158", "35");
            File mockFile = mock(File.class);
            when(info.getDataFile()).thenReturn(mockFile);
            when(mockFile.getName()).thenReturn("HK_HBAP_WMDS_BOND_20230530081158_35.xml");
            when(dataHandlerCommonService.handleCommonData(any(), any(), any(), any())).thenReturn(new CustomizedEsIndexForProduct());
            assertDoesNotThrow(() -> underTest.process(xmlr, entity, SITE_KEY, version, info));
        }
    }
}
