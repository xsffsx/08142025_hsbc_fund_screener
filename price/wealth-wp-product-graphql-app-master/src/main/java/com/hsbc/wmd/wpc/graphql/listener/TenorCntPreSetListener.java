package com.dummy.wmd.wpc.graphql.listener;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.ProdTypeCde;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

/**
 * Set tenor count in product creation or updating stage as well
 * Currently this function is only for the product created by WPS UI
 */
@Component
public class TenorCntPreSetListener extends BaseChangeListener {


    @Override
    public void beforeInsert(Document doc) {
        long tenorCount = calculateTenorCount(doc);
        if (tenorCount < 0) {
            Long prdInvstTnorNum = calculatePrdInvstTnorNum(doc);
            if (Objects.nonNull(prdInvstTnorNum)) doc.put(Field.prdInvstTnorNum, prdInvstTnorNum);
        } else {
            doc.put(Field.termRemainDayCnt, tenorCount);
            doc.put(Field.prdInvstTnorNum, tenorCount);
        }
    }

    @Override
    public void beforeUpdate(Document prod, List<OperationInput> operations) {
        String prodTypeCde = prod.getString(Field.prodTypeCde);
        String lastUpdatedBy = prod.getString(Field.lastUpdatedBy);
        if (ProdTypeCde.BOND_CD.equals(prodTypeCde) && "wps".equals(lastUpdatedBy)) {
            this.beforeInsert(prod);
        }
    }

    private Long calculatePrdInvstTnorNum(Document doc) {
        String prdProdCde = doc.getString(Field.prdProdCde);
        Long prdProdNum = doc.getLong(Field.prdProdNum);
        if (Objects.isNull(prdProdCde) || Objects.isNull(prdProdNum)) return null;

        /*
         * Y - year, M - month, W - week, default - day (D)
         */
        switch (prdProdCde) {
            case "Y":
                return prdProdNum * 365;
            case "M":
                return prdProdNum * 30;
            case "W":
                return prdProdNum * 7;
            case "D":
            default:
                return prdProdNum;
        }
    }

    private long calculateTenorCount(Document doc) {
        if (Objects.isNull(doc.get(Field.prodMturDt))) return -1;
        /*
         * product mature date to 1970-01-01 days
         * product launched date to 1970-01-01 days
         * today to 1970-01-01 days
         */
        long prodMturDays = getDays(doc, Field.prodMturDt);
        long currentDays = LocalDate.now(ZoneOffset.UTC).toEpochDay();
        long prodLnchDays = getDays(doc, Field.prodLnchDt);
        // case 1: prodMturDt <= today expired
        if (prodMturDays < currentDays) return 0;
        // case 2: prodLnchDt is null Or prodLnchDt <= today
        if (Objects.isNull(doc.get(Field.prodLnchDt)) || prodLnchDays <= currentDays) return prodMturDays - currentDays + 1;
        /*
         * as the tenor count job executed at midnight, but the this flow will triggered by wps anytime
         * then there be 1 day gap between batch job and pre-set listener
         * agreed with biz, we add it back in pre-set
         */
        return prodMturDays - prodLnchDays + 1;
    }

    private long getDays(Document doc, String fieldName) {
        Object obj = doc.get(fieldName);
        if (Objects.isNull(obj)) return 0;
        if (obj instanceof Date) {
            return ((Date) obj).toInstant().atZone(ZoneOffset.UTC).toLocalDate().toEpochDay();
        } else if (obj instanceof LocalDate) {
            return ((LocalDate) obj).toEpochDay();
        }
        // need to hanle more date type
        return 0;
    }

    @Override
    public Collection<String> interestJsonPaths() {
        return Arrays.asList(Field.prdProdCde, Field.prdProdNum, Field.prodMturDt, Field.prodLnchDt);
    }

}
