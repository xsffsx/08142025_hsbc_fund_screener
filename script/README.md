# NL2SQL 服务管理脚本使用说明

## 概述

本目录包含两个服务管理脚本：

### 1. `start_all_external_services.sh` - 外部服务管理
统一管理外部服务的脚本，用于启动、重启和验证以下服务：
- **MySQL**: 数据库服务 (端口: 3306)
- **Qdrant**: 向量数据库服务 (端口: 6333-6334)
- **LM Studio**: LLM和Embedding服务 (端口: 1234)

### 2. `start_all_service.sh` - 完整服务管理 🆕
完整的NL2SQL服务管理脚本，集成了外部服务管理、Maven构建和Spring Boot应用启动功能：
- ✅ **外部服务管理** - 集成外部服务脚本功能
- ✅ **Maven构建** - 基于成功经验的自动构建
- ✅ **Spring Boot启动** - 自动启动和监控
- ✅ **进程管理** - PID文件管理和优雅停止
- ✅ **日志管理** - 统一日志输出

## 使用方法

### 🚀 推荐使用 `start_all_service.sh` (完整服务管理)

```bash
# 启动所有服务 (外部服务 + Maven构建 + Spring Boot)
./script/start_all_service.sh start

# 重启所有服务
./script/start_all_service.sh restart

# 停止Spring Boot应用
./script/start_all_service.sh stop

# 检查完整服务状态
./script/start_all_service.sh status
```

### 📋 外部服务管理 `start_all_external_services.sh`

```bash
# 验证外部服务状态 (默认操作)
./script/start_all_external_services.sh
./script/start_all_external_services.sh verify

# 启动外部服务
./script/start_all_external_services.sh start

# 重启外部服务
./script/start_all_external_services.sh restart

# 查看详细外部服务状态
./script/start_all_external_services.sh status
```

## 服务状态说明

### ✅ 正常状态
- **MySQL**: 容器运行且健康检查通过
- **Qdrant**: 容器运行且API响应正常，集合可访问
- **LM Studio**: API响应正常，模型已加载

### ⚠️ 异常状态处理

#### MySQL问题
- 检查Docker是否运行
- 检查端口3306是否被占用
- 查看容器日志: `docker logs mysql-nl2sql-mvp1`

#### Qdrant问题  
- 容器可能需要更长启动时间
- 检查端口6333-6334是否被占用
- 查看容器日志: `docker logs qdrant-nl2sql-mvp1`

#### LM Studio问题
- 确保LM Studio应用已启动
- 确保已加载LLM模型 (如: qwen/qwen3-30b-a3b-2507)
- 确保已加载Embedding模型 (如: text-embedding-nomic-embed-text-v1.5)
- 确保Local Server已启动 (默认端口1234)

## LM Studio配置步骤

1. **启动LM Studio应用**
2. **下载并加载模型**:
   - LLM模型: qwen/qwen3-30b-a3b-2507 或其他兼容模型
   - Embedding模型: text-embedding-nomic-embed-text-v1.5
3. **启动Local Server**:
   - 进入"Local Server"页面
   - 点击"Start Server"
   - 确认端口为1234

## 服务端点

- **MySQL**: `localhost:3306`
- **Qdrant**: `http://localhost:6333`
- **LM Studio**: `http://localhost:1234`

## 脚本功能

### verify (默认)
- 检查所有服务状态
- 返回整体健康状况

### start  
- 启动Docker服务 (MySQL + Qdrant)
- 检查LM Studio服务
- 验证所有服务状态

### restart
- 重启Docker服务
- 检查LM Studio服务  
- 验证所有服务状态

### status
- 显示详细服务状态
- 显示Docker容器信息
- 显示Qdrant集合信息
- 显示LM Studio模型信息

## 故障排除

### 常见问题

1. **Docker服务未启动**
   ```bash
   # 启动Docker Desktop
   open -a Docker
   ```

2. **端口冲突**
   ```bash
   # 检查端口占用
   lsof -i :3306
   lsof -i :6333  
   lsof -i :1234
   ```

3. **LM Studio模型未加载**
   - 打开LM Studio
   - 在Models页面重新加载模型
   - 确保模型状态为"Loaded"

### 日志查看

```bash
# Docker容器日志
docker logs mysql-nl2sql-mvp1
docker logs qdrant-nl2sql-mvp1

# 脚本详细输出
./script/start_all_external_services.sh status
```

## 依赖要求

- Docker Desktop
- LM Studio应用
- curl命令
- jq命令 (可选，用于JSON格式化)

## 完整服务管理功能详解

### `start_all_service.sh` 启动流程

执行 `start` 命令时，脚本会按以下顺序执行：

1. **环境检查** - 验证Java环境和目录结构
2. **外部服务启动** - MySQL、Qdrant、LM Studio
3. **Maven构建** - 编译项目（跳过测试和格式化）
4. **Spring Boot启动** - 启动应用并监控状态
5. **状态验证** - 确认所有服务正常运行

### 服务访问地址

启动成功后可访问：
- **NL2SQL API**: http://localhost:8065/nl2sql/stream/search?query=您的查询
- **Web界面**: http://localhost:8065/nl2sql.html
- **API文档**: http://localhost:8065/swagger-ui.html

### 日志和进程管理

- **应用日志**: `logs/nl2sql-mvp1.log`
- **进程PID**: `logs/spring-boot.pid`
- **实时日志**: `tail -f logs/nl2sql-mvp1.log`

## 脚本对比

| 功能 | start_all_external_services.sh | start_all_service.sh |
|------|--------------------------------|---------------------|
| 外部服务管理 | ✅ | ✅ |
| Maven构建 | ❌ | ✅ |
| Spring Boot启动 | ❌ | ✅ |
| 进程管理 | ❌ | ✅ |
| 重启功能 | 部分 | ✅ |
| 状态检查 | 外部服务 | 完整服务 |

## 注意事项

- 首次启动可能需要较长时间等待服务就绪
- Qdrant容器可能显示"unhealthy"但API仍可正常工作
- LM Studio需要手动启动和配置模型
- 建议使用 `start_all_service.sh` 进行完整的服务管理
- 推荐先运行`status`命令确认服务状态
