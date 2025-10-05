package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.hhhh.group.secwealth.mktdata.elastic.bean.ai.response.StockListResponse;
import com.hhhh.group.secwealth.mktdata.elastic.bean.ai.stock.StockListRequest;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.validation.service.ValidationService;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StockListServiceTest {
    @Mock
    private PredsrchCommonService predsrchCommonService;
    @Mock
    private ValidationService validationService;
    @Mock
    private AppProperties appProperty;
    @Mock
    private ElasticsearchRestTemplate elasticsearchTemplate;
    @InjectMocks
    private StockListService underTest;

    @Nested
    class WhenConvertingRequest {
        @Mock
        private PredSrchRequest request;
        @Mock
        private CommonRequestHeader header;

        @Test
        void test_convertingRequest() throws Exception {
            StockListRequest stockListRequest = new StockListRequest();
            stockListRequest.setMarket("HK");
            StockListRequest stockListRequest1 = (StockListRequest)underTest.convertRequest(stockListRequest, header);
            assertEquals("HK" , stockListRequest1.getMarket());
        }
    }

    @Nested
    class WhenExecuting {

        @Test
        void test_executing() throws Exception {
            List<CustomizedEsIndexForProduct> list = new ArrayList<>();
            CustomizedEsIndexForProduct customizedEsIndexForProduct = new CustomizedEsIndexForProduct();
            customizedEsIndexForProduct.setSymbol("00005");
            customizedEsIndexForProduct.setMarket("HK");
            customizedEsIndexForProduct.setProductNameAnalyzed(Arrays.asList("1","2","3"));
            customizedEsIndexForProduct.setProductShrtNameAnalyzed(Arrays.asList("1","2","3"));
            list.add(customizedEsIndexForProduct);
            StockListRequest stockListRequest = new StockListRequest();
            stockListRequest.setMarket("HK");
            stockListRequest.setAssetClasses(new String[]{"SEC"});
            stockListRequest.setPageNum(1);
            stockListRequest.setPageSize(10);
            assertNull(underTest.execute(stockListRequest));
        }
    }

    @Nested
    class WhenValidatingServiceResponse {
        @Mock
        private Object serviceResponse;

        @Test
        void test_validatingServiceResponse() {
            assertDoesNotThrow(() -> underTest.validateServiceResponse(serviceResponse));
        }
    }

    @Nested
    class WhenConvertingResponse {
        @Mock
        private Object validServiceResponse;

        @Test
        void test_convertingResponse() throws Exception {
            when(appProperty.getNameByIndex(any())).thenReturn(0);
            CommonRequestHeader header = new CommonRequestHeader();
            header.setLocale("zh_CN");
            header.setCountryCode("HK");
            header.setAppCode("test");
            ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
            StockListRequest request = new StockListRequest();
            request.setMarket("HK");
            request.setPageNum(1);
            request.setPageSize(1);
            ArgsHolder.putArgs("orgRequest", request);
            List<CustomizedEsIndexForProduct> list = new ArrayList<>();
            CustomizedEsIndexForProduct customizedEsIndexForProduct = new CustomizedEsIndexForProduct();
            customizedEsIndexForProduct.setSymbol("00005");
            customizedEsIndexForProduct.setMarket("HK");
            customizedEsIndexForProduct.setProductNameAnalyzed(Arrays.asList("1","2","3"));
            customizedEsIndexForProduct.setProductShrtNameAnalyzed(Arrays.asList("1","2","3"));
            list.add(customizedEsIndexForProduct);
            SearchHits<CustomizedEsIndexForProduct> searchHits = mock(SearchHitsImpl.class);
            SearchHit searchHit = mock(SearchHit.class);
            when(searchHit.getContent()).thenReturn(customizedEsIndexForProduct);
            when(searchHits.getSearchHits()).thenReturn(Arrays.asList(new SearchHit[]{searchHit}));
            StockListResponse resp = underTest.convertResponse(searchHits);
            assertEquals("00005", resp.getData().get(0).getSymbol());
        }
    }
}
