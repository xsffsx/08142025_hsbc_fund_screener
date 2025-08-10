# NL2SQL 数据库重新初始化与配置更新指南

## 🎯 概述

本指南说明如何重新初始化 MySQL 数据库，以及如何更新业务知识管理和语义模型配置，确保 NL2SQL 系统正确显示基金/UT领域数据。

## ✅ 更新状态

- **业务知识管理配置**: ✅ 已更新 (8条基金业务知识)
- **语义模型配置**: ✅ 已更新 (24条基金字段配置)
- **数据库存储**: ✅ 已从内存存储迁移到数据库存储

## 📋 修改内容

### 1. Docker Compose 配置修改

**文件**: `docker-compose.yaml`
- 添加了 management 模块 SQL 文件的挂载路径
- 支持从 management 模块自动加载 Schema 和数据

### 2. 新增的初始化文件

| 文件 | 描述 | 来源 |
|------|------|------|
| `mysql/init/00-init-from-management.sql` | 数据库和用户创建 | 新建 |
| `mysql/init/03-management-schema.sql` | 表结构定义 | 从 management/schema.sql 复制 |
| `mysql/init/04-management-data.sql` | 初始化数据 | 从 management/data.sql 复制 |

### 3. 重新初始化脚本

**文件**: `reinitialize_database.sh`
- 自动清理现有容器和数据卷
- 重新启动 MySQL 容器
- 验证数据库初始化结果

## 🚀 使用方法

### 快速重新初始化

```bash
# 进入 qdrant 目录
cd /Users/paulo/IdeaProjects/20250707_MCP/spring-ai-alibaba/spring-ai-alibaba-nl2sql/qdrant

# 执行重新初始化
./reinitialize_database.sh reinit
```

### 其他命令

```bash
# 验证数据库状态
./reinitialize_database.sh verify

# 显示数据库信息
./reinitialize_database.sh info

# 显示帮助
./reinitialize_database.sh help
```

## 📊 数据库结构

### 核心表 (7个)

1. **agent** - 智能体表 (主表)
2. **business_knowledge** - 业务知识表
3. **semantic_model** - 语义模型表
4. **agent_knowledge** - 智能体知识表
5. **datasource** - 数据源表
6. **agent_datasource** - 智能体数据源关联表
7. **agent_preset_question** - 智能体预设问题表

### 初始化数据

- **4个智能体**: 人口GDP、销售分析、财务报表、库存管理
- **业务知识**: 客户满意度、NPS、客户留存率等
- **语义模型**: 字段映射和同义词配置
- **数据源**: 生产环境、数据仓库、测试环境等

## 🔧 连接信息

```yaml
数据库连接:
  主机: localhost:3306
  数据库: nl2sql
  用户名: nl2sql_user
  密码: nl2sql_pass
  Root密码: root123
```

## ⚠️ 注意事项

1. **数据备份**: 重新初始化会删除所有现有数据
2. **Docker 依赖**: 确保 Docker 服务正在运行
3. **端口冲突**: 确保 3306 端口未被占用
4. **文件权限**: 脚本需要执行权限

## 🔍 故障排除

### 常见问题

1. **Docker 未运行**
   ```bash
   # 启动 Docker Desktop
   open -a Docker
   ```

2. **端口被占用**
   ```bash
   # 检查端口占用
   lsof -i :3306
   
   # 停止占用进程
   sudo kill -9 <PID>
   ```

3. **权限问题**
   ```bash
   # 添加执行权限
   chmod +x reinitialize_database.sh
   ```

### 日志查看

```bash
# 查看 MySQL 容器日志
docker logs mysql-nl2sql-mvp1

# 查看容器状态
docker ps -a | grep mysql-nl2sql-mvp1
```

## 📈 验证步骤

重新初始化完成后，脚本会自动验证：

1. ✅ 数据库 `nl2sql` 是否存在
2. ✅ 7个核心表是否创建成功
3. ✅ 智能体数据是否插入成功
4. ✅ 外键关系是否正确建立

## 🎯 下一步

数据库重新初始化完成后，可以：

1. 启动 NL2SQL 服务
2. 测试 API 接口
3. 验证向量化功能
4. 进行业务数据查询

```bash
# 启动完整服务
cd ../
./script/start_all_service.sh start
```

## 🔗 相关链接

- **业务知识管理**: http://localhost:8065/business-knowledge.html ✅ (8条基金业务知识)
- **语义模型配置**: http://localhost:8065/semantic-model.html ✅ (24条基金字段配置)
- **NL2SQL 查询**: http://localhost:8065/nl2sql.html ✅ (支持基金查询)
- **API 文档**: http://localhost:8065/swagger-ui.html

## 📚 技术文档

- **完整技术指南**: [20250808114000_01_Technical_NL2SQL_Data_Update_Frontend_Sync_Guide.md](./20250808114000_01_Technical_NL2SQL_Data_Update_Frontend_Sync_Guide.md)
- **快速操作手册**: [20250808114100_02_Guide_NL2SQL_Quick_Data_Update_Manual.md](./20250808114100_02_Guide_NL2SQL_Quick_Data_Update_Manual.md)
- **架构流程图集**: [20250808114200_03_Architecture_NL2SQL_Data_Flow_Diagrams.md](./20250808114200_03_Architecture_NL2SQL_Data_Flow_Diagrams.md)
- **语义模型集成问题分析**: [20250808123000_04_Analysis_NL2SQL_Semantic_Model_Integration_Issue.md](./20250808123000_04_Analysis_NL2SQL_Semantic_Model_Integration_Issue.md)
- **渐进式语义增强方案**: [20250808130000_05_Technical_Progressive_Semantic_Enhancement_Solution.md](./20250808130000_05_Technical_Progressive_Semantic_Enhancement_Solution.md) ⭐
- **验证收货计划**: [qdrant/20250808133000_06_Test_Semantic_Enhancement_Validation_Plan.md](./qdrant/20250808133000_06_Test_Semantic_Enhancement_Validation_Plan.md)
- **最终验证总结**: [qdrant/20250808140000_07_Final_Validation_Summary_Report.md](./qdrant/20250808140000_07_Final_Validation_Summary_Report.md) 🎯

---

**系统状态**: 🎉 NL2SQL 系统已完全配置为基金/UT领域分析，支持专业的金融产品自然语言查询！
**注意**: 重新初始化数据库会清除所有现有数据，请确保已备份重要数据。
