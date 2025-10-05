package com.dummy.wmd.wpc.graphql.scalar.datetime;

import graphql.language.StringValue;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class GraphqlLocalDateTimeCoercingTest {

    private GraphqlLocalDateTimeCoercing graphqlLocalDateTimeCoercing;
    @Mock
    private DateTimeFormatter formatter;
    @Mock
    private LocalDateTimeConverter converter;

    @Before
    public void setUp() {
        graphqlLocalDateTimeCoercing = new GraphqlLocalDateTimeCoercing(false, formatter);
        ReflectionTestUtils.setField(graphqlLocalDateTimeCoercing, "converter", converter);
    }

    @Test
    public void testSerialize_givenLocalDateTime_returnsStringDate() {
        Mockito.when(converter.formatDate(any(LocalDateTime.class),any(DateTimeFormatter.class))).thenReturn("2023-08-15T00:00:00.000Z");
        String stringDate = graphqlLocalDateTimeCoercing.serialize(LocalDateTime.now());
        Assert.assertNotNull(stringDate);
    }

    @Test
    public void testSerialize_givenStringDateTime_returnsStringDate() {
        Mockito.when(converter.parseDate(anyString())).thenReturn(LocalDateTime.now());
        Mockito.when(converter.formatDate(any(LocalDateTime.class),any(DateTimeFormatter.class))).thenReturn("2023-08-15T00:00:00.000Z");
        String stringDate = graphqlLocalDateTimeCoercing.serialize("2023-08-15T00:00:00.000Z");
        Assert.assertNotNull(stringDate);
    }

    @Test(expected = CoercingSerializeException.class)
    public void testSerialize_givenStringDateTime_throwException() {
        String date = "2023-08-15T00:00:00.000Z";
        graphqlLocalDateTimeCoercing.serialize(date);
    }

    @Test
    public void testParseValue_givenDate_returnsLocalDate() {
        Date date = new Date();
        LocalDateTime localDateTime = graphqlLocalDateTimeCoercing.parseValue(date);
        Assert.assertNotNull(localDateTime);
    }

    @Test(expected = CoercingParseValueException.class)
    public void testParseValue_givenStringDate_throwException() {
        String date = "2023-08-15T00:00:00.000Z";
        graphqlLocalDateTimeCoercing.parseValue(date);
    }

    @Test
    public void testParseLiteral_givenStringValue_returnsLocalDateTime() {
        StringValue stringValue = new StringValue("2023-08-15T00:00:00.000Z");
        Mockito.when(converter.parseDate(anyString())).thenReturn(LocalDateTime.now());
        LocalDateTime localDateTime = graphqlLocalDateTimeCoercing.parseLiteral(stringValue);
        Assert.assertNotNull(localDateTime);
    }

    @Test(expected = CoercingParseLiteralException.class)
    public void testParseLiteral_givenStringValue_throwException() {
        StringValue stringValue = new StringValue("2023-08-15T00:00:00.000Z");
        graphqlLocalDateTimeCoercing.parseLiteral(stringValue);
    }

    @Test
    public void testConvertImpl_givenObject_returnsLocalDateTime() {
        try {
            Method convertImpl = graphqlLocalDateTimeCoercing.getClass().getDeclaredMethod("convertImpl", Object.class);
            convertImpl.setAccessible(true);
            LocalDateTime localDateTime = (LocalDateTime)convertImpl.invoke(graphqlLocalDateTimeCoercing, LocalDateTime.now());
            Assert.assertNotNull(localDateTime);
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
