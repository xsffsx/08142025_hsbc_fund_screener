package com.dummy.wmd.wpc.graphql.listener;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.function.Function;

import static com.dummy.wmd.wpc.graphql.constant.LegacyConstants.*;

@RunWith(MockitoJUnitRunner.class)
public class GrpNumFieldListenerTest {

    @InjectMocks
    private GrpNumFieldListener grpNumFieldListener;

    @Test
    public void testCalculate_givenELIDocument_returnsObject() {
        Document prod = CommonUtils.readResourceAsDocument("/files/ELI-50504681.json");

        Map<String, Object> eqtyLinkInvst = (Map<String, Object>) prod.get(Field.eqtyLinkInvst);
        List<Map> undlStock = (List) eqtyLinkInvst.get(Field.undlStock);

        eqtyLinkInvst.put("prodExtnlCatCde", "PPN");
        prod.put("prodSubtpCde", "PPN_TWSF");
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));

        undlStock.add(Collections.emptyMap());
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));

        eqtyLinkInvst.put("prodSubtpCde", "BEN_ELI");
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));

        prod.put("prodSubtpCde", "PPN_50");
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));

        eqtyLinkInvst.put("prodExtnlCatCde", null);
        prod.put("prodSubtpCde", null);
        prod.put(Field.grpNumAudioRecording, null);
        prod.put(Field.grpNumRiskDisclosure, null);
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNull(prod.get(Field.grpNumRiskDisclosure));

        prod.put("prodSubtpCde", "PPN_50");
        JsonPath.parse(prod).put(Field.eqtyLinkInvst, "cptlProtcPct", -1D);
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));

        undlStock.clear();
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNull(prod.get(Field.grpNumRiskDisclosure));

        prod.put("prodTypeCde", "OH");
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNull(prod.get(Field.grpNumRiskDisclosure));
    }

    @Test
    public void testCalculate_givenUTDocument_returnsObject() {
        Document prod = CommonUtils.readResourceAsDocument("/files/UT-1000047408.json");
        Document utTrstInstm = prod.get("utTrstInstm", Document.class);
        utTrstInstm.put("piFundInd", INDICATOR_YES);
        String origRecording = prod.getString(Field.grpNumAudioRecording);
        String origDisclosure = prod.getString(Field.grpNumRiskDisclosure);
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertEquals(origRecording, prod.get(Field.grpNumAudioRecording));
        Assert.assertEquals(origDisclosure, prod.get(Field.grpNumRiskDisclosure));

        utTrstInstm.put("piFundInd", INDICATOR_NO);
        prod.put("cmplxProdInd", null);
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));

        prod.put("cmplxProdInd", COMPLEX_PROD_IND);
        utTrstInstm.put("closeEndFundInd", INDICATOR_YES);
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));

        prod.put("cmplxProdInd", "D");
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));

        utTrstInstm.put("closeEndFundInd", INDICATOR_NO);
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));

        prod.put("cmplxProdInd", "C");
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));
    }

    @Test
    public void testCalculate_givenBONDDocument_returnsObject() {
        Document prod = CommonUtils.readResourceAsDocument("/files/BOND-286859053.json");
        prod.put("cmplxProdInd", COMPLEX_PROD_IND);
        prod.put("prodSubtpCde", CD);
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));

        prod.put("prodSubtpCde", null);
        prod.put("highYieldBondInd", INDICATOR_YES);
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));

        prod.put("cmplxProdInd", null);
        prod.put("highYieldBondInd", INDICATOR_YES);
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));

        prod.put("highYieldBondInd", INDICATOR_NO);
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));

        prod.put("cmplxProdInd", COMPLEX_PROD_IND);
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));
    }

    @Test
    public void testCalculate_givenDPSocument_returnsObject() {
        Document prod = new Document();
        prod.put("prodTypeCde", "DPS");
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));
    }

    @Test
    public void testCalculate_givenSIDcument_returnsObject() {
        Document prod = new Document();
        prod.put("prodTypeCde", "SID");
        prod.put("prodSubtpCde", CPI3);
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));

        prod.put("prodSubtpCde", CFFD);
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));

        prod.put("prodSubtpCde", CRA);
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));

        prod.put("prodSubtpCde", IRRA);
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertNotNull(prod.get(Field.grpNumAudioRecording));
        Assert.assertNotNull(prod.get(Field.grpNumRiskDisclosure));

        prod.put("prodSubtpCde", "abc");
        grpNumFieldListener.beforeValidation(null, prod);
        Assert.assertFalse(StringUtils.isBlank((String) prod.get(Field.grpNumAudioRecording)));
        Assert.assertTrue(StringUtils.isBlank((String) prod.get(Field.grpNumRiskDisclosure)));
    }

    @Test
    public void testGenEliGroupCdeRiskDisclosure_DCDC_ELI_RD09() {
        testGenEliGroupCdeRiskDisclosure(2, "DCDC ELI", "PPN_50", 0.0, "RD09");
    }


    @Test
    public void testGenEliGroupCdeRiskDisclosure_PPN_50_RD35() {
        testGenEliGroupCdeRiskDisclosure(2, "PPN", "PPN_50", 1.0, "RD19");
    }

    @Test
    public void testGenEliGroupCdeRiskDisclosure_PPN_50_RD19() {
        testGenEliGroupCdeRiskDisclosure(2, "PPN", "PPN_50", 0.0, "RD35");
    }

    @Test
    public void testGenEliGroupCdeRiskDisclosure_DCDC_ELI_RD08() {
        testGenEliGroupCdeRiskDisclosure(1, "DCDC ELI", "PPN_50", 0.0, "RD08");
    }

    @Test
    public void testGenEliGroupCdeRiskDisclosure_BENELI_RD59() {
        testGenEliGroupCdeRiskDisclosure(1, "DCDC ELI", "BEN_ELI", 0.0, "RD59");
    }

    @Test
    public void testGenEliGroupCdeRiskDisclosure_PPN_50_RD18() {
        testGenEliGroupCdeRiskDisclosure(1, "PPN", "PPN_50", 1.0, "RD18");
    }

    @Test
    public void testGenEliGroupCdeRiskDisclosure_PPN_50_RD34() {
        testGenEliGroupCdeRiskDisclosure(1, "PPN", "PPN_50", 0.0, "RD34");
    }

    @Test
    public void testGenEliGroupCdeRiskDisclosure_PPN_53() {
        testGenEliGroupCdeRiskDisclosure(1, "PPN", "PPN_53", 0.0, "RD59");
    }

    @Test
    public void testGenEliGroupCdeRiskDisclosure_null() {
        testGenEliGroupCdeRiskDisclosure(1, "PPN_FCN", "PPN_51", 0.0, null);
    }

    @Test
    public void testGenEliAudioRecording_DCDC_ELI_AR02() {
        testGenEliAudioRecording(2, "DCDC ELI", "PPN_50", 0.0, "AR02");
    }

    @Test
    public void testGenEliAudioRecording_BENELI_AR02() {
        testGenEliAudioRecording(2, "DCDC ELI", "PPN_50", 0.0, "AR02");
    }

    @Test
    public void testGenEliAudioRecording_PPN_50_AR25() {
        testGenEliAudioRecording(2, "PPN", "PPN_50", 1.0, "AR12");
    }

    @Test
    public void testGenEliAudioRecording_PPN_50_AR12_AR02() {
        testGenEliAudioRecording(2, "PPN", "PPN_50", 0.0, "AR25");
    }

    @Test
    public void testGenEliAudioRecording_DCDC_ELI_AR01_AR01() {
        testGenEliAudioRecording(1, "DCDC ELI", "PPN_50", 0.0, "AR01");
    }

    @Test
    public void testGenEliAudioRecording_BENELI_AR01() {
        testGenEliAudioRecording(1, "DCDC ELI", "BEN_ELI", 0.0, "AR49");
    }

    @Test
    public void testGenEliAudioRecording_PPN_50_AR11() {
        testGenEliAudioRecording(1, "PPN", "PPN_50", 1.0, "AR11");
    }

    @Test
    public void testGenEliAudioRecording_PPN_50_AR24() {
        testGenEliAudioRecording(1, "PPN", "PPN_50", 0.0, "AR24");
    }

    @Test
    public void testGenEliAudioRecording_null() {
        testGenEliAudioRecording(1, "PPN_FCN", "PPN_51", 0.0, null);
    }

    @Test
    public void testGenEliAudioRecording_PPN_53() {
        testGenEliAudioRecording(1, "PPN", "PPN_53", 0.0, "AR49");
    }


    @Test
    public void testGenEliAudioRecording_FX01() {
        testGenEliAudioRecording(1, "FX", "FXN_01", 0.0, "AR41");
    }

    @Test
    public void testGenEliAudioRecording_FX02() {
        testGenEliAudioRecording(2, "FX", "FXN_01", 0.0, "AR42");
    }


    @Test
    public void testGenEliGroupCdeRiskDisclosure_FX02() {
        testGenEliGroupCdeRiskDisclosure(2, "FX", "FXN_01", 0.0, "RD52");
    }

    @Test
    public void testGenEliGroupCdeRiskDisclosure_FX01() {
        testGenEliGroupCdeRiskDisclosure(1, "FX", "FXN_01", 0.0, "RD51");
    }

    @Test
    public void testConstructor() {
        Collection<String> allInterestJsonPaths = grpNumFieldListener.interestJsonPaths();
        Assert.assertNotNull(allInterestJsonPaths);
    }

    private void testGenEliAudioRecording(
            int size,
            String prodExtnlCatCde,
            String prodSubtpCde,
            double cptlProtcPct,
            String expectedResult) {
        testGenCode(size,
                prodExtnlCatCde,
                prodSubtpCde,
                cptlProtcPct,
                expectedResult,
                prod -> grpNumFieldListener.genEliAudioRecording(prod));
    }

    private void testGenEliGroupCdeRiskDisclosure(
            int size,
            String prodExtnlCatCde,
            String prodSubtpCde,
            double cptlProtcPct,
            String expectedResult) {
        testGenCode(size,
                prodExtnlCatCde,
                prodSubtpCde,
                cptlProtcPct,
                expectedResult,
                prod -> grpNumFieldListener.genEliGroupCdeRiskDisclosure(prod));
    }

    private void testGenCode(
            int size,
            String prodExtnlCatCde,
            String prodSubtpCde,
            double cptlProtcPct,
            String expectedResult,
            Function<Map<String, Object>, String> func) {
        Document prod = new Document();
        prod.put("prodSubtpCde", prodSubtpCde);
        Document eqtyLinkInvst = new Document();
        eqtyLinkInvst.put("prodExtnlCatCde", prodExtnlCatCde);
        eqtyLinkInvst.put("cptlProtcPct", cptlProtcPct);
        List<Map> undlStock = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            undlStock.add(new Document());
        }
        eqtyLinkInvst.put(Field.undlStock, undlStock);
        prod.put(Field.eqtyLinkInvst, eqtyLinkInvst);
        String actualResult = func.apply(prod);
        Assert.assertEquals(actualResult, expectedResult);
    }
}
