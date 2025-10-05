package com.dummy.wmd.wpc.graphql.validator;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static graphql.Assert.assertNotNull;

public class ConditionalRulesTests {

    private ConditionalRules conditionalRulesUnderTest;

    @Before
    public void setUp() throws Exception {
        conditionalRulesUnderTest = new ConditionalRules();
    }
    @Test
    public void testConditionalRules(){
        conditionalRulesUnderTest.setCondition("condition");
        conditionalRulesUnderTest.setValidators(new ArrayList<>());
        assertNotNull(conditionalRulesUnderTest.getCondition());
        assertNotNull(conditionalRulesUnderTest.getValidators());
        assertNotNull(conditionalRulesUnderTest.toString());
    }
}
