#!/usr/bin/ksh

#
# Created 05 November, 2011
#
# Transfer Treemap interfaces to SC via SFTP
# 

if [[ $# -ne 3 ]]
then
   print "usage: wpc_transfer_multiple_treemap.sh <country code> <organization code> <system code>"
   exit
fi

# Input Parameters
typeset -u ctry_cde=$1
typeset -l ctry_path=$1
typeset -u orgn_cde=$2
typeset -u sys_cde=$3

#### Path and File Definition -- START ####
script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
log_path=${LOG_FILE_PATH}
log_file="${ctry_cde}_${orgn_cde}_product_transfer_multiple_treemap_${sys_cde}_$(date +%Y%m%d).log"
ack_file="treemap.ack"
local_path="${ADAPTOR_OUTPUT_FILE_PATH}/Treemap/${ctry_cde}"
batchfile="${local_path}/batchfile_$(date +%Y%m%d%H%M%S)"
#### Path and File Definition -- END ####
script_name=$(basename $0)
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transferring Treemap interfaces to ${sys_cde} ...." >> ${log_path}/${log_file}
chmod -f 764 ${log_path}/${log_file}

# FTP Account Setting
FTP_COUNT=$(eval echo \$${sys_cde}_${ctry_cde}_FTP_COUNT)

if [[ -z "$FTP_COUNT" || $FTP_COUNT -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") ${sys_cde} system not support SFTP transfer." >> ${log_path}/${log_file}
   logger -t root "OTPSERR_EBJ000 ${script_name}: ${sys_cde} system not support SFTP transfer. Please contact WMD Support."
   exit -1
fi
echo "=========================================================" >> ${log_path}/${log_file}
echo "Hostname: `hostname`" >> ${log_path}/${log_file}


echo "${local_path}/${ack_file}"

if [[ -f ${local_path}/${ack_file} ]]
then
	#before_count=`ls -l ${local_path}/*.csv.gz | wc -l`

	# generate batchfile for sftp
	if [[ ! -f $batchfile ]]
	then
		touch $batchfile
	fi
	loop_ind=0
    inner_status=0
    while [[ $loop_ind -lt $FTP_COUNT ]]
    do
       let sftp_seq=loop_ind+1
       sftp_user=$(eval echo \$${sys_cde}_${ctry_cde}_FTP_USER_$sftp_seq)
       sftp_host=$(eval echo \$${sys_cde}_${ctry_cde}_FTP_HOST_$sftp_seq)
       sftp_port=$(eval echo \$${sys_cde}_${ctry_cde}_FTP_PORT_$sftp_seq)
       sftp_path=$(eval echo \$${sys_cde}_${ctry_cde}_FTP_PATH_$sftp_seq)
	   
	   echo "lcd $local_path" > $batchfile
       echo "cd $sftp_path"  >> $batchfile
	   echo "put -p *.csv.gz" >> $batchfile
	   echo "pwd" >> $batchfile
	   echo "ls -la *$(date +%Y%m)*.csv.gz" >> $batchfile
	   echo "quit" >> $batchfile
       # perform sftp
	   output=`/usr/bin/sftp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -oPort=$sftp_port -b $batchfile $sftp_user@$sftp_host 2>&1` 
       sftp_result=$?
	   # log sftp result
	   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transferring to ${sftp_host} for ${ctry_cde}${orgn_cde} ${sys_cde}" >> ${log_path}/${log_file}
	   echo "$output" >> ${log_path}/${log_file}
	   if [[ $sftp_result -eq 0 ]]
	   then
	      echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transferring to ${sftp_host} for ${ctry_cde}${orgn_cde} ${sys_cde} successfully" >> ${log_path}/${log_file}
	   else
	      inner_status=1
		  echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to transfer the files to ${sys_cde} server: ${sftp_host}." >> ${log_path}/${log_file}
          logger -t root "OTPSERR_EBJ000 ${script_name}: $(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to transfer the files to ${sys_cde} server: ${sftp_host}. Please contact WMD Support."
	   fi
   
       (( loop_ind += 1 ))
    done
	if [[ ${inner_status} -eq 0 ]]
    then
       # log message to indicate file transfer successfully		
	    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") transferred to ${sys_cde} successfully." >> ${log_path}/${log_file}

		cd $local_path
		#rm -rf *.csv.gz
		rm -f ${ack_file}
		rm $batchfile
		
		for files in `ls treemap*.csv.gz`
		do
  			mv ${files} ${files}.bak
		done
		
		for files in `ls ${ctry_cde}${orgn_cde}treemap*.csv`
		do
  			mv ${files} ${files}.bak
			#Not to retain csv files after transfer
			rm -f ${files}
		done
    else
       echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Transfer WPC XML interfaces to ${sys_cde} server failed." >> ${log_path}/${log_file}
       exit -1
    fi
else
    echo "${ctry_cde}/${orgn_cde} Treemap interfaces generation process has not been completed, transferal process is terminated." >> ${log_path}/${log_file}
fi
