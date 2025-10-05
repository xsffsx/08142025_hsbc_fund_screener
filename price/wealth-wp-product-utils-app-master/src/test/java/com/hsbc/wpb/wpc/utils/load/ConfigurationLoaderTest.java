package com.dummy.wpb.wpc.utils.load;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.dummy.wpb.wpc.utils.model.ConfigFile;
import com.dummy.wpb.wpc.utils.model.ConfigPackageInfo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ConfigurationLoaderTest {

    @Mock
    private MongoDatabase mockMongodb;

    @Mock
    private Map<String, String> mockEntryMap;

    private ConfigurationLoader configurationLoaderUnderTest;

    @Before
    public void setUp() throws Exception {
        when(mockEntryMap.get("config-package-info.yaml")).thenReturn("{configList:[{'id':'ALL/product-metadata','name':'product-metadata','forEntity':'ALL','description':'description','configFile':'f.txt'," +
                "'schemaFile':'schemaFile'}]}");

        configurationLoaderUnderTest = spy(new ConfigurationLoader(mockMongodb, mockEntryMap));

        MongoCollection mockCollection = mock(MongoCollection.class);
        ReflectionTestUtils.setField(configurationLoaderUnderTest,"collection",mockCollection);
        when(mockCollection.countDocuments()).thenReturn(1L);

        ConfigPackageInfo configPkgInfo = mock(ConfigPackageInfo.class);
        ReflectionTestUtils.setField(configurationLoaderUnderTest,"packageInfo",configPkgInfo);

        ReflectionTestUtils.setField(configurationLoaderUnderTest,"entryMap",mockEntryMap);
        when(mockEntryMap.get(any())).thenReturn("[{\"a\":\"b\"}]");

        when(configPkgInfo.getConfigList()).thenReturn(setConfigFile());

        ObjectMapper mockObjectMapper  = mock(ObjectMapper.class);
        ReflectionTestUtils.setField(configurationLoaderUnderTest,"yamlMapper",mockObjectMapper);
        when(mockObjectMapper.readValue(anyString(),Mockito.<Class> any())).thenReturn(Lists.newArrayList(ImmutableMap.of("a","a")));
    }

    @Test
    @SuppressWarnings("squid:S2699")
    public void testLoad() {
        Mockito.mockConstruction(MetadataLoader.class, (mock, context) -> {
            doNothing().when(mock).load();
        });

        configurationLoaderUnderTest.load();
    }

    @Test
    @SuppressWarnings("squid:S2699")
    public void testLoad_MapReturnsNull() {
        when(mockEntryMap.get("configFile")).thenReturn(null);
        configurationLoaderUnderTest.load();
    }

    private List<ConfigFile> setConfigFile() {
        ConfigFile c = new ConfigFile();
        c.setForEntity("ALL");
        c.setName("product-metadata");
        c.setConfigFile("f.json");

        ConfigFile c2 = new ConfigFile();
        c2.setForEntity("CNdummy");
        c2.setName("product-metadata");
        c2.setConfigFile("f.json");

        return Lists.newArrayList(c, c2);
    }
}
