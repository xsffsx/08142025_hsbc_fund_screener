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

if [[ $# -ne 3 ]]
then
    return 255
fi

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Executing ARC_ftp_req.sh ..." >> $logfile

localDirectory="$1"
cd "$localDirectory"

echo "$localDirectory" >> $logfile

ARC_USER=${ARC_FS_FINDOC_FTP_USER}
ARC_HOST=${ARC_FS_FINDOC_FTP_HOST}
ARC_PORT=${ARC_FS_FINDOC_FTP_PORT}

# create ack file
basefilename=`basename $3 .csv`
filename="$basefilename.csv"
ackfile="$basefilename.ack"
if [[ ! -f "$ackfile" ]]
then
    touch "$ackfile"
fi

batchfile="${findoc_incoming_arc_path}/arc_ftp_req_pdf.txt"

# Create batchfile
if [[ ! -f $batchfile ]]
then
	touch $batchfile
	chmod 770 $batchfile
fi

# Create sFTP batchfile
	echo "lcd $localDirectory" > $batchfile
	echo "cd $2" >> $batchfile
	echo "put $filename" >> $batchfile
	echo "quit" >> $batchfile

output=`/usr/bin/sftp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -oPort=$ARC_PORT -b $batchfile $ARC_USER@$ARC_HOST 2>&1`
sftp_result=$?

if (( $? != 0 || $sftp_result != 0 ))
then
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") FTP/SFTP Failed" >> $logfile
	logger -t root "OTPSERR_EBJ000 ARC_ftp_req.sh: FTP/SFTP Failed. Please contact WMD Support."
fi

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") To ARC" >> $logfile
echo "$output" >> $logfile
echo "$output"|grep -E "sftp> quit"
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") End sending cvs" >> $logfile

if (( $? == 0 && $sftp_result == 0 ))
then
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") $3 transferred successfully to ARC" >> $logfile

    batchfile2="${findoc_incoming_arc_path}/arc_ftp_req_ack.txt"
    # Create batchfile
    if [[ ! -f $batchfile2 ]]
    then
        touch $batchfile2
        chmod 770 $batchfile2
    fi

    # Create sFTP batchfile
        echo "lcd $localDirectory" > $batchfile2
        echo "cd $2" >> $batchfile2
        echo "put $ackfile" >> $batchfile2
        echo "quit" >> $batchfile2

    output2=`/usr/bin/sftp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -oPort=$ARC_PORT -b $batchfile2 $ARC_USER@$ARC_HOST 2>&1`
    sftp_result2=$?
    echo "$output2"|grep -E -q "sftp> quit"
    if (( $? != 0 || $sftp_result2 != 0 ))
    then
        echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") FTP/SFTP Failed" >> $logfile
        logger -t root "OTPSERR_EBJ000 ARC_ftp_req.sh: FTP/SFTP Failed. Please contact WMD Support."
    fi

    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") ACK To ARC" >> $logfile
    echo "$output2" >> $logfile
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") End sending ack" >> $logfile
    rm $ackfile

else
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") $3 failed to transfer to ARC" >> $logfile
fi

if [ "$sftp_result" == "0" ] && [ "$sftp_result2" == "0" ]
then
    return 0
else
    return 255
fi
