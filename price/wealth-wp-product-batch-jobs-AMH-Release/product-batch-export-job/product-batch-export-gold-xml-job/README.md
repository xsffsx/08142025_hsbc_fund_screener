# product-batch-export-gold-xml-job

This is an XML export job to generate Digital Asset Currency (DAC) product data in XML

To run the job in local: 

`com.dummy.wpb.product.ExportGoldXmlJobApplication HK HBAP GSOPSD`

Document: https://wpb-confluence.systems.uk.dummy/display/WWS/product-batch-export-gold-xml-job

### 1) Generate outgoing XML file
#### 1-a) Run with default product type (A,P,S) and default file type (FULLSET)
> /appvol/product-spring-batch/bin/HKHBAP/cron/wpc_gen_xml_header_dac.sh HK HBAP GSOPSD

Criteria: prodStatCde prodStatCde in ('A', 'S', 'P')

#### 1-b) Run with product type = A and file type = DELTA
> /appvol/product-spring-batch/bin/HKHBAP/cron/wpc_gen_xml_header_dac.sh HK HBAP GSOPSD 'A' DELTA

Criteria: prodStatCde in ('A') & recUpdtDtTm >= last_success_time

#### Outgoing file path:
> /appvol/product-spring-batch/data/outgoing/HKHBAP/GSOPSD

### 2) Transfer XML file to target system via SFTP
> /appvol/product-spring-batch/bin/HKHBAP/cron/wpc_xml_multi_transfer_GSOPS.sh HK HBAP GSOPSD
