package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.Gac;
import com.dummy.wpb.product.model.ProductType;
import com.dummy.wpb.product.utils.JsonPathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.Decimal128;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@StepScope
public class GacItemProcessor implements ItemProcessor<Map<String, Object>, Map<String, Object>> {

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Value("#{jobExecutionContext['noProdId']}")
    private Integer noProdIdNum;

    @Value("#{jobExecutionContext['illegalProdId']}")
    private Integer illegalProdIdNum;

    private static final String BREAKDOWNROW = "BREAKDOWN_ROWS.BREAKDOWN_ROW";
    private static final String CONTRIBUTIONVALUE = "VALUE";

    @Override
    public Map<String, Object> process(Map<String, Object> item) {
        try {
            String prodId = (String) item.get("CLIENT_ID");
            String assetType = (String) item.get("ASSET_TYPE");
            String prodTypeLevel1 = (String) item.get("PRODUCT_TYPE_LEVEL_ONE");
            String prodTypeLevel2 = (String) item.get("PRODUCT_TYPE_LEVEL_TWO");
            String prodTypeLevel3 = (String) item.get("PRODUCT_TYPE_LEVEL_THREE");
            String volatility = JsonPathUtils.readValue(item, "RISK.STANDALONE_RISK");
            List<Map<String, Object>> breakdownMaps = JsonPathUtils.readValue(item, "BREAKDOWNS.BREAKDOWN");
            List<Map<String, Object>> breakdownRows1 = null;
            List<Map<String, Object>> breakdownRows2 = null;
            List<Map<String, Object>> breakdownRows3 = null;
            if (CollectionUtils.isNotEmpty(breakdownMaps)) {
                breakdownRows1 = JsonPathUtils.readValue(breakdownMaps.get(0), BREAKDOWNROW);
                breakdownRows2 = JsonPathUtils.readValue(breakdownMaps.get(1), BREAKDOWNROW);
                breakdownRows3 = JsonPathUtils.readValue(breakdownMaps.get(2), BREAKDOWNROW);
            }
            ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();
            if (!validateGacFileData(prodId, jobContext)) {
                return null;
            }


            Gac gac = new Gac();
            gac.setAssetType(assetType);

            ProductType productType = new ProductType(prodTypeLevel1, prodTypeLevel2, prodTypeLevel3);
            gac.setProductType(BeanMap.create(productType));

            gac.setAssetClass(buildAssetClass(breakdownRows1, breakdownRows2, breakdownRows3, gac));

            return buildTransformValues(prodId, gac, volatility);
        } catch (Exception e) {
            log.error(String.format("Gac file record transform fail(CLIENT_ID: %s): ", item.get("CLIENT_ID")) + e.getMessage());
            return null;
        }
    }

    private boolean validateGacFileData(String prodId, ExecutionContext jobContext) {
        if (StringUtils.isEmpty(prodId)) {
            log.error(String.format("No CLIENT_ID: %s", prodId));
            noProdIdNum ++;
            jobContext.put("noProdId", noProdIdNum);
            return false;
        }else if (!isNumeric(prodId)) {
            log.error(String.format("Illegal CLIENT_ID: %s", prodId));
            illegalProdIdNum ++;
            jobContext.put("illegalProdId", illegalProdIdNum);
            return false;
        }
        return true;
    }

    private Map<String, Object> buildTransformValues(String prodId, Gac gac, String volatility) {
        HashMap<String, Object> transformValues = new HashMap<>();
        transformValues.put(Field.prodId, Long.valueOf(prodId));
        transformValues.put(Field.gac, BeanMap.create(gac));
        transformValues.put(Field.volatility, Decimal128.parse(volatility));
        return transformValues;
    }

    private List<Map<String, Object>> buildAssetClass(List<Map<String, Object>> breakdownRows1, List<Map<String, Object>> breakdownRows2,
                                                      List<Map<String, Object>> breakdownRows3, Gac gac) {
        ArrayList<Map<String, Object>> assetClasses = new ArrayList<>();
        if (breakdownRows1 != null) {
            List<Map<String, Object>> level1Asset = getAssetClass(breakdownRows1, 1L).stream()
                    .sorted((o1, o2) -> ((Decimal128) o2.get(Field.contribution)).compareTo((Decimal128) o1.get(Field.contribution)))
                    .collect(Collectors.toList());
            assetClasses.addAll(level1Asset);
            Decimal128 assClasWthTopContrbtn = (Decimal128) level1Asset.get(0).get(Field.contribution);
            List<String> assClasWthHighWght = level1Asset.stream().filter(asset -> ((Decimal128) asset.get(Field.contribution)).compareTo(assClasWthTopContrbtn) == 0)
                    .map(asset -> (String) asset.get("level1Name"))
                    .collect(Collectors.toList());
            gac.setAssClasWthHighWght(assClasWthHighWght);
            gac.setAssClasWthTopContrbtn(assClasWthTopContrbtn);
        }

        if (breakdownRows2 != null) {
            assetClasses.addAll(getAssetClass(breakdownRows2, 2L));
        }

        if (breakdownRows3 != null) {
            assetClasses.addAll(getAssetClass(breakdownRows3, 3L));
        }

        return CollectionUtils.isNotEmpty(assetClasses) ? assetClasses : null;
    }

    private List<Map<String, Object>> getAssetClass(List<Map<String, Object>> breakdownRows, Long levelNum) {
        return breakdownRows.stream()
                .filter(breakdownRow -> !StringUtils.isEmpty((String) breakdownRow.get(CONTRIBUTIONVALUE))
                        && (Decimal128.parse((String) breakdownRow.get(CONTRIBUTIONVALUE))).compareTo(Decimal128.parse("0.0")) != 0)
                .map(breakdownRow -> {
                    String[] names = ((String) breakdownRow.get("NAME")).split("\\|");
                    Map<String, Object> assetClass = new HashMap<>();
                    assetClass.put("rowid", UUID.randomUUID().toString());
                    assetClass.put("level", levelNum);
                    assetClass.put("level1Name", names[0]);
                    if (levelNum.compareTo(2L) >= 0) {
                        assetClass.put("level2Name", names[1]);
                    }
                    if (levelNum.compareTo(3L) >= 0) {
                        assetClass.put("level3Name", names[2]);
                    }
                    assetClass.put(Field.contribution, Decimal128.parse((String) breakdownRow.get(CONTRIBUTIONVALUE)));
                    return assetClass;
                }).collect(Collectors.toList());
    }

    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
