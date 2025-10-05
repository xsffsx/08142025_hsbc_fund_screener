package com.dummy.wpc.datadaptor.dao;

import java.util.List;

import com.dummy.wpc.datadaptor.to.ProductKeyTO;

public interface IProdDAO {

	public List<ProductKeyTO> retrieveProdAltPrimNumByProdType(final String ctryRecCde, final String grpMemRecCde, final String prodType);
}
