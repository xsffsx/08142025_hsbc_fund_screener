package com.hhhh.group.secwealth.mktdata.elastic.configuration;

import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import com.hhhh.group.secwealth.mktdata.elastic.properties.VolumeServiceProperties;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.IllegalInitializationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VolumeConfigTest {
    @Mock
    private AppProperties appProperty;
    @Mock
    private VolumeServiceProperties volumeServiceProperty;
    @Mock
    private Map<String, String[]> privateKeyIdMap;
    @Mock
    private Map<String, String[]> privateKeyPathMap;
    @Mock
    private Map<String, String[]> remoteFilePathMap;
    @Mock
    private Map<String, String[]> remoteFileDirMap;
    @Mock
    private Map<String, String> hostMap;
    @Mock
    private Map<String, String> userMap;
    @InjectMocks
    private VolumeConfig underTest;

    @Nested
    class WhenInit {

        @Test
        void init() {
            String siteKey = "HK_HBAP";
            List<String> supportSites = new ArrayList<>();
            supportSites.add("Default");
            supportSites.add(siteKey);
            String remoteFilePathStr = "/appvol/rbpwpcbc/data/outgoing/HKHBAP/WMDS/MD5/";
            when(volumeServiceProperty.getValue(siteKey, VolumeServiceProperties.REMOTE_FILE_PATH_KEY)).thenReturn(remoteFilePathStr);
            String privateKeyIdStr = "id_dsa";
            when(volumeServiceProperty.getValue(siteKey, VolumeServiceProperties.PRIVATEKEY_ID_KEY)).thenReturn(privateKeyIdStr);
            String privateKeyPathStr = "/predsrch/volumeServicePrivateKey/id_dsa_hbap_prod";
            when(volumeServiceProperty.getValue(siteKey, VolumeServiceProperties.PRIVATEKEY_PATH_KEY)).thenReturn(privateKeyPathStr);
            String remoteFileDirStr = "/appvol/rbpwpcbc/data/outgoing/HKHBAP/WMDS/MD5/";
            when(volumeServiceProperty.getValue(siteKey, VolumeServiceProperties.REMOTE_FILE_DIR_KEY)).thenReturn(remoteFileDirStr);
            String host = "was-w2-amh-sftp-prod-wpcbatch.hk.hhhh";
            when(volumeServiceProperty.getValue(siteKey, "host")).thenReturn(host);
            String user = "wpcftp01";
            when(volumeServiceProperty.getValue(siteKey, "user")).thenReturn(user);
            when(appProperty.getSupportSites()).thenReturn(supportSites);
            assertThrows(IllegalInitializationException.class, () -> underTest.init());
        }
    }
}
