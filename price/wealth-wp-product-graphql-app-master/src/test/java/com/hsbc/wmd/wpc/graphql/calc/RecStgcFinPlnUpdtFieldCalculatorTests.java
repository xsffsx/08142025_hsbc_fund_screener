package com.dummy.wmd.wpc.graphql.calc;

import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.dummy.wmd.wpc.graphql.utils.DBUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class RecStgcFinPlnUpdtFieldCalculatorTests {

    @InjectMocks
    private RecStgcFinPlnUpdtFieldCalculator recStgcFinPlnUpdtFieldCalculator;

    @Test
    public void testCalculate_givenDocument_returnsObject() {
        Document doc = CommonUtils.readResourceAsDocument("/files/BOND-286859053.json");
        MockedStatic<DBUtils> dbUtils = Mockito.mockStatic(DBUtils.class);
        MongoCollection mongoCollection = Mockito.mock(MongoCollection.class);
        dbUtils.when(() -> DBUtils.getCollection(anyString())).thenReturn(mongoCollection);
        FindIterable findIterable = Mockito.mock(FindIterable.class);
        Mockito.when(mongoCollection.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.projection(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.first()).thenReturn(doc).thenReturn(null);
        Object object1 = recStgcFinPlnUpdtFieldCalculator.calculate(doc);
        Assert.assertNotNull(object1);
        Object object2 = recStgcFinPlnUpdtFieldCalculator.calculate(doc);
        Assert.assertNotNull(object2);
        dbUtils.close();
    }
}
