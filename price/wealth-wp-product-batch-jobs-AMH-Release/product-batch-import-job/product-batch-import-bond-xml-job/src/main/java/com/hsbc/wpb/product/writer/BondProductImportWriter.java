package com.dummy.wpb.product.writer;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.model.graphql.ReferenceData;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.ReferenceDataService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dummy.wpb.product.constant.BatchConstants.BOND_GUNTR_REF_TYPE_CDE;
import static com.dummy.wpb.product.constant.BatchConstants.BOND_ISSUER_REF_TYPE_CDE;

public class BondProductImportWriter extends ProductImportItemWriter {

    private final ReferenceDataService referenceDataService;


    private final Map<String, Map<String, String>> bondNameMap = new ConcurrentHashMap<>();

    public BondProductImportWriter(ProductService productService, ReferenceDataService referenceDataService) {
        super(productService);
        this.referenceDataService = referenceDataService;
    }

    @SneakyThrows
    @Override
    public void beforeWrite(List<? extends ProductStreamItem> streamItems) {
        List<ReferenceData> missingBondNames = new ArrayList<>();

        for (ProductStreamItem streamItem : streamItems) {
            Stream.of(Field.isrBondName, Field.grntrName).forEach(fieldName -> {
                Document updatedProduct = streamItem.getUpdatedProduct();
                Map<String, Object> debtInstm = updatedProduct.get(Field.debtInstm, Map.class);
                String ctryRecCde = updatedProduct.getString(Field.ctryRecCde);
                String grpMembrRecCde = updatedProduct.getString(Field.grpMembrRecCde);

                String cdvTypeCde = fieldName.equals(Field.isrBondName) ? BOND_ISSUER_REF_TYPE_CDE : BOND_GUNTR_REF_TYPE_CDE;
                String cdvDesc = (String) debtInstm.get(fieldName);
                String cdvCde = StringUtils.substring(cdvDesc, 0, 30);

                String nameKey = String.format("%s.%s.%s", ctryRecCde, grpMembrRecCde, cdvTypeCde);
                Map<String, String> nameMap = bondNameMap.computeIfAbsent(nameKey, (key) -> queryReferenceData(ctryRecCde, grpMembrRecCde, cdvTypeCde));

                if (StringUtils.isNotBlank(cdvCde) && !nameMap.containsKey(cdvCde)) {
                    ReferenceData referenceData = new ReferenceData();
                    referenceData.setCtryRecCde(ctryRecCde);
                    referenceData.setGrpMembrRecCde(grpMembrRecCde);
                    referenceData.setCdvDispSeqNum(0L);
                    referenceData.setCdvCde(cdvCde);
                    referenceData.setCdvDesc(cdvDesc);
                    referenceData.setCdvTypeCde(cdvTypeCde);
                    referenceData.setRecCreatDtTm(OffsetDateTime.now().toString());
                    referenceData.setRecUpdtDtTm(OffsetDateTime.now().toString());
                    missingBondNames.add(referenceData);
                    nameMap.put(cdvCde, cdvDesc);
                }

                debtInstm.put(fieldName, cdvCde);
            });
        }

        if (!missingBondNames.isEmpty()) {
            referenceDataService.createRefData(missingBondNames);
        }
    }


    @SneakyThrows
    private Map<String, String> queryReferenceData(String ctryRecCde, String grpMembrRecCde, String cdvType) {
        Criteria criteria = new Criteria()
                .and(Field.ctryRecCde).is(ctryRecCde)
                .and(Field.grpMembrRecCde).is(grpMembrRecCde)
                .and(Field.cdvTypeCde).is(cdvType);

        return referenceDataService.referenceDataByFilter(criteria.getCriteriaObject())
                .stream()
                .collect(Collectors.toMap(
                        ReferenceData::getCdvCde,
                        ReferenceData::getCdvDesc,
                        (cde1, cde2) -> cde1.chars().allMatch(Character::isDigit) ? cde1 : cde2,
                        ConcurrentHashMap::new));
    }
}
