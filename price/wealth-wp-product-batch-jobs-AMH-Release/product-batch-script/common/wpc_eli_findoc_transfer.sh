#!/usr/bin/ksh

#
# Transfer the generated file to multiple server.
#

if [[ $# -ne 4 ]]
then
    print "usage: wpc_eli_findoc_transfer.sh <country code> <organization code> <system code> <file name>"
    print "example: wpc_eli_findoc_transfer.sh HK HBAP ELI DCHKB0711265_FIN-DOC1_PFS.pdf"
    exit -1
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -l ctry_path=$1
typeset -u orgnCde=$2
typeset -u sysCde=$3
typeset file=$4

#### Path and File Definition -- START ####
script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
log_path=${LOG_FILE_PATH}
log_file="${ctryCde}_${orgnCde}_wpc_eli_findoc_transfer_${sysCde}_$(date +%Y%m%d).log"
local_path="${FINDOC_INCOMING_PATH}/${ctryCde}${orgnCde}/${sysCde}"
command_file="${local_path}/${ctryCde}_${orgnCde}_${sysCde}_${file}_COMMAND"

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transferring WPC ELI fin doc to ${sysCde} server ...." >> ${log_path}/${log_file}
chmod -f 764 ${log_path}/${log_file}

# FTP Account Setting
FTP_COUNT=$(eval echo \$${sysCde}_FINDOC_FTP_COUNT)

if [[ -z "$FTP_COUNT" || $FTP_COUNT -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") ${sysCde} system not support SFTP transfer." >> ${log_path}/${log_file}
   logger -t root "WPC1011E wpc_eli_findoc_transfer.sh: ${sysCde} system not support SFTP transfer. Please contact WMD Support."
   exit -1
fi

echo "=========================================================" >> ${log_path}/${log_file}
echo "Hostname: `hostname`" >> ${log_path}/${log_file}
shellPara=$@
echo "Parameters: ${shellPara}" >> ${log_path}/${log_file}

status=0

if [[ ! -f $command_file ]]
then
   touch $command_file
   chmod 764 $command_file
fi

loop_ind=0
inner_status=0
while [[ $loop_ind -lt $FTP_COUNT ]]
do
   let sftp_seq=loop_ind+1
   sftp_user=$(eval echo \$${sysCde}_FINDOC_FTP_USER)
   sftp_host=$(eval echo \$${sysCde}_FINDOC_FTP_HOST)
   sftp_port=$(eval echo \$${sysCde}_FINDOC_FTP_PORT)
   sftp_path=$(eval echo \$${sysCde}_FINDOC_FTP_PATH)
   
   echo "lcd $local_path" > $command_file
   echo "cd $sftp_path" >> $command_file
   echo "put -p ${file}" >> $command_file
   echo "chmod 755 ${file}" >> $command_file
   echo "ls -la ${file}" >> $command_file
   echo "quit" >> $command_file
   
   # perform sftp
   output=`/usr/bin/sftp -oPort=$sftp_port -b $command_file $sftp_user@$sftp_host 2>&1`
   sftp_result=$?
   
   # log sftp result
   echo "$output" >> ${log_path}/${log_file}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transferring interfaces to ${sysCde} server ${sftp_host} finish." >> ${log_path}/${log_file}
   
   if [[ $sftp_result -eq 0 ]]
   then
	  echo "lcd $local_path" > $command_file
	  echo "cd $sftp_path" >> $command_file
	  echo "put ${file}" >> $command_file
	  echo "chmod 755 ${file}" >> $command_file
	  # remove ls here, to avoid asynchrony in TRIS - Jadder 20130528
	  #echo "ls -la *${ctryCde}_${orgnCde}_${sysCde}_*_${seq_num}*" >> $command_file
	  echo "quit" >> $command_file
   
	  # perform sftp
	  output=`/usr/bin/sftp -oPort=$sftp_port -b $command_file $sftp_user@$sftp_host 2>&1`
	  sftp_result=$?

	  # log sftp result
	  echo "$output" >> ${log_path}/${log_file}
	  echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transferring header file to ${sysCde} server ${sftp_host} finish." >> ${log_path}/${log_file}

	  if [[ $sftp_result -eq 0 ]]
	  then
		 # log message to indicate file transfer successfully
		 echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") transferred interfaces with header file to ${sysCde} server ${sftp_host} successfully." >> ${log_path}/${log_file}
	  else
		 inner_status=1
		 # log message to indicate file transfer failure
		 echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to transfer header file to ${sysCde} server: ${sftp_host}." >> ${log_path}/${log_file}
		 logger -t root "WPC1011E wpc_eli_findoc_transfer.sh: $(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to transfer header file to ${sysCde} server: ${sftp_host}. Please contact WMD Support."
	  fi
   else
	  inner_status=1
	  # log message to indicate file transfer failure
	  echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to transfer interfaces to ${sysCde} server: ${sftp_host}." >> ${log_path}/${log_file}
	  logger -t root "WPC1011E wpc_eli_findoc_transfer.sh: $(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to transfer interfaces to ${sysCde} server: ${sftp_host}. Please contact WMD Support."
   fi
   
   (( loop_ind += 1 ))
done

if [[ ${inner_status} -eq 0 ]]
then
   mv ${local_path}/${file} "${local_path}/${file}.bak"
   # rename ack file
   #len=${#file}
   #ackFile="${file:0:len-4}.ack"
   #mv ${local_path}/${ackFile} "${local_path}/${ackFile}.bak"
else
   status=1
fi

rm $command_file

if [[ ${status} -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transfer WPC XML interfaces to ${sysCde} FIN DOC server successful." >> ${log_path}/${log_file}
else
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transfer WPC XML interfaces to ${sysCde} FIN DOC server failed." >> ${log_path}/${log_file}
   exit -1
fi