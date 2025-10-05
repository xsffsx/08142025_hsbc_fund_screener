package com.dummy.wmd.wpc.graphql.codec;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.anyLong;

@RunWith(MockitoJUnitRunner.class)
public class OffsetDateTimeCodecTest {

    @InjectMocks
    private OffsetDateTimeCodec offsetDateTimeCodec;
    @Mock
    private BsonReader bsonReader;
    @Mock
    private DecoderContext decoderContext;
    @Mock
    private BsonWriter bsonWriter;
    @Mock
    private OffsetDateTime offsetDateTime;
    @Mock
    private EncoderContext encoderContext;
    @Mock
    private Instant instant;

    @Test
    public void testDecode_givenBsonReaderAndDecoderContext_returnsLong() {
        Mockito.when(bsonReader.getCurrentBsonType()).thenReturn(BsonType.DATE_TIME);
        Mockito.when(bsonReader.readDateTime()).thenReturn(System.currentTimeMillis());
        OffsetDateTime dateTime = offsetDateTimeCodec.decode(bsonReader, decoderContext);
        Assert.assertNotNull(dateTime);
    }

    @Test(expected = CodecConfigurationException.class)
    public void testDecode_givenBsonReaderAndDecoderContext_throwException() {
        Mockito.when(bsonReader.getCurrentBsonType()).thenReturn(BsonType.ARRAY);
        offsetDateTimeCodec.decode(bsonReader, decoderContext);
    }

    @Test
    public void testEncode_givenBsonWriterAndOffsetDateTimeAndEncoderContext_DoesNotThrow() {
        try {
            Mockito.when(offsetDateTime.toInstant()).thenReturn(instant);
            Mockito.when(instant.toEpochMilli()).thenReturn(System.currentTimeMillis());
            Mockito.doNothing().when(bsonWriter).writeDateTime(anyLong());
            offsetDateTimeCodec.encode(bsonWriter, offsetDateTime, encoderContext);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test(expected = CodecConfigurationException.class)
    public void testEncode_givenBsonWriterAndOffsetDateTimeAndEncoderContext_throwException() {
        Mockito.when(offsetDateTime.toInstant()).thenReturn(instant);
        Mockito.when(instant.toEpochMilli()).thenReturn(System.currentTimeMillis());
        Mockito.doThrow(new ArithmeticException()).when(bsonWriter).writeDateTime(anyLong());
        offsetDateTimeCodec.encode(bsonWriter, offsetDateTime, encoderContext);
    }

    @Test
    public void testGetEncoderClass_NoArgs_returnsOffsetDateTimeClass() {
        Class<OffsetDateTime> offsetDateTimeClass = offsetDateTimeCodec.getEncoderClass();
        Assert.assertNotNull(offsetDateTimeClass);
    }
}
