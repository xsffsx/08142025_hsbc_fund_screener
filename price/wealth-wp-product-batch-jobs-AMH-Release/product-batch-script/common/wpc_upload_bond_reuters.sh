#!/usr/bin/ksh

SCRIPT_DIR=$(dirname $0)

if [[ $# -lt 2 ]]
then
    echo "usage: wpc_upload_bond_reuters.sh <country code> <organization code>"
    exit
fi

source ${SCRIPT_DIR}/WPCEnvSetting.sh
DATE=`date +%Y%m%d`

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2

LOG_FILE_PATH="${LOG_FILE_PATH}"
LOG_FILE="product_import_bond_reuters_${ctryCde}${orgnCde}_${DATE}.log"

LOG_CLOUD_PATH=${LOG_FILE_CLOUD_PATH}
LOG_CLOUD_FILE=${LOG_FILE}

parm="ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} prodTypeCde=BOND systemCde=REUTERSBOND"

echo "Upload BOND REUTERS Input parameters: ${parm}"
echo "Upload BOND REUTERS Log File: ${LOG_FILE_PATH}/${LOG_FILE}"


if [[ ! -f ${LOG_FILE_PATH}/${LOG_FILE} ]]
then
    touch ${LOG_FILE_PATH}/${LOG_FILE}
    chmod 775 ${LOG_FILE_PATH}/${LOG_FILE}
fi

echo "=========================================================" >> ${LOG_FILE_PATH}/${LOG_FILE}
echo "Hostname: `hostname`" >> ${LOG_FILE_PATH}/${LOG_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") BOND REUTERS File Upload Job starting...." >> ${LOG_FILE_PATH}/${LOG_FILE}

echo "=========================================================" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "Hostname: `hostname`" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") BOND REUTERS File Upload Job starting...." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml,${SPRING_BATCH_CONFIG_PATH}/product-batch-import-bond-reuters-job.yml"
${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-batch-import-bond-reuters-job-2.0.0-SNAPSHOT.jar ${parm} --spring.config.location=${springConfig} >> ${LOG_FILE_PATH}/${LOG_FILE}

status=$?

if [[ ${status} -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Upload BOND REUTERS File." >> ${LOG_FILE_PATH}/${LOG_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Upload BOND REUTERS File." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") BOND REUTERS Input Upload Done." >> ${LOG_FILE_PATH}/${LOG_FILE}
   exit 0
fi