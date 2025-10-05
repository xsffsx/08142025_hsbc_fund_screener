package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.svc.httpclient.HttpConnectionManager;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.common.util.SiteFeature;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.TrailingReturnChartRequest;
import com.hhhh.group.secwealth.mktdata.fund.util.MstarAccessCodeHelper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrailingReturnChartServiceImplTest {

    @Mock
    private ConcurrentHashMap<String, String> countryCodeMap;
    @Mock
    private InternalProductKeyUtil internalProductKeyUtil;
    @Mock
    private SiteFeature siteFeature;
    @Mock
    private LocaleMappingUtil localeMappingUtil;
    @Mock
    private JAXBContext chartClassJC;

    @Mock
    private MstarAccessCodeHelper accessCodeHelper;
    @Mock
    private JAXBContext dataClassJC;
    @Mock
    private JAXBContext tableClassJC;

    @Mock
    private HttpConnectionManager connManager;

    @InjectMocks
    private TrailingReturnChartServiceImpl underTest;
    @Mock
    private TrailingReturnChartRequest request;

    @BeforeEach
    void setup() throws Exception {
        Field chartURL = underTest.getClass().getDeclaredField("chartURL");
        chartURL.setAccessible(true);
        chartURL.set(underTest,"https://eultrcqa.morningstar.com/api/rest.svc/timeseries_price");

        Field url = underTest.getClass().getDeclaredField("url");
        url.setAccessible(true);
        url.set(underTest,"https://eultrcqa.morningstar.com/api/rest.svc/timeseries_price");

        Field tableUrl = underTest.getClass().getDeclaredField("tableURL");
        tableUrl.setAccessible(true);
        tableUrl.set(underTest,"https://eultrcqa.morningstar.com/api/rest.svc/timeseries_price");
        Mockito.when(request.getCountryCode()).thenReturn("HK");
        Mockito.when(request.getGroupMember()).thenReturn("HBAP");
        Mockito.when(request.getLocale()).thenReturn("en");
        SearchProduct searchProduct = new SearchProduct();
        searchProduct.setExternalKey("000000`");
        searchProduct.setSearchObject(new SearchableObject());
        Mockito.when(internalProductKeyUtil.getProductBySearchWithAltClassCde(any(), any(), any(), any(), any(), any(), any())).thenReturn(searchProduct);
        Mockito.when(request.getProductType()).thenReturn("UT");
        Unmarshaller un = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.dayenddate.DayEndDateData")).createUnmarshaller();
        when(dataClassJC.createUnmarshaller()).thenReturn(un);
        Unmarshaller charun = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.trailingreturnschart.TrailingReturnsChartData")).createUnmarshaller();
        when(chartClassJC.createUnmarshaller()).thenReturn(charun);
        Unmarshaller tableun = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.bestwrostreturn.BestWrostReturnData")).createUnmarshaller();
        when(tableClassJC.createUnmarshaller()).thenReturn(tableun);
        String res = buildRes();
        when(connManager.sendRequest(any(String.class),any(String.class),any())).thenReturn(res);

    }
    @Test
    void execute() throws Exception {
        Mockito.when(request.getTrailingReturnsPeriod()).thenReturn("3");
        Assert.assertNotNull(underTest.execute(request));
        Mockito.when(request.getTrailingReturnsPeriod()).thenReturn("12");
        Assert.assertNotNull(underTest.execute(request));
        Mockito.when(request.getTrailingReturnsPeriod()).thenReturn("36");
        Assert.assertNotNull(underTest.execute(request));
        Mockito.when(request.getTrailingReturnsPeriod()).thenReturn("60");
        Assert.assertNotNull(underTest.execute(request));
    }

    public static String buildRes(){
        String res = "<response>\n" +
                "<status>\n" +
                "<code>0</code>\n" +
                "<message>OK</message>\n" +
                "</status>\n" +
                "<data _idtype=\"mstarid\" _id=\"F0000119QG\">\n" +
                "<api _id=\"l6z1o1i01ijwupyt\">\n" +
                "<DP-DayEndDate>2023-10-17</DP-DayEndDate>\n" +
                "</api>\n" +
                "</data>\n" +
                "</response>";
        return res;
    }
}