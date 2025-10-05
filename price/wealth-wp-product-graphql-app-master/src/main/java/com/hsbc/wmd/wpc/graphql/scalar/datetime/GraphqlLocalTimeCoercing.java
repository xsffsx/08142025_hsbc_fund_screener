package com.dummy.wmd.wpc.graphql.scalar.datetime;

import graphql.Internal;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * @author Alexey Zhokhov
 */
@Internal
public class GraphqlLocalTimeCoercing implements Coercing<LocalTime, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME.withZone(ZoneOffset.UTC);

    private LocalTime convertImpl(Object input) {
        if (input instanceof String) {
            try {
                return LocalTime.parse((String) input, FORMATTER);
            } catch (DateTimeParseException ignored) {
                // nothing to-do
            }
        } else if (input instanceof LocalTime) {
            return (LocalTime) input;
        } else if (input instanceof Date) {
            return LocalDateTime.ofInstant(((Date)input).toInstant(), ZoneId.of("UTC")).toLocalTime();
        }
        return null;
    }

    @Override
    public String serialize(Object input) {
        if (input instanceof LocalTime) {
            return DateTimeHelper.toISOString((LocalTime) input);
        } else if(input instanceof Date) {
            LocalTime time = LocalDateTime.ofInstant(((Date)input).toInstant(), ZoneId.of("UTC")).toLocalTime();
            return DateTimeHelper.toISOString(time);
        } else {
            LocalTime result = convertImpl(input);
            if (result == null) {
                throw new CoercingSerializeException(getErrorMessage(input));
            }
            return DateTimeHelper.toISOString(result);
        }
    }

    @Override
    public LocalTime parseValue(Object input) {
        LocalTime result = convertImpl(input);
        if (result == null) {
            throw new CoercingParseValueException(getErrorMessage(input));
        }
        return result;
    }

    @Override
    public LocalTime parseLiteral(Object input) {
        String value = ((StringValue) input).getValue();
        LocalTime result = convertImpl(value);
        if (result == null) {
            throw new CoercingParseLiteralException(getErrorMessage(input));
        }

        return result;
    }

    private static String getErrorMessage(Object input) {
        return "Invalid value '" + input + "' for LocalTime";
    }
}
