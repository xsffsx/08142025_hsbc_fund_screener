package com.hhhh.group.secwealth.mktdata.common.service;

import com.hhhh.group.secwealth.mktdata.common.service.impl.LabciProtalSearchService;
import com.hhhh.group.secwealth.mktdata.predsrch.pres.beans.MultiPredSrchRequest;
import com.hhhh.group.secwealth.mktdata.predsrch.pres.beans.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.predsrch.pres.beans.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.labci.Header;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.labci.StocksList;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.labci.SymbolSearchBoby;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.labci.SymbolSearchEnvelop;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.properties.LabciProtalProperties;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.LabciProtalService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@ActiveProfiles({"test", "hase_stma_api_support-test", "uat_hase_stma_eqty-test"})
public class LabciProtalSearchServiceTest {

    @Mock
    private HttpClientHelper httpClientHelper;

    @Mock
    private LabciProtalProperties labciProtalProperties;

    @Mock
    private LabciProtalService labciProtalService;


    @InjectMocks
    private LabciProtalSearchService labciProtalSearchService;


    private  Header header = new Header();

    private  SymbolSearchEnvelop symbolSearchEnvelop = new SymbolSearchEnvelop();


    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        String httpResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<envelop>\n" +
                "\t<header>\n" +
                "\t\t<msgid>1</msgid>\n" +
                "\t\t<marketid>Tom</marketid>\n" +
                "\t\t<responsecode>Cruise</responsecode>\n" +
                "\t\t<errormsg>001</errormsg>\n" +
                "\t</header>\n" +
                "         <body>\n" +
                "\t\t<stockslist>\n" +
                "               \n" +
                "                </stockslist>\n" +
                "\t\t<totalrecordno>Tom</totalrecordno>\n" +
                "\t\t<lastupdateddate>Cruise</lastupdateddate>\n" +
                "\t\t<lastupdatedtime>001</lastupdatedtime>\n" +
                "                <timezone>001</timezone>\n" +
                "\t</body>\n" +
                "      \n" +
                "</envelop>";
        when(httpClientHelper.doGet(any(String.class), any(String.class), any(ArrayList.class), any(HashMap.class))).thenReturn(httpResult);


        header.setMarketid("1");
        header.setMsgid("2");
        header.setResponsecode("100");
        SymbolSearchBoby symbolSearchBoby = new SymbolSearchBoby();

        symbolSearchEnvelop.setHeader(header);
        List<StocksList> stocksLists = new ArrayList() {{
            StocksList stocksList = new StocksList();
            stocksList.setStockname("Stockname");
            stocksList.setStocksymbol("Stocksymbol");
            stocksList.setExchange("Exchange");
            add(stocksList);
        }};
        symbolSearchBoby.setLastupdateddate("Lastupdateddate");
        symbolSearchBoby.setLastupdatedtime("Lastupdatedtime");
        symbolSearchBoby.setTotalrecordno("Totalrecordno");
        symbolSearchBoby.setTimezone("Timezone");
        symbolSearchBoby.setStockslist(stocksLists);
        symbolSearchEnvelop.setBody(symbolSearchBoby);


    }

    @Test
    public void testPredsrch() throws Exception {
        PredSrchRequest mock = mock(PredSrchRequest.class);
        Assert.assertNotNull(labciProtalSearchService.predsrch(mock));
    }


    @Test
    public void testConverPredRespnose() throws Exception {
        PredSrchRequest predSrchRequest = new PredSrchRequest();
        Assert.assertNotNull(labciProtalSearchService.converPredRespnose(symbolSearchEnvelop,predSrchRequest));
    }


    @Test
    public void testMultiPredsrch() throws Exception {
        MultiPredSrchRequest multiPredSrchRequest = new MultiPredSrchRequest();
        multiPredSrchRequest.setKeyword(new String[]{"KEY","WORD"});
        multiPredSrchRequest.setMarket("market");
        multiPredSrchRequest.setTopNum("topnum");
        Assert.assertNotNull(labciProtalSearchService.multiPredsrch(multiPredSrchRequest));
    }

    @Test
    public void testMultiPredsrchWithCache() throws Exception {
        MultiPredSrchRequest multiPredSrchRequest = new MultiPredSrchRequest();
        multiPredSrchRequest.setKeyword(new String[]{"KEY","WORD"});
        multiPredSrchRequest.setMarket("market");
        multiPredSrchRequest.setTopNum("topnum");
        Assert.assertNotNull(labciProtalSearchService.multiPredsrchWithCache(multiPredSrchRequest));
    }

    @Test
    public void testConverMutiPredRespnoseh() {
        Boolean flag=true;
        try {
            labciProtalSearchService.converMutiPredRespnose(new ArrayList<PredSrchResponse>(),symbolSearchEnvelop);
        } catch (Exception e) {
            flag=false;
        }
        Assert.assertTrue(flag);
    }

}
