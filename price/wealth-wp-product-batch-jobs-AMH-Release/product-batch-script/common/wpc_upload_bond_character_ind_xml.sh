#!/usr/bin/ksh

SCRIPT_DIR=$(dirname $0)

if [[ $# -lt 2 ]]
then
    echo "usage: wpc_upload_bond_character_ind_xml.sh <country code> <organization code>  <incoming path/file exist(optional)> "
    exit
fi

   
source ${SCRIPT_DIR}/WPCEnvSetting.sh
DATE=`date +%Y%m%d`

# Input Parameters
typeset uploadPath="${BATCH_INCOMING_PATH}/${ctryCde}${orgnCde}"

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset uploadPath="${BATCH_INCOMING_PATH}/${ctryCde}${orgnCde}"

if [[ $3 ]]
then
   uploadPath=$3
fi

LOG_FILE_PATH="${LOG_FILE_PATH}"
LOG_FILE="product-batch-import-bond-characterind_${ctryCde}${orgnCde}_${DATE}.log"

LOG_CLOUD_PATH=${LOG_FILE_CLOUD_PATH}
LOG_CLOUD_FILE=${LOG_FILE}

parm="${uploadPath}"

echo "Upload BOND Character Ind Input parameters: ${parm}"
echo "Upload BOND Character Ind Log File: ${LOG_FILE_PATH}/${LOG_FILE}"


if [[ ! -f ${LOG_FILE_PATH}/${LOG_FILE} ]]
then
    touch ${LOG_FILE_PATH}/${LOG_FILE}
    chmod 775 ${LOG_FILE_PATH}/${LOG_FILE}
fi

echo "=========================================================" >> ${LOG_FILE_PATH}/${LOG_FILE}
echo "Hostname: `hostname`" >> ${LOG_FILE_PATH}/${LOG_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") BOND Character Ind File Upload Job starting...." >> ${LOG_FILE_PATH}/${LOG_FILE}

echo "=========================================================" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "Hostname: `hostname`" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") BOND Character Ind File Upload Job starting...." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml"
${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-batch-import-bond-characterind-job-2.0.0-SNAPSHOT.jar ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} incomingPath=${uploadPath} --spring.config.location=${springConfig} >> ${LOG_FILE_PATH}/${LOG_FILE}

status=$?

if [[ ${status} -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Upload BOND Character Ind File." >> ${LOG_FILE_PATH}/${LOG_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Upload BOND Character Ind File." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") BOND Character Ind Input Upload Done." >> ${LOG_FILE_PATH}/${LOG_FILE}
   exit 0
fi
