package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.BatchConstants;
import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.model.ExcelColumnInfo;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.error.ErrorLogger;
import com.dummy.wpb.product.utils.BsonUtils;
import com.mongodb.client.model.Filters;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;


@Slf4j
public class BondReutersReader extends AbstractPaginatedDataItemReader<BondReutersStreamItem> {

    @Value("#{jobParameters['ctryRecCde']}")
    String ctryRecCdeParam;

    @Value("#{jobParameters['grpMembrRecCde']}")
    String grpMembrRecCdeParam;

    @Value("#{jobParameters['prodTypeCde']}")
    String prodTypeCdeParam;

    @Autowired
    private ProductService productService;

    private BondReutersService bondReutersService;
    private ReutersBondParseService reutersBondParseService;
    private int currentIndex = 0;

    private static final List<String> IGNORE_FIELDS = Arrays.asList("debtInstm.yieldHist", "prodIdDebtInstm");

    private List<List<ExcelColumnInfo>> excelColumnInfoLists;

    public BondReutersReader(BondReutersService bondReutersService) {
        this.bondReutersService = bondReutersService;
        this.reutersBondParseService = new ReutersBondParseService();
        this.setName("bondReutersReader");
        this.setPageSize(100);
    }

    @Override
    protected void doOpen() {
        try {
            this.excelColumnInfoLists = bondReutersService.run();
        } catch (productBatchException e) {
            ErrorLogger.logErrorMsg(e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    protected Iterator<BondReutersStreamItem> doPageRead() {
        if (CollectionUtils.isEmpty(excelColumnInfoLists)){
            return null;
        }

        int currSize = 0;
        List<List<ExcelColumnInfo>> rowList = new ArrayList<>(pageSize);
        while (currSize < pageSize && currentIndex < excelColumnInfoLists.size()) {
            rowList.add(excelColumnInfoLists.get(currentIndex++));
            currSize++;
        }

        if (rowList.isEmpty()) {
            return null;
        }
        return doRead(rowList);
    }

    private Iterator<BondReutersStreamItem> doRead(List<List<ExcelColumnInfo>> rowList) {

        List<BondReutersStreamItem> result = new ArrayList<>(pageSize);

        List<Document> importProdList = new ArrayList<>(pageSize);
        for (List<ExcelColumnInfo> row : rowList) {
            importProdList.add(reutersBondParseService.parseExcelObject(row));
        }

        List<Document> prodList = productService.productByFilters(buildFilter(importProdList), IGNORE_FIELDS);

        for (Document importProd : importProdList) {
            BondReutersStreamItem streamItem = new BondReutersStreamItem();
            streamItem.setImportProduct(importProd);

            for (int i = 0; i < prodList.size(); i++) {
                if (Objects.equals(importProd.get(Field.prodAltPrimNum), prodList.get(i).get(Field.prodAltPrimNum))) {
                    streamItem.setOriginalProduct(prodList.get(i));
                    prodList.remove(i);//remove the product to save time in next loop
                    break;
                }
            }

            BatchImportAction action = MapUtils.isEmpty(streamItem.getOriginalProduct()) ? BatchImportAction.ADD : BatchImportAction.UPDATE;
            streamItem.setActionCode(action);
            result.add(streamItem);
        }

        return result.iterator();
    }


    private Map<String, Object> buildFilter(List<Document> importProdList) {
        List<String> productKeyList = new ArrayList<>(pageSize);
        importProdList.forEach(importProd -> productKeyList.add(importProd.getString(Field.prodAltPrimNum)));
        Bson filters = Filters.and(Filters.eq(Field.ctryRecCde, ctryRecCdeParam),
                Filters.eq(Field.grpMembrRecCde, grpMembrRecCdeParam),
                Filters.ne(Field.prodStatCde, BatchConstants.DELISTED),
                Filters.in(Field.prodAltPrimNum, productKeyList)
        );

        return BsonUtils.toMap(filters);
    }
}