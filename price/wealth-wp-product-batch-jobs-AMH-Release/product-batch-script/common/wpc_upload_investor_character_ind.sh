#!/usr/bin/ksh

SCRIPT_DIR=$(dirname $0)


if [[ $# -lt 3 ]]
then
   print "usage: wpc_upload_ic_ind.sh <country code> <organization code> <batch code> <incoming path(optional)> "
   print "<batch code> possible value:"
   print " -- I: init ic to wpc from GSOPS via interface at first time"
   print " -- M: 20-minutes IC flag"
   print " -- D: daily ic flag update"
   print " -- R: monthly reconciliation"
   print " -- E: exception items retry after CUTAS product update job"
   print "<file name (optional)>"
   print '	 default: ${BATCH_INCOMING_PATH}/${ctryCde}${orgnCde}/'
   exit
fi

   
source ${SCRIPT_DIR}/WPCEnvSetting.sh
DATE=`date +%Y%m%d`

# Input Parameters
typeset uploadPath="${BATCH_INCOMING_PATH}/${ctryCde}${orgnCde}"

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u batchCode=$3
typeset uploadPath="${BATCH_INCOMING_PATH}/${ctryCde}${orgnCde}"

if [[ $4 ]]
then
   uploadPath=$4
fi

LOG_FILE_PATH="${LOG_FILE_PATH}"
LOG_FILE="product-batch-import-investor-characterind_${ctryCde}${orgnCde}_${DATE}.log"

LOG_CLOUD_PATH=${LOG_FILE_CLOUD_PATH}
LOG_CLOUD_FILE=${LOG_FILE}

parm="${uploadPath}"

echo "Upload Investor Character Ind Input parameters: ${parm}"
echo "Upload Investor Character Ind Log File: ${LOG_FILE_PATH}/${LOG_FILE}"


if [[ ! -f ${LOG_FILE_PATH}/${LOG_FILE} ]]
then
    touch ${LOG_FILE_PATH}/${LOG_FILE}
    chmod 775 ${LOG_FILE_PATH}/${LOG_FILE}
fi

echo "=========================================================" >> ${LOG_FILE_PATH}/${LOG_FILE}
echo "Hostname: `hostname`" >> ${LOG_FILE_PATH}/${LOG_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Investor Character Ind File Upload Job starting...." >> ${LOG_FILE_PATH}/${LOG_FILE}

echo "=========================================================" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "Hostname: `hostname`" >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Investor Character Ind File Upload Job starting...." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml"
${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-batch-import-investor-characterind-job-2.0.0-SNAPSHOT.jar ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} incomingPath=${uploadPath} batchCode=${batchCode} --spring.config.location=${springConfig} >> ${LOG_FILE_PATH}/${LOG_FILE}

status=$?

if [[ ${status} -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Upload Investor Character Ind File." >> ${LOG_FILE_PATH}/${LOG_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Upload Investor Character Ind File." >> ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Investor Character Ind Input Upload Done." >> ${LOG_FILE_PATH}/${LOG_FILE}
   exit 0
fi
