#!/usr/bin/ksh

SCRIPT_DIR=$(dirname $0)

if [[ $# -lt 3 ]]
then
    echo "usage: wpc_upload_esg.sh <country code> <organization code> <product type code>"
    exit
fi

source ${SCRIPT_DIR}/WPCEnvSetting.sh
DATE=`date +%Y%m%d`

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u prodTypeCde=$3

typeset uploadPath="${BATCH_INCOMING_PATH}/${ctryCde}${orgnCde}"

typeset csvPattern="GLB_dummy_GENERIC_ESG_SMART-${ctryCde}_[0-9]*.csv"

LOG_FILE_PATH="${LOG_FILE_PATH}"
LOG_FILE="product_import_esg_${ctryCde}${orgnCde}_${DATE}.log"
LOG_FILE_TEMP="$(basename ${LOG_FILE} .log)_`date +%s%3N`.log"

LOG_CLOUD_PATH=${LOG_FILE_CLOUD_PATH}
LOG_CLOUD_FILE=${LOG_FILE}

parm="${ctryCde} ${orgnCde} ${prodTypeCde}"

echo "Upload Global ESG CSV Input parameters: ${parm}"
echo "Upload Global ESG CSV Log File: ${LOG_FILE_PATH}/${LOG_FILE}"


if [[ ! -f ${LOG_FILE_PATH}/${LOG_FILE} ]]
then
    touch ${LOG_FILE_PATH}/${LOG_FILE}
    chmod 775 ${LOG_FILE_PATH}/${LOG_FILE}
fi

echo "=========================================================" >> ${LOG_FILE_PATH}/${LOG_FILE}
echo "Hostname: `hostname`" >> ${LOG_FILE_PATH}/${LOG_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Global ESG CSV File Upload Job starting...." >> ${LOG_FILE_PATH}/${LOG_FILE}

echo "=========================================================" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "Hostname: `hostname`" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Global ESG CSV File Upload Job starting...." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}

fileCount=`find ${uploadPath} -name ${csvPattern} -type f |wc -l`

if [ $fileCount -eq 0 ]
then
  echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") can not find any file to match ${csvPattern}" >> ${LOG_FILE_PATH}/${LOG_FILE}
  echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") can not find any file to match ${csvPattern}" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
  exit -1
fi

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml"

${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-batch-import-esg-job-2.0.0-SNAPSHOT.jar ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} prodTypeCde=${prodTypeCde} incomingPath=${uploadPath} filePattern=${csvPattern} --spring.config.location=${springConfig}  2>&1 | tee ${LOG_FILE_PATH}/${LOG_FILE_TEMP} | tee -a ${LOG_FILE_PATH}/${LOG_FILE} | tee -a ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}

status=$?

if [[ ${status} -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Upload Global ESG CSV File." >> ${LOG_FILE_PATH}/${LOG_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Upload Global ESG CSV File." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Global ESG CSV Upload Done." >> ${LOG_FILE_PATH}/${LOG_FILE}
   exit 0
fi