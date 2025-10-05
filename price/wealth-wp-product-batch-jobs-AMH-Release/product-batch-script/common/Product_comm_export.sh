#!/usr/bin/ksh

if [[ $# -lt 6 ]]
then
    print "usage: product_comm_export.sh <country code> <organization code> <system code> <product type code> <statCde> <interface type> <service> <data type (optional)> <MD5 file (optional)>"
    print "  <product type code> support market code and split with '~'"
    print "  <interface type> support below value"
    print "   -- PROD"
    print "   -- PRICE"
    print "   -- PERFM"
    print "   -- ELIG"
    print "   -- REFER"
    print "  <statCde> valid value: "
    print "   -- ALL"
    print "   -- IN([STAT_CDE])"
    print "   -- NOT([STAT_CDE])"
    print "  <data type (optional)> indicate which type data will be generated, valid value: "
    print "   -- FULLSET"
    print "   -- DELTA"
    print "  <MD5 file (optional)> valid value: MD5"
    print 'example: wpc_gen_xml_header.sh HK HASE MDS WRTS~US "NOT(P,D,E)" PROD prodExportService DELTA MD5'
    exit -1
fi

typeset -u ctryCde=$1
typeset -u orgnCde=$2
typeset -u sysCde=$3
typeset -u prodType=$4
typeset -u statCde=$5
typeset interFace=$6
typeset service=$7

SCRIPT_DIR=$(dirname $0)
source ${SCRIPT_DIR}/WPCEnvSetting.sh
DATE=`date +%Y%m%d`
JAR_FILE=$(find "${JAR_FILE_PATH}" -iname "product-batch-thymeleaf-export-job*.jar")
LOG_FILE="product_export_${sysCde}_${ctryCde}_${orgnCde}_${DATE}.log"
LOG_CLOUD_PATH=${LOG_FILE_CLOUD_PATH}

genFilePath="${OUTPUT_FILE_PATH}/${ctryCde}${orgnCde}/${sysCde}"
md5FilePath="${OUTPUT_FILE_PATH}/${ctryCde}${orgnCde}/${sysCde}/MD5"

if [[ ${statCde} == 'ALL' ]]
then
  statCde=""
fi

if [[ $8 ]]
then
  typeset -u dataType=$8
fi

if [[ "$prodType" == *"~"* ]]
then
  tradeCde=$(echo $prodType | cut -d "~" -f 2)
  prodType=$(echo $prodType | cut -d "~" -f 1)
fi

if [[ ${dataType} = "DELTA" ]]
then
  typeset endTime=$(date +"%Y-%m-%dT%H:%M:%S.%3NZ")
fi

shellPara=$@

echo "Shell Parameters: ${shellPara}" | tee -a ${LOG_FILE_PATH}/${LOG_FILE} ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
echo "product export log file: ${LOG_FILE_PATH}/${LOG_FILE}"

if [[ ! -f ${LOG_FILE_PATH}/${LOG_FILE} ]]
then
  touch ${LOG_FILE_PATH}/${LOG_FILE}
  chmod 775 ${LOG_FILE_PATH}/${LOG_FILE}
fi

LOG_CLOUD_FILE=${LOG_FILE}

echo "=========================================================" | tee -a ${LOG_FILE_PATH}/${LOG_FILE} ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}

echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") product export job starting...." | tee -a ${LOG_FILE_PATH}/${LOG_FILE} ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}

LOG_FILE_TEMP="$(basename ${LOG_FILE} .log)_`date +%s%3N`.log"

param="ctryCde=${ctryCde} orgnCde=${orgnCde} sysCde=${sysCde} prodType=${prodType} statCde=${statCde} interFace=${interFace} service=${service} tradeCde=${tradeCde} endTime=${endTime}"

echo "param:${param}" | tee -a ${LOG_FILE_PATH}/${LOG_FILE} ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}

springConfig="${SPRING_BATCH_CONFIG_PATH}/product-batch-common.yml"

${JAVA_PATH} -jar ${JAR_FILE} ${param} --spring.config.location=${springConfig}  2>&1 | tee ${LOG_FILE_PATH}/${LOG_FILE_TEMP} | tee -a ${LOG_FILE_PATH}/${LOG_FILE} | tee -a ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}

status=$?

source ${SCRIPT_DIR}/wpc_handleDBException.sh

if [[ ${status} -eq 0 ]]
then
  echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Successfully Generate XML Output Interface For ${sysCde}" | tee -a ${LOG_FILE_PATH}/${LOG_FILE}  ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
else
  echo "Problem Encountered During Generate XML File For ${ctryCde} ${orgnCde} ${sysCde}." | tee -a ${LOG_FILE_PATH}/${LOG_FILE}  ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
  exit -1
fi


#### Generate MD5 file start ####
header_exist="false"

if [[ -n $9 && $9 = "MD5" ]]
then
    for outFile in `ls $genFilePath | grep -E "^${ctryCde}_${orgnCde}_${sysCde}_.*[0-9]{12}_[0-9]{1,5}.out\$"`; do
        echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Generating MD5 for interfaces with header file: ${outFile}" >> ${LOG_FILE_PATH}/${LOG_FILE}
		header_exist="true"
		cd $genFilePath
		seq_num=`ls $outFile | awk -F "_" '{print $NF}' | awk -F "." '{print $1}'`
		filePattern="*${ctryCde}_${orgnCde}_${sysCde}_*_${seq_num}.xml"
		inner_status=0
		ls $filePattern | while read xmlFile; do
			md5FileName="${xmlFile}.MD5"
			echo "Generating MD5 file for ${sysCde} ..."
			md5sum ${xmlFile} | awk '{print $1}' > ${md5FilePath}/${md5FileName}
			genMD5_result=$?
			if [[ $genMD5_result -eq 0 ]]
			then
				chmod 775 ${md5FilePath}/${md5FileName}
				echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") MD5 file ${md5FileName} generated for interface file ${xmlFile} to ${sysCde} server successfully" | tee -a ${LOG_FILE_PATH}/${LOG_FILE}
				cp ${xmlFile} ${md5FilePath}/${xmlFile}
				chmod 775 ${md5FilePath}/${xmlFile}
				echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Move interfaces successfully" >> ${LOG_FILE_PATH}/${LOG_FILE}
			else
				inner_status=1
				# log message to indicate generate MD5 failure
				echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Failed to generate MD5 file for interface file ${xmlFile} to ${sysCde} server"  | tee -a ${LOG_FILE_PATH}/${LOG_FILE}  ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
			fi
		done
		cp ${outFile} ${md5FilePath}/${outFile}
		chmod 775 ${md5FilePath}/${outFile}
		mv ${outFile} ${outFile}.bak
		echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Move interfaces header file successfully" | tee -a ${LOG_FILE_PATH}/${LOG_FILE}

		if [[ ${inner_status} -eq 0 ]]
		then
			echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Move MD5, interfaces and header file for ${outFile} successfully" | tee -a ${LOG_FILE_PATH}/${LOG_FILE}
		else
			status=1
		fi
	done
else
	echo "MD5 parameter is empty or the parameter value is invalid."
fi

if [[ $9 = "MD5" && ${header_exist} = "false" ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") No header file found for generate MD5 for ${sysCde} server." | tee -a ${LOG_FILE_PATH}/${LOG_FILE}  ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
   exit -1
fi

if [[ ${status} -eq 0 ]]
then
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Outbound generate MD5 of WPC XML interfaces to ${sysCde} server successfully." | tee -a ${LOG_FILE_PATH}/${LOG_FILE}
else
   echo "$(date +"["%H:%M:%S" "%d"/"%m"/"%Y"]") Outbound generate MD5 of WPC XML interfaces to ${sysCde} server failed." | tee -a ${LOG_FILE_PATH}/${LOG_FILE}  ${LOG_CLOUD_PATH}/${LOG_CLOUD_FILE}
   exit -1
fi
#### Generate MD5 file end ####