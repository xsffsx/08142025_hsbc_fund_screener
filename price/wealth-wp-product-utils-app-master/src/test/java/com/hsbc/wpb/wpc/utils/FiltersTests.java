package com.dummy.wpb.wpc.utils;

import com.mongodb.client.model.Filters;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.IntegerCodec;
import org.bson.codecs.LongCodec;
import org.bson.codecs.StringCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.junit.Test;

import static com.mongodb.client.model.Filters.*;
import static org.junit.Assert.assertNotNull;

public class FiltersTests {
    private static CodecRegistry codecRegistry = CodecRegistries.fromCodecs(new IntegerCodec(), new LongCodec(), new StringCodec());

    @Test
    public void test1() {
        Bson filter = and(or(eq("prodId", 12345), eq("prodType", "UT")), nin("prodSubType", 1,2,3));
        BsonDocument doc = filter.toBsonDocument(BsonDocument.class, codecRegistry);
        assertNotNull(doc.toJson());
    }

    @Test
    public void test2() {
        Bson filter = and(or(eq("a", 1), eq("b", 2)), eq("c", 3));
        BsonDocument doc = filter.toBsonDocument(BsonDocument.class, codecRegistry);
        assertNotNull(doc.toJson());
    }

    @Test
    public void test3() {
        Bson filter = and(and(and(eq("a", 1), eq("b", 2)), eq("c", 3)));
        BsonDocument doc = filter.toBsonDocument(BsonDocument.class, codecRegistry);
        assertNotNull(doc.toJson());
    }

    @Test
    public void test4(){
        /*
        db.col.find({
          $and: [
            { qty: { "$elemMatch": { part: "xyz", qty: { $lt: 11 } } } },
            { qty: { "$elemMatch": { qty: 40, size: "XL" } } }
          ]
        })
         */
        Bson filter = and(
                elemMatch("qty", and(eq("part", "xyz"), lt("qty", 11))),
                elemMatch("qty", and(eq("qty", 40), eq("size", "XL")))
        );
        BsonDocument doc = filter.toBsonDocument(BsonDocument.class, codecRegistry);
        assertNotNull(doc.toJson());
    }

    @Test
    public void test5(){
        Bson filter = or(eq("part", "xyz"), lt("qty", 11));
        filter = or(filter, ne("a", 1));
        BsonDocument doc = filter.toBsonDocument(BsonDocument.class, codecRegistry);
        assertNotNull(doc.toJson());
    }

    @Test
    public void test6(){
        Bson filter = not(ne("restrCustGroup.eligCuspCatInd", "A"));
        BsonDocument doc = filter.toBsonDocument(BsonDocument.class, codecRegistry);
        assertNotNull(doc.toJson());
    }

    @Test
    public void test7(){
        Document doc = Document.parse("{$group : {_id : \"max\", prodId : {$max : \"$prodId\"}}}");
        assertNotNull(doc);
    }
}
