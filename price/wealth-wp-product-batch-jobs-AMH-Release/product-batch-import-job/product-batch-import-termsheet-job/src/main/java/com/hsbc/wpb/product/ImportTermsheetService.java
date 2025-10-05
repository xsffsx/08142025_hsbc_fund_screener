package com.dummy.wpb.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.product.configuration.TermsheetConfiguration;
import com.dummy.wpb.product.constant.BatchConstants;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.model.TermsheetFile;
import com.dummy.wpb.product.model.graphql.*;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.OperationService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

import static com.dummy.wpb.product.ImportTermsheetJobApplication.JOB_NAME;

@Service
@Slf4j
public class ImportTermsheetService implements InitializingBean {

    private static final String DOC_STATUS_ACTIVE = "A";

    private static final String DOC_SERVER_STATUS_C = "C";

    private static final String FIN_DOC_CUST_CLASS_CDE_ALL = "ALL";

    private static final String FIN_DOC_LANG_BL = "BL";

    private static final String FIN_DOC_LANG_ZH = "ZH";

    private static final String FIN_DOC_LANG_EN = "EN";

    private static final String FIN_DOC_LANG_TW = "TW";

    private static final String CONST_FILTER = "filter";

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ProductService productService;

    @Autowired
    private OperationService operationService;

    private final TermsheetConfiguration termsheetConfiguration;

    private final GraphQLService graphQLService;

    public ImportTermsheetService(TermsheetConfiguration termsheetConfiguration, GraphQLService graphQLService, ProductService productService) {
        this.termsheetConfiguration = termsheetConfiguration;
        this.graphQLService = graphQLService;
        this.productService = productService;
    }

    /**
     * Check if incoming file is valid
     */
    public boolean isValidIncomingFile(File directory, File ackFile) {
        boolean isValid = true;
        // 1. check valid ack files, sample: sid_ap_FH9450_bl.ack
        String[] filenameArray = ackFile.getName().split(BatchConstants.UNDERLINE);
        if (filenameArray.length != 4) {
            log.warn("Invalid ack file name [{}] ", ackFile.getName());
            isValid = false;
        } else {
            // 2. check incoming pdf file
            String pdfName = ackFile.getName().replace(BatchConstants.FILE_SUFFIX_ACK, BatchConstants.FILE_SUFFIX_PDF);
            List<File> pdfFiles = CommonUtils.scanFileInPath(directory, pdfName, BatchConstants.FILE_SUFFIX_PDF);
            if (CollectionUtils.isEmpty(pdfFiles)) {
                log.warn("Incoming pdf file not found for [{}]", ackFile.getName());
                isValid = false;
            }
        }
        return isValid;
    }

    @SuppressWarnings("rawtypes")
    public void processTermsheetData(Document originalProduct, TermsheetFile termsheetFile, File ackFile) throws Exception {
        String ctryRecCde = originalProduct.getString(Field.ctryRecCde);
        String grpMembrRecCde = originalProduct.getString(Field.grpMembrRecCde);
        String prodTypeCde = originalProduct.getString(Field.prodTypeCde);
        String prodAltPrimNum = originalProduct.getString(Field.prodAltPrimNum);
        Document updatedProduct = JsonUtil.deepCopy(originalProduct);
        // update product status from P to A
        if (BatchConstants.PRODUCT_STATUS_PENDING.equals(updatedProduct.getString(Field.prodStatCde))) {
            updatedProduct.put(Field.prodStatCde, BatchConstants.PRODUCT_STATUS_ACTIVE);
        }
        // get current product financial document list
        List<Map<String, Object>> prodFinDocs = (List<Map<String, Object>>) updatedProduct.get(Field.finDoc);
        if (CollectionUtils.isEmpty(prodFinDocs)) {
            prodFinDocs = new ArrayList<>();
        }
        // handle termsheet file by language
        String language = termsheetFile.getLangFinDocCde();
        if (FIN_DOC_LANG_BL.equals(language)) {
            // English
            Document finDocEn = processFinDoc(termsheetFile, FIN_DOC_LANG_EN);
            processProdFinDoc(prodFinDocs, termsheetFile, FIN_DOC_LANG_EN, finDocEn.getInteger(Field.rsrcItemIdFinDoc));
            // Chinese (simplified)
            Document finDocZh = processFinDoc(termsheetFile, FIN_DOC_LANG_ZH);
            processProdFinDoc(prodFinDocs, termsheetFile, FIN_DOC_LANG_ZH, finDocZh.getInteger(Field.rsrcItemIdFinDoc));
            // Chinese (traditional)
            Document finDocTw = processFinDoc(termsheetFile, FIN_DOC_LANG_TW);
            processProdFinDoc(prodFinDocs, termsheetFile, FIN_DOC_LANG_TW, finDocTw.getInteger(Field.rsrcItemIdFinDoc));
        } else {
            Document finDoc = processFinDoc(termsheetFile, language);
            processProdFinDoc(prodFinDocs, termsheetFile, language, finDoc.getInteger(Field.rsrcItemIdFinDoc));
        }
        // put newProdFinDocs into product.finDoc
        updatedProduct.put(Field.finDoc, prodFinDocs);

        // call graphql service to update product table
        try {
            ProductBatchUpdateResult updateResult = saveProductData(originalProduct, updatedProduct);
            // Add .bak suffix for ack file after updated
            if (updateResult != null && CollectionUtils.isEmpty(updateResult.getInvalidProducts())) {
                CommonUtils.addFileSuffix(ackFile, BatchConstants.FILE_SUFFIX_BAK);
            }
        } catch (Exception e) {
            log.warn("Product update failed (CTRY_REC_CDE: {}, GRP_MEMBR_REC_CDE: {}, PROD_TYPE_CDE: {}, PROD_ALT_PRIM_NUM: {}) ",
                    ctryRecCde, grpMembrRecCde, prodTypeCde, prodAltPrimNum, e);
        }
    }

    private Document processFinDoc(TermsheetFile termsheetFile, String language) throws Exception {
        Document finDoc = queryFinDocByKey(
                termsheetFile.getCtryRecCde(),
                termsheetFile.getGrpMembrRecCde(),
                termsheetFile.getDocFinTypeCde(),
                termsheetFile.getProdTypeCde(),
                termsheetFile.getProdAltPrimNum(),
                language);
        if (finDoc == null) {
            finDoc = createFinDocByLang(termsheetFile, language);
        } else {
            updateFinDocByLang(termsheetFile, finDoc, language);
        }
        return finDoc;
    }

    private void processProdFinDoc(List<Map<String, Object>> prodFinDocs, TermsheetFile termsheetFile, String language, Integer rsrcItemIdFinDoc) {
        boolean ifProdFinDocMatched = false;
        for (Map<String, Object> prodFinDoc : prodFinDocs) {
            if (termsheetFile.getDocFinTypeCde().equals(prodFinDoc.get(Field.docFinTypeCde))
                    && language.equals(prodFinDoc.get(Field.langFinDocCde))) {
                ifProdFinDocMatched = true;
                updateProdFinDoc(termsheetFile, prodFinDoc, language);
                break;
            }
        }
        if (!ifProdFinDocMatched) {
            Document newProdFinDoc = createProdFinDoc(termsheetFile, language, rsrcItemIdFinDoc);
            prodFinDocs.add(newProdFinDoc);
        }
    }

    /**
     * Query product by p code
     * @param ctryRecCde country code (e.g. GB)
     * @param grpMembrRecCde  organization code (e.g. HBEU)
     * @param prodTypeCde product type (e.g. SID)
     * @param prodAltPrimNum p code
     * @return null if product not found
     */
    @SneakyThrows
    public Document queryProductByCode(String ctryRecCde, String grpMembrRecCde, String prodTypeCde, String prodAltPrimNum) {
        String query = CommonUtils.readResource("/gql/prod-fin-doc-query.gql");
        String dataPath = ("data.productByFilter");
        Criteria criteria = new Criteria()
                .and(Field.ctryRecCde).is(ctryRecCde)
                .and(Field.grpMembrRecCde).is(grpMembrRecCde)
                .and(Field.prodTypeCde).is(prodTypeCde)
                .and(Field.prodAltPrimNum).is(prodAltPrimNum)
                .and(Field.prodStatCde).ne(BatchConstants.PRODUCT_STATUS_DELISTED);
        GraphQLRequest graphQLRequest = buildGraphQlQueryRequest(query, dataPath, criteria);

        List<Map<String, Object>> list = graphQLService.executeRequest(graphQLRequest, List.class);
        return getFirstDocument(list);
    }

    /**
     * Query financial document by id
     * @param ctryRecCde country code
     * @param grpMembrRecCde organization code
     * @param rsrcItemIdFinDoc financial document id
     * @return null if fin doc not found
     */
    @SneakyThrows
    public Document queryFinDocById(String ctryRecCde, String grpMembrRecCde, Integer rsrcItemIdFinDoc) {
        String query = CommonUtils.readResource("/gql/fin-doc-query.gql");
        String dataPath = "data.finDocByFilter";
        Criteria criteria = new Criteria()
                .and(Field.ctryRecCde).is(ctryRecCde)
                .and(Field.grpMembrRecCde).is(grpMembrRecCde)
                .and(Field.rsrcItemIdFinDoc).is(rsrcItemIdFinDoc);
        GraphQLRequest graphQLRequest = buildGraphQlQueryRequest(query, dataPath, criteria);

        List<Map<String, Object>> list = graphQLService.executeRequest(graphQLRequest, List.class);
        return getFirstDocument(list);
    }


    /**
     * Query financial document by key
     * @param ctryRecCde country code
     * @param grpMembrRecCde organization code
     * @param docFinTypeCde Fin doc type
     * @param docFinCatCde Fin doc category
     * @param docFinCde Fin doc code
     * @param langFinDocCde Fin doc language
     * @return null if fin doc not found
     */
    @SneakyThrows
    public Document queryFinDocByKey(String ctryRecCde,
                                String grpMembrRecCde,
                                String docFinTypeCde,
                                String docFinCatCde,
                                String docFinCde,
                                String langFinDocCde) {
        String query = CommonUtils.readResource("/gql/fin-doc-query.gql");
        String dataPath = "data.finDocByFilter";
        Criteria criteria = new Criteria()
                .and(Field.ctryRecCde).is(ctryRecCde)
                .and(Field.grpMembrRecCde).is(grpMembrRecCde)
                .and(Field.docFinTypeCde).is(docFinTypeCde)
                .and(Field.docFinCatCde).is(docFinCatCde)
                .and(Field.docFinCde).is(docFinCde)
                .and(Field.langFinDocCde).is(langFinDocCde);

        GraphQLRequest graphQLRequest = buildGraphQlQueryRequest(query, dataPath, criteria);

        List<Map<String, Object>> list = graphQLService.executeRequest(graphQLRequest, List.class);
        return getFirstDocument(list);
    }

    /**
     * Build GraphQL query request
     * @param query resource/gql/***.gql
     * @param dataPath json path of GrahpQL response
     * @param criteria GraphQL criteria
     */
    private GraphQLRequest buildGraphQlQueryRequest(String query, String dataPath, Criteria criteria) {
        GraphQLRequest graphQLRequest = new GraphQLRequest();
        graphQLRequest.setQuery(query);
        graphQLRequest.setDataPath(dataPath);
        graphQLRequest.setVariables(Collections.singletonMap(CONST_FILTER,new Query(criteria).getQueryObject()));
        return graphQLRequest;
    }

    /**
     * Get the first element from GraphQL response and parse into Document
     * @param list GraphQL response data list
     * @return null if list is empty
     */
    private Document getFirstDocument(List<Map<String, Object>> list) {
        Document result = null;
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, Object> map = list.get(0);
            map.entrySet().removeIf(e -> null == e.getValue());
            result = new Document(map);
        }
        return result;
    }

    /**
     * Create financial document
     */
    public Document createFinDocByLang(TermsheetFile termsheetFile, String language) throws Exception {
        Document finDoc = queryFinDocByKey(
                termsheetFile.getCtryRecCde(),
                termsheetFile.getGrpMembrRecCde(),
                termsheetFile.getDocFinTypeCde(),
                termsheetFile.getProdTypeCde(),
                termsheetFile.getProdAltPrimNum(),
                language);
        if (finDoc == null) {
            Document newFinDoc = handleFinDocFields(termsheetFile, new Document(), language);
            // call graphql to create fin doc
            String query = CommonUtils.readResource("/gql/fin-doc-batch-create.gql");
            String dataPath = "data.finDocBatchCreate";
            Map<String, Object> variables = new HashMap<>();
            variables.put("finDoc", newFinDoc);
            GraphQLRequest request = buildGraphQlMutationRequest(query, dataPath, variables);
            FinDocBatchCreateResult createResult = graphQLService.executeRequest(request, FinDocBatchCreateResult.class);
            if (CollectionUtils.isNotEmpty(createResult.getInvalidFinDocs())) {
                log.error("Create financial document failed.");
                for (InvalidFinDoc invalidFinDoc : createResult.getInvalidFinDocs()) {
                    for (InvalidProductError error : invalidFinDoc.getErrors()) {
                        log.error(objectMapper.writeValueAsString(error));
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(createResult.getCreatedFinDocs())) {
                finDoc = createResult.getCreatedFinDocs().get(0);
            }
        }

        return finDoc;
    }
    
    /**
     * Update financial document
     */
    public void updateFinDocByLang(TermsheetFile file, Document finDoc, String language) throws Exception {
        Document newFinDoc =  handleFinDocFields(file, finDoc, language);
        // call graphql to update fin doc
        String query = CommonUtils.readResource("/gql/fin-doc-batch-update.gql");
        String dataPath = "data.finDocBatchUpdate";
        Map<String, Object> variables = new HashMap<>();
        Map<String, String> filter = new HashMap<>();
        filter.put(Field._id, String.valueOf(finDoc.get(Field._id)));
        variables.put(CONST_FILTER, filter);
        variables.put("operations", operationService.calcOperations(finDoc, newFinDoc));
        GraphQLRequest request = buildGraphQlMutationRequest(query, dataPath, variables);
        FinDocBatchUpdateResult updateResult = graphQLService.executeRequest(request, FinDocBatchUpdateResult.class);
        if (CollectionUtils.isNotEmpty(updateResult.getInvalidFinDocs())) {
            log.error("Update financial document failed.");
            for (InvalidFinDoc invalidFinDoc : updateResult.getInvalidFinDocs()) {
                for (InvalidProductError error : invalidFinDoc.getErrors()) {
                    log.error(objectMapper.writeValueAsString(error));
                }
            }
        }
    }

    /**
     * Create product financial document
     * @param file
     * @param language
     * @param seqNum
     * @return
     */
    public Document createProdFinDoc(TermsheetFile file, String language, long seqNum) {
        Document newProdFinDoc = new Document();
        handleProdFinDocFields(file, newProdFinDoc, language);
        newProdFinDoc.put(Field.rsrcItemIdFinDoc, seqNum);
        newProdFinDoc.put(Field.recCreatDtTm, LocalDateTime.now());
        return newProdFinDoc;
    }

    /**
     * Update product financial document
     * @param file
     * @param prodFinDoc
     * @param language
     * @return
     */
    @SuppressWarnings("rawtypes")
    public void updateProdFinDoc(TermsheetFile file, Map prodFinDoc, String language) {
        handleProdFinDocFields(file, prodFinDoc, language);
    }

    /**
     * Handle financial document fields for create / update
     * @param file
     * @param finDoc
     * @param language
     * @return
     */
    private Document handleFinDocFields(TermsheetFile file, Document finDoc, String language) {
        Document newFinDoc = new Document();
        if (finDoc != null) {
            newFinDoc = new Document(finDoc);
        }

        newFinDoc.put(Field.ctryRecCde, file.getCtryRecCde());
        newFinDoc.put(Field.grpMembrRecCde, file.getGrpMembrRecCde());
        newFinDoc.put(Field.docFinCatCde, file.getProdTypeCde());
        newFinDoc.put(Field.docFinCde, file.getProdAltPrimNum());

        newFinDoc.put(Field.docStatCde, DOC_STATUS_ACTIVE);
        newFinDoc.put(Field.docServrStatCde, DOC_SERVER_STATUS_C);

        newFinDoc.put(Field.docFinTypeCde, file.getDocFinTypeCde());
        newFinDoc.put(Field.langFinDocCde, language);
        newFinDoc.put(Field.urlFileServrText, termsheetConfiguration.getSidUrl());
        newFinDoc.put(Field.formtDocCde, file.getType());

        File pdfFile = file.getRawFile();
        newFinDoc.put(Field.docSzNum, pdfFile.length());
        newFinDoc.put(Field.docRecvName, pdfFile.getName());
        newFinDoc.put(Field.fileDocName, pdfFile.getName());

        return newFinDoc;
    }

    /**
     * Handle product financial document fields for create / update
     * @param file
     * @param prodFinDoc
     * @param language
     * @return 
     */
    @SuppressWarnings("rawtypes")
    private void handleProdFinDocFields(TermsheetFile file, Map prodFinDoc, String language) {
        prodFinDoc.put(Field.docFinTypeCde, file.getDocFinTypeCde());
        prodFinDoc.put(Field.finDocCustClassCde, FIN_DOC_CUST_CLASS_CDE_ALL);
        prodFinDoc.put(Field.langFinDocCde, language);
        prodFinDoc.put(Field.recUpdtDtTm, LocalDateTime.now());
        String urlDocText = termsheetConfiguration.getSidUrl() + file.getRawFile().getName();
        prodFinDoc.put(Field.urlDocText, urlDocText);
    }

    /**
     * Build graphql mutation request (fin-doc-batch-create.gql / fin-doc-batch-update.gql) 
     * @param query
     * @param dataPath
     * @param variables
     * @return
     */
    public GraphQLRequest buildGraphQlMutationRequest(String query, String dataPath, Map<String, Object> variables) {
        GraphQLRequest graphQLRequest = new GraphQLRequest();
        graphQLRequest.setQuery(query);
        graphQLRequest.setDataPath(dataPath);
        graphQLRequest.setVariables(variables);

        return graphQLRequest;
    }

    /**
     * Call graphql to update product table
     * @param originalProduct
     * @param updatedProduct
     * @throws Exception
     */
    @SneakyThrows
    public ProductBatchUpdateResult saveProductData(Document originalProduct, Document updatedProduct) {
        List<Operation> operations = operationService.calcOperations(originalProduct, updatedProduct);

        if (CollectionUtils.isEmpty(operations)){
            return null;
        }

        operations.add(new Operation("put", Field.lastUpdatedBy, JOB_NAME));
        ProductBatchUpdateByIdInput updateInput = new ProductBatchUpdateByIdInput();
        updateInput.setProdId(updatedProduct.get(Field.prodId, Number.class).longValue());
        updateInput.setOperations(operations);

        ProductBatchUpdateResult updateResult = productService.batchUpdateProductById(Collections.singletonList(updateInput));
        logProductBatchUpdateResult(updateResult,
                originalProduct.getString(Field.ctryRecCde),
                originalProduct.getString(Field.grpMembrRecCde),
                originalProduct.getString(Field.prodTypeCde),
                originalProduct.getString(Field.prodAltPrimNum));
        return updateResult;
    }

    /**
     * Log product batch update result from graphql
     * @param updateResult
     * @param ctryRecCde
     * @param grpMembrRecCde
     * @param prodTypeCde
     * @param prodAltPrimNum
     */
    private void logProductBatchUpdateResult(ProductBatchUpdateResult updateResult,
            String ctryRecCde,
            String grpMembrRecCde,
            String prodTypeCde,
            String prodAltPrimNum) {
        if (updateResult != null) {
            if (CollectionUtils.isEmpty(updateResult.getInvalidProducts())) {
                log.info("Product update succeeded (CTRY_REC_CDE: {}, GRP_MEMBR_REC_CDE: {}, PROD_TYPE_CDE: {}, PROD_ALT_PRIM_NUM: {})",
                        ctryRecCde, grpMembrRecCde, prodTypeCde, prodAltPrimNum);
            } else {
                for (InvalidProduct invalidProduct : updateResult.getInvalidProducts()) {
                    invalidProduct.getErrors().forEach(err -> log.warn("[{}] {}", err.getJsonPath(), err.getMessage()));
                }
                log.warn("Product update failed (CTRY_REC_CDE: {}, GRP_MEMBR_REC_CDE: {}, PROD_TYPE_CDE: {}, PROD_ALT_PRIM_NUM: {})",
                        ctryRecCde, grpMembrRecCde, prodTypeCde, prodAltPrimNum);
            } 
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println();
    }
}
