# Function: 
ELI Financial document upload.

# tables:
* product(TB_PROD): Store product information.
* product.finDoc(PROD_FIN_DOC): Store relationship of product and findoc document.

# Entrance
* ImportEliFinDocJobApplication: Spring boot application entrance
* ImportEliFinDocConfiguration: Spring batch entrance

# Steps:
* Step1: importEliFinDocStep ---Include ImportEliFinDocTasklet which define all process:      
       - According to parameter, enter Eli Findoc post product process(run every 15 mins from 8:00 to 20:00), or Eli Findoc Monitor(monitor every 3 seconds from 8:00 to 20:00).  
       - Check ack files. For each ack file, process one by one, if related pdf file exist, start to handle.   
       - Get prodAltPrimNum from pdf file name, search Eli product from DB.   
       - Merge Pdf according to prodSubtpCde of Eli product.   
       - Call the wpc_eli_findoc_transfer.sh to send pdf to document server.      
       - Example:     /appvol/product-spring-batch/bin/HKHBAP/cron/wpc_eli_findoc_transfer.sh HK HBAP ELI DFBAS2100001_FINAL_TERMSHEETS_PFS.pdf    
       - Update finDoc, prodStatCde, recUpdtDtTm for table product.    
       

