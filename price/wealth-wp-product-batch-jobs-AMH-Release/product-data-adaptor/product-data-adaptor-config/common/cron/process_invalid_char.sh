adaptorPath=$1
file=$2
cur_date=`date +%Y%m%d%H%M%S%3N`
orig_file=${file}.orig.${cur_date}

log_cloud_path="@adaptor.app.cloud.log.path@"
log_cloud_file="wpc_GSOPS_invalidChar$(date +%Y%m%d).log"

echo ${file}
echo ${orig_file}

cd ${adaptorPath}
mv ${file} ${orig_file}
iconv -f UTF-8 -t UTF-8//IGNORE --output=${file} ${orig_file}
chmod 755 ${file}

file_size=`ls -l $file | awk '{print $5}'`
orig_file_size=`ls -l $orig_file | awk '{print $5}'`

if [[ ${file_size} -eq ${orig_file_size} ]]
then
	echo "No invalid chars found, the backup file will be removed."
	rm -r ${orig_file}
	exit 0
else
	echo "Invalid chars found in ${orig_file}."
	logger -t root "WPC1013E GSOPS file has invalid chars in ${orig_file}. Please contact WMD Support."
	echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") WPC1013E GSOPS file has invalid chars in ${orig_file}. Please contact WMD Support." >> ${log_cloud_path}/${log_cloud_file}
fi
