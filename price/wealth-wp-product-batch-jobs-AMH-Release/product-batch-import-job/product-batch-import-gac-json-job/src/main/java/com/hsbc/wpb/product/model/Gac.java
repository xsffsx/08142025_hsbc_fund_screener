package com.dummy.wpb.product.model;

import lombok.Data;
import org.bson.types.Decimal128;
import java.util.List;
import java.util.Map;

@Data
public class Gac {
    private String assetType;
    private List<String> assClasWthHighWght;
    private Decimal128 assClasWthTopContrbtn;
    private Map<String,Object> productType;
    private List<Map<String,Object>> assetClass;
}
