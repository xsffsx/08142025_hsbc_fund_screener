#!/usr/bin/ksh

#
# Created 24 Jan, 2024
# SMART+ sanction buy & sell list update job
# https://wpb-confluence.systems.example.com/pages/viewpage.action?pageId=3511116152
# 
if [ $# -lt 3 ]
then
    print "Usage: wpc_sanction_list_update.sh <country code> <organization code> <product type code>"
    print "  - <country code> is required indicates country code, e.g. HK and SG"
    print "  - <organization code> is required indicates organization code, e.g. dummy, HBAP, and HASE."
    print "  - <product type code> is required. Please enter only ONE product type code from supported values."
    print "    Supported product type codes: ELI, WRTS"
	  print "Example: wpc_sanction_list_update.sh HK HBAP ELI"
    exit
fi

# Input Parameters
typeset -u ctryRecCde=$1
typeset -u grpMembrRecCde=$2
typeset -u prodTypeCde=$3

JOB_NAME="Internal Sanction Buy And Sell List Update Job"

#### Path and File Definition -- START ####
SCRIPT_DIR=$(dirname $0)
source ${SCRIPT_DIR}/WPCEnvSetting.sh

LOG_FILE="product_sanction_update_${ctryRecCde}_${grpMembrRecCde}_${prodTypeCde}_$(date +%Y%m%d).log"
LOG_CLOUD_PATH=${LOG_FILE_CLOUD_PATH}
LOG_CLOUD_FILE=${LOG_FILE}

#### Path and File Definition -- END ####

echo "${JOB_NAME} log file: ${LOG_FILE_PATH}/${LOG_FILE}"
echo "${JOB_NAME} cloud log file: ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}"

echo "=========================================================" | tee -a ${LOG_FILE_PATH}/${LOG_FILE} | tee -a ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "Hostname: $(hostname)" | tee -a ${LOG_FILE_PATH}/${LOG_FILE} | tee -a ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "${JOB_NAME} input parameters: [ctryRecCde=${ctryRecCde}, grpMembrRecCde=${grpMembrRecCde}, prodTypeCde=${prodTypeCde}]" | tee -a ${LOG_FILE_PATH}/${LOG_FILE} | tee -a ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "$(date +"[%H:%M:%S %d/%m/%Y]") ${JOB_NAME} is starting ..." | tee -a ${LOG_FILE_PATH}/${LOG_FILE} | tee -a ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}

#### program -- START ####
springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml,${SPRING_BATCH_CONFIG_PATH}/product-sanction-update-job.yml"
${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-sanction-update-job-2.0.0-SNAPSHOT.jar ctryRecCde=${ctryRecCde} grpMembrRecCde=${grpMembrRecCde} prodTypeCde=${prodTypeCde} --spring.config.location=${springConfig} 2>&1 | tee -a ${LOG_FILE_PATH}/${LOG_FILE} | tee -a ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}

echo "$(date +"[%H:%M:%S %d/%m/%Y]") ${JOB_NAME} is completed." | tee -a ${LOG_FILE_PATH}/${LOG_FILE} | tee -a ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
