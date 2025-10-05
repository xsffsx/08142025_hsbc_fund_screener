#!/usr/bin/ksh

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2


script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
logfile="${LOG_FILE_PATH}/product_transfer2ens_$(date +%Y%m%d).log"

log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file="product_transfer2ens_$(date +%Y%m%d).log"

local_path="${OUTPUT_FILE_PATH}/${ctryCde}${orgnCde}/ENS"
batchfile="${local_path}/product_transfer2ens_batch_$(date +%Y%m%d%H%M%S)"
remote_path="ICDHFI/INFOLDER"

if [[ ! -f "$logfile" ]]
then
	touch "$logfile"
	chmod 755 $logfile
fi

echo "=========================================================" >> $logfile
echo "Hostname: `hostname`" >> $logfile
shellPara=$@
echo "Parameters: ${shellPara}" >> $logfile
echo "=========================================================" >> ${log_cloud_path}/${log_cloud_file}
echo "Hostname: `hostname`" >> ${log_cloud_path}/${log_cloud_file}
echo "Parameters: ${shellPara}" >> ${log_cloud_path}/${log_cloud_file}

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transfer to ENS BEGIN..." >>$logfile
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transfer to ENS BEGIN..." >> ${log_cloud_path}/${log_cloud_file}

cd $local_path
for name in HFI*
do
	test -s $name
	if (( $? != 0 ))
	then
			rm -f $name
	fi
done

for name in HFIK*
do
	HFIM_NAME=HFIM${name#*HFIK}
	if test -e $HFIM_NAME
	then

	# Create sFTP batchfile
	echo "lcd $local_path" > $batchfile
	echo "cd $remote_path" >> $batchfile
	echo "put $HFIM_NAME" >> $batchfile
	echo "put $name" >> $batchfile
	echo "quit" >> $batchfile

	output=`/usr/bin/sftp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -oPort=${ENS_PORT} -b $batchfile ${ENS_USER}@${ENS_HOST} 2>&1` 
	sftp_result=$?
	echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") To ENS" >> $logfile
	echo "$output" >> $logfile

	if (( $sftp_result != 0 ))
	then
		echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") FTP/SFTP Failed" >> $logfile
		logger -t root "OTPSERR_EBJ000 product_transfer2ens FTP/SFTP Failed. Please contact WMD Support."
		echo "OTPSERR_EBJ000 product_transfer2ens FTP/SFTP Failed. Please contact WMD Support." >> ${log_cloud_path}/${log_cloud_file}
	else
		echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Succeed to submit file $name and $HFIM_NAME to ENS" >>$logfile
		echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Succeed to submit file $name and $HFIM_NAME to ENS" >>${log_cloud_path}/${log_cloud_file}
		rm $name
		rm $HFIM_NAME
	fi
	rm $batchfile
	fi

done

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transfer to ENS END" >>$logfile
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transfer to ENS END" >>${log_cloud_path}/${log_cloud_file}
#### program -- END ####
