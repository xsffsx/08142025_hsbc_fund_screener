package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.common.svc.httpclient.HttpConnectionManager;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.common.util.SiteFeature;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.AdvanceChartRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundQuoteSummaryRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.AdvanceChartResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.advanceChart.Result;
import com.hhhh.group.secwealth.mktdata.fund.util.MstarAccessCodeHelper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import static com.hhhh.group.secwealth.mktdata.fund.service.FundSearchResultServiceImplTest.findMethodWithAccess;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuoteSummaryServiceExecutorTest {
    @Mock
    private InternalProductKeyUtil internalProductKeyUtil;
    @Mock
    private JAXBContext dataClassJC;
    @Mock
    private HttpConnectionManager connManager;
    @Mock
    private MstarAccessCodeHelper accessCodeHelper;
    @InjectMocks
    private QuoteSummaryServiceExecutor underTest;
    @Mock
    private Map<String, String> bestFitIndexCalendarYearMap;
    @Mock
    private Map<String, String> fieldNeeddataPointNewapiMap;
    @Mock
    private SiteFeature siteFeature;
    @Mock
    private FundQuoteSummaryRequest request;
    @BeforeEach
    void setup() throws Exception {
        when(request.getSiteKey()).thenReturn("hk_hbap");
        Mockito.when(request.getCountryCode()).thenReturn("HK");
        Mockito.when(request.getGroupMember()).thenReturn("HBAP");
        Mockito.when(bestFitIndexCalendarYearMap.get(anyString())).thenReturn("HBAP");
        Mockito.when(fieldNeeddataPointNewapiMap.get(anyString())).thenReturn("HBAP");
        Mockito.when(siteFeature.getStringDefaultFeature(anyString(),anyString())).thenReturn("fscbiLocalPhone");
        Mockito.when(request.getLocale()).thenReturn("en");
        SearchProduct searchProduct = new SearchProduct();
        searchProduct.setExternalKey("000000`");
        searchProduct.setSearchObject(new SearchableObject());
        Mockito.when(internalProductKeyUtil.getProductBySearchWithAltClassCde(any(), any(), any(), any(), any(), any(), any())).thenReturn(searchProduct);
        Mockito.when(request.getProductType()).thenReturn("UT");
        Unmarshaller un = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.QuoteSummaryData")).createUnmarshaller();
        when(dataClassJC.createUnmarshaller()).thenReturn(un);
        String res = buildRes();
        when(connManager.sendRequest(any(String.class),any(String.class),any())).thenReturn(res);

    }

    @Test
    void execute() throws Exception {
        Assert.assertNotNull(underTest.execute(request));
    }
    public static String buildRes(){
        String res = "<response>\n" +
                "\t<status>\n" +
                "\t\t<code>0</code>\n" +
                "\t\t<message>OK</message>\n" +
                "\t</status>\n" +
                "\t<data _idtype=\"mstarid\" _id=\"0P000019TO\">\n" +
                "\t\t<api _id=\"pqzxwl5qhgisju94\">\n" +
                "\t\t\t<TS-NAV52wkLow>6.13000</TS-NAV52wkLow>\n" +
                "\t\t\t<TS-NAV52wkHigh>6.74000</TS-NAV52wkHigh>\n" +
                "\t\t\t<CYR-Year1>-13.14400</CYR-Year1>\n" +
                "\t\t\t<CYR-BestFitIndexYear1>-14.11083</CYR-BestFitIndexYear1>\n" +
                "\t\t\t<CYR-CategoryYear1>-9.57494</CYR-CategoryYear1>\n" +
                "\t\t\t<MPTPI-IndexName>Bloomberg US Agg Bond TR USD</MPTPI-IndexName>\n" +
                "\t\t\t<MPTBF-IndexID>F00000M90J</MPTBF-IndexID>\n" +
                "\t\t\t<DP-CategoryCode>EUCA000775</DP-CategoryCode>\n" +
                "\t\t\t<DP-CategoryName>USD Flexible Bond</DP-CategoryName>\n" +
                "\t\t\t<FSCBI-ProviderCompanyName>AllianceBernstein (Luxembourg) S.à r.l.</FSCBI-ProviderCompanyName>\n" +
                "\t\t\t<AMCB-AdministratorCompanies>\n" +
                "\t\t\t\t<AdministratorCompany>\n" +
                "\t\t\t\t\t<CompanyID>0C00001SZ2</CompanyID>\n" +
                "\t\t\t\t\t<CompanyName>Brown Brothers Harriman (Lux) SCA</CompanyName>\n" +
                "\t\t\t\t\t<CompanyCity>Luxembourg</CompanyCity>\n" +
                "\t\t\t\t\t<CompanyCountry>LUX</CompanyCountry>\n" +
                "\t\t\t\t\t<CompanyPostalCode>L-1470</CompanyPostalCode>\n" +
                "\t\t\t\t\t<CompanyAddress>Brown Brothers Harriman (Luxembourg) S.C.A.,80, route d’Esch</CompanyAddress>\n" +
                "\t\t\t\t\t<CompanyFax>Luxembourg</CompanyFax>\n" +
                "\t\t\t\t</AdministratorCompany>\n" +
                "\t\t\t</AMCB-AdministratorCompanies>\n" +
                "\t\t\t<KD-InceptionDate>2002-09-16</KD-InceptionDate>\n" +
                "\t\t\t<IC-InvestmentStrategy>The primary investment objective of the Portfolio is to seek to provide a high level of current income consistent with preservation of capital. The Portfolio seeks to meet its investment objective by investing in a diversified portfolio of fixed-income securities. As a secondary objective, the Portfolio will seek to increase its capital through appreciation of its investments in order to preserve and, if possible, increase the purchasing power of each Shareholder's investment.</IC-InvestmentStrategy>\n" +
                "\t\t\t<IC-MultilingualInvestmentStrategies>\n" +
                "\t\t\t\t<MultilingualStrategy>\n" +
                "\t\t\t\t\t<NarrativeLanguageId>KO</NarrativeLanguageId>\n" +
                "\t\t\t\t\t<NarrativeLanguageName>Korean</NarrativeLanguageName>\n" +
                "\t\t\t\t\t<InvestmentStrategy>포트폴리오의 주요 투자 목적은 투자원금의 보전과 함께 높은 수준의 현재수익을 추구하는데 있습니다. 포트폴리오는 채권의 분산된 포트폴리오에 투자함으로써 투자목적을 이루고자 합니다. 부차적인 목적으로서 포트폴리오는 각 수익자의 투자자산을 보전하고 가능하면 이들의 구매력을 증가시키기 위하여 투자자산의 가격상승을 통한 자본증식을 추구하고자 합니다. 포트폴리오는 옵션, 선물 및 통화거래를 통하여 금리 및 통화 위험을 헷지할 수 있습니다</InvestmentStrategy>\n" +
                "\t\t\t\t</MultilingualStrategy>\n" +
                "\t\t\t</IC-MultilingualInvestmentStrategies>\n" +
                "\t\t\t<FNA-AsOfOriginalReportedCurrencyId>USD</FNA-AsOfOriginalReportedCurrencyId>\n" +
                "\t\t\t<NA-ShareClassNetAssets>5203155064.65</NA-ShareClassNetAssets>\n" +
                "\t\t\t<PS-TurnoverRatio>25.79</PS-TurnoverRatio>\n" +
                "\t\t\t<PS-EquityStylebox>8</PS-EquityStylebox>\n" +
                "\t\t\t<PS-FixedIncomeStylebox>5</PS-FixedIncomeStylebox>\n" +
                "\t\t\t<DMP-SharesOutstanding>849301999.57000</DMP-SharesOutstanding>\n" +
                "\t\t\t<FSCBI-CurrencyId>CU$$$$$USD</FSCBI-CurrencyId>\n" +
                "\t\t\t<DP-DayEndNAV>6.13000</DP-DayEndNAV>\n" +
                "\t\t\t<YLD-Yield1Yr>6.36656</YLD-Yield1Yr>\n" +
                "\t\t\t<DP-Dividend>0.033000000</DP-Dividend>\n" +
                "\t\t\t<DP-Currency>US Dollar</DP-Currency>\n" +
                "\t\t\t<DP-DividendDate>2023-09-29</DP-DividendDate>\n" +
                "\t\t\t<RMBAC-Beta3Yr>1.03</RMBAC-Beta3Yr>\n" +
                "\t\t\t<PI-MinimumInitial>2000</PI-MinimumInitial>\n" +
                "\t\t\t<PI-MinimumSubsequent>750</PI-MinimumSubsequent>\n" +
                "\t\t\t<PI-PurchaseCurrencyId>CU$$$$$USD</PI-PurchaseCurrencyId>\n" +
                "\t\t\t<PI-MinimumInitialUnit>Monetary</PI-MinimumInitialUnit>\n" +
                "\t\t\t<PI-MinimumSubsequentUnit>Monetary</PI-MinimumSubsequentUnit>\n" +
                "\t\t\t<LS-MaximumFrontLoad>5.00000</LS-MaximumFrontLoad>\n" +
                "\t\t\t<PF-ActualManagementFee>1.10000</PF-ActualManagementFee>\n" +
                "\t\t\t<ARF-AnnualReportDate>2022-08-31</ARF-AnnualReportDate>\n" +
                "\t\t\t<MR-RatingOverall>3</MR-RatingOverall>\n" +
                "\t\t\t<MR-Rating3Year>2</MR-Rating3Year>\n" +
                "\t\t\t<MR-Rating5Year>2</MR-Rating5Year>\n" +
                "\t\t\t<MR-Rating10Year>3</MR-Rating10Year>\n" +
                "\t\t\t<MR-RatingDate>2023-09-30</MR-RatingDate>\n" +
                "\t\t\t<AMCB-DistributorCompanies>\n" +
                "\t\t\t\t<DistributorCompany>\n" +
                "\t\t\t\t\t<CompanyID>0C00008UDV</CompanyID>\n" +
                "\t\t\t\t\t<CompanyName>AllianceBernstein Investments</CompanyName>\n" +
                "\t\t\t\t\t<CompanyCity>Luxembourg</CompanyCity>\n" +
                "\t\t\t\t\t<CompanyCountry>LUX</CompanyCountry>\n" +
                "\t\t\t\t\t<CompanyPostalCode>L-2453</CompanyPostalCode>\n" +
                "\t\t\t\t\t<CompanyAddress>2-4, rue Eugene Ruppert</CompanyAddress>\n" +
                "\t\t\t\t\t<CompanyFax>Luxembourg</CompanyFax>\n" +
                "\t\t\t\t</DistributorCompany>\n" +
                "\t\t\t</AMCB-DistributorCompanies>\n" +
                "\t\t\t<FSCBI-ProviderCompanyWebsite>www.abglobal.com</FSCBI-ProviderCompanyWebsite>\n" +
                "\t\t\t<FM-Managers>\n" +
                "\t\t\t\t<Manager>\n" +
                "\t\t\t\t\t<Display>Display</Display>\n" +
                "\t\t\t\t\t<ManagerId>106563</ManagerId>\n" +
                "\t\t\t\t\t<Name>Scott DiMaggio</Name>\n" +
                "\t\t\t\t\t<Role>Manager</Role>\n" +
                "\t\t\t\t\t<StartDate>2018-10-01</StartDate>\n" +
                "\t\t\t\t\t<Tenure>5</Tenure>\n" +
                "\t\t\t\t</Manager>\n" +
                "\t\t\t</FM-Managers>\n" +
                "\t\t\t<PI-MinimumInitialInvestmentUnit>Monetary</PI-MinimumInitialInvestmentUnit>\n" +
                "\t\t\t<AMCB-ProviderCompanyCountryHeadquarters>\n" +
                "\t\t\t\t<CountryHeadquarters>\n" +
                "\t\t\t\t\t<CountryId>LUX</CountryId>\n" +
                "\t\t\t\t\t<CountryName>Luxembourg</CountryName>\n" +
                "\t\t\t\t\t<PrimaryHeadquarter>true</PrimaryHeadquarter>\n" +
                "\t\t\t\t\t<CompanyAddress>18, rue Eugene Ruppert</CompanyAddress>\n" +
                "\t\t\t\t\t<CompanyCity>Luxembourg</CompanyCity>\n" +
                "\t\t\t\t\t<CompanyPostalCode>L-2453</CompanyPostalCode>\n" +
                "\t\t\t\t\t<CompanyPhone>+352 46 39 36 151</CompanyPhone>\n" +
                "\t\t\t\t</CountryHeadquarters>\n" +
                "\t\t\t</AMCB-ProviderCompanyCountryHeadquarters>\n" +
                "\t\t\t<MPTPI-IndexID>XIUSA000MC</MPTPI-IndexID>\n" +
                "\t\t\t<CYR-PrimaryIndexYear1>-13.01013</CYR-PrimaryIndexYear1>\n" +
                "\t\t\t<LS-ProspectusCustodianFee>\n" +
                "\t\t\t\t<FeeSchedule>\n" +
                "\t\t\t\t\t<LowBreakpoint>0</LowBreakpoint>\n" +
                "\t\t\t\t\t<BreakpointUnit>Monetary</BreakpointUnit>\n" +
                "\t\t\t\t\t<Unit>Percentage</Unit>\n" +
                "\t\t\t\t\t<Value>0.50000</Value>\n" +
                "\t\t\t\t</FeeSchedule>\n" +
                "\t\t\t</LS-ProspectusCustodianFee>\n" +
                "\t\t\t<TTR-ReturnYTD>0.81162</TTR-ReturnYTD>\n" +
                "\t\t\t<TTR-BestFitIndexReturnYTD>1.45759</TTR-BestFitIndexReturnYTD>\n" +
                "\t\t\t<TTR-CategoryReturnYTD>1.14695</TTR-CategoryReturnYTD>\n" +
                "\t\t\t<AABRP-AssetAllocBondNet>137.46392</AABRP-AssetAllocBondNet>\n" +
                "\t\t\t<AABRP-PreferredStockNet>1.28904</AABRP-PreferredStockNet>\n" +
                "\t\t\t<AABRP-AssetAllocCashNet>-39.35899</AABRP-AssetAllocCashNet>\n" +
                "\t\t\t<AABRP-OtherNet>0.42850</AABRP-OtherNet>\n" +
                "\t\t\t<AABRP-AssetAllocEquityNet>0.17722</AABRP-AssetAllocEquityNet>\n" +
                "\t\t\t<CQBRP-CreditQualA>6.56000</CQBRP-CreditQualA>\n" +
                "\t\t\t<CQBRP-CreditQualAA>0.54000</CQBRP-CreditQualAA>\n" +
                "\t\t\t<CQBRP-CreditQualAAA>37.59000</CQBRP-CreditQualAAA>\n" +
                "\t\t\t<CQBRP-CreditQualB>9.27000</CQBRP-CreditQualB>\n" +
                "\t\t\t<CQBRP-CreditQualBB>20.46000</CQBRP-CreditQualBB>\n" +
                "\t\t\t<CQBRP-CreditQualBBB>24.50000</CQBRP-CreditQualBBB>\n" +
                "\t\t\t<CQBRP-CreditQualBelowB>0.59000</CQBRP-CreditQualBelowB>\n" +
                "\t\t\t<CQBRP-CreditQualNotRated>0.49000</CQBRP-CreditQualNotRated>\n" +
                "\t\t\t<CQBRP-CreditQualDate>2023-06-30</CQBRP-CreditQualDate>\n" +
                "\t\t\t<TTR-Return1Mth>-1.99060</TTR-Return1Mth>\n" +
                "\t\t\t<TTR-Return3Mth>-1.59578</TTR-Return3Mth>\n" +
                "\t\t\t<TTR-Return6Mth>-1.46133</TTR-Return6Mth>\n" +
                "\t\t\t<TTR-Return1Yr>4.80984</TTR-Return1Yr>\n" +
                "\t\t\t<TTR-Return3Yr>-3.21469</TTR-Return3Yr>\n" +
                "\t\t\t<TTR-Return5Yr>0.48899</TTR-Return5Yr>\n" +
                "\t\t\t<TTR-Return10Yr>1.78467</TTR-Return10Yr>\n" +
                "\t\t\t<TTR-MonthEndDate>2023-09-30</TTR-MonthEndDate>\n" +
                "\t\t\t<TTR-BestFitIndexReturn1Mth>-1.86856</TTR-BestFitIndexReturn1Mth>\n" +
                "\t\t\t<TTR-BestFitIndexReturn3Mth>-1.62532</TTR-BestFitIndexReturn3Mth>\n" +
                "\t\t\t<TTR-BestFitIndexReturn6Mth>-1.62812</TTR-BestFitIndexReturn6Mth>\n" +
                "\t\t\t<TTR-BestFitIndexReturn1Yr>4.61475</TTR-BestFitIndexReturn1Yr>\n" +
                "\t\t\t<TTR-BestFitIndexReturn3Yr>-3.86354</TTR-BestFitIndexReturn3Yr>\n" +
                "\t\t\t<TTR-BestFitIndexReturn5Yr>1.05824</TTR-BestFitIndexReturn5Yr>\n" +
                "\t\t\t<TTR-BestFitIndexReturn10Yr>2.40958</TTR-BestFitIndexReturn10Yr>\n" +
                "\t\t\t<TTR-CategoryReturn1Mth>-1.39881</TTR-CategoryReturn1Mth>\n" +
                "\t\t\t<TTR-CategoryReturn3Mth>-1.04383</TTR-CategoryReturn3Mth>\n" +
                "\t\t\t<TTR-CategoryReturn6Mth>-0.52041</TTR-CategoryReturn6Mth>\n" +
                "\t\t\t<TTR-CategoryReturn1Yr>4.32434</TTR-CategoryReturn1Yr>\n" +
                "\t\t\t<TTR-CategoryReturn3Yr>-1.78302</TTR-CategoryReturn3Yr>\n" +
                "\t\t\t<TTR-CategoryReturn5Yr>1.04994</TTR-CategoryReturn5Yr>\n" +
                "\t\t\t<TTR-CategoryReturn10Yr>1.68128</TTR-CategoryReturn10Yr>\n" +
                "\t\t\t<DP-Return1Mth>-2.79933</DP-Return1Mth>\n" +
                "\t\t\t<DP-Return3Mth>-3.91761</DP-Return3Mth>\n" +
                "\t\t\t<DP-Return6Mth>-3.03608</DP-Return6Mth>\n" +
                "\t\t\t<DP-ReturnYTD>-0.64707</DP-ReturnYTD>\n" +
                "\t\t\t<DP-Return1Yr>4.11833</DP-Return1Yr>\n" +
                "\t\t\t<DP-Return3Yr>-3.88138</DP-Return3Yr>\n" +
                "\t\t\t<DP-Return5Yr>0.32316</DP-Return5Yr>\n" +
                "\t\t\t<DP-Return10Yr>1.53176</DP-Return10Yr>\n" +
                "\t\t\t<DP-DayEndDate>2023-10-17</DP-DayEndDate>\n" +
                "\t\t\t<SR-StubYearEndReturnEndDate>2002-12-31</SR-StubYearEndReturnEndDate>\n" +
                "\t\t\t<SR-StubYearEndReturn>4.50188</SR-StubYearEndReturn>\n" +
                "\t\t\t<FB-PrimaryProspectusBenchmarks>\n" +
                "\t\t\t\t<PrimaryProspectusBenchmark>\n" +
                "\t\t\t\t\t<IndexId>XIUSA000MC</IndexId>\n" +
                "\t\t\t\t\t<IndexName>Bloomberg US Agg Bond TR USD</IndexName>\n" +
                "\t\t\t\t\t<Weighting>100</Weighting>\n" +
                "\t\t\t\t</PrimaryProspectusBenchmark>\n" +
                "\t\t\t</FB-PrimaryProspectusBenchmarks>\n" +
                "\t\t\t<DP-CategoryReturn1Mth>-1.50814</DP-CategoryReturn1Mth>\n" +
                "\t\t\t<DP-CategoryReturn3Mth>-2.24250</DP-CategoryReturn3Mth>\n" +
                "\t\t\t<DP-CategoryReturn6Mth>-1.28546</DP-CategoryReturn6Mth>\n" +
                "\t\t\t<DP-CategoryReturn1Yr>4.81307</DP-CategoryReturn1Yr>\n" +
                "\t\t\t<DP-CategoryReturn3Yr>-1.77225</DP-CategoryReturn3Yr>\n" +
                "\t\t\t<DP-CategoryReturn5Yr>1.17277</DP-CategoryReturn5Yr>\n" +
                "\t\t\t<DP-CategoryReturn10Yr>1.82484</DP-CategoryReturn10Yr>\n" +
                "\t\t\t<DP-CategoryReturnYTD>1.48743</DP-CategoryReturnYTD>\n" +
                "\t\t\t<TTR-PrimaryIndexReturn1Mth>-2.54117</TTR-PrimaryIndexReturn1Mth>\n" +
                "\t\t\t<TTR-PrimaryIndexReturn3Mth>-3.23118</TTR-PrimaryIndexReturn3Mth>\n" +
                "\t\t\t<TTR-PrimaryIndexReturn6Mth>-4.04821</TTR-PrimaryIndexReturn6Mth>\n" +
                "\t\t\t<TTR-PrimaryIndexReturn1Yr>0.64440</TTR-PrimaryIndexReturn1Yr>\n" +
                "\t\t\t<TTR-PrimaryIndexReturn3Yr>-5.20584</TTR-PrimaryIndexReturn3Yr>\n" +
                "\t\t\t<TTR-PrimaryIndexReturn5Yr>0.10259</TTR-PrimaryIndexReturn5Yr>\n" +
                "\t\t\t<TTR-PrimaryIndexReturn10Yr>1.12650</TTR-PrimaryIndexReturn10Yr>\n" +
                "\t\t\t<TTR-PrimaryIndexReturnYTD>-1.20609</TTR-PrimaryIndexReturnYTD>\n" +
                "\t\t\t<TS-DayEndBidOfferPricesDate>2023-10-17</TS-DayEndBidOfferPricesDate>\n" +
                "\t\t\t<TS-DayEndNAVDate>2023-10-17</TS-DayEndNAVDate>\n" +
                "\t\t\t<FM2-Managers>\n" +
                "\t\t\t\t<Manager>\n" +
                "\t\t\t\t\t<Display>Display</Display>\n" +
                "\t\t\t\t\t<ManagerId>106563</ManagerId>\n" +
                "\t\t\t\t\t<Name>Scott A. DiMaggio</Name>\n" +
                "\t\t\t\t\t<Role>Manager</Role>\n" +
                "\t\t\t\t\t<StartDate>2018-10-01</StartDate>\n" +
                "\t\t\t\t\t<Tenure>5</Tenure>\n" +
                "\t\t\t\t</Manager>\n" +
                "\t\t\t</FM2-Managers>\n" +
                "\t\t\t<AT-ActualFrontLoad>6.25000</AT-ActualFrontLoad>\n" +
                "\t\t\t<FNA-AsOfOriginalReported>20315786568.03</FNA-AsOfOriginalReported>\n" +
                "\t\t\t<MPTBF-IndexName>Bloomberg Gbl Agg Corp 0901 TR Hdg USD</MPTBF-IndexName>\n" +
                "\t\t</api>\n" +
                "\t\t<ProspectusPrimaryIndex _id=\"XIUSA000MC\">\n" +
                "\t\t\t<ProspectusPrimaryIndexReturn1Mth>-2.54117</ProspectusPrimaryIndexReturn1Mth>\n" +
                "\t\t\t<ProspectusPrimaryIndexReturn3Mth>-3.23118</ProspectusPrimaryIndexReturn3Mth>\n" +
                "\t\t\t<ProspectusPrimaryIndexReturn6Mth>-4.04821</ProspectusPrimaryIndexReturn6Mth>\n" +
                "\t\t\t<ProspectusPrimaryIndexReturn1Yr>0.64440</ProspectusPrimaryIndexReturn1Yr>\n" +
                "\t\t\t<ProspectusPrimaryIndexReturn3Yr>-5.20584</ProspectusPrimaryIndexReturn3Yr>\n" +
                "\t\t\t<ProspectusPrimaryIndexReturn5Yr>0.10259</ProspectusPrimaryIndexReturn5Yr>\n" +
                "\t\t\t<ProspectusPrimaryIndexReturn10Yr>1.12650</ProspectusPrimaryIndexReturn10Yr>\n" +
                "\t\t\t<ProspectusPrimaryIndexYear1>-13.01013</ProspectusPrimaryIndexYear1>\n" +
                "\t\t\t<ProspectusPrimaryIndexYear2>-1.54205</ProspectusPrimaryIndexYear2>\n" +
                "\t\t\t<ProspectusPrimaryIndexYear3>7.50664</ProspectusPrimaryIndexYear3>\n" +
                "\t\t\t<ProspectusPrimaryIndexYear4>8.71670</ProspectusPrimaryIndexYear4>\n" +
                "\t\t\t<ProspectusPrimaryIndexYear5>0.01137</ProspectusPrimaryIndexYear5>\n" +
                "\t\t\t<ProspectusPrimaryIndexYear6>3.54184</ProspectusPrimaryIndexYear6>\n" +
                "\t\t\t<ProspectusPrimaryIndexYear7>2.64748</ProspectusPrimaryIndexYear7>\n" +
                "\t\t\t<ProspectusPrimaryIndexYear8>0.54996</ProspectusPrimaryIndexYear8>\n" +
                "\t\t\t<ProspectusPrimaryIndexYear9>5.96569</ProspectusPrimaryIndexYear9>\n" +
                "\t\t\t<ProspectusPrimaryIndexYear10>-2.02370</ProspectusPrimaryIndexYear10>\n" +
                "\t\t\t<ProspectusPrimaryIndexReturnYTD>-1.20609</ProspectusPrimaryIndexReturnYTD>\n" +
                "\t\t</ProspectusPrimaryIndex>\n" +
                "\t</data>\n" +
                "</response>";
        return res;
    }
}