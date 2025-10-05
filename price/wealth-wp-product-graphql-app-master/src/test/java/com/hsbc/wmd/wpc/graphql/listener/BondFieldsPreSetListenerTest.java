package com.dummy.wmd.wpc.graphql.listener;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.ProdTypeCde;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class BondFieldsPreSetListenerTest {

    @InjectMocks
    private BondFieldsPreSetListener bondFieldsPreSetListener;

    @Test
    public void testBeforeValidation_givenMapAndMap_replaceCcy() {
        Document oldProd = new Document();
        Document newProd = new Document();
        String ccyProdCde = "NZD";
        newProd.put(Field.prodTypeCde, ProdTypeCde.BOND_CD);
        newProd.put(Field.ccyProdCde, ccyProdCde);
        bondFieldsPreSetListener.beforeValidation(oldProd, newProd);
        assertEquals(ccyProdCde,newProd.get(Field.ccyProdMktPrcCde));
    }

    @Test
    public void testBeforeValidation_givenMapAndMap_ccyNoChangeForNonBond() {
        Document oldProd = new Document();
        Document newProd = new Document();
        String ccyProdCde = "NZD";
        newProd.put(Field.prodTypeCde, ProdTypeCde.UNIT_TRUST);
        newProd.put(Field.ccyProdCde, ccyProdCde);
        bondFieldsPreSetListener.beforeValidation(oldProd, newProd);
        assertNull(newProd.get(Field.ccyProdMktPrcCde));
    }

    @Test
    public void testBeforeValidation_givenMapAndMap_ccyNoChangeForEmptyCcy() {
        Document oldProd = new Document();
        Document newProd = new Document();
        newProd.put(Field.prodTypeCde, ProdTypeCde.BOND_CD);
        bondFieldsPreSetListener.beforeValidation(oldProd, newProd);
        assertNull(newProd.get(Field.ccyProdMktPrcCde));
    }

}

