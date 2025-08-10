# Cognos MySQL数据库部署指南

## 概述

本项目将Cognos数据库架构从PostgreSQL转换为MySQL，用于支持NL2SQL项目的数据查询和分析功能。

## 文件结构

```
qdrant/
├── docker-compose.yaml              # Docker Compose配置文件
├── start_cognos_mysql.sh            # 启动脚本
├── README_Cognos_MySQL.md           # 本文档
└── mysql/
    └── init/
        ├── 01_create_cognos_tables_mysql.sql  # 表结构创建脚本
        └── 02_insert_sample_data.sql          # 示例数据插入脚本
```

## 快速开始

### 1. 前置要求

- Docker Desktop 已安装并运行
- Docker Compose 已安装
- 至少 2GB 可用内存
- 端口 3306 和 6333 未被占用

### 2. 启动数据库

```bash
# 进入项目目录
cd spring-ai-alibaba/spring-ai-alibaba-nl2sql/qdrant

# 运行启动脚本
./start_cognos_mysql.sh
```

启动脚本会自动：
- 检查Docker状态
- 创建必要的目录
- 启动MySQL和Qdrant服务
- 等待服务就绪
- 验证数据库连接
- 显示连接信息

### 3. 验证安装

启动完成后，可以通过以下方式验证：

```bash
# 连接MySQL数据库
docker exec -it mysql-nl2sql-mvp1 mysql -u root -proot123 nl2sql

# 查看表列表
SHOW TABLES;

# 查看产品表数据
SELECT * FROM TB_PROD LIMIT 5;
```

## 数据库连接信息

### MySQL连接参数

| 参数 | 值 |
|------|-----|
| 主机 | localhost |
| 端口 | 3306 |
| 数据库 | nl2sql |
| 用户名 | nl2sql_user |
| 密码 | nl2sql_pass |
| Root密码 | root123 |

### 连接字符串示例

```
# JDBC连接字符串
jdbc:mysql://localhost:3306/nl2sql?useSSL=false&serverTimezone=UTC&characterEncoding=utf8mb4

# Python连接字符串
mysql+pymysql://nl2sql_user:nl2sql_pass@localhost:3306/nl2sql
```

## 数据库架构

### 核心表结构

1. **TB_PROD** - 产品主表
   - 存储所有金融产品的基础信息
   - 支持产品父子关系（PROD_PARNT_ID）
   - 包含价格、风险等级、交易权限等信息

2. **TB_PROD_USER_DEFIN_EXT_FIELD** - 产品扩展字段表
   - 支持动态字段扩展
   - 多种数据类型支持
   - 字段继承机制

3. **TB_DEBT_INSTM** - 债券工具表
   - 债券特有属性
   - 票息、收益率信息

4. **TB_EQTY_LINK_INVST** - 股票挂钩投资表
   - 结构性产品信息
   - 挂钩标的管理

5. **CDE_DESC_VALUE** - 代码描述值表
   - 系统代码字典
   - 多语言支持

### 示例数据

数据库包含以下示例数据：
- 5个产品记录（基金、债券、结构性产品、股票）
- 20+个代码描述记录
- 相关的扩展字段和关联数据

## 常用操作

### 查看容器状态

```bash
# 查看所有容器状态
docker-compose ps

# 查看MySQL容器日志
docker-compose logs -f mysql

# 查看Qdrant容器日志
docker-compose logs -f qdrant
```

### 数据库操作

```bash
# 连接数据库
docker exec -it mysql-nl2sql-mvp1 mysql -u root -proot123 nl2sql

# 备份数据库
docker exec mysql-nl2sql-mvp1 mysqldump -u root -proot123 nl2sql > backup.sql

# 恢复数据库
docker exec -i mysql-nl2sql-mvp1 mysql -u root -proot123 nl2sql < backup.sql
```

### 服务管理

```bash
# 停止服务
docker-compose down

# 重启服务
docker-compose restart

# 重新构建并启动
docker-compose up -d --build

# 删除所有数据（谨慎使用）
docker-compose down -v
```

## 性能优化

### 索引策略

数据库已预配置以下索引：
- 主键索引：所有表的主键
- 外键索引：关联表的外键字段
- 复合索引：常用查询组合字段
- 业务索引：产品状态、类型等业务字段

### 查询优化建议

1. **使用索引字段**：WHERE条件优先使用已建索引的字段
2. **避免全表扫描**：大表查询时添加适当的过滤条件
3. **限制返回字段**：只SELECT需要的字段
4. **使用LIMIT**：分页查询时使用LIMIT限制返回行数

## 故障排除

### 常见问题

1. **端口冲突**
   ```bash
   # 检查端口占用
   lsof -i :3306
   lsof -i :6333
   
   # 修改docker-compose.yaml中的端口映射
   ```

2. **内存不足**
   ```bash
   # 检查Docker内存限制
   docker stats
   
   # 调整docker-compose.yaml中的内存限制
   ```

3. **权限问题**
   ```bash
   # 确保脚本有执行权限
   chmod +x start_cognos_mysql.sh
   
   # 检查目录权限
   ls -la mysql/
   ```

4. **数据初始化失败**
   ```bash
   # 查看MySQL初始化日志
   docker-compose logs mysql
   
   # 手动执行SQL脚本
   docker exec -i mysql-nl2sql-mvp1 mysql -u root -proot123 nl2sql < mysql/init/01_create_cognos_tables_mysql.sql
   ```

### 日志查看

```bash
# 查看所有服务日志
docker-compose logs

# 实时查看MySQL日志
docker-compose logs -f mysql

# 查看最近100行日志
docker-compose logs --tail=100 mysql
```

## 开发建议

### 连接池配置

```yaml
# application.yml示例
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/nl2sql?useSSL=false&serverTimezone=UTC
    username: nl2sql_user
    password: nl2sql_pass
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
```

### 最佳实践

1. **事务管理**：使用@Transactional注解管理事务
2. **连接池**：配置合适的连接池大小
3. **查询优化**：使用EXPLAIN分析查询计划
4. **监控告警**：配置数据库性能监控

## 扩展功能

### 添加新表

1. 在`mysql/init/`目录下创建新的SQL文件
2. 文件名使用数字前缀确保执行顺序
3. 重启容器使更改生效

### 数据迁移

1. 准备迁移脚本
2. 使用事务确保数据一致性
3. 备份现有数据
4. 执行迁移并验证

## 联系支持

如有问题，请参考：
1. 查看容器日志排查问题
2. 检查网络和端口配置
3. 验证Docker和Docker Compose版本
4. 参考官方文档进行故障排除
