package com.dummy.wmd.wpc.graphql.validator;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static graphql.Assert.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceDataValidatorTest {

    @Mock
    private ReferenceDataService referenceDataService;
    @Mock
    private ValidationContext context;
    private ReferenceDataValidator referenceDataValidator;

    @Before
    public void setUp() throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put(Field.cdvTypeCde,"${cdvTypeCde}");
        param.put(Field.cdvParntTypeCde,"cdvParntTypeCde");
        param.put(Field.cdvParntCde,"cdvParntCde");
        referenceDataValidator = new ReferenceDataValidator(param, referenceDataService);
    }

    @Test
    public void testValidate_givenEmptyString_returnsNull() {
        // Run the test
        try {
            referenceDataValidator.validate("", context);
        }catch (Exception e){
            Assert.fail();
        }
    }
    @Test
    public void testValidate_givenString_returnsNull() {
        Mockito.when(context.getCtryRecCde()).thenReturn("HK");
        Mockito.when(context.getGrpMembrRecCde()).thenReturn("HASE");
        Mockito.when(context.resolveVariable("${cdvTypeCde}")).thenReturn("cdvTypeCde");
        // Run the test
        try {
            referenceDataValidator.validate("value", context);
        }catch (Exception e){
            Assert.fail();
        }

    }

    @Test
    public void testSupport_givenNull_returnsFalse() {
        assertThat(referenceDataValidator.support(null)).isFalse();
    }
    @Test
    public void testSupport_givenObject_returnsFalse() {
        assertThat(referenceDataValidator.support(new Date())).isFalse();
    }
    @Test
    public void testSupport_givenObject_returnsTrue() {
        assertThat(referenceDataValidator.support("value")).isTrue();
    }

    @Test
    public void testGetName() {
        assertThat(referenceDataValidator.getName()).isEqualTo("referenceData");
    }

    @Test
    public void testGetDefaultMessage() {

        assertNotNull(referenceDataValidator.getDefaultMessage("value"));
    }

    @Test
    public void testToString() {
        assertNotNull(referenceDataValidator.toString());
    }
}
