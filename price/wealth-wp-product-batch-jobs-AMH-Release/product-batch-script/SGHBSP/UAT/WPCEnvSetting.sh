#!/usr/bin/ksh

#############################################################
###          Environment Setting                          ###
#############################################################
export ENCRYPT_KEY=$ENCRYPT_KEY

JAVA_PATH="/appvol/openbank/jdk1.8.0_212/bin/java"
JDBC_LIB_PATH="/appvol/wpc/app/lib/ojdbc7-12.1.0.2.jar"
MQ_LIB_PATH="@mq.lib.path@"
SYS_TIMEZONE_DELTA="+8 hours"
GFIX_LISTENER_SERVICE_PERIOD="35280"
GFIX_LISTENER_MONITOR_INTERVAL="120"
GFIX_MASTER_HOST="hkgv3ls0787"
BATCH_URL="https://batch-controller.wealth-platform-sgh.sit.aws.cloud.dummy/api/product-dp-file-sit-egress/export"
BATCH_INGRESS_URL="https://batch-controller.wealth-platform-sgh.sit.aws.cloud.dummy/api/product-dp-file-ingress"
BATCH_INTERNAL_JOBS_URL="https://batch-controller.wealth-platform-sgh.sit.aws.cloud.dummy/api/product-dp-file-ingress/jobs"

#############################################################
###          Batch Server Path INFO                       ###
#############################################################
BATCH_APP_PATH="/appvol/product-spring-batch/bin/SGHBSP"
CONFIG_FILE_PATH="${BATCH_APP_PATH}/configFile"
JAR_FILE_PATH="${BATCH_APP_PATH}/lib"
BATCH_SCRIPT_PATH="${BATCH_APP_PATH}/cron"
DB_KEY_FILE_PATH="${BATCH_APP_PATH}/key"
BATCH_APP_LOG_PATH="${BATCH_APP_PATH}/logs"
EXCEL_CONFIG_PATH="${BATCH_APP_PATH}/batch/configFile/xls"
ADAPTOR_SCRIPT_PATH="${BATCH_APP_PATH}/adaptor/cron"
SPRING_BATCH_CONFIG_PATH="${BATCH_APP_PATH}/config"

### Batch Data Path ###
BATCH_DATA_PATH="/appvol/product-spring-batch/data"
LOG_FILE_PATH="${BATCH_DATA_PATH}/processLog"
LOG_FILE_CLOUD_PATH="/var/log/app"
REPORT_FILE_PATH="${BATCH_DATA_PATH}/report"
STAT_REPORT_PATH="${BATCH_DATA_PATH}/stat"
BATCH_LOG_PATH="/var/log/app"

### Output File Path ###
OUTPUT_FILE_PATH="${BATCH_DATA_PATH}/outgoing"
WPC_OUTPUT_FILE_PATH="/appvol/wpc/data/outgoing"
REUTERS_OUTPUT_FILE_PATH="${OUTPUT_FILE_PATH}/reuters"

###SMART PATH, this need to remove after convergence deployed#####
SAMRT_FILE_PATH="/appvol/convergence/data/outgoing"

### RBP NAS Server Path ###
RBP_OUTPUT_FILE_PATH="@rbp.output.path@"
### Incoming File Path ###
INCOMING_DATA_PATH="${BATCH_DATA_PATH}/incoming"
BATCH_LANDING_PATH="${BATCH_DATA_PATH}/landing/batch"
BATCH_INCOMING_PATH="${INCOMING_DATA_PATH}/batch"
FINDOC_INCOMING_PATH="${INCOMING_DATA_PATH}/findoc"
#FINDOC_INCOMING_PATH="${BATCH_DATA_PATH}/FinDoc/req/incoming"
DATASYNC_INCOMING_PATH="${INCOMING_DATA_PATH}/datasync"

### Adaptor Path ###
ADAPTOR_PATH="${BATCH_DATA_PATH}/Adaptor"
ADAPTOR_OUTPUT_FILE_PATH="${ADAPTOR_PATH}/outgoing"
MANUAL_PRC_FILE_PATH="${ADAPTOR_PATH}/priceUpload"
MANUAL_PROD_FILE_PATH="${ADAPTOR_PATH}/priceUpload"
ADAPTOR_INCOMING_FILE_PATH="${ADAPTOR_PATH}/incoming"

#############################################################
###          Third-party account password                 ###
#############################################################
TR_FILE_USERNAME="150715"
TR_FILE_PASSWORD="aWIVF4xWNUnpJrgKh1EI1g=="
TR_PROXY_HOST="intpxy1.hk.dummy"
TR_PROXY_PORT="8080"

#############################################################
###          XML Generation FTP Profile                   ###
#############################################################
RBP_TW_FTP_USER="@ftp.user.rbp.tw@"
RBP_TW_FTP_HOST="@ftp.host.rbp.tw@"
RBP_TW_FTP_PORT=@ftp.port.rbp.tw@
RBP_TW_FTP_PATH="@ftp.remote.path.rbp.tw@"

RBP_SG_FTP_USER="@ftp.user.rbp.sg@"
RBP_SG_FTP_HOST="@ftp.host.rbp.sg@"
RBP_SG_FTP_PORT=@ftp.port.rbp.sg@
RBP_SG_FTP_PATH="@ftp.remote.path.rbp.sg@"

RBP_GB_FTP_USER="@ftp.user.rbp.gb@"
RBP_GB_FTP_HOST="@ftp.host.rbp.gb@"
RBP_GB_FTP_PORT=@ftp.port.rbp.gb@
RBP_GB_FTP_PATH="@ftp.remote.path.rbp.gb@"

RBP_FTP_COUNT=1
RBP_FTP_USER_1="rtpftp01"
RBP_FTP_HOST_1="hkg3vl3992o.hk.dummy"
RBP_FTP_PORT_1=22
RBP_FTP_PATH_1="/dummy/rtp/data/wpcinhkdummy/plaintext/sec/rbp/product/source"

RSP_FTP_COUNT=1
RSP_FTP_USER_1="rtpftp01"
RSP_FTP_HOST_1="hkg3vl3992o.hk.dummy"
RSP_FTP_PORT_1=22
RSP_FTP_PATH_1="/dummy/rtp/data/wpcinhkdummy/plaintext/sec/rsp/product/source"

########add new country GTAP,UKDW########## 
GTAP_FTP_COUNT=@ftp.count.gtap@
GTAP_FTP_USER="@ftp.user.gtap@"
GTAP_FTP_HOST="@ftp.host.gtap@"
GTAP_FTP_PORT=@ftp.port.gtap@
GTAP_FTP_PATH="@ftp.remote.path.gtap@"


GTAP_FTP_HOST_1="@ftp.host.gtap.1@"
GTAP_FTP_USER_1="@ftp.user.gtap.1@"
GTAP_FTP_PORT_1=@ftp.port.gtap.1@
GTAP_FTP_PATH_1="@ftp.remote.path.gtap.1@"

UKDW_FTP_COUNT=@ftp.count.ukdw@
UKDW_FTP_HOST_1="@ftp.host.ukdw.1@"
UKDW_FTP_USER_1="@ftp.user.ukdw.1@"
UKDW_FTP_PORT_1=@ftp.port.ukdw.1@
UKDW_FTP_PATH_1="@ftp.remote.path.ukdw.1@"
 
UKDW_FTP_HOST_2="@ftp.host.ukdw.2@"
UKDW_FTP_USER_2="@ftp.user.ukdw.2@"
UKDW_FTP_PORT_2=@ftp.port.ukdw.2@
UKDW_FTP_PATH_2="@ftp.remote.path.ukdw.2@"

########add new country GTAP,UKDW########## 

MKD_FTP_COUNT=1
MKD_FTP_HOST_1="app1-stockconnect-mkdu.hk.dummy"
MKD_FTP_USER_1="mkdftp01"
MKD_FTP_PORT_1=22
MKD_FTP_PATH_1="/dummy/mkd/data/interface/wpc/MD5"
MKD_FTP_HOST_2=""
MKD_FTP_USER_2=""
MKD_FTP_PORT_2=
MKD_FTP_PATH_2=""
MKD_FTP_HOST_3="@ftp.host.mkd.3@"
MKD_FTP_USER_3="@ftp.user.mkd.3@"
MKD_FTP_PORT_3=@ftp.port.mkd.3@
MKD_FTP_PATH_3="@ftp.remote.path.mkd.3@"
MKD_FTP_HOST_4="@ftp.host.mkd.4@"
MKD_FTP_USER_4="@ftp.user.mkd.4@"
MKD_FTP_PORT_4=@ftp.port.mkd.4@
MKD_FTP_PATH_4="@ftp.remote.path.mkd.4@"



MKD_ID_FTP_COUNT=@ftp.count.mkd.id@
MKD_ID_FTP_HOST_1="@ftp.host.mkd.1.id@"
MKD_ID_FTP_USER_1="@ftp.user.mkd.1.id@"
MKD_ID_FTP_PORT_1=@ftp.port.mkd.1.id@
MKD_ID_FTP_PATH_1="@ftp.remote.path.mkd.1.id@"
MKD_ID_FTP_HOST_2="@ftp.host.mkd.2.id@"
MKD_ID_FTP_USER_2="@ftp.user.mkd.2.id@"
MKD_ID_FTP_PORT_2=@ftp.port.mkd.2.id@
MKD_ID_FTP_PATH_2="@ftp.remote.path.mkd.2.id@"
MKD_ID_FTP_HOST_3="@ftp.host.mkd.3.id@"
MKD_ID_FTP_USER_3="@ftp.user.mkd.3.id@"
MKD_ID_FTP_PORT_3=@ftp.port.mkd.3.id@
MKD_ID_FTP_PATH_3="@ftp.remote.path.mkd.3.id@"

MKD_PRC_FTP_COUNT=2
MKD_PRC_FTP_HOST_1="10.210.61.204"
MKD_PRC_FTP_USER_1="mkdftp01"
MKD_PRC_FTP_PORT_1=22
MKD_PRC_FTP_PATH_1="/home/user/mkdftp01"
MKD_PRC_FTP_HOST_2="10.210.148.70"
MKD_PRC_FTP_USER_2="mkdftp01"
MKD_PRC_FTP_PORT_2=22
MKD_PRC_FTP_PATH_2="/home/mkdftp01"
MKD_PRC_FTP_HOST_3="@ftp.host.mkd.price.3@"
MKD_PRC_FTP_USER_3="@ftp.user.mkd.price.3@"
MKD_PRC_FTP_PORT_3=@ftp.port.mkd.price.3@
MKD_PRC_FTP_PATH_3="@ftp.remote.path.mkd.price.3@"
MKD_PRC_FTP_HOST_4="@ftp.host.mkd.price.4@"
MKD_PRC_FTP_USER_4="@ftp.user.mkd.price.4@"
MKD_PRC_FTP_PORT_4=@ftp.port.mkd.price.4@
MKD_PRC_FTP_PATH_4="@ftp.remote.path.mkd.price.4@"

MDH_FTP_COUNT=@ftp.count.mdh@
MDH_FTP_HOST_1="@ftp.host.mdh.1@"
MDH_FTP_USER_1="@ftp.user.mdh.1@"
MDH_FTP_PORT_1=@ftp.port.mdh.1@
MDH_FTP_PATH_1="@ftp.remote.path.mdh.1@"
MDH_FTP_HOST_2="@ftp.host.mdh.2@"
MDH_FTP_USER_2="@ftp.user.mdh.2@"
MDH_FTP_PORT_2=@ftp.port.mdh.2@
MDH_FTP_PATH_2="@ftp.remote.path.mdh.2@"

MDSSTBHK_FTP_COUNT=1
MDSSTBHK_FTP_HOST_1="app-stb-mdsu.hk.dummy"
MDSSTBHK_FTP_USER_1="mdsftp01"
MDSSTBHK_FTP_PORT_1=22
MDSSTBHK_FTP_PATH_1="/appvol/mdsbe/data/wpc/predictive_search/hk"

MDSSTBCN_FTP_COUNT=1
MDSSTBCN_FTP_HOST_1="app-stb-mdsu.hk.dummy"
MDSSTBCN_FTP_USER_1="mdsftp01"
MDSSTBCN_FTP_PORT_1=22
MDSSTBCN_FTP_PATH_1="/appvol/mdsbe/data/wpc/predictive_search/cn"

BI_FTP_USER="@ftp.user.bi@"
BI_FTP_HOST="@ftp.host.bi@"
BI_FTP_PORT=@ftp.port.bi@
BI_FTP_PATH="@ftp.remote.path.bi@"

BI_SG_FTP_USER="@ftp.user.bi.sg@"
BI_SG_FTP_HOST="@ftp.host.bi.sg@"
BI_SG_FTP_PORT=@ftp.port.bi.sg@
BI_SG_FTP_PATH="@ftp.remote.path.bi.sg@"

BI_MY_FTP_USER="@ftp.user.bi.my@"
BI_MY_FTP_HOST="@ftp.host.bi.my@"
BI_MY_FTP_PORT=@ftp.port.bi.my@
BI_MY_FTP_PATH="@ftp.remote.path.bi.my@"

BI_TW_FTP_USER="@ftp.user.bi.tw@"
BI_TW_FTP_HOST="@ftp.host.bi.tw@"
BI_TW_FTP_PORT=@ftp.port.bi.tw@
BI_TW_FTP_PATH="@ftp.remote.path.bi.tw@"

BI_AE_FTP_USER="@ftp.user.bi.ae@"
BI_AE_FTP_HOST="@ftp.host.bi.ae@"
BI_AE_FTP_PORT=@ftp.port.bi.ae@
BI_AE_FTP_PATH="@ftp.remote.path.bi.ae@"

BI_FR_FTP_USER="@ftp.user.bi.fr@"
BI_FR_FTP_HOST="@ftp.host.bi.fr@"
BI_FR_FTP_PORT=@ftp.port.bi.fr@
BI_FR_FTP_PATH="@ftp.remote.path.bi.fr@"

BI_GB_FTP_USER="@ftp.user.bi.gb@"
BI_GB_FTP_HOST="@ftp.host.bi.gb@"
BI_GB_FTP_PORT=@ftp.port.bi.gb@
BI_GB_FTP_PATH="@ftp.remote.path.bi.gb@"

BI_CN_FTP_USER="@ftp.user.bi.cn@"
BI_CN_FTP_HOST="@ftp.host.bi.cn@"
BI_CN_FTP_PORT=@ftp.port.bi.cn@
BI_CN_FTP_PATH="@ftp.remote.path.bi.cn@"

BI_IN_FTP_USER="@ftp.user.bi.in@"
BI_IN_FTP_HOST="@ftp.host.bi.in@"
BI_IN_FTP_PORT=@ftp.port.bi.in@
BI_IN_FTP_PATH="@ftp.remote.path.bi.in@"

BI_ID_FTP_USER="@ftp.user.bi.id@"
BI_ID_FTP_HOST="@ftp.host.bi.id@"
BI_ID_FTP_PORT=@ftp.port.bi.id@
BI_ID_FTP_PATH="@ftp.remote.path.bi.id@"

BI_HK_FTP_USER="spmftp83"
BI_HK_FTP_HOST="ht31-etl-systems.hk.dummy"
BI_HK_FTP_PORT=22
BI_HK_FTP_PATH="/ds/ht31/data/dpr_ht3105/dpr_biscst_uat/landing/wpc"

MDS_FTP_COUNT=2
MDS_FTP_HOST_1="app1-amh-mdsu.hk.dummy"
MDS_FTP_USER_1="mkdftp01"
MDS_FTP_PORT_1=22
MDS_FTP_PATH_1="/dummy/mkd/data/interface/wpc/MD5"
MDS_FTP_HOST_2="app2-amh-mdsu.hk.dummy"
MDS_FTP_USER_2="mkdftp01"
MDS_FTP_PORT_2=22
MDS_FTP_PATH_2="/dummy/mkd/data/interface/wpc/MD5"

CAS_FTP_COUNT=3
CAS_FTP_USER_1="casftp01"
CAS_FTP_HOST_1="casivdev01.hk.dummy"
CAS_FTP_PORT_1=22
CAS_FTP_PATH_1="/appvol/APP80/CASIV/sftp"
CAS_FTP_USER_2="casftp01"
CAS_FTP_HOST_2="casifuat01.hk.dummy"
CAS_FTP_PORT_2=22
CAS_FTP_PATH_2="/appvol/APP80/CASIV/sftp"
CAS_FTP_USER_3="casftp01"
CAS_FTP_HOST_3="casifuat02.hk.dummy"
CAS_FTP_PORT_3=22
CAS_FTP_PATH_3="/appvol/APP80/CASIV/sftp"
CAS_FTP_USER_4="@ftp.user.cas.4@"
CAS_FTP_HOST_4="@ftp.host.cas.4@"
CAS_FTP_PORT_4=@ftp.port.cas.4@
CAS_FTP_PATH_4="@ftp.remote.path.cas.4@"

SRBP_FTP_COUNT=1
SRBP_FTP_USER_1="rtpftp01"
SRBP_FTP_HOST_1="batch.wealth-platform-amh.uat.aws.cloud.dummy"
SRBP_FTP_PORT_1=22
SRBP_FTP_PATH_1="/appvol/rbpbc/data/wpcinhkhbap/plaintext/incoming"
SRBP_FTP_USER_2="@ftp.user.srbp.2@"
SRBP_FTP_HOST_2="@ftp.host.srbp.2@"
SRBP_FTP_PORT_2=@ftp.port.srbp.2@
SRBP_FTP_PATH_2="@ftp.remote.path.srbp.2@"
SRBP_FTP_USER_3="@ftp.user.srbp.3@"
SRBP_FTP_HOST_3="@ftp.host.srbp.3@"
SRBP_FTP_PORT_3=@ftp.port.srbp.3@
SRBP_FTP_PATH_3="@ftp.remote.path.srbp.3@"
SRBP_FTP_USER_4="@ftp.user.srbp.4@"
SRBP_FTP_HOST_4="@ftp.host.srbp.4@"
SRBP_FTP_PORT_4=@ftp.port.srbp.4@
SRBP_FTP_PATH_4="@ftp.remote.path.srbp.4@"

GHSS_FTP_COUNT=1
GHSS_FTP_SPEC_PROC_IND="Y"
GHSS_FTP_USER_1="wspsftsr"
GHSS_FTP_HOST_1="DAP005A.HK.dummy"
GHSS_FTP_PORT_1=22
GHSS_FTP_COMMAND_1="system \"CHGAUT OBJ('filePath') USER(ICDRTPOWN) DTAAUT(*RWX) OBJAUT(*ALL)\""
GHSS_FTP_PATH_1="/DAP005/IHAQDLS/RPCHSFLR/WPC"


WGHSS_FTP_COUNT=@ftp.count.wghss@
WGHSS_FTP_SPEC_PROC_IND="@ftp.spec.proc.ind.wghss@"
WGHSS_FTP_USER_1="@ftp.user.wghss.1@"
WGHSS_FTP_HOST_1="@ftp.host.wghss.1@"
WGHSS_FTP_PORT_1=@ftp.port.wghss.1@
WGHSS_FTP_COMMAND_1="@ftp.command.wghss.1@"
WGHSS_FTP_PATH_1="@ftp.remote.path.wghss.1@"

GSOPS_IDW_HK_FTP_USER="ICDSFTSB"
GSOPS_IDW_HK_FTP_HOST="DEV.DAP005.HK.dummy"
GSOPS_IDW_HK_FTP_PORT="22"
GSOPS_IDW_HK_FTP_PATH="/home/ris_uat_hkg/input/ris-hfi-gsops"
GSOPS_IDW_FTP_SPEC_PROC_IND="Y"

GSOPSU_FTP_COUNT=1
GSOPSU_FTP_USER_1="ICDSFTSB"
GSOPSU_FTP_HOST_1="DEV.DAP005.HK.dummy"
GSOPSU_FTP_PORT_1="22"
GSOPSU_FTP_PATH_1="/dap005/home/ris_uat_hkg/input/ris-wpc-gsops"

GSOPSE_FTP_COUNT=1
GSOPSE_FTP_USER_1="ICDSFTSB"
GSOPSE_FTP_HOST_1="DEV.DAP005.HK.dummy"
GSOPSE_FTP_PORT_1="22"
GSOPSE_FTP_PATH_1="/dap005/home/ris_uat_hkg/input/ris-wpc-gsops"

GSOPSB_FTP_COUNT=1
GSOPSB_FTP_USER_1="ICDSFTSB"
GSOPSB_FTP_HOST_1="DEV.DAP005.HK.dummy"
GSOPSB_FTP_PORT_1="22"
GSOPSB_FTP_PATH_1="/dap005/home/ris_uat_hkg/input/ris-wpc-gsops"

#############################################################
###          IDS/IDW FTP Profile                          ###
#############################################################
IDS_FTP_USER="idsftp01"
IDS_FTP_HOST="uat1-w2-amh-idsonline.hk.dummy"
IDS_FTP_PORT=22
IDS_FTP_PATH="/dummy/rbpidsdl/clusterdata/UTB/controlm/data/HKHBAU"

IDS_PR_HK_FTP_USER="idsftp01"
IDS_PR_HK_FTP_HOST="uat1-w2-amh-idsonline.hk.dummy"
IDS_PR_HK_FTP_PORT=22
IDS_PR_HK_FTP_PATH="/dummy/rbpidsdl/clusterdata/controlm/data/HKHBAP"

IDS_ALL_HK_FTP_USER="idsftp01"
IDS_ALL_HK_FTP_HOST="uat1-w2-amh-idsonline.hk.dummy"
IDS_ALL_HK_FTP_PORT=22
IDS_ALL_HK_FTP_PATH="/dummy/rbpidsdl/clusterdata/controlm/data/HKHBAP"

IDS_SG_FTP_USER="@ftp.user.ids.sg@"
IDS_SG_FTP_HOST="@ftp.host.ids.sg@"
IDS_SG_FTP_PORT=@ftp.port.ids.sg@
IDS_SG_FTP_PATH="@ftp.remote.path.ids.sg@"

IDW_FTP_USER="idsftp01"
IDW_FTP_HOST="uat1-w2-amh-idsonline.hk.dummy"
IDW_FTP_PORT=22
IDW_FTP_PATH="/dummy/rbpidsdl/clusterdata/controlm/data/HKHBAP"

IDW_SG_FTP_USER="@ftp.user.idw.sg@"
IDW_SG_FTP_HOST="@ftp.host.idw.sg@"
IDW_SG_FTP_PORT=@ftp.port.idw.sg@
IDW_SG_FTP_PATH="@ftp.remote.path.idw.sg@"

IDS_CN_FTP_USER="@ftp.user.ids.cn@"
IDS_CN_FTP_HOST="@ftp.host.ids.cn@"
IDS_CN_FTP_PORT=@ftp.port.ids.cn@
IDS_CN_FTP_PATH="@ftp.remote.path.ids.cn@"

IDW_CN_FTP_USER="@ftp.user.idw.cn@"
IDW_CN_FTP_HOST="@ftp.host.idw.cn@"
IDW_CN_FTP_PORT=@ftp.port.idw.cn@
IDW_CN_FTP_PATH="@ftp.remote.path.idw.cn@"

IDS_ID_FTP_USER="@ftp.user.ids.id@"
IDS_ID_FTP_HOST="@ftp.host.ids.id@"
IDS_ID_FTP_PORT=@ftp.port.ids.id@
IDS_ID_FTP_PATH="@ftp.remote.path.ids.id@"

IDW_ID_FTP_USER="@ftp.user.idw.id@"
IDW_ID_FTP_HOST="@ftp.host.idw.id@"
IDW_ID_FTP_PORT=@ftp.port.idw.id@
IDW_ID_FTP_PATH="@ftp.remote.path.idw.id@"

IDS_SG_FTP_USER="@ftp.user.ids.sg@"
IDS_SG_FTP_HOST="@ftp.host.ids.sg@"
IDS_SG_FTP_PORT=@ftp.port.ids.sg@
IDS_SG_FTP_PATH="@ftp.remote.path.ids.sg@"

IDS_MY_FTP_USER="@ftp.user.ids.my@"
IDS_MY_FTP_HOST="@ftp.host.ids.my@"
IDS_MY_FTP_PORT=@ftp.port.ids.my@
IDS_MY_FTP_PATH="@ftp.remote.path.ids.my@"

IDS_TW_FTP_USER="@ftp.user.ids.tw@"
IDS_TW_FTP_HOST="@ftp.host.ids.tw@"
IDS_TW_FTP_PORT=@ftp.port.ids.tw@
IDS_TW_FTP_PATH="@ftp.remote.path.ids.tw@"

IDS_IN_FTP_USER="@ftp.user.ids.in@"
IDS_IN_FTP_HOST="@ftp.host.ids.in@"
IDS_IN_FTP_PORT=@ftp.port.ids.in@
IDS_IN_FTP_PATH="@ftp.remote.path.ids.in@"

#############################################################
WDS_FTP_USER="idsftp01"
WDS_FTP_HOST="uat1-w2-amh-idsonline.hk.dummy"
WDS_FTP_PORT=22
WDS_FTP_PATH="/dummy/rbpidsdl/clusterdata/controlm/data/${ctry_cde}${orgn_cde}"
###          IDS_TL FTP Profile                           ###
#############################################################
IDS_TL_FTP_USER="idsftu1"
IDS_TL_FTP_HOST="app-sit-bat-amh-ids.hk.dummy"
IDS_TL_FTP_PORT=22
IDS_TL_FTP_PATH="/dummy/rbpidsdl/clusterdata/TransformationLayer/${ctry_cde}/input/poll"

IDS_TL_SG_FTP_USER="@ftp.user.ids_tl.sg@"
IDS_TL_SG_FTP_HOST="@ftp.host.ids_tl.sg@"
IDS_TL_SG_FTP_PORT=@ftp.port.ids_tl.sg@
IDS_TL_SG_FTP_PATH="@ftp.remote.path.ids_tl.sg@"

IDS_TL_CN_FTP_USER="@ftp.user.ids_tl.cn@"
IDS_TL_CN_FTP_HOST="@ftp.host.ids_tl.cn@"
IDS_TL_CN_FTP_PORT=@ftp.port.ids_tl.cn@
IDS_TL_CN_FTP_PATH="@ftp.remote.path.ids_tl.cn@"

IDS_TL_ID_FTP_USER="@ftp.user.ids_tl.id@"
IDS_TL_ID_FTP_HOST="@ftp.host.ids_tl.id@"
IDS_TL_ID_FTP_PORT=@ftp.port.ids_tl.id@
IDS_TL_ID_FTP_PATH="@ftp.remote.path.ids_tl.id@"

IDS_TL_SG_FTP_USER="@ftp.user.ids_tl.sg@"
IDS_TL_SG_FTP_HOST="@ftp.host.ids_tl.sg@"
IDS_TL_SG_FTP_PORT=@ftp.port.ids_tl.sg@
IDS_TL_SG_FTP_PATH="@ftp.remote.path.ids_tl.sg@"

IDS_TL_MY_FTP_USER="@ftp.user.ids_tl.my@"
IDS_TL_MY_FTP_HOST="@ftp.host.ids_tl.my@"
IDS_TL_MY_FTP_PORT=@ftp.port.ids_tl.my@
IDS_TL_MY_FTP_PATH="@ftp.remote.path.ids_tl.my@"

IDS_TL_TW_FTP_USER="@ftp.user.ids_tl.tw@"
IDS_TL_TW_FTP_HOST="@ftp.host.ids_tl.tw@"
IDS_TL_TW_FTP_PORT=@ftp.port.ids_tl.tw@
IDS_TL_TW_FTP_PATH="@ftp.remote.path.ids_tl.tw@"

IDS_TL_IN_FTP_USER="@ftp.user.ids_tl.in@"
IDS_TL_IN_FTP_HOST="@ftp.host.ids_tl.in@"
IDS_TL_IN_FTP_PORT=@ftp.port.ids_tl.in@
IDS_TL_IN_FTP_PATH="@ftp.remote.path.ids_tl.in@"

#############################################################
###          Treemap FTP Profile                          ###
#############################################################
RMP_FTP_USER="rmpftp01"
RMP_FTP_HOST="hkgv3ls0359.p2g.netd2.hk.dummy"
RMP_FTP_PORT=22
RMP_FTP_PATH="/appvol/APP/RMP/portaldata/wpc/${ctry_cde}"

RMP_SG_FTP_USER="rmpftp01"
RMP_SG_FTP_HOST="hkl20048350.hc.cloud.example.com"
RMP_SG_FTP_PORT=22
RMP_SG_FTP_PATH="/appvol/APP/RMP/portaldata/wpc/${ctry_cde}"

RMP_SG_FTP_COUNT=1
RMP_SG_FTP_USER_1="wpsftp"
RMP_SG_FTP_HOST_1="localhost"
RMP_SG_FTP_PORT_1=22
RMP_SG_FTP_PATH_1="/appvol/rmp/data/portaldata/wpc/${ctry_cde}"
RMP_SG_FTP_USER_2=""
RMP_SG_FTP_HOST_2=""
RMP_SG_FTP_PORT_2=
RMP_SG_FTP_PATH_2=""

#############################################################
###          Reuters Bond FTP Profile                     ###
#############################################################

REUTERS_SG_FTP_USER="@ftp.user.reuters.sg@"
REUTERS_SG_FTP_HOST="@ftp.host.reuters.sg@"
REUTERS_SG_FTP_PORT=@ftp.port.reuters.sg@
REUTERS_SG_FTP_PATH="@ftp.remote.path.reuters.sg@"

REUTERS_FTP_USER="@ftp.user.reuters@"
REUTERS_FTP_HOST="@ftp.host.reuters@"
REUTERS_FTP_PORT=@ftp.port.reuters@
REUTERS_FTP_PATH="@ftp.remote.path.reuters@"

REUTERS_HK_FTP_USER="r9001453"
REUTERS_HK_FTP_HOST="hosted.datascope.reuters.com"
REUTERS_HK_FTP_PORT=22
REUTERS_HK_FTP_PATH="/reports"

REUTERS_CN_FTP_USER="@ftp.user.reuters.cn@"
REUTERS_CN_FTP_HOST="@ftp.host.reuters.cn@"
REUTERS_CN_FTP_PORT=@ftp.port.reuters.cn@
REUTERS_CN_FTP_PATH="@ftp.remote.path.reuters.cn@"

REUTERS_MY_FTP_USER="@ftp.user.reuters.my@"
REUTERS_MY_FTP_HOST="@ftp.host.reuters.my@"
REUTERS_MY_FTP_PORT=@ftp.port.reuters.my@
REUTERS_MY_FTP_PATH="@ftp.remote.path.reuters.my@"

REUTERS_TW_FTP_USER="@ftp.user.reuters.tw@"
REUTERS_TW_FTP_HOST="@ftp.host.reuters.tw@"
REUTERS_TW_FTP_PORT=@ftp.port.reuters.tw@
REUTERS_TW_FTP_PATH="@ftp.remote.path.reuters.tw@"

#############################################################
###          TRIS FTP Profile                             ###
#############################################################
TRIS_FTP_COUNT=@ftp.count.tris@
TRIS_FTP_USER_1="@ftp.user.tris.1@"
TRIS_FTP_HOST_1="@ftp.host.tris.1@"
TRIS_FTP_PORT_1=@ftp.port.tris.1@
TRIS_FTP_PATH_1="@ftp.remote.path.tris.1@"
TRIS_FTP_USER_2="@ftp.user.tris.2@"
TRIS_FTP_HOST_2="@ftp.host.tris.2@"
TRIS_FTP_PORT_2=@ftp.port.tris.2@
TRIS_FTP_PATH_2="@ftp.remote.path.tris.2@"
TRIS_FTP_USER_3="@ftp.user.tris.3@"
TRIS_FTP_HOST_3="@ftp.host.tris.3@"
TRIS_FTP_PORT_3=@ftp.port.tris.3@
TRIS_FTP_PATH_3="@ftp.remote.path.tris.3@"
TRIS_FTP_USER_4="@ftp.user.tris.4@"
TRIS_FTP_HOST_4="@ftp.host.tris.4@"
TRIS_FTP_PORT_4=@ftp.port.tris.4@
TRIS_FTP_PATH_4="@ftp.remote.path.tris.4@"

#############################################################
###          TSUTIL FTP Profile                           ###
#############################################################
TSUTIL_FTP_COUNT=@ftp.count.tsutil@
TSUTIL_FTP_HOST_1="@ftp.host.tsutil.1@"
TSUTIL_FTP_USER_1="@ftp.user.tsutil.1@"
TSUTIL_FTP_PORT_1=@ftp.port.tsutil.1@
TSUTIL_FTP_PATH_1="@ftp.remote.path.tsutil.1@"
TSUTIL_FTP_HOST_2="@ftp.host.tsutil.2@"
TSUTIL_FTP_USER_2="@ftp.user.tsutil.2@"
TSUTIL_FTP_PORT_2=@ftp.port.tsutil.2@
TSUTIL_FTP_PATH_2="@ftp.remote.path.tsutil.2@"

#############################################################
###          SPOMS CD FTP Profile                         ###
#############################################################
SPOMS_CD_FTP_USER=""
SPOMS_CD_FTP_HOST=""
SPOMS_CD_FTP_PORT=""
SPOMS_CD_FTP_PATH=""
#############################################################
###          ELI FINDOC FTP Profile                         ###
#############################################################
ELI_FINDOC_FTP_COUNT="1"
ELI_FINDOC_FTP_USER="wrsftp01"
ELI_FINDOC_FTP_HOST="10.210.166.84"
ELI_FINDOC_FTP_PORT="22"
ELI_FINDOC_FTP_PATH="/appvol/wrs/data/hk/invest/eli"

#############################################################
###          SID FINDOC FTP Profile                         ###
#############################################################
SID_FINDOC_FTP_COUNT="1"
SID_FINDOC_FTP_USER="wrsftp01"
SID_FINDOC_FTP_HOST="10.210.166.84"
SID_FINDOC_FTP_PORT="22"
SID_FINDOC_FTP_PATH="/appvol/wrs/data/hk/invest/sid"

MS_CD_GB_FTP_USER="@ftp.ms.cd.user@"
MS_CD_GB_FTP_HOST="@ftp.ms.cd.host@"
MS_CD_GB_FTP_PORT="@ftp.ms.cd.port@"
MS_CD_GB_FTP_PATH="@ftp.ms.cd.path@"

#############################################################
###          TW DDS FTP Profile                         ###
#############################################################
DDS_TW_FTP_USER="@ftp.user.dds.tw@"
DDS_TW_FTP_HOST="@ftp.host.dds.tw@"
DDS_TW_FTP_PORT=@ftp.port.dds.tw@
DDS_TW_FTP_PATH="@ftp.remote.path.dds.tw@"

MS_CD_FTP_USER="@ftp.ms.cd.user@"
MS_CD_FTP_HOST="@ftp.ms.cd.host@"
MS_CD_FTP_PORT="@ftp.ms.cd.port@"
MS_CD_FTP_PATH="@ftp.ms.cd.path@"

#############################################################
###          FILE SERVER FTP Profile                      ###
#############################################################
FS_FINDOC_FTP_USER="wrsftp01"
FS_FINDOC_FTP_HOST="10.210.166.84"
FS_FINDOC_FTP_PORT="22"

ARC_FS_FINDOC_FTP_USER="arcftp02"
ARC_FS_FINDOC_FTP_HOST="ftp.dev.arc.hk.dummy"
ARC_FS_FINDOC_FTP_PORT="22"
ARC_FS_FTP_REMOTE_PATH="/dummy/arc/app/ftp/outgoing"

ENS_USER="hbap-sftp-ens-test"
ENS_HOST="hkw00108781.hbap.adroot.dummy"
ENS_PORT="22"

#############################################################
###          SMART SFTP Profile                         ###
#############################################################
SMART_GB_FTP_USER="gpbdatasvcsdev"
SMART_GB_FTP_HOST="gbl20097547.hc.cloud.example.com"
SMART_GB_FTP_PORT="22 "
SMART_GB_FTP_PATH="/dummy/gpbdatahub/dev/filetft/input/fds/wpc/"

#############################################################
###          WIM SFTP Profile                         ###
#############################################################
#WIM_HK_FTP_USER="cst01exe"
#WIM_HK_FTP_HOST="ht65-etl.hk.dummy"
#WIM_HK_FTP_PORT="22"
#WIM_HK_FTP_PATH="/ds/ht65/data/dpr_ht6502/dpr_biscst_uat/landing/wpc_mongo"
