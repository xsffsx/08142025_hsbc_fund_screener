#!/bin/ksh
### *******************************************************************************************
### *
### * Script Name: DeProvMDSBE.sh
### * Author:      Jayshil Patel
### * Generated:   5Jul 2016
### * Purpose:     This script is to de-provision the various DB artefacts for a given 
### *              MDSBE database service from the <DB-schema> schema 
### *              in <DB-name> database.
### * 
### * Usage:       $ DeProvMDSBE.sh <DB-schema> <DB-name>
### *         e.g. $ DeProvMDSBE.sh mdsbea mdsbe
### * 
### *******************************************************************************************
### * 
### * History
### * Date       Version  Modifier Modification
### * 31Jan 2009 1.0      JPatel   Initial Version
### *
### * 10Jul 2009 1.1      JPatel   Input parameters now support multi-values.
### *                              The values in multi-values are separated by comma(,).
### *
### * 18Nov 2011 1.2      JPatel   Drop the application user ${SCHEMA_NAME}_appuser.
### *                              Drop the application roles ${SCHEMA_NAME}_user/read.
### *
### * 06Dec 2011 1.3      HMKwok   Convert SCHEMA_NAME input to uppercase
### *
### * 19Feb 2013 1.4      HMKwok   Removal of DBExist checking
### *******************************************************************************************

[ "${2}" == "" ] \
&& (
echo "Improper Usage !!"
echo "Proper Usage: ${0} <DB-schema> <DB-name>"
echo "e.g.: ${0} mdsbea TESTDB"
) \
&& (echo "<<DBPkg CALL ERROR !!>>") && exit 1

. ./UseMeMDSBE.lib

mkdir -p Sqls Logs
TStamp=$(date '+%a-%d%b%Y_%H%M')
LogFile="$(pwd)/Logs/$(basename ${0})_${1}_${2}_$(hostname)_${TStamp}.log"
echo "Logfile being generated : ${LogFile}"

{
 
echo "Call >> ${0} $@ begin. `date`"

DB_NAME=${2}
ORACLE_SID=$(Deduce_SID ${DB_NAME})
export ORACLE_SID

#-- 1. Ensure the existence of <DB-name> database.
# ! (DBExist ${DB_NAME}) && (echo "<<DBPkg CALL ERROR !!>>") && exit 1;
! (OSProcExist "ora_smon_${ORACLE_SID},Oracle database instance ${ORACLE_SID}") && (echo "<<DBPkg CALL ERROR !!>>") && exit 1;

SCHEMA_NAME=$( echo ${1} | tr [a-z] [A-Z] )

#-- 2. Ensure existence of <DB-schema> schema in the <DB-name> database.
! (DBThingExist "USER,${SCHEMA_NAME},${DB_NAME}") && (echo "<<DBPkg CALL ERROR !!>>") && exit 1;
#-- 3. Ensure non-existence of live session with access to <DB-schema> schema in the <DB-name> database.
! (CheckAccess "${SCHEMA_NAME},${DB_NAME}") && (echo "<<DBPkg CALL ERROR !!>>") && exit 1;
#-- 4. Ensure that the <DB-schema> schema is configured for <DB-service> database service in the <DB-name> database.
! (TagExist "DBSvc,MDSBE,${SCHEMA_NAME},${DB_NAME}") && (echo "<<DBPkg CALL ERROR !!>>") && exit 1;

sqlplus <<!
sys as sysdba

set echo on timing on time on

REM Drop application users.

SET SERVEROUTPUT ON
BEGIN
 
 FOR APPUSERS IN ( SELECT grantee FROM (SELECT DISTINCT grantee FROM dba_tab_privs WHERE owner = '${SCHEMA_NAME}'
                    UNION ALL
                   SELECT DISTINCT grantee FROM dba_role_privs
                    WHERE granted_role IN (SELECT DISTINCT grantee FROM dba_tab_privs WHERE owner = '${SCHEMA_NAME}')
                      AND grantee NOT IN ('SYS') AND grantee NOT LIKE ('GSDEPLOY%'))
                    MINUS (SELECT ROLE FROM dba_roles)
                 )
 LOOP
 
 BEGIN
   EXECUTE IMMEDIATE 'DROP USER "'||APPUSERS.GRANTEE||'" CASCADE';
   DBMS_OUTPUT.PUT_LINE('Application user '||APPUSERS.GRANTEE||' account is dropped #');
 EXCEPTION
   WHEN OTHERS THEN
   DBMS_OUTPUT.PUT_LINE('Application user '||APPUSERS.GRANTEE||' account can not be dropped !');
 END;
 
 END LOOP;
 
END;
/

--drop user ${SCHEMA_NAME}_APPUSER cascade;

REM Drop application roles.

SET SERVEROUTPUT ON
BEGIN
 
 FOR APPROLES IN ( SELECT DISTINCT grantee FROM dba_tab_privs 
                    WHERE owner = '${SCHEMA_NAME}' AND grantee NOT IN ('SYS') AND grantee NOT LIKE ('GSDEPLOY%')
                   INTERSECT (SELECT ROLE FROM dba_roles))
 LOOP
 
 BEGIN
   EXECUTE IMMEDIATE 'DROP ROLE '||APPROLES.GRANTEE||' CASCADE';
   DBMS_OUTPUT.PUT_LINE('Database role '||APPROLES.GRANTEE||' is dropped #');
 EXCEPTION
   WHEN OTHERS THEN
   DBMS_OUTPUT.PUT_LINE('Database role '||APPROLES.GRANTEE||' can not be dropped !');
 END;
 
 END LOOP;
 
END;
/

--drop role ${SCHEMA_NAME}_user;
--drop role ${SCHEMA_NAME}_read;

REM Drop Schema.
drop user ${SCHEMA_NAME} cascade;

REM Drop tablespaces.
drop tablespace ${SCHEMA_NAME}_TTS including contents and datafiles;
drop tablespace ${SCHEMA_NAME}_ITS including contents and datafiles;

!

echo "Call >> ${0} $@ end. `date`"
 
} > ${LogFile}

exit 0
### **************************************END OF FILE******************************************
