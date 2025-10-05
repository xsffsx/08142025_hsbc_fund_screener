#!/usr/bin/ksh


# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2

#
script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh

log_path=${LOG_FILE_PATH}
logfile="${log_path}/FinDoc_HandleARCH$(date +%Y%m%d).log"
log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file="wpc_FinDoc_HandleARCH$(date +%Y%m%d).log"
findoc_incoming_arc_path=${FINDOC_INCOMING_PATH}/HKHBAP/ARC

if [[ ! -f "$logfile" ]]
then
    touch "$logfile"
fi

chmod -f go+r $logfile

echo "=========================================================" >> $logfile
echo "Hostname: `hostname`" >> $logfile
shellPara=$@
echo "Parameters: ${shellPara}" >> $logfile

echo "=========================================================" >> ${log_cloud_path}/${log_cloud_file}
echo "Hostname: `hostname`" >> ${log_cloud_path}/${log_cloud_file}
echo "Parameters: ${shellPara}" >> ${log_cloud_path}/${log_cloud_file}

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Executing ARC_list_ack.sh ..." >> $logfile
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Executing ARC_list_ack.sh ..." >> ${log_cloud_path}/${log_cloud_file}

run=`ps -f -A`
integer runtime=$(echo "$run"|grep -c "${script_path}/ARC_list_ack.sh")
#echo "$run"|grep -E "ARC_list_ack.sh" >> $logfile
if [[ $runtime -le 1 ]]
then

    ARC_ack_location=${findoc_incoming_arc_path}/in
    ARC_ack_done=${findoc_incoming_arc_path}/done
    cd $ARC_ack_location
    rm -f "$ARC_ack_location"/*

    remoteDirectory=${ARC_FS_FTP_REMOTE_PATH}

    ARC_prev_listing_file=${findoc_incoming_arc_path}/ARC_prev_listing.txt
    if [[ ! -f "$ARC_prev_listing_file" ]]
    then
        touch "$ARC_prev_listing_file"
        chmod 777 $ARC_prev_listing_file
    fi
    ARC_ack_listing_file=${findoc_incoming_arc_path}/ARC_ack_listing.txt
    touch "$ARC_ack_listing_file"
    chmod 777 $ARC_ack_listing_file

    ARC_compare_listing_file=${findoc_incoming_arc_path}/ARC_compare_listing.txt
    touch "$ARC_compare_listing_file"
    chmod 777 $ARC_compare_listing_file

    ARC_new_listing_file=${findoc_incoming_arc_path}/ARC_new_listing.txt
    touch "$ARC_new_listing_file"
    > $ARC_new_listing_file
    chmod 777 $ARC_new_listing_file

    ARC_USER=${ARC_FS_FINDOC_FTP_USER}
    ARC_HOST=${ARC_FS_FINDOC_FTP_HOST}
    ARC_PORT=${ARC_FS_FINDOC_FTP_PORT}

    batchfile="${findoc_incoming_arc_path}/arc_list_ack.txt"

    # Create batchfile
    if [[ ! -f $batchfile ]]
    then
        touch $batchfile
        chmod 770 $batchfile
    fi

        # Create sFTP batchfile
        echo "lcd $ARC_ack_location" > $batchfile
        echo "cd $remoteDirectory" > $batchfile
        #echo "prompt n" >> $batchfile
        echo "get 0H*" >> $batchfile
        echo "quit" >> $batchfile

    output=`/usr/bin/sftp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -oPort=$ARC_PORT -b $batchfile $ARC_USER@$ARC_HOST 2>&1`
    sftp_result=$?
    echo "$output"|grep -E -q "sftp>"
    if (( $? != 0 ))
    then
        echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") FTP/SFTP Failed" >> $logfile
        echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") FTP/SFTP Failed" >> ${log_cloud_path}/${log_cloud_file}
        logger -t root "OTPSERR_EBJ000 ARC_list_ack.sh: FTP/SFTP Failed. Please contact WMD Support."
        echo "OTPSERR_EBJ000 ARC_list_ack.sh: FTP/SFTP Failed. Please contact WMD Support." >> ${log_cloud_path}/${log_cloud_file}
    fi

    #ls -l $ARC_ack_location >> $logfile
    ls -l $ARC_ack_location > $ARC_ack_listing_file

    chmod 774 $ARC_ack_location/*.*

    integer cntline=0

    while read line
    do
      ((cntline+=1))
    done < $ARC_ack_listing_file

    #To adjust the counted first line information
    cntline=cntline-1

    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") List ARC directory" >> $logfile
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") List ARC directory" >> ${log_cloud_path}/${log_cloud_file}
    echo $output >> $logfile

    #integer recno=$(echo "$output"|grep -c "100%")

    #echo "Record retreived - $recno" >> $logfile
    echo "No of record in listing directory - ${cntline}" >> $logfile
    echo "No of record in listing directory - ${cntline}" >> ${log_cloud_path}/${log_cloud_file}

    echo "$output"|grep -E "sftp>"
    if (( $? != 0 ))
    then
        echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Ack File Transfer Error 1" >> $logfile
        echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Ack File Transfer Error 1" >> ${log_cloud_path}/${log_cloud_file}
        chmod -f go+r $ARC_prev_listing_file
        return 255
    fi

    awk '/.ack/ {print $9}' $ARC_ack_listing_file > $ARC_compare_listing_file
    rm $ARC_ack_listing_file

    integer cnt=0

    while read line1
    do
        aString[$cnt]=$(echo $line1)
        ((cnt+=1))
    done < $ARC_compare_listing_file

    integer cnt2=0

    while read line2
    do
        bString[$cnt2]=$(echo $line2)
        ((cnt2+=1))
    done < $ARC_prev_listing_file

    if [[ $cnt2 -eq 0 ]]
    then
        if [[ $cnt -ne 0 ]]
        then
            match=n
            cp $ARC_compare_listing_file $ARC_new_listing_file
        fi
    else
        match=y                               #Looping two arrays till not match found
        integer i=0
        until ((i==cnt))
        do
            integer display=0
            recmatch=y
            integer j=0
            until ((j==cnt2))
            do
                ((display=display+1))
                if [[ ${aString[$i]} = ${bString[$j]} ]]
                then break
                fi
                if [[ ${aString[$i]} < ${bString[$j]} ]]
                then recmatch=n
                break
                fi
                ((j=j+1))
                if ((j==cnt2))
                then recmatch=n
                fi
            done
            if [[ $recmatch = n ]]
            then
                echo "${aString[$i]}" >> $ARC_new_listing_file
                match=n
            fi
            ((i=i+1))
       done
    fi


    if [[ $match = n ]]
    then
        echo "Recieved ack" >> $logfile
        ls -l $ARC_ack_location >> $logfile
        echo "Processing ack" >> $logfile
        awk '{print $0}' $ARC_new_listing_file >> $logfile
        # Trigger HFIBatch program
        parm="ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} fileName=${ARC_new_listing_file}"

        temp_log_file=$$_"temp_${log_cloud_file}"

	springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml,${SPRING_BATCH_CONFIG_PATH}/product-finDoc-archive-ack-job.yml"
	${JAVA_PATH} -jar ${JAR_FILE_PATH}/product-finDoc-archive-ack-job-2.0.0-SNAPSHOT.jar ${parm} --spring.config.location=${springConfig} >> ${log_path}/${temp_log_file} 2>&1

	status=$?

	# read from temp file and write into both logs, then remove temp file
	while read line
	do
		echo $line >> $logfile
		echo $$" ${line}" >> ${log_cloud_path}/${log_cloud_file}
	done < ${log_path}/${temp_log_file}
	rm ${log_path}/${temp_log_file}

        if (( $status != 0 ))
        then
            echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Java program not started" >> $logfile
            echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Java program not started" >> ${log_cloud_path}/${log_cloud_file}
            logger -t root "WPC1010E ARC_list_ack.sh: Java program not started. Please contact WMD Support."
            echo "WPC1010E ARC_list_ack.sh: Java program not started. Please contact WMD Support." >> ${log_cloud_path}/${log_cloud_file}
        fi

        cp $ARC_compare_listing_file $ARC_prev_listing_file
        mv $ARC_ack_location/*.* $ARC_ack_done

    else
        cp $ARC_compare_listing_file $ARC_prev_listing_file
        echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") ARC_list_ack.sh completed -- Return code 0000" >> $logfile
        echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") ARC_list_ack.sh completed -- Return code 0000" >> ${log_cloud_path}/${log_cloud_file}
    fi

    chmod -f go+r $ARC_prev_listing_file

else
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") The previous process is still running." >> $logfile
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") The previous process is still running." >> ${log_cloud_path}/${log_cloud_file}
fi