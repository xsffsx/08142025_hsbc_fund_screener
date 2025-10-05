package com.dummy.wmd.wpc.graphql.calc;

import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;

@RunWith(MockitoJUnitRunner.class)
public class FieldCalculatorTests {

    @Mock
    private ReferenceDataService referenceDataService;

    @Test
    public void testSetReferenceDataService_givenReferenceDataService_DoesNotThrow() {
        try {
            MockFieldCalculatorImpl mockFieldCalculator= new MockFieldCalculatorImpl();
            mockFieldCalculator.setReferenceDataService(referenceDataService);
        } catch (Exception e) {
            Assert.fail();
        }
    }

     public class MockFieldCalculatorImpl implements FieldCalculator {

         @Override
         public Object calculate(Document prod) {
             return prod;
         }

         @Override
         public Collection<String> interestJsonPaths() {
             return null;
         }
     }
}
