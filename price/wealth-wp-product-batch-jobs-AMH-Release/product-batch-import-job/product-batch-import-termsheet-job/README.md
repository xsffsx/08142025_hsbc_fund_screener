# product-batch-import-termsheet-job

This is SID term sheet pdf file upload job.

* WPC receives term sheet pdf files from SPOMS
* WPC updates related financial document data in DB
* WPC sends the pdf file to target server (usually via SFTP)

### Command

> /appvol/product-spring-batch/bin/HKHBAP/cron/wpc_spoms_termsheet_maint_product.sh HK HBAP SID Y

### Incoming file path

> /appvol/product-spring-batch/data/incoming/findoc/HKHBAP/SID

### Relevant tables

* fin_doc (db.fin_doc)

* prod_fin_doc (db.product.fin_doc)

### Reference

https://wpb-confluence.systems.uk.dummy/display/WWS/03+%28New%29+SID+Term+Sheet+PDF+File+Job+in+product-spring-batch
