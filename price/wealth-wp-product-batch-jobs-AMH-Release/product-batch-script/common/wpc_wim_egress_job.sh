#!/usr/bin/ksh

SCRIPT_DIR=$(dirname $0)
source ${SCRIPT_DIR}/WPCEnvSetting.sh
DATE=`date +%Y%m%d`

# Input Parameters
typeset outputPath="${OUTPUT_FILE_PATH}/HKHBAP/WIM/"


if [[ $1 ]]
then
   outputPath=$1
fi

LOG_FILE_PATH="${LOG_FILE_PATH}"
LOG_FILE="product_wim_egress_job_${DATE}.log"

LOG_CLOUD_PATH=${LOG_FILE_CLOUD_PATH}
LOG_CLOUD_FILE=${LOG_FILE}

echo "WIM Egress Job Output Path: ${outputPath}"
echo "WIM Egress Job Log File: ${LOG_FILE_PATH}/${LOG_FILE}"


if [[ ! -f ${LOG_FILE_PATH}/${LOG_FILE} ]]
then
    touch ${LOG_FILE_PATH}/${LOG_FILE}
    chmod 775 ${LOG_FILE_PATH}/${LOG_FILE}
fi

echo "=========================================================" >> ${LOG_FILE_PATH}/${LOG_FILE}
echo "Hostname: `hostname`" >> ${LOG_FILE_PATH}/${LOG_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WIM Egress Job starting...." >> ${LOG_FILE_PATH}/${LOG_FILE}

echo "=========================================================" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "Hostname: `hostname`" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WIM Egress Job starting...." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml,${SPRING_BATCH_CONFIG_PATH}/product-wim-egress-job.yml"
${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-wim-egress-job-2.0.0-SNAPSHOT.jar outputPath=${outputPath} --spring.config.location=${springConfig} >> ${LOG_FILE_PATH}/${LOG_FILE}

status=$?

if [[ ${status} -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Export WIM Csv File." >> ${LOG_FILE_PATH}/${LOG_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Export WIM Csv File." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WIM Csv File Export Done." >> ${LOG_FILE_PATH}/${LOG_FILE}
   exit 0
fi
