#!/usr/bin/ksh 

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u filePath=$3

#### Configuration File ####

script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
log_path=${LOG_FILE_PATH}
logfile="${ctryCde}_${orgnCde}_FinDoc_HandlePWS_$(date +%Y%m%d).log"
log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file="wpc_${logfile}"

if [[ ! -f "${log_path}/${logfile}" ]]
then
    touch "${log_path}/${logfile}"
fi
chmod -f go+r  ${log_path}/${logfile}

echo "=========================================================" >> ${log_path}/${logfile}
echo "Hostname: `hostname`" >>  ${log_path}/${logfile}
shellPara=$@
echo "Parameters: ${shellPara}" >>  ${log_path}/${logfile}
echo "=========================================================" >> ${log_cloud_path}/${log_cloud_file}
echo "Hostname: `hostname`" >> ${log_cloud_path}/${log_cloud_file}
echo "Parameters: ${shellPara}" >> ${log_cloud_path}/${log_cloud_file}

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Executing wpc_pws_list_ack.sh ..." >>  ${log_path}/${logfile}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Executing wpc_pws_list_ack.sh ..." >>  ${log_cloud_path}/${log_cloud_file}

run=`ps -f -A`
 runtime=$(echo "$run"|grep -c "${script_path}/wpc_pws_list_ack.sh")

#echo "$run"|grep -E "wpc_pws_list_ack.sh" >>  ${log_path}/${logfile}

if [[ $runtime -le 1 ]]
then

    PWS_ack_location=${FINDOC_INCOMING_PATH}/${ctryCde}${orgnCde}/PWS/in
    cd $PWS_ack_location

    PWS_prev_listing_file=${FINDOC_INCOMING_PATH}/${ctryCde}${orgnCde}/PWS/PWS_prev_listing.txt
    if [[ ! -f "$PWS_prev_listing_file" ]]
    then       
        touch "$PWS_prev_listing_file"
        chmod 774 $PWS_prev_listing_file
    fi

    PWS_ack_listing_file=${FINDOC_INCOMING_PATH}/${ctryCde}${orgnCde}/PWS/PWS_ack_listing.txt
    touch "$PWS_ack_listing_file"	
    chmod 774 $PWS_ack_listing_file

    PWS_new_listing_file=${FINDOC_INCOMING_PATH}/${ctryCde}${orgnCde}/PWS/PWS_new_listing.txt
    touch "$PWS_new_listing_file"	
    chmod 774 $PWS_new_listing_file


	PWS_USER=${FS_FINDOC_FTP_USER}
	PWS_HOST=${FS_FINDOC_FTP_HOST}
	PWS_PORT=${FS_FINDOC_FTP_PORT}

    batchfile="$OUTPUT_FILE_PATH/${ctryCde}${orgnCde}/wpc_pws_list_ack.txt"

    # Create batchfile
    if [[ ! -f $batchfile ]]
    then
        touch $batchfile
        chmod 770 $batchfile
    fi

    # Create sFTP batchfile
    echo "cd /hk_HFI_ACK" > $batchfile
    #echo "prompt n" >> $batchfile
    echo "get *.000.*.ack" >> $batchfile
    echo "quit" >> $batchfile

    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") List PWS directory" >>  ${log_path}/${logfile}

    output=`/usr/bin/sftp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -oPort=$PWS_PORT -b $batchfile $PWS_USER@$PWS_HOST 2>&1`
    sftp_result=$?
    #echo $output >>  ${log_path}/${logfile}

    #echo "$output"|grep -E "^250 CWD command successful."

    #echo "$output"|grep -E -q "sftp>"

    if (( $? != 0 ))
    then
        echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") SFTP Failed" >> ${log_path}/${logfile}
        logger -t root "WPC1009E wpc_pws_list_ack.sh: SFTP Failed. Please contact WMD Support."
        echo "WPC1009E wpc_pws_list_ack.sh: SFTP Failed. Please contact WMD Support." >> ${log_cloud_path}/${log_cloud_file}
    fi

    ls -l $PWS_ack_location > $PWS_ack_listing_file

    awk '/.ack/ {print $9}' $PWS_ack_listing_file > $PWS_new_listing_file
    rm $PWS_ack_listing_file

     cnt=0

    while read line1
    do 
        aString[$cnt]=$(echo $line1)
        #echo ${aString[$cnt]} >>  ${log_path}/${logfile}
        ((cnt+=1))
    done < $PWS_new_listing_file   

     cnt2=0

    while read line2
    do 
        bString[$cnt2]=$(echo $line2)
        #echo ${bString[$cnt2]} >>  ${log_path}/${logfile}
        ((cnt2+=1))		
    done < $PWS_prev_listing_file
        
    if [[ $cnt2 -eq 0 ]]
    then 
        if [[ $cnt -ne 0 ]]
        then 
            match=n
        fi
    else 
        match=y      
         i=0
        until ((i==cnt))
        do
             j=0
            until ((j==cnt2))
            do 
                if [[ ${aString[$i]} = ${bString[$j]} ]];then
                    break
                fi	
                ((j=j+1))
                if ((j==cnt2))
                then match=n
                fi
            done
            if [[ $match = n ]];then
            break
            fi
            ((i=i+1))
        done
    fi

    echo "$match" >>  ${log_path}/${logfile}

    if [[ $match = n ]]
    then

# Trigger Batch program 
parm="ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} fileName=${filePath}"

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Handle PWS Acknowledgement service Begins.... " >> ${log_path}/${logfile}
chmod -f 764 ${log_path}/${logfile}

temp_log_file=$$_"temp_${logfile}"

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml,${SPRING_BATCH_CONFIG_PATH}/product-finDoc-release-ack-job.yml"
${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-finDoc-release-ack-job-2.0.0-SNAPSHOT.jar ${parm} --spring.config.location=${springConfig} >> ${log_path}/${temp_log_file} 2>&1

status=$?

# read from temp file and write into both logs, then remove temp file
while read line
do
	echo $line >> ${log_path}/${logfile}
	echo $$" ${line}" >> ${log_cloud_path}/${log_cloud_file}
done < ${log_path}/${temp_log_file}
rm ${log_path}/${temp_log_file}

		if (( $status != 0 ))
		then
			echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Java program not started" >> ${log_path}/${logfile}
			logger -t root "WPC1010E wpc_pws_list_ack.sh: Java program not started. Please contact WMD Support."
			echo "WPC1010E wpc_pws_list_ack.sh: Java program not started. Please contact WMD Support." >> ${log_cloud_path}/${log_cloud_file}
			exit -1

		fi

		cp $PWS_new_listing_file $PWS_prev_listing_file
		if [[ -d $PWS_ack_location ]]
		then
			cd $PWS_ack_location 
			rm *
		fi
	
	
	    else
        echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") PWS_list_ack.sh completed -- Return code 0000" >> ${log_path}/${logfile} 
        echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") PWS_list_ack.sh completed -- Return code 0000" >> ${log_cloud_path}/${log_cloud_file}
    fi

    chmod -f go+r $PWS_prev_listing_file

else
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") The previous process is still running." >> ${log_path}/${logfile}
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") The previous process is still running." >> ${log_cloud_path}/${log_cloud_file}
fi
#### Batch program -- END ####