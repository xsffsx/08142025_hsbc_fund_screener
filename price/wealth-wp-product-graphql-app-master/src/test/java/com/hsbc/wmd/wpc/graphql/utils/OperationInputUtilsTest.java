package com.dummy.wmd.wpc.graphql.utils;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.Operation;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import org.assertj.core.util.Lists;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class OperationInputUtilsTest {

    @Test
    public void testExtractAllPaths() {
        List<OperationInput> operations = Lists.newArrayList(
                new OperationInput(Operation.put, Field.prodMktPrcAmt, 1.235),
                new OperationInput(Operation.put, Field.tradeElig, new Document(Field.ageAllowTrdMinNum, 18)),
                new OperationInput(Operation.put, "a", new Document("b", new Document("c", "foo"))),
                new OperationInput(Operation.set, "$.undlAset[?(@.rowid=='9ed2ced0-8f23-4bcd-b08d-8227e9fec2d7')].asetUndlCde", "IR"),
                new OperationInput(Operation.set, "$.eqtyLinkInvst.undlStock[*].prodBreakEvenCallPrcAmt", 35.31)
        );

        List<String> paths = OperationInputUtils.extractAllPaths(operations);
        Assert.assertEquals(
                Lists.newArrayList(
                        Field.prodMktPrcAmt,
                        "tradeElig.ageAllowTrdMinNum",
                        "a.b.c",
                        "undlAset",
                        "eqtyLinkInvst.undlStock"),
                paths
        );

    }
}
