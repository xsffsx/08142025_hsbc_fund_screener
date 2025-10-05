package com.dummy.wpb.product.writer;

import com.dummy.wpb.product.condition.ConditionalOnJobParameters;
import com.dummy.wpb.product.configuration.SystemUpdateConfigHolder;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.service.ProductFormatService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.ReferenceDataService;
import com.dummy.wpb.product.utils.JsonPathUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dummy.wpb.product.constant.BatchConstants.*;
import static com.dummy.wpb.product.constant.BatchImportAction.ADD;
import static com.dummy.wpb.product.constant.BatchImportAction.UPDATE;

@Component
@StepScope
@ConditionalOnJobParameters({"ctryRecCde", "HK"})
public class HKBondProductImportWriter extends BondProductImportWriter {
    @Value("#{jobParameters['ctryRecCde']}")
    private String ctryRecCde;

    @Value("#{jobParameters['grpMembrRecCde']}")
    private String grpMembrRecCde;

    @Value("#{jobParameters['systemCde']}")
    private String systemCde;

    @Autowired
    private SystemUpdateConfigHolder updateConfigHolder;

    @Autowired
    ProductFormatService productFormatService;

    public HKBondProductImportWriter(ProductService productService, ReferenceDataService referenceDataService) {
        super(productService, referenceDataService);
    }

    @SneakyThrows
    @Override
    public void afterWrite(List<? extends ProductStreamItem> streamItemList) {
        List<ProductStreamItem> otherEntityItemList = new ArrayList<>();

        //special function --- when GSOPS update/create BOND prod need update/create CMB same prod code.
        if (SYS_CDE_AMHGSOPS_PD.equals(systemCde) && HBAP.equals(grpMembrRecCde)) {
            otherEntityItemList = buildOtherEntityStreamItemList(streamItemList, dummy);
        }

        // special function --- when CMB prod create, if WPB same prod not exist need create also.
        if (RBT.equals(systemCde) && dummy.equals(grpMembrRecCde)) {
            List<ProductStreamItem> addStreamItemList = streamItemList.stream().filter(item -> item.getActionCode() == ADD).collect(Collectors.toList());
            otherEntityItemList = buildOtherEntityStreamItemList(addStreamItemList, HBAP);
        }

        super.beforeWrite(otherEntityItemList);
        super.write(otherEntityItemList);
    }

    private List<ProductStreamItem> buildOtherEntityStreamItemList(List<? extends ProductStreamItem> streamItemList, String grpMembrRecCde) {
        if (CollectionUtils.isEmpty(streamItemList)) {
            return Collections.emptyList();
        }

        Criteria[] criteriaArr = new Criteria[streamItemList.size()];
        for (int i = 0; i < streamItemList.size(); i++) {
            ProductStreamItem streamItem = streamItemList.get(i);
            Document updatedProduct = streamItem.getUpdatedProduct();
            Criteria criteria = new Criteria()
                    .and(Field.ctryRecCde).is(ctryRecCde)
                    .and(Field.grpMembrRecCde).is(grpMembrRecCde)
                    .and(Field.prodTypeCde).is(updatedProduct.get(Field.prodTypeCde))
                    .and(Field.prodAltPrimNum).is(updatedProduct.get(Field.prodAltPrimNum));
            criteriaArr[i] = criteria;
        }
        List<Document> productList = productService.productByFilters(new Criteria().orOperator(criteriaArr).getCriteriaObject(), Collections.singletonList(Field.fieldGroupCtoff));

        List<ProductStreamItem> otherEntityStreamItemList = new ArrayList<>();
        for (ProductStreamItem streamItem : streamItemList) {
            Document updatedProduct = streamItem.getUpdatedProduct();
            Document otherEntityHistory = productList.stream()
                    .filter(product -> StringUtils.equals(product.getString(Field.prodAltPrimNum), updatedProduct.getString(Field.prodAltPrimNum)))
                    .findFirst()
                    .orElse(null);

            ProductStreamItem otherEntityStreamItem = new ProductStreamItem();
            otherEntityStreamItem.setActionCode(otherEntityHistory == null ? ADD : UPDATE);

            if (otherEntityStreamItem.getActionCode() != streamItem.getActionCode()) {
                continue;
            }

            otherEntityStreamItem.setOriginalProduct(otherEntityHistory);


            Document otherEntityProduct = formatOtherEntityProduct(otherEntityHistory, updatedProduct, grpMembrRecCde);

            //restore isrBondName and grntrName
            Stream.of("debtInstm.isrBondName", "debtInstm.grntrName").forEach(name ->
                    JsonPathUtils.setValue(otherEntityProduct, name, JsonPathUtils.readValue(streamItem.getImportProduct(), name))
            );

            if (ADD == otherEntityStreamItem.getActionCode()) {
                otherEntityStreamItem.setUpdatedProduct(otherEntityProduct);
            } else {
                Document otherEntityUpdatedProduct = JsonUtil.deepCopy(otherEntityStreamItem.getOriginalProduct());
                otherEntityStreamItem.setUpdatedProduct(otherEntityUpdatedProduct);
                List<String> updateAttrs = updateConfigHolder.getUpdateAttrs(systemCde, CollectionName.product.name());
                productFormatService.formatByUpdateAttrs(otherEntityProduct, otherEntityUpdatedProduct, updateAttrs);
            }

            otherEntityStreamItem.setImportProduct(otherEntityProduct);
            otherEntityStreamItemList.add(otherEntityStreamItem);
        }

        return otherEntityStreamItemList;
    }

    private Document formatOtherEntityProduct(Document otherEntityHistory, Document updatedProduct, String grpMembrRecCde) {
        Document otherEntityProduct = JsonUtil.deepCopy(updatedProduct);

        otherEntityProduct.put(Field.grpMembrRecCde, grpMembrRecCde);

        if (otherEntityHistory != null) {
            otherEntityProduct.put(Field._id, otherEntityHistory.get(Field._id));
            otherEntityProduct.put(Field.prodId, otherEntityHistory.get(Field.prodId));
            otherEntityProduct.put(Field.prodCde, otherEntityHistory.get(Field.prodCde));
        }

        // will copy to cmb product when update/create wpb product
        if (dummy.equals(grpMembrRecCde)) {
            otherEntityProduct.putIfAbsent(Field.cmplxProdInd, "S");
            otherEntityProduct.putIfAbsent(Field.buyRestrictChannel, "SRBPI");
            if ("CNY".equals(otherEntityProduct.get(Field.ccyProdCde))) {
                Document finDoc = new Document();
                finDoc.put(Field.docFinTypeCde, "KRS-CNY");
                finDoc.put(Field.langFinDocCde, "EN");
                finDoc.put(Field.finDocCustClassCde, "ALL");
                finDoc.put(Field.urlDocText, "http://www.hfi.dummy.com.hk/data/hk/invest/bond/bond_krscny_krs01_cmb_en.pdf");
                otherEntityProduct.put(Field.finDoc, Collections.singletonList(finDoc));
            }

            //Currently only GSOPS.PD sync wpb bond to cmb bond
            JsonPathUtils.setValue(otherEntityProduct, Field.debtInstm + DOT + Field.isAlgoInd, INDICATOR_YES);
            JsonPathUtils.setValue(otherEntityProduct, Field.debtInstm + DOT + Field.isManualTradeInd, INDICATOR_NO);
        }
        if (HBAP.equals(grpMembrRecCde)) {
            JsonPathUtils.setValue(otherEntityProduct, "debtInstm.cmbOnlyInd", INDICATOR_YES);
        }
        return otherEntityProduct;
    }
}
