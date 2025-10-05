#!/usr/bin/ksh

#
# Transfer the generated XML interface file to multiple server.
#

if [[ $# -lt 3 ]]
then
    print "usage: wpc_xml_multi_transfer.sh <country code> <organization code> <system code> <md5 (optional)>"
    print "example: wpc_xml_multi_transfer.sh HK HBAP GSOPS"
    exit -1
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -l ctry_path=$1
typeset -u orgnCde=$2
typeset -u sysCde=$3

#### Added md5 for audit itme enhancement ####
typeset -u md5=""

if [[ $4 ]]
then
   typeset -u md5=$4
fi

if [[ ${md5} = "MD5" ]]
then
   echo "With MD5 check"
else
   typeset -u md5=""
fi

#### Path and File Definition -- START ####
script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
log_path=${LOG_FILE_PATH}
log_file="${ctryCde}_${orgnCde}_wpc_xml_multi_transfer_${sysCde}_$(date +%Y%m%d).log"
local_path="${OUTPUT_FILE_PATH}/${ctryCde}${orgnCde}/${sysCde}"

log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file="wpc_${log_file}"

if [[ ${md5} = "MD5" ]]
then
   local_path="${local_path}/${md5}"
fi

command_file="${local_path}/${ctryCde}_${orgnCde}_${sysCde}_COMMAND"

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transferring WPC XML interfaces to ${sysCde} server ...." >> ${log_path}/${log_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transferring WPC XML interfaces to ${sysCde} server ...." >> ${log_cloud_path}/${log_cloud_file}
chmod -f 764 ${log_path}/${log_file}
chmod -f 764 ${log_cloud_path}/${log_cloud_file}

# FTP Account Setting
FTP_COUNT=$(eval echo \$${sysCde}_${ctryCde}_FTP_COUNT)

if [[ -z $FTP_COUNT ]]
then
    FTP_COUNT=$(eval echo \$${sysCde}_FTP_COUNT)
fi

if [[ -z "$FTP_COUNT" || $FTP_COUNT -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") ${sysCde} system not support SFTP transfer." >> ${log_path}/${log_file}
   logger -t root "OTPSERR_EBJ000 wpc_xml_multi_transfer.sh: ${sysCde} system not support SFTP transfer. Please contact WMD Support."
   echo "OTPSERR_EBJ000 wpc_xml_multi_transfer.sh: ${sysCde} system not support SFTP transfer. Please contact WMD Support." >> ${log_cloud_path}/${log_cloud_file}
   exit -1
fi

echo "=========================================================" >> ${log_path}/${log_file}
echo "Hostname: `hostname`" >> ${log_path}/${log_file}
shellPara=$@
echo "Parameters: ${shellPara}" >> ${log_path}/${log_file}
echo "=========================================================" >> ${log_cloud_path}/${log_cloud_file}
echo "Hostname: `hostname`" >> ${log_cloud_path}/${log_cloud_file}
echo "Parameters: ${shellPara}" >> ${log_cloud_path}/${log_cloud_file}

header_exist="false"
status=0
headerFileIndex=0
for file in $(ls ${local_path} | grep ^${ctryCde}_${orgnCde}_${sysCde}_.*'[0-9]\{12\}'_'[0-9]\{1,5\}'.out$ | sort -n); do
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transferring interfaces with header file: ${file}" >> ${log_path}/${log_file}
    header_exist="true"
    fileArray[$headerFileIndex]=$file
    seq_num=$(print ${file} | awk -F"_" '{print $NF}' | awk -F"." '{print $1}')
	
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
       sftp_user=$(eval echo \$${sysCde}_FTP_USER_$sftp_seq)
       sftp_host=$(eval echo \$${sysCde}_FTP_HOST_$sftp_seq)
       sftp_port=$(eval echo \$${sysCde}_FTP_PORT_$sftp_seq)
       sftp_path=$(eval echo \$${sysCde}_FTP_PATH_$sftp_seq)

       echo "lcd $local_path" > $command_file
       echo "cd $sftp_path" >> $command_file
       echo "put -p *${ctryCde}_${orgnCde}_${sysCde}_*_${seq_num}*.xml" >> $command_file
       echo "chmod 755 *${ctryCde}_${orgnCde}_${sysCde}_*_${seq_num}*.xml" >> $command_file
	   filename="*${ctryCde}_${orgnCde}_${sysCde}_*_${seq_num}*.xml"
       echo "ls -la *${ctryCde}_${orgnCde}_${sysCde}_*_${seq_num}*" >> $command_file
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
          echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transferring header file to ${sysCde} server ${sftp_host} finish." >> ${log_cloud_path}/${log_cloud_file}

          if [[ $sftp_result -eq 0 ]]
          then
             # log message to indicate file transfer successfully
             echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") transferred interfaces with header file to ${sysCde} server ${sftp_host} successfully." >> ${log_path}/${log_file}
             echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") transferred interfaces with header file to ${sysCde} server ${sftp_host} successfully." >> ${log_cloud_path}/${log_cloud_file}
          else
             inner_status=1
             # log message to indicate file transfer failure
             echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to transfer header file to ${sysCde} server: ${sftp_host}." >> ${log_path}/${log_file}
             logger -t root "OTPSERR_EBJ000 wpc_xml_multi_transfer.sh: $(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to transfer header file to ${sysCde} server: ${sftp_host}. Please contact WMD Support."
             echo "OTPSERR_EBJ000 wpc_xml_multi_transfer.sh: $(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to transfer header file to ${sysCde} server: ${sftp_host}. Please contact WMD Support." >> ${log_cloud_path}/${log_cloud_file}
          fi
       else
          inner_status=1
          # log message to indicate file transfer failure
          echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to transfer interfaces to ${sysCde} server: ${sftp_host}." >> ${log_path}/${log_file}
          logger -t root "OTPSERR_EBJ000 wpc_xml_multi_transfer.sh: $(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to transfer interfaces to ${sysCde} server: ${sftp_host}. Please contact WMD Support."
          echo "OTPSERR_EBJ000 wpc_xml_multi_transfer.sh: $(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to transfer interfaces to ${sysCde} server: ${sftp_host}. Please contact WMD Support." >> ${log_cloud_path}/${log_cloud_file}
       fi

       (( loop_ind += 1 ))
    done

    if [[ ${inner_status} -eq 0 ]]
    then
       mv ${local_path}/${file} "${local_path}/${file}.bak" 
       ls $local_path/$filename | while read line; do mv $line $line.bak; done;
    else
       status=1
    fi
	(( headerFileIndex += 1 ))
    rm $command_file
done

if [[ ${header_exist} = "false" ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") No header file found for transfer to ${sysCde} server." >> ${log_path}/${log_file}
   logger -t root "OTPSERR_EBJ000 wpc_xml_transfer.sh: No header file found for transfer to ${sysCde} server. Please contact WMD Support."
   echo "OTPSERR_EBJ000 wpc_xml_transfer.sh: No header file found for transfer to ${sysCde} server. Please contact WMD Support." >> ${log_cloud_path}/${log_cloud_file}
   exit -1
fi

if [[ ${status} -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transfer WPC XML interfaces to ${sysCde} server successful." >> ${log_path}/${log_file}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transfer WPC XML interfaces to ${sysCde} server successful." >> ${log_cloud_path}/${log_cloud_file}
else
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transfer WPC XML interfaces to ${sysCde} server failed." >> ${log_path}/${log_file}
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transfer WPC XML interfaces to ${sysCde} server failed." >> ${log_cloud_path}/${log_cloud_file}
   exit -1
fi

#### program -- END ####