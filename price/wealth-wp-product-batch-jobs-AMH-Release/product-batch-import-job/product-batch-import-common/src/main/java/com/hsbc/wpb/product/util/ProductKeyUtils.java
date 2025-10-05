package com.dummy.wpb.product.util;

import com.dummy.wpb.product.constant.ConfigConstants;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductKeyUtils {

    private static List<String> ignoreStatFilterList;
    private static final List<String> defaultStatFilterList = Collections.singletonList("D");
    private static List<String> searchByProdCdeList;
    private static List<String> searchByUniqueList;
    private static List<String> searchByPrimaryList;
    private static List<String> searchByAllList;

    static {
        try {
            ignoreStatFilterList = LegacyConfig.getList("PRODKEY.FILTER_PROD_STATUS_CODE.IGNORE_TYPE");
            searchByProdCdeList = LegacyConfig.getList(ConfigConstants.PRODKEY_PRODCDE);
            searchByUniqueList = LegacyConfig.getList(ConfigConstants.PRODKEY_UNIQUE);
            searchByPrimaryList = LegacyConfig.getList(ConfigConstants.PRODKEY_PRIMARY);
            searchByAllList = LegacyConfig.getList(ConfigConstants.PRODKEY_ALL);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Reference SQL: (ctryProdTrade1Cde and ccyProdCde doesn't seem to in the conditions)
     * WITH PROD_ALT_ID_0 AS
     * (SELECT A0.*
     * FROM PROD_ALT_ID A0
     * WHERE (A0.CTRY_REC_CDE,
     * A0.GRP_MEMBR_REC_CDE,
     * A0.PROD_CDE_ALT_CLASS_CDE,
     * A0.PROD_TYPE_CDE,
     * A0.PROD_ALT_NUM) IN ((?, ?, ?, ?, ?))),
     * PROD_0 AS
     * (SELECT B0.*
     * FROM PROD B0
     * WHERE B0.PROD_ID IN
     * (SELECT A1.PROD_ID
     * FROM PROD_ALT_ID_0 A1))
     * SELECT B.PROD_ID,
     * A.PROD_ALT_NUM,
     * A.PROD_TYPE_CDE,
     * A.PROD_CDE_ALT_CLASS_CDE,
     * B.CTRY_PROD_TRADE_1_CDE,
     * B.CCY_PROD_CDE
     * FROM PROD_ALT_ID_0 A
     * JOIN PROD_0 B ON B.CTRY_REC_CDE = A.CTRY_REC_CDE
     * AND B.GRP_MEMBR_REC_CDE = A.GRP_MEMBR_REC_CDE
     * AND B.PROD_ID = A.PROD_ID
     * WHERE B.CTRY_REC_CDE = ?
     * AND B.GRP_MEMBR_REC_CDE = ?
     * AND NOT EXISTS
     * (SELECT 1
     * FROM PROD_OFER_CHANL C
     * WHERE C.CTRY_REC_CDE = B.CTRY_REC_CDE
     * AND C.GRP_MEMBR_REC_CDE = B.GRP_MEMBR_REC_CDE
     * AND C.PROD_ID = B.PROD_ID
     * AND C.CHANL_COMN_CDE = ?)
     * AND ((B.PROD_STAT_CDE NOT IN ('D', 'P')
     * AND A.PROD_TYPE_CDE = ?
     * AND A.PROD_CDE_ALT_CLASS_CDE = ?
     * AND A.PROD_ALT_NUM = ?));
     *
     * @param keyList
     * @return
     */
    public static Bson productKeyFilters(List<ProductKey> keyList) {
        List<Bson> orList = new LinkedList<>();
        keyList.forEach(key -> {
            String classCode = key.getProdCdeAltClassCde();
            if (searchByProdCdeList.contains(classCode)) {
                orList.add(eq("prodCde", key.getProdAltNum()));
            } else {    // search by ALT_ID
                handleProdKeyAltId(key, classCode, orList);
            }
        });
        return or(orList);
    }

    private static void handleProdKeyAltId(ProductKey key, String classCode, List<Bson> orList) {
        List<Bson> elemList = new LinkedList<>();

                /*
                workaround for Document DB query to hit the correct index, useless in MongoDB

                Amazon DocumentDB does not currently support the ability to use indexes with the $distinct, $elemMatch, and $lookup operators.
                As a result, utilizing these operators will result in collection scans. Performing a filter or match before utilizing one of these
                operators will reduce the amount of data that needs to be scanned, and thus can improve performance.
                see https://docs.aws.amazon.com/documentdb/latest/developerguide/functional-differences.html#functional-differences.indexes
                */
        elemList.add(eq("altId.prodAltNum", key.getProdAltNum()));

        List<Bson> altIdList = new LinkedList<>();
        altIdList.add(eq("prodAltNum", key.getProdAltNum()));
        if (searchByUniqueList.contains(classCode)) {
            // PRODKEY.UNIQUE means: productAlternativeNumber + productCodeAlternativeClassCode
            altIdList.add(eq(Field.prodCdeAltClassCde, classCode));
        } else if (searchByPrimaryList.contains(classCode)) {
            // PRODKEY.PRIMARY means: productTypeCode + productAlternativeNumber + productCodeAlternativeClassCode
            altIdList.add(eq(Field.prodTypeCde, key.getProdTypeCde()));
            altIdList.add(eq(Field.prodCdeAltClassCde, classCode));
        } else if (searchByAllList.contains(classCode)) {
            // PRODKEY.ALL means: productTypeCode + productAlternativeNumber + productCodeAlternativeClassCode + productTradableCode + currencyProductCode
            altIdList.add(eq(Field.prodTypeCde, key.getProdTypeCde()));
            altIdList.add(eq(Field.prodCdeAltClassCde, classCode));
            if (StringUtils.hasText(key.getCtryProdTradeCde())) {
                elemList.add(eq("ctryProdTradeCde.0", key.getCtryProdTradeCde()));
            }
            if (StringUtils.hasText(key.getCcyProdCde())) {
                elemList.add(eq("ccyProdCde", key.getCcyProdCde()));
            }
        } else {
            // PRODKEY.OTHER means: productTypeCode + productAlternativeNumber + productCodeAlternativeClassCode + productTradableCode
            altIdList.add(eq(Field.prodTypeCde, key.getProdTypeCde()));
            altIdList.add(eq(Field.prodCdeAltClassCde, classCode));
            if (StringUtils.hasText(key.getCtryProdTradeCde())) {
                elemList.add(eq("ctryProdTradeCde.0", key.getCtryProdTradeCde()));
            }
        }
        elemList.add(elemMatch("altId", and(altIdList)));
        if (!ignoreStatFilterList.contains(classCode)) {
            elemList.add(nin("prodStatCde", defaultStatFilterList));
        }
        orList.add(and(elemList));
    }

    public static boolean areEqual(ProductKey productKey1, ProductKey productKey2) {
        String altClassCode = productKey1.getProdCdeAltClassCde();
        if (ObjectUtils.notEqual(altClassCode, productKey2.getProdCdeAltClassCde())) {
            return false;
        }

        if (searchByProdCdeList.contains(altClassCode) || searchByPrimaryList.contains(altClassCode)) {
            return Objects.equals(productKey1.getProdCdeAltClassCde(), productKey2.getProdCdeAltClassCde())
                    && Objects.equals(productKey1.getProdAltNum(), productKey2.getProdAltNum())
                    && Objects.equals(productKey1.getProdTypeCde(), productKey2.getProdTypeCde());
        } else if (searchByAllList.contains(altClassCode)) {
            return Objects.equals(productKey1.getProdCdeAltClassCde(), productKey2.getProdCdeAltClassCde())
                    && Objects.equals(productKey1.getProdAltNum(), productKey2.getProdAltNum())
                    && Objects.equals(productKey1.getProdTypeCde(), productKey2.getProdTypeCde())
                    && Objects.equals(productKey1.getCtryProdTradeCde(), productKey2.getCtryProdTradeCde())
                    && Objects.equals(productKey1.getCcyProdCde(), productKey2.getCcyProdCde());
        } else {
            return Objects.equals(productKey1.getProdCdeAltClassCde(), productKey2.getProdCdeAltClassCde())
                    && Objects.equals(productKey1.getProdAltNum(), productKey2.getProdAltNum())
                    && Objects.equals(productKey1.getProdTypeCde(), productKey2.getProdTypeCde())
                    && Objects.equals(productKey1.getCtryProdTradeCde(), productKey2.getCtryProdTradeCde());
        }
    }


    public static String buildProdKeyInfo(Document prod) {
        String ctryRecCde = prod.getString(Field.ctryRecCde);
        String grpMembrRecCde = prod.getString(Field.grpMembrRecCde);
        String prodTypeCde = prod.getString(Field.prodTypeCde);
        String prodAltPrimNum = prod.getString(Field.prodAltPrimNum);

        return String.format("(ctryRecCde: %s, grpMembrRecCde: %s, prodTypeCde: %s, prodAltPrimNum: %s)", ctryRecCde, grpMembrRecCde, prodTypeCde, prodAltPrimNum);
    }
}
