package com.dummy.wpb.product.model;

import lombok.Data;

@Data
public class ProductType {
    private String level1;
    private String level2;
    private String level3;

    public ProductType(String level1, String level2, String level3) {
        this.level1 = level1;
        this.level2 = level2;
        this.level3 = level3;
    }


}
