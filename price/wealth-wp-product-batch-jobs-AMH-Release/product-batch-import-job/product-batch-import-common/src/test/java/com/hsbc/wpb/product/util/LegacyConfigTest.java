package com.dummy.wpb.product.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.model.graphql.LegacyConfigInfo;
import com.dummy.wpb.product.service.GraphQLService;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
public class LegacyConfigTest {

    private GraphQLService graphQLService = mock(GraphQLService.class);

    private LegacyConfig legacyConfig = new LegacyConfig(graphQLService);

    private ObjectMapper mapper = new ObjectMapper();


    @Test
    public void testInit() throws Exception {
        List<LegacyConfigInfo> configList = mapper.readValue(new File(new ClassPathResource("/test/legacyConfig.json").getFile().getAbsolutePath()),new TypeReference<List<LegacyConfigInfo>>() {
        });
        Mockito.when(graphQLService.executeRequest(Mockito.any(GraphQLRequest.class),Mockito.<TypeReference<List<LegacyConfigInfo>>>any()))
                .thenReturn(configList);
        legacyConfig.init();
        Assert.assertEquals("[W]", LegacyConfig.getList("PRODKEY.FILTER_PROD_STATUS_CODE.IGNORE_TYPE").toString());
        Assert.assertEquals(60, LegacyConfig.getInt("wpc.webservice.data.cutoff.delay"));
    }

    private String pro = "GB.HBEU.DEFAULT.TIMEZONE=Europe/London\neng=EN\nFinDoc.NotFndUrlPll=\nPRODKEY.PRIMARY=P|T|R\nPRODKEY.UNIQUE=\nGB.HBEU.RBP.TIMEZONE=Europe/London\ndefault_lang_cde=en_GB\nPRODKEY.PRODCDE=W\nGB.HBEU.GHFICUTAS.TIMEZONE=Europe/London\nwpc.webservice.data.cutoff.delay=60\nPRODKEY.FILTER_PROD_STATUS_CODE.IGNORE_TYPE=W\nattrPair={\"prodNavPrcAmt\":[\"ccyProdMktPrcCde\",\"prcEffDt\"],\"prodBidPrcAmt\":[\"ccyProdMktPrcCde\",\"prcEffDt\"],\"prodOffrPrcAmt\":[\"ccyProdMktPrcCde\",\"prcEffDt\"],\"prodMktPrcAmt\":[\"ccyProdMktPrcCde\",\"prcEffDt\"],\"prodMktPricePrevAmt\":[\"ccyProdMktPrcCde\"],\"invstInitMinAmt\":[\"ccyInvstCde\"],\"invstInitMaxAmt\":[\"ccyInvstCde\"],\"asetVoltlClass_Cde\":[\"asetVoltlClass_WghtPct\"],\"asetVoltlClass_WghtPct\":[\"asetVoltlClass_Cde\"],\"asetGeoLocAlloc_Cde\":[\"asetGeoLocAlloc_WghtPct\"],\"asetGeoLocAlloc_WghtPct\":[\"asetGeoLocAlloc_Cde\"],\"asetSicAlloc_Cde\":[\"asetSicAlloc_WghtPct\"],\"asetSicAlloc_WghtPct\":[\"asetSicAlloc_Cde\"],\"finDoc_DocURL_*\":[\"finDoc_LangCde_*\"],\"finDoc_LangCde_*\":[\"finDoc_DocURL_*\"],\"invstIncrmMinAmt\":[\"ccyInvstCde\"],\"rdmMinAmt\":[\"ccyInvstCde\"],\"retnMinAmt\":[\"ccyInvstCde\"],\"wrapCeilAmt\":[\"ccyInvstCde\"],\"utPrcProdLnchAmt\":[\"ccyProdMktPrcCde\"],\"fundAmt\":[\"ccyProdMktPrcCde\"],\"fundRtainMinAmt\":[\"ccyInvstCde\"],\"invstMipMinAmt\":[\"ccyInvstCde\"],\"invstMipIncrmMinAmt\":[\"ccyInvstCde\"],\"rdmMipMinAmt\":[\"ccyInvstCde\"],\"fundMipRtainMinAmt\":[\"ccyInvstCde\"],\"fundSwInMinAmt\":[\"ccyInvstCde\"],\"fundSwOutMinAmt\":[\"ccyInvstCde\"],\"fundSwOutRtainMinAmt\":[\"ccyInvstCde\"],\"fundSwOutMaxAmt\":[\"ccyInvstCde\"],\"tranMaxAmt\":[\"ccyInvstCde\"],\"IntIndAccrAmt\":[\"ccyInvstCde\"],\"InvstIncMinAmt\":[\"ccyInvstCde\"],\"invstSoldLestAmt\":[\"ccyInvstCde\"],\"invstIncrmSoldAmt\":[\"ccyInvstCde\"],\"prodClsBidPrcAmt\":[\"ccyProdMktPrcCde\"],\"prodClsOffrPrcAmt\":[\"ccyProdMktPrcCde\"],\"yieldToCallBidPct\":[\"yieldEffDt\"],\"YieldToMturBidPct\":[\"yieldEffDt\"],\"yieldToCallOfferPct\":[\"yieldEffDt\"],\"yieldToMturOfferText\":[\"yieldEffDt\"],\"yieldBidClosePct\":[\"yieldEffDt\"],\"yieldOfferClosePct\":[\"yieldEffDt\"],\"yieldDt\":[\"yieldEffDt\"],\"yieldBidPct\":[\"yieldEffDt\"],\"yieldOfferPct\":[\"yieldEffDt\"],\"prodPrcChngAmt\":[\"ccyProdMktPrcCde\"],\"prodOpenPrcAmt\":[\"ccyProdMktPrcCde\"],\"prodTdyHighPrcAmt\":[\"ccyProdMktPrcCde\"],\"prodTdyLowPrcAmt\":[\"ccyProdMktPrcCde\"],\"prodTrnvrAmt\":[\"ccyProdMktPrcCde\"],\"prodPrc52WeekMaxAmt\":[\"ccyProdMktPrcCde\"],\"prodPrc52WeekMinAmt\":[\"ccyProdMktPrcCde\"],\"earnPerShareAnnlAmt\":[\"ccyProdMktPrcCde\"],\"prodExerPrcAmt\":[\"ccyProdMktPrcCde\"],\"loanBdgtProdIPOAmt\":[\"ccyProdMktPrcCde\"],\"loanProdIPOTtlAmt\":[\"ccyProdMktPrcCde\"],\"denAmt\":[\"ccyInvstCde\"],\"trdMinAmt\":[\"ccyInvstCde\"],\"undlStock_ProdStrkPrcAmt\":[\"undlStock_CcyInstmUndlPrcCde\"],\"undlStock_ProdStrkCallPrcAmt\":[\"undlStock_CcyInstmUndlPrcCde\"],\"undlStock_ProdStrkPutPrcAmt\":[\"undlStock_CcyInstmUndlPrcCde\"],\"undlStock_ProdClosePrcAmt\":[\"undlStock_CcyInstmUndlPrcCde\"],\"undlStock_ProdCloseLowPrcAmt\":[\"undlStock_CcyInstmUndlPrcCde\"],\"undlStock_ProdCloseUpprPrcAmt\":[\"undlStock_CcyInstmUndlPrcCde\"],\"undlStock_ProdClosePutPrcAmt\":[\"undlStock_CcyInstmUndlPrcCde\"],\"undlStock_ProdCloseCallPrcAmt\":[\"undlStock_CcyInstmUndlPrcCde\"],\"undlStock_ProdExerPrcAmt\":[\"undlStock_CcyInstmUndlPrcCde\"],\"undlStock_ProdBrkEvnPrcAmt\":[\"undlStock_CcyInstmUndlPrcCde\"],\"undlStock_ProdBrkEvnLowPrcAmt\":[\"undlStock_CcyInstmUndlPrcCde\"],\"undlStock_ProdBrkEvnUpprPrcAmt\":[\"undlStock_CcyInstmUndlPrcCde\"],\"undlStock_ProdBrkEvnPutPrcAmt\":[\"undlStock_CcyInstmUndlPrcCde\"],\"undlStock_ProdBrkEvnCallPrcAmt\":[\"undlStock_CcyInstmUndlPrcCde\"],\"undlStock_ProdKnockInPrcAmt\":[\"undlStock_CcyInstmUndlPrcCde\"],\"rtrnIntrm_PaidDt\":[\"rtrnIntrm_Pct\"],\"rtrnIntrm_Pct\":[\"rtrnIntrm_PaidDt\"]}\nGB.HBEU.OHD.TIMEZONE=Europe/London\nFinDoc.DocType.FACTSHEET.DEFAULT=USERDOC-1\nPRODKEY.ALL=I\nTERM_REMAIN_DAY_CNT.DIVIDER=0\nen_us=ENG\nPRODKEY.OTHER=M\nsortingAttributeMapping={\"invstMinHoriz\":\"ext.invstMinHoriz\"}\nextAttrPair={\"OHI_invstInitMinAmt\":[\"ccyInvstCde\"],\"OHB_invstInitMinAmt\":[\"ccyInvstCde\"],\"MOB_invstInitMinAmt\":[\"ccyInvstCde\"]}\nFinDoc.NotFndUrl=\nFinDoc.NotFndUrlSll=\npl=EN\nPRODKEY.FILTER_PROD_STATUS_CODE.DEFAULT=D,P\nsl=EN\nGB.HBEU.IDS.TIMEZONE=Europe/London\nGB.HBEU.ISM10.TIMEZONE=Europe/London";

    @Test
    public void testRegularExpression() throws IOException {
        Properties properties = new Properties();
        properties.load(new StringReader(pro));
        String value1 = properties.getProperty("GB.HBEU.DEFAULT.TIMEZONE");
        Assert.assertEquals("[Europe/London]", Arrays.asList(value1.split("\\s*[|,]\\s*")).toString());
        Assert.assertEquals("[Europe/London]", Arrays.asList(value1.split("[|,]")).toString());
        Assert.assertEquals("[Europe/London]", Arrays.asList(value1.split("\\s*(?:\\||,)\\s*")).toString());
        String value2 = properties.getProperty("PRODKEY.PRIMARY");
        Assert.assertEquals("[P, T, R]", Arrays.asList(value2.split("\\s*[|,]\\s*")).toString());
        Assert.assertEquals("[P, T, R]", Arrays.asList(value2.split("[|,]")).toString());
        Assert.assertEquals("[P, T, R]", Arrays.asList(value2.split("\\s*(?:\\||,)\\s*")).toString());

    }
    @Test
    public void testInit_exception() {
        try {
            Mockito.when(graphQLService.executeRequest(Mockito.any(GraphQLRequest.class),Mockito.<TypeReference<List<LegacyConfigInfo>>>any()))
                    .thenThrow(IllegalArgumentException.class);
            legacyConfig.init();
        } catch (Exception e) {
            Assert.fail("An unexpected exception arised.");
        }
    }
}