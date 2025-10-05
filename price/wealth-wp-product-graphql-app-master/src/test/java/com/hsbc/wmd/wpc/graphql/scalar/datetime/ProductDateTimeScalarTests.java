package com.dummy.wmd.wpc.graphql.scalar.datetime;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class productDateTimeScalarTests {

    private productDateTimeScalar productDateTimeScalar;
    private Coercing coercing;

    @Before
    public void setUp() {
        productDateTimeScalar = new productDateTimeScalar();
        coercing = productDateTimeScalar.getCoercing();
    }

    @Test
    public void testSerialize_givenObject_DoesNotThrow() {
        try {
            OffsetDateTime offsetDateTime = ScalarUtils.dateToOffsetDateTime(new Date());
            coercing.serialize(offsetDateTime);
            coercing.serialize(new Date());
            coercing.serialize(ZonedDateTime.of(LocalDateTime.now(), ZoneOffset.UTC));
            coercing.serialize("2023-08-31T15:37:29.227");
            coercing.serialize(System.currentTimeMillis());
        } catch (CoercingSerializeException e) {
            Assert.fail();
        }
    }

    @Test(expected = CoercingSerializeException.class)
    public void testSerialize_givenObject_throwException() {
        coercing.serialize(123.4);
    }

    @Test
    public void testParseValue_givenObject_DoesNotThrow() {
        try {
            OffsetDateTime offsetDateTime = ScalarUtils.dateToOffsetDateTime(new Date());
            coercing.parseValue(new Date());
            coercing.parseValue(offsetDateTime);
            coercing.parseValue(ZonedDateTime.of(LocalDateTime.now(), ZoneOffset.UTC));
            coercing.parseValue("2023-08-31T15:37:29.227");
        } catch (CoercingParseValueException e) {
            Assert.fail();
        }
    }

    @Test(expected = CoercingParseValueException.class)
    public void testParseValue_givenObject_throwException() {
        coercing.parseValue(123.4);
    }

    @Test
    public void testParseLiteral_givenObject_returnsOffsetDateTime() {
        StringValue stringValue = new StringValue("2023-08-28");
        Object offsetDateTime = coercing.parseLiteral(stringValue);
        Assert.assertNotNull(offsetDateTime);
    }

    @Test(expected = CoercingParseLiteralException.class)
    public void testParseLiteral_givenObject_throwException1() {
        coercing.parseLiteral("2023-08-28");
    }

    @Test(expected = CoercingParseLiteralException.class)
    public void testParseLiteral_givenObject_throwException2() {
        MockedStatic<OffsetDateTime> dateTimeMockedStatic= Mockito.mockStatic(OffsetDateTime.class);
        try {
            dateTimeMockedStatic.when(() -> OffsetDateTime.ofInstant(any(Instant.class), any(ZoneId.class))).
                    thenThrow(new DateTimeParseException("error", "error", 1));
            StringValue stringValue = new StringValue("2023-08-28");
            coercing.parseLiteral(stringValue);
        } finally {
            dateTimeMockedStatic.close();
        }
    }
}
