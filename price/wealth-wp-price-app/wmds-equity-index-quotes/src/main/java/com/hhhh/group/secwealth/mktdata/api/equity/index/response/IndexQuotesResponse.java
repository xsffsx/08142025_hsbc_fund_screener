/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.index.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndexQuotesResponse {

	private List<Indice> indices;
	
	private List<Messages> messages;
}
