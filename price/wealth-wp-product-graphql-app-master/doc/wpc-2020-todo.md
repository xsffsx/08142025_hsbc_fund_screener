# WPC 2020 TODO

## Pending issues

- JSON Schema validation can't handle object array unique keys (but this can be achieved with MongoDB multi fields index)

## MongoDB

- Define the MongoDB data model, include data other than produc data.
- JSON Schema for all data validation
  - Product metadata
  - Product data
  - Reference data
  - Configuration
  - ASET_VOLTL_CLASS_CHAR
  - ASET_VOLTL_CLASS_CORL
  - PROD_TYPE_STAF_LIC_CHECK
  - PROD_SUBTP_STAF_LIC_CHECK
  - PROD_TYPE_FIN_DOC_TYPE_REL
  - PROD_TYPE_FIN_DOC
  - PROD_SUBTP_FIN_DOC
- A solution of how to keep product data history, see change stream (watch)

## Data migration

### Tools

Write a command line program, which fetch data from Oracle DB and put to target MongoDB, including product metadata, product data, reference data an all other data, through the GraphQL services.

### Process

Define data migration process

## WPC GraphQL Service

### Query

- productsByFilter(Done): A generic query interface, supports sorting and pagination
- productsByNamedQuery: Named query interface, with pre-defined query template and input variables, supports sorting and pagination

### Mutation

- createProducts: Create product service
- updateProducts: Update products service
  - validation on update
  - keep product history on update
- validateProducts: Validate products data service
- reportService: What's the report requirements, can we provide a generic service interface for that

## WPC MQ Service Adapter

What's the message needed to be implement in MQ interface?

## WPC REST Service Adapter

- customerEligiblity

## WPC SOAP Service Adapter

- pw0 - DataEnquiry: Pending
  > com.dummy.wmd.wpc.webservice.service.impl.WpcDataEnquiryWebServiceImplDelegate
- pw1 - ProductKeyListWithEligibilityCheckImplService: Almost done
  > com.dummy.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.ProductKeyListWithEligibilityCheckImplDelegate
- pw2 - ProductDetailByKeysEnquiryService: Almost done
  > com.dummy.wmd.wpc.webservice.productdetailbykeys.service.impl.WpcProductDetailByKeysEnquiryServiceImplDelegate
- pw3 - ReferenceDataEnquiryService: Pending
  > com.dummy.wmd.wpc.webservice.refdataenq.service.impl.WpcReferenceDataEnquiryServiceImplDelegate
- pw5 - ProductCustomerEligibilityEnquiry: Pending
- pw6 - ProductStaffEligibilityEnquiry: Pending
  > com.dummy.wmd.wpc.webservice.prodstaffeligenq.service.impl.WpcProductStaffEligibilityEnquiryDelegate
- pw7 - PriceHistoryEnquiry: Pending
  > com.dummy.wmd.wpc.webservice.pricehistoryenq.service.impl.WpcPriceHistoryEnquiryImplDelegate
- pw8 - WpcProductFinancialDocumentEnquiryService: Pending
  > com.dummy.wmd.wpc.webservice.productfinancialdocumentenq.service.impl.WpcProductFinancialDocumentEnquiryServiceImplDelegate
- pw18 - AssetVolatilityClassEnquiryService: Pending
- pw19 - AssetVolatilityClassCorrelationEnquiryService
- pwx - ProductMaintService
  > com.dummy.wmd.wpc.webservice.prodmainten.service.impl.WpcProductMaintenanceServiceDelegate
- pwy - ProductCountService
  > com.dummy.wmd.wpc.webservice.prodcount.service.impl.WpcProductCountWebServiceImplDelegate
- pwz - WpcProdKeyMapService
  > com.dummy.wmd.wpc.webservice.prodkeymap.service.impl.WpcProdKeyMapServiceImplDelegate

## WPC Batch

- More study needed in the current batch jobs
- List all possible upstreams / downstreams and their requirements
- Categorized upstreams / downstreams as input / output, see if can provide a more generic interface
- Customizable data upload, user can select the fields to update, a template can be generated with the filtered product keys / product ids.

Existing batch jobs, total 61, see `wpc2020\batch\batch job list.xlsx`

- com.dummy.hbap.wpc.batch.main.GFIXListenerBroker
- com.dummy.hbap.wpc.batch.main.GFIXSingleRequesterBroker
- com.dummy.hbap.wpc.batch.main.PerformanceIdService
- com.dummy.hbap.wpc.batch.main.WPCBatchBondInstmXMLExportService
- com.dummy.hbap.wpc.batch.main.WPCBatchBondNewlyIntroIndService
- com.dummy.hbap.wpc.batch.main.WPCBatchDataSyncImportService
- com.dummy.hbap.wpc.batch.main.WPCBatchELIFinDocService
- com.dummy.hbap.wpc.batch.main.WPCBatchImportService
- com.dummy.hbap.wpc.batch.main.WPCBatchInvestorCharacterIndService
- com.dummy.hbap.wpc.batch.main.WpcBatchLocalProductCreationService
- com.dummy.hbap.wpc.batch.main.WpcBatchLocalReferDataInheritanceService
- com.dummy.hbap.wpc.batch.main.WPCBatchProdFinDocHashHexCalService
- com.dummy.hbap.wpc.batch.main.WPCBatchProductPriceHistoryMaintainService
- com.dummy.hbap.wpc.batch.main.WPCBatchReutersBondImportService
- com.dummy.hbap.wpc.batch.main.WPCBatchSIDFinDocService
- com.dummy.hbap.wpc.batch.main.WPCBatchTimeSeriesExportService
- com.dummy.hbap.wpc.batch.main.WpcBatchTrDssService
- com.dummy.hbap.wpc.batch.main.WPCBatchXMLExportService
- com.dummy.hbap.wpc.batch.main.WPCBatchXMLGroupedMultiExportService
- com.dummy.hbap.wpc.batch.main.WPCBatchXMLMultiExportService
- com.dummy.hbap.wpc.batch.main.WPCBatchYieldPrcHisExportService
- com.dummy.hbap.wpc.batch.main.WPCBondHighestYieldIndService
- com.dummy.hbap.wpc.batch.main.WPCBondTopPerformanceService
- com.dummy.hbap.wpc.batch.main.WPCCognosDBPurgeService
- com.dummy.hbap.wpc.batch.main.WPCDBHouseKeepService
- com.dummy.hbap.wpc.batch.main.WPCDBProcedureTriggerService
- com.dummy.hbap.wpc.batch.main.WPCDuplicateReportGenService
- com.dummy.hbap.wpc.batch.main.WpcLocalVolatilityCharacteristicInheritService
- com.dummy.hbap.wpc.batch.main.WpcLocalVolatilityCorrelationInheritService
- com.dummy.hbap.wpc.batch.main.WPCMaterializedViewRefreshService
- com.dummy.hbap.wpc.batch.main.WPCMissingPriceReportGenService
- com.dummy.hbap.wpc.batch.main.WPCMissingPriceUTReportGenService
- com.dummy.hbap.wpc.batch.main.WPCMorningStarRegisterService
- com.dummy.hbap.wpc.batch.main.WPCOptionsCreateService
- com.dummy.hbap.wpc.batch.main.WPCOptionsDBPurgeService
- com.dummy.hbap.wpc.batch.main.WPCOptionsDBRetentionPeriodService
- com.dummy.hbap.wpc.batch.main.WPCPendingProdReportGenService
- com.dummy.hbap.wpc.batch.main.WPCPerformanceCalculationService
- com.dummy.hbap.wpc.batch.main.WPCPriceExceptionControlReportGenService
- com.dummy.hbap.wpc.batch.main.WPCPriceInvestigationReportGenService
- com.dummy.hbap.wpc.batch.main.WPCPriceReportDataCleanService
- com.dummy.hbap.wpc.batch.main.WPCPriceStaleReportGenService
- com.dummy.hbap.wpc.batch.main.WPCPriceUploadGenRecordService
- com.dummy.hbap.wpc.batch.main.WPCPriceUploadProcessRecordService
- com.dummy.hbap.wpc.batch.main.WPCProductFileGenService
- com.dummy.hbap.wpc.batch.main.WPCProductFileGenToTreemapService
- com.dummy.hbap.wpc.batch.main.WPCProductUploadGenRecordService
- com.dummy.hbap.wpc.batch.main.WPCProductUploadProcessRecordService
- com.dummy.hbap.wpc.batch.main.WPCReportGenerationService
- com.dummy.hbap.wpc.batch.main.WPCReutersFieldsAnalystReport
- com.dummy.hbap.wpc.batch.main.WPCSalesAppSyncJobForProService
- com.dummy.hbap.wpc.batch.main.WPCSPOMSProductMaintenanceService
- com.dummy.hbap.wpc.batch.main.WPCStatReportService
- com.dummy.hbap.wpc.batch.main.WPCTenorInDaysCalculationService
- com.dummy.hbap.wpc.batch.main.XSLTUtil
- com.dummy.wpc.findocbatch.findoc.FinDocBatchMappingService
- com.dummy.wpc.findocbatch.findoc.FinDocBatchUploadService
- com.dummy.wpc.findocbatch.findoc.script.DocRelService
- com.dummy.wpc.findocbatch.findoc.script.HandlePWSAckService
- com.dummy.wpc.batch.filter.WPCBatchXMLFilter
- com.dummy.wpc.datadaptor.EntryPoint

## WPS

- Think of how to construct the product viewer and editor interfaces, to improve user experience.
- Reference WPS document for all the interfaces need to be implemented (see `\\Hbap.adroot.dummy\hk\IT Operations\hk_nstp_mig\WMDDATA\DOC\CAD\RIS\HFI\WPC\Production Support\1st Line Support\WPS Screen Layout v2.4.4[Up to Wealth v2+ Sprint 17].doc`)

### Existing WPS Function List

- Enquiry:
  - Enquire Customer Eligibility
  - Enquire Reference Data
  - Enquire Product Detail
  - Enquire Staff Eligibility
  - Enquire Product Allowable Channel
  - Enquire Reference Data Allowable Channel
  - Enquire System Parameter
  - Enquire Activity Log
  - Enquire System Report
  - Enquire Asset Volatility Class
  - Enquire Asset Volatility Class Correlation
  - Enquire Product Group
  - Enquire Product Relation
- Approve:
  - Approve Customer Eligibility
  - Approve Reference Data
  - Approve Product Detail
  - Approve Staff Eligibility
  - Approve Product Allowable Channel
  - Approve Reference Data Allowable Channel
  - Approve System Parameter
  - Approve Product / Price Upload
  - Approve Asset Volatility Class
  - Approve Asset Volatility Class Correlation
  - Approve Product Group
  - Approve Product Relation
  - Approve Excel Upload Form
  - Excel Form Upload
- Maintain:
  - Maintain Customer Eligibility
  - Maintain Reference Data
  - Maintain Product Detail
  - Maintain Staff Eligibility
  - Maintain Product Allowable Channel
  - Maintain Reference Data Allowable Channel
  - Maintain System Parameter
  - Maintain Asset Volatility Class
  - Maintain Asset Volatility Class Correlation
  - Maintain Product Group
  - Maintain Product Relation
  - Maintain Excel Upload Form

### Other interfaces

- Product metadata viewer (and editor?)
- Configuration viewer (and editor?)