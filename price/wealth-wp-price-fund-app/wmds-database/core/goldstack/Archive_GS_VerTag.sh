#!/bin/ksh
### **************************************************************************************************************
### *
### * Script Name: Archive_GS_VerTag.sh
### * Author:      Peter P C LEI
### * Date:        01Jun2015
### * Purpose:     As the goldstack framework supports the version tag as long as 2000 characters, we need this tool
### *              to archive the old version information then reset current version tag in DBA_TAB_COMMENTS.
### *
### * Usage:       $ Archive_GS_VerTag.sh <Current DB-Service Version> <DB-schema> <DB-name>
### *         e.g. $ Archive_GS_VerTag.sh 3-0-5z02 MDSBEA TESTGS
### *
### **************************************************************************************************************
### *
### * History
### * Date       Version  Modifier    Modification
### * 01Jun2015  1.0      Peter Lei   Initial Version
### **************************************************************************************************************

. ./UseMeMDSBE.lib

[ "${4}" == "" ] \
&& (
echo "Improper Usage !!"
echo "Proper Usage: ${0} <GS DB-Service> <Current DB-Service Version> <DB-schema> <DB-name>"
echo ""
echo "e.g.: ${0} MDSBE 3-0-5z02 MDSBEA TESTDB"
echo "e.g.: Archive version tag to table MDSBEA_<DATE>, then reset current DB service version of schema MDSBEA to 3-0-5z02 in DBA_TAB_COMMENTS." 
echo "e.g.: <Current DB-Service Version> must be correct, otherwise it will apply wrong script in consequent upgrade."

) \
&& (echo "<<DBPkg CALL ERROR !!>>") && exit 1

#-- Set local variable using input parameters.
GS_DB_SERVICE=${1}
VERSION=${2}
SCHEMA_NAME=$( echo ${3} | tr [a-z] [A-Z] )
DB_NAME=${4}

ORACLE_SID=$(Deduce_SID ${DB_NAME})
export ORACLE_SID

mkdir -p Sqls Logs
chmod 777 Sqls Logs

TStamp=$(date '+%a-%d%b%Y_%H%M')
LogFile="$(pwd)/Logs/$(basename ${0})_${1}_${2}_${3}_${4}_$(hostname)_${TStamp}.log"
echo "INFO: [ Logfile being generated ] ${LogFile}"

{

#-- Check 1. Ensure the <DB-name> database is up and running.
! (OSProcExist "ora_smon_${ORACLE_SID},Oracle database instance ${ORACLE_SID}") && (echo "<<DBPkg CALL ERROR !!>>") \
&& exit 1

echo "Call >> ${0} ${1} ${2} ${3} ${4} begin. `date`"

#-- Backup DBA_TAB_COMMENTS then check.
DATETAG=$(date '+%y%m%d%S')
sqlplus / as sysdba <<!
start Create_Comments_BACKUP.sql ${SCHEMA_NAME} ${DATETAG}

!

(cat Logs/Create_Comments_BACKUP_${SCHEMA_NAME}_${DATETAG}.log | grep 'ORA-') && (echo "<<ERROR BACKUP DBA_TAB_COMMENTS !!>>") && exit 1

sqlplus / as sysdba <<!
purge dba_recyclebin;
start Clear_Comments.sql ${SCHEMA_NAME} DB-service ${GS_DB_SERVICE}
start Create_Comments_MDSBE.sql ${SCHEMA_NAME} DB-service ${GS_DB_SERVICE}
start Create_Comments_MDSBE.sql ${SCHEMA_NAME} DB-version ${VERSION}

!

echo "Call >> ${0} ${1} ${2} ${3} end. `date`"

} > ${LogFile}

return 0

