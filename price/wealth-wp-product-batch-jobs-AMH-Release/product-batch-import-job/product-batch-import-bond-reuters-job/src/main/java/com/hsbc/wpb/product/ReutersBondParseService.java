package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ExcelColumnInfo;
import com.dummy.wpb.product.service.ExcelParseService;
import com.dummy.wpb.product.utils.JsonPathUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dummy.wpb.product.constant.BatchConstants.*;

public class ReutersBondParseService extends ExcelParseService {

    /**
     * Used to set up value of array fields.
     * <p>
     * eg: debtInstm.credRtng[?(@.creditRtngAgcyCde=='CRMOODYS')].creditRtngCde.
     * <p>
     * group(1) will take debtInstm.credRtng[?(@.creditRtngAgcyCde=='CRMOODYS')] as jsonPath,
     * <p>
     * group(2) will take creditRtngAgcyCde as listPath.
     * <p>
     * group(3) will take creditRtngAgcyCde as listKeyName.
     * <p>
     * group(4) will take CRMOODYS as listKeyValue.
     * <p>
     * group(5) will take creditRtngCde as fieldName of elements.
     */
    private static final Pattern listFieldPattern = Pattern.compile("((\\S+)\\[\\?\\(@\\.(\\S+)=='(\\S+)'\\)])\\.(\\S+)");//NOSONAR

    @Override
    protected void setProductField(Document product, ExcelColumnInfo columnInfo) {
        if (StringUtils.equalsAny(columnInfo.getDataType(), "creditRtngCde", "altId")) {
            setProductListFields(product, columnInfo);
            return;
        }

        super.setProductField(product, columnInfo);
    }


    @Override
    protected void afterSetProductField(Document product) {
        Document debtInstm = product.get(Field.debtInstm, new Document());

        String cptlTierText = debtInstm.getString("cptlTierText");
        if (null != cptlTierText) {
            if (StringUtils.startsWith(cptlTierText, "Subordinated")) {
                debtInstm.put(Field.subDebtInd, INDICATOR_YES);
            } else {
                debtInstm.put(Field.subDebtInd, INDICATOR_NO);
            }
        }

        BigDecimal coupnAnnlRate = debtInstm.get(Field.coupnAnnlRate, BigDecimal.class);
        if (null != coupnAnnlRate) {
            debtInstm.put(Field.coupnAnnlText, new DecimalFormat("#0.0000").format(coupnAnnlRate) + "%");
        }

        List<Map<String, Object>> altId = (List<Map<String, Object>>) product.get(Field.altId);
        if (CollectionUtils.isNotEmpty(altId)) {
            altId.forEach(item -> item.put(Field.prodTypeCde, BOND_CD));
        }

        debtInstm.compute("bondFltSprdRate", (k, bondFltSprdRate) -> {
            if (null == bondFltSprdRate) {
                return null;
            }
            return ((BigDecimal) bondFltSprdRate).setScale(4, RoundingMode.HALF_UP);
        });

        String extAble = (String) debtInstm.remove("extAble");
        String callAble = (String) debtInstm.remove("callAble");
        if (StringUtils.equalsAnyIgnoreCase(INDICATOR_YES, extAble, callAble)) {
            debtInstm.put("flexMatOptInd", INDICATOR_YES);
        } else {
            debtInstm.put("flexMatOptInd", INDICATOR_NO);
        }
    }

    @Override
    protected Object convertOtherValue(ExcelColumnInfo columnInfo) {
        if ("prodBodLotQtyCnt".equalsIgnoreCase(columnInfo.getDataType())) {
            return convertBigDecimalValue(columnInfo.getValue()).divide(new BigDecimal(1000000), RoundingMode.DOWN);
        }
        return super.convertOtherValue(columnInfo);
    }

    public void setProductListFields(Document product, ExcelColumnInfo columnInfo) {
        Matcher matcher = listFieldPattern.matcher(columnInfo.getJsonPath());
        if (!matcher.find()) {
            return;
        }

        String listPath = matcher.group(2);
        String keyName = matcher.group(3);
        String keyValue = matcher.group(4);
        String fieldName = matcher.group(5);

        List<Map<String, Object>> listValue = JsonPathUtils.readValue(product, listPath, new ArrayList<>());
        if (StringUtils.isBlank(columnInfo.getValue())) {
            // that means should remove matched element from list
            listValue.removeIf(element -> Objects.equals(element.get(keyName), keyValue));
            return;
        }

        Map<String, Object> element = listValue
                .stream()
                .filter(item -> Objects.equals(item.get(keyName), keyValue))
                .findFirst()
                .orElse(new LinkedHashMap<>());
        if (element.isEmpty()) {
            listValue.add(element);
        }
        element.putIfAbsent(keyName, keyValue);
        element.put(fieldName, columnInfo.getValue());

        JsonPathUtils.setValue(product, listPath, listValue);
    }

}
