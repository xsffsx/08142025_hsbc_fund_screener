#!/usr/bin/ksh

#
# Created 06 Sep, 2023
# Generate XML Output Interface - Digital Asset Currency products
# https://wpb-confluence.systems.example.com/display/WWS/product-batch-export-gold-xml-job
# 
if [ $# -lt 3 ]
then
    print "usage: wpc_gen_xml_dac.sh <country code> <organization code> <system code> <product status (optional)> <file type (optional)>"
    print "  <country code> country code, valid value: "
    print "   	CN, GB, SG"
    print "  <organization code> organization code, valid value: "
    print "   	dummy,HBAP,HBEU"
    print "  <system code> system code, valid value: "
    print "   	MDS, SRBP, WGHSS"
    print "  <product status (optional)> product status, valid value: "
    print "   	A, D, E, P, S, ALL, can support multiple status codes like 'A,P,S'"
    print "  <file type (optional)> file type, valid value: "
    print "   	FULLSET - include all matched products"
    print "   	DELTA - include updated products since last execution"
	  print " example: wpc_tokenized_gold_price_update.sh HK HBAP GSOPSD 'A,P,S' FULLSET"
    exit
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u sysCde=$3

if [[ $4 ]]
then
  typeset -u prodStatCde=$4
else
  prodStatCde="A,P,S"
fi

if [[ $5 ]]
then
  typeset -u fileType=$5
else
  fileType="FULLSET"
fi

parm="$ctryCde $orgnCde $sysCde $prodStatCde $fileType"
echo "Generate XML Output DAC Interface parameters: ${parm}"

#### Path and File Definition -- START ####
script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
log_path=${LOG_FILE_PATH}
log_file="${ctryCde}_${orgnCde}_wpc_gen_xml_${sysCde}_DAC_$(date +%Y%m%d).log"

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

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WPC Generate XML Output DAC Interface job starting...." >> ${log_path}/${log_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WPC Generate XML Output DAC Interface job starting...." >> ${log_cloud_path}/${log_cloud_file}
chmod -f 764 ${log_path}/${log_file}

temp_log_file=$$_"temp_${log_file}"

#### program -- START ####
springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml"
param="ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} systemCde=${sysCde} prodStatCde=${prodStatCde} fileType=${fileType}"
${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-batch-export-gold-xml-job-2.0.0-SNAPSHOT.jar ${param} --spring.config.location=${springConfig} >> ${log_path}/${temp_log_file} 2>&1

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
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WPC Generate XML Output DAC Interface job is done successfully." >> ${log_path}/${log_file}
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WPC Generate XML Output DAC Interface job is done successfully." >> ${log_cloud_path}/${log_cloud_file}
else
    logger -t root "WPC0001E M AMH TDC wpc_gen_xml_dac.sh: Problem encountered when invoking WPC Generate XML Output DAC Interface job. Please contact WMD Support."
    echo "WPC0001E M AMH TDC wpc_gen_xml_dac.sh: Problem encountered when invoking WPC Generate XML Output DAC Interface job. Please contact WMD Support." >> ${log_cloud_path}/${log_cloud_file}
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failure invoke WPC Generate XML Output DAC Interface job." >> ${log_path}/${log_file}
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failure invoke WPC Generate XML Output DAC Interface job." >> ${log_cloud_path}/${log_cloud_file}
fi

#### WPC Generate XML Output Interface job -- END ####