package com.dummy.wmd.wpc.graphql.calc;

import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
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

import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class AsetVoltlClassMajrPrntCdeFieldCalculatorTests {

    @InjectMocks
    private AsetVoltlClassMajrPrntCdeFieldCalculator asetVoltlClassMajrPrntCdeFieldCalculator;
    @Mock
    private ReferenceDataService referenceDataService;

    @Test
    public void testCalculate_givenDocument_returnsObject() {
        Document prod = CommonUtils.readResourceAsDocument("/files/UT-40004996.json");
        Document reference_data = CommonUtils.readResourceAsDocument("/files/PASFUNDCATLV2-reference-data.json");
        reference_data.replace("cdvCde", "AUS_EQTY");
        List<Document> reference_datas = new LinkedList<>();
        reference_datas.add(reference_data);
        Mockito.when(referenceDataService.getReferDataByFilter(any(Bson.class))).thenReturn(reference_datas);
        Object object = asetVoltlClassMajrPrntCdeFieldCalculator.calculate(prod);
        Assert.assertNotNull(object);
        prod.replace("asetVoltlClass", null);
        Object object2 = asetVoltlClassMajrPrntCdeFieldCalculator.calculate(prod);
        Assert.assertNull(object2);
    }

    @Test
    public void testSetReferenceDataService_givenReferenceDataService_DoesNotThrow() {
        try {
            asetVoltlClassMajrPrntCdeFieldCalculator.setReferenceDataService(referenceDataService);
        } catch (Exception e) {
            Assert.fail();
        }
    }
}

