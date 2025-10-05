#!/bin/ksh
#
# Execute shell script with below format:
#
#    /SCRIPT_HOME/./smart_copy_to_s3.sh  <country code> <orgnCde> <sysCde>
#
# For example:
#
#    /SCRIPT_HOME/./smart_copy_to_s3.sh HK HBAP iHUB
#    44000051
#
###################################################################################################

SCRIPT_DIR=$(dirname $0)
source ${SCRIPT_DIR}/WPCEnvSetting.sh
BASE_DIR="${BATCH_DATA_PATH}"

if [[ $# -lt 3 ]]
then
    echo "usage: smart_copy_to_s3.sh <country code> <orgnCde> <sysCde>"
    echo "example: smart_copy_to_s3.sh HK HBAP IHUB"
    exit -1
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u sysCde=$3




# Environment Variables
SCRIPT_DIR="${BATCH_APP_PATH}"
DATE=`date -d tomorrow +%Y%m%d`
LOG_FILE_DIR="${LOG_FILE_PATH}"
SMART_PATH="${OUTPUT_FILE_PATH}/${ctryCde}${orgnCde}/${sysCde}"
LOG_FILE="smart_copy_to_s3_${sysCde}_${DATE}.log"

log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file="${LOG_FILE}"

if [[ ! -f ${LOG_FILE_DIR}/${LOG_FILE} ]]
then
    touch ${LOG_FILE_DIR}/${LOG_FILE}
    chmod 775 ${LOG_FILE_DIR}/${LOG_FILE}
fi
#
# Log message with timestamp
#
function log
{
  print "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") "$1
}


echo "Hostname: `hostname`" >> ${LOG_FILE_DIR}/${LOG_FILE}
echo "=========================================================" >> ${LOG_FILE_DIR}/${LOG_FILE}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Copying file from Smart EC2 to S3 bucket starting." >> ${LOG_FILE_DIR}/${LOG_FILE}

echo "Hostname: `hostname`" >> ${log_cloud_path}/${log_cloud_file}
echo "=========================================================" >> ${log_cloud_path}/${log_cloud_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Copying file from Smart EC2 to S3 bucket starting." >> ${log_cloud_path}/${log_cloud_file}
#
# copy the interface files to destination source folder.
#

filePattern="*${DATE}.csv"
echo "filePattern : ${filePattern}"
file_count=$(ls ${SMART_PATH}/${filePattern} | wc -l)
   if [[ ${file_count} -eq 0 ]]
   then
	echo "no available csv file to copy" >> ${LOG_FILE_DIR}/${LOG_FILE}
	echo "no available csv file to copy" >> ${log_cloud_path}/${log_cloud_file}
	exit
	else
	fileName=`ls ${SMART_PATH}/${filePattern}| sort -n`
	for file in $fileName; do
		echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Copying interfaces with header file start: ${file}" >> ${LOG_FILE_DIR}/${LOG_FILE}
		echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Copying interfaces with header file start: ${file}" >> ${log_cloud_path}/${log_cloud_file}
		echo "aws s3 cp ${file} ${S3_BUCKETNAME} --sse aws:kms --region ap-east-1" >> ${LOG_FILE_DIR}/${LOG_FILE}
		echo "aws s3 cp ${file} ${S3_BUCKETNAME} --sse aws:kms --region ap-east-1" >> ${log_cloud_path}/${log_cloud_file}
		aws s3 cp ${file} ${S3_BUCKETNAME} --sse aws:kms --region ap-east-1
		#rename the files after transferred
 		mv  ${file} ${file}.bak
		echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Copying interfaces with header file done: ${file}" >> ${LOG_FILE_DIR}/${LOG_FILE}
		echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Copying interfaces with header file done: ${file}" >> ${log_cloud_path}/${log_cloud_file}
done
fi


echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Copying file from Smart EC2 to S3 bucket end." >> ${LOG_FILE_DIR}/${LOG_FILE}
echo "=========================================================" >> ${LOG_FILE_DIR}/${LOG_FILE}

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Copying file from Smart EC2 to S3 bucket end." >> ${log_cloud_path}/${log_cloud_file}
echo "=========================================================" >> ${log_cloud_path}/${log_cloud_file}



