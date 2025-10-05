#!/bin/ksh
### ******************************************************************************************************************
### *
### * Script Name: ProvUserMDSBE.sh
### * Author:      Jayshil Patel
### * Generated:   5Jul 2016 
### * Purpose:     This script creates <DB-user> application user(s) of MDSBE database service
### *              for read or read/write purpose for the <DB-schema> schema
### *              in <DB-name> database(s).
### *
### * Usage:       $ ProvUserMDSBE.sh <DB-user[,[...]]> <DB-schema> <DB-name[,[...]]> [<DB-access>]
### *         e.g. $ ProvUserMDSBE.sh mdsbe_a1,mdsbe_a2 mdsbea mdsbe <R|RW>
### *
### ******************************************************************************************************************
### *
### * History
### * Date       Version  Modifier Modification
### * 31Jan 2009 1.0      JPatel   Initial Version
### *
### * 10Jul 2009 1.1      JPatel   Input parameters now support multi-values.
### *                              The values in multi-values are separated by comma(,).
### *
### * 19Jan 2010 1.2      JPatel   New parameter for DB access (i.e. read or read/write) added.
### *
### * 22Feb 2010 1.3      JPatel   Fixed a BUG of ORACLE_SID generation before instance check.
### *
### * 31May 2011 1.4      CBoot    1. A new subroutine call PreReqSet added to set necessary GOLDSTACK expected 
### *                                 database password verify function, database profiles, database roles
### *                                 if missing in the target database. The asmcmd commands are removed.
### *                              2. Additional subroutine SetGSComp to set GOLDSTACK_SCHEMA_PROFILE to provisioned  
### *                                 schema owner.
### *
### * 19Feb 2013 1.5      HMKwok   Removal of DBExist checking
### *
### * 16May 2016 1.6      PeterL   Replace GOLDSTACK_USER_PROFILE with DOS_16_NORES
### * 
### * 05Jul 2016 1.7      PeterL   Update non-existence checking of appuser. If empty user exists the script will continue.
### ******************************************************************************************************************
 
[ "${3}" == "" ] \
&& (
echo "Improper Usage !!"
echo "Proper Usage: ${0} <DB-user[,[...]]> <DB-schema> <DB-name[,[...]]> [<DB-access>]"
echo "e.g.: ${0} mdsbe_a1,mdsbe_a2 mdsbea TESTDB <R|RW>"
) \
&& (echo "<<DBPkg CALL ERROR !!>>") && exit 1

. ./UseMeMDSBE.lib

mkdir -p Sqls Logs
TStamp=$(date '+%a-%d%b%Y_%H%M')
LogFile="$(pwd)/Logs/$(basename ${0})_${1}_${2}_${3}_$(hostname)_${TStamp}.log"
echo "Logfile being generated : ${LogFile}"
 
{

echo "Call >> ${0} $@ begin. `date`"
 
DB_ACCESS=$(echo ${4:-RW} | tr '[a-z]' '[A-Z]')
export DB_ACCESS

#-- 1. Ensure the value of <DB-access> is valid.
! (ValueExist "${DB_ACCESS},Database access :" "R,RW") && (echo "<<DBPkg CALL ERROR !!>>") && exit 1

SCHEMA_NAME=`echo ${2} | tr [a-z] [A-Z]`
export SCHEMA_NAME

for DB_NAME in $(echo ${3} | sed -e 's/,/ /g')
do

ORACLE_SID=$(Deduce_SID ${DB_NAME})
export ORACLE_SID

#-- 2. Ensure the existence of <DB-name> database.
#Removal of checking 20130219
#! (DBExist ${DB_NAME}) && (echo "<<DBPkg CALL ERROR !!>>") && continue;
! (OSProcExist "ora_smon_${ORACLE_SID},Oracle database instance ${ORACLE_SID}") && (echo "<<DBPkg CALL ERROR !!>>") && continue;

#-- 3. Ensure the necessary GOLDSTACK specific password verify function, 
#      database profiles, database roles exist in the target <DB-name> database.
! (PreReqSet ${DB_NAME}) && (echo "<<DBPkg CALL ERROR !!>>") && continue;

[ "${DB_ACCESS}" = "RW" ] && (! (DBThingExist "ROLE,${SCHEMA_NAME}_USER,${DB_NAME}")) \
&& (echo "<<DBPkg CALL ERROR !!>>") && continue;
[ "${DB_ACCESS}" = "R" ] && (! (DBThingExist "ROLE,${SCHEMA_NAME}_READ,${DB_NAME}")) \
&& (echo "<<DBPkg CALL ERROR !!>>") && continue;
! (DBThingExist "TABLESPACE,${SCHEMA_NAME}_TTS,${DB_NAME}") && (echo "<<DBPkg CALL ERROR !!>>") && continue;
! (DBThingExist "USER,${SCHEMA_NAME},${DB_NAME}") && (echo "<<DBPkg CALL ERROR !!>>") && continue;

for APPUSER_NAME in $(echo ${1} | sed -e 's/,/ /g')
do

#-- 4. Ensure the non-existence of <DB-user> database user in the <DB-name> database.
(DBThingExist "USER,${APPUSER_NAME},${DB_NAME}") && (! (IsEmptyUser "${APPUSER_NAME},${DB_NAME}")) && (echo "<<DBPkg CALL ERROR !!>>") && continue;
#-- 5. Ensure that the <DB-schema> schema is configured for <DB-service> database service in the <DB-name> database.
! (TagExist "DBSvc,MDSBE,${SCHEMA_NAME},${DB_NAME}") && (echo "<<DBPkg CALL ERROR !!>>") && continue;

sqlplus <<!
sys as sysdba

start Create_Appuser_MDSBE.sql ${SCHEMA_NAME} ${APPUSER_NAME} ${DB_ACCESS}
start Create_Synonyms_MDSBE.sql ${SCHEMA_NAME}

!

#Set Schema Profile to be DOS_16_NORES
! (SetGSComp "${APPUSER_NAME},DOS_16_NORES") && (echo "<<DBPkg CALL ERROR !!>>") && exit 0

##for APPUSER_NAME...
done
##for DB_NAME...
done

echo "Call >> ${0} $@ end. `date`"
 
} > ${LogFile}

exit 0
### *****************************************************END OF FILE**************************************************
