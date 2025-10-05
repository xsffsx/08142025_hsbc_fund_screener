#!/usr/bin/ksh
#
# Last Edit Jue, 2017
#

# Usage Message
usageHelper="1)usage:\nwpc_spoms_maint.sh <country code> <organization code> <product type code> "
if [[ $# -lt 3 ]]
then
    print ${usageHelper}
    exit
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u prodTypeCde=$3

# Define Path
script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
monitorFilePath="${FINDOC_INCOMING_PATH}/${ctryCde}${orgnCde}/${prodTypeCde}"
log_path=${LOG_FILE_PATH}

log_file="${ctryCde}_${orgnCde}_wpc_spoms_maint_${prodTypeCde}_$(date +%Y%m%d).log"

log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file="wpc_${log_file}"

echo "=========================================================" >> ${log_path}/${log_file}
parm="${ctryCde} ${orgnCde} ${prodTypeCde} ${monitorFilePath}"
echo "Process SBOMS Interface parameters: ${parm}" >> ${log_path}/${log_file}
echo "Hostname: `hostname`" >> ${log_path}/${log_file}
shellPara=$@
echo "Parameters: ${shellPara}" >> ${log_path}/${log_file}
echo "=========================================================" >> ${log_cloud_path}/${log_cloud_file}
echo "Hostname: `hostname`" >> ${log_cloud_path}/${log_cloud_file}
echo "Parameters: ${shellPara}" >> ${log_cloud_path}/${log_cloud_file}
#### program -- START ####


echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Process SBOMS ${prodTypeCde} Interface...." >> ${log_path}/${log_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Process SBOMS ${prodTypeCde} Interface...." >> ${log_cloud_path}/${log_cloud_file}
chmod -f 764 ${log_path}/${log_file}
chmod -f 764 ${log_cloud_path}/${log_cloud_file}

temp_log_file=$$_"temp_${log_file}"

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml,${SPRING_BATCH_CONFIG_PATH}/product-batch-import-eli-finDoc-job.yml"
parm="ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} prodTypeCde=${prodTypeCde} fileName=${monitorFilePath} actionCde=N"
${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-batch-import-eli-finDoc-job-2.0.0-SNAPSHOT.jar ${parm} --spring.config.location=${springConfig} >> ${log_path}/${temp_log_file} 2>&1

status=$?

# read from temp file and write into both logs, then remove temp file
while read line
do
	echo $line >> ${log_path}/${log_file}
	echo $$" ${line}" >> ${log_cloud_path}/${log_cloud_file}
done < ${log_path}/${temp_log_file}
rm ${log_path}/${temp_log_file}

if [[ ${status} -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully process SBOMS ${prodTypeCde} Interfaces." >> ${log_path}/${log_file}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully process SBOMS ${prodTypeCde} Interfaces." >> ${log_cloud_path}/${log_cloud_file}
else
   logger -t root "WPC1013E wpc_eli_spoms_maint.sh: Problem encountered process SBOMS ${prodTypeCde} file for ${ctryCde}. Please contact WMD Support."
   echo "WPC1013E wpc_eli_spoms_maint.sh: Problem encountered process SBOMS ${prodTypeCde} file for ${ctryCde}. Please contact WMD Support." >> ${log_cloud_path}/${log_cloud_file}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to process SBOMS ${prodTypeCde} Interfaces." >> ${log_path}/${log_file}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to process SBOMS ${prodTypeCde} Interfaces." >> ${log_cloud_path}/${log_cloud_file}
fi

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Process SBOMS ${prodTypeCde} Interface Generation Done." >> ${log_path}/${log_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Process SBOMS ${prodTypeCde} Interface Generation Done." >> ${log_cloud_path}/${log_cloud_file}
#### program -- END ####
