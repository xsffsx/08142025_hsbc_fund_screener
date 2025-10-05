package com.hhhh.group.secwealth.mktdata.test.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentIPO {

    private String symbol;

    private Integer boardLot;

    private String ipoSponsor;

    private String asOfDateTime;

}
