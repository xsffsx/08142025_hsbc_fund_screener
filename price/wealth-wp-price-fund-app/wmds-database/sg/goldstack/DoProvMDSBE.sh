#!/bin/ksh
### **************************************************************************************************************
### * 
### * Script Name: DoProvMDSBE.sh
### * Author:      Jayshil Patel
### * Generated:   5Jul 2016
### * Purpose:     This is the base script that calls various other scripts to provision <DB-version> version 
### *              of the MDSBE database service in <DB-schema> schema(s) in <DB-name> database(s) with the 
### *              related tablespace datafiles located in the <DB-store> datastore.
### *              <DB-store> can be /OS-mountpoint or ASM-diskgroup or OMF set location.
### *
### * Usage:       $ DoProvMDSBE.sh <DB-version> <DB-schema> <DB-name> [<DB-store>]
### *         e.g. $ DoProvMDSBE.sh 1-1 mdsbea mdsbe /u06
### *         e.g. $ DoProvMDSBE.sh 1-1 mdsbea mdsbe +DATA1
### *         e.g. $ DoProvMDSBE.sh 1-1 mdsbea mdsbe 
### * 
### **************************************************************************************************************
### * 
### * History
### * Date       Version  Modifier Modification
### * 26Dec 2010 1.0      JPatel   Initial Version
### *
### * 06May 2011 1.1      JPatel   A new subroutine call PreReqSet added to set necessary GOLDSTACK expected 
### *                              database password verify function, database profiles, database roles
### *                              if missing in the target database. The asmcmd commands are removed.
### *
### * 15Jul 2011 1.2      JPatel   The order of action of ProvUserMDSBE.sh adjusted from 12 to 16 in sequence of 
### *                              actions. This fixes a bug for a new provision of schema that does not create
### *                              the default "RW" application user as schema was not getting associated with
### *                              target MDSBE.
### *
### * 15Aug 2011 1.3      JPatel   The Action 17 and 18 were swapped since Inventory need to be generated after
### *                              recompile of all objects within target schema.
### *
### * 11Nov 2011 1.4      JPatel   The Original Action 16 ( ProvUser.sh ) merged with Action 14.
### *
### * 19Feb 2013 1.5      HMKwok   Removal of DBExist checking
### * 10Dec 2013 1.6      PeterL   1. Rewrite GetVerPath function and involve with new arguments to 
### *                                 support complex version path.
### *                              2. Check FromToVersion path availability in Check 5A.
### *                              3. Bring out warning msg to user and allow to cancel upgrade in Action 7A.
### *                              4. Move /bin/ksh up to first line -- bug fix.
### *                              5. Revise DataStoreExist to allow more flexiable <DB-store>. 
### *                                 If this is a Filesystem, user have to input the full datafile directory.
### *                                 eg. /oradata/<DB-name>/datafile1   -- 2014 L2 standard
### *                                 eg. /u0n/oradata/<DB-name>/df      -- 2013 L2 standard
### *
### * 11May 2016 1.7      PeterL   1. Create goldstack framework as an independent product.
### *                              
### * 16May 2016 1.8      PeterL   1. Update PreReqSet to grant "create table" privilege to schema owner 
### *                                         -- a workaround to "alter session bug" in 11.2.0.4 afterwards.
### *                              2. Add subroutine [TagCheck] and script Archive_GS_VerTag.sh to support long version path 
### *                                         -- varchar2 limited to 4000.
### *                              3. Introduce parameter DDL_LOCK_TIMEOUT to support DBA's choice for DEPLOY_ONLINE.
### *                              4. Enforce DataStore as OMF when OMF is used, rather then blank.
### **************************************************************************************************************
 
#-- Action 1. Load the library of subroutines.
. ./UseMeMDSBE.lib
 
[ "${4}" == "" ] \
&& (
echo "Improper Usage !!"
echo "Proper Usage: ${0} <DB-version> <DB-schema> <DB-name> <DB-store>"
echo "INFO: 1-0-0 is an example here, please check the version path for the real version name!"
echo "e.g.: ${0} 1-0-0 MDSBEA TESTDB /u06/oradata/TESTDB/df"
echo "e.g.: ${0} 1-0-0 MDSBEA TESTDB /oradata/TESTDB/datafile1"
echo "e.g.: ${0} 1-0-0 MDSBEA TESTDB +DATA01"
echo "e.g.: ${0} 1-0-0 MDSBEA TESTDB OMF"

GetVerPath MDSBE
) \
&& (echo "<<DBPkg CALL ERROR !!>>") && exit 1
 
#-- Action 2. Set and display target logfile and its absolute location.
mkdir -p Sqls Logs
TStamp=$(date '+%a-%d%b%Y_%H%M')
LogFile="$(pwd)/Logs/$(basename ${0})_${1}_${2}_${3}_$(hostname)_${TStamp}.log"
echo "Logfile being generated : ${LogFile}"
 
{
 
echo "Call >> ${0} $@ begin. `date`"
 
#-- Action 3. Set local variable using input parameters.
VERSION=${1}
DB_NAME=${3}
DB_STORE=${4}
export VERSION DB_STORE

DEPLOY_ONLINE=${5}

#-- Check 1. Validate the <DB-store> datastore.
[ "${DB_STORE}" != "OMF" ] && (! (DataStoreExist "${DB_STORE}")) \
&& (echo "<<DBPkg CALL ERROR !!>>") && exit 1
#-- Check 2. Ensure the target version in the current package.
echo "INFO: $(GetVerPath MDSBE)"
(! (GetVerPath MDSBE ${VERSION} 2>/dev/null 1>/dev/null )) \
&& (echo "<<DBPkg CALL ERROR !!>>") && exit 1
 
#-- Action 4. Set DB_NAME and deduce ORACLE_SID.
ORACLE_SID=$(Deduce_SID ${DB_NAME})
export ORACLE_SID
 
#-- Check 3. Ensure the existence of <DB-name> database and ASM instance for an ASM based database.
# remove DBExist checking . sufficient with OSProcExist 20130219
#! (DBExist ${DB_NAME}) && (echo "<<DBPkg CALL ERROR !!>>") && exit 1
! (OSProcExist "ora_smon_${ORACLE_SID},Oracle database instance ${ORACLE_SID}") && (echo "<<DBPkg CALL ERROR !!>>") \
&& exit 1
[ "$(echo ${DB_STORE} | cut -c1)" == "+" ] && (! (OSProcExist "asm_smon_+ASM,Oracle ASM instance")) \
&& (echo "<<DBPkg CALL ERROR !!>>") && exit 1
 
#-- Check 4. Ensure that the necessary config is done for OMF for an OMF enabled <DB-name> database.
[ "${DB_STORE}" = "OMF" ] && (echo "OMF enabled database ${DB_NAME} is considered...") \
&& (! (DBThingExist "PARAMETER,DB_CREATE_FILE_DEST:SET?,${DB_NAME}")) \
&& (echo "<<DBPkg CALL ERROR !!>>") && exit 1

#-- Action 5. Set SCHEMA_NAME
SCHEMA_NAME=$( echo ${2} | tr [a-z] [A-Z] )
 
#-- Check 5. Ensure the necessary GOLDSTACK specific password verify function,  
#-- Check 5. database profiles, database roles exist in the target <DB-name> database.
! (PreReqSet ${DB_NAME} ${SCHEMA_NAME}) && (echo "<<DBPkg CALL ERROR !!>>") && exit 1

#-- Action 6. Create data file directory for file system based database store.
if [ "$(echo ${DB_STORE} | cut -c1)" == "/" ] ;
then

## On some database cluster - <DB-store> OS-mountpoint have oradata embedded e.g. /u04/oradata in TKO database cluster.
## Following command removes the extra '/oradata' at end.
DB_STORE=$(echo ${DB_STORE} | sed -e "s/\/oradata$//g")
## On some database cluster - <DB-store> OS-mountpoint have oradata embedded e.g. /u04/oradata in TKO database cluster.

fi

###### 
##e.g. Database Service/Create date: {ICCM/05Apr2011}
##e.g. Version/Provision date: {1-2-1-7/05Apr2011}:{1-3-0-0/05Apr2011}:{1-4-0-0/05Apr2011}
###### 

#-- Action 7. Ensure the <DB-schema> schema is not already provisioned for the <DB-version>.
#-- Action 7. Deduce suitable version path based on versions (if any) deployed in selected schema in selected database
#-- Action 7. and input target version.

SrvInDB=$(DBSrvInfo ${SCHEMA_NAME},${DB_NAME} | grep "^Database Service/Create date")
VerInDB=$(DBSrvInfo ${SCHEMA_NAME},${DB_NAME} | grep "^Version/Provision date")
[ $(echo ${SrvInDB} | grep "{MDSBE/" | wc -l) -ne 0 ] \
&& [ $(echo ${VerInDB} | grep "{${VERSION}/" | wc -l) -ne 0 ] \
&& ( echo "INFO: ${VERSION} of MDSBE database service is already provisioned in ${SCHEMA_NAME} schema of ${DB_NAME} database !" ) \
&& ( echo "INFO: Versions in ${SCHEMA_NAME} schema of ${DB_NAME} database=${VerInDB}" ) \
&& exit 0

LastVerInDB=$(echo ${VerInDB} | awk -F"{" '{ print $NF }' | cut -d"/" -f1)
#-- Check 5A. Ensure the version path is available.
(! (GetVerPath MDSBE ${VERSION} ${LastVerInDB})) \
&& (echo "<<DBPkg CALL ERROR !!>>") && exit 1

if [ "${LastVerInDB}" != "" ];
then
 VERSION_PATH=$(GetVerPath MDSBE ${VERSION} ${LastVerInDB} | grep "^INFO: From to version path:" | cut -d":" -f3)
 (! (GetVerPath MDSBE ${VERSION} ${LastVerInDB} 2>/dev/null 1>/dev/null )) && (echo "<<DBPkg CALL ERROR !!>>") \
 && exit 1
 FROM_VERSION=$( echo ${VERSION_PATH} | awk '{ print substr($1,1,index($1,">")-1) }' )
 VP=$( echo ${VERSION_PATH} | awk '{ print substr($1,index($1,">")+1) }' )
 VERSION_PATH=${VP}
else
 VERSION_PATH=$(GetVerPath MDSBE ${VERSION} | grep "^INFO: From to version path:" | cut -d":" -f3)
 (! (GetVerPath MDSBE ${VERSION} 2>/dev/null 1>/dev/null )) && (echo "<<DBPkg CALL ERROR !!>>") \
 && exit 1
 FROM_VERSION=""
fi

} > ${LogFile}
 
#-- Action 7A. Bring out warning message to user and allow break in 30 Seconds if this is a upgrade
if [ "${FROM_VERSION}" != "" ];then
    echo ""
    echo "*******************************   WARNING   *******************************************"
    echo "You see this warning as it is an upgrade on top of existing schema. Please read! "
    sleep 3
    echo "Current Goldstack DB service version detected in database is: $LastVerInDB"
    sleep 3
    echo "You are going to deploy goldstack upgrade code of version: ${VERSION_PATH}"
    sleep 3
    echo "If anything is in doubt, please CANCEL in 30 Seconds! "
    echo "***************************************************************************************"
    sleep 3
    echo "Ticking ..."
    sleep 30
    echo "Upgrade start ..."
fi

{
#-- Action 8. Begin loop for versions in the deduced version path in the selected schema within 
#-- Action 8. the selected database ( source ${1} ) separted by ">" in the deduced version path.

for TO_VERSION in $(echo "${VERSION_PATH}" | sed -e "s/>/ /g")
do
 
echo "INFO: Now processing version $( [ "${FROM_VERSION}" = "" ] && (echo "") || (echo "from ${FROM_VERSION} to ")) ${TO_VERSION} ..."
(TagExist "DBVer,${TO_VERSION},${SCHEMA_NAME},${DB_NAME}" 1>/dev/null) && FROM_VERSION=${TO_VERSION} \
&& exit 1

#-- Action 9. Fork for provision process or upgrade process.

if [ "${FROM_VERSION}" = "" ];
then

#-- Check 6. Ensure the non-existence of <DB-schema> schema and roles in the <DB-name> database.
(DBThingExist "USER,${SCHEMA_NAME},${DB_NAME}") && (! (IsEmptyUser "${SCHEMA_NAME},${DB_NAME}")) && (echo "<<DBPkg CALL ERROR !!>>") \
&& exit 1
(DBThingExist "ROLE,${SCHEMA_NAME}_READ,${DB_NAME}") && (echo "<<DBPkg CALL ERROR !!>>") \
&& exit 1
(DBThingExist "ROLE,${SCHEMA_NAME}_USER,${DB_NAME}") && (echo "<<DBPkg CALL ERROR !!>>") \
&& exit 1
#-- Check 7. Ensure the non-existence of the <DB-schema>_tts and <DB-schema>_its tablespaces in the <DB-name> database.
(DBThingExist "TABLESPACE,${SCHEMA_NAME}_tts,${DB_NAME}") && (echo "<<DBPkg CALL ERROR !!>>") \
&& exit 1
(DBThingExist "TABLESPACE,${SCHEMA_NAME}_its,${DB_NAME}") && (echo "<<DBPkg CALL ERROR !!>>") \
&& exit 1
#-- Check 8. Ensure the existence of necessary create schema script.
! (FileExist "Create_Schema_MDSBE${TO_VERSION}.sql,MDSBE schema create ") && (echo "<<DBPkg CALL ERROR !!>>") \
&& exit 1

#-- Action 10. Create tablespace/schemas/roles for provision process.
sqlplus <<!
sys as sysdba

start Create_TS_MDSBE.sql ${SCHEMA_NAME} ${DB_NAME} "${DB_STORE}"
start Create_User_MDSBE.sql ${SCHEMA_NAME}

!

#-- Check 9. Ensure the existence of <DB-schema> schema in the <DB-name> database.
! (DBThingExist "USER,${SCHEMA_NAME},${DB_NAME}") && (echo "<<DBPkg CALL ERROR !!>>") && exit 1
#-- Check 10. Ensure the existence of the <DB-schema>_tts and <DB-schema>_its tablespaces in the <DB-name> database.
! (DBThingExist "TABLESPACE,${SCHEMA_NAME}_tts,${DB_NAME}") && (echo "<<DBPkg CALL ERROR !!>>") && exit 1
! (DBThingExist "TABLESPACE,${SCHEMA_NAME}_its,${DB_NAME}") && (echo "<<DBPkg CALL ERROR !!>>") && exit 1

#-- Action 11. Create database service specific version specific database objects in selected schema in selected database.
sqlplus <<!
sys as sysdba

start Create_Schema_MDSBE${TO_VERSION}.sql ${SCHEMA_NAME}
!

#-- Action 12. Set Schema Profile to be STANDARD_16_NORES 
! (SetGSComp "${SCHEMA_NAME},STANDARD_16_NORES") && (echo "<<DBPkg CALL ERROR !!>>") && exit 1
 
else

#-- Check 6. Ensure the <DB-schema> schema is configured for <DB-service> database service in the <DB-name> database.
! (TagExist "DBSvc,MDSBE,${SCHEMA_NAME},${DB_NAME}") && (echo "<<DBPkg CALL ERROR !!>>") && exit 1
#-- Check 7. Ensure that the <DB-schema> schema is configured with the <FromDB-version> version in the <DB-name> database.
! (TagExist "DBVer,${FROM_VERSION},${SCHEMA_NAME},${DB_NAME}") && (DBSrvInfo "${SCHEMA_NAME},${DB_NAME}") \
&& (echo "<<DBPkg CALL ERROR !!>>") && exit 1
#-- Check 8. Ensure that the <DB-schema> schema is not already configured/upgraded to the new <ToDB-version> version in the <DB-name> database.
(TagExist "DBVer,${TO_VERSION},${SCHEMA_NAME},${DB_NAME}") && (DBSrvInfo "${SCHEMA_NAME},${DB_NAME}") \
&& (echo "<<DBPkg CALL ERROR !!>>") && exit 1

#-- Check 8A. Ensure the length of version tag is not exceed.
#-- ! (TagCheck "DBSvc,MDSBE,${SCHEMA_NAME},${DB_NAME}") && (echo "<<DBPkg CALL ERROR !!>>") && exit 1
EXCEED="NO"
! (TagCheck "DBSvc,MDSBE,${SCHEMA_NAME},${DB_NAME}") && EXCEED="YES"
if [ "YES" == $EXCEED ]; then
VerInDB2=$(DBSrvInfo ${SCHEMA_NAME},${DB_NAME} | grep "^Version/Provision date")
LastVerInDB2=$(echo ${VerInDB2} | awk -F"{" '{ print $NF }' | cut -d"/" -f1)
echo "INFO: DB Service version before archive is $LastVerInDB2 ."

! ( ./Archive_GS_VerTag.sh MDSBE ${LastVerInDB2} ${SCHEMA_NAME} ${DB_NAME} ) && (echo "<<ARCHIVE VERSION TAG ERROR !!>>") && exit 1

VerInDB3=$(DBSrvInfo ${SCHEMA_NAME},${DB_NAME} | grep "^Version/Provision date")
LastVerInDB3=$(echo ${VerInDB3} | awk -F"{" '{ print $NF }' | cut -d"/" -f1) 
echo "INFO: DB Service version  after archive is $LastVerInDB3 ."

[ $LastVerInDB2 != $LastVerInDB3 ] && (echo "<<ERROR: VERSION TAG UNMATCH AFTER ARCHIVE !!>>") && exit 1
[ ${FROM_VERSION} != $LastVerInDB3 ] && (echo "<<ERROR: VERSION TAG UNMATCH !!>>") && exit 1

fi


#-- Check 9. Ensure that <DB-schema> schema is not being accessed in the <DB-name> database.
if [ "DEPLOY_ONLINE" != "${DEPLOY_ONLINE}" ];then
        ! (CheckAccess "${SCHEMA_NAME},${DB_NAME}") && (echo "<<DBPkg CALL ERROR !!>>") && exit 1
fi
#-- Check 10. Ensure the existence of necessary upgrade script.
! (FileExist "Upgrade_Schema_MDSBE_${FROM_VERSION}_${TO_VERSION}.sql,MDSBE schema upgrade ") && (echo "<<DBPkg CALL ERROR !!>>") \
&& exit 1

#-- Action 13. Upgrade the selected schema with selected version specific database objects in selected schema in selected database.
sqlplus <<!
sys as sysdba
ALTER SESSION SET DDL_LOCK_TIMEOUT = 60;

start Upgrade_Schema_MDSBE_${FROM_VERSION}_${TO_VERSION}.sql ${SCHEMA_NAME} ${DB_NAME} "${DB_STORE}"
!

fi

#-- Action 14. Stamp the appropriate database service and database version in the selected schema in selected database.
#-- Action 14. Create standard application user specific to selected schema in selected database.

sqlplus <<!
sys as sysdba

start Create_Comments_MDSBE.sql ${SCHEMA_NAME} DB-service MDSBE
start Create_Comments_MDSBE.sql ${SCHEMA_NAME} DB-version ${TO_VERSION}
!

[ "${FROM_VERSION}" = "" ] \
&& ( ./ProvUserMDSBE.sh ${SCHEMA_NAME}_APPUSER ${SCHEMA_NAME} ${DB_NAME} "RW" )

FROM_VERSION=${TO_VERSION}
#for TO_VERSION...
done

#-- Action 15. Perform following in the selected schema in selected database:
#-- Action 15. a) Grant appropriate database object privileges to appropriate readonly and readwrite roles
#-- Action 15. b) Locate table/lob and index segments in their respective _tts and _its tablespaces
#-- Action 15. c) Create private synonyms for each schema object in the related application user(s)
sqlplus <<!
sys as sysdba

start Create_Grants_MDSBE.sql ${SCHEMA_NAME}
start Locate_Segments_MDSBE.sql ${SCHEMA_NAME}
start Create_Synonyms_MDSBE.sql ${SCHEMA_NAME}
!

#-- Action 16. Perform database wide re-compilation of INVALID database objects.
sqlplus <<!
sys as sysdba

set echo off
@?/rdbms/admin/utlrp.sql;

!
 
#-- Action 17. Generate and display the schema inventory
DBSrvInfoSum "${SCHEMA_NAME},${DB_NAME}"

echo "Call >> ${0} $@ end. `date`"
 
} >> ${LogFile}
echo "Goldstack deployment exit." 
exit 0
### *************************************************END OF FILE**************************************************
