#!/usr/bin/ksh

if [[ $# -lt 3 ]]
then
   print "========================="
   print "usage: wpc_gen_treemap.sh <country code> <organization code> <request json file name>"
   print "                         "
   print "  example (with necessary param only): wpc_gen_treemap.sh SG HBAP RMP"
   exit
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u requestFile=$3

log_file="${ctryCde}_${orgnCde}_product_gen_treemap_${requestFile}_$(date +%Y%m%d).log"

#### Path and File Definition ####
script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
output_path="${ADAPTOR_OUTPUT_FILE_PATH}/Treemap/${ctryCde}"

${BATCH_SCRIPT_PATH}/wpc_thymeleaf_egress_job.sh ${ctryCde} ${orgnCde} ${requestFile} ${log_file}

# gzip csv file via a loop, begin
cd ${output_path}
chmod 775 ${ctryCde}${orgnCde}treemap*_*.csv

for files in `ls ${ctryCde}${orgnCde}treemap*_*.csv`
do
  gzFilName=${files##${ctryCde}${orgnCde}}.gz
  gzip -c ${files} > ${gzFilName}
done

chmod 775 treemap*_*.csv.gz
touch "treemap.ack"
chmod 775 treemap.ack
#### program -- END ####