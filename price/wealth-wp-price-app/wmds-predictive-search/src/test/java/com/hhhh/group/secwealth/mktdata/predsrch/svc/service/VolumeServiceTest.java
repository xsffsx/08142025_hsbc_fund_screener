package com.hhhh.group.secwealth.mktdata.predsrch.svc.service;

import com.hhhh.group.secwealth.mktdata.config.SpringTestConfig;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.impl.VolumeServiceImpl;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.FileAccessHelper;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.VolumeServiceConfig;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.WpcFileUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;

@RunWith(PowerMockRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = SpringTestConfig.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@PrepareForTest({FileAccessHelper.class, WpcFileUtil.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*",
        "javax.xml.transform.*", "org.xml.*", "javax.management.*",
        "javax.net.ssl.*", "com.sun.org.apache.xalan.internal.xsltc.trax.*"})
public class VolumeServiceTest {

    @Mock
    private VolumeServiceConfig volumeServiceConfig;

    @InjectMocks
    private VolumeServiceImpl volumeService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

    }


    @Test
    public void testCopyToLocalData() throws Exception {
        Boolean flag = true;

        PowerMockito.mockStatic(FileAccessHelper.class);
        PowerMockito.when(FileAccessHelper.isConnect(Mockito.any(String.class), Mockito.any(String.class), Matchers.anyInt())).thenReturn(true);
        PowerMockito.when(FileAccessHelper.getChildren(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(String.class))).thenReturn(new String[]{"./test"});


        PowerMockito.mockStatic(WpcFileUtil.class);
        PowerMockito.when(WpcFileUtil.converOutFileNameToTimestamp(Mockito.any(String.class))).thenReturn(20l);
        PowerMockito.when(WpcFileUtil.getTime(Mockito.any(String.class))).thenReturn("pattern");


        ReflectionTestUtils.setField(volumeService, "supportSites", new String[]{"Default|Default", "a|A|A"});
        ReflectionTestUtils.setField(volumeService, "siteConverterRule", new String[]{"Default|Default", "a|A|A"});
        ReflectionTestUtils.setField(volumeService, "privateKeyPathMap", new HashMap<String, String[]>() {{
            put("Default|Default", new String[]{"Default|Default", "a|A|A"});
        }});
        ReflectionTestUtils.setField(volumeService, "remoteFilePathMap", new HashMap<String, String[]>() {{
            put("Default|Default", new String[]{"Default|Default", "a|A|A"});
        }});
        ReflectionTestUtils.setField(volumeService, "sourceFilePath", "./test");
        ReflectionTestUtils.setField(volumeService, "retryTime", 1);
        Mockito.when(volumeServiceConfig.getValue("Default|Default", VolumeServiceConfig.PREDSRCH_IS_VERIFY)).thenReturn("true");
        Mockito.when(volumeServiceConfig.getValue("Default|Default", VolumeServiceConfig.VOLUME_SERVICE_ENABLED)).thenReturn("true");
        try {
            volumeService.copyToLocalData();
        } catch (Exception e) {
              flag = false;
        }
         org.junit.Assert.assertTrue(flag);
    }
    @Test
    public void testIsConnectBySite() throws Exception {
        Boolean flag = true;

        PowerMockito.mockStatic(FileAccessHelper.class);
        PowerMockito.when(FileAccessHelper.isConnect(Mockito.any(String.class), Mockito.any(String.class), Matchers.anyInt())).thenReturn(true);
        PowerMockito.when(FileAccessHelper.getChildren(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(String.class))).thenReturn(new String[]{"./test"});


        PowerMockito.mockStatic(WpcFileUtil.class);
        PowerMockito.when(WpcFileUtil.converOutFileNameToTimestamp(Mockito.any(String.class))).thenReturn(20l);
        PowerMockito.when(WpcFileUtil.getTime(Mockito.any(String.class))).thenReturn("pattern");


        ReflectionTestUtils.setField(volumeService, "supportSites", new String[]{"Default|Default", "a|A|A"});
        ReflectionTestUtils.setField(volumeService, "siteConverterRule", new String[]{"Default|Default", "a|A|A"});
        ReflectionTestUtils.setField(volumeService, "privateKeyPathMap", new HashMap<String, String[]>() {{
            put("Default|Default", new String[]{"Default|Default", "a|A|A"});
        }});
        ReflectionTestUtils.setField(volumeService, "remoteFilePathMap", new HashMap<String, String[]>() {{
            put("Default|Default", new String[]{"Default|Default", "a|A|A"});
        }});
        ReflectionTestUtils.setField(volumeService, "sourceFilePath", "./test");
        ReflectionTestUtils.setField(volumeService, "retryTime", 1);
        Mockito.when(volumeServiceConfig.getValue("Default|Default", VolumeServiceConfig.PREDSRCH_IS_VERIFY)).thenReturn("true");
        Mockito.when(volumeServiceConfig.getValue("Default|Default", VolumeServiceConfig.VOLUME_SERVICE_ENABLED)).thenReturn("true");
        try {
            volumeService.isConnectBySite("Default|Default");
        } catch (Exception e) {
            flag = false;
        }
         org.junit.Assert.assertTrue(flag);

    }

}
