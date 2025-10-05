package com.dummy.wpb.wpc.utils.validation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.dummy.wpb.wpc.utils.CommonUtils;
import com.dummy.wpb.wpc.utils.model.ConfigFile;
import com.dummy.wpb.wpc.utils.model.ConfigPackageInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ConfigurationValidator implements Validator{
    private Map<String, String> entryMap;
    private ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    private static String configYaml = "config-package-info.yaml";

    public ConfigurationValidator(Map<String, String> entryMap) {
        this.entryMap = entryMap;
    }

    @Override
    public List<Error> validate() {
        List<Error> errors = new ArrayList<>();

        try {
            // check the exists of the package info file
            String configContent = entryMap.get(configYaml);
            if (null == configContent) {
                errors.add(new Error("configuration", "config-package-info.yaml not found in the package root."));
                return errors;
            }
            ConfigPackageInfo packageInfo = yamlMapper.readValue(configContent, ConfigPackageInfo.class);

            // validate package info file with json schema
            String schemaContent = CommonUtils.readResource("/config-package-info-schema.json");
            JsonSchemaValidator schemaValidator = new JsonSchemaValidator(configYaml, schemaContent, configContent);
            errors.addAll(schemaValidator.validate());
            if(!errors.isEmpty()) return errors;

            // make sure all files listed are exist
            for (ConfigFile conf : packageInfo.getConfigList()) {
                String configFile = conf.getConfigFile();
                if (null == entryMap.get(configFile)) {
                    errors.add(new Error(configYaml, "config file not found: " + configFile, conf));
                }
                String schemaFile = conf.getSchemaFile();
                if (null != schemaFile && null == entryMap.get(schemaFile)) {
                    errors.add(new Error(configYaml, "schema file not found: " + schemaFile, conf));
                }
            }

            // if there is schema provided, validate the config with the schema
            for (ConfigFile conf : packageInfo.getConfigList()) {
                String configFile = conf.getConfigFile();
                String schemaFile = conf.getSchemaFile();
                if(null != schemaFile) {
                    schemaContent = entryMap.get(schemaFile);
                    configContent = entryMap.get(configFile);
                    schemaValidator = new JsonSchemaValidator(configFile, schemaContent, configContent);
                    List<Error> errList = schemaValidator.validate();
                    if(!errList.isEmpty()) {
                        log.warn("Json Schema validation failed for {} against {}", schemaFile, configFile);
                    }
                    errors.addAll(errList);
                }
            }

            // extra validation on product metadata
            String configFile = packageInfo.getConfigFile("product-metadata");
            configContent = entryMap.get(configFile);
            List<Map<String, Object>> metadataList = yamlMapper.readValue(configContent, new TypeReference<List<Map<String, Object>>>(){});
            MetadataValidator metadataValidator = new MetadataValidator(metadataList);
            errors.addAll(metadataValidator.validate());
        } catch (Exception e) {
            errors.add(new Error("Exception", e.getMessage(), CommonUtils.exceptionInfo(e)));
        }

        return errors;
    }
}
