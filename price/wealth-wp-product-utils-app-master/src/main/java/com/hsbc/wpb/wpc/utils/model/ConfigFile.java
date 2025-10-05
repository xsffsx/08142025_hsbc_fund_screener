package com.dummy.wpb.wpc.utils.model;

import lombok.Data;

@Data
@SuppressWarnings("java:S1700")
public class ConfigFile {
    /*
    - name: product-metadata
      forEntity: ALL
      description: product-metadata.yaml
      configFile: product-metadata.yaml
      schemaFile: product-metadata-schema.json
     */
    private String id;
    private String name;
    private String forEntity;
    private String description;
    private String configFile;
    private String schemaFile;
}
