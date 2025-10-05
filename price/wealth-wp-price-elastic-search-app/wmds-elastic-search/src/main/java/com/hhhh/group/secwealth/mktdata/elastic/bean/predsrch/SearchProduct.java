package com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchProduct {
	
    private String externalKey;

    private String prodCdeAltClassCde;

    private CustomizedEsIndexForProduct searchObject;

}
