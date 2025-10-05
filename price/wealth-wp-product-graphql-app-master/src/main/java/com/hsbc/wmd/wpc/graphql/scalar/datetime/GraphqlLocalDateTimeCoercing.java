package com.dummy.wmd.wpc.graphql.scalar.datetime;

import graphql.Internal;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Alexey Zhokhov
 */
@Internal
public class GraphqlLocalDateTimeCoercing implements Coercing<LocalDateTime, String> {

    private final DateTimeFormatter formatter;
    private final LocalDateTimeConverter converter;

    private static final String INVALID_VALUE = "Invalid value '";
    private static final String TIME_TYPE = "' for LocalDateTime";

    public GraphqlLocalDateTimeCoercing(boolean zoneConversionEnabled, DateTimeFormatter formatter) {
        this.formatter = formatter;
        this.converter = new LocalDateTimeConverter(zoneConversionEnabled, formatter);
    }

    private LocalDateTime convertImpl(Object input) {
        if (input instanceof String) {
            LocalDateTime localDateTime = converter.parseDate((String) input);

            if (localDateTime != null) {
                return localDateTime;
            }
        } else if(input instanceof Date) {
            return LocalDateTime.ofInstant(((Date)input).toInstant(), ZoneId.of("UTC"));
        } else if (input instanceof LocalDateTime) {
            return (LocalDateTime) input;
        }
        return null;
    }

    @Override
    public String serialize(Object input) {
        if (input instanceof LocalDateTime) {
            return converter.formatDate((LocalDateTime) input, formatter);
        } else {
            LocalDateTime result = convertImpl(input);
            if (result == null) {
                throw new CoercingSerializeException(INVALID_VALUE + input + TIME_TYPE);
            }
            return converter.formatDate(result, formatter);
        }
    }

    @Override
    public LocalDateTime parseValue(Object input) {
        LocalDateTime result = convertImpl(input);
        if (result == null) {
            throw new CoercingParseValueException(INVALID_VALUE + input + TIME_TYPE);
        }
        return result;
    }

    @Override
    public LocalDateTime parseLiteral(Object input) {
        String value = ((StringValue) input).getValue();
        LocalDateTime result = convertImpl(value);
        if (result == null) {
            throw new CoercingParseLiteralException(INVALID_VALUE + input + TIME_TYPE);
        }

        return result;
    }

}
