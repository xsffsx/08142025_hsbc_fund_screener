package com.hhhh.group.secwealth.mktdata.elastic.processor;

import com.google.common.collect.Lists;
import com.hhhh.group.secwealth.mktdata.elastic.bean.ChanlAttrSeg;
import com.hhhh.group.secwealth.mktdata.elastic.bean.DataSiteEntity;
import com.hhhh.group.secwealth.mktdata.elastic.bean.ProdInfoSeg;
import com.hhhh.group.secwealth.mktdata.elastic.bean.Version;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.service.DataHandlerCommonService;
import org.junit.Assert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.util.ReflectionUtils.findMethod;
import static org.springframework.util.ReflectionUtils.makeAccessible;

@ExtendWith(MockitoExtension.class)
public class UtTrstInstmProductProcessorTest {
    @Mock
    private DataHandlerCommonService dataHandlerCommonService;
    @InjectMocks
    private UtTrstInstmProductProcessor underTest;

    @Nested
    class WhenProcessing {
        private final String SITE_KEY = "HK_HBAP";
        @Mock
        private DataSiteEntity entity;
        @Mock
        private DataSiteEntity.DataFileInfo info;

        @Test
        void test_process() throws Exception {
            String path = "src/test/resources/files/HK_HBAP_WMDS_UT_20230530081158_35.xml";
            File file = new File(path);
            XMLInputFactory xmlif = XMLInputFactory.newInstance();
            XMLStreamReader xmlr = xmlif.createXMLStreamReader(new FileReader(file));
            Version version = new Version("20230530081158", "35");
            when(entity.getObjList()).thenReturn(Lists.newArrayList());
            File mockFile = mock(File.class);
            when(info.getDataFile()).thenReturn(mockFile);
            CustomizedEsIndexForProduct product = new CustomizedEsIndexForProduct();
            when(dataHandlerCommonService.handleCommonData(any(), any(), any(), any())).thenReturn(product);
            when(mockFile.getName()).thenReturn("HK_HBAP_WMDS_UT_20230530081158_35.xml");
            assertDoesNotThrow(() -> underTest.process(xmlr, entity, SITE_KEY, version, info));
        }

        @Test
        void test_process_exception() throws XMLStreamException {
            XMLStreamReader xmlr = mock(XMLStreamReader.class);
            Version version = new Version("20230530081158", "35");
            DataSiteEntity dataSiteEntity = mock(DataSiteEntity.class);
            when(xmlr.nextTag()).thenThrow(XMLStreamException.class);;
            File mockFile = mock(File.class);
            when(info.getDataFile()).thenReturn(mockFile);
            when(mockFile.getName()).thenReturn("HK_HBAP_WMDS_UT_20230530081158_35.xml");
            assertDoesNotThrow(() -> underTest.process(xmlr, dataSiteEntity, "HK_HBAP", version, info));
        }

    }

    @Test
    void test_method() throws Exception {
        Method method = findMethodWithAccess(underTest, "updateChanlAllowBuyAndSell",  CustomizedEsIndexForProduct.class, ProdInfoSeg.class);
        ProdInfoSeg prodInfoSeg = new ProdInfoSeg();
        List<ChanlAttrSeg> list = new ArrayList<>();
        ChanlAttrSeg chanlAttrSegAllowBuy = new ChanlAttrSeg();
        chanlAttrSegAllowBuy.setChanlCde("CMB_I");
        chanlAttrSegAllowBuy.setFieldCde("allowBuyProdInd");
        chanlAttrSegAllowBuy.setFieldValue("Y");
        list.add(chanlAttrSegAllowBuy);
        ChanlAttrSeg chanlAttrSegAllowSell = new ChanlAttrSeg();
        chanlAttrSegAllowSell.setChanlCde("CMB_I");
        chanlAttrSegAllowSell.setFieldCde("allowSellProdInd");
        chanlAttrSegAllowSell.setFieldValue("Y");
        list.add(chanlAttrSegAllowSell);
        ChanlAttrSeg chanlAttrSegAllowSwIn = new ChanlAttrSeg();
        chanlAttrSegAllowSwIn.setChanlCde("CMB_I");
        chanlAttrSegAllowSwIn.setFieldCde("allowSwInProdInd");
        chanlAttrSegAllowSwIn.setFieldValue("Y");
        list.add(chanlAttrSegAllowSwIn);
        ChanlAttrSeg chanlAttrSegAllowSwOut = new ChanlAttrSeg();
        chanlAttrSegAllowSwOut.setChanlCde("CMB_I");
        chanlAttrSegAllowSwOut.setFieldCde("allowSwOutProdInd");
        chanlAttrSegAllowSwOut.setFieldValue("Y");
        list.add(chanlAttrSegAllowSwOut);
        prodInfoSeg.setChanlAttrSeg(list);
        String.valueOf(method.invoke(underTest, new CustomizedEsIndexForProduct(),prodInfoSeg));
        Assert.assertNotNull(method);
    }
    public static Method findMethodWithAccess(Object target, String name, Class<?>... paramTypes) {
        Method method = findMethod(target.getClass(), name, paramTypes);
        makeAccessible(Objects.requireNonNull(method));
        return method;
    }
}
