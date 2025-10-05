package com.dummy.wpb.product;

import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.model.graphql.ReferenceData;
import lombok.Data;

import java.util.List;

@Data
public class BondReutersStreamItem extends ProductStreamItem {
    private List<ReferenceData> missingRefDataList;
}
