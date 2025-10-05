#!/usr/bin/ksh

#
# Transfer the generated file to multiple server.
#

if [[ $# -ne 4 ]]
then
    print "usage: wpc_findoc_copy_to_s3.sh <country code> <organization code> <product type code> <file name>"
    print "example: wpc_findoc_copy_to_s3.sh HK HBAP ELI DCHKB0711265_FIN-DOC1_PFS pdf"
    exit -1
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u prodTypeCde=$3
typeset file=$4

script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
log_path=${LOG_FILE_PATH}
findoc_coming_path=${FINDOC_INCOMING_PATH}
log_file="${ctryCde}_${orgnCde}_wpc_findoc_copy_${prodTypeCde}_s3_$(date +%Y%m%d).log"
s3_path=${FINDOC_CP_S3_PATH}
copy_script=${FINDOC_CP_S3_SCRIPT}
prod_type_path=$(echo "$prodTypeCde" | tr '[:upper:]' '[:lower:]')

echo "aws cp command : aws s3 cp $findoc_coming_path/$ctryCde$orgnCde/$prodTypeCde/$file s3://$s3_path/$prod_type_path/$file $copy_script" >> ${log_path}/${log_file}
aws s3 cp $findoc_coming_path/$ctryCde$orgnCde/$prodTypeCde/$file s3://$s3_path/$prod_type_path/$file $copy_script >> ${log_path}/${log_file}
status=$?

if [[ $status -eq 0 ]]
          then
      echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") transferred interfaces with header file to S3 successfully." >> ${log_path}/${log_file}
else
      echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to transfer header file to S3. Please contact WMD Support" >> ${log_path}/${log_file}
      logger -t root "WPC1011E wpc_findoc_copy_to_s3.sh: $(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") failed to transfer to S3. Please contact WMD Support."
fi