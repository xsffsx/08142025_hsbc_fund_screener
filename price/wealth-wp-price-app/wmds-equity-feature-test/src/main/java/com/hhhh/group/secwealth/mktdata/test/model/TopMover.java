package com.hhhh.group.secwealth.mktdata.test.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TopMover {

    @JsonAlias("chainKey")
    private String chainKey;

    @JsonAlias("boardType")
    private String boardType;

    @JsonAlias("tableKey")
    private String tableKey;

    @JsonAlias("products")
    private List<TopMoverProducts> topMoverProducts;

}
