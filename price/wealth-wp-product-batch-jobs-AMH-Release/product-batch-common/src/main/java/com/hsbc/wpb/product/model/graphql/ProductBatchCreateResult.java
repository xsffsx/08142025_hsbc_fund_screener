package com.dummy.wpb.product.model.graphql;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.error.ErrorCode;
import com.dummy.wpb.product.error.ErrorLogger;
import lombok.Data;
import org.bson.Document;
import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductBatchCreateResult {

	private List<Document> createdProducts;

	private List<InvalidProduct> invalidProducts;

	public void logCreateResult(Logger log) {
		this.logCreateResult(log, null);
	}

	public void logCreateResult(Logger log, String signature){
		for (Document product : createdProducts) {
			String keyInfo = buildProdKeyInfo(product);
			log.info(String.format("\nProduct has been created %s", keyInfo));
		}

		List<String> failedProductList = new ArrayList<>();
		for (InvalidProduct invalidProduct : invalidProducts) {
			Document product = invalidProduct.getProduct();
			String keyInfo = buildProdKeyInfo(product);
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(String.format("\nProduct created failed %s",keyInfo));
			invalidProduct.getErrors().forEach(err -> {
				stringBuilder.append(String.format("\n[%s] %s",err.getJsonPath(),err.getMessage()));
				failedProductList.add(product.getString(Field.prodAltPrimNum));
			});
			log.info(stringBuilder.toString());
		}
		if (!CollectionUtils.isEmpty(failedProductList) && signature != null) {
			ErrorLogger.logErrorMsg(ErrorCode.OTPSERR_EBJ103, signature, String.join(",", failedProductList));
		}
	}

	private String buildProdKeyInfo(Document prod) {
		String ctryRecCde = prod.getString(Field.ctryRecCde);
		String grpMembrRecCde = prod.getString(Field.grpMembrRecCde);
		String prodTypeCde = prod.getString(Field.prodTypeCde);
		String prodAltPrimNum = prod.getString(Field.prodAltPrimNum);

		return String.format("(ctryRecCde: %s, grpMembrRecCde: %s, prodTypeCde: %s, prodAltPrimNum: %s)", ctryRecCde, grpMembrRecCde, prodTypeCde, prodAltPrimNum);
	}
}
