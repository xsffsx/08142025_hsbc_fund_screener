package com.dummy.wmd.wpc.graphql.codec;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecConfigurationException;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static java.lang.String.format;

/**
 * OffsetDateTime Codec.
 *
 * <p>Encodes and decodes {@code OffsetDateTime} objects to and from {@code DateTime}. Data is stored to millisecond accuracy.</p>
 * <p>Converts the {@code OffsetDateTime} values to and from {@link ZoneOffset#UTC}.</p>
 * <p>Note: Requires Java 8 or greater.</p>
 *
 * @mongodb.driver.manual reference/bson-types
 * @since 3.7
 */
public class OffsetDateTimeCodec implements Codec<OffsetDateTime> {

    private long validateAndReadDateTime(final BsonReader reader) {
        BsonType currentType = reader.getCurrentBsonType();
        if (!currentType.equals(BsonType.DATE_TIME)) {
            throw new CodecConfigurationException(format("Could not decode into %s, expected '%s' BsonType but got '%s'.",
                    getEncoderClass().getSimpleName(), BsonType.DATE_TIME, currentType));
        }
        return reader.readDateTime();
    }

    @Override
    public OffsetDateTime decode(final BsonReader reader, final DecoderContext decoderContext) {
        return Instant.ofEpochMilli(validateAndReadDateTime(reader)).atZone(ZoneOffset.UTC).toOffsetDateTime();
    }

    /**
     * {@inheritDoc}
     * <p>Converts the {@code OffsetDateTime} to {@link ZoneOffset#UTC} via {@link OffsetDateTime#toInstant()}.</p>
     * @throws CodecConfigurationException if the OffsetDateTime cannot be converted to a valid Bson DateTime.
     */
    @Override
    public void encode(final BsonWriter writer, final OffsetDateTime value, final EncoderContext encoderContext) {
        try {
            writer.writeDateTime(value.toInstant().toEpochMilli());
        } catch (ArithmeticException e) {
            throw new CodecConfigurationException(format("Unsupported OffsetDateTime value '%s' could not be converted to milliseconds: %s",
                    value, e.getMessage()), e);
        }
    }

    @Override
    public Class<OffsetDateTime> getEncoderClass() {
        return OffsetDateTime.class;
    }
}