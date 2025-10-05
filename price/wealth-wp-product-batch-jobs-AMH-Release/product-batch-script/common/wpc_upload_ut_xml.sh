#!/usr/bin/ksh

SCRIPT_DIR=$(dirname $0)

if [[ $# -lt 3 ]]
then
    echo "usage: wpc_upload_ut_xml.sh <country code> <organization code> <system code>  <incoming path/file exist(optional)> "
    exit
fi

source ${SCRIPT_DIR}/WPCEnvSetting.sh
DATE=`date +%Y%m%d`

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u sysCde=$3
typeset uploadPath="${BATCH_INCOMING_PATH}/${ctryCde}${orgnCde}"


if [[ $4 ]]
then
   uploadPath=$4
fi

LOG_FILE_PATH="${LOG_FILE_PATH}"
LOG_FILE="product_import_${sysCde}_ut_xml_${ctryCde}${orgnCde}_${DATE}.log"

LOG_CLOUD_PATH=${LOG_FILE_CLOUD_PATH}
LOG_CLOUD_FILE=${LOG_FILE}

parm="${ctryCde} ${orgnCde} ${sysCde} ${uploadPath}"

echo "Upload UT XML Input parameters: ${parm}"
echo "Upload UT XML Log File: ${LOG_FILE_PATH}/${LOG_FILE}"


if [[ ! -f ${LOG_FILE_PATH}/${LOG_FILE} ]]
then
    touch ${LOG_FILE_PATH}/${LOG_FILE}
    chmod 775 ${LOG_FILE_PATH}/${LOG_FILE}
fi

echo "=========================================================" >> ${LOG_FILE_PATH}/${LOG_FILE}
echo "Hostname: `hostname`" >> ${LOG_FILE_PATH}/${LOG_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") UT XML File Upload Job starting...." >> ${LOG_FILE_PATH}/${LOG_FILE}

echo "=========================================================" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "Hostname: `hostname`" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") UT XML File Upload Job starting...." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml,${SPRING_BATCH_CONFIG_PATH}/product-batch-import-ut-xml-job.yml"
${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-batch-import-ut-xml-job-2.0.0-SNAPSHOT.jar ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} systemCde=${sysCde} incomingPath=${uploadPath} --spring.config.location=${springConfig} >> ${LOG_FILE_PATH}/${LOG_FILE}

status=$?

if [[ ${status} -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Upload UT XML File." >> ${LOG_FILE_PATH}/${LOG_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Upload UT XML File." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") UT XML Input Upload Done." >> ${LOG_FILE_PATH}/${LOG_FILE}
   exit 0
fi