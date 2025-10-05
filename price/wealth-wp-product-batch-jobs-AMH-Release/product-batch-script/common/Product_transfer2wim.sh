#!/usr/bin/ksh

#
# Created 17 April, 2023
# By 44000051
#
# Transfer WIM interfaces to WIM system by SFTP
#

if [[ $# -ne 3 ]]
then
   print "usage: product_transfer2wim.sh <country code> <organization code> <consumer>"
   exit
fi

# Input Parameters
typeset -u ctry_cde=$1
typeset -u orgn_cde=$2
typeset -u consumer=$3

currentTime=$(date +"%Y%m%d")
#### Path and File Definition -- START ####
script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
log_path=${LOG_FILE_PATH}
log_file="${ctry_cde}_${orgn_cde}_product_transfer_${consumer}_${currentTime}.log"
local_path="${OUTPUT_FILE_PATH}/${ctry_cde}${orgn_cde}/${consumer}"
batchfile="${OUTPUT_FILE_PATH}/${ctry_cde}${orgn_cde}/${consumer}/${ctry_cde}_${orgn_cde}_batchfile_${currentTime}"

log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file=${log_file}


#### Enhance for support multiple entity start ####
FTP_USER=$(eval echo \$WIM_${ctry_cde}_FTP_USER)
FTP_HOST=$(eval echo \$WIM_${ctry_cde}_FTP_HOST)
FTP_PORT=$(eval echo \$WIM_${ctry_cde}_FTP_PORT)
remote_path=$(eval echo \$WIM_${ctry_cde}_FTP_PATH)
#### Enhance for support multiple entity end ####

#### Path and File Definition -- END ####

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transferring ${consumer} interfaces to WIM Server start...." >> ${log_path}/${log_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transferring ${consumer} interfaces to WIM Server start...." >> ${log_cloud_path}/${log_cloud_file}
echo "$FTP_USER" >> ${log_path}/${log_file}
echo "$FTP_HOST" >> ${log_path}/${log_file}
echo "$FTP_PORT" >> ${log_path}/${log_file}
echo "$remote_path" >> ${log_path}/${log_file}


echo "=========================================================" >> ${log_path}/${log_file}
echo "Hostname: `hostname`" >> ${log_path}/${log_file}
shellPara=$@
echo "Parameters: ${shellPara}" >> ${log_path}/${log_file}


file_count=$(ls ${local_path} | grep "_${currentTime}.csv$" | wc -l)
if [[ ${file_count} -eq 0 ]]
  then
#if [[ ! -f  $(find ${local_path}/ -type f | grep "_${currentTime}.csv$") ]]
#then
	echo "no available csv send to WIM" >> ${log_path}/${log_file}
	echo "no available csv send to WIM" >> ${log_cloud_path}/${log_cloud_file}
	exit
fi

files=$(ls ${local_path} | grep "_${currentTime}.csv$" | sort -n)
echo "file need to transfer:${files}">>${log_path}/${log_file}


 # generate batchfile for sftp
    if [[ ! -f $batchfile ]]
        then
            touch $batchfile
            chmod 764 $batchfile
    fi

    echo "lcd $local_path" > $batchfile
    echo "cd $remote_path" >> $batchfile
    echo "put -p *${currentTime}.csv" >> $batchfile
    echo "chmod 755 *${currentTime}.csv" >> $batchfile
    echo "ls -la *${currentTime}*" >> $batchfile
    echo "quit" >> $batchfile

    # perform sftp
    output=`/usr/bin/sftp -o StrictHostKeyChecking=no -oPort=$FTP_PORT -b $batchfile $FTP_USER@$FTP_HOST 2>&1`
    sftp_result=$?


    if [[ $sftp_result -eq 0 ]]
      then
       # log sftp result
       echo "$output" >> ${log_path}/${log_file}
       echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transferring file to ${consumer} server ${FTP_HOST} finish." >> ${log_path}/${log_file}

       echo $$" $output" >> ${log_cloud_path}/${log_cloud_file}
       echo $$" $(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transferring file to ${consumer} server ${FTP_HOST} finish." >> ${log_cloud_path}/${log_cloud_file}

       # log message to indicate file transfer successfully
       echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transferred interfaces file to ${consumer} server ${FTP_HOST} successfully." >> ${log_path}/${log_file}
       echo $$" $(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transferred interfaces file to ${consumer} server ${FTP_HOST} successfully." >> ${log_cloud_path}/${log_cloud_file}

	   cd ${local_path}
	   for name in `ls ${local_path} | grep "_${currentTime}.csv"`; do
 	     mv $name ${name}.bak
	   done
       rm $batchfile
    else
       # log message to indicate file transfer failure
       echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to transfer file to ${consumer} server: ${FTP_HOST}." >> ${log_path}/${log_file}
       echo $$" $(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") OTPSERR_EBJ000 Failed to transfer file to ${consumer} server: ${FTP_HOST}." >> ${log_cloud_path}/${log_cloud_file}
       logger -t root "OTPSERR_EBJ000 product_transfer2wim.sh: $(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to transfer file to ${consumer} server: ${FTP_HOST}. Please contact WMD Support."
       rm $batchfile
       exit -1
       fi

# read from temp file and write into both logs, then remove temp file
while read line
do
	echo $line >> ${log_path}/${log_file}
	echo $$" ${line}" >> ${log_cloud_path}/${log_cloud_file}
done
chmod -f 764 ${log_path}/${log_file}
chmod -f 764 ${log_cloud_path}/${log_cloud_file}

