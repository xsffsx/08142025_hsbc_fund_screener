# NL2SQL 数据更新快速操作手册

**创建时间**: 2025年8月8日 11:41:00  
**文档类型**: Guide  
**版本**: v1.0

## 🚀 快速操作流程

### 1. 数据更新（3种方式）

#### 方式A: SQL脚本更新（推荐）
```bash
# 1. 编辑数据文件
vim spring-ai-alibaba/spring-ai-alibaba-nl2sql/spring-ai-alibaba-nl2sql-management/src/main/resources/sql/data.sql

# 2. 执行SQL脚本
docker exec -i mysql-nl2sql-mvp1 mysql -uroot -proot123 nl2sql < data.sql

# 3. 重启应用
./spring-ai-alibaba/spring-ai-alibaba-nl2sql/script/start_all_service.sh restart
```

#### 方式B: 直接SQL命令
```bash
# 直接在数据库中执行SQL
docker exec -i mysql-nl2sql-mvp1 mysql -uroot -proot123 nl2sql -e "
INSERT INTO business_knowledge (business_term, description, synonyms, is_recall, data_set_id, agent_id) 
VALUES ('新业务术语', '描述信息', '同义词1,同义词2', 1, 'dataset_001', 2);
"

# 重启应用
./spring-ai-alibaba/sprizng-ai-alibaba-nl2sql/script/start_all_service.sh restart
```

#### 方式C: API接口更新
```bash
# 通过API添加数据
curl -X POST http://localhost:8065/api/business-knowledge \
  -H "Content-Type: application/json" \
  -d '{
    "businessTerm": "新术语",
    "description": "描述",
    "synonyms": "同义词",
    "defaultRecall": true,
    "datasetId": "dataset_001",
    "agentId": "2"
  }'
```

### 2. 验证更新

```bash
# 验证数据库
docker exec -i mysql-nl2sql-mvp1 mysql -uroot -proot123 nl2sql -e "SELECT COUNT(*) FROM business_knowledge;"

# 验证API
curl -s http://localhost:8065/api/business-knowledge | jq length

# 验证前端
open http://localhost:8065/business-knowledge.html
```

## 🔧 Service重构检查清单

### 检查是否使用内存存储
```bash
# 搜索问题代码模式
grep -r "ConcurrentHashMap\|HashMap.*new" src/main/java/
grep -r "initSampleData\|init.*Data" src/main/java/
```

### 重构为数据库存储
```java
// ❌ 错误方式 - 内存存储
private final Map<Long, BusinessKnowledge> knowledgeStore = new ConcurrentHashMap<>();

// ✅ 正确方式 - 数据库存储
@Autowired
private JdbcTemplate jdbcTemplate;

public List<BusinessKnowledge> findAll() {
    return jdbcTemplate.query("SELECT * FROM business_knowledge", rowMapper);
}
```

## 📊 数据结构参考

### business_knowledge 表结构
```sql
CREATE TABLE business_knowledge (
  id INT AUTO_INCREMENT PRIMARY KEY,
  business_term VARCHAR(255) NOT NULL,
  description TEXT,
  synonyms TEXT,
  is_recall INT DEFAULT 1,
  data_set_id VARCHAR(255),
  agent_id INT,
  created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 示例数据格式
```sql
INSERT INTO business_knowledge (id, business_term, description, synonyms, is_recall, data_set_id, agent_id) VALUES 
(1001, '基金产品类型', '基金产品类型与子类型用于标识产品品类', '产品类型,基金类型,Fund Type', 1, 'finance_fund', 2),
(1002, '风险等级', '风险等级用于衡量基金产品的风险水平', '风险级别,风险水平,Risk Level', 1, 'finance_fund', 2);
```

## 🚨 故障排除

### 常见错误及解决方案

| 错误现象 | 可能原因 | 解决方案 |
|----------|----------|----------|
| 前端显示"加载中..." | API请求失败 | 检查应用状态和日志 |
| API返回空数组 | 数据库连接问题 | 检查MySQL容器状态 |
| 编译失败 | Java语法错误 | 运行格式化和重新编译 |
| 前端显示旧数据 | Service使用内存存储 | 重构Service使用数据库 |

### 快速诊断命令
```bash
# 检查服务状态
./spring-ai-alibaba/spring-ai-alibaba-nl2sql/script/start_all_service.sh status

# 检查数据库连接
docker exec mysql-nl2sql-mvp1 mysqladmin ping

# 检查应用日志
tail -f spring-ai-alibaba/spring-ai-alibaba-nl2sql/logs/nl2sql-mvp1.log

# 检查API响应
curl -I http://localhost:8065/api/business-knowledge
```

## 📝 最佳实践

### 数据更新原则
1. **备份优先**: 更新前备份重要数据
2. **测试验证**: 在测试环境先验证
3. **分步执行**: 大批量更新分批进行
4. **监控日志**: 关注应用启动和运行日志

### 开发建议
- ✅ 使用数据库作为单一数据源
- ✅ 避免在代码中硬编码业务数据
- ✅ 实现适当的缓存机制
- ✅ 添加数据变更审计日志

### 部署建议
- ✅ 数据库初始化脚本版本化管理
- ✅ 应用重启后验证数据一致性
- ✅ 建立数据回滚和恢复机制
- ✅ 监控数据同步状态

## 🔗 相关链接

- **业务知识管理**: http://localhost:8065/business-knowledge.html
- **API文档**: http://localhost:8065/swagger-ui.html
- **NL2SQL查询**: http://localhost:8065/nl2sql.html
- **系统日志**: `tail -f logs/nl2sql-mvp1.log`

## 📞 技术支持

如遇到问题，请按以下顺序排查：
1. 检查服务状态和日志
2. 验证数据库连接和数据
3. 确认API接口响应
4. 检查前端网络请求
5. 查看浏览器控制台错误

---

**快速参考**: 数据更新 → 重启应用 → 验证前端 → 完成！
