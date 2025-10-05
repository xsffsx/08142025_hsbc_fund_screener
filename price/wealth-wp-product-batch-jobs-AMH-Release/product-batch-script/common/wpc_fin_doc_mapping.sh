#!/usr/bin/ksh

#
# Created 26 Jun, 2023
#
# Financial Document Mapping Service
# 

if [[ $# -lt 2 ]]
then
   print "usage: wpc_fin_doc_mapping.sh <country code> <groupmember code> <create Ack file indicator (optional:N/Y)>"
   print "create Ack file indicator: if N, means the ack file need to create manually;if Y, means the ack file will be created by progam automatically"
   print "if the create Ack file indicator is not defined, default value 'N' will be used for this indicator"
   print "example1: wpc_fin_doc_mapping.sh TW dummy"
   print "example2: wpc_fin_doc_mapping.sh GB HBEU Y"
   exit
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u crtAckIndicator=$3

#### Path and File Definition -- START ####
script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
log_path=${LOG_FILE_PATH}
log_file="${ctryCde}_${orgnCde}_fin_doc_mapping_$(date +%Y%m%d).log"
incoming_path=${FINDOC_INCOMING_PATH}

log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file="wpc_${log_file}"

chkFilePath="${BATCH_DATA_PATH}/FinDoc/req/chk"
chkFilePatter="${ctryCde}${orgnCde}_MAP.WPCE20.D*.T*.xls"
#### Path and File Definition -- END ####

echo "=========================================================" >> ${log_path}/${log_file}
echo "Hostname: `hostname`" >> ${log_path}/${log_file}
shellPara=$@
echo "Parameters: ${shellPara}" >> ${log_path}/${log_file}
echo "=========================================================" >> ${log_cloud_path}/${log_cloud_file}
echo "Hostname: `hostname`" >> ${log_cloud_path}/${log_cloud_file}
echo "Parameters: ${shellPara}" >> ${log_cloud_path}/${log_cloud_file}
#### WPCFinDocBatch program -- START ####

#to check whether the NAS is mounted normally
if [[ ! -d ${incoming_path} ]]
then
   logger -t root "WPC1013E wpc_fin_doc_mapping.sh: The interface incoming directory[${incoming_path}] not exist. Please contact WMD Support."
   echo "WPC1013E wpc_fin_doc_mapping.sh: The interface incoming directory[${incoming_path}] not exist. Please contact WMD Support." >> ${log_cloud_path}/${log_cloud_file}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") The interface incoming directory[${incoming_path}] not exist." >> ${log_path}/${log_file}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") The interface incoming directory[${incoming_path}] not exist." >> ${log_cloud_path}/${log_cloud_file}
   exit -1
fi

#if the create Ack file indicator = 'Y', create the corresponding ack file for the files with prefix "${ctryCde}${orgnCde}_MAP."
if [[ ! -n "$crtAckIndicator" ]]
then
    crtAckIndicator="N"
fi

if [[ $crtAckIndicator == "Y" ]]
then

	for incomingFile in `ls ${incoming_path}/${ctryCde}${orgnCde}_MAP.*.xls | grep ${ctryCde}${orgnCde}_MAP.`; do

		ackFile=${incomingFile/.xls/.ack}
		#echo "ackFile is ${ackFile}"
		if [[ ! -f "${ackFile}" ]]
		then
			touch "${ackFile}"
			chmod 775 ${ackFile}
		fi	
	done
	for incomingFile in `ls ${incoming_path}/${ctryCde}${orgnCde}_MAP.*.xlsx | grep ${ctryCde}${orgnCde}_MAP.`; do

		ackFile=${incomingFile/.xlsx/.ack}
		#echo "ackFile is ${ackFile}"
		if [[ ! -f "${ackFile}" ]]
		then
			touch "${ackFile}"
			chmod 775 ${ackFile}
		fi
	done
fi
	
## check if the excel is empty or only has header rows
for file in `find $incoming_path -maxdepth 1 \( -name "${ctryCde}${orgnCde}_MAP.WPCE20.D*.T*.xls" -o -name "${ctryCde}${orgnCde}_MAP.WPCE20.D*.T*.xlsx" \) -size -4700c`; do
	echo "$file is empty only move it to path: $chkFilePath" >> ${log_path}/${log_file}
	cp -f $file $chkFilePath
	baseFileName=`basename $file`
	chmod 755 $chkFilePath/$baseFileName
	rm -f $file
	rm -f ${file/.xls*/.ack}	
done

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Financial Document Mapping Begins.... " >> ${log_path}/${log_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Financial Document Mapping Begins.... " >> ${log_cloud_path}/${log_cloud_file}
chmod -f 764 ${log_path}/${log_file}
chmod -f 764 ${log_cloud_path}/${log_cloud_file}

temp_log_file=$$_"temp_${log_file}"

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml,${SPRING_BATCH_CONFIG_PATH}/product-batch-import-finDoc-mapping-job.yml"
${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-batch-import-finDoc-mapping-job-2.0.0-SNAPSHOT.jar ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} --spring.config.location=${springConfig} >> ${log_path}/${temp_log_file} 2>&1

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
        logger -t root "WPC1010E wpc_fin_doc_mapping.sh: Java program not started. Please contact WMD Support."
        echo "WPC1010E wpc_fin_doc_mapping.sh: Java program not started. Please contact WMD Support." >> ${log_cloud_path}/${log_cloud_file}
        exit -1

fi

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Financial Document Mapping End " >> ${log_path}/${log_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Financial Document Mapping End " >> ${log_cloud_path}/${log_cloud_file}

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Begin Housekeep......" >> ${log_path}/${log_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Begin Housekeep......" >> ${log_cloud_path}/${log_cloud_file}
find $chkFilePath -name $chkFilePatter -mtime +15 -exec rm {} \;
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Housekeep END." >> ${log_path}/${log_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Housekeep END." >> ${log_cloud_path}/${log_cloud_file}

#### WPCFinDocBatch program -- END ####
