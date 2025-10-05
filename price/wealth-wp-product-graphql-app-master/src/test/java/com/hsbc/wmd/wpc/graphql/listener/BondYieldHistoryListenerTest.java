package com.dummy.wmd.wpc.graphql.listener;


import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.listener.BondYieldHistoryListener;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class BondYieldHistoryListenerTest {


    private BondYieldHistoryListener bondYieldHistoryListener = new BondYieldHistoryListener();


    private Document newProduct;

    private Document oldProduct;

    @Before
    public void setUp() {
        newProduct = CommonUtils.readResourceAsDocument("/files/BOND-286859053.json");
        oldProduct = CommonUtils.readResourceAsDocument("/files/BOND-286859053.json");
    }

    @Test
    public void testBeforeValidation_givenValidPrice() {
        Document newDebtInstm = newProduct.get(Field.debtInstm, Document.class);

        newDebtInstm.put(Field.yieldEffDt, LocalDate.now());
        bondYieldHistoryListener.beforeValidation(oldProduct, newProduct);

        List<Document> yieldHistList = newProduct.get(Field.debtInstm, Document.class).getList(Field.yieldHist, Document.class);
        Assert.assertEquals(yieldHistList.get(yieldHistList.size() - 1).get(Field.yieldEffDt), LocalDate.now());
    }

    @Test
    public void testBeforeValidation_givenInvalidPrice() {
        Document newDebtInstm = newProduct.get(Field.debtInstm, Document.class);
        Document oldDebtInstm = oldProduct.get(Field.debtInstm, Document.class);

        newDebtInstm.put(Field.yieldEffDt, LocalDate.now());
        Stream.of(Field.yieldBidPct, Field.yieldToCallBidPct, Field.yieldToMturBidPct, Field.yieldBidClosePct, Field.yieldOfferPct,
                Field.yieldToCallOfferPct, Field.yieldToMturOfferText, Field.yieldOfferClosePct).forEach(newDebtInstm::remove);
        bondYieldHistoryListener.beforeValidation(oldProduct, newProduct);
        Assert.assertEquals(oldDebtInstm, newProduct.get(Field.debtInstm, Document.class));

        newDebtInstm = newProduct.get(Field.debtInstm, Document.class);
        newDebtInstm.remove(Field.yieldEffDt);
        bondYieldHistoryListener.beforeValidation(oldProduct, newProduct);
        Assert.assertEquals(oldDebtInstm, newProduct.get(Field.debtInstm, Document.class));
    }
}
