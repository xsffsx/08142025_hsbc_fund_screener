package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;

@Data
public class ExlCol {
    private String value;
    private String mandatory;
    private String type;

    public ExlCol() {
    }

    public ExlCol(String value, String mandatory, String type) {
        this.value = value;
        this.mandatory = mandatory;
        this.type = type;
    }
}
