#!/usr/bin/ksh

#
# Generate MD5 for outbound files to other systems
#

if [[ $# -ne 3 ]]
then
    print "usage: wpc_outgoing_MD5.sh <country code> <organization code> <system code>"
    print "example: wpc_outgoing_MD5.sh SG dummy MKD"
    exit -1
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u sysCde=$3

#### Path and File Definition -- START ####
script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
log_path=${LOG_FILE_PATH}
log_file="${ctryCde}_${orgnCde}_wpc_outgoing_MD5_${sysCde}_$(date +%Y%m%d).log"
local_path="${OUTPUT_FILE_PATH}/${ctryCde}${orgnCde}/${sysCde}"
#local_path="${OUTPUT_FILE_PATH}/${sysCde}"
output_path="${local_path}/MD5"
cmdMD5="md5sum"

log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file="wpc_${log_file}"
temp_log_file="temp_${log_file}"

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Outbound generating MD5 for WPC XML interfaces to ${sysCde} server ...." >> ${log_path}/${temp_log_file}

echo "=========================================================" >> ${log_path}/${temp_log_file}
shellPara=$@
echo "Parameters: ${shellPara}" >> ${log_path}/${temp_log_file}

header_exist="false"
status=0

for file in $(ls ${local_path} | grep ^${ctryCde}_${orgnCde}_${sysCde}_.*'[0-9]\{12\}'_'[0-9]\{1,5\}'.out$ | sort -n); do
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Generating MD5 for interfaces with header file: ${file}" >> ${log_path}/${temp_log_file}
    header_exist="true"
    seq_num=$(print ${file} | awk -F"_" '{print $NF}' | awk -F"." '{print $1}')
    inner_status=0
    
    filePattern="*${ctryCde}_${orgnCde}_${sysCde}_*_${seq_num}.xml"
	cd ${local_path}
	ls ${filePattern} > outgoingFile.txt

	for filename in $(cat outgoingFile.txt)
	do 
		md5_filename="${filename}.MD5"
		${cmdMD5} ${filename} | awk 'BEGIN{FS=" "} {print $1}'>  ${output_path}/${md5_filename}
		genMD5_result=$?
	
		if [[ $genMD5_result -eq 0 ]]
		then
			chmod 775 ${output_path}/${md5_filename}
			echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") MD5 file ${md5_filename} generated for interface file ${filename} to ${sysCde} server successfully" >> ${log_path}/${temp_log_file}
		
			cp ${filename} ${output_path}/${filename}
			chmod 775 ${output_path}/${filename}
			echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Move interfaces successfully" >> ${log_path}/${temp_log_file}
		else
			inner_status=1			
			# log message to indicate generate MD5 failure
			echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to generate MD5 file for interface file ${filename} to ${sysCde} server" >> ${log_path}/${temp_log_file}
			logger -t root "OTPSERR_ZDP001 wpc_outgoing_MD5.sh: $(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to generate MD5 file for interface file ${filename} to ${sysCde} server. Please contact WMD Support."
		fi
	done
	
	cp ${file} ${output_path}/${file}      
	chmod 775 ${output_path}/${file}
	mv ${file} ${file}.bak
	echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Move interfaces header file successfully" >> ${log_path}/${temp_log_file}

	if [[ ${inner_status} -eq 0 ]]
    then
        echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Move MD5, interfaces and header file for ${file} successfully" >> ${log_path}/${temp_log_file}
    else
       status=1
    fi
done

# read from temp file and write into both logs, then remove temp file
while read line
do
	echo $line >> ${log_path}/${log_file}
	echo $$" ${line}" >> ${log_cloud_path}/${log_cloud_file}
done < ${log_path}/${temp_log_file}
rm ${log_path}/${temp_log_file}
chmod -f 764 ${log_path}/${log_file}
chmod -f 764 ${log_cloud_path}/${log_cloud_file}


if [[ ${header_exist} = "false" ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") No header file found for generate MD5 for ${sysCde} server." >> ${log_path}/${log_file}
   echo $$" $(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") No header file found for generate MD5 for ${sysCde} server." >> ${log_cloud_path}/${log_cloud_file}
   logger -t root "OTPSERR_ZDP001 wpc_outgoing_MD5.sh: No header file found for generate MD5 for ${sysCde} server. Please contact WMD Support."
   exit -1
fi

if [[ ${status} -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Outbound generate MD5 of WPC XML interfaces to ${sysCde} server successfully." >> ${log_path}/${log_file}
   echo $$" $(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Outbound generate MD5 of WPC XML interfaces to ${sysCde} server successfully." >> ${log_cloud_path}/${log_cloud_file}
else
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Outbound generate MD5 of WPC XML interfaces to ${sysCde} server failed." >> ${log_path}/${log_file}
   echo $$" $(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Outbound generate MD5 of WPC XML interfaces to ${sysCde} server failed." >> ${log_cloud_path}/${log_cloud_file}
   exit -1
fi
