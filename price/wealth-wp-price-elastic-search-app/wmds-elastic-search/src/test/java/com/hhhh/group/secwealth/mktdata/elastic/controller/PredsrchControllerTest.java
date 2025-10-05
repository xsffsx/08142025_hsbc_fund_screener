package com.hhhh.group.secwealth.mktdata.elastic.controller;

import com.hhhh.group.secwealth.mktdata.elastic.bean.ai.stock.StockListRequest;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.MultiPredSrchRequest;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.helper.InternalSearchRequest;
import com.hhhh.group.secwealth.mktdata.elastic.service.MultiPredsrchService;
import com.hhhh.group.secwealth.mktdata.elastic.service.PredsrchService;
import com.hhhh.group.secwealth.mktdata.elastic.service.ScheduleDataLoadService;
import com.hhhh.group.secwealth.mktdata.elastic.service.StockListService;
import com.hhhh.group.secwealth.mktdata.elastic.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PredsrchControllerTest {
    @Mock
    private PredsrchService predsrchService;
    @Mock
    private StockListService stockListService;
    @Mock
    private MultiPredsrchService multiPredsrchService;
    @Mock
    private InternalProductKeyUtil internalProductKeyUtil;
    @Mock
    private ScheduleDataLoadService scheduleDataLoadService;
    @InjectMocks
    private PredsrchController underTest;

    @Nested
    class WhenMultiingPredsrch {
        private final String SYMBOL = "00005";
        @Mock
        private MultiPredSrchRequest request;
        @Mock
        private CommonRequestHeader header;

        @Test
        void test_multiPredsrch() throws Exception {
            assertNull(underTest.multiPredsrch(SYMBOL, request, header));
        }
    }

    @Nested
    class WhenPredsrching {
        private final String SYMBOL = "SYMBOL";
        @Mock
        private PredSrchRequest request;
        @Mock
        private CommonRequestHeader header;

        @Test
        void test_predsrch() throws Exception {
            assertNull(underTest.predsrch(SYMBOL, request, header));
        }
    }

    @Nested
    class WhenLoadingData {

        @Test
        void test_loadData() throws Exception {
            assertEquals("success", underTest.loadData());
        }
    }

    @Nested
    class WhenGettingProductBySearchWithAltClassCde {
        @Mock
        private InternalSearchRequest request;

        @Test
        void test_getProductBySearchWithAltClassCde() throws Exception {
            assertNull(underTest.getProductBySearchWithAltClassCde(request));
        }
    }

    @Nested
    class WhenGetStockList {
        @Mock
        private StockListRequest request;
        @Mock
        private CommonRequestHeader header;
        @Mock
        private StockListService stockListService;
        @Test
        void test_getProductBySearchWithAltClassCde() throws Exception {
            assertNull(underTest.getStockList(request, header));
        }
    }
}
