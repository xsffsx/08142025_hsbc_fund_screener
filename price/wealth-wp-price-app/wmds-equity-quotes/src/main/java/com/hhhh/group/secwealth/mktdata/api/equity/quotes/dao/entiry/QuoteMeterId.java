/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Getter
@Setter
public class QuoteMeterId implements Serializable {


    private static final long serialVersionUID = -65588770973068609L;

    private String subscriberId;

    private int exchangeId;

    public QuoteMeterId() {

    }

}
