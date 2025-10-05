package com.dummy.wmd.wpc.graphql.calc;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.Operation;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import com.dummy.wmd.wpc.graphql.service.MetadataService;
import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.mockito.Mockito.any;

@RunWith(MockitoJUnitRunner.class)
public class CaculationManagerTest {

    @Mock
    private ReferenceDataService referenceDataService;
    @Mock
    private MongoTemplate mongoTemplate;

    private List<Document> calculatedFields;

    @Mock
    private MetadataService metadataService;
    @InjectMocks
    private CalculationManager calculationManager;

    @Before
    public void setUp() {
        calculatedFields = new ArrayList<>();
        calculatedFields.add(
                new Document()
                        .append(Field.calculatedBy, new Document().append(Field.className, "com.dummy.wmd.wpc.graphql.calc.AsetClassCdeFieldCalculator"))
                        .append(Field.jsonPath, "asetClassCde")
        );
        calculatedFields.add(
                new Document()
                        .append(Field.calculatedBy, new Document().append(Field.className, "com.dummy.wmd.wpc.graphql.calc.AsetVoltlClassMajrPrntCdeFieldCalculator"))
                        .append(Field.jsonPath, "asetVoltlClassMajrPrntCde")
        );

        calculationManager = new CalculationManager(metadataService);
        ReflectionTestUtils.setField(calculationManager, "referenceDataService", referenceDataService);
        ReflectionTestUtils.setField(calculationManager, "mongoTemplate", mongoTemplate);
        ReflectionTestUtils.setField(calculationManager, "calculatedFields", calculatedFields);
    }

    @Test
    public void testUpdateCalculatedFields_given_prod() {
         MockedConstruction<AsetClassCdeFieldCalculator> classCdeFieldCalculatorMockedConstruction =
                Mockito.mockConstruction(AsetClassCdeFieldCalculator.class, (calc, context) -> {
                    Mockito.doCallRealMethod().when(calc).interestJsonPaths();
                    Mockito.doReturn("CT").when(calc).calculate(any());
                });
        MockedConstruction<AsetVoltlClassMajrPrntCdeFieldCalculator> asetVoltlClassMajrPrntCdeFieldCalculatorMockedConstruction =
                Mockito.mockConstruction(AsetVoltlClassMajrPrntCdeFieldCalculator.class, (calc, context) -> {
                    Mockito.doCallRealMethod().when(calc).interestJsonPaths();
                    Mockito.doReturn("CE").when(calc).calculate(any());
                });
        try {
            Document newProd = new Document(Field._id,123456L);
            calculationManager.updateCalculatedFields(newProd);

            Mockito.verify(asetVoltlClassMajrPrntCdeFieldCalculatorMockedConstruction.constructed().get(0), Mockito.times(1)).calculate(any());
            Mockito.verify(classCdeFieldCalculatorMockedConstruction.constructed().get(0), Mockito.times(1)).calculate(any());
        } finally {
            classCdeFieldCalculatorMockedConstruction.close();
            asetVoltlClassMajrPrntCdeFieldCalculatorMockedConstruction.close();
        }
    }


    @Test
    public void testUpdateCalculatedFields_given_oldprodAndNewprodAndOperation() {
        MockedConstruction<AsetClassCdeFieldCalculator> classCdeFieldCalculatorMockedConstruction =
                Mockito.mockConstruction(AsetClassCdeFieldCalculator.class, (calc, context) -> {
                    Mockito.doCallRealMethod().when(calc).interestJsonPaths();
                    Mockito.doReturn("CT").when(calc).calculate(any());
                });
        MockedConstruction<AsetVoltlClassMajrPrntCdeFieldCalculator> asetVoltlClassMajrPrntCdeFieldCalculatorMockedConstruction =
                Mockito.mockConstruction(AsetVoltlClassMajrPrntCdeFieldCalculator.class, (calc, context) -> {
                    Mockito.doCallRealMethod().when(calc).interestJsonPaths();
                    Mockito.doReturn("CE").when(calc).calculate(any());
                });
        try {
            Document newProd = new Document(Field._id,123456L);
            Document oldProd = new Document(newProd);
            List<OperationInput> operations = Collections.singletonList(new OperationInput(Operation.put, Field.asetVoltlClass, new ArrayList<>()));
            calculationManager.updateCalculatedFields(oldProd, newProd, operations);

            Mockito.verify(asetVoltlClassMajrPrntCdeFieldCalculatorMockedConstruction.constructed().get(0), Mockito.times(1)).calculate(any());
            Mockito.verify(classCdeFieldCalculatorMockedConstruction.constructed().get(0), Mockito.times(0)).calculate(any());
        } finally {
            classCdeFieldCalculatorMockedConstruction.close();
            asetVoltlClassMajrPrntCdeFieldCalculatorMockedConstruction.close();
        }
    }


}
