# Cognos财富管理数据库增量测试数据生成指南

## 📊 概述

本指南详细说明了如何在现有Cognos财富管理数据库基础上生成大量增量测试数据，包括产品、债券、股票挂钩投资等核心业务数据。

## 🎯 增量数据生成目标

### 数据量目标
- **TB_PROD**: 3000条新产品记录 (ID: 4001-7000)
- **TB_PROD_USER_DEFIN_EXT_FIELD**: 9000条新扩展字段记录
- **TB_DEBT_INSTM**: 250条新债券工具记录
- **TB_EQTY_LINK_INVST**: 250条新股票挂钩投资记录
- **TB_EQTY_LINK_INVST_UNDL_STOCK**: 500条新标的股票记录
- **TB_PROD_ALT_ID**: 12000条新产品替代ID记录

**总计**: 约25000+条新记录

### 现有数据基础
- **TB_PROD**: 3001条现有记录 (ID: 1000-4000)
- **TB_PROD_USER_DEFIN_EXT_FIELD**: 5669条现有记录
- **其他关联表**: 基本为空，需要全新生成

## 📁 新增文件结构

```
mysql/product/
├── 06_generate_incremental_test_data.sql      # 增量产品和扩展字段数据生成
├── 07_generate_incremental_related_data.sql   # 增量关联表数据生成
├── execute_incremental_data_generation.sh     # 增量数据生成执行脚本
└── INCREMENTAL_DATA_GENERATION_GUIDE.md       # 本指南
```

## 🏗️ 增量数据生成策略

### 1. 产品数据增量生成 (ID: 4001-7000)

#### 产品类型分布
- **MF (互惠基金)**: ~25% - 约750条
- **BD (债券)**: ~25% - 约750条
- **ST (股票)**: ~20% - 约600条
- **ETF (交易所交易基金)**: ~10% - 约300条
- **ELI (股票挂钩投资)**: ~8% - 约250条
- **其他类型**: ~12% - 约350条

#### 增强的产品类型
新增了更多产品类型以丰富数据多样性：
- **WARRANT** (认股权证)
- **CBBC** (牛熊证)
- **OPTION** (期权)
- **REIT** (房地产投资信托)
- **FX** (外汇)
- **COMMODITY** (商品)
- **CRYPTO** (加密货币)

### 2. 业务逻辑增强

#### 产品命名逻辑
```sql
-- 基金产品命名示例
CONCAT(
    ELT(1 + FLOOR(RAND() * 10), '汇丰', '中银', '恒生', '东亚', '渣打', '花旗', '摩根', '富达', '贝莱德', '施罗德'),
    ELT(1 + FLOOR(RAND() * 8), '亚洲', '环球', '中国', '新兴市场', '欧洲', '美国', '日本', '科技'),
    ELT(1 + FLOOR(RAND() * 6), '股票', '债券', '平衡', '增长', '收益', '价值'),
    '基金', LPAD(i, 4, '0')
)
```

#### 价格生成逻辑
- **基金**: 5-55 HKD，买卖价差0.5%-1.5%
- **债券**: 95-105 HKD，买卖价差0.2%-1.0%
- **股票**: 1-501 HKD，买卖价差0.1%-1.0%
- **ETF**: 10-100 HKD，买卖价差0.3%-0.8%
- **ELI**: 10-100 HKD，买卖价差1.0%-3.0%

### 3. 扩展字段智能生成

#### 基金产品扩展字段
- **FUND_MANAGER**: 基金经理姓名
- **BENCHMARK**: 基准指数
- **MANAGEMENT_FEE**: 管理费率
- **PERFORMANCE_FEE**: 业绩费率

#### 债券产品扩展字段
- **ISSUER**: 发行人
- **CREDIT_RATING**: 信用评级
- **COUPON_RATE**: 票息率
- **YIELD_TO_MATURITY**: 到期收益率

#### 股票产品扩展字段
- **COMPANY_NAME**: 公司名称
- **MARKET_CAP**: 市值分类
- **SECTOR**: 行业分类
- **PE_RATIO**: 市盈率

### 4. 关联表数据生成

#### 债券工具表
- 自动为所有债券类型产品生成对应记录
- 包含发行人、信用评级、票息类型等详细信息
- 支持固定利率、浮动利率、零息债券等类型

#### 股票挂钩投资表
- 为ELI产品生成结构化投资信息
- 包含障碍水平、保护水平、最大收益等参数
- 支持多种ELI类型：自动赎回、障碍反向可转换等

#### 标的股票表
- 每个ELI产品生成1-4个标的资产
- 包含恒生指数、腾讯、阿里巴巴等主要标的
- 详细的价格参数：行权价、敲入价、障碍价等

## 🚀 执行方法

### 快速执行
```bash
# 进入产品目录
cd mysql/product

# 执行增量数据生成
./execute_incremental_data_generation.sh
```

### 分步执行
```bash
# 1. 生成增量产品和扩展字段数据
docker exec -i mysql-nl2sql-mvp1 mysql -u root -proot123 --default-character-set=utf8mb4 nl2sql < 06_generate_incremental_test_data.sql

# 2. 生成增量关联表数据
docker exec -i mysql-nl2sql-mvp1 mysql -u root -proot123 --default-character-set=utf8mb4 nl2sql < 07_generate_incremental_related_data.sql
```

### 验证数据
```bash
# 检查新增数据统计
docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 --default-character-set=utf8mb4 -e "
USE nl2sql;
SELECT 'TB_PROD (新增)' AS table_name, COUNT(*) AS new_records 
FROM TB_PROD WHERE PROD_ID BETWEEN 4001 AND 7000;
"
```

## 📊 数据验证

### 自动验证查询

#### 增量数据统计
```sql
-- 新增产品统计
SELECT 
    PROD_TYPE_CDE,
    COUNT(*) AS 新增数量,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM TB_PROD WHERE PROD_ID BETWEEN 4001 AND 7000), 2) AS 百分比
FROM TB_PROD 
WHERE PROD_ID BETWEEN 4001 AND 7000
GROUP BY PROD_TYPE_CDE
ORDER BY COUNT(*) DESC;

-- 扩展字段分布
SELECT 
    FIELD_CDE,
    COUNT(*) AS 字段数量
FROM TB_PROD_USER_DEFIN_EXT_FIELD 
WHERE PROD_ID BETWEEN 4001 AND 7000
GROUP BY FIELD_CDE
ORDER BY COUNT(*) DESC;
```

#### 关联关系验证
```sql
-- 验证债券数据完整性
SELECT 
    COUNT(p.PROD_ID) AS 债券产品数,
    COUNT(d.PROD_ID_DEBT_INSTM) AS 债券工具数,
    ROUND(COUNT(d.PROD_ID_DEBT_INSTM) * 100.0 / COUNT(p.PROD_ID), 2) AS 覆盖率
FROM TB_PROD p
LEFT JOIN TB_DEBT_INSTM d ON p.PROD_ID = d.PROD_ID_DEBT_INSTM
WHERE p.PROD_ID BETWEEN 4001 AND 7000 AND p.PROD_TYPE_CDE = 'BD';

-- 验证ELI数据完整性
SELECT 
    COUNT(p.PROD_ID) AS ELI产品数,
    COUNT(e.PROD_ID_EQTY_LINK_INVST) AS ELI工具数,
    COUNT(s.PROD_ID_EQTY_LINK_INVST) AS 标的股票数
FROM TB_PROD p
LEFT JOIN TB_EQTY_LINK_INVST e ON p.PROD_ID = e.PROD_ID_EQTY_LINK_INVST
LEFT JOIN TB_EQTY_LINK_INVST_UNDL_STOCK s ON e.PROD_ID_EQTY_LINK_INVST = s.PROD_ID_EQTY_LINK_INVST
WHERE p.PROD_ID BETWEEN 4001 AND 7000 AND p.PROD_TYPE_CDE = 'ELI';
```

### 数据质量检查
```sql
-- 检查价格合理性
SELECT 
    PROD_TYPE_CDE,
    MIN(PROD_MKT_PRC_AMT) AS 最低价格,
    MAX(PROD_MKT_PRC_AMT) AS 最高价格,
    AVG(PROD_MKT_PRC_AMT) AS 平均价格
FROM TB_PROD 
WHERE PROD_ID BETWEEN 4001 AND 7000 AND PROD_MKT_PRC_AMT IS NOT NULL
GROUP BY PROD_TYPE_CDE;

-- 检查中文数据显示
SELECT 
    PROD_ID,
    PROD_NAME,
    PROD_DESC
FROM TB_PROD 
WHERE PROD_ID BETWEEN 4001 AND 4005
ORDER BY PROD_ID;
```

## ⚡ 性能优化

### 执行优化
- 使用存储过程减少网络开销
- 批量插入，每500条记录提交一次
- 禁用外键检查和唯一性检查
- 使用正确的UTF8MB4字符集

### 内存优化
- 合理设置MySQL内存参数
- 监控执行过程中的内存使用
- 避免一次性生成过多数据

## 🛠️ 故障排除

### 常见问题

#### 1. 字符集问题
```bash
# 确保使用正确的字符集连接
docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 --default-character-set=utf8mb4
```

#### 2. 外键约束错误
```bash
# 检查外键约束状态
docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
USE nl2sql;
SHOW VARIABLES LIKE 'foreign_key_checks';
"
```

#### 3. 存储过程执行失败
```bash
# 检查存储过程状态
docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
USE nl2sql;
SHOW PROCEDURE STATUS WHERE Name LIKE 'GenerateIncremental%';
"
```

#### 4. 磁盘空间不足
```bash
# 检查磁盘使用
docker exec mysql-nl2sql-mvp1 df -h
```

## 📈 扩展建议

### 增加数据复杂性
- 添加更多产品类型和子类型
- 实现产品之间的关联关系
- 增加历史价格数据
- 添加交易记录和持仓数据

### 性能测试
- 测试大数据量下的查询性能
- 评估索引效果
- 优化复杂查询语句

### 业务场景扩展
- 添加客户数据和投资组合
- 实现风险管理数据
- 增加合规和监管数据

## 📞 支持

如有问题，请参考：
1. 执行脚本输出的详细日志
2. MySQL容器日志：`docker logs mysql-nl2sql-mvp1`
3. 数据验证查询结果
4. 手动检查数据完整性

通过这套增量数据生成方案，您将在现有基础上获得约25000条新的高质量测试数据，完全满足NL2SQL项目的扩展测试需求。
