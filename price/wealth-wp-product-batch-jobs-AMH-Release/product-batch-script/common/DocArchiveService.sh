#!/bin/ksh

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2

script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh

#### Configuration File ####
log_path=${LOG_FILE_PATH}
logfile="${log_path}/FinDoc_DocArchiveService$(date +%Y%m%d).log"
log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file="wpc_FinDoc_DocArchiveService$(date +%Y%m%d).log"

if [[ ! -f "$logfile" ]]
then
	touch "$logfile"
fi

chmod -f go+r $logfile

echo "=========================================================" >> $logfile
echo "Hostname: `hostname`" >> $logfile
shellPara=$@
echo "Parameters: ${shellPara}" >> $logfile
echo "=========================================================" >> ${log_cloud_path}/${log_cloud_file}
echo "Hostname: `hostname`" >> ${log_cloud_path}/${log_cloud_file}
echo "Parameters: ${shellPara}" >> ${log_cloud_path}/${log_cloud_file}

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Executing DocArchiveService" >> $logfile
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Executing DocArchiveService" >> ${log_cloud_path}/${log_cloud_file}

#### HFIBatch program -- START ####
parm="ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde}"
temp_log_file=$$_"temp_${log_cloud_file}"

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml,${SPRING_BATCH_CONFIG_PATH}/product-finDoc-archive-job.yml"
${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-finDoc-archive-job-2.0.0-SNAPSHOT.jar ${parm} --spring.config.location=${springConfig} >> ${log_path}/${temp_log_file} 2>&1

status=$?

# read from temp file and write into both logs, then remove temp file
while read line
do
	echo $line >> $logfile
	echo $$" ${line}" >> ${log_cloud_path}/${log_cloud_file}
done < ${log_path}/${temp_log_file}
rm ${log_path}/${temp_log_file}

if (( $status != 0 ))
then
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Java program not started" >> $logfile
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Java program not started" >> ${log_cloud_path}/${log_cloud_file}
	  logger -t root "WPC1013E DocArchiveService.sh: Java program not started. Please contact WMD Support."
	  echo "WPC1013E DocArchiveService.sh: Java program not started. Please contact WMD Support." >> ${log_cloud_path}/${log_cloud_file}
fi

echo "END" >>$logfile
echo "END" >>${log_cloud_path}/${log_cloud_file}

#### HFIBatch program -- END ####

