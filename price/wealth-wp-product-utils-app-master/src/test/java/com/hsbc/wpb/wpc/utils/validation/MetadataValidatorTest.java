package com.dummy.wpb.wpc.utils.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)

public class MetadataValidatorTest {

    @Test
    public void testValidate_AllFieldsValid_ReturnsNoErrors() {
        List<Map<String, Object>> metadataList =  new LinkedList(
                Arrays.asList(new HashMap() {{
                    put("_id", 1);
                    put("code", "a");
                    put("table", "a");
                    put("fieldName", "a");
                    put("fieldScale", 2);
                    put("fieldPrecision", 1);
                    put("fieldType", 1);
                    put("fieldTypeName", "a");
                    put("fieldNullable", 1);
                    put("attrName", "a");
                    put("jsonPath", "a");
                    put("parent", "[ROOT]");
                    put("instrument", "a");
                    put("classification", "a");
                    put("mandatory", "a");
                    put("dataUsage", "a");
                    put("logicalEntityName", "a");
                    put("businessName", "a");
                    put("businessDefinition", "a");
                    put("graphQLType", "a");
                }}));
        MetadataValidator validator = new MetadataValidator(metadataList);
        List<Error> result = validator.validate();
        assertThat(result).isEmpty();
    }
    @Test
    public void testValidate_listField_ReturnsErrors() {
     List<Map<String, Object>> metadataList =  new LinkedList(
                    Arrays.asList(new HashMap() {{
                        put("_id", "DEBT_INSTM_YIELD_HIST");
                        put("table", "DEBT_INSTM_YIELD_HIST");
                        put("attrName", "yieldHist");
                        put("jsonPath", "debtInstm.yieldHist");
                        put("parent", "debtInstm");
                        put("businessName", "Yield History");
                        put("businessDefinition", "Debt Instrument Yield History");
                        put("graphQLType", "[DebtInstmYieldHistType]");
                    }}));
            MetadataValidator validator = new MetadataValidator(metadataList);
            List<Error> result = validator.validate();
            assertThat(result).isNotEmpty();
            assertThat(result.get(0).getMessage()).contains("attrName should be end with [*] for an array field");
        }

        @Test
        public void testValidate_duplicatedCase_ReturnsErrors() {
         List<Map<String, Object>> metadataList =  new LinkedList(
                        Arrays.asList(new HashMap() {{
                            put("_id", "DEBT_INSTM_YIELD_HIST");
                            put("table", "DEBT_INSTM_YIELD_HIST");
                            put("attrName", "yieldHist[*]");
                            put("jsonPath", "debtInstm.yieldHist[*]");
                            put("parent", "debtInstm");
                            put("businessName", "Yield History");
                            put("businessDefinition", "Debt Instrument Yield History");
                            put("graphQLType", "[DebtInstmYieldHistType]");
                        }},new HashMap() {{
                            put("_id", "DEBT_INSTM_YIELD_HIST");
                            put("table", "DEBT_INSTM_YIELD_HIST");
                            put("attrName", "yieldHist[*]");
                            put("jsonPath", "debtInstm.yieldHist[*]");
                            put("parent", "debtInstm");
                            put("businessName", "Yield History");
                            put("businessDefinition", "Debt Instrument Yield History");
                            put("graphQLType", "[DebtInstmYieldHistType]");
                        }}));
                MetadataValidator validator = new MetadataValidator(metadataList);
                List<Error> result = validator.validate();
                assertThat(result).isNotEmpty();
                assertThat(result.get(0).getMessage()).contains("duplicated _id field");
            }

    @Test
    public void testValidate_jsonPath_ReturnsErrors() {
     List<Map<String, Object>> metadataList =  new LinkedList(
                    Arrays.asList(new HashMap() {{
                        put("_id", "DEBT_INSTM_YIELD_HIST");
                        put("table", "DEBT_INSTM_YIELD_HIST");
                        put("attrName", "yieldHist[*]");
                        put("jsonPath", "debtInstm.yieldHist[*]");
                        put("parent", "a");
                        put("businessName", "Yield History");
                        put("businessDefinition", "Debt Instrument Yield History");
                        put("graphQLType", "[DebtInstmYieldHistType]");
                    }}));
            MetadataValidator validator = new MetadataValidator(metadataList);
            List<Error> result = validator.validate();
            assertThat(result).isNotEmpty();
            assertThat(result.get(0).getMessage()).contains("wrong jsonPath, should be a.yieldHist[*]");
        }
    }
