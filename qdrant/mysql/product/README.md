# Cognos产品数据库文件

## 📁 文件说明

本目录包含Cognos财富管理产品数据库的所有相关文件，已从原PostgreSQL架构转换为MySQL兼容格式。

### 🗂️ 文件结构

```
mysql/product/
├── README.md                           # 本说明文档
├── 01_create_cognos_tables_mysql.sql   # MySQL表结构创建脚本
├── 02_insert_sample_data.sql           # 示例数据插入脚本
├── start_cognos_mysql.sh               # 启动脚本
├── verify_cognos_data.sh               # 数据验证脚本
├── README_Cognos_MySQL.md              # 详细部署指南
└── DEPLOYMENT_SUMMARY.md               # 部署总结报告
```

## 🚀 快速开始

### 1. 启动数据库

```bash
# 进入产品目录
cd mysql/product

# 运行启动脚本
./start_cognos_mysql.sh
```

### 2. 验证数据

```bash
# 运行验证脚本
./verify_cognos_data.sh
```

### 3. 手动执行SQL

```bash
# 创建表结构
docker exec -i mysql-nl2sql-mvp1 mysql -u root -proot123 nl2sql < 01_create_cognos_tables_mysql.sql

# 插入示例数据
docker exec -i mysql-nl2sql-mvp1 mysql -u root -proot123 nl2sql < 02_insert_sample_data.sql
```

## 📊 数据库架构

### 核心表结构

| 表名 | 记录数 | 说明 |
|------|--------|------|
| TB_PROD | 5 | 产品主表 - 存储所有金融产品信息 |
| CDE_DESC_VALUE | 20 | 代码描述值表 - 系统代码字典 |
| TB_PROD_USER_DEFIN_EXT_FIELD | 6 | 产品扩展字段表 - 动态属性支持 |
| TB_DEBT_INSTM | 1 | 债券工具表 - 债券特有属性 |
| TB_EQTY_LINK_INVST | 1 | 股票挂钩投资表 - 结构性产品 |
| TB_EQTY_LINK_INVST_UNDL_STOCK | 2 | 股票挂钩标的表 - 标的资产信息 |
| TB_PROD_ALT_ID | 5 | 产品替代ID表 - 多种标识符支持 |
| PROD_FORM_REQMT | 5 | 产品表单要求表 - 合规要求 |
| PROD_RESTR_CUST_CTRY | 3 | 产品限制客户国家表 - 地域限制 |
| PROD_OVRID_FIELD | 3 | 产品覆盖字段表 - 字段覆盖控制 |
| LOG_EQTY_LINK_INVST | 0 | 股票挂钩投资日志表 - 操作日志 |

### 示例产品数据

- **HKFUND001** - 汇丰成长基金 (互惠基金)
- **HKFUND002** - 汇丰收益债券基金 (互惠基金)
- **HKBOND001** - 汇丰企业债券 (债券)
- **HKELI001** - 汇丰结构性产品 (股票挂钩投资)
- **HK0005** - 汇丰控股 (股票)

## 🔧 技术特点

### PostgreSQL到MySQL转换

- ✅ **数据类型映射**: NUMERIC → DECIMAL
- ✅ **时间戳处理**: TIMESTAMP(6) + 自动更新
- ✅ **字符集支持**: utf8mb4 + unicode_ci
- ✅ **索引优化**: 复合索引 + 覆盖索引
- ✅ **外键约束**: CASCADE删除 + 引用完整性

### 核心业务功能

- ✅ **产品继承机制**: 父子产品关系支持
- ✅ **扩展字段系统**: 动态属性配置
- ✅ **多语言支持**: 中英文描述
- ✅ **风险等级管理**: 5级风险分类
- ✅ **交易权限控制**: 细粒度权限管理

## 📝 使用示例

### 基本查询

```sql
-- 查看所有产品
SELECT PROD_ID, PROD_CDE, PROD_NAME, PROD_TYPE_CDE, PROD_STAT_CDE 
FROM TB_PROD;

-- 查看产品类型分布
SELECT 
    pt.CDV_DESC AS PROD_TYPE_DESC,
    COUNT(*) AS PRODUCT_COUNT
FROM TB_PROD p
LEFT JOIN CDE_DESC_VALUE pt ON pt.CDV_TYPE_CDE = 'PROD_TYPE' AND pt.CDV_CDE = p.PROD_TYPE_CDE
GROUP BY pt.CDV_DESC;
```

### 复杂查询

```sql
-- 产品详细信息（含代码描述）
SELECT 
    p.PROD_ID,
    p.PROD_CDE,
    p.PROD_NAME,
    pt.CDV_DESC AS PROD_TYPE_DESC,
    ps.CDV_DESC AS PROD_STAT_DESC,
    rl.CDV_DESC AS RISK_LEVEL_DESC,
    p.PROD_MKT_PRC_AMT,
    p.CCY_PROD_CDE
FROM TB_PROD p
LEFT JOIN CDE_DESC_VALUE pt ON pt.CDV_TYPE_CDE = 'PROD_TYPE' AND pt.CDV_CDE = p.PROD_TYPE_CDE
LEFT JOIN CDE_DESC_VALUE ps ON ps.CDV_TYPE_CDE = 'PROD_STAT' AND ps.CDV_CDE = p.PROD_STAT_CDE
LEFT JOIN CDE_DESC_VALUE rl ON rl.CDV_TYPE_CDE = 'RISK_LEVEL' AND rl.CDV_CDE = p.RISK_LVL_CDE;
```

### 扩展字段查询

```sql
-- 查看产品扩展属性
SELECT 
    p.PROD_CDE,
    p.PROD_NAME,
    ext.FIELD_CDE,
    ext.FIELD_STRNG_VALUE_TEXT AS FIELD_VALUE
FROM TB_PROD p
JOIN TB_PROD_USER_DEFIN_EXT_FIELD ext ON p.PROD_ID = ext.PROD_ID
ORDER BY p.PROD_CDE, ext.FIELD_CDE;
```

## 🔗 相关文档

- **README_Cognos_MySQL.md** - 完整的部署和使用指南
- **DEPLOYMENT_SUMMARY.md** - 部署总结和统计信息
- **原始分析文档** - 位于 `finance_product/wealth-wp-octopus-batch-jobs-AMH-Release/doc/`

## 🛠️ 维护命令

```bash
# 查看容器状态
docker-compose ps

# 查看MySQL日志
docker-compose logs -f mysql

# 连接数据库
docker exec -it mysql-nl2sql-mvp1 mysql -u root -proot123 nl2sql

# 备份数据
docker exec mysql-nl2sql-mvp1 mysqldump -u root -proot123 nl2sql > backup.sql

# 停止服务
docker-compose down
```

## 📞 支持

如有问题，请参考：
1. 详细部署指南：`README_Cognos_MySQL.md`
2. 部署总结报告：`DEPLOYMENT_SUMMARY.md`
3. 验证脚本：`./verify_cognos_data.sh`
4. 容器日志：`docker-compose logs mysql`
