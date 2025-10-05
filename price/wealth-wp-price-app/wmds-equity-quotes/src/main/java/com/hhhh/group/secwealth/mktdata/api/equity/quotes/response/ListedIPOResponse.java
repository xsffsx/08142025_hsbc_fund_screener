package com.hhhh.group.secwealth.mktdata.api.equity.quotes.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListedIPOResponse{
	
	private List<ListIPOResult> listedIPO;
}
