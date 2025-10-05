#!/usr/bin/ksh

SCRIPT_DIR=$(dirname $0)

if [[ $# -lt 3 ]]
then
    echo "usage: wpc_eli_risk_level_update.sh.sh <country code> <organization code> <eli Payoff Type Cde list>"
    exit
fi

source ${SCRIPT_DIR}/WPCEnvSetting.sh
DATE=`date +%Y%m%d`

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u eliPayoffTypeCde=$3

LOG_FILE_PATH="${LOG_FILE_PATH}"
LOG_FILE="product_prod_eli_risk_level_update_${ctryCde}${orgnCde}_${DATE}.log"

LOG_CLOUD_PATH=${LOG_FILE_CLOUD_PATH}
LOG_CLOUD_FILE=${LOG_FILE}

parm="${ctryCde} ${orgnCde}"

echo "Product Eli Risk Level Update Job Input parameters: ${parm}"
echo "Product Eli Risk Level Update Job Log File: ${LOG_FILE_PATH}/${LOG_FILE}"


if [[ ! -f ${LOG_FILE_PATH}/${LOG_FILE} ]]
then
    touch ${LOG_FILE_PATH}/${LOG_FILE}
    chmod 775 ${LOG_FILE_PATH}/${LOG_FILE}
fi

echo "=========================================================" >> ${LOG_FILE_PATH}/${LOG_FILE}
echo "Hostname: `hostname`" >> ${LOG_FILE_PATH}/${LOG_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Product Eli Risk Level Update Job Job starting...." >> ${LOG_FILE_PATH}/${LOG_FILE}

echo "=========================================================" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "Hostname: `hostname`" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Product Eli Risk Level Update Job Job starting...." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml,${SPRING_BATCH_CONFIG_PATH}/product-product-field-calculation-job.yml"
${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-product-field-calculation-job-2.0.0-SNAPSHOT.jar ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} eliPayoffTypeCde=${eliPayoffTypeCde} calculatedField=eliRiskLvlCde --spring.config.location=${springConfig} >> ${LOG_FILE_PATH}/${LOG_FILE}

status=$?

if [[ ${status} -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Product Eli Risk Level Update Job Successfully." >> ${LOG_FILE_PATH}/${LOG_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Product Eli Risk Level Update Job Successfully." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Product Eli Risk Level Update Job Job Done." >> ${LOG_FILE_PATH}/${LOG_FILE}
   exit 0
fi