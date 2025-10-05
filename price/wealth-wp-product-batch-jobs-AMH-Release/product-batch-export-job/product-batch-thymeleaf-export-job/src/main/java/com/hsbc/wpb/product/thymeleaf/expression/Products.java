package com.dummy.wpb.product.thymeleaf.expression;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.ProductType;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class Products {

    public String asetUndlCde(Document product, int index) {
        return product.getList(Field.undlAset, Document.class, Collections.emptyList())
                .stream()
                .filter(undlAset -> undlAset.get("seqNum", Number.class).intValue() == index)
                .findFirst()
                .orElse(new Document())
                .getString(Field.asetUndlCde);
    }

    public Document listProd(Document product, String listProdCde) {
        return product.getList("listProd", Document.class, Collections.emptyList())
                .stream()
                .filter(listProd -> listProdCde.equals(listProd.get("listProdCde")))
                .findFirst()
                .orElse(new Document());
    }

    public String prodAltNum(Document product, String prodCdeAltClassCde) {
        return product.getList(Field.altId, Document.class, Collections.emptyList())
                .stream()
                .filter(altId -> prodCdeAltClassCde.equals(altId.get(Field.prodCdeAltClassCde)))
                .findFirst()
                .orElse(new Document())
                .getString(Field.prodAltNum);
    }

    public String creditRtngCde(Document debtInstm, String creditRtngAgcyCde) {
        return debtInstm.getList(Field.credRtng, Document.class, Collections.emptyList())
                .stream()
                .filter(credRtng -> creditRtngAgcyCde.equals(credRtng.get(Field.creditRtngAgcyCde)))
                .findFirst()
                .orElse(new Document())
                .getString(Field.creditRtngCde);
    }

    public Double undlStock(Document eqtyLinkInvst, String field) {
        int size = eqtyLinkInvst.getList(Field.undlStock, Document.class, Collections.emptyList()).size();
        if (size == 1) {
            return eqtyLinkInvst.getList(Field.undlStock, Document.class, Collections.emptyList())
                    .get(0)
                    .getDouble(field);
        }
        return null;
    }

    public String natureCde(Document product) {
        ProductType productType = null;
        try {
            productType = ProductType.valueOf(product.getString(Field.prodTypeCde));
        } catch (IllegalArgumentException e) {
            return "";
        }

        switch (productType) {
            case BOND:
            case SEC:
            case WRTS:
                return "Liquid";
            case UT:
                return product.getString(Field.prodSubtpCde).trim().equals("GF") ? null : "Liquid";
            case DPS:
            case ELI:
            case SN:
            case SID:
                return "Maturing";
            default:
                return "";
        }
    }
}