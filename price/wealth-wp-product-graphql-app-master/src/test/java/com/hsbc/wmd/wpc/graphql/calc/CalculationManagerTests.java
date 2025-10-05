package com.dummy.wmd.wpc.graphql.calc;

import com.dummy.wmd.wpc.graphql.service.MetadataService;
import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class CalculationManagerTests {

    @InjectMocks
    private CalculationManager calculationManager;
    @Mock
    private ReferenceDataService referenceDataService;
    @Mock
    private MetadataService metadataService;

    @Before
    public void setUp() {
        calculationManager = new CalculationManager(metadataService);
        ReflectionTestUtils.setField(calculationManager,"referenceDataService", referenceDataService);
    }

    private Document prepareData(boolean flag) {
        Document metadata = CommonUtils.readResourceAsDocument("/files/ASET-CLASS-CDE-metadata.json");
        if(flag) {
            metadata.replace("jsonPath", ".");
        }
        List<Document> metadatas = new LinkedList<>();
        metadatas.add(metadata);
        ReflectionTestUtils.setField(calculationManager, "calculatedFields", metadatas);
        Document prod = CommonUtils.readResourceAsDocument("/files/ELI-50504681.json");
        Mockito.when(referenceDataService.getReferDataByFilter(any(Bson.class))).thenReturn(new ArrayList<>());
        return prod;
    }

    @Test
    public void testUpdateCalculatedFields_givenDocument_DoesNotThrow() {
        try {
            Document prod = prepareData(false);
            calculationManager.updateCalculatedFields(prod);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateCalculatedFields_givenDocument_throwException() {
        Document prod = prepareData(true);
        calculationManager.updateCalculatedFields(prod);
    }

    @Test(expected = InvocationTargetException.class)
    public void testNewCalculatorInstance_givenClassName_throwException() throws Exception {
        Method newCalculatorInstance = calculationManager.getClass().getDeclaredMethod("newCalculatorInstance", String.class);
        newCalculatorInstance.setAccessible(true);
        newCalculatorInstance.invoke(calculationManager, "ClassName");
    }
}
