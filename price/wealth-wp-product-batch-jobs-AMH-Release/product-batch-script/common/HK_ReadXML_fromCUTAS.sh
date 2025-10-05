#!/usr/bin/ksh

#
# Created 01 Nov, 2011
#
# the cycle job for import UT, Price & UT performance xml
# 

script_path=$(dirname $0)

${script_path}/wpc_upload_ut_xml.sh HK HBAP AMHCUTAS
status=$?

${script_path}/wpc_upload_product_price_xml.sh HK HBAP AMHCUTAS
status=$?

${script_path}/wpc_upload_ut_perfm_xml.sh HK HBAP AMHCUTAS

