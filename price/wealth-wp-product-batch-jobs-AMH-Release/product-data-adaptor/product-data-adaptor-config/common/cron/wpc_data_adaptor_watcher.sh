#!/usr/bin/ksh

#
# Created 19 Apr, 2017
#

if [[ $# -lt 3 ]]
then
    print "usage: wpc_data_adaptor.sh <ctry code> <organization code> <job code> <multi file support> <check_ack> <wait_time> <wait_repeats>"
    print "  <ctry code> valid value: "
    print "   -- TW or GB, atc"
    print "  <organization code> valid value: "
    print "   -- dummy, atc"
    print "  <job code> valid value: "
    print "   -- ABC.UT, ABC.BOND, atc"
    print "  <multi file support> it is not mandatory , default is Y valid value: "
    print "   -- Y - support multiple files"
    print "   -- N - do not support multiple files"
    print "  <check_ack> it is not mandatory , valid value: "
    print "   -- Y - check ack file"
    print "   -- N - no need check ack file"
    print "  <wait_time> valid value: "
    print "   -- 60, atc"
    print "  <wait_repeats> valid value: "
    print "   -- 10, atc"    
    print 'example: wpc_data_adaptor.sh TW dummy ABC.BOND N Y 900 17'
    exit
fi

# Input Parameters
typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u jobCde=$3
typeset -u multiSupport=$4
typeset -u checkack=$5
typeset -u waittime=$6
typeset -u waitrepeats=$7

parm="${ctryCde} ${orgnCde} ${jobCde} ${multiSupport} ${checkack} ${waittime} ${waitrepeats}"

#### Path and File Definition -- START ####
log_path="@adaptor.app.log.path@"
config_path="@adaptor.app.config.path@"
log_file="${ctryCde}${orgnCde}ADPPROC${jobCde}$(date +%Y%m%d).log"

log_cloud_path="@adaptor.app.cloud.log.path@"
log_cloud_file="wpc_${log_file}"
temp_log_file="temp_${log_file}"

echo "=========================================================" >> ${log_path}/${log_file}
echo "Hostname: `hostname`" >> ${log_path}/${log_file}
shellPara=$@
echo "Parameters: ${shellPara}" >> ${log_path}/${log_file}

#### program -- START ####
appjars="@adaptor.app.lib.path@"
for name in `ls ${appjars} | grep .jar`; do
 classpath=${classpath}${appjars}/$name:
done

classpath=${classpath}${config_path}

mainClass="com.dummy.wpc.datadaptor.EntryPoint"

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Generating Data Adaptor Output ${jobCde} Interface...." >> ${log_path}/${log_file}
chmod -f 764 ${log_path}/${log_file}

@java.path@ -classpath ${classpath} -Xmx512m -DconfigPath=${config_path} ${mainClass} ${parm} >> ${log_path}/${temp_log_file}

# read from temp file and write into both logs, then remove temp file
while read line
do
	echo $line >> ${log_path}/${log_file}
	echo $$" ${line}" >> ${log_cloud_path}/${log_cloud_file}
done < ${log_path}/${temp_log_file}
chmod -f 764 ${log_path}/${log_file}
chmod -f 764 ${log_cloud_path}/${log_cloud_file}
rm ${log_path}/${temp_log_file}

exit 0
 
#### program -- END ####
