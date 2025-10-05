# Reference page: 
https://wpb-confluence.systems.uk.dummy/pages/viewpage.action?pageId=2767400144

# tables:
* fin_doc_upld: FinDoc Upload Request table,all information of findoc upload will be inserted into this table, no matter  the upload is successful or not.
* sys_parm(fin_doc_parm): FinDoc system parameter table,defined whether the upload documents need to be approved or not, can be archive or not,  and also defined the FTP Server  Url (FSURL).
* amendment(pend_aprove_tran): Pending approve table, if the uploaded document needs to be approved,  it would be Inserted into this table first after it is uploaded successfully.
* fin_doc_hist: FinDoc summary table,the approved document information or document which is  uploaded  successfully and no need to be approved will be inserted into this table.
* product.finDoc(prod_fin_doc): Store relationship of product and findoc document.
* prod_type_fin_doc(PROD_TYPE_FIN_DOC / PROD_SUBTP_FIN_DOC): Store relationship of product and findoc document.
* referenceData: Search related data.

# Path:
* FinDoc.ftp = /appvol/product-spring-batch/data/incoming/findoc   --- store path for import request file & pdf files
* FinDoc.doc.src = /appvol/product-spring-batch/data/FinDoc/req/incoming   ---if there is duplicate exception for upload form, it will be moved to this folder
* FinDoc.doc.chk = /appvol/product-spring-batch/data/FinDoc/req/chk      ---after document processed, the upload form will be moved to this folder
* Findoc.pdf.path= /appvol/product-spring-batch/data/FinDoc/pdf
* FinDoc.pdf.rej= /appvol/product-spring-batch/data/FinDoc/pdf/rej   ---the rejected documents will be moved to this folder
* FinDoc.pdf.chk= /appvol/product-spring-batch/data/FinDoc/pdf/chk   ---the documents processed will be moved to this folder to wait for the approval;and the documents which are approved but not release will stay in this folder as well.
* FinDoc.pdf.aprv= /appvol/product-spring-batch/data/FinDoc/pdf/aprv ---The approved documents will be moved to this folder after release
* FinDoc.reject.ENS= /appvol/product-spring-batch/data/outgoing/HKHBAP/ENS/  ---store path for ENS file

# Steps:
* Step1: importFinDocFilesStep ---scan files and check if list excel/csv file existed in DB;
* Step2: FileFormatDecider ---determine the file format and define the conditions for the next step;
* Step3: importFinDocFilesWithExcelStep/importFinDocFilesWithCsvStep ---process list excel/csv file, move list file & pdf files
* Step4: finishFinDocFilesStep --- move list excel/csv file to finDoc.doc.chk path, generate error message file


