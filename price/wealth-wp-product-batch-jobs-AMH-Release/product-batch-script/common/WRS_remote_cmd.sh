#!/bin/ksh
if [[ $# -ne 2 ]]
then
    print "usage: WRS_remote_cmd.sh <country code> <organization code>"
    exit
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2

script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
log_path=${LOG_FILE_PATH}
# config_path=${CONFIG_FILE_PATH}
logfile="$log_path/${ctryCde}_${orgnCde}_WRS_remote_cmd_$(date +%Y%m%d).log"
chmod 775 $logfile
echo "=========================================================" >> $logfile
echo "Hostname: `hostname`" >> ${logfile}
shellPara=$@
echo "Parameters: ${shellPara}" >> ${logfile}

PWS_USER=${FS_FINDOC_FTP_USER}
PWS_HOST=${FS_FINDOC_FTP_HOST}
PWS_PORT=${FS_FINDOC_FTP_PORT}

# Create ssh batch
ssh -q -T -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -oPort=$PWS_PORT $PWS_USER@$PWS_HOST "/appvol/wrs/data/scripts/run.sh"
sftp_result3=$?
echo "$output3" >> ${logfile}

if (( $? != 0 || $sftp_result3 != 0 ))
then
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") SFTP Failed" >> ${logfile}
    logger -t root "WPC1010E PWS_ftp.sh: SFTP Failed. Please contact WMD Support."
fi
