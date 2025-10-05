# product-batch-script

### Introduction

This project stores shell scripts for product batch jobs.

#### 1. /product-btach-script/common/

Common *.sh scripts for all environment

#### 2. /product-btach-script/HKHBAP/param/

Batch job parameters shared by HKHBAP market (for SIT, UAT & PROD env)

#### 3. /product-btach-script/HKHBAP/SIT/

Specific properties for HKHBAP SIT environment (e.g. WPCEnvSetting.sh, which contains HTTP & SFTP profiles)

### How to build in Maven (take HKHBAP SIT for example)

    cd wealth-wp-product-batch-jobs/package/package-assembly
    mvn clean install -Dentity=HKHBAP -Denv=SIT"

https://alm-github.systems.example.com/RBWM-Wealth-Engineering-IT/wealth-wp-product-batch-jobs/blob/master/package/copy-batch-resources/pom.xml

    <!-- Copy batch scripts and param files -->
    <echo message="Copy batch scripts and param files for ${entity} ${env}" />
    <copy todir="${target.batch.cron}" overwrite="true">
        <fileset dir="${root}/product-batch-script/common/" />
        <fileset dir="${root}/product-batch-script/${entity}/param" />
        <fileset dir="${root}/product-batch-script/${entity}/${env}" />
    </copy>

- Step 1: copy all files under /product-btach-script/common/

- Step 2: copy all files under /product-btach-script/HKHBAP/param/ 

  (will override the files in step 1 if there is duplicate file name)

- Step 3: copy all files under /product-btach-script/HKHBAP/SIT

  (will override the files in step 1 & step 2 if there is duplicate file name)

- Step 4: Zip the above files



