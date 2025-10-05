#!/usr/bin/ksh
#
# Created 30 Sep, 2010
#
# message monitoring the Communication with GFIX for General Price for Online Bond Trading 
# 

# Input Parameters
monitorInteval="300"
loggerIndicator="Y"
typeset instance=$1

if [[ $# -ne 1 ]]
then
   print "usage: WPC-GFIX-LogMonitoring.sh <instance name>"
   exit
fi


#### program -- START ####
script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh
log_path=${LOG_FILE_PATH}
log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file="WPC-GFIX-LogMonitoring.log"

cd ${log_path}

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") log monitoring start ...."
sleep $monitorInteval
ls -l SMART_NON_RELIABLE_IN_${instance}.log
while ( [ `ps -ef | grep java | grep GFIXListenerBroker | grep -v grep | wc -l` -ne 0 ] )
do
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") log monitoring: SMART_NON_RELIABLE_IN_${instance}.log"
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") log monitoring: SMART_NON_RELIABLE_IN_${instance}.log" >> ${log_cloud_path}/${log_cloud_file}
   lastRecordTime=`tail -100 SMART_NON_RELIABLE_IN_${instance}.log | grep "35=" | tail -1 | awk '{print $2}'`
   lastRecordTime=$(date +""%Y"-"%m"-"%d" "${lastRecordTime}"")
   lastRecordTimeSecond=$(date +%s -d "$lastRecordTime")
   fileModifyTime=`ls -l SMART_NON_RELIABLE_IN_${instance}.log | awk '{print $8}'`
   fileModifyTime=$(date +""%Y"-"%m"-"%d" "${fileModifyTime}:59"")
   fileModifyTimeSecond=$(date +%s -d "$fileModifyTime")
   currentSecond=$(date +%s)
   lastRecordInterval=$(($currentSecond-$lastRecordTimeSecond-$monitorInteval))
   fileModifyInterval=$(($currentSecond-$fileModifyTimeSecond-$monitorInteval))

   if ( [ ${lastRecordInterval} -gt 0 ] || [ ${fileModifyInterval} -gt 0 ] )
   then
      if [[ ${loggerIndicator} = "Y" ]]
      then
         loggerIndicator="N"
         logger -t root "WPC1013E GFIX: no more message received, pls seek GFIX's reconnection and then resubscribe in WPC"
         echo "WPC1013E GFIX: no more message received, pls seek GFIX's reconnection and then resubscribe in WPC" >> ${log_cloud_path}/${log_cloud_file}
         echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WPC1013E GFIX: no more message received, pls seek GFIX's reconnection and then resubscribe in WPC"
      fi
   elif [[ $loggerIndicator = "N" ]]
   then
      loggerIndicator="Y"
      echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WPC1013E GFIX: more message received"
      echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WPC1013E GFIX: more message received" >> ${log_cloud_path}/${log_cloud_file}
   fi

   sleep $monitorInteval
done

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") log monitoring end ...."
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") log monitoring end ...." >> ${log_cloud_path}/${log_cloud_file}

#### program -- END ####
