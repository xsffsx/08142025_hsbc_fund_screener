package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.Field;
import org.bson.Document;
import org.springframework.batch.item.ItemProcessor;

import java.util.Objects;

import static org.apache.commons.lang.StringUtils.EMPTY;

public class ProductPeriodUpdateProcessor implements ItemProcessor<Document, Document> {

    @Override
    public Document process(Document product) throws Exception {
        Long newPrdInvstTnorNum = calculate(product);
        Long oldPrdInvstTnorNum = product.getLong(Field.prdInvstTnorNum);
        if (Objects.equals(newPrdInvstTnorNum, oldPrdInvstTnorNum)) {
            return null;
        }
        return product.append(Field.prdInvstTnorNum, newPrdInvstTnorNum);

    }

    private Long calculate(Document product) {
        if (null != product.get(Field.termRemainDayCnt)) {
            return product.get(Field.termRemainDayCnt, Number.class).longValue();
        }

        Number prdProdNum = product.get(Field.prdProdNum, Number.class);
        if (null == prdProdNum) {
            return null;
        }

        String prdProdCde = product.get(Field.prdProdCde, EMPTY).toUpperCase();
        switch (prdProdCde) {
            case "Y":
                return prdProdNum.longValue() * 365;
            case "M":
                return prdProdNum.longValue() * 30;
            case "W":
                return prdProdNum.longValue() * 7;
            case "D":
                return prdProdNum.longValue();
            default:
                return null;
        }
    }
}
