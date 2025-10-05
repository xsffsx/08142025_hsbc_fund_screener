package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class PriceCompareServiceTests {

    @InjectMocks
    private PriceCompareService priceCompareService;
    @Mock
    private ProductService productService;

    @Test
    public void testUpdateStlPrcCmparFromWps_givenDocument_returnsDocument() {
        Document document = CommonUtils.readResourceAsDocument("/files/UT-1000047408.json");
        List<Map<String, Object>> prcCmparList = (List<Map<String, Object>>) document.get(Field.prcCmpar);
        Map<String, Object> map = prcCmparList.get(0);
        map.replace("prodPrcTypeCde", "BID");
        map.replace("rptProdPrcTypeCde", "STL");
        Document document2 = CommonUtils.readResourceAsDocument("/files/UT-1000047408.json");
        document2.replace("prodBidPrcAmt",null);
        document2.replace("prodOffrPrcAmt",333.4);
        document2.replace("prodNavPrcAmt",231.4);
        document2.replace("prodMktPrcAmt",281.4);
        Mockito.when(productService.findFirst(any(Bson.class))).thenReturn(document);
        Document doc = priceCompareService.updateStlPrcCmparFromWps(document2);
        Assert.assertNotNull(doc);
    }
}
