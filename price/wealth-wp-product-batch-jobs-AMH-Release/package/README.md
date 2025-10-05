# How to build the project

## Overview

https://alm-github.systems.uk.dummy/RBWM-Wealth-Engineering-IT/wealth-wp-product-batch-jobs/blob/master/package/package-assembly/pom.xml

##### Step 1: Build product-batch-parent

##### Step 2: Copy *.jar packages in step 1 to /package/output/lib

##### Step 3: Copy /product-batch-script/ files to /package/output/cron

/product-btach-script/common/ - Common *.sh scripts for all environment

/product-btach-script/HKHBAP/param/ - Batch job parameters for HKHBAP market

/product-btach-script/HKHBAP/SIT/ - Specific properties for HKHBAP SIT environment

##### Step 4: Zip /package/output/ folder into *.tgz

## How To

### 1. How to build in local workspace

Sample: Build a *.tgz package for HKHBAP SIT environment

`cd C:\workspace\wealth-wp-product-batch-jobs\package\package-assembly
`

`mvn clean package -Dentity=HKHBAP -Denv=SIT
`
### 2. How to build & deploy on cloud server

Sample: Deployment for HKHBAP SIT environment

##### Build

https://jenkins-custom-glwm01.digital-tools.euw1.prod.aws.cloud.dummy/job/WEALTH-PLATFORM/job/Common%20Service/job/WPC/job/AWS-AMH-DEV/job/build-wealth-wp-product-batch-jobs(amh-sit)/

Jenkins pipeline builds the project into a *.tgz package and uploads on repository

##### Deploy

https://jenkins-custom-glwm01.digital-tools.euw1.prod.aws.cloud.dummy/job/WP-HK-HBAP/job/AWS-SIT/job/SMART/job/deploy-wealth-wp-product-batch-jobs(amh-sit)/

Jenkins pipeline downloads the *.tgz package from repository, and depress on AMH EC2 batch server

Path = `/appvol/product-spring-batch/bin/HKHBAP`

### 3. How to control jobs in the package

Since each job is a self-contain spring boot jar which can run alone, the size is relatively large. Each country may have a different set of jobs, to reduce the deploy package size, you can simply exclude them from the pom modules.

For export jobs

```text
product-batch-export-job/pom.xml
```

For import jobs

```text
product-batch-import-job/pom.xml
```

### 4. How to add a new entity

Add script folder like `product-batch-script/HKHBAP`.

Add config folder like `product-batch-config/HKHBAP`.

