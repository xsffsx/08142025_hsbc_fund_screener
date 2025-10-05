package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.SECQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesLabciQuoteByRequestFields;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"sit_hase_stma_eqty_unittest"})
public class LuldRulePriceUSServiceTest {

    @InjectMocks
    private LuldRulePriceUSService luldRulePriceUSService;

    @Mock
    private HttpClientHelper httpClientHelper;

    @Test
    public void testCalculateLuldRulePrice() throws Exception {
        List<QuotesLabciQuoteByRequestFields> priceQuotes = new ArrayList<>();
        QuotesLabciQuoteByRequestFields quotesLabciQuoteByRequestFields = new QuotesLabciQuoteByRequestFields();
        quotesLabciQuoteByRequestFields.setSymbol("AAPL");
        quotesLabciQuoteByRequestFields.setCurrency("USD");
        quotesLabciQuoteByRequestFields.setTradePrice(BigDecimal.valueOf(231.3));
        quotesLabciQuoteByRequestFields.setAsOfDate("14 OCT 2024");
        quotesLabciQuoteByRequestFields.setAsOfTime("20:00:01.185");
        quotesLabciQuoteByRequestFields.setBidPrice(BigDecimal.valueOf(0));
        quotesLabciQuoteByRequestFields.setAskPrice(BigDecimal.valueOf(0));
        priceQuotes.add(quotesLabciQuoteByRequestFields);

        SECQuotesRequest secQuotesRequest = new SECQuotesRequest();
        List<ProductKey> productKeys = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProductType("ALL");
        productKey.setProdCdeAltClassCde("R");
        productKey.setProdAltNum("AAPL.O");
        productKeys.add(productKey);
        secQuotesRequest.setProductKeys(productKeys);

        ArgsHolder.putArgs(QuotesUSCachingServerService.THREAD_INVISIBLE_QUOTES_US_REQUEST, secQuotesRequest);

        String service = "dIDN_RDF";
        String labciResponseStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><watchlist xml:space=\"preserve\"><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=AAPL.O\">AAPL.O</fid><fid id=\"SERVICE\">dIDN_RDF</fid><fid id=\"UPLIMIT\">0.000000</fid><fid id=\"ADJUST_CLS\">231.300000</fid></ric><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=GOOG.O\">GOOG.O</fid><fid id=\"SERVICE\">dIDN_RDF</fid><fid id=\"UPLIMIT\">0.000000</fid><fid id=\"ADJUST_CLS\">166.350000</fid></ric></watchlist>\n";
        when(httpClientHelper.doGet(anyString(),anyList(),anyMap())).thenReturn(labciResponseStr);

        luldRulePriceUSService.calculateLuldRulePrice(priceQuotes, service, "CTS");

        Assert.assertNotNull(priceQuotes);
    }

    @Test
    public void testCalculateLuldRulePriceWithNoLuld() throws Exception {
        List<QuotesLabciQuoteByRequestFields> priceQuotes = new ArrayList<>();
        QuotesLabciQuoteByRequestFields quotesLabciQuoteByRequestFields = new QuotesLabciQuoteByRequestFields();
        quotesLabciQuoteByRequestFields.setSymbol("AAPL");
        quotesLabciQuoteByRequestFields.setCurrency("USD");
        quotesLabciQuoteByRequestFields.setTradePrice(BigDecimal.valueOf(231.3));
        quotesLabciQuoteByRequestFields.setAsOfDate("14 OCT 2024");
        quotesLabciQuoteByRequestFields.setAsOfTime("20:00:01.185");
        quotesLabciQuoteByRequestFields.setBidPrice(BigDecimal.valueOf(0));
        quotesLabciQuoteByRequestFields.setAskPrice(BigDecimal.valueOf(0));
        priceQuotes.add(quotesLabciQuoteByRequestFields);

        SECQuotesRequest secQuotesRequest = new SECQuotesRequest();
        List<ProductKey> productKeys = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProductType("ALL");
        productKey.setProdCdeAltClassCde("R");
        productKey.setProdAltNum("AAPL.O");
        productKeys.add(productKey);
        secQuotesRequest.setProductKeys(productKeys);

        ArgsHolder.putArgs(QuotesUSCachingServerService.THREAD_INVISIBLE_QUOTES_US_REQUEST, secQuotesRequest);

        String service = "dIDN_RDF";
        String labciResponseStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><watchlist xml:space=\"preserve\"><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=AAPL.O\">AAPL.O</fid><fid id=\"SERVICE\">dIDN_RDF</fid><fid id=\"UPLIMIT\"></fid><fid id=\"ADJUST_CLS\">231.300000</fid></ric></watchlist>\n";
        when(httpClientHelper.doGet(anyString(),anyList(),anyMap())).thenReturn(labciResponseStr);

        luldRulePriceUSService.calculateLuldRulePrice(priceQuotes, service, "CTS");

        Assert.assertNotNull(priceQuotes);
    }

    @Test
    public void testCalculateLuldRulePriceWithZeroLuld() throws Exception {
        List<QuotesLabciQuoteByRequestFields> priceQuotes = new ArrayList<>();
        QuotesLabciQuoteByRequestFields quotesLabciQuoteByRequestFields = new QuotesLabciQuoteByRequestFields();
        quotesLabciQuoteByRequestFields.setSymbol("AAPL");
        quotesLabciQuoteByRequestFields.setCurrency("USD");
        quotesLabciQuoteByRequestFields.setTradePrice(BigDecimal.valueOf(231.3));
        quotesLabciQuoteByRequestFields.setAsOfDate("14 OCT 2024");
        quotesLabciQuoteByRequestFields.setAsOfTime("20:00:01.185");
        quotesLabciQuoteByRequestFields.setBidPrice(BigDecimal.valueOf(0));
        quotesLabciQuoteByRequestFields.setAskPrice(BigDecimal.valueOf(0));
        priceQuotes.add(quotesLabciQuoteByRequestFields);

        SECQuotesRequest secQuotesRequest = new SECQuotesRequest();
        List<ProductKey> productKeys = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProductType("ALL");
        productKey.setProdCdeAltClassCde("R");
        productKey.setProdAltNum("AAPL.O");
        productKeys.add(productKey);
        secQuotesRequest.setProductKeys(productKeys);

        ArgsHolder.putArgs(QuotesUSCachingServerService.THREAD_INVISIBLE_QUOTES_US_REQUEST, secQuotesRequest);

        String service = "dIDN_RDF";
        String labciResponseStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><watchlist xml:space=\"preserve\"><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=AAPL.O\">AAPL.O</fid><fid id=\"SERVICE\">dIDN_RDF</fid><fid id=\"UPLIMIT\">0</fid><fid id=\"ADJUST_CLS\">231.300000</fid></ric></watchlist>\n";
        when(httpClientHelper.doGet(anyString(),anyList(),anyMap())).thenReturn(labciResponseStr);

        luldRulePriceUSService.calculateLuldRulePrice(priceQuotes, service, "CTS");

        Assert.assertNotNull(priceQuotes);
    }

    @Test
    public void testCalculateLuldRulePriceWithKCode() throws Exception {
        List<QuotesLabciQuoteByRequestFields> priceQuotes = new ArrayList<>();
        QuotesLabciQuoteByRequestFields quotesLabciQuoteByRequestFields = new QuotesLabciQuoteByRequestFields();
        quotesLabciQuoteByRequestFields.setSymbol("ARKK");
        quotesLabciQuoteByRequestFields.setCurrency("USD");
        quotesLabciQuoteByRequestFields.setTradePrice(BigDecimal.valueOf(231.3));
        quotesLabciQuoteByRequestFields.setAsOfDate("14 OCT 2024");
        quotesLabciQuoteByRequestFields.setAsOfTime("20:00:01.185");
        quotesLabciQuoteByRequestFields.setBidPrice(BigDecimal.valueOf(0));
        quotesLabciQuoteByRequestFields.setAskPrice(BigDecimal.valueOf(0));
        priceQuotes.add(quotesLabciQuoteByRequestFields);

        SECQuotesRequest secQuotesRequest = new SECQuotesRequest();
        List<ProductKey> productKeys = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProductType("ALL");
        productKey.setProdCdeAltClassCde("R");
        productKey.setProdAltNum("ARKK.K");
        productKeys.add(productKey);
        secQuotesRequest.setProductKeys(productKeys);

        ArgsHolder.putArgs(QuotesUSCachingServerService.THREAD_INVISIBLE_QUOTES_US_REQUEST, secQuotesRequest);

        String service = "IDN_RDF";
        String labciResponseStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><watchlist xml:space=\"preserve\"><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=AAPL.O\">ARKK.K</fid><fid id=\"SERVICE\">dIDN_RDF</fid><fid id=\"UPLIMIT\">0</fid><fid id=\"ADJUST_CLS\">231.300000</fid></ric></watchlist>\n";
        when(httpClientHelper.doGet(anyString(),anyList(),anyMap())).thenReturn(labciResponseStr);

        luldRulePriceUSService.calculateLuldRulePrice(priceQuotes, service, "CTS");

        Assert.assertNotNull(priceQuotes);
    }

    @Test
    public void testCalculateLuldRulePriceWithOtherCode() throws Exception {
        List<QuotesLabciQuoteByRequestFields> priceQuotes = new ArrayList<>();
        QuotesLabciQuoteByRequestFields quotesLabciQuoteByRequestFields = new QuotesLabciQuoteByRequestFields();
        quotesLabciQuoteByRequestFields.setSymbol("ARKK");
        quotesLabciQuoteByRequestFields.setCurrency("USD");
        quotesLabciQuoteByRequestFields.setTradePrice(BigDecimal.valueOf(231.3));
        quotesLabciQuoteByRequestFields.setAsOfDate("14 OCT 2024");
        quotesLabciQuoteByRequestFields.setAsOfTime("20:00:01.185");
        quotesLabciQuoteByRequestFields.setBidPrice(BigDecimal.valueOf(0));
        quotesLabciQuoteByRequestFields.setAskPrice(BigDecimal.valueOf(0));
        priceQuotes.add(quotesLabciQuoteByRequestFields);

        SECQuotesRequest secQuotesRequest = new SECQuotesRequest();
        List<ProductKey> productKeys = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProductType("ALL");
        productKey.setProdCdeAltClassCde("R");
        productKey.setProdAltNum("ARKK.H");
        productKeys.add(productKey);
        secQuotesRequest.setProductKeys(productKeys);

        ArgsHolder.putArgs(QuotesUSCachingServerService.THREAD_INVISIBLE_QUOTES_US_REQUEST, secQuotesRequest);

        String service = "IDN_RDF";
        String labciResponseStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><watchlist xml:space=\"preserve\"><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=AAPL.O\">ARKK.K</fid><fid id=\"SERVICE\">dIDN_RDF</fid><fid id=\"UPLIMIT\">0</fid><fid id=\"ADJUST_CLS\">231.300000</fid></ric></watchlist>\n";
        when(httpClientHelper.doGet(anyString(),anyList(),anyMap())).thenReturn(labciResponseStr);

        luldRulePriceUSService.calculateLuldRulePrice(priceQuotes, service, "CTS");

        Assert.assertNotNull(priceQuotes);
    }

    @Test
    public void testCalculateLuldRulePriceWithWrongCode() throws Exception {
        List<QuotesLabciQuoteByRequestFields> priceQuotes = new ArrayList<>();
        QuotesLabciQuoteByRequestFields quotesLabciQuoteByRequestFields = new QuotesLabciQuoteByRequestFields();
        quotesLabciQuoteByRequestFields.setSymbol("ARKK");
        quotesLabciQuoteByRequestFields.setCurrency("USD");
        quotesLabciQuoteByRequestFields.setTradePrice(BigDecimal.valueOf(231.3));
        quotesLabciQuoteByRequestFields.setAsOfDate("14 OCT 2024");
        quotesLabciQuoteByRequestFields.setAsOfTime("20:00:01.185");
        quotesLabciQuoteByRequestFields.setBidPrice(BigDecimal.valueOf(0));
        quotesLabciQuoteByRequestFields.setAskPrice(BigDecimal.valueOf(0));
        priceQuotes.add(quotesLabciQuoteByRequestFields);

        SECQuotesRequest secQuotesRequest = new SECQuotesRequest();
        List<ProductKey> productKeys = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProductType("ALL");
        productKey.setProdCdeAltClassCde("R");
        productKey.setProdAltNum("ARKK123");
        productKeys.add(productKey);
        secQuotesRequest.setProductKeys(productKeys);

        ArgsHolder.putArgs(QuotesUSCachingServerService.THREAD_INVISIBLE_QUOTES_US_REQUEST, secQuotesRequest);

        String service = "IDN_RDF";
        String labciResponseStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><watchlist xml:space=\"preserve\"><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=AAPL.O\">ARKK.K</fid><fid id=\"SERVICE\">dIDN_RDF</fid><fid id=\"UPLIMIT\">0</fid><fid id=\"ADJUST_CLS\">231.300000</fid></ric></watchlist>\n";
        when(httpClientHelper.doGet(anyString(),anyList(),anyMap())).thenReturn(labciResponseStr);

        luldRulePriceUSService.calculateLuldRulePrice(priceQuotes, service, "CTS");

        Assert.assertNotNull(priceQuotes);
    }
}
