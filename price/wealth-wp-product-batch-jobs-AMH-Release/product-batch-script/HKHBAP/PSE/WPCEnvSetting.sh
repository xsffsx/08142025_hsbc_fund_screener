#!/usr/bin/ksh

#############################################################
###          Environment Setting                          ###
#############################################################
export ENCRYPT_KEY=$ENCRYPT_KEY

JAVA_PATH="/appvol/jre1.8.0_241/bin/java"
JDBC_LIB_PATH="/appvol/wpc/app/lib/ojdbc7-12.1.0.2.jar"
MQ_LIB_PATH="@mq.lib.path@"
SYS_TIMEZONE_DELTA="+8 hours"
GFIX_LISTENER_SERVICE_PERIOD="35280"
GFIX_LISTENER_MONITOR_INTERVAL="120"
GFIX_MASTER_HOST="hkgv3ls0787"


BATCH_URL="https://product-dp-file-egress-preprod-tran-debug.uat.wealth-platform-amh.ape1.dev.aws.cloud.dummy/export/"
BATCH_INGRESS_URL=""
BATCH_INTERNAL_JOBS_URL=""


#############################################################
###          Batch Server Path INFO                       ###
#############################################################
BATCH_APP_PATH="/appvol/product-spring-batch-pse/bin/HKHBAP"
CONFIG_FILE_PATH="${BATCH_APP_PATH}/configFile"
JAR_FILE_PATH="${BATCH_APP_PATH}/lib"
BATCH_SCRIPT_PATH="${BATCH_APP_PATH}/cron"
DB_KEY_FILE_PATH="${BATCH_APP_PATH}/key"
BATCH_APP_LOG_PATH="${BATCH_APP_PATH}/logs"
EXCEL_CONFIG_PATH="${BATCH_APP_PATH}/batch/configFile/xls"
ADAPTOR_SCRIPT_PATH="${BATCH_APP_PATH}/adaptor/cron"
SPRING_BATCH_CONFIG_PATH="${BATCH_APP_PATH}/config"

### Batch Data Path ###
BATCH_DATA_PATH="/appvol/product-spring-batch-pse/data"
LOG_FILE_PATH="${BATCH_DATA_PATH}/processLog"
LOG_FILE_CLOUD_PATH="/var/log/app"
REPORT_FILE_PATH="${BATCH_DATA_PATH}/report"
STAT_REPORT_PATH="${BATCH_DATA_PATH}/stat"
BATCH_LOG_PATH="/var/log/app"

### Output File Path ###
OUTPUT_FILE_PATH="${BATCH_DATA_PATH}/outgoing"
WPC_OUTPUT_FILE_PATH="/appvol/wpc/data/outgoing"
WPC_LOG_FILE_PATH="/appvol/wpc/data/processLog"
REUTERS_OUTPUT_FILE_PATH="${OUTPUT_FILE_PATH}/reuters"


### RBP NAS Server Path ###
RBP_OUTPUT_FILE_PATH="@rbp.output.path@"
### Incoming File Path ###
INCOMING_DATA_PATH="${BATCH_DATA_PATH}/incoming"
BATCH_LANDING_PATH="${BATCH_DATA_PATH}/landing/batch"
BATCH_INCOMING_PATH="${INCOMING_DATA_PATH}/batch"
FINDOC_INCOMING_PATH="${INCOMING_DATA_PATH}/findoc"
#FINDOC_INCOMING_PATH="${BATCH_DATA_PATH}/FinDoc/req/incoming"
DATASYNC_INCOMING_PATH="${INCOMING_DATA_PATH}/datasync"
######### FINDOC COPY TERMSHEET TO S3 ########
FINDOC_CP_S3_PATH=wealth-dev-appcontent/wealth-platform-amh/v1/wpc/data/hk/invest
FINDOC_CP_S3_SCRIPT="--content-type application/pdf --region eu-west-1 --sse --endpoint-url https://bucket.vpce-0e83146b05ad4df52-1o03o9fz.s3.eu-west-1.vpce.amazonaws.com"

### Adaptor Path ###
ADAPTOR_PATH="${BATCH_DATA_PATH}/Adaptor"
ADAPTOR_OUTPUT_FILE_PATH="${ADAPTOR_PATH}/outgoing"
MANUAL_PRC_FILE_PATH="${ADAPTOR_PATH}/priceUpload"
MANUAL_PROD_FILE_PATH="${ADAPTOR_PATH}/priceUpload"
ADAPTOR_INCOMING_FILE_PATH="${ADAPTOR_PATH}/incoming"