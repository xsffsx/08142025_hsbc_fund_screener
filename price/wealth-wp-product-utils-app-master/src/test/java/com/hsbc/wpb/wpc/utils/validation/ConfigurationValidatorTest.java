package com.dummy.wpb.wpc.utils.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.dummy.wpb.wpc.utils.CommonUtils;
import com.dummy.wpb.wpc.utils.MockUtils;
import com.dummy.wpb.wpc.utils.model.ConfigFile;
import com.dummy.wpb.wpc.utils.model.ConfigPackageInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ConfigurationValidatorTest {

    private Map<String, String> mockEntryMap;

    private ConfigurationValidator configurationValidatorUnderTest;

    MockedStatic<CommonUtils> commonUtils;

    @Before
    public void setUp() throws Exception {

        try {
            Map<String, String> mockEntryMap = spy(new HashMap() {{
                put("config-package-info.yaml", "hi");
                put("SG", "a");
            }});

            configurationValidatorUnderTest = spy(new ConfigurationValidator(mockEntryMap));
            MockUtils.mockPrivate(configurationValidatorUnderTest, "entryMap", mockEntryMap);

            ConfigFile configFile = new ConfigFile();
            configFile.setConfigFile("config-package-info.yaml");
            //when(configPackageInfo.getConfigList()).thenReturn(Arrays.asList(configFile));


            // ObjectMapper yamlMapper = spy(new ObjectMapper(new YAMLFactory()));
            ObjectMapper yamlMapper = mock(ObjectMapper.class);
            MockUtils.mockPrivate(configurationValidatorUnderTest, "yamlMapper", yamlMapper);
            ConfigPackageInfo configPackageInfo = spy(ConfigPackageInfo.class);
            configPackageInfo.setConfigList(new ArrayList<>(Arrays.asList(configFile)));

            doReturn("aaa").when(configPackageInfo).getConfigFile("product-metadata");

            when(mockEntryMap.get("config-package-info.yaml")).thenReturn("configContent");

            //when(yamlMapper.readValue("configContent", ConfigPackageInfo.class)).thenReturn(configPackageInfo);

            //when(yamlMapper.readValue(any(String.class), eq(ConfigPackageInfo.class))).thenReturn(configPackageInfo);

            Mockito.when(yamlMapper.readValue(anyString(), any(Class.class))).thenReturn(configPackageInfo).thenReturn(null);

            commonUtils = Mockito.mockStatic(CommonUtils.class);
            commonUtils.when(() -> CommonUtils.readResource(any())).thenReturn("hello");

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @After
    public void clean() {
        commonUtils.close();
    }

    @Test
    public void testValidate() {
        // Setup
        final List<Error> expectedResult = Arrays.asList(new Error("subject", "message", "data"));

        // Run the test
        final List<Error> result = configurationValidatorUnderTest.validate();
    }

    @Test
    public void testValidate_MapReturnsNull() {

        try (MockedConstruction<JsonSchemaValidator> jsonSchemaValidatorMockedConstruction = Mockito.mockConstruction(JsonSchemaValidator.class, (mock, context) -> {
            when(mock.validate()).thenReturn(new ArrayList<Error>());
        });
             MockedConstruction<MetadataValidator> metadataValidatorMockedConstruction = Mockito.mockConstruction(MetadataValidator.class, (mock, context) -> {
                 when(mock.validate()).thenReturn(new ArrayList<Error>());
             })
        ) {
            // Setup
            Map<String, String> mockEntryMap = spy(new HashMap() {{
                put("config-package-info.yaml", "hi");
                put("SG", "a");
            }});

            final List<Error> expectedResult = Arrays.asList(new Error("subject", "message", "data"));
            when(mockEntryMap.get("config-package-info.yaml")).thenReturn("configContent");

            // Run the test
            final List<Error> result = configurationValidatorUnderTest.validate();

            // Verify the results
//            assertThat(result).isEqualTo(expectedResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testValidate_ConfigYamlNotFound() {
        // Setup
        Map<String, String> mockEntryMap = new HashMap();
        configurationValidatorUnderTest = new ConfigurationValidator(mockEntryMap);

        // Run the test
        List<Error> result = configurationValidatorUnderTest.validate();

        // Verify
        assert result.size() == 1;
        assert result.get(0).getMessage().contains("config-package-info.yaml not found");
    }

    @Test
    public void testValidate_FileOrSchemaNotFound() throws Exception {
        // Setup
       try (MockedConstruction<JsonSchemaValidator> jsonSchemaValidatorMockedConstruction = Mockito.mockConstruction(JsonSchemaValidator.class, (mock, context) -> {
            when(mock.validate()).thenReturn(new ArrayList<Error>());
        });
             MockedConstruction<MetadataValidator> metadataValidatorMockedConstruction = Mockito.mockConstruction(MetadataValidator.class, (mock, context) -> {
                 when(mock.validate()).thenReturn(new ArrayList<Error>());
             })
        ) {
           Map<String, String> mockEntryMap = spy(new HashMap() {{
               put("config-package-info.yaml", "configContent");
               put("file1.yaml", "fileContent");
           }});
           configurationValidatorUnderTest = spy(new ConfigurationValidator(mockEntryMap));

           // Mock ConfigPackageInfo
           ConfigPackageInfo configPackageInfo = mock(ConfigPackageInfo.class);
           ConfigFile file1 = new ConfigFile();
           file1.setConfigFile("file1.yaml");
           file1.setSchemaFile("schema1.json");

           ConfigFile file2 = new ConfigFile();
           file2.setConfigFile("file2.yaml");
           file2.setSchemaFile(null);

           List<ConfigFile> configFiles = Arrays.asList(file1, file2);
           when(configPackageInfo.getConfigList()).thenReturn(configFiles);

           // Mock ObjectMapper
           ObjectMapper yamlMapper = mock(ObjectMapper.class);
           MockUtils.mockPrivate(configurationValidatorUnderTest, "yamlMapper", yamlMapper);
           when(yamlMapper.readValue(anyString(), any(Class.class))).thenReturn(configPackageInfo);

           // Run the test
           List<Error> result = configurationValidatorUnderTest.validate();

           // Verify
           assert result.size() == 2;
           assert result.get(0).getMessage().contains("schema file not found");
           assert result.get(1).getMessage().contains("file not found");
       }
    }
}
