package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.model.ProductTo;
import com.dummy.wpb.product.model.graphql.Operation;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateInput;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.utils.BsonUtils;
import com.dummy.wpb.product.utils.JsonPathUtils;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.dummy.wpb.product.ImportEliFinDocJobApplication.JOB_NAME;


@Service
@Slf4j
public class ImportEliFinDocService {

	@Autowired
	public ProductService productService;

	public Document queryProductByPriNum(ProductTo productTo) {
		List<Document> prod = productService.productByFilters(BsonUtils.toMap(buildProductFilter(productTo)));
		return CollectionUtils.isEmpty(prod) ? null : prod.get(0);
	}

	private Bson buildProductFilter(ProductTo productTo) {
		return Filters.and(Filters.eq(Field.ctryRecCde, productTo.getCtryRecCde()),
				Filters.eq(Field.grpMembrRecCde, productTo.getGrpMembrRecCde()),
				Filters.eq(Field.prodTypeCde, productTo.getProdTypeCde()),
				Filters.eq(Field.prodAltPrimNum, productTo.getProdAltPrimNum()));
	}

	public void updateToDB(Document productTO, String docFinTypeCde, String urlDocText) {
		List<Map<String, Object>> prodFinDocs = JsonPathUtils.readValue(productTO, Field.finDoc);
		List<Map<String, Object>> finDocs = CollectionUtils.isEmpty(prodFinDocs) ? new ArrayList<>() : prodFinDocs;
		prepareFindoc(docFinTypeCde, FinDocConstants.LANG_CDE_EN, urlDocText, finDocs);
		prepareFindoc(docFinTypeCde, FinDocConstants.LANG_CDE_TW, urlDocText, finDocs);
		String prodStatCde = productTO.getString(Field.prodStatCde).equals(FinDocConstants.PENDING) ? FinDocConstants.APPROVAL : productTO.getString(Field.prodStatCde);
		updateProdByFilters(productTO.getInteger(Field.prodId).longValue(), finDocs, prodStatCde);
	}

	private void prepareFindoc(String docFinTypeCde, String langFinDocCde, String urlDocText, List<Map<String, Object>> finDocs) {
		Optional<Map<String, Object>> finDoc = finDocs.stream()
				.filter(fd -> StringUtils.equals((String)fd.get(Field.docFinTypeCde), docFinTypeCde) &&
						StringUtils.equals((String)fd.get(Field.langFinDocCde), langFinDocCde)).findFirst();
		if (CollectionUtils.isEmpty(finDocs) || !finDoc.isPresent()) {
			finDocs.add(newFinDoc(docFinTypeCde, langFinDocCde, urlDocText));
		} else {
			Map<String, Object> finDocMap = finDoc.get();
			finDocMap.put(Field.urlDocText, urlDocText);
			finDocMap.put(Field.recUpdtDtTm, new DateTime(DateTimeZone.UTC).toString());
		}
	}

	private Document newFinDoc(String docFinTypeCde, String langFinDocCde, String urlDocText) {
		Document finDoc = new Document();
		finDoc.put(Field.docFinTypeCde, docFinTypeCde);
		finDoc.put(Field.langFinDocCde, langFinDocCde);
		finDoc.put(Field.rsrcItemIdFinDoc, null);
		finDoc.put(Field.urlDocText, urlDocText);
		finDoc.put(Field.finDocConHashCde, null);
		finDoc.put(Field.finDocCustClassCde, FinDocConstants.CUST_CLASS_CDE_DEFAULT);
		return finDoc;
	}

	public void updateProdByFilters(Long prodId, List<Map<String, Object>> finDocs,
									String prodStatCde) {
		Map<String, Object> filterMap = BsonUtils.toMap(Filters.eq(Field.prodId, prodId));
		List<Operation> opList = buildOps("put", Field.finDoc, finDocs);
		List<Operation> opList2 = buildOps("put", Field.prodStatCde, prodStatCde);
		List<Operation> opList3 = buildOps("put", Field.lastUpdatedBy, JOB_NAME);
		opList.addAll(opList2);
		opList.addAll(opList3);
		ProductBatchUpdateResult updateResult = productService.batchUpdateProduct(new ProductBatchUpdateInput(filterMap, opList));
		updateResult.logUpdateResult(log);
	}

	public List<Operation> buildOps(String opAction, String fieldPath, Object value) {
		List<Operation> ops = new LinkedList<>();
		Operation op = new Operation();
		op.setOp(opAction);
		op.setPath(fieldPath);
		op.setValue(value);
		ops.add(op);
		return ops;
	}
}