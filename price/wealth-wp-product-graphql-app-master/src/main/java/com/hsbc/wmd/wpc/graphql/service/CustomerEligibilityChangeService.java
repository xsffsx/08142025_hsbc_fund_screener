package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.ActionCde;
import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.productConstants;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.utils.BooleanUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.dummy.wmd.wpc.graphql.constant.productConstants.*;
import static com.dummy.wmd.wpc.graphql.constant.ProdTypeCde.BOND_CD;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.ne;

@Service
public class CustomerEligibilityChangeService implements ChangeService {

    protected static final List<String> fieldList = Arrays.asList(
            Field._id,
            Field.ctryRecCde,
            Field.grpMembrRecCde,
            Field.prodName,
            Field.prodTypeCde,
            Field.prodSubtpCde,
            Field.prodAltPrimNum,
            Field.revision,
            Field.restrCustCtry,
            Field.restrCustGroup,
            Field.tradeElig,
            Field.formReqmt,
            Field.recCreatDtTm,
            Field.recUpdtDtTm);

    @Autowired
    private DocumentRevisionService documentRevisionService;

    @Autowired
    private ProductChangeService productChangeService;

    @Override
    public List<Error> validate(Document amendment) {
        Document custElig = (Document) amendment.get(Field.doc);
        Document product = mergeToProduct(custElig);
        List<Error> errors = productChangeService.validate(new Document(Field.doc, product).append(Field.actionCde, ActionCde.update.name()));
        errors.removeIf(err -> !StringUtils.containsAny(err.getJsonPath(), Field.tradeElig, Field.restrCustCtry, Field.restrCustGroup, Field.formReqmt));
        return errors;
    }

    @Override
    public void add(Document custElig) {
        update(custElig);
    }

    @Override
    public void update(Document custElig) {
        Document oldCustElig = this.findFirst(eq(Field._id, custElig.get(Field._id)));
        Document product = mergeToProduct(custElig);
        productChangeService.update(product);
        documentRevisionService.saveDocumentRevision(DocType.product_customer_eligibility, oldCustElig);

        updateForCmbIfNecessary(custElig);
    }

    private Document mergeToProduct(Document custElig) {
        Object id = custElig.get(Field._id);
        Document product = productChangeService.findFirst(eq(Field._id, id));

        if (null == product) {
            throw new productErrorException(productErrors.DocumentNotFound, "Product record not found, id=" + id);
        }

        custElig.computeIfPresent(Field.tradeElig, (k, tradeElig) -> {
            ((Map<String, Object>) tradeElig).putIfAbsent("operTypeCde", "T");
            product.put(Field.tradeElig, tradeElig);
            return tradeElig;
        });

        Stream.of(Field.restrCustCtry, Field.restrCustGroup, Field.formReqmt, Field.lastUpdatedBy).forEach(fieldName -> {
            Object value = custElig.get(fieldName);
            if (null != value) {
                product.put(fieldName, value);
            }
        });

        return product;
    }

    @Override
    public void delete(Document custElig) {
        Document product = productChangeService.findFirst(eq(Field._id, custElig.get(Field._id)));
        Stream.of(Field.tradeElig, Field.restrCustCtry, Field.restrCustGroup, Field.formReqmt).forEach(fieldName -> {
            if (custElig.containsKey(fieldName)) {
                product.remove(fieldName);
            }
        });

        product.put(Field.lastUpdatedBy, productConstants.wps);
        productChangeService.update(product);
    }

    @Override
    public Document findFirst(Bson filter) {
        Document product = productChangeService.findFirst(filter);
        Document custElig = new Document();
        fieldList.forEach(field -> custElig.put(field, product.get(field)));
        return custElig;
    }


    /**
     * reference:
     * <a href = "https://alm-github.systems.uk.dummy/GPBW-Technology/wealth-wp-wpc-app-hk/blob/aws-dev/WPS_Integration/src/main/java/com/dummy/wpc/ism10/si/services/maint/MaintProdCustEligibility.java#L666">MaintProdCustEligibility</a>
     */
    private void updateForCmbIfNecessary(Document cmbProduct) {
        String ctryRecCde = cmbProduct.getString(Field.ctryRecCde);
        String grpMembrRecCde = cmbProduct.getString(Field.grpMembrRecCde);
        String prodTypeCde = cmbProduct.getString(Field.prodTypeCde);

        boolean cmbBond = BooleanUtils.and(
                StringUtils.equals(HK, ctryRecCde),
                StringUtils.equals(dummy, grpMembrRecCde),
                StringUtils.equals(BOND_CD, prodTypeCde)
        );

        // just for hk cmb bond product
        if (!cmbBond) {
            return;
        }

        Bson filter = Filters.and(
                eq(Field.ctryRecCde, ctryRecCde),
                eq(Field.grpMembrRecCde, HBAP),
                eq(Field.prodTypeCde, BOND_CD),
                eq(Field.prodAltPrimNum, cmbProduct.getString(Field.prodAltPrimNum)),
                ne(Field.prodStatCde, "D")
        );

        Document wpbProduct = productChangeService.findFirst(filter);
        // if not tradeElig in wpb side, skip it
        if (null == wpbProduct || !wpbProduct.containsKey(Field.tradeElig)) {
            return;
        }

        applyChangeToWpb(cmbProduct, wpbProduct);

        productChangeService.update(wpbProduct);
        Document custElig = new Document();
        fieldList.forEach(field -> custElig.put(field, wpbProduct.get(field)));
        documentRevisionService.saveDocumentRevision(DocType.product_customer_eligibility, custElig);
    }

    private void applyChangeToWpb(Document cmbProduct, Document wpbProduct) {
        Document wpbTradeElig = wpbProduct.get(Field.tradeElig, Document.class);
        Document cmbTradeElig = cmbProduct.get(Field.tradeElig, Document.class);
        wpbTradeElig.put(Field.ageAllowTrdMinNum, cmbTradeElig.get(Field.ageAllowTrdMinNum));
        wpbTradeElig.put(Field.ageAllowTrdMaxNum, cmbTradeElig.get(Field.ageAllowTrdMaxNum));
        wpbTradeElig.put(Field.ovridMinAgeRestrInd, cmbTradeElig.get(Field.ovridMinAgeRestrInd));
        wpbTradeElig.put(Field.ovridMaxAgeRestrInd, cmbTradeElig.get(Field.ovridMaxAgeRestrInd));

        wpbProduct.put(Field.restrCustCtry, cmbProduct.get(Field.restrCustCtry));
        wpbProduct.put(Field.formReqmt, cmbProduct.get(Field.formReqmt));
        wpbProduct.put(Field.lastUpdatedBy, productConstants.wps);
    }
}
