package com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * <b> Native response from the Cache Distribute. </b>
 * </p>
 */
@Getter
@Setter
@EqualsAndHashCode
public class CacheDistributeResponse implements Serializable {

    private static final long serialVersionUID = -432938821783380282L;

    private String recordLastUpdateTime;

    private String recordExpiryTime;

    private String value;

    private String key;

    private String cacheExistingFlag;

}
