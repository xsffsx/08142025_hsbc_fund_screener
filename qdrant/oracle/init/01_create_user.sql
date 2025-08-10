-- Oracle 19c 初始化脚本
-- 创建NL2SQL用户和表空间

-- 连接到PDB (修复：使用正确的PDB名称)
ALTER SESSION SET CONTAINER = XEPDB1;

-- 创建表空间
CREATE TABLESPACE NL2SQL_DATA
DATAFILE '/opt/oracle/oradata/FREE/XEPDB1/nl2sql_data01.dbf'
SIZE 100M
AUTOEXTEND ON
NEXT 10M
MAXSIZE UNLIMITED;

-- 创建用户
CREATE USER nl2sql_user IDENTIFIED BY nl2sql_pass
DEFAULT TABLESPACE NL2SQL_DATA
TEMPORARY TABLESPACE TEMP
QUOTA UNLIMITED ON NL2SQL_DATA;

-- 授权
GRANT CONNECT, RESOURCE, CREATE VIEW, CREATE SYNONYM TO nl2sql_user;
GRANT CREATE SESSION TO nl2sql_user;
GRANT SELECT_CATALOG_ROLE TO nl2sql_user;

-- 创建基础表结构（示例）
CONNECT nl2sql_user/nl2sql_pass@XEPDB1;

-- 创建测试表
CREATE TABLE employees (
    employee_id NUMBER(6) PRIMARY KEY,
    first_name VARCHAR2(20),
    last_name VARCHAR2(25) NOT NULL,
    email VARCHAR2(25) NOT NULL UNIQUE,
    phone_number VARCHAR2(20),
    hire_date DATE NOT NULL,
    job_id VARCHAR2(10) NOT NULL,
    salary NUMBER(8,2),
    commission_pct NUMBER(2,2),
    manager_id NUMBER(6),
    department_id NUMBER(4)
);

-- 插入测试数据
INSERT INTO employees VALUES (100, 'Steven', 'King', 'SKING', '515.123.4567', DATE '2003-06-17', 'AD_PRES', 24000, NULL, NULL, 90);
INSERT INTO employees VALUES (101, 'Neena', 'Kochhar', 'NKOCHHAR', '515.123.4568', DATE '2005-09-21', 'AD_VP', 17000, NULL, 100, 90);
INSERT INTO employees VALUES (102, 'Lex', 'De Haan', 'LDEHAAN', '515.123.4569', DATE '2001-01-13', 'AD_VP', 17000, NULL, 100, 90);

COMMIT;

-- 创建视图
CREATE VIEW employee_summary AS
SELECT 
    department_id,
    COUNT(*) as employee_count,
    AVG(salary) as avg_salary,
    MAX(salary) as max_salary,
    MIN(salary) as min_salary
FROM employees
GROUP BY department_id;

COMMIT;
