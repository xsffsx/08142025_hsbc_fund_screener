package com.dummy.wpb.wpc.utils.model;

import lombok.Data;

import java.util.List;

@Data
public class ConfigPackageInfo {
    private List<ConfigFile> configList;

    public String getConfigFile(String name) {
        ConfigFile configFile= configList.stream().filter(conf -> conf.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        return null == configFile? null: configFile.getConfigFile();
    }
}
