package com.dummy.wpb.product.processor;

import com.google.common.collect.ImmutableMap;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.PriceReport;
import com.dummy.wpb.product.model.ProductPriceStreamItem;
import com.dummy.wpb.product.utils.JsonPathUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.Document;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

import static com.dummy.wpb.product.PriceLimitHolder.getInvLimit;
import static com.dummy.wpb.product.PriceLimitHolder.getTolLimit;

@Order(2)
public class PrcCmparProcessor implements ItemProcessor<ProductPriceStreamItem, ProductPriceStreamItem> {

    private static final int RATESCALE = 6;

    private static final Map<String, String> prodPrcTypeMap = new ImmutableMap.Builder<String, String>()
            .put("BID", Field.prodBidPrcAmt)
            .put("OFFR", Field.prodOffrPrcAmt)
            .put("MKT", Field.prodMktPrcAmt)
            .put("NAV", Field.prodNavPrcAmt)
            .build();

    @Value("#{'${batch.prc.tol.systemCde}'.split(',')}")
    private List<String> prcTolReportSysCdes;

    @Value("#{'${batch.prc.inv.systemCde}'.split(',')}")
    private List<String> prcInvReportSysCdes;

    @Value("#{jobParameters['systemCde']}")
    private String systemCde;

    @Override
    public ProductPriceStreamItem process(ProductPriceStreamItem streamItem) throws Exception {
        if (!prcTolReportSysCdes.contains(systemCde) && !prcInvReportSysCdes.contains(systemCde)) {
            return streamItem;
        }

        Document prevProd = streamItem.getOriginalProduct();
        Document currProd = streamItem.getUpdatedProduct();
        if (ObjectUtils.compare(
                Objects.toString(currProd.get(Field.prcEffDt), null),
                Objects.toString(prevProd.get(Field.prcEffDt), null)) < 0) {
            // skip price report checking if incoming prcEffDt less than prev prcEffDt
            return streamItem;
        }

        List<Map<String, Object>> origPrcCmpars = JsonPathUtils.readValue(prevProd, Field.prcCmpar);

        List<Map<String, Object>> updatePrcCmpars = ObjectUtils.defaultIfNull(JsonUtil.deepCopy(origPrcCmpars), new ArrayList<>());

        prodPrcTypeMap.forEach((prodPrcType, amtField) -> {
            BigDecimal currAmt = new BigDecimal(Objects.toString(currProd.get(amtField), "0"));
            BigDecimal prevAmt = new BigDecimal(Objects.toString(prevProd.get(amtField), "0"));

            BigDecimal rate = calcChrgRate(currAmt, prevAmt);
            // check price report type: TOL, INV
            BigDecimal invLimit = getInvLimit(prodPrcType);
            BigDecimal tolLimit = getTolLimit(prodPrcType);

            if (ObjectUtils.allNull(invLimit,tolLimit)){
                return;
            }

            String rptProdPrcTypeCde = analyzePrcRptType(rate, invLimit, tolLimit, currAmt, prevAmt);

            if (StringUtils.isNotBlank(rptProdPrcTypeCde)) {
                Map<String, Object> prcCmpar = updatePrcCmpars.stream()
                        .filter(item -> Objects.equals(item.get(Field.rptProdPrcTypeCde), rptProdPrcTypeCde) && Objects.equals(item.get(Field.prodPrcTypeCde), prodPrcType))
                        .findFirst()
                        .orElse(new HashMap<>());
                if (prcCmpar.isEmpty()) {
                    updatePrcCmpars.add(prcCmpar);
                }
                initPrcCmpar(prcCmpar, prevProd, currProd);
                prcCmpar.putIfAbsent(Field.prodPrcTypeCde, prodPrcType);
                prcCmpar.put(Field.rptProdPrcTypeCde, rptProdPrcTypeCde);
                prcCmpar.put("limitPrcDviatPct", NumberUtils.toDouble(tolLimit));
                prcCmpar.put("prodPrcDviatPct", NumberUtils.toDouble(rate));
            }
        });

        if (!updatePrcCmpars.isEmpty()) {
            currProd.put(Field.prcCmpar, updatePrcCmpars);
        }
        return streamItem;
    }

    private void initPrcCmpar(Map<String, Object> prcCmpar, Map<String, Object> prevProd, Map<String, Object> currProd) {
        prcCmpar.putIfAbsent(Field.prodTypeCde, prevProd.get(Field.prodTypeCde));
        prcCmpar.putIfAbsent(Field.prodAltPrimNum, prevProd.get(Field.prodAltPrimNum));
        prcCmpar.putIfAbsent(Field.recCreatDtTm, LocalDateTime.now().toString());

        prcCmpar.put(Field.prodBidPrcAmt, prevProd.get(Field.prodBidPrcAmt));
        prcCmpar.put(Field.prodOffrPrcAmt, prevProd.get(Field.prodOffrPrcAmt));
        prcCmpar.put(Field.prodNavPrcAmt, prevProd.get(Field.prodNavPrcAmt));
        prcCmpar.put(Field.prodMktPrcAmt, prevProd.get(Field.prodMktPrcAmt));

        prcCmpar.put(Field.prcEffDt, prevProd.get(Field.prcEffDt));
        prcCmpar.put(Field.recUpdtDtTm, LocalDateTime.now().toString());

        prcCmpar.put("prodBidPrcExtnlAmt", currProd.get(Field.prodBidPrcAmt));
        prcCmpar.put("prodOffrPrcExtnlAmt", currProd.get(Field.prodOffrPrcAmt));
        prcCmpar.put("prodNavPrcExtnlAmt", currProd.get(Field.prodNavPrcAmt));
        prcCmpar.put("prodMktPrcExtnlAmt", currProd.get(Field.prodMktPrcAmt));
        prcCmpar.put("prcEffExtnlDt", currProd.get(Field.prcEffDt));
    }

    private BigDecimal calcChrgRate(BigDecimal curr, BigDecimal prev) {
        if (curr.compareTo(BigDecimal.ZERO) == 0 || prev.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return (curr.subtract(prev).multiply(new BigDecimal(100)).divide(prev, RATESCALE, RoundingMode.HALF_UP));
    }

    private String analyzePrcRptType(final BigDecimal rate,
                                     final BigDecimal invLimit,
                                     final BigDecimal tolLimit,
                                     final BigDecimal currAmt,
                                     final BigDecimal prevAmt) {

        if (rate == null) {
            if (currAmt.compareTo(BigDecimal.ZERO) == 0 && prevAmt.compareTo(BigDecimal.ZERO) == 0) {
                // special case, regard as valid
                return null;
            }

            // single "zero" or "null" case
            if (tolLimit == null) {
                return null;
            }

            return PriceReport.REPORT_TYPE_TOLERANCE;
        }

        if (tolLimit != null && rate.abs().compareTo(tolLimit) > 0) {
            // tolerance report (exception report)
            return PriceReport.REPORT_TYPE_TOLERANCE;
        }

        if (invLimit != null && rate.abs().compareTo(invLimit) > 0) {
            // investigation report
            return PriceReport.REPORT_TYPE_INVESTIGATION;

        }
        return null;
    }
}
