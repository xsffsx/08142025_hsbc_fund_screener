# Cognos大量测试数据生成指南

## 📊 概述

本指南详细说明了如何为Cognos财富管理数据库生成大量测试数据，包括数据结构分析、生成策略和执行方法。

## 🎯 数据生成目标

### 数据量目标
- **TB_PROD**: 3000条产品记录
- **CDE_DESC_VALUE**: 500条代码描述记录
- **TB_PROD_USER_DEFIN_EXT_FIELD**: 8000条扩展字段记录
- **TB_DEBT_INSTM**: 800条债券工具记录
- **TB_EQTY_LINK_INVST**: 200条股票挂钩投资记录
- **TB_EQTY_LINK_INVST_UNDL_STOCK**: 400条标的股票记录
- **TB_PROD_ALT_ID**: 12000条产品替代ID记录
- **PROD_FORM_REQMT**: 6000条表单要求记录
- **PROD_RESTR_CUST_CTRY**: 1500条地域限制记录
- **PROD_OVRID_FIELD**: 1000条字段覆盖记录

**总计**: 约32000+条记录

## 📁 文件结构

```
mysql/product/
├── 01_create_cognos_tables_mysql.sql      # 表结构创建脚本
├── 02_insert_sample_data.sql              # 示例数据插入脚本
├── 03_generate_bulk_test_data.sql          # 大量数据生成脚本(第一部分)
├── 04_generate_related_tables_data.sql     # 关联表数据生成脚本(第二部分)
├── 05_generate_auxiliary_tables_data.sql   # 辅助表数据生成脚本(第三部分)
├── execute_bulk_data_generation.sh        # 执行脚本
├── verify_cognos_data.sh                  # 验证脚本
└── BULK_DATA_GENERATION_GUIDE.md          # 本指南
```

## 🏗️ 数据生成策略

### 1. 业务逻辑一致性

#### 产品类型分布
- **MF (互惠基金)**: 40% - 主要投资产品
- **BD (债券)**: 25% - 固定收益产品
- **ETF (交易所交易基金)**: 15% - 被动投资产品
- **ST (股票)**: 10% - 个股投资
- **ELI (股票挂钩投资)**: 5% - 结构性产品
- **其他 (REIT, WARRANT, CBBC等)**: 5% - 特殊产品

#### 风险等级分布
- **等级1 (保守型)**: 15%
- **等级2 (稳健型)**: 25%
- **等级3 (平衡型)**: 30%
- **等级4 (积极型)**: 20%
- **等级5 (激进型)**: 10%

#### 货币分布
- **HKD (港币)**: 40%
- **USD (美元)**: 30%
- **EUR (欧元)**: 10%
- **其他货币**: 20%

### 2. 价格生成逻辑

#### 基金产品 (MF)
```sql
nav_price = 5 + RAND() * 50        -- 净值 5-55
bid_price = nav_price * 0.995-1.005 -- 买入价 (0.5%价差)
offer_price = nav_price * 1.005-1.015 -- 卖出价
```

#### 债券产品 (BD)
```sql
market_price = 95 + RAND() * 10     -- 市价 95-105
bid_price = market_price * 0.998-1.000 -- 买入价 (0.2%价差)
offer_price = market_price * 1.000-1.002 -- 卖出价
```

#### 股票产品 (ST)
```sql
market_price = 1 + RAND() * 500     -- 股价 1-501
bid_price = market_price * 0.999-1.000 -- 买入价 (0.1%价差)
offer_price = market_price * 1.000-1.001 -- 卖出价
```

### 3. 关联关系维护

#### 外键完整性
- 所有子表记录都有对应的父表记录
- 债券工具表只关联债券类型产品
- 股票挂钩投资表只关联ELI类型产品
- 扩展字段根据产品类型生成相应属性

#### 业务规则一致性
- 高风险产品自动添加额外合规要求
- 结构性产品自动添加地域限制
- 基金产品包含基金经理和基准信息
- 债券产品包含发行人和信用评级信息

## 🚀 执行方法

### 快速执行
```bash
# 进入产品目录
cd mysql/product

# 执行数据生成
./execute_bulk_data_generation.sh
```

### 分步执行
```bash
# 1. 创建表结构 (如果未创建)
docker exec -i mysql-nl2sql-mvp1 mysql -u root -proot123 nl2sql < 01_create_cognos_tables_mysql.sql

# 2. 生成基础数据
docker exec -i mysql-nl2sql-mvp1 mysql -u root -proot123 nl2sql < 03_generate_bulk_test_data.sql

# 3. 生成关联表数据
docker exec -i mysql-nl2sql-mvp1 mysql -u root -proot123 nl2sql < 04_generate_related_tables_data.sql

# 4. 生成辅助表数据
docker exec -i mysql-nl2sql-mvp1 mysql -u root -proot123 nl2sql < 05_generate_auxiliary_tables_data.sql

# 5. 验证数据
./verify_cognos_data.sh
```

### 手动执行 (调试用)
```bash
# 连接数据库
docker exec -it mysql-nl2sql-mvp1 mysql -u root -proot123 nl2sql

# 查看执行进度
SELECT COUNT(*) FROM TB_PROD WHERE PROD_ID >= 1000;

# 查看存储过程
SHOW PROCEDURE STATUS WHERE Name LIKE 'Generate%';

# 手动调用存储过程
CALL GenerateProducts();
```

## 📊 数据验证

### 自动验证
```bash
# 运行验证脚本
./verify_cognos_data.sh
```

### 手动验证查询

#### 基础统计
```sql
-- 产品数量统计
SELECT 
    PROD_TYPE_CDE,
    COUNT(*) AS PRODUCT_COUNT,
    ROUND(AVG(PROD_MKT_PRC_AMT), 2) AS AVG_PRICE
FROM TB_PROD 
WHERE PROD_ID >= 1000
GROUP BY PROD_TYPE_CDE;

-- 风险等级分布
SELECT 
    RISK_LVL_CDE,
    COUNT(*) AS COUNT,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM TB_PROD WHERE PROD_ID >= 1000), 2) AS PERCENTAGE
FROM TB_PROD 
WHERE PROD_ID >= 1000
GROUP BY RISK_LVL_CDE;
```

#### 关联关系验证
```sql
-- 验证外键完整性
SELECT 
    'Debt Instruments' AS relationship,
    COUNT(d.PROD_ID_DEBT_INSTM) AS child_count,
    COUNT(p.PROD_ID) AS parent_count
FROM TB_DEBT_INSTM d
LEFT JOIN TB_PROD p ON d.PROD_ID_DEBT_INSTM = p.PROD_ID
WHERE d.PROD_ID_DEBT_INSTM >= 1000;

-- 验证扩展字段分布
SELECT 
    FIELD_CDE,
    COUNT(*) AS FIELD_COUNT
FROM TB_PROD_USER_DEFIN_EXT_FIELD 
WHERE PROD_ID >= 1000
GROUP BY FIELD_CDE
ORDER BY FIELD_COUNT DESC;
```

#### 数据质量检查
```sql
-- 检查价格合理性
SELECT 
    PROD_TYPE_CDE,
    MIN(PROD_MKT_PRC_AMT) AS MIN_PRICE,
    MAX(PROD_MKT_PRC_AMT) AS MAX_PRICE,
    AVG(PROD_MKT_PRC_AMT) AS AVG_PRICE,
    STDDEV(PROD_MKT_PRC_AMT) AS PRICE_STDDEV
FROM TB_PROD 
WHERE PROD_ID >= 1000 AND PROD_MKT_PRC_AMT IS NOT NULL
GROUP BY PROD_TYPE_CDE;

-- 检查日期逻辑
SELECT 
    COUNT(*) AS invalid_dates
FROM TB_PROD 
WHERE PROD_ID >= 1000 
  AND PROD_MTUR_DT IS NOT NULL 
  AND PROD_MTUR_DT <= PROD_LNCH_DT;
```

## ⚡ 性能优化

### 执行优化
- 使用批量插入减少事务开销
- 禁用外键检查和唯一性检查
- 每1000条记录提交一次事务
- 使用存储过程减少网络开销

### 查询优化
- 为生成的数据创建适当索引
- 使用EXPLAIN分析查询计划
- 考虑分区大表提高查询性能

## 🛠️ 故障排除

### 常见问题

#### 1. 存储过程执行失败
```bash
# 检查错误日志
docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "SHOW ERRORS;"

# 手动删除存储过程
docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
USE nl2sql;
DROP PROCEDURE IF EXISTS GenerateProducts;
DROP PROCEDURE IF EXISTS GenerateExtFields;
"
```

#### 2. 内存不足
```bash
# 检查MySQL内存使用
docker stats mysql-nl2sql-mvp1

# 调整批量大小
# 修改脚本中的批量提交间隔
```

#### 3. 磁盘空间不足
```bash
# 检查磁盘使用
docker exec mysql-nl2sql-mvp1 df -h

# 清理临时文件
docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
OPTIMIZE TABLE TB_PROD;
OPTIMIZE TABLE TB_PROD_USER_DEFIN_EXT_FIELD;
"
```

#### 4. 数据不一致
```bash
# 重新生成数据
docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
USE nl2sql;
DELETE FROM TB_PROD WHERE PROD_ID >= 1000;
DELETE FROM TB_PROD_USER_DEFIN_EXT_FIELD WHERE PROD_ID >= 1000;
"

# 重新执行生成脚本
./execute_bulk_data_generation.sh
```

## 📈 扩展建议

### 增加数据量
- 修改存储过程中的max_products参数
- 调整随机数生成范围
- 增加产品类型和属性变化

### 增加复杂性
- 添加产品父子关系
- 实现更复杂的价格模型
- 增加时间序列数据

### 性能测试
- 生成更大数据集 (10万+ 记录)
- 测试复杂查询性能
- 评估索引效果

## 📞 支持

如有问题，请参考：
1. 执行脚本输出的错误信息
2. MySQL容器日志：`docker logs mysql-nl2sql-mvp1`
3. 验证脚本：`./verify_cognos_data.sh`
4. 手动数据检查查询

通过这套完整的数据生成方案，您将获得一个包含丰富、真实、一致的财富管理产品测试数据集，完全满足NL2SQL项目的测试和开发需求。
