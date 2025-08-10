-- Oracle健康检查脚本
SET PAGESIZE 0
SET FEEDBACK OFF
SET HEADING OFF

SELECT 'Oracle Database is running' FROM dual;

-- 检查PDB状态
SELECT 'PDB Status: ' || open_mode FROM v$pdbs WHERE name = 'ORCLPDB1';

-- 检查监听器状态
SELECT 'Listener Status: Active' FROM dual WHERE EXISTS (
    SELECT 1 FROM v$listener_network WHERE type = 'tcp'
);

EXIT;
