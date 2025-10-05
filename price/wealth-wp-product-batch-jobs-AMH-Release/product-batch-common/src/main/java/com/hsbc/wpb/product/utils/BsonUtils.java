package com.dummy.wpb.product.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.bson.BsonDocument;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import java.util.Map;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class BsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static CodecRegistry codecRegistry;

    static {
        codecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromCodecs(
                        new IntegerCodec(),
                        new LongCodec(),
                        new StringCodec(),
                        new DateCodec(),
                        new LongCodec(),
                        new DoubleCodec(),
                        new BooleanCodec(),
                        new BsonBooleanCodec()
                )
        );
    }

    public static String toJson(Bson bson) {
        BsonDocument doc = bson.toBsonDocument(BsonDocument.class, codecRegistry);
        return doc.toJson();
    }

    @SneakyThrows
    public static Map<String, Object> toMap(Bson bson) {
        return objectMapper.readValue(toJson(bson), Map.class);
    }
}
