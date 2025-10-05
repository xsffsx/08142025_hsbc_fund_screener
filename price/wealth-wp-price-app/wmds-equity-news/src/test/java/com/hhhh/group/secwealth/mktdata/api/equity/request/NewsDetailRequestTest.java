package com.hhhh.group.secwealth.mktdata.api.equity.request;

import com.hhhh.group.secwealth.mktdata.api.equity.news.response.etnet.EtnetNewsDetail;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

@SuppressWarnings({"java:S5786"})
public class NewsDetailRequestTest {

    @Test
    public void testAllDominGetterSetter() {
        EtnetNewsDetail etnetNewsDetail = new EtnetNewsDetail();
        etnetNewsDetail.setId("test");
        etnetNewsDetail.setHeadline("test");
        etnetNewsDetail.setContent("test");
        etnetNewsDetail.setErr_desc("test");
        etnetNewsDetail.setErr_code("test");
        etnetNewsDetail.setAsOfDateTime("test");
        assertNotNull(etnetNewsDetail);
    }


}