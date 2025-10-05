#!/bin/ksh

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
hfiAckDir=$3
hfiDelAckFile=$4
# file path configure
script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
log_path=${LOG_FILE_PATH}
log_file="$log_path/${ctryCde}_${orgnCde}_FinDoc_HandlePwsAckDel_$(date +%Y%m%d).log"
logfile_wpc_pws_del_ack="$log_path/${ctryCde}_${orgnCde}_wpc_pws_del_ack_$(date +%Y%m%d).log"
if [[ ! -f $logfile_wpc_pws_del_ack ]]; then
    touch $logfile_wpc_pws_del_ack
	chmod -f 755 $logfile_wpc_pws_del_ack
fi
if [[ ! -f $log_file ]]; then
    touch $log_file
	chmod -f 755 $log_file
fi
chmod -f 755 $hfiDelAckFile
echo "=========================================================" >> $log_file
echo "Hostname: `hostname`" >> $log_file
shellPara=$@
echo "Parameters: $shellPara" >> $log_file
if [[ $# -ne 4 ]]; then
	echo "$(date +"[%H:%M:%S %d/%m/%Y]") Invalid input parameters for wpc_pws_del_ack.sh:  $shellPara. " >> $log_file
	exit -1
fi
echo "$(date +"[%H:%M:%S %d/%m/%Y]") Executing wpc_pws_del_ack.sh ..." >> $log_file
PWS_USER=${FS_FINDOC_FTP_USER}
PWS_HOST=${FS_FINDOC_FTP_HOST}
PWS_PORT=${FS_FINDOC_FTP_PORT}
# delete ack files from PWS server
batchfile="$OUTPUT_FILE_PATH/${ctryCde}${orgnCde}/wpc_pws_del_ack.txt"
# Create batchfile
if [[ ! -f $batchfile ]]; then
    touch $batchfile
    chmod 755 $batchfile
fi
# Create sFTP batchfile
echo "cd $hfiAckDir" > $batchfile
cat $hfiDelAckFile | while read line; do
	echo "rm $line" >> $batchfile
done
echo "quit" >> $batchfile
output=`/usr/bin/sftp -oPort=$PWS_PORT -b $batchfile $PWS_USER@$PWS_HOST 2>&1`
if [[ $? -ne 0 ]]; then
    echo "$(date +"[%H:%M:%S %d/%m/%Y]") SFTP Failed" >> $log_file
    logger -t root "WPC1009E wpc_pws_del_ack.sh: SFTP Failed. Please contact WMD Support."
fi
echo "$(date +"[%H:%M:%S %d/%m/%Y]") Delete from PWS" >> $log_file
echo "$output" > $logfile_wpc_pws_del_ack
echo "$output" >> $log_file 

# statistic process result
ackFileCnt=$(cat $hfiDelAckFile | wc -l)
processed=$(sed -n '/sftp> rm/p' $logfile_wpc_pws_del_ack | wc -l)
errCnt=$(sed -n '/Command failed/p' $logfile_wpc_pws_del_ack | wc -l) 
echo "Total ack file: $ackFileCnt" >> $log_file
echo "Processed: $processed" >> $log_file
echo "Failed: $errCnt" >> $log_file

## remove unused file
rm $logfile_wpc_pws_del_ack
rm $batchfile

if [[ $processed -eq $ackFileCnt && $errCnt -eq 0 ]]; then 
    echo "$(date +"[%H:%M:%S %d/%m/%Y]") $2 deleted successfully from PWS" >> $log_file
else
    echo "$(date +"[%H:%M:%S %d/%m/%Y]") $2 failed to delete from PWS" >> $log_file
   	exit -1
fi
