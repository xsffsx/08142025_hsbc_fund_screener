package com.dummy.wpb.wpc.utils.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ConfigPackageInfoTests {

    @Test
    public void testGetConfigFile() {
        ConfigFile configFile = new ConfigFile();
        configFile.setName("name");
        List<ConfigFile> configList = new ArrayList<>();
        configList.add(configFile);
        ConfigPackageInfo configPackageInfo = new ConfigPackageInfo();
        configPackageInfo.setConfigList(configList);
        configPackageInfo.getConfigList();
        String name = configPackageInfo.getConfigFile("name");
        Assert.assertNull(name);
    }
}
