package com.dummy.wmd.wpc.graphql.scalar.datetime;

import graphql.Internal;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import org.joda.time.DateTime;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.function.Function;

import static graphql.scalars.util.Kit.typeName;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#DateTime}
 */
@SuppressWarnings({"java:S3776","java:S1874"})
@Internal
public class productDateTimeScalar extends GraphQLScalarType {

    public productDateTimeScalar() {
        super("DateTime", "An RFC-3339 compliant DateTime Scalar", new Coercing<OffsetDateTime, String>() {
            @Override
            public String serialize(Object input) throws CoercingSerializeException {
                OffsetDateTime offsetDateTime;
                if (input instanceof OffsetDateTime) {
                    offsetDateTime = (OffsetDateTime) input;
                } else if (input instanceof Date) { // Convert Date into a OffsetDateTime
                    offsetDateTime = ScalarUtils.dateToOffsetDateTime((Date)input);
                } else if (input instanceof ZonedDateTime) {
                    offsetDateTime = ((ZonedDateTime) input).toOffsetDateTime();
                } else if (input instanceof String) {
                    offsetDateTime = parseOffsetDateTime(input.toString(), CoercingSerializeException::new);
                } else if (input instanceof Long) {
                    offsetDateTime = ScalarUtils.dateToOffsetDateTime(new Date((Long)input));
                } else {
                    throw new CoercingSerializeException(
                            "Expected something we can convert to 'java.time.OffsetDateTime' but was '" + typeName(input) + "'."
                    );
                }
                try {
                    return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(offsetDateTime);
                } catch (DateTimeException e) {
                    throw new CoercingSerializeException(
                            "Unable to turn TemporalAccessor into OffsetDateTime because of : '" + e.getMessage() + "'."
                    );
                }
            }

            @Override
            public OffsetDateTime parseValue(Object input) throws CoercingParseValueException {
                OffsetDateTime offsetDateTime;
                if (input instanceof OffsetDateTime) {
                    offsetDateTime = (OffsetDateTime) input;
                } else if (input instanceof Date) { // Convert Date into a OffsetDateTime
                    offsetDateTime = ScalarUtils.dateToOffsetDateTime((Date)input);
                } else if (input instanceof ZonedDateTime) {
                    offsetDateTime = ((ZonedDateTime) input).toOffsetDateTime();
                } else if (input instanceof String) {
                    offsetDateTime = parseOffsetDateTime(input.toString(), CoercingParseValueException::new);
                } else {
                    throw new CoercingParseValueException(
                            "Expected a 'String' but was '" + typeName(input) + "'."
                    );
                }
                return offsetDateTime;
            }

            @Override
            public OffsetDateTime parseLiteral(Object input) throws CoercingParseLiteralException {
                if (!(input instanceof StringValue)) {
                    throw new CoercingParseLiteralException(
                            "Expected AST type 'StringValue' but was '" + typeName(input) + "'."
                    );
                }
                return parseOffsetDateTime(((StringValue) input).getValue(), CoercingParseLiteralException::new);
            }

            private OffsetDateTime parseOffsetDateTime(String s, Function<String, RuntimeException> exceptionMaker) {
                try {
                    // Use joda time to handle more date time format, like "2019-12-11T06:52:27.642+0000" can't be handled before
                    DateTime dateTime = new DateTime(s);
                    Instant instant = Instant.ofEpochMilli(dateTime.getMillis());
                    return OffsetDateTime.ofInstant(instant, ZoneId.of(dateTime.getZone().getID()));
                } catch (DateTimeParseException e) {
                    throw exceptionMaker.apply("Invalid RFC3339 value : '" + s + "'. because of : '" + e.getMessage() + "'");
                }
            }
        });
    }

}
