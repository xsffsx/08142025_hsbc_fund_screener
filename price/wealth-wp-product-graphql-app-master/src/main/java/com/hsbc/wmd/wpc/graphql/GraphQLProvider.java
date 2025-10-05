package com.dummy.wmd.wpc.graphql;

import com.google.common.io.Resources;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.productDataFetcherExceptionHandler;
import com.dummy.wmd.wpc.graphql.fetcher.*;
import com.dummy.wmd.wpc.graphql.fetcher.amendment.*;
import com.dummy.wmd.wpc.graphql.fetcher.assetvolt.AssetVolatilityClassCharUpdateFetcher;
import com.dummy.wmd.wpc.graphql.fetcher.assetvolt.AssetVolatilityClassCorlUpdateFetcher;
import com.dummy.wmd.wpc.graphql.fetcher.stafflicensecheck.StafLicCheckImportFetcher;
import com.dummy.wmd.wpc.graphql.fetcher.dashboard.GroupByProductStatusFetcher;
import com.dummy.wmd.wpc.graphql.fetcher.dashboard.GroupByProductTypeFetcher;
import com.dummy.wmd.wpc.graphql.fetcher.defaultconfig.DefaultFieldConfigCreateFetcher;
import com.dummy.wmd.wpc.graphql.fetcher.defaultconfig.DefaultFieldConfigFetcher;
import com.dummy.wmd.wpc.graphql.fetcher.findoc.FinDocBatchCreateFetcher;
import com.dummy.wmd.wpc.graphql.fetcher.findoc.FinDocBatchUpdateFetcher;
import com.dummy.wmd.wpc.graphql.fetcher.product.*;
import com.dummy.wmd.wpc.graphql.fetcher.referencedata.*;
import com.dummy.wmd.wpc.graphql.fetcher.sysparam.SysParamBatchUpdateFetcher;
import com.dummy.wmd.wpc.graphql.fetcher.upload.*;
import com.dummy.wmd.wpc.graphql.scalar.datetime.LocalDateTimeScalar;
import com.dummy.wmd.wpc.graphql.scalar.datetime.LocalTimeScalar;
import com.dummy.wmd.wpc.graphql.scalar.datetime.productDateScalar;
import com.dummy.wmd.wpc.graphql.scalar.datetime.productDateTimeScalar;
import com.dummy.wmd.wpc.graphql.service.MetadataService;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import graphql.GraphQL;
import graphql.Scalars;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.AsyncSerialExecutionStrategy;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;
@SuppressWarnings({"java:S1874"})
@Component
public class GraphQLProvider {
    @Autowired
    private MongoDatabase mongoDatabase;
    @Autowired
    ProductMetadataHelper productMetadata;
    @Autowired
    private GraphQLDataFetchers graphQLDataFetchers;
    @Autowired
    private productInstrumentation productInstrumentation;
    @Autowired
    private ReportListFetcher reportListFetcher;
    @Autowired
    private GroupByProductStatusFetcher groupByProductStatusFetcher;
    @Autowired
    private GroupByProductTypeFetcher groupByProductTypeFetcher;
    @Autowired
    private UploadLogFileQueryFetcher uploadLogFileQueryFetcher;
    @Autowired
    private UploadResultQueryFetcher uploadResultQueryFetcher;
    @Autowired
    private SummaryFetcher summaryFetcher;
    @Autowired
    private DiffFromLatestFetcher diffFromLatestFetcher;
    @Autowired
    private DiffFromAmendmentFetcher diffFromAmendmentFetcher;
    @Autowired
    private AmendmentDocLatestFetcher amendmentDocLatestFetcher;
    @Autowired
    private ProductByFilterQueryFetcher productByFilterQueryFetcher;
    @Autowired
    private PbProductByFilterQueryFetcher pbProductByFilterQueryFetcher;
    @Autowired
    private DocumentCountByFilterQueryFetcher documentCountByFilterQueryFetcher;
    @Autowired
    private ValidateProductFetcher validateProductFetcher;
    @Autowired
    private FieldSelectionQueryFetcher fieldSelectionQueryFetcher;
    @Autowired
    private DocumentRevisionPatchFetcher documentRevisionPatchFetcher;
    @Autowired
    private DocumentRevisionDiffFetcher documentRevisionDiffFetcher;
    @Autowired
    private UserInfoFetcher userInfoFetcher;
    @Autowired
    private SupportEntitiesFetcher supportEntitiesFetcher;
    @Autowired
    private FileIngressStatusQueryFetcher fileIngressStatusQueryFetcher;

    @Autowired
    private FileIngressRecordQueryFetcher fileIngressRecordQueryFetcher;
    @Autowired
    private ProductBatchCreateFetcher productBatchCreateFetcher;
    @Autowired
    private ProductBatchUpdateFetcher productBatchUpdateFetcher;
    @Autowired
    private ProductBatchUpdateByIdFetcher productBatchUpdateByIdFetcher;
    @Autowired
    private AmendmentCreateFetcher amendmentCreateFetcher;
    @Autowired
    private AmendmentUpdateFetcher amendmentUpdateFetcher;
    @Autowired
    private AmendmentDeleteFetcher amendmentDeleteFetcher;
    @Autowired
    private AmendmentRequestApprovalFetcher amendmentRequestApprovalFetcher;
    @Autowired
    private AmendmentApproveFetcher amendmentApproveFetcher;
    @Autowired
    private AmendmentByFilterQueryFetcher amendmentByFilterQueryFetcher;
    @Autowired
    private UploadRequestApprovalFetcher uploadRequestApprovalFetcher;
    @Autowired
    private FindocUploadFetcher findocUploadFetcher;
    @Autowired
    private UploadApproveFetcher uploadApproveFetcher;
    @Autowired
    private ReferenceDataBatchCreateFetcher referenceDataBatchCreateFetcher;
    @Autowired
    private ReferenceDataBatchUpdateFetcher referenceDataBatchUpdateFetcher;
    @Autowired
    private ReferenceDataBatchImportFetcher referenceDataBatchImportFetcher;
     @Autowired
    private ReferenceDataBatchDeleteFetcher referenceDataBatchDeleteFetcher;

    @Autowired
    private SysParamBatchUpdateFetcher sysParamBatchUpdateFetcher;
    @Autowired
    private AssetVolatilityClassCharUpdateFetcher assetVolatilityClassCharUpdateFetcher;
    @Autowired
    private AssetVolatilityClassCorlUpdateFetcher assetVolatilityClassCorlUpdateFetcher;
    @Autowired
    private StafLicCheckImportFetcher stafLicCheckImportFetcher;
    @Autowired
    private ProductPriceHistoryQueryFetcher productPriceHistoryQueryFetcher;
    @Autowired
    private ReferenceDataParentQueryFetcher referenceDataParentQueryFetcher;
    @Autowired
    private ReferenceDataChildrenQueryFetcher referenceDataChildrenQueryFetcher;
    @Autowired
    private EsgDataByProdIdListFetcher esgDataByProdIdListFetcher;
    @Autowired
    private GraphQLTypeSchemaFetcher graphQLTypeSchemaFetcher;
    @Autowired
    private FinDocBatchCreateFetcher finDocBatchCreateFetcher;
    @Autowired
    private FinDocBatchUpdateFetcher finDocBatchUpdateFetcher;
    @Autowired
    private ProductPriceHistoryBatchImportFetcher productPriceHistoryBatchImportFetcher;
    @Autowired
    private BatchExecutionFetcher batchExecutionFetcher;
    @Autowired
    private BatchExecutionStepFetcher batchExecutionStepFetcher;
    @Autowired
    private BatchExecutionParamsFetcher batchExecutionParamsFetcher;
    @Autowired
    private MetadataService metadataService;
    @Autowired
    private DefaultFieldConfigCreateFetcher defaultFieldConfigCreateFetcher;
    @Autowired
    private DefaultFieldConfigFetcher defaultFieldConfigFetcher;

    public static final GraphQLHolder holder = new GraphQLHolder();

    private static final String PRODUCT_TYPE = "ProductType";
    private static final String REFERENCE_DATA = "ReferenceData";
    private static final String AMENDMENT_TYPE = "AmendmentType";

    public static GraphQLSchema getGraphQLSchema() {
        return holder.graphQLSchema;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    @PostConstruct
    public void init() throws IOException {
        DataFetcherExceptionHandler exceptionHandler = new productDataFetcherExceptionHandler();
        holder.graphQLSchema = buildSchema(new URL[]{
                Resources.getResource("schema/root-schema.graphqls"),
                Resources.getResource("schema/product-metadata-schema.graphqls"),
                Resources.getResource("schema/prod_type_fin_doc.graphqls"),
                Resources.getResource("schema/staff-license-check-schema.graphqls"),
                Resources.getResource("schema/prod-atrib-map-schema.graphqls"),
                Resources.getResource("schema/chanl-comn-cde-schema.graphqls"),
                Resources.getResource("schema/product-mutation-schema.graphqls"),
                Resources.getResource("schema/amendment-schema.graphqls"),
                Resources.getResource("schema/asset-volatility-class-schema.graphqls"),
                Resources.getResource("schema/data-processing-schema.graphqls"),
                Resources.getResource("schema/pb-product-schema.graphqls"),
                Resources.getResource("schema/fin-doc-schema.graphqls"),
                Resources.getResource("schema/fin-doc-upld-schema.graphqls"),
                Resources.getResource("schema/customer-input-schema.graphqls"),
                Resources.getResource("schema/staff-input-schema.graphqls"),
                Resources.getResource("schema/product-relation-schema.graphqls"),
                Resources.getResource("schema/prod-price-history-schema.graphqls"),
                Resources.getResource("schema/batch-job-execution-schema.graphql"),
                Resources.getResource("schema/default-config-schema.graphqls"),
                productMetadata.getProductOutputSchema(),
                productMetadata.getProductInputSchema()});

        holder.graphQL = GraphQL.newGraphQL(holder.graphQLSchema)
                .queryExecutionStrategy(new AsyncExecutionStrategy(exceptionHandler))
                .mutationExecutionStrategy(new AsyncSerialExecutionStrategy(exceptionHandler))
                .instrumentation(productInstrumentation)
                .build();
    }

    private GraphQLSchema buildSchema(URL[] sdlFiles) throws IOException {
        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();

        for (URL url : sdlFiles) {
            String sdl = Resources.toString(url, StandardCharsets.UTF_8);
            // each registry is merged into the main registry
            typeRegistry.merge(schemaParser.parse(sdl));
        }

        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("productMetadata", graphQLDataFetchers.getProductMetaDataFetcher())
                        .dataFetcher("productMetadataByFilter", new ByFilterQueryFetcher(mongoDatabase, CollectionName.metadata))
                        .dataFetcher("productById", graphQLDataFetchers.getProductByIdDataFetcher())
                        .dataFetcher("pbProductById", graphQLDataFetchers.getPbProductByIdDataFetcher())
                        .dataFetcher("productByFilter", productByFilterQueryFetcher)
                        .dataFetcher("pbProductByFilter", pbProductByFilterQueryFetcher)
                        .dataFetcher("productPriceHistoryByFilter", new ByFilterQueryFetcher(mongoDatabase, CollectionName.prod_prc_hist))
                        .dataFetcher("documentCountByFilter", documentCountByFilterQueryFetcher)
                        .dataFetcher("referenceDataByFilter", new ByFilterQueryFetcher(mongoDatabase, CollectionName.reference_data))
                        .dataFetcher("prodTypeFinDocByFilter", new ByFilterQueryFetcher(mongoDatabase, CollectionName.prod_type_fin_doc))
                        .dataFetcher("finDocByFilter", new ByFilterQueryFetcher(mongoDatabase, CollectionName.fin_doc))
                        .dataFetcher("finDocUpldByFilter", new ByFilterQueryFetcher(mongoDatabase, CollectionName.fin_doc_upld))
                        .dataFetcher("staffLicenseCheckByFilter", new ByFilterQueryFetcher(mongoDatabase, CollectionName.staff_license_check))
                        .dataFetcher("prodAtribMapByFilter", new ByFilterQueryFetcher(mongoDatabase, CollectionName.prod_atrib_map))
                        .dataFetcher("chanlComnCdeByFilter", new ByFilterQueryFetcher(mongoDatabase, CollectionName.chanl_comn_cde))
                        .dataFetcher("assetVolatilityClassCharByFilter", new ByFilterQueryFetcher(mongoDatabase, CollectionName.aset_voltl_class_char))
                        .dataFetcher("assetVolatilityClassCorlByFilter", new ByFilterQueryFetcher(mongoDatabase, CollectionName.aset_voltl_class_corl))
                        .dataFetcher("productValidate", validateProductFetcher)
                        .dataFetcher("allFieldsOf", fieldSelectionQueryFetcher)
                        .dataFetcher("configurationByFilter", new ByFilterQueryFetcher(mongoDatabase, CollectionName.configuration))
                        .dataFetcher("amendmentByFilter", amendmentByFilterQueryFetcher)
                        .dataFetcher("documentRevisionPatch", documentRevisionPatchFetcher)
                        .dataFetcher("documentRevisionDiff", documentRevisionDiffFetcher)
                        .dataFetcher("supportEntities", supportEntitiesFetcher)
                        .dataFetcher("userInfo", userInfoFetcher)
                        .dataFetcher("uploadByFilter", new ByFilterQueryFetcher(mongoDatabase, CollectionName.file_upload))
                        .dataFetcher("requestLogByFilter", new ByFilterQueryFetcher(mongoDatabase, CollectionName.request_log))
                        .dataFetcher("reportList", reportListFetcher)
                        .dataFetcher("dpFileIngressStatus", fileIngressStatusQueryFetcher)
                        .dataFetcher("dpDataIngressStatus", fileIngressRecordQueryFetcher)
                        .dataFetcher("dashboardData", environment -> new HashMap<>())
                        .dataFetcher("prodTypeChanlAttrByFilter", new ByFilterQueryFetcher(mongoDatabase, CollectionName.prod_type_chanl_attr))
                        .dataFetcher("esgDataByProdIdList", esgDataByProdIdListFetcher)
                        .dataFetcher("graphQLTypeSchema", graphQLTypeSchemaFetcher)
                        .dataFetcher("sysParmByFilter", new ByFilterQueryFetcher(mongoDatabase, CollectionName.sys_parm))
                        .dataFetcher("batchJobStatusByFilter", batchExecutionFetcher)
                        .dataFetcher("executionStepById", batchExecutionStepFetcher)
                        .dataFetcher("executionParamsById", batchExecutionParamsFetcher)
                        .dataFetcher("defaultFieldConfig", defaultFieldConfigFetcher)
                )
                .type("Mutation", typeWiring -> typeWiring
                        .dataFetcher("productBatchCreate", productBatchCreateFetcher)
                        .dataFetcher("productBatchUpdate", productBatchUpdateFetcher)
                        .dataFetcher("productBatchUpdateById", productBatchUpdateByIdFetcher)
                        .dataFetcher("amendmentCreate", amendmentCreateFetcher)
                        .dataFetcher("amendmentUpdate", amendmentUpdateFetcher)
                        .dataFetcher("amendmentDelete", amendmentDeleteFetcher)
                        .dataFetcher("amendmentRequestApproval", amendmentRequestApprovalFetcher)
                        .dataFetcher("amendmentApprove", amendmentApproveFetcher)
                        .dataFetcher("uploadRequestApproval", uploadRequestApprovalFetcher)
                        .dataFetcher("uploadFindocRequest", findocUploadFetcher)
                        .dataFetcher("uploadApprove", uploadApproveFetcher)
                        .dataFetcher("referenceDataBatchCreate", referenceDataBatchCreateFetcher)
                        .dataFetcher("referenceDataBatchUpdate", referenceDataBatchUpdateFetcher)
                        .dataFetcher("referenceDataBatchImport", referenceDataBatchImportFetcher)
                        .dataFetcher("referenceDataBatchDelete", referenceDataBatchDeleteFetcher)
                        .dataFetcher("sysParamBatchUpdate", sysParamBatchUpdateFetcher)
                        .dataFetcher("assetVolatilityClassCharUpdate", assetVolatilityClassCharUpdateFetcher)
                        .dataFetcher("assetVolatilityClassCorlUpdate", assetVolatilityClassCorlUpdateFetcher)
                        .dataFetcher("staffLicenseCheckImport", stafLicCheckImportFetcher)
                        .dataFetcher("finDocBatchCreate", finDocBatchCreateFetcher)
                        .dataFetcher("finDocBatchUpdate", finDocBatchUpdateFetcher)
                        .dataFetcher("productPriceHistoryBatchImport", productPriceHistoryBatchImportFetcher)
                        .dataFetcher("defaultFieldConfigCreate", defaultFieldConfigCreateFetcher)
                )
                .type(newTypeWiring("ProdRelnType").dataFetcher(Field.prodRel, graphQLDataFetchers.getProdRelnFetcher()))
                .type(newTypeWiring("EqtyLinkInvstUndlStockType").dataFetcher(Field.prodUndlInstm, dataFetchingEnvironment -> dataFetchingEnvironment.getDataLoader("prodUndlInstm").load(dataFetchingEnvironment)))
                .type(newTypeWiring(PRODUCT_TYPE).dataFetcher(Field.priceHistory, productPriceHistoryQueryFetcher))
                .type(newTypeWiring(PRODUCT_TYPE).dataFetcher(Field.amendments, new RelatedAmendmentsQueryFetcher(mongoDatabase, DocType.product)))
                .type(newTypeWiring(REFERENCE_DATA).dataFetcher(Field.parent, referenceDataParentQueryFetcher))
                .type(newTypeWiring(REFERENCE_DATA).dataFetcher(Field.children, referenceDataChildrenQueryFetcher))
                .type(newTypeWiring(REFERENCE_DATA).dataFetcher("isInUseByProduct", env -> env.getDataLoader("isInUseByProduct").load(env)))
                .type(newTypeWiring(REFERENCE_DATA).dataFetcher(Field.amendments, new RelatedAmendmentsQueryFetcher(mongoDatabase, DocType.reference_data)))
                .type(newTypeWiring("AssetVolatilityClassChar").dataFetcher(Field.amendments, new RelatedAmendmentsQueryFetcher(mongoDatabase, DocType.aset_voltl_class_char)))
                .type(newTypeWiring("AssetVolatilityClassCorl").dataFetcher(Field.amendments, new RelatedAmendmentsQueryFetcher(mongoDatabase, DocType.aset_voltl_class_corl)))
                .type(newTypeWiring("StaffLicenseCheck").dataFetcher(Field.amendments, new RelatedAmendmentsQueryFetcher(mongoDatabase, DocType.staff_license_check)))
                .type(newTypeWiring(AMENDMENT_TYPE).dataFetcher(Field.docLatest, amendmentDocLatestFetcher))
                .type(newTypeWiring(AMENDMENT_TYPE).dataFetcher(Field.diffFromAmendment, diffFromAmendmentFetcher))
                .type(newTypeWiring(AMENDMENT_TYPE).dataFetcher(Field.diffFromLatest, diffFromLatestFetcher))
                .type(newTypeWiring(AMENDMENT_TYPE).dataFetcher(Field.summary, summaryFetcher))
                .type(newTypeWiring("UploadType").dataFetcher(Field.uploadResult, uploadResultQueryFetcher))
                .type(newTypeWiring("UploadType").dataFetcher(Field.logFile, uploadLogFileQueryFetcher))
                .type(newTypeWiring("DashboardData").dataFetcher(Field.groupByProductType, groupByProductTypeFetcher))
                .type(newTypeWiring("DashboardData").dataFetcher(Field.groupByProductStatus, groupByProductStatusFetcher))
                //.type(newTypeWiring("AmendmentType").dataFetcher("hasConflict", new AmendmentHasConflictFetcher(mongoDatabase)))
                .scalar(new productDateScalar())
                .scalar(Scalars.GraphQLBigDecimal)
                .scalar(new productDateTimeScalar())
                .scalar(LocalTimeScalar.create("LocalTime"))
                .scalar(LocalDateTimeScalar.create("LocalDateTime", false, null))
                .scalar(ExtendedScalars.Json)
                .scalar(ExtendedScalars.GraphQLLong)
                .build();

        setCalculatedFieldFetcher(runtimeWiring);
        return runtimeWiring;
    }

    private void setCalculatedFieldFetcher(RuntimeWiring runtimeWiring) {
        List<Document> calculatedMetadatas = metadataService.getMetadataList(Filters.exists("calculatedBy.rootObjectPath"));

        for (Document metadata : calculatedMetadatas) {
            String parent = metadata.getString("parent");

            String typeName;
            if ("[ROOT]" .equals(parent)) {
                typeName = PRODUCT_TYPE;
            } else {
                List<Document> result = metadataService.getMetadataList(Filters.eq("jsonPath", parent));
                String graphQLType = result.get(0).getString("graphQLType");
                typeName = graphQLType.replace("[", "").replace("]", "");
            }

            if (StringUtils.equalsAny(typeName, "ProdRelnType", "EqtyLinkInvstUndlStockType")) {
                continue;
            }

            runtimeWiring.getDataFetcherForType(typeName)
                    .put(metadata.getString("attrName"), graphQLDataFetchers.getCalculatedFieldFetcher(metadata));
        }
    }

    @Bean
    public GraphQL graphQL() {
        return holder.graphQL;
    }

    private static class GraphQLHolder {
        private GraphQL graphQL;
        private GraphQLSchema graphQLSchema;
    }
}
