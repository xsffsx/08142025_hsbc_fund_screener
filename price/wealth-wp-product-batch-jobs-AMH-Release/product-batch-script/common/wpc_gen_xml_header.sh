#!/bin/bash

SCRIPT_DIR=$(dirname $0)
source ${SCRIPT_DIR}/WPCEnvSetting.sh
BASE_DIR="${BATCH_DATA_PATH}"
#SCRIPT_DIR="${BATCH_APP_PATH}"
DATE=`date +%Y%m%d`
LOG_FILE_DIR="${LOG_FILE_PATH}"

if [[ $# -lt 1 ]]
then
    echo "usage: wpc_gen_xml_header.sh <system code>"
    echo "example: wpc_gen_xml_header.sh BI"
    exit -1
fi

# Input Parameters
typeset -u sysCde=$1
LOG_FILE="product_gen_file_for_${sysCde}_${DATE}.log"
genFilePath="${OUTPUT_FILE_PATH}/${sysCde}"

log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file=${LOG_FILE}

temp_file=$(mktemp -t temp_json.XXXXXXXX)
if [ ! -f $temp_file ]; then
  touch $temp_file
fi

if [[ ! -f ${LOG_FILE_DIR}/${LOG_FILE} ]]
then
    touch ${LOG_FILE_DIR}/${LOG_FILE}
    chmod 775 ${LOG_FILE_DIR}/${LOG_FILE}
fi

#
# Log message with timestamp
#
function log
{
  print "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") "$1
}

echo "=========================================================" >> ${LOG_FILE_DIR}/${LOG_FILE}
echo "Hostname: `hostname`" >> ${LOG_FILE_DIR}/${LOG_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Generating XML Output Interface for ${sysCde} start." >> ${LOG_FILE_DIR}/${LOG_FILE}

echo "=========================================================" >> ${log_cloud_path}/${log_cloud_file}
echo "Hostname: `hostname`" >> ${log_cloud_path}/${log_cloud_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Generating XML Output Interface for ${sysCde} ${fileTypeCde} start." >> ${log_cloud_path}/${log_cloud_file}

BATCH_URL=${BATCH_URL}
http_code=`curl -k -X POST $BATCH_URL -H "Content-type:application/json" -d@${SCRIPT_DIR}/${sysCde}.json`;
#echo "${SCRIPT_DIR}${sysCde}.json">${LOG_FILE_DIR}/${LOG_FILE}

echo "${http_code}">>${LOG_FILE_DIR}/${LOG_FILE}
echo "${http_code}">>${log_cloud_path}/${log_cloud_file}
status=$?
if [[ ${status} -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Generating XML Output Interface for ${sysCde} successful." >> ${LOG_FILE_DIR}/${LOG_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Generating XML Output Interface for ${sysCde} ${fileTypeCde} successful." >> ${log_cloud_path}/${log_cloud_file}
   echo "Generate XML Output Interface for ${sysCde} Done"
   echo "Generate XML Output Interface for ${sysCde} Done" >> ${LOG_FILE_DIR}/${LOG_FILE}
   echo "Generate XML Output Interface for ${sysCde} ${fileTypeCde} Done" >> ${log_cloud_path}/${log_cloud_file}
else
   logger -t root "OTPSERR_ZBJ001 wpc_gen_xml_header.sh: Problem encountered during generate interface file for ${sysCde}. Please contact WMD Support."
   echo "wpc_gen_xml_header.sh: Problem encountered during generate interface file for ${sysCde} ${fileTypeCde}. Please contact WMD Support." >>${log_cloud_path}/${log_cloud_file}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Generating XML Output Interface for ${sysCde} failed." >> ${LOG_FILE_DIR}/${LOG_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") OTPSERR_ZBJ001 Generating XML Output Interface for ${sysCde} ${fileTypeCde} failed." >> ${log_cloud_path}/${log_cloud_file}
   echo "Generate XML Output Interface for ${sysCde} Done"
   echo "Generate XML Output Interface for ${sysCde} not Done" >> ${LOG_FILE_DIR}/${LOG_FILE}
   echo "Generate XML Output Interface for ${sysCde} ${fileTypeCde} not Done" >> ${log_cloud_path}/${log_cloud_file}
   exit -1
fi

#${SCRIPT_DIR}gen_file_status_check_by_system.sh ${sysCde}

#exit ${rc}
#### program -- END ####