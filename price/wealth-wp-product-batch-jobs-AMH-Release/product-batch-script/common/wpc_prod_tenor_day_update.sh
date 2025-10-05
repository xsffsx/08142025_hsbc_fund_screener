#!/usr/bin/ksh

#
# Created 29 June, 2023
# Modified change file name
#
# wpc_prod_tenor_day_update.sh
# 
if [ $# -lt 3 ] || [ $# -gt 6 ] 
 then
    print "usage: wpc_prod_tenor_day_update.sh <country code> <organization code> <product type code list>(optional,split by comma)"
	print "  <country code> that is mandatory indicates country code, valid value: "
	print "   	SG,JP,TW."
	print "  <organization code> that is mandatory indicates organization code, valid value: "
	print "   	dummy,KCTR."
#	print "  <remainDayCount Or StatusCode Ind> is mandatory, valid value: "	
#	print "  	-D Term remain day cnt update."
#	print "  	-S Product Status Code Update, which is valid for the following product type code:"
#	print "  	   DPS,BOND,SEC,WRTS,UT,SID,ELI,SN"
#	print "  	-P Period Investment Tenor Number, which is updated base on Period Investment Code(i.e. Y/M/W/D),"
#	print "  	   and Term Remaining Day Count calculated by WPC."
	print "  <product type code list> that is optional(Manadatory for S, i.e. Product Status Update),valid value: "
	print "  	--ALL that is default value indicates all product type, however, ALL is not supported for Product Status Update."
	print "  	--DPS,BOND,SEC,WRTS,UT,SID,ELI,SN"
#	print "  <product status offset hour> that is optional, which denotes product status update offset hour,"
#	print "     default value is 0, which is mandatory if <product subtype code list> has value."
#	print "  <product subtype code list> that is optional, which denotes product sub type code of ELI, valild value:"
#	print "  	ELI_DCDC,ELI-RN"
	print " example: wpc_prod_tenor_day_update.sh HK dummy D 'UT,SEC,BOND'"
    exit
fi

if [[ $# -lt 3 ]]; then
    print "usage: wpc_prod_tenor_day_update.sh <country code> <organization code> <product type code list>"
    print "example: wpc_prod_tenor_day_update.sh HK HBAP ALL"
    print "example: wpc_prod_tenor_day_update.sh HK HBAP 'UT,SEC,BOND'"
    exit -1
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u prodType=$3
parm=$@
echo "Input parameters: ${parm}"

#### Path and File Definition -- START ####
script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
log_path=${LOG_FILE_PATH}
log_file="${ctryCde}_${orgnCde}_wpc_prod_tenor_day_update_$(date +%Y%m%d).log"

log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file="${log_file}"

config_path=${CONFIG_FILE_PATH}
#### Path and File Definition -- END ####

echo "=========================================================" >> ${log_path}/${log_file}
echo "Hostname: `hostname`" >> ${log_path}/${log_file}
shellPara=$@
echo "Parameters: ${shellPara}" >> ${log_path}/${log_file}
echo "=========================================================" >> ${log_cloud_path}/${log_cloud_file}
echo "Hostname: `hostname`" >> ${log_cloud_path}/${log_cloud_file}
echo "Parameters: ${shellPara}" >> ${log_cloud_path}/${log_cloud_file}
#### program -- START ####

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WPC prod tenor in days update job starting...." >> ${log_path}/${log_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WPC prod tenor in days update job starting...." >> ${log_cloud_path}/${log_cloud_file}
chmod -f 764 ${log_path}/${log_file}

temp_log_file=$$_"temp_${log_file}"

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml"
${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-product-tenor-day-update-job-2.0.0-SNAPSHOT.jar ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} prodTypeCde=${prodType} --spring.config.location=${springConfig} >> ${log_path}/${temp_log_file} 2>&1

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
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WPC prod tenor in days update job is done successfully." >> ${log_path}/${log_file}
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WPC prod tenor in days update job is done successfully." >> ${log_cloud_path}/${log_cloud_file}
else
    logger -t root "WPC0001E M AMH TDC wpc_prod_tenor_day_update.sh: Problem encountered when invoking WPC prod tenor in days update job. Please contact WMD Support."
    echo "WPC0001E M AMH TDC wpc_prod_tenor_day_update.sh: Problem encountered when invoking WPC prod tenor in days update job. Please contact WMD Support." >> ${log_cloud_path}/${log_cloud_file}
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failure invoke WPC prod tenor in days update job." >> ${log_path}/${log_file}
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failure invoke WPC prod tenor in days update job." >> ${log_cloud_path}/${log_cloud_file}
fi

#### WPC prod tenor in days update -- END ####