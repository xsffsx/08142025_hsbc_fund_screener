#!/usr/bin/ksh

#
# Created 01 Nov, 2011
#
# the cycle job for import UT, UT performance, Asset char/Asset correlation xml
# 

log()
{
  LEVEL=$1
  MSG=$2
  TIME=`date +%H:%M:%S" "%d"/"%m"/"%Y`
  printf "[%-19s] [%-5s] %-50s\n" "${TIME}" "${LEVEL}" "${MSG}"
}

info()
{
  log "INFO" "$1"
}

error()
{
  log "ERROR" "$1"
}

checkRc()
{
	if [[ $rc -eq 0 ]]
	then
		info "$1 is done successfully."
		return $rc
	else
		error "Failed to process $1."
		return $rc
	fi
}

main()
{
	script_path=$(dirname $0)
	
	status=0
	rc=0
	
	if [[ $# -eq 0 ]]
	#Backward compatible for non parameters
	then
		${script_path}/wpc_upload_sn_xml.sh HK HBAP AMHGSOPS.PD;rc=$?
		(( status += rc ))
		checkRc "AMHGSOPS.PD SN"

		${script_path}/wpc_upload_bond_xml.sh HK HBAP AMHGSOPS.PD;rc=$?
		(( status += rc ))
		checkRc "AMHGSOPS.PD BOND"

		${script_path}/wpc_upload_sn_xml.sh HK HBAP AMHGSOPS.CE;rc=$?
		(( status += rc ))
		checkRc "AMHGSOPS.CE SN"

		${script_path}/wpc_upload_cust_elig_xml.sh HK HBAP AMHGSOPS.CE;rc=$?
		(( status += rc ))
		checkRc "AMHGSOPS.CE CUSTELIG"
	else
		TYPE=$1
		
		if [[ $TYPE == "PD" ]]
		# HFI/WPC Product Detail from GSOPS
		then
			${script_path}/wpc_upload_sn_xml.sh HK HBAP AMHGSOPS.PD;rc=$?
			(( status += rc ))
			checkRc "AMHGSOPS.PD SN"

			${script_path}/wpc_upload_bond_xml.sh HK HBAP AMHGSOPS.PD;rc=$?
			(( status += rc ))
			checkRc "AMHGSOPS.PD BOND"
			
			${script_path}/wpc_upload_eli_xml.sh HK HBAP AMHGSOPS.PD;rc=$?
			(( status += rc ))
			checkRc "AMHGSOPS.PD ELI"
		elif [[ $TYPE == "AS" ]]
		# Product Detail (A-share) from GSOPS
		then
			${script_path}/wpc_upload_sec_xml.sh HK HBAP AMHGSOPS.AS;rc=$?
			(( status += rc ))
			checkRc "AMHGSOPS.AS SEC WRTS"
		elif [[ $TYPE == "CE" ]]
		# CE from GSOPS
		then
			${script_path}/wpc_upload_sn_xml.sh HK HBAP AMHGSOPS.CE;rc=$?
			(( status += rc ))
			checkRc "AMHGSOPS.CE SN"
			${script_path}/wpc_upload_cust_elig_xml.sh HK HBAP AMHGSOPS.CE;rc=$?
			(( status += rc ))
			checkRc "AMHGSOPS.CE CUSTELIG"
		else
			error "Invalid type: $TYPE"
		fi		
	fi
}

main $@
exit $?
