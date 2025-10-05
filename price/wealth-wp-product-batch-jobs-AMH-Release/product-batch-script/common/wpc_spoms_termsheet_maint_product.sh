#!/usr/bin/ksh
#
# Last Edit Jue, 2017
#

# Usage Message
usageHelper="1)usage:\n wpc_spoms_termsheet_maint_product.sh <country code> <organization code> <product type code> <is post product>"
if [[ $# -lt 3 ]]
then
    print ${usageHelper}
    exit
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u prodTypeCde=$3
typeset -u postProduct=$4

# Define Path
script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
monitorFilePath="${FINDOC_INCOMING_PATH}/${ctryCde}${orgnCde}/${prodTypeCde}"
log_path=${LOG_FILE_PATH}
config_path=${CONFIG_FILE_PATH}

log_file="${ctryCde}_${orgnCde}_wpc_spoms_termsheet_maint_product_${prodTypeCde}_$(date +%Y%m%d).log"

log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file="wpc_${log_file}"

echo "=========================================================" >> ${log_path}/${log_file}
parm="${ctryCde} ${orgnCde} ${prodTypeCde} ${monitorFilePath} ${postProduct}"
echo "Post process SPOMS Termsheets product Interface parameters: ${parm}" >> ${log_path}/${log_file}
echo "Hostname: `hostname`" >> ${log_path}/${log_file}
shellPara=$@
echo "Parameters: ${shellPara}" >> ${log_path}/${log_file}
echo "=========================================================" >> ${log_cloud_path}/${log_cloud_file}
echo "Hostname: `hostname`" >> ${log_cloud_path}/${log_cloud_file}
echo "Parameters: ${shellPara}" >> ${log_cloud_path}/${log_cloud_file}

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Post process SPOMS Termsheets product ${prodTypeCde} Interface...." >> ${log_path}/${log_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Post process SPOMS Termsheets product ${prodTypeCde} Interface...." >> ${log_cloud_path}/${log_cloud_file}
chmod -f 755 ${log_path}/${log_file}

temp_log_file=$$_"temp_${log_file}"

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml,${SPRING_BATCH_CONFIG_PATH}/product-batch-import-termsheet-job.yml"
param="ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} prodTypeCde=${prodTypeCde} finDocPath=${monitorFilePath} isPostProduct=${postProduct}"
${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-batch-import-termsheet-job-2.0.0-SNAPSHOT.jar ${param} --spring.config.location=${springConfig} >> ${log_path}/${temp_log_file} 2>&1

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
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Post process SPOMS Termsheets product ${prodTypeCde} Interfaces." >> ${log_path}/${log_file}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Post process SPOMS Termsheets product ${prodTypeCde} Interfaces." >> ${log_cloud_path}/${log_cloud_file}
else
   logger -t root "WPC1013E wpc_spoms_termsheet_maint_product.sh: Problem encountered during Post process SPOMS Termsheets product ${prodTypeCde}. Please contact WMD Support."
   echo "WPC1013E wpc_spoms_termsheet_maint_product.sh: Problem encountered during Post process SPOMS Termsheets product ${prodTypeCde}. Please contact WMD Support." >> ${log_cloud_path}/${log_cloud_file}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to Post process SPOMS Termsheets product ${prodTypeCde} Interfaces." >> ${log_path}/${log_file}
fi

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Post process SPOMS Termsheets product ${prodTypeCde} Interface Done." >> ${log_path}/${log_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Post process SPOMS Termsheets product ${prodTypeCde} Interface Done." >> ${log_cloud_path}/${log_cloud_file}
#### program -- END ####
