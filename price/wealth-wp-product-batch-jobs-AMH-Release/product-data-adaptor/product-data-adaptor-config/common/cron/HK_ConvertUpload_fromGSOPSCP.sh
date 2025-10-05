#!/usr/bin/ksh

#
# Created 03 Nar, 2020
#
# the cycle job for convert GSOPS file and process price xml
# 

script_path=$(dirname $0)

if [[ $# -eq 2 ]]
then
    typeset -u waitTime=$1
    typeset -u waitRepeats=$2
    ${script_path}/wpc_data_adaptor.sh HK HBAP AMHGSOPS.CP N N ${waitTime} ${waitRepeats}
else
    ${script_path}/wpc_data_adaptor.sh HK HBAP AMHGSOPS.CP N N
fi

status=$?

if [[ ${status} -eq 0 ]]
then
    /appvol/product-spring-batch/bin/HKHBAP/cron/wpc_upload_product_price_xml.sh HK HBAP AMHGSOPS.CP
fi
