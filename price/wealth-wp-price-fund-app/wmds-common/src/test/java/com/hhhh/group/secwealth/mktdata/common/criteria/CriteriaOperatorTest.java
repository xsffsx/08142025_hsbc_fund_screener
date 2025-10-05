package com.hhhh.group.secwealth.mktdata.common.criteria;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CriteriaOperatorTest {

    @Test
    void test() throws Exception {

        Assertions.assertEquals("MIN", CriteriaOperator.MIN);

        Assertions.assertEquals("MAX", CriteriaOperator.MAX);

        Assertions.assertEquals(" OR ", CriteriaOperator.OR);

        Assertions.assertEquals(" AND ", CriteriaOperator.AND);

        Assertions.assertEquals(" NOT ", CriteriaOperator.NOT);

        Assertions.assertEquals("in", CriteriaOperator.IN_OPERATOR);

        Assertions.assertEquals("IS IN SET", CriteriaOperator.IN_OPERATOR_EXPRESSION);

        Assertions.assertEquals(':', CriteriaOperator.COLON);

        Assertions.assertEquals(',', CriteriaOperator.COMMA);

        Assertions.assertEquals("*", CriteriaOperator.INFINITE);

        Assertions.assertEquals("gt", CriteriaOperator.GT_OPERATOR);

        Assertions.assertEquals("ge", CriteriaOperator.GE_OPERATOR);

        Assertions.assertEquals("lt", CriteriaOperator.LT_OPERATOR);

        Assertions.assertEquals("le", CriteriaOperator.LE_OPERATOR);

        Assertions.assertEquals("eq", CriteriaOperator.EQ_OPERATOR);

        Assertions.assertEquals("ne", CriteriaOperator.NE_OPERATOR);
    }
}
