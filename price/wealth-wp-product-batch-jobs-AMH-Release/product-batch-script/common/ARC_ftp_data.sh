#!/bin/ksh


script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh

log_path=${LOG_FILE_PATH}
logfile="${log_path}/FinDoc_DocArchiveService$(date +%Y%m%d).log"
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

if [[ $# -ne 5 ]];
then
    return 255
fi

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Executing ARC_ftp_data.sh ..." >> $logfile

localDirectory="$3"
cd "$localDirectory"

echo "$localDirectory" >> $logfile

ARC_USER=${ARC_FS_FINDOC_FTP_USER}
ARC_HOST=${ARC_FS_FINDOC_FTP_HOST}
ARC_PORT=${ARC_FS_FINDOC_FTP_PORT}

integer cnt=0
ackfile[0]="0.0"
afilename[0]="0.0"
while read line
do
    afilename[$cnt]=$line
    basefilename=`basename $line .pdf`
    ackfile[$cnt]="$basefilename.ack"
    if [[ ! -f "${ackfile[$cnt]}" ]]
    then
        touch "${ackfile[$cnt]}"
    fi
    ((cnt=cnt+1))
done < $5

batchfile="${findoc_incoming_arc_path}/arc_ftp_data_pdf.txt"

# Create batchfile
if [[ ! -f $batchfile ]]
then
	touch $batchfile
	chmod 770 $batchfile
fi

# Create sFTP batchfile
echo "lcd $localDirectory" > $batchfile
echo "cd $4" >> $batchfile
#echo "prompt n" >> $batchfile
integer i=0
while (( $i < $cnt ))
do
    echo "put ${afilename[$i]}" >> $batchfile
    ((i=i+1))
done
echo "quit" >> $batchfile

echo "Start SFTP now......" >> $logfile

output=`/usr/bin/sftp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -oPort=$ARC_PORT -b $batchfile $ARC_USER@$ARC_HOST 2>&1`
sftp_result=$?
echo "$output"|grep -E -q "sftp> quit"
if (( $? != 0 || $sftp_result != 0 ))
then
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") FTP/SFTP Failed" >> $logfile
	logger -t root "WPC1011E ARC_ftp_data: FTP/SFTP Failed. Please contact WMD Support."
fi

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") To ARC" >> $logfile
echo "$output" >> $logfile

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") End sending pdf" >> $logfile

#integer recno=$(echo "$output"|grep -c "100%")

#echo "record no - $recno" >> $logfile
echo "count no - $cnt" >> $logfile
echo "$output"|grep -E "sftp> quit"
if (( $? == 0 ))
then
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") $5 transferred successfully to ARC" >> $logfile

    batchfile2="${findoc_incoming_arc_path}/arc_ftp_data_ack.txt"

    # Create batchfile
    if [[ ! -f $batchfile2 ]]
    then
        touch $batchfile2
        chmod 770 $batchfile2
    fi

    # Create sFTP batchfile
    echo "lcd $localDirectory" > $batchfile2
    echo "cd $4" >> $batchfile2
    #echo "prompt n" >> $batchfile2
    integer j=0
    while (( $j < $cnt ))
    do
        echo "put ${ackfile[$j]}" >> $batchfile2
        ((j=j+1))
    done
    echo "quit" >> $batchfile2

    output2=`/usr/bin/sftp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null  -oPort=$ARC_PORT -b $batchfile2 $ARC_USER@$ARC_HOST 2>&1`
    sftp_result2=$?
    echo "$output2"|grep -E -q "sftp> quit"

    if (( $? != 0 || $sftp_result2 != 0 ))
    then
        echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") FTP/SFTP Failed" >> $logfile
        logger -t root "OTPSERR_EBJ000 ARC_ftp_data.sh: FTP/SFTP Failed. Please contact WMD Support."
    fi

    echo "$output2" >> $logfile
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") End sending ack" >> $logfile

else
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") $5 OTPSERR_EBJ000 failed to transfer to ARC" >> $logfile
fi

rm ${ackfile[*]}
chmod -f go+r $5

if [ "$sftp_result" == "0" ] && [ "$sftp_result2" == "0" ]
then
    return 0
else
    return 255
fi

