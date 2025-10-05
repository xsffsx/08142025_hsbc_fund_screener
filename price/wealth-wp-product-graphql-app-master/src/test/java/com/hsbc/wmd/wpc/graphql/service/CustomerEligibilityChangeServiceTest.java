package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import org.assertj.core.util.Lists;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import com.dummy.wmd.wpc.graphql.validator.Error;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.stream.Stream;

import static com.dummy.wmd.wpc.graphql.constant.productConstants.HBAP;
import static org.mockito.ArgumentMatchers.any;

public class CustomerEligibilityChangeServiceTest {

    DocumentRevisionService documentRevisionService = Mockito.mock(DocumentRevisionService.class);

    ProductChangeService productChangeService = Mockito.mock(ProductChangeService.class);

    CustomerEligibilityChangeService custEligChangeService = new CustomerEligibilityChangeService();

    Document product = null;

    Document custElig = null;

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(custEligChangeService, "documentRevisionService", documentRevisionService);
        ReflectionTestUtils.setField(custEligChangeService, "productChangeService", productChangeService);

        product = Document.parse(CommonUtils.readResource("/files/ELI-50504681.json"));
        custElig = Document.parse(CommonUtils.readResource("/files/custElig-50504681.json"));
    }


    @Test
    public void testValidate() {
        Error formReqmtError = new Error("formReqmt", "formReqmt@unique", "Key combination of [formReqCde] in the array should be unique");

        Mockito.when(productChangeService.validate(any())).thenReturn(Lists.newArrayList(
                new Error("dispComProdSrchInd", "dispComProdSrchInd@required", "Required"),
                formReqmtError
        ));

        Mockito.when(productChangeService.findFirst(any())).thenReturn(product);
        Document amendment = new Document();
        amendment.put(Field.doc, new Document(Field._id, 123456L));
        List<Error> errors = custEligChangeService.validate(amendment);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals(formReqmtError, errors.get(0));
    }

    @Test
    public void testWpbAdd_givenWpbEliProduct() {
        Mockito.when(productChangeService.findFirst(any())).thenReturn(product);
        ArgumentCaptor<Document> productCaptor = ArgumentCaptor.forClass(Document.class);
        Mockito.doNothing().when(productChangeService).update(productCaptor.capture());
        custEligChangeService.add(custElig);

        Document product = productCaptor.getValue();

        Assert.assertEquals(product.get(Field.tradeElig), custElig.get(Field.tradeElig));
        Assert.assertEquals(product.get(Field.restrCustCtry), custElig.get(Field.restrCustCtry));
    }

    @Test
    public void testCmbUpdate_givenCmbBondProduct() {
        Document cmbCustElig = Document.parse(CommonUtils.readResource("/files/custElig-cmbbond-74200842.json"));
        Document wpbCustElig = Document.parse(CommonUtils.readResource("/files/custElig-wpbbond-61125568.json"));

        Document cmbProduct = new Document(cmbCustElig);
        Document wpbProduct = new Document(wpbCustElig);
        Stream.of(Field.tradeElig,Field.restrCustGroup,Field.restrCustCtry,Field.formReqmt).forEach(cmbProduct::remove);
        Mockito.when(productChangeService.findFirst(any()))
                .thenAnswer(invocation -> {
                    Bson filter = invocation.getArgument(0);
                    return filter.toString().contains(HBAP) ? wpbProduct : cmbProduct;
                });

        ArgumentCaptor<Document> productCaptor = ArgumentCaptor.forClass(Document.class);
        Mockito.doNothing().when(productChangeService).update(productCaptor.capture());

        custEligChangeService.update(cmbCustElig);
        List<Document> allCustEligs = productCaptor.getAllValues();

        Assert.assertEquals(2, allCustEligs.size());

        Document wpbUpdatedProduct = productCaptor.getValue();

        Assert.assertNotEquals(wpbUpdatedProduct.get(Field.tradeElig, Document.class).get("ovridNatlChkInd"), cmbCustElig.get(Field.tradeElig, Document.class).get("ovridNatlChkInd"));
        Assert.assertNotEquals(wpbUpdatedProduct.get(Field.restrCustGroup), cmbCustElig.get(Field.restrCustGroup));
        Assert.assertEquals(wpbUpdatedProduct.get(Field.tradeElig, Document.class).get(Field.ageAllowTrdMaxNum), cmbCustElig.get(Field.tradeElig, Document.class).get(Field.ageAllowTrdMaxNum));
        Assert.assertEquals(wpbUpdatedProduct.get(Field.restrCustCtry), cmbCustElig.get(Field.restrCustCtry));
    }

    @Test
    public void testDelete() {
        Assert.assertNotNull(product.get(Field.tradeElig));
        Assert.assertNotNull(product.get(Field.restrCustCtry));

        Mockito.when(productChangeService.findFirst(any())).thenReturn(product);
        ArgumentCaptor<Document> productCaptor = ArgumentCaptor.forClass(Document.class);
        Mockito.doNothing().when(productChangeService).update(productCaptor.capture());
        custEligChangeService.delete(custElig);

        Document product = productCaptor.getValue();

        Assert.assertNull(product.get(Field.tradeElig));
        Assert.assertNull(product.get(Field.restrCustCtry));

    }
}
