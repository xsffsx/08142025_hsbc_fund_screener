#!/usr/bin/ksh

#
# Created 28 July, 2023
# Tokenized Gold product price update job
# https://wpb-confluence.systems.example.com/pages/viewpage.action?pageId=2819382546
# 
if [ $# -lt 2 ]
then
    print "usage: wpc_tokenized_gold_price_update.sh <country code> <organization code>"
    print "  <country code> that is mandatory indicates country code, valid value: "
    print "   	SG,JP,TW."
    print "  <organization code> that is mandatory indicates organization code, valid value: "
    print "   	dummy,KCTR."
	  print " example: wpc_tokenized_gold_price_update.sh HK dummy"
    exit
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
parm=$@
echo "Input parameters: ${parm}"

#### Path and File Definition -- START ####
script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
log_path=${LOG_FILE_PATH}
log_file="${ctryCde}_${orgnCde}_wpc_tokenized_gold_price_update_$(date +%Y%m%d).log"

log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file="wpc_${log_file}"

config_path=${CONFIG_FILE_PATH}
#### Path and File Definition -- END ####

echo "=========================================================" >> ${log_path}/${log_file}
echo "Hostname: `hostname`" >> ${log_path}/${log_file}
shellPara=$@
echo "Parameters: ${shellPara}" >> ${log_path}/${log_file}
echo "=========================================================" >> ${log_cloud_path}/${log_cloud_file}
echo "Hostname: `hostname`" >> ${log_cloud_path}/${log_cloud_file}
echo "Parameters: ${shellPara}" >> ${log_cloud_path}/${log_cloud_file}

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WPC Tokenized Gold price update job starting...." >> ${log_path}/${log_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WPC Tokenized Gold price update job starting...." >> ${log_cloud_path}/${log_cloud_file}
chmod -f 764 ${log_path}/${log_file}

temp_log_file=$$_"temp_${log_file}"

#### program -- START ####
springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml,${SPRING_BATCH_CONFIG_PATH}/product-gold-price-update-job.yml"
${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-gold-price-update-job-2.0.0-SNAPSHOT.jar ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} --spring.config.location=${springConfig} >> ${log_path}/${temp_log_file} 2>&1

status=$?

# read from temp file and write into both logs, then remove temp file
while read line
do
    echo $line >> ${log_path}/${log_file}
    echo $$" ${line}" >> ${log_cloud_path}/${log_cloud_file}
done < ${log_path}/${temp_log_file}
rm ${log_path}/${temp_log_file}

if (( $status == 0 ))
then
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WPC Tokenized Gold price update job is done successfully." >> ${log_path}/${log_file}
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WPC Tokenized Gold price update job is done successfully." >> ${log_cloud_path}/${log_cloud_file}
fi

#### WPC Tokenized Gold price update job -- END ####