package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import com.hhhh.group.secwealth.mktdata.elastic.properties.VolumeServiceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.io.File;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VerifyFilesServiceTest {
    @Mock
    private AppProperties appProperty;
    @Mock
    private VolumeServiceProperties volumeServiceProperty;
    @Mock
    private VolumeService volumeService;
    @Mock
    private File sourceFile;
    @InjectMocks
    private VerifyFilesService underTest;

    @Nested
    class WhenVerifyingFiles {

        @BeforeEach
        void setup() {
            ReflectionTestUtils.setField(underTest, "sourceFilePath", "/home/vcap/app/wlp/usr/servers/defaultServer/md5/");
            ReflectionTestUtils.setField(underTest, "rejectFilePath", "/home/vcap/app/wlp/usr/servers/defaultServer/reject_path/");
            ReflectionTestUtils.setField(underTest, "filePath", "/home/vcap/app/wlp/usr/servers/defaultServer/predictive_search/");
            ReflectionTestUtils.setField(underTest, "siteConverterRule", Arrays.asList("SG_HBSP|MKD", "SG_hhhh|MKD"));
            doReturn(Arrays.asList("SG_HBSP", "SG_hhhh")).when(appProperty).getSupportSites();
            doReturn("true").when(volumeServiceProperty).getValue(any(), any());
        }

        @Test
        void verifyFilesTest() {
            assertDoesNotThrow(() -> underTest.verifyFiles());
        }

        @Test
        void test_verifyingFilesByRetry() {
            assertDoesNotThrow(() -> underTest.verifyFiles(true));
        }
    }
}
