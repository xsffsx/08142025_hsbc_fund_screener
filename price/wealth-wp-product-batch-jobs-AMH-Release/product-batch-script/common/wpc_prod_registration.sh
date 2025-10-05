#!/usr/bin/ksh

SCRIPT_DIR=$(dirname $0)

if [[ $# -lt 6 ]]
then
    echo "usage: wpc_prod_registration.sh <country code> <organization code> <prodTypeCde> <groupSize> <supportAltIdCdes> <isDaltaSync>"
    echo "prodTypeCde: ALL or UT ..."
    echo "groupSize: 300"
    echo "supportAltIdCdes: I,S"
    echo "isDaltaSync: true"
    echo "Sample: .../wpc_prod_registration.sh HK HBAP ALL 300 I,S true"
    exit
fi

source ${SCRIPT_DIR}/WPCEnvSetting.sh
DATE=`date +%Y%m%d`
# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u prodTypeCde=$3
typeset -u groupSize=$4
typeset -u supportAltIdCdes=$5
typeset -u isNotAllProd=$6

LOG_FILE_PATH="${LOG_FILE_PATH}"
LOG_FILE="product_prod_registration_${ctryCde}${orgnCde}_${DATE}.log"


LOG_CLOUD_PATH=${LOG_FILE_CLOUD_PATH}
LOG_CLOUD_FILE=${LOG_FILE}

if [[ ! -f ${LOG_FILE_PATH}/${LOG_FILE} ]]
then
    touch ${LOG_FILE_PATH}/${LOG_FILE}
    chmod 775 ${LOG_FILE_PATH}/${LOG_FILE}
fi

echo "=========================================================" >> ${LOG_FILE_PATH}/${LOG_FILE}
echo "Hostname: `hostname`" >> ${LOG_FILE_PATH}/${LOG_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Update prod globalId job starting...." >> ${LOG_FILE_PATH}/${LOG_FILE}

echo "=========================================================" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "Hostname: `hostname`" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Update prod globalId job starting...." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml,${SPRING_BATCH_CONFIG_PATH}/product-product-registration-job.yml"
${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-product-registration-job-2.0.0-SNAPSHOT.jar ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} prodTypeCde=${prodTypeCde} groupSize=${groupSize} supportAltIdCdes=${supportAltIdCdes} isDaltaSync=${isNotAllProd} --spring.config.location=${springConfig} >> ${LOG_FILE_PATH}/${LOG_FILE}

status=$?

if [[ ${status} -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Update prod globalId job." >> ${LOG_FILE_PATH}/${LOG_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Update prod globalId job." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Update prod globalId job Done." >> ${LOG_FILE_PATH}/${LOG_FILE}
   exit 0
fi