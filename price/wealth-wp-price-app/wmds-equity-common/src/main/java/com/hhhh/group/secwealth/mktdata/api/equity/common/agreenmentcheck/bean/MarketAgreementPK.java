/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.bean;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * <b> The primary key class for Market Agreement. </b>
 * </p>
 */
@Getter
@Setter
public class MarketAgreementPK implements Serializable {

    private static final long serialVersionUID = 3168905736469355680L;

    private String subscriberId;

    private long documentId;
}
