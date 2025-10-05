/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.service;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ServiceProductKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuotesServiceRequest {

    private List<ServiceProductKey> serviceProductKeys;

    private Boolean delay;

}
