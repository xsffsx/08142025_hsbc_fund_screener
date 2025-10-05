package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.starter.validation.service.ValidationService;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PredsrchServiceTest {
    @Mock
    private PredsrchCommonService predsrchCommonService;
    @Mock
    private ValidationService validationService;
    @InjectMocks
    private PredsrchService underTest;

    @Nested
    class WhenConvertingRequest {
        @Mock
        private PredSrchRequest request;
        @Mock
        private CommonRequestHeader header;

        @Test
        void test_convertingRequest() throws Exception {
            PredSrchRequest predSrchRequest = new PredSrchRequest();
            predSrchRequest.setMarket("HK");
            String[] asset = new String[]{"SEC"};
            predSrchRequest.setAssetClasses(asset);
            predSrchRequest.setKeyword("00005");
            PredSrchRequest predRequest = (PredSrchRequest)underTest.convertRequest(predSrchRequest, header);
            assertEquals("HK" , predRequest.getMarket());
        }
    }

    @Nested
    class WhenExecuting {

        @Test
        void test_executing() throws Exception {
            PredSrchRequest predSrchRequest = new PredSrchRequest();
            predSrchRequest.setMarket("HK");
            predSrchRequest.setAssetClasses(new String[]{"SEC"});
            predSrchRequest.setKeyword("00005");
            assertNotNull(underTest.execute(predSrchRequest));
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
        @Mock
        private Object validServiceResponse;

        @Test
        void test_convertingResponse() throws Exception {
            List<PredSrchResponse> list = new ArrayList<>();
            PredSrchResponse predSrchResponse = new PredSrchResponse();
            predSrchResponse.setSymbol("00005");
            predSrchResponse.setMarket("HK");
            list.add(predSrchResponse);
            List<PredSrchResponse> resp = underTest.convertResponse(list);
            assertEquals("00005", resp.get(0).getSymbol());
        }
    }
}
