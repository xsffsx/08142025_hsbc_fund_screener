package com.dummy.wmd.wpc.graphql.calc;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.jayway.jsonpath.JsonPath;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class AsetClassCdeFieldCalculatorTest {

    @InjectMocks
    private AsetClassCdeFieldCalculator asetClassCdeFieldCalculator;
    @Mock
    private ReferenceDataService referenceDataService;

    @Test
    public void testCalculate_givenProdWithoutAnyCode_returnsOther() {
        Document prod = new Document();
        Assert.assertEquals("OH", asetClassCdeFieldCalculator.calculate(prod));
    }

    @Test
    public void testCalculate_givenProd_returnsActualValue() {
        Document prod = CommonUtils.readResourceAsDocument("/files/ELI-50504681.json");
        Mockito.when(referenceDataService.getReferDataByFilter(any(Bson.class))).thenReturn(Collections.singletonList(new Document()));
        Object object = asetClassCdeFieldCalculator.calculate(new Document());
        String mktInvstCde = prod.get(Field.mktInvstCde, "");
        List<Object> list = JsonPath.read(prod, "undlAset[?(@.seqNum==1)].asetUndlCde");
        String asetUndlCde = (String) list.get(0);
        Assert.assertNotNull(mktInvstCde + asetUndlCde, asetClassCdeFieldCalculator.calculate(prod));
    }

    @Test
    public void testCalculate_givenProd_whenNotExistRefData_returnsOther() {
        Document prod = CommonUtils.readResourceAsDocument("/files/ELI-50504681.json");
        Mockito.when(referenceDataService.getReferDataByFilter(any(Bson.class))).thenReturn(Collections.emptyList());
        Assert.assertEquals("OH", asetClassCdeFieldCalculator.calculate(prod));
    }
}