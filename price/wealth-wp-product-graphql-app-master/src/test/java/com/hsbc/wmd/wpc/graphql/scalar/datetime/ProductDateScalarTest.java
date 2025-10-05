package com.dummy.wmd.wpc.graphql.scalar.datetime;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;

public class productDateScalarTest {

    private productDateScalar productDateScalar;
    private Coercing coercing;

    @Before
    public void setUp() {
        productDateScalar = new productDateScalar();
        coercing = productDateScalar.getCoercing();
    }

    @Test
    public void testSerialize_givenObject_DoesNotThrow() {
        try {
            TemporalAccessor temporalAccessor = ScalarUtils.dateToOffsetDateTime(new Date());
            coercing.serialize(temporalAccessor);
            coercing.serialize(new Date());
            coercing.serialize("2023-08-28");
            coercing.serialize(System.currentTimeMillis());
        } catch (CoercingSerializeException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test(expected = CoercingSerializeException.class)
    public void testSerialize_givenObject_throwException() {
        coercing.serialize(new Object());
    }

    @Test
    public void testParseLiteral_givenObject_returnsLocalDate() {
        StringValue stringValue = new StringValue("2023-08-28");
        Object localDate = coercing.parseLiteral(stringValue);
        Assert.assertNotNull(localDate);
    }

    @Test(expected = CoercingParseLiteralException.class)
    public void testParseLiteral_givenObject_throwException() {
        coercing.parseLiteral("");
    }

    @Test
    public void testParseValue_givenObject_DoesNotThrow() {
        try {
            TemporalAccessor temporalAccessor = ScalarUtils.dateToOffsetDateTime(new Date());
            coercing.parseValue(temporalAccessor);
            coercing.parseValue(new Date());
            coercing.parseValue("2023-08-28T00:00:00");
        } catch (CoercingSerializeException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test(expected = CoercingParseValueException.class)
    public void testParseValue_givenObject_throwException() {
        coercing.parseValue(new Object());
    }

    @Test(expected = CoercingParseValueException.class)
    public void testParseValue_givenObject_throwException2() {
        MockedStatic<LocalDate> localDateMockedStatic= Mockito.mockStatic(LocalDate.class);
        try {
            TemporalAccessor temporalAccessor = Mockito.mock(TemporalAccessor.class);
            localDateMockedStatic.when(() -> LocalDate.from(any(TemporalAccessor.class))).thenThrow(new DateTimeException("error"));
            coercing.parseValue(temporalAccessor);
        } finally {
            localDateMockedStatic.close();
        }
    }

    @Test(expected = InvocationTargetException.class)
    public void testParseLocalDate_givenStringAndFuntion_throwException() throws Exception{
        Method method = coercing.getClass().getDeclaredMethod("parseLocalDate", String.class, Function.class);
        method.setAccessible(true);
        Function<String, RuntimeException> exceptionMaker = str ->  new CoercingParseLiteralException(str);
        method.invoke(coercing, "2023-08-32T00:00:00", exceptionMaker);
    }
}
