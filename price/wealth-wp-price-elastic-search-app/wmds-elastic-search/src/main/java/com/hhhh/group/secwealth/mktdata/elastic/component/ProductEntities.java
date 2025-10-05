/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.component;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.Product;

@Component
public class ProductEntities {

	// key=supportSite +"_" + product type
	@Autowired
	@Qualifier(value = "productEntitiesMap")
	private Map<String, Product> prodEntities;

	public Map<String, Product> getProductEntities() {
		return this.prodEntities;
	}

	public void setProductEntities(Map<String, Product> productEntities) {
		this.prodEntities = productEntities;
	}

}
