#!/bin/bash

SCRIPT_DIR=$(dirname $0)
source ${SCRIPT_DIR}/WPCEnvSetting.sh
BASE_DIR="${BATCH_DATA_PATH}"
#SCRIPT_DIR="${BATCH_APP_PATH}"
DATE=`date +%Y%m%d`
LOG_FILE_DIR="${LOG_FILE_PATH}"

if [[ $# -lt 1 ]]
then
    echo "usage: wpc_gen_report.sh <report type>" 
    echo "example:wpc_gen_report.sh REFEDATA" 
    exit -1
fi

# Input Parameters
typeset -u reportTypeCde=$1
LOG_FILE="product_gen_report_for_${reportTypeCde}_${DATE}.log"
genFilePath="${REPORT_FILE_PATH}"

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
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Generating Report Output Interface for ${reportTypeCde} start." >> ${LOG_FILE_DIR}/${LOG_FILE}

echo "=========================================================" >> ${log_cloud_path}/${log_cloud_file}
echo "Hostname: `hostname`" >>  ${log_cloud_path}/${log_cloud_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Generating Report Output Interface for ${reportTypeCde} start." >> ${log_cloud_path}/${log_cloud_file}

BATCH_URL=${BATCH_URL}
http_code=`curl -k -X POST $BATCH_URL -H "Content-type:application/json" -d@${SCRIPT_DIR}/${reportTypeCde}.json`;
#echo "${SCRIPT_DIR}${reportTypeCde}.json">${LOG_FILE_DIR}/${LOG_FILE}

echo "${http_code}">>${LOG_FILE_DIR}/${LOG_FILE}
echo "${http_code}">>${log_cloud_path}/${log_cloud_file}
status=$?
if [[ ${status} -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Generating Report Output Interface for ${reportTypeCde} successful." >> ${LOG_FILE_DIR}/${LOG_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Generating Report Output Interface for ${reportTypeCde} successful." >> ${log_cloud_path}/${log_cloud_file}
 #  files=$(find ${genFilePath}/ -type f ! -perm 775 | grep _${reportTypeCde}_)
  # for file in $files; do
  #    chmod 775 ${file}
   #done
   echo "Generate Report Output Interface for ${reportTypeCde} Done"
   echo "Generate Report Output Interface for ${reportTypeCde} Done" >> ${LOG_FILE_DIR}/${LOG_FILE}
   echo "Generate Report Output Interface for ${reportTypeCde} Done" >> ${log_cloud_path}/${log_cloud_file}
else
   logger -t root "OTPSERR_ZDP001 wpc_gen_report.sh: Problem encountered during generate interface file for ${reportTypeCde}. Please contact WMD Support."
   echo "OTPSERR_ZDP001 wpc_gen_report.sh: Problem encountered during generate interface file for ${reportTypeCde}. Please contact WMD Support.">>${log_cloud_path}/${log_cloud_file}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Generating Report Output Interface for ${reportTypeCde} failed." >> ${LOG_FILE_DIR}/${LOG_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Generating Report Output Interface for ${reportTypeCde} failed." >> ${log_cloud_path}/${log_cloud_file}
   echo "Generate Report Output Interface for ${reportTypeCde} Done"
   echo "Generate Report Output Interface for ${reportTypeCde} not Done" >> ${LOG_FILE_DIR}/${LOG_FILE}
   echo "Generate Report Output Interface for ${reportTypeCde} not Done" >> ${log_cloud_path}/${log_cloud_file}
   exit -1
fi

#${SCRIPT_DIR}gen_file_status_check_by_system.sh ${reportTypeCde}

#exit ${rc}
#### program -- END ####