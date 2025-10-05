# Function: 
Financial Document Upload by Business--only for mapping files.

# tables:
* fin_doc(FIN_DOC): Financial Document table, use to check if related pdf already upload and exist in this table.
* prod_type_fin_doc_type_rel(PROD_TYPE_FIN_DOC_TYPE_REL): The table which store relationship between product type and document Type. Use to check if document Type is valid value.
* product.finDoc(PROD_FIN_DOC): Store relationship of product and findoc document.
* prod_type_fin_doc(PROD_TYPE_FIN_DOC / PROD_SUBTP_FIN_DOC): Store relationship of product and findoc document.
* referenceData: Search related data.

# Path:
* finDoc.ftp = /appvol/product-spring-batch/data/incoming/findoc   --- store path for import mapping files.
* finDoc.doc.src = /appvol/product-spring-batch/data/FinDoc/req/incoming   --- move to this folder when process.
* finDoc.doc.chk = /appvol/product-spring-batch/data/FinDoc/req/chk      --- after document processed, the upload form will be moved to this folder.

# Entrance
* ImportFinDocMappingJobApplication: Spring boot application entrance
* ImportFinDocMappingConfiguration: Spring batch entrance

# Steps:
* Step1: FinDocMappingUploadJoblistener ---Scan mapping files from finDoc.ftp and move to finDoc.doc.src, and after document processed, will move  upload form to finDoc.doc.chk. Generate error message file.
* Step2: importFinDocMappingWithExcelStep ---Process mapping files.  
        - MultiResourceItemReader for reading multiple excel files, and it delegate PoiItemReader for reading single excel file, save every rows from excel into Java Object FinDocMapInput  
        - FinDocFilesProcessor for processing FinDocMapInput which are resolved by MultiResourceItemReader, main work is validating FinDocMapInput and return valid FinDocMapInput list.  
        - FinDocFilesWriter for saving valid FinDocMapInput list to MongoDB.  


