package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.criteria.Criterias;
import com.hhhh.group.secwealth.mktdata.common.svc.httpclient.HttpConnectionManager;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMstarService;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundListRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.FundListResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundCalendarYearReturn;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundCumulativeReturn;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct;
import com.hhhh.group.secwealth.mktdata.fund.util.MstarAccessCodeHelper;
import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Value;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FundListServiceExecutorTest {

    private static final String URL = "URL";
    private static final String DATA_CLASS = "DATA_CLASS";
    private static final String INSTID = "INSTID";
    private static final String INSTUID = "INSTUID";
    private static final String EMAIL = "EMAIL";
    private static final String GROUP = "GROUP";
    private static final String SESSION_U_R_L = "SESSION_U_R_L";
    private static final int RETRY_TIMES = 78;
    private static final String url = "https://api.morningstar.com/v2/service/mf/vlb1ftg01abvbrrx";
    @Mock
    private InternalProductKeyUtil internalProductKeyUtil;
    @Mock
    private Map<String, Map<String, String>> multiLanguageForInvestmentStrategyMap;
    @Mock
    private JAXBContext dataClassJC;
    @Mock
    private FastDateFormat formatter;
    @Mock
    private HttpConnectionManager connManager;
    @Mock
    private MstarAccessCodeHelper accessCodeHelper;
    @Mock
    private AbstractMstarService abstractMstarService;

    @InjectMocks
    private FundListServiceExecutor underTest;

    @Nested
    class WhenIniting {
        @BeforeEach
        void setup() throws ClassNotFoundException, JAXBException {
            dataClassJC = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundlist.FundListData"));
        }


    }

    @Nested
    class WhenExecuting {
        @Mock
        private FundListRequest request;


        @BeforeEach
        void setup() throws Exception{
            Field url = underTest.getClass().getDeclaredField("url");
            url.setAccessible(true);
            url.set(underTest,"https://api.morningstar.com/v2/service/mf/vlb1ftg01abvbrrx");
            Field formatter = underTest.getClass().getDeclaredField("formatter");
            formatter.setAccessible(true);
            formatter.set(underTest,FastDateFormat.getInstance("yyyy-MM-dd"));
            when(request.getChannelRestrictCode()).thenReturn("SRBPI");
            when(request.getCountryCode()).thenReturn("HK");
            when(request.getGroupMember()).thenReturn("HBAP");
            when(request.getLocale()).thenReturn("en");
            when(request.getProductKeys()).thenReturn(DataBuilder.buildProductKeyList());
            when(internalProductKeyUtil.getProductBySearchWithAltClassCde(anyString(),anyString(),anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(DataBuilder.buildSearchProduct());
            Unmarshaller un = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundlist.FundListData")).createUnmarshaller();
            when(dataClassJC.createUnmarshaller()).thenReturn(un);
            String res = DataBuilder.buildRespStr();
            when(connManager.sendRequest(any(String.class),any(String.class),any())).thenReturn(res);
        }
        @Test
         void test_WhenExecuting() throws Exception {
            underTest.execute(request);
            Assertions.assertEquals("SRBPI",request.getChannelRestrictCode());
        }
    }

    @Nested
    class WhenSendingFundListRequest {
        @Mock
        private FundListRequest request;

        @BeforeEach
        void setup() throws Exception{
            Field url = underTest.getClass().getDeclaredField("url");
            url.setAccessible(true);
            url.set(underTest,"https://api.morningstar.com/v2/service/mf/vlb1ftg01abvbrrx");
            Field formatter = underTest.getClass().getDeclaredField("formatter");
            formatter.setAccessible(true);
            formatter.set(underTest,FastDateFormat.getInstance("yyyy-MM-dd"));
            Mockito.when(request.getChannelRestrictCode()).thenReturn("SRBPI");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getLocale()).thenReturn("en");
            Mockito.when(request.getProductKeys()).thenReturn(DataBuilder.buildProductKeyList());
            Mockito.when(internalProductKeyUtil.getProductBySearchWithAltClassCde(anyString(),anyString(),anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(DataBuilder.buildSearchProduct());
            Unmarshaller un = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundlist.FundListData")).createUnmarshaller();
            Mockito.when(dataClassJC.createUnmarshaller()).thenReturn(un);
            String res = DataBuilder.buildRespStr();
            Mockito.when(connManager.sendRequest(any(String.class),any(String.class),any())).thenReturn(res);
        }

        @Test
         void test_WhenWhenSendingFundList() throws Exception{
            underTest.sendFundListRequest(request);
            Assertions.assertEquals("SRBPI",request.getChannelRestrictCode());
        }
    }

    private static class DataBuilder {
        public static List<ProductKey> buildProductKeyList(){
            List<ProductKey> productKeyList = new ArrayList<>();
            ProductKey productKey = new ProductKey();
            productKey.setMarket("HK");
            productKey.setProdAltNum("U62509");
            productKey.setProdCdeAltClassCde("M");
            productKey.setProductType("UT");
            productKeyList.add(productKey);
            return productKeyList;
        }

        public static List<Criterias> buildCriterias(){
            List<Criterias> criteriasList = new ArrayList<>();
            Criterias criterias = new Criterias();
            criterias.setCriteriaKey("topTenHoldings");
            criterias.setCriteriaValue(true);
            criteriasList.add(criterias);
            return criteriasList;
        }

        public static SearchProduct buildSearchProduct(){
            SearchProduct searchProduct = new SearchProduct();
            searchProduct.setExternalKey("0P0001BFG7");
            SearchableObject searchObject = new SearchableObject();
            searchObject.setSymbol("540002");
            searchProduct.setSearchObject(searchObject);
            return searchProduct;
        }

        public static List<SearchProduct>  buildSearchProductList(){
            List<SearchProduct>  searchProductList = new ArrayList<>();
            SearchProduct searchProduct = buildSearchProduct();
            searchProductList.add(searchProduct);
            return searchProductList;
        }

        public static List<FundListProduct>  buildFundListProduct(){
            List<FundListProduct>  fundListProductList = new ArrayList<>();
            FundListProduct fundListProduct = new FundListProduct();
            fundListProduct.setProdAltNumXCode("0P0001BFG7");
            fundListProduct.setSymbol("540002");
            FundListProduct.Header header = fundListProduct.new Header();
            fundListProduct.setHeader(header);
            FundListProduct.Summary summary = fundListProduct.new Summary();
            fundListProduct.setSummary(summary);
            FundListProduct.Profile profile = fundListProduct.new Profile();
            fundListProduct.setProfile(profile);
            FundListProduct.Performance performance = fundListProduct.new Performance();
            fundListProduct.setPerformance(performance);
            FundListProduct.Performance.CumulativeTotalReturns cumulativeTotalReturns = fundListProduct.getPerformance().new CumulativeTotalReturns();
            List<FundCumulativeReturn> fundCumulativeReturnList = new ArrayList<>();
            FundCumulativeReturn fundCumulativeReturn = new FundCumulativeReturn();
            for (int i = 0; i < 8; i++) {
                fundCumulativeReturnList.add(fundCumulativeReturn);
            }
            cumulativeTotalReturns.setItems(fundCumulativeReturnList);
            FundListProduct.Performance.CalendarYearTotalReturns calendarYearTotalReturns = fundListProduct.getPerformance().new CalendarYearTotalReturns();
            List<FundCalendarYearReturn> fundCalendarYearReturnList = new ArrayList<>();
            FundCalendarYearReturn fundCalendarYearReturn =new FundCalendarYearReturn();
            for (int i = 0; i < 5; i++) {
                fundCalendarYearReturnList.add(fundCalendarYearReturn);
            }
            calendarYearTotalReturns.setItems(fundCalendarYearReturnList);
            fundListProduct.getPerformance().setCumulativeTotalReturns(cumulativeTotalReturns);
            fundListProduct.getPerformance().setCalendarYearTotalReturns(calendarYearTotalReturns);
            FundListProduct.YieldAndCredit yieldAndCredit = fundListProduct.new YieldAndCredit();
            fundListProduct.setYieldAndCredit(yieldAndCredit);

            fundListProductList.add(fundListProduct);
            return fundListProductList;
        }

        public static FundListResponse buildFundListResponse(){
            FundListResponse fundListResponse = new FundListResponse();
            fundListResponse.setSearchProductList(buildSearchProductList());
            fundListResponse.setProducts(buildFundListProduct());
            return fundListResponse;
        }

        public static String buildRespStr(){
            String res = "<response>\n" +
                    "<status>\n" +
                    "<code>0</code>\n" +
                    "<message>OK</message>\n" +
                    "</status>\n" +
                    "<data _idtype=\"mstarid\" _id=\"0P0001BFG7\">\n" +
                    "<api _id=\"vlb1ftg01abvbrrx\">\n" +
                    "<AABRP-AssetAllocBondLong>93.62912</AABRP-AssetAllocBondLong>\n" +
                    "<AABRP-AssetAllocBondNet>93.58858</AABRP-AssetAllocBondNet>\n" +
                    "<AABRP-AssetAllocBondShort>0.04054</AABRP-AssetAllocBondShort>\n" +
                    "<AABRP-AssetAllocCashLong>4.35000</AABRP-AssetAllocCashLong>\n" +
                    "<AABRP-AssetAllocCashNet>1.87615</AABRP-AssetAllocCashNet>\n" +
                    "<AABRP-AssetAllocCashShort>2.47385</AABRP-AssetAllocCashShort>\n" +
                    "<AABRP-AssetAllocConvBondLong>4.53527</AABRP-AssetAllocConvBondLong>\n" +
                    "<AABRP-AssetAllocEquityLong>0.00000</AABRP-AssetAllocEquityLong>\n" +
                    "<AABRP-AssetAllocEquityNet>0.00000</AABRP-AssetAllocEquityNet>\n" +
                    "<AABRP-AssetAllocEquityShort>0.00000</AABRP-AssetAllocEquityShort>\n" +
                    "<AABRP-ConvertibleLong>4.53527</AABRP-ConvertibleLong>\n" +
                    "<AABRP-ConvertibleNet>4.53527</AABRP-ConvertibleNet>\n" +
                    "<AABRP-ConvertibleShort>0.00000</AABRP-ConvertibleShort>\n" +
                    "<AABRP-OtherLong>4.53527</AABRP-OtherLong>\n" +
                    "<AABRP-OtherNet>4.53527</AABRP-OtherNet>\n" +
                    "<AABRP-OtherShort>0.00000</AABRP-OtherShort>\n" +
                    "<AABRP-PortfolioDate>2021-05-31</AABRP-PortfolioDate>\n" +
                    "<AABRP-PreferredStockLong>0.00000</AABRP-PreferredStockLong>\n" +
                    "<AABRP-PreferredStockNet>0.00000</AABRP-PreferredStockNet>\n" +
                    "<AABRP-PreferredStockShort>0.00000</AABRP-PreferredStockShort>\n" +
                    "<REBRP-PortfolioDate>2021-05-31</REBRP-PortfolioDate>\n" +
                    "<GBSRP-FixedIncPrimarySectorAgencyMortgage-BackedLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorAgencyMortgage-BackedLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorAssetBackedLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorAssetBackedLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorBankLoanLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorBankLoanLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorCashEquivalentsLongRescaled>4.23230</GBSRP-FixedIncPrimarySectorCashEquivalentsLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorCommercialMortgageBackedLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorCommercialMortgageBackedLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorConvertibleLongRescaled>3.94365</GBSRP-FixedIncPrimarySectorConvertibleLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorCorporateBondLongRescaled>73.45355</GBSRP-FixedIncPrimarySectorCorporateBondLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorCoveredBondLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorCoveredBondLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorForwardFutureLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorForwardFutureLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorGovernmentLongRescaled>3.60119</GBSRP-FixedIncPrimarySectorGovernmentLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorGovernmentRelatedLongRescaled>14.28870</GBSRP-FixedIncPrimarySectorGovernmentRelatedLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorMunicipalTaxExemptLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorMunicipalTaxExemptLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorMunicipalTaxableLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorMunicipalTaxableLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorNonAgencyResidentialMortgageBackedLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorNonAgencyResidentialMortgageBackedLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorOptionWarrantLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorOptionWarrantLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorPreferredStockLongRescaled>0.48061</GBSRP-FixedIncPrimarySectorPreferredStockLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorSwapLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorSwapLongRescaled>\n" +
                    "<GBSRP-FixedIncSuperSectorCashEquivalentsLongRescaled>4.23230</GBSRP-FixedIncSuperSectorCashEquivalentsLongRescaled>\n" +
                    "<GBSRP-FixedIncSuperSectorCorporateLongRescaled>77.87781</GBSRP-FixedIncSuperSectorCorporateLongRescaled>\n" +
                    "<GBSRP-FixedIncSuperSectorDerivativeLongRescaled>0.00000</GBSRP-FixedIncSuperSectorDerivativeLongRescaled>\n" +
                    "<GBSRP-FixedIncSuperSectorGovernmentLongRescaled>17.88989</GBSRP-FixedIncSuperSectorGovernmentLongRescaled>\n" +
                    "<GBSRP-FixedIncSuperSectorMunicipalLongRescaled>0.00000</GBSRP-FixedIncSuperSectorMunicipalLongRescaled>\n" +
                    "<GBSRP-FixedIncSuperSectorSecuritizedLongRescaled>0.00000</GBSRP-FixedIncSuperSectorSecuritizedLongRescaled>\n" +
                    "<GBSRP-PortfolioDate>2021-05-31</GBSRP-PortfolioDate>\n" +
                    "<PSRP-NumberOfBondHoldings>410</PSRP-NumberOfBondHoldings>\n" +
                    "<PSRP-NumberOfStockHoldings>0</PSRP-NumberOfStockHoldings>\n" +
                    "<T10H-Holdings>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>BD</HoldingType>\n" +
                    "<Name>China Construction Bank Corporation 2.45%</Name>\n" +
                    "<CountryId>CHN</CountryId>\n" +
                    "<Country>China</Country>\n" +
                    "<CurrencyId>USD</CurrencyId>\n" +
                    "<Currency>US Dollar</Currency>\n" +
                    "<CUSIP>Y1R396QU0</CUSIP>\n" +
                    "<ISIN>XS2140531950</ISIN>\n" +
                    "<Weighting>1.09707</Weighting>\n" +
                    "<NumberOfShare>27000000</NumberOfShare>\n" +
                    "<MarketValue>27555660</MarketValue>\n" +
                    "<ShareChange>0</ShareChange>\n" +
                    "<MaturityDate>2030-06-24</MaturityDate>\n" +
                    "<Coupon>2.45</Coupon>\n" +
                    "</HoldingDetail>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>B</HoldingType>\n" +
                    "<Name>Tencent Holdings Limited 3.98%</Name>\n" +
                    "<CountryId>CHN</CountryId>\n" +
                    "<Country>China</Country>\n" +
                    "<CurrencyId>USD</CurrencyId>\n" +
                    "<Currency>US Dollar</Currency>\n" +
                    "<CUSIP>88032XAN4</CUSIP>\n" +
                    "<ISIN>US88032XAN49</ISIN>\n" +
                    "<Weighting>1.00874</Weighting>\n" +
                    "<NumberOfShare>23000000</NumberOfShare>\n" +
                    "<MarketValue>25337030</MarketValue>\n" +
                    "<ShareChange>0</ShareChange>\n" +
                    "<MaturityDate>2029-04-11</MaturityDate>\n" +
                    "<Coupon>3.98</Coupon>\n" +
                    "</HoldingDetail>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>BC</HoldingType>\n" +
                    "<Name>DBS Group Holdings Ltd 1.82%</Name>\n" +
                    "<CountryId>SGP</CountryId>\n" +
                    "<Country>Singapore</Country>\n" +
                    "<CurrencyId>USD</CurrencyId>\n" +
                    "<Currency>US Dollar</Currency>\n" +
                    "<CUSIP>Y20246J57</CUSIP>\n" +
                    "<ISIN>XS2310058891</ISIN>\n" +
                    "<Weighting>0.90768</Weighting>\n" +
                    "<NumberOfShare>22819000</NumberOfShare>\n" +
                    "<MarketValue>22798691</MarketValue>\n" +
                    "<ShareChange>0</ShareChange>\n" +
                    "<MaturityDate>2031-03-10</MaturityDate>\n" +
                    "<Coupon>1.82</Coupon>\n" +
                    "</HoldingDetail>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>BC</HoldingType>\n" +
                    "<Name>DBS Group Holdings Ltd 3.3%</Name>\n" +
                    "<CountryId>SGP</CountryId>\n" +
                    "<Country>Singapore</Country>\n" +
                    "<CurrencyId>USD</CurrencyId>\n" +
                    "<Currency>US Dollar</Currency>\n" +
                    "<CUSIP>Y20246J32</CUSIP>\n" +
                    "<ISIN>XS2122408854</ISIN>\n" +
                    "<Weighting>0.85526</Weighting>\n" +
                    "<NumberOfShare>20927000</NumberOfShare>\n" +
                    "<MarketValue>21481984</MarketValue>\n" +
                    "<ShareChange>0</ShareChange>\n" +
                    "<MaturityDate>2049-12-31</MaturityDate>\n" +
                    "<Coupon>3.3</Coupon>\n" +
                    "</HoldingDetail>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>B</HoldingType>\n" +
                    "<Name>SF Holding Investment Limited 2.88%</Name>\n" +
                    "<CountryId>CHN</CountryId>\n" +
                    "<Country>China</Country>\n" +
                    "<CurrencyId>USD</CurrencyId>\n" +
                    "<Currency>US Dollar</Currency>\n" +
                    "<CUSIP>G8081BAB9</CUSIP>\n" +
                    "<ISIN>XS2099049699</ISIN>\n" +
                    "<Weighting>0.82199</Weighting>\n" +
                    "<NumberOfShare>20500000</NumberOfShare>\n" +
                    "<MarketValue>20646370</MarketValue>\n" +
                    "<ShareChange>0</ShareChange>\n" +
                    "<MaturityDate>2030-02-20</MaturityDate>\n" +
                    "<Coupon>2.88</Coupon>\n" +
                    "</HoldingDetail>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>B</HoldingType>\n" +
                    "<Name>Sinopec Group Overseas Development (2018) Limited 2.7%</Name>\n" +
                    "<CountryId>CHN</CountryId>\n" +
                    "<Country>China</Country>\n" +
                    "<CurrencyId>USD</CurrencyId>\n" +
                    "<Currency>US Dollar</Currency>\n" +
                    "<CUSIP>G82016AP4</CUSIP>\n" +
                    "<ISIN>USG82016AP45</ISIN>\n" +
                    "<Weighting>0.81377</Weighting>\n" +
                    "<NumberOfShare>20200000</NumberOfShare>\n" +
                    "<MarketValue>20439976</MarketValue>\n" +
                    "<ShareChange>0</ShareChange>\n" +
                    "<MaturityDate>2030-05-13</MaturityDate>\n" +
                    "<Coupon>2.7</Coupon>\n" +
                    "</HoldingDetail>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>B</HoldingType>\n" +
                    "<Name>CNAC (HK) Finbridge Company Limited 3.88%</Name>\n" +
                    "<CountryId>CHN</CountryId>\n" +
                    "<Country>China</Country>\n" +
                    "<CurrencyId>USD</CurrencyId>\n" +
                    "<Currency>US Dollar</Currency>\n" +
                    "<CUSIP>Y1670YAP0</CUSIP>\n" +
                    "<ISIN>XS2011969735</ISIN>\n" +
                    "<Weighting>0.79056</Weighting>\n" +
                    "<NumberOfShare>20000000</NumberOfShare>\n" +
                    "<MarketValue>19857000</MarketValue>\n" +
                    "<ShareChange>0</ShareChange>\n" +
                    "<MaturityDate>2029-06-19</MaturityDate>\n" +
                    "<Coupon>3.88</Coupon>\n" +
                    "</HoldingDetail>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>BD</HoldingType>\n" +
                    "<Name>Development Bank of the Philippines 2.38%</Name>\n" +
                    "<CountryId>PHL</CountryId>\n" +
                    "<Country>Philippines</Country>\n" +
                    "<CurrencyId>USD</CurrencyId>\n" +
                    "<Currency>US Dollar</Currency>\n" +
                    "<CUSIP>Y20339AL4</CUSIP>\n" +
                    "<ISIN>XS2301051541</ISIN>\n" +
                    "<Weighting>0.77512</Weighting>\n" +
                    "<NumberOfShare>20000000</NumberOfShare>\n" +
                    "<MarketValue>19469000</MarketValue>\n" +
                    "<ShareChange>0</ShareChange>\n" +
                    "<MaturityDate>2031-03-11</MaturityDate>\n" +
                    "<Coupon>2.38</Coupon>\n" +
                    "</HoldingDetail>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>BD</HoldingType>\n" +
                    "<Name>Bank of China Limited 5%</Name>\n" +
                    "<CountryId>CHN</CountryId>\n" +
                    "<Country>China</Country>\n" +
                    "<CurrencyId>USD</CurrencyId>\n" +
                    "<Currency>US Dollar</Currency>\n" +
                    "<CUSIP>06120TAA6</CUSIP>\n" +
                    "<ISIN>US06120TAA60</ISIN>\n" +
                    "<Weighting>0.7581</Weighting>\n" +
                    "<NumberOfShare>17000000</NumberOfShare>\n" +
                    "<MarketValue>19041530</MarketValue>\n" +
                    "<ShareChange>0</ShareChange>\n" +
                    "<MaturityDate>2024-11-13</MaturityDate>\n" +
                    "<Coupon>5</Coupon>\n" +
                    "</HoldingDetail>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>FO</HoldingType>\n" +
                    "<Name>hhhh US Dollar Liquidity Y</Name>\n" +
                    "<CountryId>IRL</CountryId>\n" +
                    "<Country>Ireland</Country>\n" +
                    "<CurrencyId>USD</CurrencyId>\n" +
                    "<Currency>US Dollar</Currency>\n" +
                    "<ISIN>IE00BYYJJ149</ISIN>\n" +
                    "<Weighting>0.725</Weighting>\n" +
                    "<NumberOfShare>18210295</NumberOfShare>\n" +
                    "<MarketValue>18210295</MarketValue>\n" +
                    "<ShareChange>8669786</ShareChange>\n" +
                    "</HoldingDetail>\n" +
                    "</T10H-Holdings>\n" +
                    "<PSRP-PortfolioDate>2021-05-31</PSRP-PortfolioDate>\n" +
                    "<PS-PortfolioCurrencyId>USD</PS-PortfolioCurrencyId>\n" +
                    "<IC-InvestmentStrategy>The Sub-Fund seeks a reasonably high income whilst maintaining a prudent policy of capital conservation, through investing in fixed interest securities primarily within the Asian region. The Sub-Fund invests primarily in Asian bonds, including both government and corporate bond, and also investment grade, non-investment grade and non-rated bond. Derivative instruments may be acquired for investment purposes or hedging purposes. The Sub-Fund may invest up to 10% of its net assets in contingent convertible securities; however such investment is not expected to exceed 5%.</IC-InvestmentStrategy>\n" +
                    "<IC-MultilingualInvestmentStrategies>\n" +
                    "<MultilingualStrategy>\n" +
                    "<NarrativeLanguageId>ZH</NarrativeLanguageId>\n" +
                    "<NarrativeLanguageName>Chinese Simplified</NarrativeLanguageName>\n" +
                    "<InvestmentStrategy>本基金透过投资于亚洲区内（不包括日本）平均年期为2 至5 年（以存续期风险表示）的定息证券和政府债券，目标是运用审慎的资本保存策略来获取高水平的合理回报。本基金将广泛投资于上市债券。然而，若本基金经理人认为情况切合谋取最高资本增值的目的，本基金亦可投资于政府债券，其他非上市定息证券及金融票据，及持有现金存款以伺候投资机会。本基金最少将70% 的非现金资产投资于成立在本基金名称所反映的地区之发行机构所发行或在该地区发行，或以本基金名称所反映的货币或与所反映的地区有关的货币为结算货币之债券</InvestmentStrategy>\n" +
                    "</MultilingualStrategy>\n" +
                    "<MultilingualStrategy>\n" +
                    "<NarrativeLanguageId>ZH</NarrativeLanguageId>\n" +
                    "<NarrativeLanguageName>Chinese Traditional</NarrativeLanguageName>\n" +
                    "<InvestmentStrategy>本基金透過投資於亞洲區內（不包括日本）平均年期為2 至5 年（以存續期風險表示）的定息證券和政府債券，目標是運用審慎的資本保存策略來獲取高水平的合理回報。本基金將廣泛投資於上市債券。然而，若本基金經理人認為情況切合謀取最高資本增值的目的，本基金亦可投資於政府債券，其他非上市定息證券及金融票據，及持有現金存款以伺候投資機會。本基金最少將70% 的非現金資產投資於成立在本基金名稱所反映的地區之發行機構所發行或在該地區發行，或以本基金名稱所反映的貨幣或與所反映的地區有關的貨幣為結算貨幣之債券</InvestmentStrategy>\n" +
                    "</MultilingualStrategy>\n" +
                    "</IC-MultilingualInvestmentStrategies>\n" +
                    "<TS-NAV52wkHigh>10.54800</TS-NAV52wkHigh>\n" +
                    "<TS-NAV52wkLow>10.15900</TS-NAV52wkLow>\n" +
                    "<DP-Dividend>0.028000000</DP-Dividend>\n" +
                    "<DP-DividendDate>2021-06-30</DP-DividendDate>\n" +
                    "<CQBRP-CreditQualA>25.75550</CQBRP-CreditQualA>\n" +
                    "<CQBRP-CreditQualAA>0.74183</CQBRP-CreditQualAA>\n" +
                    "<CQBRP-CreditQualAAA>-1.86111</CQBRP-CreditQualAAA>\n" +
                    "<CQBRP-CreditQualB>10.44063</CQBRP-CreditQualB>\n" +
                    "<CQBRP-CreditQualBB>17.84357</CQBRP-CreditQualBB>\n" +
                    "<CQBRP-CreditQualBBB>41.77932</CQBRP-CreditQualBBB>\n" +
                    "<CQBRP-CreditQualBelowB>0.00000</CQBRP-CreditQualBelowB>\n" +
                    "<CQBRP-CreditQualNotRated>5.30024</CQBRP-CreditQualNotRated>\n" +
                    "<CQBRP-CreditQualDate>2021-05-31</CQBRP-CreditQualDate>\n" +
                    "<MR-Rating3Year>3</MR-Rating3Year>\n" +
                    "<MR-Rating5Year>3</MR-Rating5Year>\n" +
                    "<MR-Rating10Year>4</MR-Rating10Year>\n" +
                    "<MR-RatingDate>2021-06-30</MR-RatingDate>\n" +
                    "<MR-RatingOverall>4</MR-RatingOverall>\n" +
                    "<TTR-BestFitIndexReturnYTD>-0.24825</TTR-BestFitIndexReturnYTD>\n" +
                    "<TTR-CategoryReturnYTD>-0.35220</TTR-CategoryReturnYTD>\n" +
                    "<CYR-BestFitIndexYear1>6.59040</CYR-BestFitIndexYear1>\n" +
                    "<CYR-BestFitIndexYear2>10.99896</CYR-BestFitIndexYear2>\n" +
                    "<CYR-BestFitIndexYear3>-0.39108</CYR-BestFitIndexYear3>\n" +
                    "<CYR-BestFitIndexYear4>5.42789</CYR-BestFitIndexYear4>\n" +
                    "<CYR-BestFitIndexYear5>5.15405</CYR-BestFitIndexYear5>\n" +
                    "<CYR-BestFitIndexYear6>2.04320</CYR-BestFitIndexYear6>\n" +
                    "<CYR-BestFitIndexYear7>8.97368</CYR-BestFitIndexYear7>\n" +
                    "<CYR-BestFitIndexYear8>-3.12026</CYR-BestFitIndexYear8>\n" +
                    "<CYR-CategoryYear1>6.56298</CYR-CategoryYear1>\n" +
                    "<CYR-CategoryYear2>10.69667</CYR-CategoryYear2>\n" +
                    "<CYR-CategoryYear3>-2.87234</CYR-CategoryYear3>\n" +
                    "<CYR-CategoryYear4>7.07348</CYR-CategoryYear4>\n" +
                    "<CYR-CategoryYear5>4.78317</CYR-CategoryYear5>\n" +
                    "<CYR-CategoryYear6>-0.28268</CYR-CategoryYear6>\n" +
                    "<CYR-CategoryYear7>3.84262</CYR-CategoryYear7>\n" +
                    "<CYR-CategoryYear8>-4.26289</CYR-CategoryYear8>\n" +
                    "<CYR-CategoryYear9>10.69333</CYR-CategoryYear9>\n" +
                    "<CYR-CategoryYear10>1.42354</CYR-CategoryYear10>\n" +
                    "<TTR-BestFitIndexReturn1Mth>0.61175</TTR-BestFitIndexReturn1Mth>\n" +
                    "<TTR-BestFitIndexReturn3Mth>1.55631</TTR-BestFitIndexReturn3Mth>\n" +
                    "<TTR-BestFitIndexReturn6Mth>-0.24825</TTR-BestFitIndexReturn6Mth>\n" +
                    "<TTR-BestFitIndexReturn1Yr>3.46375</TTR-BestFitIndexReturn1Yr>\n" +
                    "<TTR-BestFitIndexReturn3Yr>6.47659</TTR-BestFitIndexReturn3Yr>\n" +
                    "<TTR-BestFitIndexReturn5Yr>4.10508</TTR-BestFitIndexReturn5Yr>\n" +
                    "<TTR-CategoryReturn1Mth>-0.06869</TTR-CategoryReturn1Mth>\n" +
                    "<TTR-CategoryReturn3Mth>1.04697</TTR-CategoryReturn3Mth>\n" +
                    "<TTR-CategoryReturn6Mth>-0.35220</TTR-CategoryReturn6Mth>\n" +
                    "<TTR-CategoryReturn1Yr>5.98113</TTR-CategoryReturn1Yr>\n" +
                    "<TTR-CategoryReturn3Yr>5.83279</TTR-CategoryReturn3Yr>\n" +
                    "<TTR-CategoryReturn6Yr>3.92459</TTR-CategoryReturn6Yr>\n" +
                    "<TTR-CategoryReturn10Yr>3.34636</TTR-CategoryReturn10Yr>\n" +
                    "<FM-Managers>\n" +
                    "<Manager>\n" +
                    "<Display>Display</Display>\n" +
                    "<ManagerId>117371</ManagerId>\n" +
                    "<Name>Alfred Mui</Name>\n" +
                    "<Role>Manager</Role>\n" +
                    "<StartDate>2021-02-01</StartDate>\n" +
                    "<Tenure>0.41</Tenure>\n" +
                    "</Manager>\n" +
                    "<Manager>\n" +
                    "<Display>Display as Lead</Display>\n" +
                    "<ManagerId>121265</ManagerId>\n" +
                    "<Name>Steven Wong</Name>\n" +
                    "<Role>Manager</Role>\n" +
                    "<StartDate>2021-02-01</StartDate>\n" +
                    "<Tenure>0.41</Tenure>\n" +
                    "</Manager>\n" +
                    "</FM-Managers>\n" +
                    "<MPTPI-IndexName>JPM Asia Credit TR USD</MPTPI-IndexName>\n" +
                    "<DP-DayEndDate>2021-07-26</DP-DayEndDate>\n" +
                    "<TTR-MonthEndDate>2021-06-30</TTR-MonthEndDate>\n" +
                    "<DP-CategoryReturn1Mth>0.05325</DP-CategoryReturn1Mth>\n" +
                    "<DP-CategoryReturn3Mth>0.84270</DP-CategoryReturn3Mth>\n" +
                    "<DP-CategoryReturn6Mth>-0.45241</DP-CategoryReturn6Mth>\n" +
                    "<DP-CategoryReturn1Yr>3.75480</DP-CategoryReturn1Yr>\n" +
                    "<DP-CategoryReturn3Yr>5.94011</DP-CategoryReturn3Yr>\n" +
                    "<DP-CategoryReturn5Yr>3.75198</DP-CategoryReturn5Yr>\n" +
                    "<DP-CategoryReturn10Yr>3.53069</DP-CategoryReturn10Yr>\n" +
                    "<DP-CategoryReturnYTD>-0.24650</DP-CategoryReturnYTD>\n" +
                    "<SR-StubYearEndReturnEndDate>2021-07-28</SR-StubYearEndReturnEndDate>\n" +
                    "<SR-StubYearEndReturn>0.39319</SR-StubYearEndReturn>\n" +
                    "<FB-PrimaryProspectusBenchmarks>\n" +
                    "<PrimaryProspectusBenchmark>\n" +
                    "<IndexId>F00000X2TA</IndexId>\n" +
                    "<IndexName>Markit iBoxx Asian USD Dollar Bd TR USD</IndexName>\n" +
                    "<Weighting>100</Weighting>\n" +
                    "</PrimaryProspectusBenchmark>\n" +
                    "</FB-PrimaryProspectusBenchmarks>\n" +
                    "<TS-DayEndNAVDate>2021-07-26</TS-DayEndNAVDate>\n" +
                    "<MPTPI-IndexID>F00000JWM0</MPTPI-IndexID>\n" +
                    "<FM2-Managers>\n" +
                    "<Manager>\n" +
                    "<Display>Display</Display>\n" +
                    "<ManagerId>117371</ManagerId>\n" +
                    "<Name>Alfred Lap Chung Mui</Name>\n" +
                    "<Role>Manager</Role>\n" +
                    "<StartDate>2021-02-01</StartDate>\n" +
                    "<Tenure>0.41</Tenure>\n" +
                    "</Manager>\n" +
                    "<Manager>\n" +
                    "<Display>Display as Lead</Display>\n" +
                    "<ManagerId>121265</ManagerId>\n" +
                    "<Name>Steven Wong</Name>\n" +
                    "<Role>Manager</Role>\n" +
                    "<StartDate>2021-02-01</StartDate>\n" +
                    "<Tenure>0.41</Tenure>\n" +
                    "</Manager>\n" +
                    "</FM2-Managers>\n" +
                    "</api>\n" +
                    "<ProspectusPrimaryIndex _id=\"F00000X2TA\">\n" +
                    "<ProspectusPrimaryIndexReturn1Mth>0.61175</ProspectusPrimaryIndexReturn1Mth>\n" +
                    "<ProspectusPrimaryIndexReturn3Mth>1.55631</ProspectusPrimaryIndexReturn3Mth>\n" +
                    "<ProspectusPrimaryIndexReturn6Mth>-0.24825</ProspectusPrimaryIndexReturn6Mth>\n" +
                    "<ProspectusPrimaryIndexReturn1Yr>3.46375</ProspectusPrimaryIndexReturn1Yr>\n" +
                    "<ProspectusPrimaryIndexReturn3Yr>6.47659</ProspectusPrimaryIndexReturn3Yr>\n" +
                    "<ProspectusPrimaryIndexReturn5Yr>4.10508</ProspectusPrimaryIndexReturn5Yr>\n" +
                    "<ProspectusPrimaryIndexReturnYTD>-0.24825</ProspectusPrimaryIndexReturnYTD>\n" +
                    "<ProspectusPrimaryIndexYear1>6.59040</ProspectusPrimaryIndexYear1>\n" +
                    "<ProspectusPrimaryIndexYear2>10.99896</ProspectusPrimaryIndexYear2>\n" +
                    "<ProspectusPrimaryIndexYear3>-0.39108</ProspectusPrimaryIndexYear3>\n" +
                    "<ProspectusPrimaryIndexYear4>5.42789</ProspectusPrimaryIndexYear4>\n" +
                    "<ProspectusPrimaryIndexYear5>5.15405</ProspectusPrimaryIndexYear5>\n" +
                    "<ProspectusPrimaryIndexYear6>2.04320</ProspectusPrimaryIndexYear6>\n" +
                    "<ProspectusPrimaryIndexYear7>8.97368</ProspectusPrimaryIndexYear7>\n" +
                    "<ProspectusPrimaryIndexYear8>-3.12026</ProspectusPrimaryIndexYear8>\n" +
                    "</ProspectusPrimaryIndex>\n" +
                    "</data>\n" +
                    "<data _idtype=\"mstarid\" _id=\"0P0001DK0T\">\n" +
                    "<api _id=\"vlb1ftg01abvbrrx\">\n" +
                    "<AABRP-AssetAllocBondLong>0.00000</AABRP-AssetAllocBondLong>\n" +
                    "<AABRP-AssetAllocBondNet>0.00000</AABRP-AssetAllocBondNet>\n" +
                    "<AABRP-AssetAllocBondShort>0.00000</AABRP-AssetAllocBondShort>\n" +
                    "<AABRP-AssetAllocCashLong>1.12801</AABRP-AssetAllocCashLong>\n" +
                    "<AABRP-AssetAllocCashNet>0.22284</AABRP-AssetAllocCashNet>\n" +
                    "<AABRP-AssetAllocCashShort>0.90517</AABRP-AssetAllocCashShort>\n" +
                    "<AABRP-AssetAllocConvBondLong>0.00000</AABRP-AssetAllocConvBondLong>\n" +
                    "<AABRP-AssetAllocEquityLong>99.77716</AABRP-AssetAllocEquityLong>\n" +
                    "<AABRP-AssetAllocEquityNet>99.77716</AABRP-AssetAllocEquityNet>\n" +
                    "<AABRP-AssetAllocEquityShort>0.00000</AABRP-AssetAllocEquityShort>\n" +
                    "<AABRP-ConvertibleLong>0.00000</AABRP-ConvertibleLong>\n" +
                    "<AABRP-ConvertibleNet>0.00000</AABRP-ConvertibleNet>\n" +
                    "<AABRP-ConvertibleShort>0.00000</AABRP-ConvertibleShort>\n" +
                    "<AABRP-OtherLong>0.00000</AABRP-OtherLong>\n" +
                    "<AABRP-OtherNet>0.00000</AABRP-OtherNet>\n" +
                    "<AABRP-OtherShort>0.00000</AABRP-OtherShort>\n" +
                    "<AABRP-PortfolioDate>2021-06-30</AABRP-PortfolioDate>\n" +
                    "<AABRP-PreferredStockLong>0.00000</AABRP-PreferredStockLong>\n" +
                    "<AABRP-PreferredStockNet>0.00000</AABRP-PreferredStockNet>\n" +
                    "<AABRP-PreferredStockShort>0.00000</AABRP-PreferredStockShort>\n" +
                    "<REBRP-EquityRegionAfrica>0.000</REBRP-EquityRegionAfrica>\n" +
                    "<REBRP-EquityRegionAsiadevLongRescaled>1.826</REBRP-EquityRegionAsiadevLongRescaled>\n" +
                    "<REBRP-EquityRegionAsiaemrgLongRescaled>0.000</REBRP-EquityRegionAsiaemrgLongRescaled>\n" +
                    "<REBRP-EquityRegionAustralasiaLongRescaled>0.000</REBRP-EquityRegionAustralasiaLongRescaled>\n" +
                    "<REBRP-EquityRegionCanada>0.000</REBRP-EquityRegionCanada>\n" +
                    "<REBRP-EquityRegionDevelopedLongRescaled>100.000</REBRP-EquityRegionDevelopedLongRescaled>\n" +
                    "<REBRP-EquityRegionEmergingLongRescaled>0.000</REBRP-EquityRegionEmergingLongRescaled>\n" +
                    "<REBRP-EquityRegionEuropeemrgLongRescaled>0.000</REBRP-EquityRegionEuropeemrgLongRescaled>\n" +
                    "<REBRP-EquityRegionEuropeexeuro>21.336</REBRP-EquityRegionEuropeexeuro>\n" +
                    "<REBRP-EquityRegionEurozone>75.240</REBRP-EquityRegionEurozone>\n" +
                    "<REBRP-EquityRegionJapanLongRescaled>0.000</REBRP-EquityRegionJapanLongRescaled>\n" +
                    "<REBRP-EquityRegionLatinAmericaLongRescaled>0.000</REBRP-EquityRegionLatinAmericaLongRescaled>\n" +
                    "<REBRP-EquityRegionMiddleEast>0.000</REBRP-EquityRegionMiddleEast>\n" +
                    "<REBRP-EquityRegionNotClassifiedLongRescaled>0.000</REBRP-EquityRegionNotClassifiedLongRescaled>\n" +
                    "<REBRP-EquityRegionUnitedKingdomLongRescaled>1.307</REBRP-EquityRegionUnitedKingdomLongRescaled>\n" +
                    "<REBRP-EquityRegionUnitedStates>0.291</REBRP-EquityRegionUnitedStates>\n" +
                    "<REBRP-PortfolioDate>2021-06-30</REBRP-PortfolioDate>\n" +
                    "<GSSBRP-EquitySectorBasicMaterialsLongRescaled>11.05086</GSSBRP-EquitySectorBasicMaterialsLongRescaled>\n" +
                    "<GSSBRP-EquitySectorCommunicationServicesLongRescaled>0.29087</GSSBRP-EquitySectorCommunicationServicesLongRescaled>\n" +
                    "<GSSBRP-EquitySectorConsumerCyclicalLongRescaled>15.64947</GSSBRP-EquitySectorConsumerCyclicalLongRescaled>\n" +
                    "<GSSBRP-EquitySectorConsumerDefensiveLongRescaled>2.42019</GSSBRP-EquitySectorConsumerDefensiveLongRescaled>\n" +
                    "<GSSBRP-EquitySectorEnergyLongRescaled>1.75289</GSSBRP-EquitySectorEnergyLongRescaled>\n" +
                    "<GSSBRP-EquitySectorFinancialServicesLongRescaled>15.28833</GSSBRP-EquitySectorFinancialServicesLongRescaled>\n" +
                    "<GSSBRP-EquitySectorHealthcareLongRescaled>6.54404</GSSBRP-EquitySectorHealthcareLongRescaled>\n" +
                    "<GSSBRP-EquitySectorIndustrialsLongRescaled>20.83986</GSSBRP-EquitySectorIndustrialsLongRescaled>\n" +
                    "<GSSBRP-EquitySectorRealEstateLongRescaled>3.11890</GSSBRP-EquitySectorRealEstateLongRescaled>\n" +
                    "<GSSBRP-EquitySectorTechnologyLongRescaled>21.51200</GSSBRP-EquitySectorTechnologyLongRescaled>\n" +
                    "<GSSBRP-EquitySectorUtilitiesLongRescaled>1.53258</GSSBRP-EquitySectorUtilitiesLongRescaled>\n" +
                    "<GSSBRP-GlobalSectorPortfolioDate>2021-06-30</GSSBRP-GlobalSectorPortfolioDate>\n" +
                    "<GBSRP-FixedIncPrimarySectorAgencyMortgage-BackedLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorAgencyMortgage-BackedLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorAssetBackedLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorAssetBackedLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorBankLoanLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorBankLoanLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorCashEquivalentsLongRescaled>40.55283</GBSRP-FixedIncPrimarySectorCashEquivalentsLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorCommercialMortgageBackedLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorCommercialMortgageBackedLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorConvertibleLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorConvertibleLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorCorporateBondLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorCorporateBondLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorCoveredBondLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorCoveredBondLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorForwardFutureLongRescaled>59.44717</GBSRP-FixedIncPrimarySectorForwardFutureLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorGovernmentLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorGovernmentLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorGovernmentRelatedLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorGovernmentRelatedLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorMunicipalTaxExemptLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorMunicipalTaxExemptLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorMunicipalTaxableLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorMunicipalTaxableLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorNonAgencyResidentialMortgageBackedLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorNonAgencyResidentialMortgageBackedLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorOptionWarrantLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorOptionWarrantLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorPreferredStockLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorPreferredStockLongRescaled>\n" +
                    "<GBSRP-FixedIncPrimarySectorSwapLongRescaled>0.00000</GBSRP-FixedIncPrimarySectorSwapLongRescaled>\n" +
                    "<GBSRP-FixedIncSuperSectorCashEquivalentsLongRescaled>40.55283</GBSRP-FixedIncSuperSectorCashEquivalentsLongRescaled>\n" +
                    "<GBSRP-FixedIncSuperSectorCorporateLongRescaled>0.00000</GBSRP-FixedIncSuperSectorCorporateLongRescaled>\n" +
                    "<GBSRP-FixedIncSuperSectorDerivativeLongRescaled>59.44717</GBSRP-FixedIncSuperSectorDerivativeLongRescaled>\n" +
                    "<GBSRP-FixedIncSuperSectorGovernmentLongRescaled>0.00000</GBSRP-FixedIncSuperSectorGovernmentLongRescaled>\n" +
                    "<GBSRP-FixedIncSuperSectorMunicipalLongRescaled>0.00000</GBSRP-FixedIncSuperSectorMunicipalLongRescaled>\n" +
                    "<GBSRP-FixedIncSuperSectorSecuritizedLongRescaled>0.00000</GBSRP-FixedIncSuperSectorSecuritizedLongRescaled>\n" +
                    "<GBSRP-PortfolioDate>2021-06-30</GBSRP-PortfolioDate>\n" +
                    "<PSRP-NumberOfBondHoldings>0</PSRP-NumberOfBondHoldings>\n" +
                    "<PSRP-NumberOfStockHoldings>77</PSRP-NumberOfStockHoldings>\n" +
                    "<T10H-Holdings>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>E</HoldingType>\n" +
                    "<Name>Nordex SE</Name>\n" +
                    "<CountryId>DEU</CountryId>\n" +
                    "<Country>Germany</Country>\n" +
                    "<CurrencyId>EUR</CurrencyId>\n" +
                    "<Currency>Euro</Currency>\n" +
                    "<ISIN>DE000A0D6554</ISIN>\n" +
                    "<Weighting>3.19123</Weighting>\n" +
                    "<NumberOfShare>916622</NumberOfShare>\n" +
                    "<MarketValue>18634925</MarketValue>\n" +
                    "<ShareChange>49551</ShareChange>\n" +
                    "<SectorId>310</SectorId>\n" +
                    "<Sector>Industrials</Sector>\n" +
                    "<GlobalSectorId>310</GlobalSectorId>\n" +
                    "<GlobalSector>Industrials</GlobalSector>\n" +
                    "<Ticker>NDX1</Ticker>\n" +
                    "</HoldingDetail>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>E</HoldingType>\n" +
                    "<Name>SkiStar AB Class B</Name>\n" +
                    "<CountryId>SWE</CountryId>\n" +
                    "<Country>Sweden</Country>\n" +
                    "<CurrencyId>SEK</CurrencyId>\n" +
                    "<Currency>Swedish Krona</Currency>\n" +
                    "<ISIN>SE0012141687</ISIN>\n" +
                    "<Weighting>3.18943</Weighting>\n" +
                    "<NumberOfShare>1309384</NumberOfShare>\n" +
                    "<MarketValue>18624382</MarketValue>\n" +
                    "<ShareChange>19703</ShareChange>\n" +
                    "<SectorId>102</SectorId>\n" +
                    "<Sector>Consumer Cyclical</Sector>\n" +
                    "<GlobalSectorId>102</GlobalSectorId>\n" +
                    "<GlobalSector>Consumer Cyclical</GlobalSector>\n" +
                    "<Ticker>SKIS B</Ticker>\n" +
                    "</HoldingDetail>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>E</HoldingType>\n" +
                    "<Name>Signify NV</Name>\n" +
                    "<CountryId>NLD</CountryId>\n" +
                    "<Country>Netherlands</Country>\n" +
                    "<CurrencyId>EUR</CurrencyId>\n" +
                    "<Currency>Euro</Currency>\n" +
                    "<ISIN>NL0011821392</ISIN>\n" +
                    "<Weighting>2.81401</Weighting>\n" +
                    "<NumberOfShare>310216</NumberOfShare>\n" +
                    "<MarketValue>16432142</MarketValue>\n" +
                    "<ShareChange>-79243</ShareChange>\n" +
                    "<SectorId>310</SectorId>\n" +
                    "<Sector>Industrials</Sector>\n" +
                    "<GlobalSectorId>310</GlobalSectorId>\n" +
                    "<GlobalSector>Industrials</GlobalSector>\n" +
                    "<Ticker>LIGHT</Ticker>\n" +
                    "</HoldingDetail>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>E</HoldingType>\n" +
                    "<Name>Bankinter SA</Name>\n" +
                    "<CountryId>ESP</CountryId>\n" +
                    "<Country>Spain</Country>\n" +
                    "<CurrencyId>EUR</CurrencyId>\n" +
                    "<Currency>Euro</Currency>\n" +
                    "<ISIN>ES0113679I37</ISIN>\n" +
                    "<Weighting>2.69708</Weighting>\n" +
                    "<NumberOfShare>3718854</NumberOfShare>\n" +
                    "<MarketValue>15749347</MarketValue>\n" +
                    "<ShareChange>863905</ShareChange>\n" +
                    "<SectorId>103</SectorId>\n" +
                    "<Sector>Financial Services</Sector>\n" +
                    "<GlobalSectorId>103</GlobalSectorId>\n" +
                    "<GlobalSector>Financial Services</GlobalSector>\n" +
                    "<Ticker>BKT</Ticker>\n" +
                    "</HoldingDetail>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>E</HoldingType>\n" +
                    "<Name>TKH Group NV</Name>\n" +
                    "<CountryId>NLD</CountryId>\n" +
                    "<Country>Netherlands</Country>\n" +
                    "<CurrencyId>EUR</CurrencyId>\n" +
                    "<Currency>Euro</Currency>\n" +
                    "<ISIN>NL0000852523</ISIN>\n" +
                    "<Weighting>2.61809</Weighting>\n" +
                    "<NumberOfShare>362192</NumberOfShare>\n" +
                    "<MarketValue>15288124</MarketValue>\n" +
                    "<ShareChange>-14944</ShareChange>\n" +
                    "<SectorId>311</SectorId>\n" +
                    "<Sector>Technology</Sector>\n" +
                    "<GlobalSectorId>311</GlobalSectorId>\n" +
                    "<GlobalSector>Technology</GlobalSector>\n" +
                    "<Ticker>TWEKA</Ticker>\n" +
                    "</HoldingDetail>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>E</HoldingType>\n" +
                    "<Name>Peugeot Invest</Name>\n" +
                    "<CountryId>FRA</CountryId>\n" +
                    "<Country>France</Country>\n" +
                    "<CurrencyId>EUR</CurrencyId>\n" +
                    "<Currency>Euro</Currency>\n" +
                    "<ISIN>FR0000064784</ISIN>\n" +
                    "<Weighting>2.57171</Weighting>\n" +
                    "<NumberOfShare>131155</NumberOfShare>\n" +
                    "<MarketValue>15017248</MarketValue>\n" +
                    "<ShareChange>0</ShareChange>\n" +
                    "<SectorId>103</SectorId>\n" +
                    "<Sector>Financial Services</Sector>\n" +
                    "<GlobalSectorId>103</GlobalSectorId>\n" +
                    "<GlobalSector>Financial Services</GlobalSector>\n" +
                    "<Ticker>PEUG</Ticker>\n" +
                    "</HoldingDetail>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>E</HoldingType>\n" +
                    "<Name>De'Longhi SPA</Name>\n" +
                    "<CountryId>ITA</CountryId>\n" +
                    "<Country>Italy</Country>\n" +
                    "<CurrencyId>EUR</CurrencyId>\n" +
                    "<Currency>Euro</Currency>\n" +
                    "<ISIN>IT0003115950</ISIN>\n" +
                    "<Weighting>2.43965</Weighting>\n" +
                    "<NumberOfShare>388284</NumberOfShare>\n" +
                    "<MarketValue>14246140</MarketValue>\n" +
                    "<ShareChange>0</ShareChange>\n" +
                    "<SectorId>102</SectorId>\n" +
                    "<Sector>Consumer Cyclical</Sector>\n" +
                    "<GlobalSectorId>102</GlobalSectorId>\n" +
                    "<GlobalSector>Consumer Cyclical</GlobalSector>\n" +
                    "<Ticker>DLG</Ticker>\n" +
                    "</HoldingDetail>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>E</HoldingType>\n" +
                    "<Name>Elekta AB Class B</Name>\n" +
                    "<CountryId>SWE</CountryId>\n" +
                    "<Country>Sweden</Country>\n" +
                    "<CurrencyId>SEK</CurrencyId>\n" +
                    "<Currency>Swedish Krona</Currency>\n" +
                    "<ISIN>SE0000163628</ISIN>\n" +
                    "<Weighting>2.37903</Weighting>\n" +
                    "<NumberOfShare>1146440</NumberOfShare>\n" +
                    "<MarketValue>13892155</MarketValue>\n" +
                    "<ShareChange>54205</ShareChange>\n" +
                    "<SectorId>206</SectorId>\n" +
                    "<Sector>Healthcare</Sector>\n" +
                    "<GlobalSectorId>206</GlobalSectorId>\n" +
                    "<GlobalSector>Healthcare</GlobalSector>\n" +
                    "<Ticker>EKTA B</Ticker>\n" +
                    "</HoldingDetail>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>E</HoldingType>\n" +
                    "<Name>Also Holding AG</Name>\n" +
                    "<CountryId>CHE</CountryId>\n" +
                    "<Country>Switzerland</Country>\n" +
                    "<CurrencyId>CHF</CurrencyId>\n" +
                    "<Currency>Swiss Franc</Currency>\n" +
                    "<ISIN>CH0024590272</ISIN>\n" +
                    "<Weighting>2.21069</Weighting>\n" +
                    "<NumberOfShare>53291</NumberOfShare>\n" +
                    "<MarketValue>12909135</MarketValue>\n" +
                    "<ShareChange>0</ShareChange>\n" +
                    "<SectorId>311</SectorId>\n" +
                    "<Sector>Technology</Sector>\n" +
                    "<GlobalSectorId>311</GlobalSectorId>\n" +
                    "<GlobalSector>Technology</GlobalSector>\n" +
                    "<Ticker>ALSN</Ticker>\n" +
                    "</HoldingDetail>\n" +
                    "<HoldingDetail>\n" +
                    "<HoldingType>E</HoldingType>\n" +
                    "<Name>Wienerberger AG</Name>\n" +
                    "<CountryId>AUT</CountryId>\n" +
                    "<Country>Austria</Country>\n" +
                    "<CurrencyId>EUR</CurrencyId>\n" +
                    "<Currency>Euro</Currency>\n" +
                    "<ISIN>AT0000831706</ISIN>\n" +
                    "<Weighting>2.20976</Weighting>\n" +
                    "<NumberOfShare>396427</NumberOfShare>\n" +
                    "<MarketValue>12903699</MarketValue>\n" +
                    "<ShareChange>13359</ShareChange>\n" +
                    "<SectorId>101</SectorId>\n" +
                    "<Sector>Basic Materials</Sector>\n" +
                    "<GlobalSectorId>101</GlobalSectorId>\n" +
                    "<GlobalSector>Basic Materials</GlobalSector>\n" +
                    "<Ticker>WIE</Ticker>\n" +
                    "</HoldingDetail>\n" +
                    "</T10H-Holdings>\n" +
                    "<PSRP-PortfolioDate>2021-06-30</PSRP-PortfolioDate>\n" +
                    "<PS-PortfolioCurrencyId>EUR</PS-PortfolioCurrencyId>\n" +
                    "<IC-InvestmentStrategy>The Fund aims to achieve long-term capital growth. The Fund seeks to achieve its objective by investing primarily in listed equity and equity related securities of small cap companies throughout Europe excluding the United Kingdom. For the purposes of this investment policy, Europe is intended to include all countries in the European Union, Switzerland, Norway, Turkey and the members of the Commonwealth of Independent States</IC-InvestmentStrategy>\n" +
                    "<IC-MultilingualInvestmentStrategies>\n" +
                    "<MultilingualStrategy>\n" +
                    "<NarrativeLanguageId>ZH</NarrativeLanguageId>\n" +
                    "<NarrativeLanguageName>Chinese Simplified</NarrativeLanguageName>\n" +
                    "<InvestmentStrategy>本基金的目标是达致长期资本增值。本基金寻求透过主要投资于在欧洲各国（但不包括英国）的小型公司的上市股票及股票相关证券，以达致其目标。就本基金投资政策而言，欧洲包括欧盟国家（英国除外）、瑞士、挪威、土耳其及独立国联合体成员国。本基金可将不超过30%的资产净值投资于现金与等同现金、货币市场工具及不符合上述规定的公司股票及股票相关证券、或可转换证券。本基金可将不超过10%的资产净值投资于认股权证。直至任何独立国联合体成员国拥有受规管市场，本基金仅可将不超过10%的资产投资于相关国家。</InvestmentStrategy>\n" +
                    "</MultilingualStrategy>\n" +
                    "<MultilingualStrategy>\n" +
                    "<NarrativeLanguageId>ZH</NarrativeLanguageId>\n" +
                    "<NarrativeLanguageName>Chinese Traditional</NarrativeLanguageName>\n" +
                    "<InvestmentStrategy>本基金的目標是達致長期資本增值。 本基金尋求透過主要投資於在歐洲各國（但不包括英國）的小 型公司的上市股票及股票相關證券，以達致其目標。 就本基金投資政策而言，歐洲包括歐盟國家（英國除外）、瑞 士、挪威、土耳其及獨立國聯合體成員國。 本基金可將不超過30%的資產淨值投資於現金與等同現金、貨 幣市場工具及不符合上述規定的公司股票及股票相關證券、或 可轉換證券。 本基金可將不超過10%的資產淨值投資於認股權證。 直至任何獨立國聯合體成員國擁有受規管市場，本基金僅可將 不超過10%的資產投資於相關國家。</InvestmentStrategy>\n" +
                    "</MultilingualStrategy>\n" +
                    "</IC-MultilingualInvestmentStrategies>\n" +
                    "<TS-NAV52wkHigh>392.84000</TS-NAV52wkHigh>\n" +
                    "<TS-NAV52wkLow>232.84000</TS-NAV52wkLow>\n" +
                    "<DP-Dividend>0.713600000</DP-Dividend>\n" +
                    "<DP-DividendDate>2020-03-02</DP-DividendDate>\n" +
                    "<MR-Rating3Year>2</MR-Rating3Year>\n" +
                    "<MR-Rating5Year>2</MR-Rating5Year>\n" +
                    "<MR-Rating10Year>2</MR-Rating10Year>\n" +
                    "<MR-RatingDate>2021-06-30</MR-RatingDate>\n" +
                    "<MR-RatingOverall>2</MR-RatingOverall>\n" +
                    "<TTR-BestFitIndexReturnYTD>22.26043</TTR-BestFitIndexReturnYTD>\n" +
                    "<TTR-CategoryReturnYTD>16.69202</TTR-CategoryReturnYTD>\n" +
                    "<CYR-BestFitIndexYear1>28.51110</CYR-BestFitIndexYear1>\n" +
                    "<CYR-BestFitIndexYear2>32.37742</CYR-BestFitIndexYear2>\n" +
                    "<CYR-BestFitIndexYear3>-21.30471</CYR-BestFitIndexYear3>\n" +
                    "<CYR-BestFitIndexYear4>45.27625</CYR-BestFitIndexYear4>\n" +
                    "<CYR-BestFitIndexYear5>2.12086</CYR-BestFitIndexYear5>\n" +
                    "<CYR-BestFitIndexYear6>5.62198</CYR-BestFitIndexYear6>\n" +
                    "<CYR-BestFitIndexYear7>-27.98184</CYR-BestFitIndexYear7>\n" +
                    "<CYR-BestFitIndexYear8>25.09715</CYR-BestFitIndexYear8>\n" +
                    "<CYR-BestFitIndexYear9>17.48344</CYR-BestFitIndexYear9>\n" +
                    "<CYR-BestFitIndexYear10>-30.38300</CYR-BestFitIndexYear10>\n" +
                    "<CYR-CategoryYear1>14.54796</CYR-CategoryYear1>\n" +
                    "<CYR-CategoryYear2>26.15412</CYR-CategoryYear2>\n" +
                    "<CYR-CategoryYear3>-17.19764</CYR-CategoryYear3>\n" +
                    "<CYR-CategoryYear4>18.14959</CYR-CategoryYear4>\n" +
                    "<CYR-CategoryYear5>3.87670</CYR-CategoryYear5>\n" +
                    "<CYR-CategoryYear6>23.09109</CYR-CategoryYear6>\n" +
                    "<CYR-CategoryYear7>5.96333</CYR-CategoryYear7>\n" +
                    "<CYR-CategoryYear8>28.71490</CYR-CategoryYear8>\n" +
                    "<CYR-CategoryYear9>23.94601</CYR-CategoryYear9>\n" +
                    "<CYR-CategoryYear10>-17.12079</CYR-CategoryYear10>\n" +
                    "<TTR-BestFitIndexReturn1Mth>-4.91438</TTR-BestFitIndexReturn1Mth>\n" +
                    "<TTR-BestFitIndexReturn3Mth>4.30298</TTR-BestFitIndexReturn3Mth>\n" +
                    "<TTR-BestFitIndexReturn6Mth>22.26043</TTR-BestFitIndexReturn6Mth>\n" +
                    "<TTR-BestFitIndexReturn1Yr>74.47214</TTR-BestFitIndexReturn1Yr>\n" +
                    "<TTR-BestFitIndexReturn3Yr>19.68360</TTR-BestFitIndexReturn3Yr>\n" +
                    "<TTR-BestFitIndexReturn5Yr>21.25471</TTR-BestFitIndexReturn5Yr>\n" +
                    "<TTR-BestFitIndexReturn10Yr>6.34772</TTR-BestFitIndexReturn10Yr>\n" +
                    "<TTR-CategoryReturn1Mth>1.23980</TTR-CategoryReturn1Mth>\n" +
                    "<TTR-CategoryReturn3Mth>7.64780</TTR-CategoryReturn3Mth>\n" +
                    "<TTR-CategoryReturn6Mth>16.69202</TTR-CategoryReturn6Mth>\n" +
                    "<TTR-CategoryReturn1Yr>46.32537</TTR-CategoryReturn1Yr>\n" +
                    "<TTR-CategoryReturn3Yr>12.46921</TTR-CategoryReturn3Yr>\n" +
                    "<TTR-CategoryReturn6Yr>11.10135</TTR-CategoryReturn6Yr>\n" +
                    "<TTR-CategoryReturn10Yr>11.80585</TTR-CategoryReturn10Yr>\n" +
                    "<FM-Managers>\n" +
                    "<Manager>\n" +
                    "<Display>Display</Display>\n" +
                    "<ManagerId>121281</ManagerId>\n" +
                    "<Name>James Matthews</Name>\n" +
                    "<Role>Manager</Role>\n" +
                    "<StartDate>2020-12-31</StartDate>\n" +
                    "<Tenure>0.5</Tenure>\n" +
                    "</Manager>\n" +
                    "<Manager>\n" +
                    "<Display>Display</Display>\n" +
                    "<ManagerId>132247</ManagerId>\n" +
                    "<Name>Erik Esselink</Name>\n" +
                    "<Role>Manager</Role>\n" +
                    "<StartDate>2018-09-07</StartDate>\n" +
                    "<Tenure>2.81</Tenure>\n" +
                    "</Manager>\n" +
                    "</FM-Managers>\n" +
                    "<MPTPI-IndexName>MSCI Europe ex UK Small Cap NR EUR</MPTPI-IndexName>\n" +
                    "<DP-DayEndDate>2021-07-26</DP-DayEndDate>\n" +
                    "<TTR-MonthEndDate>2021-06-30</TTR-MonthEndDate>\n" +
                    "<DP-CategoryReturn1Mth>2.65379</DP-CategoryReturn1Mth>\n" +
                    "<DP-CategoryReturn3Mth>6.07273</DP-CategoryReturn3Mth>\n" +
                    "<DP-CategoryReturn6Mth>16.84651</DP-CategoryReturn6Mth>\n" +
                    "<DP-CategoryReturn1Yr>44.03490</DP-CategoryReturn1Yr>\n" +
                    "<DP-CategoryReturn3Yr>13.15880</DP-CategoryReturn3Yr>\n" +
                    "<DP-CategoryReturn5Yr>13.82077</DP-CategoryReturn5Yr>\n" +
                    "<DP-CategoryReturn10Yr>12.65277</DP-CategoryReturn10Yr>\n" +
                    "<DP-CategoryReturnYTD>19.01928</DP-CategoryReturnYTD>\n" +
                    "<FB-PrimaryProspectusBenchmarks>\n" +
                    "<PrimaryProspectusBenchmark>\n" +
                    "<IndexId>F00000IR43</IndexId>\n" +
                    "<IndexName>EMIX Smlr European Coms Ex UK TR EUR</IndexName>\n" +
                    "<Weighting>100</Weighting>\n" +
                    "</PrimaryProspectusBenchmark>\n" +
                    "</FB-PrimaryProspectusBenchmarks>\n" +
                    "<TTR-PrimaryIndexReturn1Mth>0.06513</TTR-PrimaryIndexReturn1Mth>\n" +
                    "<TTR-PrimaryIndexReturn3Mth>6.18946</TTR-PrimaryIndexReturn3Mth>\n" +
                    "<TTR-PrimaryIndexReturn6Mth>15.56976</TTR-PrimaryIndexReturn6Mth>\n" +
                    "<TTR-PrimaryIndexReturn1Yr>43.23795</TTR-PrimaryIndexReturn1Yr>\n" +
                    "<TTR-PrimaryIndexReturn3Yr>11.43347</TTR-PrimaryIndexReturn3Yr>\n" +
                    "<TTR-PrimaryIndexReturn5Yr>13.62265</TTR-PrimaryIndexReturn5Yr>\n" +
                    "<TTR-PrimaryIndexReturn10Yr>11.74286</TTR-PrimaryIndexReturn10Yr>\n" +
                    "<TTR-PrimaryIndexReturnYTD>15.56976</TTR-PrimaryIndexReturnYTD>\n" +
                    "<TS-DayEndNAVDate>2021-07-26</TS-DayEndNAVDate>\n" +
                    "<CYR-PrimaryIndexYear1>11.74391</CYR-PrimaryIndexYear1>\n" +
                    "<CYR-PrimaryIndexYear2>28.47400</CYR-PrimaryIndexYear2>\n" +
                    "<CYR-PrimaryIndexYear3>-15.78618</CYR-PrimaryIndexYear3>\n" +
                    "<CYR-PrimaryIndexYear4>20.31681</CYR-PrimaryIndexYear4>\n" +
                    "<CYR-PrimaryIndexYear5>5.46409</CYR-PrimaryIndexYear5>\n" +
                    "<CYR-PrimaryIndexYear6>24.99156</CYR-PrimaryIndexYear6>\n" +
                    "<CYR-PrimaryIndexYear7>5.94542</CYR-PrimaryIndexYear7>\n" +
                    "<CYR-PrimaryIndexYear8>33.50371</CYR-PrimaryIndexYear8>\n" +
                    "<CYR-PrimaryIndexYear9>22.75898</CYR-PrimaryIndexYear9>\n" +
                    "<CYR-PrimaryIndexYear10>-21.85858</CYR-PrimaryIndexYear10>\n" +
                    "<MPTPI-IndexID>F00000TXXV</MPTPI-IndexID>\n" +
                    "<FM2-Managers>\n" +
                    "<Manager>\n" +
                    "<Display>Display</Display>\n" +
                    "<ManagerId>121281</ManagerId>\n" +
                    "<Name>James Matthews</Name>\n" +
                    "<Role>Manager</Role>\n" +
                    "<StartDate>2020-12-31</StartDate>\n" +
                    "<Tenure>0.5</Tenure>\n" +
                    "</Manager>\n" +
                    "<Manager>\n" +
                    "<Display>Display</Display>\n" +
                    "<ManagerId>132247</ManagerId>\n" +
                    "<Name>Erik Esselink</Name>\n" +
                    "<Role>Manager</Role>\n" +
                    "<StartDate>2018-09-07</StartDate>\n" +
                    "<Tenure>2.81</Tenure>\n" +
                    "</Manager>\n" +
                    "</FM2-Managers>\n" +
                    "</api>\n" +
                    "<ProspectusPrimaryIndex _id=\"F00000IR43\">\n" +
                    "<ProspectusPrimaryIndexReturn1Mth>-0.15508</ProspectusPrimaryIndexReturn1Mth>\n" +
                    "<ProspectusPrimaryIndexReturn3Mth>6.38776</ProspectusPrimaryIndexReturn3Mth>\n" +
                    "<ProspectusPrimaryIndexReturn6Mth>15.53418</ProspectusPrimaryIndexReturn6Mth>\n" +
                    "<ProspectusPrimaryIndexReturn1Yr>44.57478</ProspectusPrimaryIndexReturn1Yr>\n" +
                    "<ProspectusPrimaryIndexReturn3Yr>12.40369</ProspectusPrimaryIndexReturn3Yr>\n" +
                    "<ProspectusPrimaryIndexReturn5Yr>14.12898</ProspectusPrimaryIndexReturn5Yr>\n" +
                    "<ProspectusPrimaryIndexReturn10Yr>11.48829</ProspectusPrimaryIndexReturn10Yr>\n" +
                    "<ProspectusPrimaryIndexReturnYTD>15.53418</ProspectusPrimaryIndexReturnYTD>\n" +
                    "<ProspectusPrimaryIndexYear1>12.54951</ProspectusPrimaryIndexYear1>\n" +
                    "<ProspectusPrimaryIndexYear2>27.77497</ProspectusPrimaryIndexYear2>\n" +
                    "<ProspectusPrimaryIndexYear3>-13.64362</ProspectusPrimaryIndexYear3>\n" +
                    "<ProspectusPrimaryIndexYear4>18.60031</ProspectusPrimaryIndexYear4>\n" +
                    "<ProspectusPrimaryIndexYear5>6.41036</ProspectusPrimaryIndexYear5>\n" +
                    "<ProspectusPrimaryIndexYear6>23.45090</ProspectusPrimaryIndexYear6>\n" +
                    "<ProspectusPrimaryIndexYear7>5.18233</ProspectusPrimaryIndexYear7>\n" +
                    "<ProspectusPrimaryIndexYear8>34.00025</ProspectusPrimaryIndexYear8>\n" +
                    "<ProspectusPrimaryIndexYear9>20.44487</ProspectusPrimaryIndexYear9>\n" +
                    "<ProspectusPrimaryIndexYear10>-21.81969</ProspectusPrimaryIndexYear10>\n" +
                    "</ProspectusPrimaryIndex>\n" +
                    "</data>\n" +
                    "</response>";
            return res;
        }
    }
}