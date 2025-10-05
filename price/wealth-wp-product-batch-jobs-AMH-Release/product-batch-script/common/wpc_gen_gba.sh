#!/usr/bin/ksh

#
# Created 4 Jan, 2011
#

if [[ $# -lt 4 ]]
then
    print "usage: wpc_gen_gba.sh <country code> <organization code> <product type code> "
    exit
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u sysCde=$3
typeset -u prodTypeCde=$4

script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh


#### Path and File Definition -- START ####
log_path=${LOG_FILE_PATH}
DATE=`date +%Y%m%d`
log_file="product_import_${sysCde}_${prodTypeCde}_xml_${ctryCde}${orgnCde}_${DATE}.log"

config_path=${CONFIG_FILE_PATH}
log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file="${log_file}"


echo "gen GBA for ${prodTypeCde} prod parameters: ${ctryCde} ${orgnCde} ${sysCde} ${prodTypeCde}"

echo "gen GBA log File: ${log_path}/${log_file}"

echo "=========================================================" >> ${log_path}/${log_file}
echo "Hostname: `hostname`" >> ${log_path}/${log_file}
shellPara=$@
echo "Parameters: ${shellPara}" >> ${log_path}/${log_file}
echo "=========================================================" >> ${log_cloud_path}/${log_cloud_file}
echo "Hostname: `hostname`" >> ${log_cloud_path}/${log_cloud_file}
echo "Parameters: ${shellPara}" >> ${log_cloud_path}/${log_cloud_file}
#### program -- START ####


chmod -f 764 ${log_path}/${log_file}

param="ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} systemCde=${sysCde} prodTypeCde=${prodTypeCde} outputPath=${log_path}"
springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml"
${JAVA_PATH} -jar -Dspring.config.location=${springConfig} ${JAR_FILE_PATH}/product-gba-generation-job-2.0.0-SNAPSHOT.jar ${param} >> ${log_path}/${log_file}

status=$?

if [[ ${status} -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Gen GBA ${prodTypeCde} prodList." >> ${log_path}/${log_file}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Gen GBA ${prodTypeCde} prodList." >> ${log_cloud_path}/${log_cloud_file}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") GEN GBA ${prodTypeCde} prodList Done." >> ${log_path}/${log_file}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") GEN GBA ${prodTypeCde} prodList Done." >> ${log_cloud_path}/${log_cloud_file}

	WPCDIR=/appvol/product/data
	CUTASDIR=${WPCDIR}/Adaptor/incoming
	CUTASFILE=`ls -1 ${CUTASDIR}/HFIUT* | grep -v "ack" | tail -n1` TEMP_CUTAS_FILE=${WPCDIR}/logs/batch/temp-cutas-gba.txt
	WPCFILE=`ls -1 ${log_path}/HKHBAPGBAUT*.txt | tail -n1`
	LOG_FILE=${log_path}/gba-ind-comp-log-`date "+%Y%m%d"`.log
	
	if [ "${CUTASFILE}" == "" ]
	then
	  echo "Unable to find CUTAS file"
	  exit 0 
	fi
	
	if [ "${WPCFILE}" == "" ]
	then
	  echo "Unable to find WPC file"
	  exit 0 
	fi
	
	date "+%Y%m%d-%H%M%S" | tee -a ${LOG_FILE}
	echo "LOG FILE  : ${LOG_FILE}"
	echo "CUTAS FILE: ${CUTASFILE}"
	echo "WPC FILE  : ${WPCFILE}"
	
	# Main logic
	iconv -c -f cp1255 -t utf8 ${CUTASFILE} | awk 'NR==1 {next} NR==2 {prev = $0; next} {print substr(prev,319,6) substr(prev,687,2); prev = $0}' | sort | uniq > ${TEMP_CUTAS_FILE}
	
	# print title
	echo "CUTAS                                                         | WPC" | tee -a ${LOG_FILE}
	diff -y ${TEMP_CUTAS_FILE} ${WPCFILE} | grep "|" | tee -a ${LOG_FILE}
	mismatch=`diff -y ${TEMP_CUTAS_FILE} ${WPCFILE} | grep "|" | wc -l`
	
	if [ ${mismatch} -eq 0 ]
	then
	  echo "CUTAS vs WPC matched successfully." | tee -a ${LOG_FILE}
	  exit 0
	else
	  echo "CUTAS vs WPC not match." | tee -a ${LOG_FILE}
	  exit 0 
	fi

fi

#### program -- END ####
