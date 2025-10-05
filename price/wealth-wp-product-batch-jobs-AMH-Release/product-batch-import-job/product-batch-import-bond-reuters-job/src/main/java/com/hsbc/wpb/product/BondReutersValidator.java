package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.BatchConstants;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.exception.InvalidRecordException;
import com.dummy.wpb.product.model.graphql.ReferenceData;
import com.dummy.wpb.product.service.ReferenceDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;

import javax.annotation.PostConstruct;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.dummy.wpb.product.constant.BatchConstants.*;

@Slf4j
public class BondReutersValidator implements Validator<BondReutersStreamItem> {

    private final ReferenceDataService referenceDataService;

    @Value("#{stepExecution}")
    StepExecution stepExecution;

    @Value("#{jobParameters['ctryRecCde']}")
    String ctryRecCde;

    @Value("#{jobParameters['grpMembrRecCde']}")
    String grpMembrRecCde;

    private final Map<String, String> bondIssuerRefMap = new ConcurrentHashMap<>();
    private final Map<String, String> bondIssuerGuntrRefMap = new ConcurrentHashMap<>();
    private final Map<String, String> bondGuntrRefMap = new ConcurrentHashMap<>();
    private final Map<String, String> couponFreqMap = new ConcurrentHashMap<>();
    private final Map<String, String> couponTypeMap = new ConcurrentHashMap<>();
    private final Map<String, Boolean> newIssuerMap = new ConcurrentHashMap<>();
    private final Map<String, Boolean> newIssuerGrntrmap = new ConcurrentHashMap<>();
    private final Map<String, Boolean> newGrntrmap = new ConcurrentHashMap<>();

    public BondReutersValidator(ReferenceDataService referenceDataService) {
        this.referenceDataService = referenceDataService;
    }


    @Override
    public void validate(BondReutersStreamItem streamItem) throws ValidationException {
        Document origProd = streamItem.getOriginalProduct();
        Document importProd = streamItem.getImportProduct();

        if (StringUtils.isBlank(importProd.getString(Field.prodAltPrimNum))) {
            log.error("Skip record without prodAltPrimNum.");
            throw new InvalidRecordException("Skip record without prodAltPrimNum.");
        }

        validateOrigProd(origProd, importProd);
        //For those non-existing bond issuer/guarantor, system will automictically create reference data for 'ISSUERGRNTR','BONDISSUER','BONDGRNTR'
        validateReferenceData(streamItem);
        //check coupon type / coupon freq code and map the value
        validateCoupon(importProd);
    }

    private void validateCoupon(Document bond) {
        Document debtInstm = bond.get(Field.debtInstm, new Document());

        //check coupon freq code, get coupon freq code from reference data, if no mapping data , then skip this record
        String couponFreq = debtInstm.getString(Field.pdcyCoupnPymtCde);
        if (StringUtils.isNotBlank(couponFreq)) {
            if (couponFreqMap.containsKey(couponFreq)) {
                debtInstm.put(Field.pdcyCoupnPymtCde, couponFreqMap.get(couponFreq));
            } else {
                log.error("Skip current bond: {}. Because the coupon freq code {} does not exist in reference data.",
                        bond.get(Field.prodAltPrimNum), couponFreq);
                throw createValidationException(bond, "Skip for non-existing coupon freq code" + couponFreq);
            }
        }

        //check coupon type, get coupon type code from reference dataf no mapping data , then skip this record
        String couponType = debtInstm.getString(Field.coupnType);
        if (StringUtils.isNotBlank(couponType)) {
            if (couponTypeMap.containsKey(couponType)) {
                debtInstm.put(Field.coupnType, couponTypeMap.get(couponType));
            } else {
                log.error("Skip current bond: {}. Because the coupon type code {} does not exist in reference data.",
                        bond.get(Field.prodAltPrimNum), couponType);
                throw createValidationException(bond, "Skip for non-existing coupon type code" + couponType);
            }
        }

    }

    private void validateReferenceData(BondReutersStreamItem streamItem) {
        Document importProd = streamItem.getImportProduct();
        Document debtInstm = importProd.get(Field.debtInstm, new Document());

        String isrBondName = preferOriginalValue(streamItem, Field.isrBondName);
        String isrBondNameDesc = debtInstm.getString(Field.isrBondNameDesc);
        String grntrName = preferOriginalValue(streamItem, Field.grntrName);
        String grntrNameDesc = debtInstm.getString(Field.grntrNameDesc);

        List<ReferenceData> missingRefList = new ArrayList<>();

        // handle BONDISSUER reference data for RIS channel
        if (StringUtils.isNoneBlank(isrBondName, isrBondNameDesc)) {
            if (!bondIssuerRefMap.containsKey(isrBondName)) {
                newIssuerMap.computeIfAbsent(isrBondName, key -> {
                    missingRefList.add(createMissingRefData(isrBondName, isrBondNameDesc, BOND_ISSUER_REF_TYPE_CDE));
                    return true;
                });
            }

            // handle ISSUERGRNTR reference data for online bond channel
            if (!bondIssuerGuntrRefMap.containsKey(isrBondName)) {
                newIssuerGrntrmap.computeIfAbsent(isrBondName, key -> {
                    missingRefList.add(createMissingRefData(isrBondName, isrBondNameDesc, BOND_ISSUER_GUNTR_REF_TYPE_CDE));
                    return true;
                });
            }
        }

        if (StringUtils.isNoneBlank(grntrName, grntrNameDesc) && !grntrName.startsWith("#N/A")) {
            if (!bondIssuerGuntrRefMap.containsKey(grntrName)) {
                newIssuerGrntrmap.computeIfAbsent(grntrName, key -> {
                    missingRefList.add(createMissingRefData(grntrName, grntrNameDesc, BOND_ISSUER_GUNTR_REF_TYPE_CDE));
                    return true;
                });
            }

            // handle BONDGRNTR reference data for online bond channel
            if (!bondGuntrRefMap.containsKey(grntrName)) {
                newGrntrmap.computeIfAbsent(grntrName, key -> {
                    missingRefList.add(createMissingRefData(grntrName, grntrNameDesc, BOND_GUNTR_REF_TYPE_CDE));
                    return true;
                });
            }
        }

        if (!missingRefList.isEmpty()) {
            streamItem.setMissingRefDataList(missingRefList);
        }

    }

    private String preferOriginalValue(BondReutersStreamItem streamItem, String key) {
        Document originalProd = streamItem.getOriginalProduct();
        Map<String, Object> orignailDebtInstm = originalProd.get(Field.debtInstm, Map.class);
        String orignalValue = (String) orignailDebtInstm.get(key);
        if (StringUtils.isNoneBlank(orignalValue)) {
            return orignalValue;
        }
        Document importProd = streamItem.getImportProduct();
        Document debtInstm = importProd.get(Field.debtInstm, new Document());
        return debtInstm.getString(key);
    }

    private ReferenceData createMissingRefData(String cdvCode, String cdvDesc, String cdvTypeCde) {
        ReferenceData referenceData = new ReferenceData();
        referenceData.setCtryRecCde(ctryRecCde);
        referenceData.setGrpMembrRecCde(grpMembrRecCde);
        referenceData.setCdvDispSeqNum(0L);
        referenceData.setCdvCde(cdvCode);
        referenceData.setCdvDesc(cdvDesc);
        referenceData.setCdvTypeCde(cdvTypeCde);
        referenceData.setRecCmntText(cdvDesc);
        referenceData.setRecCreatDtTm(OffsetDateTime.now().toString());
        referenceData.setRecUpdtDtTm(OffsetDateTime.now().toString());
        return referenceData;
    }

    private void validateOrigProd(Document origProd, Document importProd) {
        if (MapUtils.isEmpty(origProd) || origProd.get(Field.debtInstm) == null) {
            log.error("Cannot update non-existing bond record: {}", importProd.get(Field.prodAltPrimNum));
            throw createValidationException(importProd, "Cannot update non-existing bond record");
        }
        if (StringUtils.equals(origProd.getString(Field.prodStatCde), BatchConstants.EXPIRED)) {
            log.error("Cannot update expired product: {}.", origProd.get(Field.prodAltPrimNum));
            throw createValidationException(origProd, "Cannot update expired product.");
        }

    }

    private ValidationException createValidationException(Document product, String msg) {
        String errorDesc = String.format("Product[ctryRecCde=%s, grpMembrRecCde=%s, prodTypeCde=%s, prodAltPrimNum=%s] validation failed. Cause: %s",
                product.getString(Field.ctryRecCde),
                product.getString(Field.grpMembrRecCde),
                product.getString(Field.prodTypeCde),
                product.getString(Field.prodAltPrimNum),
                msg);
        return new ValidationException(errorDesc);
    }

    @PostConstruct
    private void initReferenceData() {
        Criteria criteria = new Criteria()
                .and(Field.ctryRecCde).is(ctryRecCde)
                .and(Field.grpMembrRecCde).is(grpMembrRecCde)
                .and(Field.cdvTypeCde).in(BOND_ISSUER_REF_TYPE_CDE, BOND_ISSUER_GUNTR_REF_TYPE_CDE,
                        BOND_GUNTR_REF_TYPE_CDE, THOMSON_REUTERS_COUPON_FREQ, THOMSON_REUTERS_COUPON_TYPE);

        List<ReferenceData> referenceDataList = referenceDataService.referenceDataByFilter(criteria.getCriteriaObject());

        if (CollectionUtils.isNotEmpty(referenceDataList)) {
            for (ReferenceData referenceData : referenceDataList) {
                String refTypeCde = referenceData.getCdvTypeCde();
                switch (refTypeCde) {
                    case BOND_ISSUER_REF_TYPE_CDE:
                        bondIssuerRefMap.put(StringUtils.stripToNull(referenceData.getCdvCde()), StringUtils.stripToNull(referenceData.getCdvDesc()));
                        break;
                    case BOND_ISSUER_GUNTR_REF_TYPE_CDE:
                        bondIssuerGuntrRefMap.put(StringUtils.stripToNull(referenceData.getCdvCde()), StringUtils.stripToNull(referenceData.getCdvDesc()));
                        break;
                    case BOND_GUNTR_REF_TYPE_CDE:
                        bondGuntrRefMap.put(StringUtils.stripToNull(referenceData.getCdvCde()), StringUtils.stripToNull(referenceData.getCdvDesc()));
                        break;
                    case THOMSON_REUTERS_COUPON_FREQ:
                        couponFreqMap.put(StringUtils.stripToNull(referenceData.getCdvCde()), StringUtils.stripToNull(referenceData.getCdvDesc()));
                        break;
                    case THOMSON_REUTERS_COUPON_TYPE:
                        couponTypeMap.put(StringUtils.stripToNull(referenceData.getCdvCde()), StringUtils.stripToNull(referenceData.getCdvDesc()));
                        break;
                    default:
                }
            }
        }
    }
}
