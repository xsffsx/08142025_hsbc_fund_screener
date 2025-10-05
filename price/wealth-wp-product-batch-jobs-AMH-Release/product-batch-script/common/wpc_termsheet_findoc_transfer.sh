#!/usr/bin/ksh
#
# Transfer the file to multiple server.
#

if [[ $# -ne 4 ]]; then
    print "usage: wpc_termsheet_findoc_transfer.sh <country code> <organization code> <system code> <tansferedFile name>"
    print "example: wpc_termsheet_findoc_transfer.sh HK HBAP ELI DCHKB0711265_FIN-DOC1_PFS pdf"
    exit -1
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u prodTypeCde=$3
typeset tansferedFile=$4

#### Path and tansferedFile Definition -- START ####
script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
log_path=${LOG_FILE_PATH}
log_file="${ctryCde}_${orgnCde}_wpc_termsheet_findoc_transfer_${prodTypeCde}_$(date +%Y%m%d).log"
local_path="${FINDOC_INCOMING_PATH}/${ctryCde}${orgnCde}/${prodTypeCde}"
command_file="${local_path}/${ctryCde}_${orgnCde}_${prodTypeCde}_${tansferedFile}_COMMAND"

log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file=${log_file}

chmod -f 777 ${log_path}/${log_file}
echo "$(date +[%H:%M:%S' '%d/%m/%Y]) Transferring WPC Termsheets fin doc to ${prodTypeCde} server ...." >> ${log_path}/${log_file}
echo "$(date +[%H:%M:%S' '%d/%m/%Y]) Transferring WPC Termsheets fin doc to ${prodTypeCde} server ...." >> ${log_cloud_path}/${log_cloud_file}

if [[ ! -f ${local_path}/${tansferedFile} ]];
then
  echo "${tansferedFile} doesn't exist, skip"
  echo "${tansferedFile} doesn't exist, skip" >> ${log_path}/${log_file}
  echo "${tansferedFile} doesn't exist, skip" >> ${log_cloud_path}/${log_cloud_file}
  exit 0
fi

#### check sftp access right
FTP_COUNT=$(eval echo \$${prodTypeCde}_FINDOC_FTP_COUNT)
if [[ -z "$FTP_COUNT" || $FTP_COUNT -eq 0 ]]; then
   echo "$(date +[%H:%M:%S' '%d/%m/%Y]) ${prodTypeCde} system not support SFTP transfer." >> ${log_path}/${log_file}
   echo "$(date +[%H:%M:%S' '%d/%m/%Y]) ${prodTypeCde} system not support SFTP transfer." >> ${log_cloud_path}/${log_cloud_file}
   logger -t root "WPC1011E wpc_termsheet_findoc_transfer.sh: ${prodTypeCde} system not support SFTP transfer. Please contact WMD Support."
   echo "WPC1011E wpc_termsheet_findoc_transfer.sh: ${prodTypeCde} system not support SFTP transfer. Please contact WMD Support" >>${log_cloud_path}/${log_cloud_file}
   exit -1
fi

echo "=========================================================" >> ${log_path}/${log_file}
echo "Hostname: `hostname`" >> ${log_path}/${log_file}

echo "=========================================================" >> ${log_cloud_path}/${log_cloud_file}
echo "Hostname: `hostname`" >>${log_cloud_path}/${log_cloud_file}
shellPara=$@
echo "Parameters: ${shellPara}" >> ${log_path}/${log_file}
echo "Parameters: ${shellPara}" >> ${log_cloud_path}/${log_cloud_file}
if [[ ! -f $command_file ]]; then
   touch $command_file
   chmod 755 $command_file
fi
status=0
loop_ind=0
inner_status=0
while [[ $loop_ind -lt $FTP_COUNT ]]
do
   sftp_user=$(eval echo \$${prodTypeCde}_FINDOC_FTP_USER)
   sftp_host=$(eval echo \$${prodTypeCde}_FINDOC_FTP_HOST)
   sftp_port=$(eval echo \$${prodTypeCde}_FINDOC_FTP_PORT)
   sftp_path=$(eval echo \$${prodTypeCde}_FINDOC_FTP_PATH)
   
   echo "lcd $local_path" > $command_file
   echo "cd $sftp_path" >> $command_file
   echo "put -p ${tansferedFile}" >> $command_file
   echo "chmod 755 ${tansferedFile}" >> $command_file
   echo "ls -la ${tansferedFile}" >> $command_file
   echo "quit" >> $command_file
   # perform sftp
   output=`/usr/bin/sftp -oPort=$sftp_port -b $command_file $sftp_user@$sftp_host 2>&1`
   inner_status=$?
   echo "$output" >> ${log_path}/${log_file}   
   if [[ $inner_status -eq 0 ]]; then
	   echo "$(date +[%H:%M:%S' '%d/%m/%Y]) Transferring WPC Termsheets fin doc to ${prodTypeCde} server ${sftp_host} successfully." >> ${log_path}/${log_file}
	   echo "$(date +[%H:%M:%S' '%d/%m/%Y]) Transferring WPC Termsheets fin doc to ${prodTypeCde} server ${sftp_host} successfully." >> ${log_cloud_path}/${log_cloud_file}
   else
	   inner_status=1
	   echo "$(date +[%H:%M:%S' '%d/%m/%Y]) Failed to transfer WPC Termsheets fin doc to ${prodTypeCde} server ${sftp_host}." >> ${log_path}/${log_file}
	   echo "$(date +[%H:%M:%S' '%d/%m/%Y]) Failed to transfer WPC Termsheets fin doc to ${prodTypeCde} server ${sftp_host}." >> ${log_cloud_path}/${log_cloud_file}
	   logger -t root "OTPSERR_EBJ000 wpc_termsheet_findoc_transfer.sh: $(date +[%H:%M:%S' '%d/%m/%Y]) Failed to transfer WPC Termsheets fin doc to ${prodTypeCde} server ${sftp_host}.. Please contact WMD Support."
	   echo "OTPSERR_EBJ000 wpc_termsheet_findoc_transfer.sh: $(date +[%H:%M:%S' '%d/%m/%Y]) Failed to transfer WPC Termsheets fin doc to ${prodTypeCde} server ${sftp_host}.. Please contact WMD Support.">>${log_cloud_path}/${log_cloud_file}
   fi
   (( loop_ind += 1 ))
done

if [[ ${inner_status} -ne 0 ]]; then
   status=1
fi

rm $command_file

if [[ ${status} -eq 0 ]]
then
  cd $local_path
  mv ${tansferedFile} ${tansferedFile}.bak
   echo "$(date +[%H:%M:%S' '%d/%m/%Y]) Transfer WPC XML interfaces to ${prodTypeCde} FIN DOC server successful." >> ${log_path}/${log_file}
   echo "$(date +[%H:%M:%S' '%d/%m/%Y]) Transfer WPC XML interfaces to ${prodTypeCde} FIN DOC server successful." >> ${log_cloud_path}/${log_cloud_file}
else
   echo "$(date +[%H:%M:%S' '%d/%m/%Y]) Transfer WPC XML interfaces to ${prodTypeCde} FIN DOC server failed." >> ${log_path}/${log_file}
   echo "$(date +[%H:%M:%S' '%d/%m/%Y]) Transfer WPC XML interfaces to ${prodTypeCde} FIN DOC server failed." >> ${log_cloud_path}/${log_cloud_file}
   exit -1
fi