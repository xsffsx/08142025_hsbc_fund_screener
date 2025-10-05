package com.dummy.wpb.wpc.utils.load;

import com.google.common.collect.ImmutableMap;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.Silent.class)
public class MetadataLoaderTest {

    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private List<Map<String, Object>> mockMetadataList;

    @Mock
    private MongoCollection collection;

    private MetadataLoader metadataLoaderUnderTest;

    @Before
    public void setUp() throws Exception {
        metadataLoaderUnderTest = spy(new MetadataLoader(mockMongodb, mockMetadataList));
        ReflectionTestUtils.setField(metadataLoaderUnderTest, "collection", collection);
        when(collection.countDocuments()).thenReturn(1L);
        List<Map<String, Object>> mockMetadataList = spy(new ArrayList());
        mockMetadataList.add(ImmutableMap.of("a", "1"));
        ReflectionTestUtils.setField(metadataLoaderUnderTest, "metadataList", mockMetadataList);
    }

    @Test
    public void testLoad() {
        metadataLoaderUnderTest.load();
        Assert.assertNotNull(collection);
    }
}
