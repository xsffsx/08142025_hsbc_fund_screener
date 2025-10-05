package com.dummy.wmd.wpc.graphql.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;
import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class LogFileServiceTests {

    @InjectMocks
    private LogFileService logFileService;

    @Test
    public void testGetPath_givenFileName_returnsPath() {
        Path path = logFileService.getPath("hello.txt");
        Assert.assertNotNull(path);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPath_givenFileName_throwException() {
        logFileService.getPath("\\hello.txt");
    }

    @Test
    public void testMapLogFileByFileName_givenFileNameAndDate_returnsLogName() {
        String[] prodType = new String[]{
                "UT_20230817.xls", "SEC_20230817.xls", "BOND_20230817.xls", "SID_20230817.xls", "INS_20230817.xls",
                "PRODALTID_20230817.xls", "REFDATA_20230817.xls", "CUSTELIG_20230817.xls", "PRODAVC_20230817.xls", "ASETGEOAL_20230817.xls",
                "ASETSICAL_20230817.xls", "AVCCHAR_20230817.xls", "AVCCORL_20230817.xls", "MAP.WPCE23.D123456.T123456.xls",
                "MANLPRCBOND_20230817.csv", "MANLPRCSEC_20230817.csv"
        };
        String perfix = ".hello_";
        String fileName;
        for (String pt : prodType) {
            fileName = perfix + pt;
            String log = LogFileService.mapLogFileByFileName(fileName);
            Assert.assertNotNull(log);
        }

    }
}
