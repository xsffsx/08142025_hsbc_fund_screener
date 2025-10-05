# Function: 
Financial document release.

# tables:
* fin_doc_upld: FinDoc Upload Request table,all information of findoc upload will be inserted into this table, no matter  the upload is successful or not.
* fin_doc_hist: FinDoc summary table,the approved document information or document which is  uploaded  successfully and no need to be approved will be inserted into this table.

# Path:
* finDoc.pdf.rej = /appvol/product-spring-batch/data/FinDoc/pdf/rej   ---the rejected documents will be moved to this folder
* finDoc.pdf.aprv = /appvol/product-spring-batch/data/FinDoc/pdf/aprv ---The approved documents will be moved to this folder after release
* finDoc.pdf.chk = /appvol/product-spring-batch/data/FinDoc/pdf/chk   ---the documents processed will be moved to this folder to wait for the approval;and the documents which are approved but not release will stay in this folder as well.
* finDoc.sysPath = /appvol/product-spring-batch/data/FinDoc/pdf  ---pdf upload root path
* finDoc.fsDataDir = /hk_HFI_DATA  ---File Server Data Direcory
* finDoc.maxNo.PWS = 70  ---File Server max upload pdf number
* finDoc.output.path = /appvol/product-spring-batch/data/outgoing/  ---pdf list file "HK_HBAP_aprv_FinDocFtp.txt" outgoing path

# Entrance
* FinDocReleaseJobApplication: Spring boot application entrance
* FinDocReleaseConfiguration: Spring batch entrance

# Steps:
* Step1: finDocReleaseStep ---Include FinDocReleaseTasklet which define all process:      
       - Move pdf files from finDoc.doc.chk to finDoc.pdf.rej or finDoc.pdf.aprv.    
       - Update docStatCde, docServrStatCde, urlLclSysText, recUpdtDtTm, docRleasStartDtTm for tables fin_doc_upld and fin_doc_hist.     
       - Call the wpc_transfer_pws.sh to FTP the corresponding PDF files from local to File Server.    
       - Example:     C:/sandbox/workspace/Smart/wealth-wp-product-batch-jobs/product-batch-script/common/wpc_transfer_pws.sh HK HBAP C:/dev/smart/findoc/pdf\aprv /hk_HFI_DATA C:/dev/smart/outgoing/HKHBAP\HK_HBAP_aprv_FinDocFtp.txt     


