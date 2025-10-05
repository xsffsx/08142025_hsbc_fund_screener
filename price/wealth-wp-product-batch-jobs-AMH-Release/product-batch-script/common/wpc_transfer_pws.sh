#!/bin/ksh

if [[ $# -ne 5 ]]
then
    print "usage: wpc_transfer_pws.sh <country code> <organization code> <document folder> <file server data directory> <file name>"
    exit
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2

#### define path and file -- START ####
script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
log_path=${LOG_FILE_PATH}
logfile="$log_path/${ctryCde}_${orgnCde}_fin_doc_release_$(date +%Y%m%d).log"
logfile_pws_ftp="$log_path/${ctryCde}_${orgnCde}_PWS_ftp$(date +%Y%m%d%H%M).log"
logfile_pws_ftp_ack="$log_path/${ctryCde}_${orgnCde}_PWS_ftp_ack$(date +%Y%m%d%H%M).log"
#### define path and file -- END ####

if [[ ! -f "$logfile" ]]
then
    touch "$logfile"
fi
chmod -f go+r $logfile

echo "=========================================================" >> $logfile
echo "Hostname: `hostname`" >> $logfile
shellPara=$@
echo "Parameters: ${shellPara}" >> $logfile

if [[ ! -f "$logfile_pws_ftp" ]]
then
    touch "$logfile_pws_ftp"
fi
chmod -f go+r $logfile_pws_ftp

if [[ ! -f "$logfile_pws_ftp_ack" ]]
then
    touch "$logfile_pws_ftp_ack"
fi
chmod -f go+r $logfile_pws_ftp_ack


chmod -f go+r $5

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Executing wpc_transfer_pws.sh ..." >> $logfile

cd $3
localdirectory="."

WRS_USER=${FS_FINDOC_FTP_USER}
WRS_PORT=${FS_FINDOC_FTP_PORT}
WRS_HOST=${FS_FINDOC_FTP_HOST}
cnt=0
while read line
do
    # Create ack file
    oldname[$cnt]=$line

    basefilename="$(echo $line | cut -d"." -f1).$(echo $line | cut -d"." -f2)"
    extName="$(echo $line | cut -d"." -f3)"

    PWSname="$(echo $basefilename | cut -d"." -f1).000.$(echo $basefilename | cut -d"." -f2)"

    filename[$cnt]="$PWSname.$extName"

    mv $line ${filename[$cnt]}

    ackfile[$cnt]="$PWSname.$extName.ack"

    if [[ ! -f "${ackfile[$cnt]}" ]]
    then
        touch "${ackfile[$cnt]}"
    fi

    ((cnt=cnt+1))
done < $5

echo ${filename[*]} >> $logfile 
echo ${ackfile[*]} >> $logfile

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") To PWS" >> $logfile
echo "USER:$WRS_USER, HOST:$WRS_HOST, PORT:$WRS_PORT" >> $logfile

batchfile="$OUTPUT_FILE_PATH/${ctryCde}${orgnCde}/${ctryCde}_${orgnCde}_pws_ftp_pdf.txt"

# Create batchfile
if [[ ! -f $batchfile ]]
then
    touch $batchfile
    chmod 775 $batchfile
fi

# Create sFTP batchfile
echo "lcd $3" > $batchfile
echo "cd $4" >> $batchfile
i=0
until ((i==cnt))
do
    echo "put ${filename[$i]}" >> $batchfile
    ((i=i+1))
done
echo "quit" >> $batchfile

output=`/usr/bin/sftp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -oPort=$WRS_PORT -b $batchfile $WRS_USER@$WRS_HOST`
sftp_result=$?
echo "$output" > $logfile_pws_ftp 
echo "$output" >> $logfile

#echo "$output"|grep -E -q "^sftp>"
#if (( $? != 0 ))

recno=0
recno_ack=0

#recno=$(sed -n '/100%/p' $logfile_pws_ftp | wc -l) 

#if [ "$recno" != "$cnt" ]
echo "$output"|grep -E "sftp> quit"
if (( $? != 0 || $sftp_result != 0 ))  
then
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") SFTP Failed" >> $logfile
    logger -t root "WPC1011E wpc_transfer_pws.sh: SFTP Failed. Please contact WMD Support."
else
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") ACK To PWS" >> $logfile

    batchfile2="$OUTPUT_FILE_PATH/${ctryCde}${orgnCde}/${ctryCde}_${orgnCde}_pws_ftp_ack.txt"

    # Create batchfile
    if [[ ! -f $batchfile2 ]]
    then
        touch $batchfile2
        chmod 775 $batchfile2
    fi

    # Create sFTP batchfile
    echo "lcd $3" > $batchfile2
    echo "cd $4" >> $batchfile2
    i=0
    until ((i==cnt))
    do
       echo "put ${ackfile[$i]}" >> $batchfile2
       ((i=i+1))
    done
    echo "quit" >> $batchfile2

    output2=`/usr/bin/sftp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -oPort=$WRS_PORT -b $batchfile2 $WRS_USER@$WRS_HOST`
    sftp_result2=$?
    echo "$output2" > $logfile_pws_ftp_ack
    echo "$output2" >> $logfile

    #echo "$output2"|grep -E -q "sftp> quit"
    #if (( $? != 0 ))

    #recno_ack=$(sed -n '/100%/p' $logfile_pws_ftp_ack | wc -l) 
    echo "$output"|grep -E "sftp> quit"
    if (( $? != 0 || $sftp_result2 != 0 ))
    then
        echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") SFTP Failed" >> $logfile
        logger -t root "WPC1011E wpc_transfer_pws.sh: SFTP Failed. Please contact WMD Support."
    fi
fi

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") trigger WRS cmd" >> ${logfile}
${script_path}/WRS_remote_cmd.sh ${ctryCde} ${orgnCde}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") trigger WRS cmd done" >> ${logfile}

# Remove all created ack and rename the files
i=0
until ((i==cnt))
do
    mv ${filename[$i]} ${oldname[$i]} 
    ((i=i+1))
done

rm ${ackfile[*]}
rm $logfile_pws_ftp
rm $logfile_pws_ftp_ack

if [ "$sftp_result" == "0" ] && [ "$sftp_result2" == "0" ]
then
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully transfer financial document to file server." >> ${logfile}
    exit 0
else
    logger -t root "WPC1013E wpc_transfer_pws.sh: Problem encountered during transfer financial document to file server for ${ctryCde} ${sysCde}. Please contact WMD Support."
    echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to transfer financial document to file server." >> ${logfile}
    exit -1
fi
