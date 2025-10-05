package com.dummy.wpb.product.service;


import com.dummy.wpb.product.model.SystemParameter;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

public class SystemParameterServiceTest {

    private static MongoOperations mongoOperations;

    private static SystemParameterService systemParameterService;

    private static SystemParameter sequence = new SystemParameter();

    private static SystemParameter timestamp = new SystemParameter();

    private final static String CTRY_REC_CDE = "HK";

    private final static String GRP_MEMBR_REC_CDE = "HBAP";

    private final static String PARM_CDE_SEQUENCE = "EGRESS.GSOPSD.SEQ";

    private final static String PARM_CDE_TIMESTAMP = "EGRESS.GSOPSD.DAC.PRODUCT.LAST.SUCCESSFUL.DT.TM";

    @BeforeClass
    @SneakyThrows
    public static void init() {
        mongoOperations = Mockito.mock(MongoOperations.class);
        systemParameterService = new SystemParameterService(mongoOperations);
        sequence = JsonUtil.convertJson2Object(CommonUtils.readResource("/sys_parm_sequence.json"), SystemParameter.class);
        timestamp = JsonUtil.convertJson2Object(CommonUtils.readResource("/sys_parm_timestamp.json"), SystemParameter.class);
        Mockito.when(mongoOperations.findAndReplace(Mockito.any(Query.class), Mockito.any())).thenReturn(null);
        Mockito.when(mongoOperations.save(Mockito.any())).thenReturn(null);
    }

    @Test
    public void testGetNextEgressFileSequenceTest_notNull() {
        Mockito.when(mongoOperations.findOne(Mockito.any(), Mockito.any())).thenReturn(sequence);
        String seq = systemParameterService.getNextSequence(CTRY_REC_CDE, GRP_MEMBR_REC_CDE, PARM_CDE_SEQUENCE);
        Assert.assertEquals("124", seq);
    }

    @Test
    public void testGetNextEgressFileSequenceTest_null() {
        Mockito.when(mongoOperations.findOne(Mockito.any(), Mockito.any())).thenReturn(null);
        String seq = systemParameterService.getNextSequence(CTRY_REC_CDE, GRP_MEMBR_REC_CDE, PARM_CDE_SEQUENCE);
        Assert.assertEquals("1", seq);
    }

    @Test
    public void testGetLastSuccessfulDateTimeTest_notNull() {
        Mockito.when(mongoOperations.findOne(Mockito.any(), Mockito.any())).thenReturn(timestamp);
        String timestamp = systemParameterService.getLastSuccessfulDateTime(CTRY_REC_CDE, GRP_MEMBR_REC_CDE, PARM_CDE_TIMESTAMP);
        Assert.assertEquals("2023-08-20T04:26:49.993Z", timestamp);
    }

    @Test
    public void testGetLastSuccessfulDateTimeTest_null() {
        Mockito.when(mongoOperations.findOne(Mockito.any(), Mockito.any())).thenReturn(null);
        String timestamp = systemParameterService.getLastSuccessfulDateTime(CTRY_REC_CDE, GRP_MEMBR_REC_CDE, PARM_CDE_TIMESTAMP);
        Assert.assertNull(timestamp);
    }
}
