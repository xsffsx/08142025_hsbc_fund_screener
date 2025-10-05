#!/usr/bin/ksh

#
# Created 20 Oct, 2022
#
# the cycle job for Adaptor regression test
#

log_path="@adaptor.app.log.path@/"
log_file="${ctryCde}${orgnCde}AdaptorHealthCheck$(date +%Y%m%d).log"
echo "===================adaptor check start==========================" >> ${log_path}/${log_file}
incoming_hcfile_path="@adaptor.data.hcincome.path@/"
incoming_file="@adaptor.data.income.path@/"

cp -f ${incoming_hcfile_path}/* ${incoming_file}

script_path=`dirname $0`
cd $script_path
./wpc_data_adaptor.sh HK HBAP AMHCUTAS N Y 900 17 >> ${log_path}/${log_file}
./wpc_data_adaptor.sh HK HBAP AMHGSOPS.PD N Y 900 17 Y >> ${log_path}/${log_file}
./wpc_data_adaptor.sh HK HBAP AMHGSOPS.AS N Y 900 17 Y >> ${log_path}/${log_file}
status=$?
if [[ $status -ne 0 ]];then
   echo "Adaptor regression test error, please further check specific log." >> ${log_path}/${log_file}
fi
echo "===================adaptor check done==========================" >> ${log_path}/${log_file}
