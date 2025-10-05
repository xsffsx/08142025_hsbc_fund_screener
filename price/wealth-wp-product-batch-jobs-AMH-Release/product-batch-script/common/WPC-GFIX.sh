#!/usr/bin/ksh
#
# Created 18 July, 2010
#
# Communicate with GFIX for General Price for Online Bond Trading
#

if [[ $# -lt 3 ]]
then
   print "usage: WPC-GFIX.sh <country code> <groupmember code> <action code> <startup mode[Optional]> <onServiceSecond for FF[Optional]>"
   print "Supported Action Code: startup, listener, subscribe, unsubscribe, end"
   print "If action code is startup, supported startup mode: 1, 2, M "
   print "If action code is listener, input the onServiceSecond for listener running time setup "
   print "example1: WPC-GFIX.sh HK HBAP startup 1"
   print "example2: WPC-GFIX.sh HK HBAP unsubscribe"
   print "example3: WPC-GFIX.sh HK HBAP listener 1 43200"
   exit
fi

# Input Parameters
ctryCde=$1
orgnCde=$2
actionCde=$3

script_path=$(dirname $0)
source ${script_path}/WPCEnvSetting.sh

#### Path and File Definition -- START ####
log_path=${LOG_FILE_PATH}

cd ${log_path}

instance=$4
log_file="wpc_gfix_${actionCde}_$(date +%Y%m%d)_${instance}.log"
config_path=${CONFIG_FILE_PATH}

batch_config_path="${BATCH_APP_PATH}/config"
middleware_in="wpc-middleware-config-in${instance}.xml"
middlwware_out="wpc-middleware-config-out.xml"

monitorInteval="${GFIX_LISTENER_MONITOR_INTERVAL}"
onServiceSecond="${GFIX_LISTENER_SERVICE_PERIOD}"

log_cloud_path=${LOG_FILE_CLOUD_PATH}
log_cloud_file="wpc_${log_file}"

#### program -- START ####
appjars=${JAR_FILE_PATH}
for name in `ls ${appjars} | grep .jar`; do
 classpath=${classpath}${appjars}/$name:
done
classpath=${classpath}${JDBC_LIB_PATH}:${config_path}


echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") start ...." >> ${log_path}/${log_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") start ...." >> ${log_cloud_path}/${log_cloud_file}
chmod -f go+r ${log_path}/${log_file}
chmod -f go+r ${log_cloud_path}/${log_cloud_file}

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml"

if [[ ${actionCde} == "startup" ]]
then
	echo "startup"
	if [[ $# -eq 4 ]]
		then
		if [[ ${instance} == "M" ]]
		then
			echo "startup master monitoring"
			echo "startup master monitoring" >> ${log_path}/${log_file}
			echo "startup master monitoring" >> ${log_cloud_path}/${log_cloud_file}
      param="ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} keepAliveTime=${onServiceSecond} monInterval=${monitorInteval} actionCde=masterM"
      echo "usage ${param}"
			${JAVA_PATH} -jar -Dspring.config.location=${springConfig} ${JAR_FILE_PATH}/product-batch-import-gfix-job-2.0.0-SNAPSHOT.jar ${param} >> ${log_path}/${log_file}  2>&1 &
		else
			echo "started up WPC-GFIX job"
			echo "started up WPC-GFIX job" >> ${log_path}/${log_file}
			echo "started up WPC-GFIX job" >> ${log_cloud_path}/${log_cloud_file}
			param="ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} keepAliveTime=${onServiceSecond} instance=${instance} middlewareConfig=${batch_config_path}/${middleware_in} actionCde=listener"
      echo "usage ${param}"
			${JAVA_PATH} -jar -Dspring.config.location=${springConfig} ${JAR_FILE_PATH}/product-batch-import-gfix-job-2.0.0-SNAPSHOT.jar ${param} >> ${log_path}/${log_file}  2>&1 &
			${script_path}/WPC-GFIX-LogMonitoring.sh $instance >> ${log_path}/${log_file}  2>&1
		fi

	fi
fi

if [[ ${actionCde} == "parallelRun" ]]
then
	echo "startup gfix parallelRun"
	echo "startup gfix parallelRun" >> ${log_path}/${log_file}
	echo "startup gfix parallelRun" >> ${log_cloud_path}/${log_cloud_file}
   param="ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} keepAliveTime=${onServiceSecond} monInterval=${monitorInteval} wpcLogPath=${WPC_LOG_FILE_PATH} actionCde=parallelRun"
   echo "usage ${param}"
	${JAVA_PATH} -jar -Dspring.config.location=${springConfig} ${JAR_FILE_PATH}/product-batch-import-gfix-job-2.0.0-SNAPSHOT.jar ${param} >> ${log_path}/${log_file}  2>&1 &
fi

if [[ ${actionCde} == "listener" ]]
then
   echo "listener"
   if [[ $# -ne 5 ]]
   then
      print "usage: WPC-GFIX.sh <country code> <group member code> listener <instance number> <onServicePeriod>"
      echo "usage: WPC-GFIX.sh <country code> <group member code> listener <instance number> <onServicePeriod>" >> ${log_cloud_path}/${log_cloud_file}
      exit
   fi
   param="ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} keepAliveTime=${5} instance=${instance} middlewareConfig=${batch_config_path}/${middleware_in} actionCde=${actionCde}"
   echo "usage ${param}"
   ${JAVA_PATH} -jar -Dspring.config.location=${springConfig} ${JAR_FILE_PATH}/product-batch-import-gfix-job-2.0.0-SNAPSHOT.jar ${param} >> ${log_path}/${log_file}  2>&1 &
   status=$?
fi

if [[ ${actionCde} == "unsubscribe" ]]
then
   echo "unsubscribe"
   if [[ $# -ne 3 ]]
   then
      print "usage: WPC-GFIX.sh <country code> <group member code> unsubscribe"
      echo "usage: WPC-GFIX.sh <country code> <group member code> unsubscribe" >> ${log_cloud_path}/${log_cloud_file}
      exit
   fi
   param="ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} action=2 middlewareConfig=${batch_config_path}/${middlwware_out} actionCde=unsubscribe"
   echo "usage ${param}"
   ${JAVA_PATH} -jar -Dspring.config.location=${springConfig} ${JAR_FILE_PATH}/product-batch-import-gfix-job-2.0.0-SNAPSHOT.jar ${param} >> ${log_path}/${log_file} 2>&1
   status=$?
fi

if [[ ${actionCde} == "subscribe" ]]
then
   echo "subscribe"
   if [[ $# -ne 3 ]]
   then
      print "usage: WPC-GFIX.sh <country code> <group member code> subscribe"
      echo "usage: WPC-GFIX.sh <country code> <group member code> subscribe" >> ${log_cloud_path}/${log_cloud_file}
      exit
   fi
   param="ctryRecCde=${ctryCde} grpMembrRecCde=${orgnCde} action=1 middlewareConfig=${batch_config_path}/${middlwware_out} actionCde=subscribe"
   echo "usage ${param}"
   ${JAVA_PATH} -jar -Dspring.config.location=${springConfig} ${JAR_FILE_PATH}/product-batch-import-gfix-job-2.0.0-SNAPSHOT.jar ${param} >> ${log_path}/${log_file} 2>&1
   status=$?
fi

echo $status

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") end ...." >> ${log_path}/${log_file}
echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") end ...." >> ${log_cloud_path}/${log_cloud_file}

#### program -- END ####
