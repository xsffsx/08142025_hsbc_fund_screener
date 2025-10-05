package com.dummy.wmd.wpc.graphql.scalar.datetime;

import graphql.Internal;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.function.Function;

import static graphql.scalars.util.Kit.typeName;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#Date}
 */
@SuppressWarnings({"java:S3776","java:S1874"})
@Internal
public class productDateScalar extends GraphQLScalarType {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public productDateScalar() {
        super("Date", "An RFC-3339 compliant Full Date Scalar", new Coercing<LocalDate, String>() {
            @Override
            public String serialize(Object input) throws CoercingSerializeException {
                TemporalAccessor temporalAccessor;
                if (input instanceof TemporalAccessor) {
                    temporalAccessor = (TemporalAccessor) input;
                } else if (input instanceof Date) { // Convert Date into a TemporalAccessor
                    temporalAccessor = ScalarUtils.dateToOffsetDateTime((Date)input);
                } else if (input instanceof String) {
                    temporalAccessor = parseLocalDate(input.toString(), CoercingSerializeException::new);
                } else if (input instanceof Long) {
                    temporalAccessor = ScalarUtils.dateToOffsetDateTime(new Date((Long)input));
                } else {
                    throw new CoercingSerializeException(
                            "Expected a 'String' or 'java.time.temporal.TemporalAccessor' but was '" + typeName(input) + "'."
                    );
                }
                try {
                    return dateFormatter.format(temporalAccessor);
                } catch (DateTimeException e) {
                    throw new CoercingSerializeException(
                            "Unable to turn TemporalAccessor into full date because of : '" + e.getMessage() + "'."
                    );
                }
            }

            @Override
            public LocalDate parseValue(Object input) throws CoercingParseValueException {
                TemporalAccessor temporalAccessor;
                if (input instanceof TemporalAccessor) {
                    temporalAccessor = (TemporalAccessor) input;
                } else if (input instanceof Date) { // Convert Date into a TemporalAccessor
                    temporalAccessor = ScalarUtils.dateToOffsetDateTime((Date)input);
                } else if (input instanceof String) {
                    temporalAccessor = parseLocalDate(input.toString(), CoercingParseValueException::new);
                } else {
                    throw new CoercingParseValueException(
                            "Expected a 'String' or 'java.time.temporal.TemporalAccessor' but was '" + typeName(input) + "'."
                    );
                }
                try {
                    return LocalDate.from(temporalAccessor);
                } catch (DateTimeException e) {
                    throw new CoercingParseValueException(
                            "Unable to turn TemporalAccessor into full date because of : '" + e.getMessage() + "'."
                    );
                }
            }

            @Override
            public LocalDate parseLiteral(Object input) throws CoercingParseLiteralException {
                if (!(input instanceof StringValue)) {
                    throw new CoercingParseLiteralException(
                            "Expected AST type 'StringValue' but was '" + typeName(input) + "'."
                    );
                }
                return parseLocalDate(((StringValue) input).getValue(), CoercingParseLiteralException::new);
            }

            private LocalDate parseLocalDate(String s, Function<String, RuntimeException> exceptionMaker) {
                try {
                    int idx = s.indexOf("T00:00:00");
                    if(10 == idx){
                        // error torrance, ignore the time part in case of like this 2019-11-01T00:00:00.000+0000, or 2019-11-01T00:00:00Z
                        s = s.substring(0, 10);  // remove the tailing 00:00:00
                    }
                    TemporalAccessor temporalAccessor = dateFormatter.parse(s);
                    return LocalDate.from(temporalAccessor);
                } catch (DateTimeParseException e) {
                    throw exceptionMaker.apply("Invalid RFC3339 full date value : '" + s + "'. because of : '" + e.getMessage() + "'");
                }
            }
        });
    }
}
