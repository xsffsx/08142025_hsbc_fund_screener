package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.MultiPredSrchRequest;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MultiPredsrchServiceTest {
    @InjectMocks
    private MultiPredsrchService underTest;

    @Mock
    private PredsrchCommonService predsrchCommonService;

    @Nested
    class WhenConvertingRequest {
        @Mock
        private CommonRequestHeader header;

        @Test
        void test_convertingRequest() throws Exception {
            MultiPredSrchRequest multiPredSrchRequest = new MultiPredSrchRequest();
            multiPredSrchRequest.setMarket("US");
            String[] asset = new String[]{"SEC"};
            String[] keyword = new String[]{"AAPL"};
            multiPredSrchRequest.setAssetClasses(asset);
            multiPredSrchRequest.setKeyword(keyword);
            multiPredSrchRequest.setIsPreciseSrch(false);
            MultiPredSrchRequest multiReq = (MultiPredSrchRequest)underTest.convertRequest(multiPredSrchRequest, header);
            assertEquals("US" , multiReq.getMarket());
        }
    }

    @Nested
    class WhenExecuting {

        @Test
        void test_executing() {
            MultiPredSrchRequest predSrchRequest = new MultiPredSrchRequest();
            predSrchRequest.setMarket("HK");
            predSrchRequest.setAssetClasses(new String[]{"SEC"});
            predSrchRequest.setKeyword(new String[]{"00005"});
            assertDoesNotThrow(() -> underTest.execute(predSrchRequest));
        }
    }

    @Nested
    class WhenValidatingServiceResponse {
        @Mock
        private Object serviceResponse;

        @Test
        void test_validatingServiceResponse() throws Exception {
            assertDoesNotThrow(() -> underTest.validateServiceResponse(serviceResponse));
        }
    }

    @Nested
    class WhenConvertingResponse {

        @Test
        void test_convertingResponse() throws Exception {
            List<PredSrchResponse> list = new ArrayList<>();
            PredSrchResponse predSrchResponse = new PredSrchResponse();
            predSrchResponse.setSymbol("AAPL");
            predSrchResponse.setMarket("NA");
            list.add(predSrchResponse);
            List<PredSrchResponse> resp = underTest.convertResponse(list);
            assertEquals("AAPL", resp.get(0).getSymbol());
        }
    }
}
