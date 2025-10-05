/*
 */

package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DomainError {

	private String errorCode;

	private List<String> params = new ArrayList<String>();

	public DomainError(String code) {
		super();
		this.errorCode = code;
	}

}