package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.google.common.collect.Maps;
import com.hhhh.group.secwealth.mktdata.elastic.configuration.VolumeConfig;
import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import com.hhhh.group.secwealth.mktdata.elastic.properties.VolumeServiceProperties;
import com.hhhh.group.secwealth.mktdata.elastic.util.FileAccessUtil;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VolumeServiceTest {
    @Mock
    private AppProperties appProperty;
    @Mock
    private VolumeServiceProperties volumeServiceProperty;
    @Mock
    private FileAccessUtil fileAccessHelper;
    @Mock
    private VolumeConfig volumeConfig;
    @InjectMocks
    private VolumeService underTest;

    @Nested
    class WhenCopyingToLocalData {

        @Test
        void test_copyingToLocalData() {
            List<String> list = new ArrayList<>();
            list.add("Default");
            list.add("HK_HBAP");
            ReflectionTestUtils.setField(underTest, "retryTime", 3);
            when(appProperty.getSupportSites()).thenReturn(list);
            when(volumeServiceProperty.getValue("HK_HBAP", VolumeServiceProperties.VOLUME_SERVICE_ENABLED)).thenReturn("true");
            Map<String, String[]> privateKeyIdMap = Maps.newHashMap();
            privateKeyIdMap.put("HK_HBAP", new String[]{"id_dsa"});
            when(volumeConfig.getPrivateKeyIdMap()).thenReturn(privateKeyIdMap);
            Map<String, String[]> privateKeyPathMap = Maps.newHashMap();
            privateKeyPathMap.put("HK_HBAP", new String[]{"/predsrch/volumeServicePrivateKey/id_dsa_hbap"});
            when(volumeConfig.getPrivateKeyPathMap()).thenReturn(privateKeyPathMap);
            Map<String, String[]> remoteFileDirMap = Maps.newHashMap();
            remoteFileDirMap.put("HK_HBAP", new String[]{"/hhhh/rbpwpcbc/data/outgoing/HKHBAP/WMDS/MD5/"});
            when(volumeConfig.getRemoteFileDirMap()).thenReturn(remoteFileDirMap);
            assertThrows(ApplicationException.class, () -> underTest.copyToLocalData());
        }
    }
}
