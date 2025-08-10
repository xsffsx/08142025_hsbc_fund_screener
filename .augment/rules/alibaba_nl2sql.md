---
type: "always_apply"
---

# Alibaba NL2SQL 项目规则

## 项目启动规则
- **强制要求**: 启动spring-ai-alibaba-nl2sql项目时，必须使用专用启动脚本
- **脚本路径**: `/Users/paulo/IdeaProjects/20250707_MCP/spring-ai-alibaba/spring-ai-alibaba-nl2sql/script/start_all_service.sh`
- **禁止使用**: 不要使用maven命令或java -jar直接启动
- **原因**: 该脚本包含完整的服务管理、依赖检查、外部服务启动等功能

## 启动命令详解
```bash
# 智能启动所有服务 (跳过已运行的服务) - 推荐首次启动
./script/start_all_service.sh start

# 重启所有服务 (外部服务 + Maven构建 + Spring Boot)
./script/start_all_service.sh restart

# 快速重启Spring Boot应用 (仅用于开发调试) - 推荐代码修改后使用
./script/start_all_service.sh quick

# 强制重新编译所有模块 - 编译问题时使用
./script/start_all_service.sh rebuild

# 停止Spring Boot应用
./script/start_all_service.sh stop

# 检查服务状态
./script/start_all_service.sh status
```

## 脚本功能特性
- **智能启动**: 自动检查并跳过已运行的服务
- **依赖管理**: 自动处理模块间的循环依赖问题
- **外部服务**: 自动启动PostgreSQL、Qdrant等外部服务
- **日志管理**: 自动归档和压缩历史日志文件
- **健康检查**: 启动后自动验证服务可用性
- **Java环境**: 自动配置正确的Java版本和JVM参数

## 服务访问地址
- **NL2SQL流式查询**: http://localhost:8065/nl2sql/stream/search?query=您的查询
- **Web界面**: http://localhost:8065/nl2sql.html
- **API文档**: http://localhost:8065/swagger-ui.html
- **初始化接口**: http://localhost:8065/nl2sql/init

## 外部服务配置
- **PostgreSQL数据库**: localhost:5432 (系统表存储)
- **Qdrant向量库**: http://localhost:6333 (向量存储)
- **LM Studio**: http://localhost:1234 (本地LLM服务)
- **Proxy OpenAI**: http://localhost:8089 (代理服务)

## 开发工作流建议
1. **首次启动**: `./script/start_all_service.sh start`
2. **代码修改后**: `./script/start_all_service.sh quick`
3. **编译问题时**: `./script/start_all_service.sh rebuild`
4. **完全重启**: `./script/start_all_service.sh restart`
5. **状态检查**: `./script/start_all_service.sh status`

## 数据库配置注意事项
- 系统使用PostgreSQL作为主数据库 (mvp1 profile)
- 确保使用 `postgreAccessor` 而不是 `mysqlAccessor`
- PostgreSQL容器通过docker-compose自动启动
