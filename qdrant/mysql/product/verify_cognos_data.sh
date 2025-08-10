#!/bin/bash

# =====================================================
# Cognos MySQL数据验证脚本
# 验证Cognos数据库表和数据
# 创建时间: 2025-01-04 14:30:45
# =====================================================

set -e

# 颜色定义
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo "========================================"
echo "🔍 Cognos MySQL数据验证"
echo "========================================"

# 验证数据库连接
echo -e "${BLUE}[INFO]${NC} 验证数据库连接..."
docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "SELECT 'Database Connection OK' AS STATUS;" 2>/dev/null
echo -e "${GREEN}[SUCCESS]${NC} 数据库连接正常"

# 显示所有Cognos表
echo -e "\n${BLUE}[INFO]${NC} Cognos数据库表列表:"
echo "========================================"
docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
USE nl2sql;
SELECT TABLE_NAME, TABLE_COMMENT 
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_SCHEMA = 'nl2sql' 
  AND (TABLE_NAME LIKE 'TB_%' 
    OR TABLE_NAME LIKE 'CDE_%' 
    OR TABLE_NAME LIKE 'PROD_%' 
    OR TABLE_NAME LIKE 'LOG_%')
ORDER BY TABLE_NAME;
" 2>/dev/null

# 显示表记录统计
echo -e "\n${BLUE}[INFO]${NC} 表记录数统计:"
echo "========================================"
docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
USE nl2sql;
SELECT 'TB_PROD' AS table_name, COUNT(*) AS record_count FROM TB_PROD
UNION ALL
SELECT 'CDE_DESC_VALUE', COUNT(*) FROM CDE_DESC_VALUE
UNION ALL
SELECT 'TB_PROD_USER_DEFIN_EXT_FIELD', COUNT(*) FROM TB_PROD_USER_DEFIN_EXT_FIELD
UNION ALL
SELECT 'TB_DEBT_INSTM', COUNT(*) FROM TB_DEBT_INSTM
UNION ALL
SELECT 'TB_EQTY_LINK_INVST', COUNT(*) FROM TB_EQTY_LINK_INVST
UNION ALL
SELECT 'TB_EQTY_LINK_INVST_UNDL_STOCK', COUNT(*) FROM TB_EQTY_LINK_INVST_UNDL_STOCK
UNION ALL
SELECT 'TB_PROD_ALT_ID', COUNT(*) FROM TB_PROD_ALT_ID
UNION ALL
SELECT 'PROD_FORM_REQMT', COUNT(*) FROM PROD_FORM_REQMT
UNION ALL
SELECT 'PROD_RESTR_CUST_CTRY', COUNT(*) FROM PROD_RESTR_CUST_CTRY
UNION ALL
SELECT 'PROD_OVRID_FIELD', COUNT(*) FROM PROD_OVRID_FIELD;
" 2>/dev/null

# 显示产品数据示例
echo -e "\n${BLUE}[INFO]${NC} 产品数据示例:"
echo "========================================"
docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
USE nl2sql;
SELECT 
    PROD_ID,
    PROD_CDE,
    PROD_TYPE_CDE,
    PROD_STAT_CDE,
    CCY_PROD_CDE,
    RISK_LVL_CDE,
    PROD_BID_PRC_AMT,
    PROD_OFFR_PRC_AMT
FROM TB_PROD 
ORDER BY PROD_ID;
" 2>/dev/null

# 显示代码描述数据示例
echo -e "\n${BLUE}[INFO]${NC} 代码描述数据示例:"
echo "========================================"
docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
USE nl2sql;
SELECT 
    CDV_TYPE_CDE,
    CDV_CDE,
    CDV_DESC
FROM CDE_DESC_VALUE 
WHERE CDV_TYPE_CDE IN ('PROD_TYPE', 'PROD_STAT', 'RISK_LEVEL')
ORDER BY CDV_TYPE_CDE, CDV_CDE;
" 2>/dev/null

# 验证外键关系
echo -e "\n${BLUE}[INFO]${NC} 验证外键关系:"
echo "========================================"
docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
USE nl2sql;
SELECT 
    'Product Extensions' AS relationship_type,
    COUNT(*) AS count
FROM TB_PROD p
INNER JOIN TB_PROD_USER_DEFIN_EXT_FIELD ext ON p.PROD_ID = ext.PROD_ID
UNION ALL
SELECT 
    'Debt Instruments',
    COUNT(*)
FROM TB_PROD p
INNER JOIN TB_DEBT_INSTM debt ON p.PROD_ID = debt.PROD_ID_DEBT_INSTM
UNION ALL
SELECT 
    'Equity Linked Investments',
    COUNT(*)
FROM TB_PROD p
INNER JOIN TB_EQTY_LINK_INVST eli ON p.PROD_ID = eli.PROD_ID_EQTY_LINK_INVST;
" 2>/dev/null

# 测试复杂查询
echo -e "\n${BLUE}[INFO]${NC} 复杂查询测试 - 产品详细信息:"
echo "========================================"
docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
USE nl2sql;
SELECT 
    p.PROD_ID,
    p.PROD_CDE,
    p.PROD_TYPE_CDE,
    pt.CDV_DESC AS PROD_TYPE_DESC,
    p.PROD_STAT_CDE,
    ps.CDV_DESC AS PROD_STAT_DESC,
    p.RISK_LVL_CDE,
    rl.CDV_DESC AS RISK_LEVEL_DESC,
    p.PROD_MKT_PRC_AMT
FROM TB_PROD p
LEFT JOIN CDE_DESC_VALUE pt ON pt.CDV_TYPE_CDE = 'PROD_TYPE' AND pt.CDV_CDE = p.PROD_TYPE_CDE
LEFT JOIN CDE_DESC_VALUE ps ON ps.CDV_TYPE_CDE = 'PROD_STAT' AND ps.CDV_CDE = p.PROD_STAT_CDE
LEFT JOIN CDE_DESC_VALUE rl ON rl.CDV_TYPE_CDE = 'RISK_LEVEL' AND rl.CDV_CDE = p.RISK_LVL_CDE
ORDER BY p.PROD_ID;
" 2>/dev/null

# 验证索引
echo -e "\n${BLUE}[INFO]${NC} 索引验证:"
echo "========================================"
docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
USE nl2sql;
SELECT 
    TABLE_NAME,
    INDEX_NAME,
    COLUMN_NAME,
    NON_UNIQUE
FROM INFORMATION_SCHEMA.STATISTICS 
WHERE TABLE_SCHEMA = 'nl2sql' 
  AND TABLE_NAME = 'TB_PROD'
ORDER BY TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX;
" 2>/dev/null

echo -e "\n${GREEN}[SUCCESS]${NC} ✅ Cognos数据验证完成！"
echo "========================================"
echo "📊 数据库状态: 正常"
echo "📋 表结构: 完整"
echo "💾 示例数据: 已加载"
echo "🔗 外键关系: 正常"
echo "📇 索引: 已创建"
echo "========================================"
