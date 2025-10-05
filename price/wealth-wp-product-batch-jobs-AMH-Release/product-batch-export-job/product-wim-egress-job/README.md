# WIM CSV File Egress job
## Job Description
This Job is for the csv file exported by the WIM system, including five files, and the file name corresponds to the oracle table name

## ProductStep

### Reader
Call graphql to query the products whose condition equals: [product-criteria](src/main/resources/product-criteria.json)
### Writer
Read export-table in configuration:[export-table](src/main/resources/application.yaml).

Restore the information corresponding to the oracel table from the product data and write it into the csv file
## ReferenceStep

### Reader
Call graphql to query the products whose condition equals: [referenceData-criteria](src/main/resources/referenceData-criteria.json)
### Writer
Read export-table in configuration:[export-table](src/main/resources/application.yaml).

Restore the information corresponding to the oracel table from the reference data and write it into the csv file.

## How to run:
### Run in shell script:
sh [wpc_wim_egress_job.sh](https://alm-github.systems.uk.dummy/RBWM-Wealth-Engineering-IT/wealth-wp-product-batch-jobs/blob/master/product-batch-script/HKHBAP/SIT/wpc_wim_egress_job.sh)
/appvol/product-spring-batch/data/outgoing/HKHBAP/WIM/(optional file output path)
### Run in jar/main class:
java -jar product-wim-egress-job-2.0.0-SNAPSHOT.jar outputPath=C:\dev\workspace-product\outgoing\HKHBAP\WIM
