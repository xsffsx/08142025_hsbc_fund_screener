package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.validator.Error;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static graphql.Assert.assertNotNull;

public class AssetVolatilityClassBaseChangeServiceTest {

    private AssetVolatilityClassBaseChangeService assetVolatilityClassBaseChangeService;

    @Before
    public void setUp() {
        assetVolatilityClassBaseChangeService = new AssetVolatilityClassBaseChangeService() {
            @Override
            public List<Error> validateDocument(Document doc) {
                return null;
            }

            @Override
            public Document updateDocument(Document doc) {
                return null;
            }

            @Override
            public Document addDocument(Document doc) {
                return null;
            }

            @Override
            public List<Error> validate(Document amendment) {
                return null;
            }

            @Override
            public void add(Document doc) {

            }

            @Override
            public void update(Document doc) {

            }

            @Override
            public void delete(Document doc) {

            }

            @Override
            public Document findFirst(Bson filter) {
                return null;
            }

            @Override
            public Document findFirst(Bson filter, Document docChange) {
                return null;
            }
        };
    }

    @Test
    public void testFillListRowid_givenMapList_returnsNull() {
        List<Map> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("test", "addRowId");
        list.add(map);
        // Run the test
        assetVolatilityClassBaseChangeService.fillListRowid(list);
        // Verify the results
        assertNotNull(list.get(0).get(Field.rowid));
    }
}
