package com.dummy.wmd.wpc.graphql.utils;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import org.bson.Document;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class AmendmentResolverTest {
    @Test
    public void testNoChangeInLatest() {
        Document docBase = new Document();
        Document docChanged = new Document();
        Document docLatest = new Document();
        docBase.put(Field.revision, 1L);
        docChanged.put(Field.revision, 1L);
        docLatest.put(Field.revision, 1L);
        AmendmentResolver resolver = new AmendmentResolver(docBase, docChanged, docLatest);
        Document docResolved = resolver.resolve();
        assertEquals(docChanged, docResolved);
    }

    @Test
    public void testNoConflictWithLatest() {
        Document docBase = new Document();
        docBase.put(Field.revision, 1L);
        docBase.put(Field.prodName, "product name");
        docBase.put(Field.prodStatCde, "D");

        Document docChanged = new Document(docBase);
        docChanged.put(Field.prodStatCde, "A");

        Document docLatest = new Document(docBase);
        docLatest.put(Field.revision, 2L);
        docLatest.put(Field.prodTypeCde, "UT");

        AmendmentResolver resolver = new AmendmentResolver(docBase, docChanged, docLatest);
        Document docResolved = resolver.resolve();
        assertEquals("A", docResolved.getString(Field.prodStatCde));
    }

    @Test
    public void testFalseConflictWithLatest() {
        Document docBase = new Document();
        docBase.put(Field.revision, 1L);
        docBase.put(Field.prodName, "product name");
        docBase.put(Field.prodStatCde, "D");

        Document docChanged = new Document(docBase);
        docChanged.put(Field.prodStatCde, "A");

        Document docLatest = new Document(docBase);
        docLatest.put(Field.revision, 2L);
        docLatest.put(Field.prodStatCde, "A");
        docLatest.put(Field.prodTypeCde, "UT");

        AmendmentResolver resolver = new AmendmentResolver(docBase, docChanged, docLatest);
        Document docResolved = resolver.resolve();
        assertEquals("A", docResolved.getString(Field.prodStatCde));
    }

    @Test(expected = productErrorException.class)
    public void testTrueConflictWithLatest() {
        Document docBase = new Document();
        docBase.put(Field.revision, 2L);
        docBase.put(Field.prodName, "product name");
        docBase.put(Field.prodStatCde, "D");

        Document docChanged = new Document(docBase);
        docChanged.put(Field.prodStatCde, "A");
        docChanged.put(Field.recUpdtDtTm, "2024-10-17T02:31:24Z");
        docBase.put(Field.revision, 1L);

        Document docLatest = new Document(docBase);
        docLatest.put(Field.revision, 3L);
        docLatest.put(Field.prodStatCde, "E");
        docLatest.put(Field.recUpdtDtTm, "2024-10-17T02:32:21Z");
        docLatest.put(Field.prodTypeCde, "UT");

        AmendmentResolver resolver = new AmendmentResolver(docBase, docChanged, docLatest);
        Document docResolved = resolver.resolve();
    }

    @Test(expected = productErrorException.class)
    public void testTrueConflictWithSetAndDelete() {
        Document docBase = Document.parse(CommonUtils.readResource("/files/ELI-50504681.json"));

        Document docLatest = Document.parse(CommonUtils.readResource("/files/ELI-50504681.json")).append(Field.altId, null).append(Field.revision, Long.MAX_VALUE);

        Document docChanged = Document.parse(CommonUtils.readResource("/files/ELI-50504681.json"));
        Map<String, Document> altIdMap = docChanged.getList(Field.altId, Document.class)
                .stream()
                .collect(Collectors.toMap(altId -> altId.getString(Field.prodCdeAltClassCde), Function.identity()));
        altIdMap.get("M").put(Field.prodAltNum, "ABC123");
        AmendmentResolver resolver = new AmendmentResolver(docBase, docChanged, docLatest);
        Document docResolved = resolver.resolve();
    }

    @Test(expected = productErrorException.class)
    public void testTrueConflictWithAdd() {
        Document docBase = Document.parse(CommonUtils.readResource("/files/ELI-50504681.json")).append(Field.altId, Collections.emptyList());

        Document docLatest = Document.parse(CommonUtils.readResource("/files/ELI-50504681.json")).append(Field.revision, Long.MAX_VALUE);

        Document docChanged = Document.parse(CommonUtils.readResource("/files/ELI-50504681.json"));

        AmendmentResolver resolver = new AmendmentResolver(docBase, docChanged, docLatest);
        Document docResolved = resolver.resolve();
    }
}
