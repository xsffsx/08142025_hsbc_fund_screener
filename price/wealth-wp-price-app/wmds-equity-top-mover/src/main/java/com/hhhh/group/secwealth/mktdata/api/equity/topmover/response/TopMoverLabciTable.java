/**
 * @Title TopMoverLabciTable.java
 * @description TODO
 * @author OJim
 * @time Jun 28, 2019 5:24:08 PM
 */
package com.hhhh.group.secwealth.mktdata.api.equity.topmover.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
/**
 * @Title TopMoverLabciTable.java
 * @description TODO
 * @author OJim
 * @time Jun 28, 2019 5:24:08 PM
 */
@Setter
@Getter
public class TopMoverLabciTable implements Serializable {

    private static final long serialVersionUID = 8482281124124820670L;

    private String chainKey;
    
    private String boardType;

    private String tableKey;

    private List<TopMoverLabciProduct> products;

    // private String exchangeTimezone;
    @Override
    public String toString() {
        return "TopTenTable [chainKey=" + this.chainKey + ", tableKey=" + this.tableKey + ", product=" + this.products + "]";
    }

}
