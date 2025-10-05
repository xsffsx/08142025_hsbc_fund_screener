#!/usr/bin/ksh

SCRIPT_DIR=$(dirname $0)
source ${SCRIPT_DIR}/WPCEnvSetting.sh
DATE=`date +%Y%m%d`

if [[ $# -lt 4 ]]; then
    print "usage: wpc_pw0_egress_job.sh <country code> <organization code> <product type code> <target name>"
    print "example: wpc_pw0_egress_job.sh HK HBAP WRTS apexaProduct"
    exit -1
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u prodType=$3
typeset targetName=$4
typeset outputPath="${OUTPUT_FILE_PATH}/${ctryCde}${orgnCde}/APEXA/"
parm=$@
echo "Input parameters: ${parm}"

if [[ $5 ]]
then
   outputPath=$5
fi

LOG_FILE_PATH="${LOG_FILE_PATH}"
LOG_FILE="product_export_pw0_json_file_job_${ctryCde}_${orgnCde}_${DATE}.log"

LOG_CLOUD_PATH=${LOG_FILE_CLOUD_PATH}
LOG_CLOUD_FILE=${LOG_FILE}

echo "PW0 Egress Job Output Path: ${outputPath}"
echo "PW0 Egress Job Log File: ${LOG_FILE_PATH}/${LOG_FILE}"


if [[ ! -f ${LOG_FILE_PATH}/${LOG_FILE} ]]
then
    touch ${LOG_FILE_PATH}/${LOG_FILE}
    chmod 775 ${LOG_FILE_PATH}/${LOG_FILE}
fi

echo "=========================================================" >> ${LOG_FILE_PATH}/${LOG_FILE}
echo "Hostname: `hostname`" >> ${LOG_FILE_PATH}/${LOG_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") PW0 Export JSON File Job starting...." >> ${LOG_FILE_PATH}/${LOG_FILE}

echo "=========================================================" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "Hostname: `hostname`" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") PW0 Export JSON File Job starting...." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml,${SPRING_BATCH_CONFIG_PATH}/product-export-pw0-json-file-job.yml"
${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-export-pw0-json-file-job-2.0.0-SNAPSHOT.jar ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} prodTypCde=${prodType} targetName=${targetName} outputPath=${outputPath} --spring.config.location=${springConfig} >> ${LOG_FILE_PATH}/${LOG_FILE}

status=$?

WDS_PATH=/appvol/wds/data/incoming/HKHBAP

if [[ ${status} -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Export PW0 JSON File." >> ${LOG_FILE_PATH}/${LOG_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Export PW0 JSON File." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") PW0 JSON File Export Done." >> ${LOG_FILE_PATH}/${LOG_FILE}
   # Copy to WDS folder
   find ${outputPath} -type f -name "$1_$2_$3_$4_*.json" | sort -r | head -n 1 | xargs -I {} cp {} ${WDS_PATH}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") PW0 JSON File Transfer Done." >> ${LOG_FILE_PATH}/${LOG_FILE}
   exit 0
else
   logger -t root "Problem encountered during export PW0 JSON file.."
   echo "Problem encountered during export PW0 JSON file." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to Export PW0 JSON File." >> ${LOG_FILE_PATH}/${LOG_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Export PW0 JSON Export Done." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
   exit -1
fi
