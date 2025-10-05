#!/usr/bin/ksh

#
# Created 03 Sep, 2019
#
# Financial Document Release Service
# 

if [[ $# -ne 2 ]]
then
   print "usage: wpc_fin_doc_release.sh <country code> <groupmember code>"
   print "example: wpc_fin_doc_release.sh HK HBAP"
   exit
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2

#### Path and File Definition -- START ####
script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
log_path=${LOG_FILE_PATH}
log_file="${ctryCde}_${orgnCde}_fin_doc_release_$(date +%Y%m%d).log"

log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file="wpc_${log_file}"
#### Path and File Definition -- END ####

echo "=========================================================" >> ${log_path}/${log_file}
echo "Hostname: `hostname`" >> ${log_path}/${log_file}
shellPara=$@
echo "Parameters: ${shellPara}" >> ${log_path}/${log_file}

echo $$" =========================================================" >> ${log_cloud_path}/${log_cloud_file}
echo $$" Hostname: `hostname`" >> ${log_cloud_path}/${log_cloud_file}
echo $$" Parameters: ${shellPara}" >> ${log_cloud_path}/${log_cloud_file}
#### WPCFinDocBatch program -- START ####

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Financial Document Release Begins.... " >> ${log_path}/${log_file}
chmod -f 764 ${log_path}/${log_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Financial Document Release Begins.... " >> ${log_cloud_path}/${log_cloud_file}
chmod -f 764 ${log_cloud_path}/${log_cloud_file}

temp_log_file=$$_"temp_${log_file}"

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml,${SPRING_BATCH_CONFIG_PATH}/product-finDoc-release-job.yml"
${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-finDoc-release-job-2.0.0-SNAPSHOT.jar ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} --spring.config.location=${springConfig} >> ${log_path}/${temp_log_file} 2>&1

status=$?

# read from temp file and write into both logs, then remove temp file
while read line
do
	echo $line >> ${log_path}/${log_file}
	echo $$" ${line}" >> ${log_cloud_path}/${log_cloud_file}
done < ${log_path}/${temp_log_file}
rm ${log_path}/${temp_log_file}

if (( $status != 0 ))
then
        echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Java program not started" >> ${log_path}/${log_file}
        echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Java program not started" >> ${log_cloud_path}/${log_cloud_file}
        logger -t root "WPC1010E wpc_fin_doc_release.sh: Java program not started. Please contact WMD Support."
        echo "WPC1010E wpc_fin_doc_release.sh: Java program not started. Please contact WMD Support." >> ${log_cloud_path}/${log_cloud_file}
        exit -1

fi

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Financial Document Release End " >> ${log_path}/${log_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Financial Document Release End " >> ${log_cloud_path}/${log_cloud_file}
#### WPCFinDocBatch program -- END ####
