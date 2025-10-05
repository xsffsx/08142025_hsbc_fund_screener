package com.dummy.wmd.wpc.graphql.scalar.datetime;

import graphql.language.StringValue;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.time.LocalTime;
import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class GraphqlLocalTimeCoercingTest {

    @InjectMocks
    private GraphqlLocalTimeCoercing graphqlLocalTimeCoercing;

    @Test
    public void testSerialize_givenLocalTime_returnsStingTime() {
        String localTime = graphqlLocalTimeCoercing.serialize(LocalTime.now());
        Assert.assertNotNull(localTime);
    }

    @Test
    public void testSerialize_givenDate_returnsStingTime() {
        Date date = new Date();
        String localTime = graphqlLocalTimeCoercing.serialize(date);
        Assert.assertNotNull(localTime);
    }

    @Test
    public void testSerialize_givenStingTime_returnsStingTime() {
        String time = "06:27:06.161";
        String localTime = graphqlLocalTimeCoercing.serialize(time);
        Assert.assertEquals(localTime, time);
    }

    @Test(expected = CoercingSerializeException.class)
    public void testSerialize_givenStingTime_throwException() {
        graphqlLocalTimeCoercing.serialize("");
    }

    @Test
    public void testParseValue_givenLocalTime_returnsLocalTime() {
        LocalTime localTime = graphqlLocalTimeCoercing.parseValue(LocalTime.now());
        Assert.assertNotNull(localTime);
    }

    @Test(expected = CoercingParseValueException.class)
    public void testParseValue_givenLocalTime_throwException() {
        graphqlLocalTimeCoercing.parseValue("");
    }

    @Test
    public void testParseLiteral_givenStringValue_returnsLocalTime() {
        StringValue stringValue = new StringValue("06:27:06.161");
        LocalTime localTime = graphqlLocalTimeCoercing.parseLiteral(stringValue);
        Assert.assertNotNull(localTime);
    }

    @Test(expected = CoercingParseLiteralException.class)
    public void testParseLiteral_givenStringValue_throwException() {
        StringValue stringValue = new StringValue("");
        graphqlLocalTimeCoercing.parseLiteral(stringValue);
    }

    @Test
    public void testConvertImpl_givenObject_returnsLocalTime(){
        try {
            Method convertImpl = graphqlLocalTimeCoercing.getClass().getDeclaredMethod("convertImpl", Object.class);
            convertImpl.setAccessible(true);
            LocalTime localTime = (LocalTime)convertImpl.invoke(graphqlLocalTimeCoercing,new Date());
            Assert.assertNotNull(localTime);
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
