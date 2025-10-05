package com.dummy.wmd.wpc.graphql.utils;

import com.google.gson.Gson;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.Operation;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import org.assertj.core.util.Lists;
import org.bson.Document;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JsonPathUtilsTest {
    private Gson gson = new Gson();

    @Test
    public void testApplyChanges_giveSimplePath(){
        Document doc = new Document();
        List<OperationInput> ops = Lists.list(new OperationInput(Operation.put, "a", 123));
        JsonPathUtils.applyChanges(doc, ops);
        assertEquals(1, doc.size());
        JSONAssert.assertEquals("{\"a\": 123}", gson.toJson(doc), JSONCompareMode.STRICT);

        ops = Lists.list(new OperationInput(Operation.put, "a", 456));
        JsonPathUtils.applyChanges(doc, ops);
        assertEquals(1, doc.size());
        JSONAssert.assertEquals("{\"a\": 456}", gson.toJson(doc), JSONCompareMode.STRICT);
    }

    @Test
    public void testApplyChanges_giveEmbedPath(){
        Document doc = new Document();
        List<OperationInput> ops = Lists.list(new OperationInput(Operation.put, "a.b", 123));
        JsonPathUtils.applyChanges(doc, ops);
        assertEquals(1, doc.size());
        JSONAssert.assertEquals("{\"a\": {\"b\":123}}", gson.toJson(doc), JSONCompareMode.STRICT);
    }

    @Test
    public void testApplyChanges_giveListPath() {
        Document doc = new Document();
        Document pAltId = new Document()
                .append(Field.rowid, "ABC123")
                .append(Field.prodAltPrimNum, "U12345")
                .append(Field.prodCdeAltClassCde, "P");

        JsonPathUtils.applyChanges(doc, Collections.singletonList(new OperationInput(Operation.add, "$.altId", pAltId)));
        assertEquals(1,  doc.getList(Field.altId, Document.class).size());
        JSONAssert.assertEquals(
                "{\"altId\":[{\"rowid\":\"ABC123\",\"prodAltPrimNum\":\"U12345\",\"prodCdeAltClassCde\":\"P\"}]}",
                gson.toJson(doc),
                JSONCompareMode.STRICT);

        Document mAltId = new Document()
                .append(Field.rowid, "DEF456")
                .append(Field.prodAltPrimNum, "U12345")
                .append(Field.prodCdeAltClassCde, "M");

        JsonPathUtils.applyChanges(doc, Collections.singletonList(new OperationInput(Operation.add, "$.altId", mAltId)));
        assertEquals(2,  doc.getList(Field.altId, Document.class).size());
        JSONAssert.assertEquals(
                "{\"altId\":[{\"rowid\":\"ABC123\",\"prodAltPrimNum\":\"U12345\",\"prodCdeAltClassCde\":\"P\"},{\"rowid\":\"DEF456\",\"prodAltPrimNum\":\"U12345\",\"prodCdeAltClassCde\":\"M\"}]}",
                gson.toJson(doc),
                JSONCompareMode.STRICT);
    }


    @Test
    public void testRealizeListElement(){
        String result = JsonPathUtils.realizeListElement("debtInstm.credRtng[*].creditRtngAgcyCde", "debtInstm.credRtng[0].creditRtngCde");
        assertEquals("debtInstm.credRtng[0].creditRtngAgcyCde", result);
    }

    @Test
    public void testRealizeListElement1(){
        String result = JsonPathUtils.realizeListElement("debtInstm[*].credRtng[*].creditRtngAgcyCde", "debtInstm[1].credRtng[2].creditRtngCde");
        assertEquals("debtInstm[1].credRtng[2].creditRtngAgcyCde", result);
    }
}
