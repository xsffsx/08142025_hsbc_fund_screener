# NL2SQL 快速参考卡片

## 🚀 常用命令

### 启动脚本
```bash
# 进入项目目录
cd /Users/paulo/IdeaProjects/20250707_MCP/spring-ai-alibaba/spring-ai-alibaba-nl2sql

# 首次启动 (2-3分钟)
./script/start_all_service.sh start

# 代码修改后快速重启 (30-60秒) ⭐ 开发常用
./script/start_all_service.sh quick

# 完整重启 (3-4分钟)
./script/start_all_service.sh restart

# 强制重编译 (1-2分钟)
./script/start_all_service.sh rebuild

# 停止服务
./script/start_all_service.sh stop

# 检查状态
./script/start_all_service.sh status
```

### 测试脚本
```bash
# 运行所有测试用例 ⭐ 验证功能
./script/test_api.sh test

# 交互式测试
./script/test_api.sh interactive

# 性能测试
./script/test_api.sh performance

# 检查服务状态
./script/test_api.sh check
```

## 📊 服务地址

| 服务 | 地址 | 说明 |
|------|------|------|
| Web界面 | http://localhost:8065/nl2sql.html | 用户界面 |
| API接口 | http://localhost:8065/nl2sql/nl2sql?query=查询 | REST API |
| 流式API | http://localhost:8065/nl2sql/stream/search?query=查询 | 流式接口 |
| API文档 | http://localhost:8065/swagger-ui.html | Swagger文档 |
| MySQL | localhost:3306 | 数据库 |
| Qdrant | http://localhost:6333 | 向量库 |
| LM Studio | http://localhost:1234 | LLM服务 |

## 🔧 开发工作流

### 日常开发
```bash
# 1. 修改代码
vim spring-ai-alibaba-nl2sql-chat/src/main/java/com/alibaba/cloud/ai/service/Nl2SqlService.java

# 2. 快速重启
./script/start_all_service.sh quick

# 3. 测试验证
./script/test_api.sh test

# 4. 查看日志 (如有问题)
tail -f logs/nl2sql-mvp1.log
```

### 首次环境搭建
```bash
# 1. 完整启动
./script/start_all_service.sh start

# 2. 验证功能
./script/test_api.sh test

# 3. 检查状态
./script/start_all_service.sh status
```

## 🛠️ 故障排除

### 编译失败
```bash
# 强制重编译
./script/start_all_service.sh rebuild

# 清理Maven缓存
rm -rf ~/.m2/repository/com/alibaba/cloud/ai/
```

### 端口占用
```bash
# 停止服务
./script/start_all_service.sh stop

# 检查端口
lsof -i :8065

# 强制终止
kill -9 $(lsof -t -i :8065)
```

### 外部服务异常
```bash
# 重启外部服务
./script/start_all_external_services.sh restart

# 检查Docker
docker ps
docker logs mysql-nl2sql-mvp1
```

## 📝 语义映射规则

### 表名映射
- `funds` → `B_UT_PROD`
- `fund_products` → `B_UT_PROD`

### 字段映射
- `currency` → `CCY_PROD_TRADE_CDE`
- `fund_name` → `PROD_NAME`
- `fund_id` → `PROD_ID`
- `risk_level` → `RISK_LVL_CDE`
- `return_rate` → `RETURN_RATE`

### 值映射
- `'HIGH'` → `'H'`
- `'MEDIUM'` → `'M'`
- `'LOW'` → `'L'`

## 📋 测试用例

### 预定义测试
1. **查询港币基金** → `SELECT * FROM B_UT_PROD WHERE CCY_PROD_TRADE_CDE = 'HKD'`
2. **查找美元产品** → `SELECT * FROM products WHERE CCY_PROD_TRADE_CDE = 'USD'`
3. **显示所有基金信息** → `SELECT * FROM fund_info`
4. **查询高风险基金** → `SELECT PROD_NAME FROM B_UT_PROD WHERE RISK_LVL_CDE = '高风险'`
5. **找出收益率大于5%的产品** → `SELECT product_id, RETURN_RATE FROM products WHERE RETURN_RATE > 0.05`

## 🔍 日志查看

```bash
# 实时查看应用日志
tail -f logs/nl2sql-mvp1.log

# 查看语义增强日志
grep "🎯" logs/nl2sql-mvp1.log

# 查看错误日志
grep "ERROR" logs/nl2sql-mvp1.log

# 查看SQL生成日志
grep "Enhanced SQL" logs/nl2sql-mvp1.log
```

## ⚡ 性能优化提示

1. **使用快速重启**: 代码修改后用 `quick` 而不是 `restart`
2. **增量编译**: 脚本会自动检测变更，只编译必要模块
3. **保持外部服务**: 避免频繁重启MySQL/Qdrant
4. **监控资源**: 注意内存和CPU使用情况

## 🎯 开发建议

- ⭐ **首次启动**: `./script/start_all_service.sh start`
- ⭐ **代码修改后**: `./script/start_all_service.sh quick`
- ⭐ **编译问题时**: `./script/start_all_service.sh rebuild`
- ⭐ **功能验证**: `./script/test_api.sh test`

---

**💡 提示**: 将此文件保存为书签，方便快速查阅！
