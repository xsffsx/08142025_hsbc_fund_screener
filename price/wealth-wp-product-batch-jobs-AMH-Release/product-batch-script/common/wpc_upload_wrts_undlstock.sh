#!/usr/bin/ksh

SCRIPT_DIR=$(dirname $0)

if [[ $# -lt 2 ]]
then
    echo "Usage: wpc_wrts_undlstock.sh [COUNTRY_RECORD_CODE] [GROUP_MEMBER_RECORD_CODE]"
    exit
fi

source ${SCRIPT_DIR}/WPCEnvSetting.sh

# Input Parameters
typeset -u ctryRecCde=$1
typeset -u grpMembrRecCde=$2
typeset uploadPath="${BATCH_INCOMING_PATH}/${ctryRecCde}${grpMembrRecCde}"

JOB_NAME="Import MIDFS WRTS Underlying Stocks Job"

DATE=`date +%Y%m%d`
LOG_FILE="product_import_wrts_undlstock_${ctryRecCde}${grpMembrRecCde}_${DATE}.log"
LOG_CLOUD_PATH=${LOG_FILE_CLOUD_PATH}
LOG_CLOUD_FILE=${LOG_FILE}

echo "${JOB_NAME} input parameters: [ctryRecCde=${ctryRecCde}, grpMembrRecCde=${grpMembrRecCde}]"
echo "${JOB_NAME} log file: ${LOG_FILE_PATH}/${LOG_FILE}"
echo "${JOB_NAME} cloud log file: ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}"

echo "=========================================================" | tee -a ${LOG_FILE_PATH}/${LOG_FILE} | tee -a ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "Hostname: $(hostname)" | tee -a ${LOG_FILE_PATH}/${LOG_FILE} | tee -a ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "$(date +"[%H:%M:%S %d/%m/%Y]") ${JOB_NAME} is starting ..." | tee -a ${LOG_FILE_PATH}/${LOG_FILE} | tee -a ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml,${SPRING_BATCH_CONFIG_PATH}/product-batch-import-wrts-undlstock-job.yml"

${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-batch-import-wrts-undlstock-job-2.0.0-SNAPSHOT.jar ctryRecCde=${ctryRecCde} grpMembrRecCde=${grpMembrRecCde}  incomingPath=${uploadPath} --spring.config.location=${springConfig}  2>&1 | tee -a ${LOG_FILE_PATH}/${LOG_FILE} | tee -a ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}

echo "$(date +"[%H:%M:%S %d/%m/%Y]") ${JOB_NAME} is completed." | tee -a ${LOG_FILE_PATH}/${LOG_FILE} | tee -a ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
