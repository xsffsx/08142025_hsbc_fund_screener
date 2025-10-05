package com.dummy.wpb.product.component;

import com.dummy.wpb.product.configuration.FundHouseCdeHolder;
import com.dummy.wpb.product.constant.BatchConstants;
import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.utils.JsonPathUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

import static com.dummy.wpb.product.constant.BatchConstants.*;
import static com.dummy.wpb.product.constant.BatchImportAction.ADD;
import static com.dummy.wpb.product.constant.BatchImportAction.UPDATE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SystemFormatProcessor implements ItemProcessor<ProductStreamItem, ProductStreamItem> {

    @Value("#{jobParameters['systemCde']}")
    private String systemCde;

    @Value("${batch.format-system}")
    private String formatSystemCode;

    @Override
    public ProductStreamItem process(ProductStreamItem streamItem) {
        if (StringUtils.equals(systemCde, formatSystemCode)) {
            initFormatField(streamItem);
            formatProdStatCde(streamItem);
            formatChanlAttrFields(streamItem);
        }

        return streamItem;
    }

    private void initFormatField(ProductStreamItem streamItem) {
        BatchImportAction actionCode = streamItem.getActionCode();
        if (ADD == actionCode) {
            Document importProduct = streamItem.getImportProduct();
            String prodSubtpCde = importProduct.getString(Field.prodSubtpCde);

            String asetUndlCde = EMPTY;
            if (StringUtils.equalsAny(prodSubtpCde, "GE", "RE", "SI", "SE", "IX", "HF")) {
                asetUndlCde = BatchConstants.EQUITY;
            } else if (StringUtils.equals(prodSubtpCde, "BD")) {
                asetUndlCde = BatchConstants.FIXED_INCOME;
            } else if (StringUtils.equalsAny(prodSubtpCde, "BA", "GF", "PS", "OT")) {
                asetUndlCde = BatchConstants.HYBRID;
            } else if (StringUtils.equals(prodSubtpCde, "MR")) {
                asetUndlCde = BatchConstants.FOREIGN_CURRENCIES;
            }
            if (StringUtils.isNotBlank(asetUndlCde)) {
                Map<String, Object> undlAset = new HashMap<>();
                undlAset.put("asetUndlCde", asetUndlCde);
                undlAset.put("seqNum", 1);
                JsonPathUtils.setValue(importProduct, "undlAset", undlAset);
            }

            if (StringUtils.equals(prodSubtpCde, "GF")) {
                importProduct.put("cptlGurntProdInd", INDICATOR_YES);
            }

            if (null != JsonPathUtils.readValue(importProduct, "utTrstInstm.offerEndDtTm")) {
                JsonPathUtils.setValue(importProduct, "utTrstInstm.closeEndFundInd", INDICATOR_YES);
            }

            String fundHouseCde = JsonPathUtils.readValue(importProduct, "utTrstInstm.fundHouseCde");
            if (null != fundHouseCde) {
                JsonPathUtils.setValue(importProduct, "utTrstInstm.fundHouseCde", FundHouseCdeHolder.getFundHouseCde(fundHouseCde));
            }
        }
    }

    private void formatProdStatCde(ProductStreamItem streamItem) {
        Document origProd = streamItem.getOriginalProduct();
        Document importProduct = streamItem.getImportProduct();

        BatchImportAction actionCode = streamItem.getActionCode();

        if (UPDATE == actionCode) {
            if (PENDING.equals(origProd.getString(Field.prodStatCde))) {
                importProduct.put(Field.prodStatCde, PENDING);
            }
            if (StringUtils.isBlank(importProduct.getString(Field.prodStatCde))) {
                importProduct.put(Field.prodStatCde, ACTIVE);
            }
        } else if (ADD == actionCode) {
            String prodStatCde = importProduct.getString(Field.prodStatCde);
            if (StringUtils.isBlank(prodStatCde) || StringUtils.equalsAny(prodStatCde, CLOSED_FROM_SUBSCRIPTION, DELISTED, SUSPENDED, TERMINATED)) {
                importProduct.put(Field.prodStatCde, PENDING);
            }
        }
    }

    private void formatChanlAttrFields(ProductStreamItem streamItem) {
        Document origProd = streamItem.getOriginalProduct();
        Document importProd = streamItem.getImportProduct();
        BatchImportAction actionCode = streamItem.getActionCode();

        String[] chanlAttrFields = new String[]{};

        if (UPDATE == actionCode) {
            if (ObjectUtils.isNotEmpty(importProd.get(Field.chanlAttr)) && ObjectUtils.isEmpty(origProd.get(Field.chanlAttr))) {
                chanlAttrFields = new String[]{"N", "N", "N", "N", "N"};
            } else {
                chanlAttrFields = updateFormat(streamItem);
            }
        } else if (ADD == actionCode) {
            chanlAttrFields = addFormat(streamItem);
        }

        setChanlAttrFields(importProd, chanlAttrFields);
    }

    private String[] addFormat(ProductStreamItem streamItem) {
        Document importProd = streamItem.getImportProduct();

        String prodStatCde = MapUtils.getString(importProd, Field.prodStatCde, EMPTY);

        switch (prodStatCde) {
            case EMPTY:
                String[] fields = new String[5];
                fields[0] = "Y";
                fields[2] = JsonPathUtils.readValue(importProd, "utTrstInstm.suptMipInd", INDICATOR_YES);
                String closeEndFundInd = JsonPathUtils.readValue(importProd, "utTrstInstm.closeEndFundInd");
                if (StringUtils.equals(INDICATOR_YES, closeEndFundInd)) {
                    fields[1] = "N";
                    fields[3] = "N";
                    fields[4] = "N";
                } else {
                    fields[1] = "Y";
                    fields[3] = "Y";
                    fields[4] = "Y";
                }
                return fields;
            case DELISTED:
                return new String[]{"N", "Y", "N", "N", "N"};
            case SUSPENDED:
                return new String[]{"N", "N", null, "N", "N"};
            case CLOSED_FROM_SUBSCRIPTION:
                return new String[]{"N", "Y", "N", "N", "Y"};
            case TERMINATED:
                return new String[]{"N", "N", "N", "N", "N"};
            default:
                return new String[]{};

        }
    }

    private String[] updateFormat(ProductStreamItem streamItem) {
        Document origProd = streamItem.getOriginalProduct();
        Document importProd = streamItem.getImportProduct();

        if (ObjectUtils.isEmpty(importProd.get(Field.chanlAttr)) && ObjectUtils.isNotEmpty(importProd.get(Field.chanlAttr))) {
            return new String[]{"Y", "Y",
                    MapUtils.getString(importProd, Field.suptMipInd, "Y"), "Y", "Y"};
        }


        String prodStatCde = importProd.getString(Field.prodStatCde);
        switch (prodStatCde) {
            case ACTIVE:
            case PENDING:
            case CLOSED_FROM_SUBSCRIPTION:
                return new String[]{origProd.getString(Field.allowBuyProdInd), origProd.getString(Field.allowSellProdInd),
                        origProd.getString(Field.allowSellMipProdInd), origProd.getString(Field.allowSwInProdInd), origProd.getString(Field.allowSwOutProdInd)};

            case DELISTED:
                return new String[]{"N", "Y", "N", "N", "N"};

            case SUSPENDED:
                return new String[]{"N", "N", origProd.getString(Field.allowSellMipProdInd), "N", "N"};

            case TERMINATED:
                return new String[]{"N", "N", "N", "N", "N"};
            default:
                return new String[]{};
        }
    }

    private void setChanlAttrFields(Document prod, String[] chanlAttrFields) {
        if (chanlAttrFields.length == 5) {
            prod.put(Field.allowBuyProdInd, chanlAttrFields[0]);
            prod.put(Field.allowSellProdInd, chanlAttrFields[1]);
            prod.put(Field.allowSellMipProdInd, chanlAttrFields[2]);
            prod.put(Field.allowSwInProdInd, chanlAttrFields[3]);
            prod.put(Field.allowSwOutProdInd, chanlAttrFields[4]);
        }
    }
}
