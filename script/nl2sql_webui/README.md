# NL2SQL Vue.js Web UI 使用指南

## 📋 项目介绍

NL2SQL Vue.js Web UI 是一个基于 Vue 3 + Vite 构建的现代化前端应用，为 Spring AI Alibaba NL2SQL 项目提供完整的Web管理界面。

### 🎯 主要功能

- **🧠 业务知识管理**: 管理业务术语、同义词和描述信息
- **🔧 语义模型配置**: 配置字段映射和语义增强规则  
- **🤖 智能体管理**: 创建和管理NL2SQL智能体
- **🗄️ 数据源管理**: 配置和测试数据库连接
- **📊 实时监控**: 查看系统状态和性能指标

### 🏗️ 技术栈

- **前端框架**: Vue 3.4+
- **构建工具**: Vite 5.0+
- **路由管理**: Vue Router 4.2+
- **HTTP客户端**: Axios 1.6+
- **代码高亮**: Highlight.js 11.9+
- **Markdown解析**: Marked 11.0+
- **图标库**: Bootstrap Icons 1.11+

## 🚀 快速启动

### 方式一：使用启动脚本（推荐）

```bash
# 进入项目根目录
cd /Users/paulo/PycharmProjects/20250809

# 使用启动脚本
./script/nl2sql_webui/start_webui.sh
```

### 方式二：手动启动

```bash
# 进入Web UI目录
cd spring-ai-alibaba/spring-ai-alibaba-nl2sql/spring-ai-alibaba-nl2sql-web-ui

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

### 📋 启动脚本选项

```bash
# 显示帮助信息
./script/nl2sql_webui/start_webui.sh --help

# 仅检查依赖
./script/nl2sql_webui/start_webui.sh --check

# 仅安装依赖
./script/nl2sql_webui/start_webui.sh --install

# 跳过后端服务检查
./script/nl2sql_webui/start_webui.sh --no-backend
```

## 🌐 访问地址

启动成功后，可通过以下地址访问：

- **本地访问**: http://localhost:3000 (如端口被占用会自动切换到3001等)
- **网络访问**: http://[你的IP]:3000

## 🔧 系统要求

### 必需依赖

- **Node.js**: >= 16.0.0
- **npm**: >= 7.0.0
- **Spring Boot后端**: 运行在端口8065

### 推荐环境

- **操作系统**: macOS, Linux, Windows
- **浏览器**: Chrome 90+, Firefox 88+, Safari 14+
- **内存**: >= 4GB
- **磁盘空间**: >= 1GB

## 🔗 后端集成

Web UI 通过 Vite 代理与 Spring Boot 后端集成：

### 代理配置

```javascript
// vite.config.js
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8065',
      changeOrigin: true
    },
    '/nl2sql': {
      target: 'http://localhost:8065', 
      changeOrigin: true
    }
  }
}
```

### 后端服务要求

确保以下Spring Boot服务正在运行：

- **主服务**: http://localhost:8065
- **健康检查**: http://localhost:8065/actuator/health
- **API文档**: http://localhost:8065/swagger-ui.html

## 📚 功能模块详解

### 1. 业务知识管理

**访问路径**: `/business-knowledge`

**主要功能**:
- 查看业务知识列表
- 搜索业务术语和同义词
- 添加/编辑/删除业务知识
- 按数据集和智能体筛选

**API接口**:
```javascript
// 获取业务知识列表
GET /api/knowledge

// 根据智能体ID获取
GET /api/knowledge/agent/{agentId}

// 搜索业务知识
GET /api/knowledge/search?content={keyword}

// 添加业务知识
POST /api/knowledge/add

// 更新业务知识  
PUT /api/knowledge/update/{id}

// 删除业务知识
DELETE /api/knowledge/delete/{id}
```

### 2. 语义模型配置

**访问路径**: `/semantic-model`

**主要功能**:
- 管理字段语义映射
- 配置同义词和描述
- 启用/禁用字段召回
- 批量操作支持

**API接口**:
```javascript
// 获取语义模型列表
GET /api/fields

// 根据智能体ID获取
GET /api/fields/agent/{agentId}

// 搜索语义模型
GET /api/fields/search?content={keyword}

// 添加语义模型
POST /api/fields/add

// 更新语义模型
PUT /api/fields/{id}

// 删除语义模型
DELETE /api/fields/{id}
```

### 3. 智能体管理

**访问路径**: `/agent`

**主要功能**:
- 创建和配置智能体
- 管理智能体状态
- 关联数据源和知识库
- 设置预设问题

**API接口**:
```javascript
// 获取智能体列表
GET /api/agent

// 获取智能体详情
GET /api/agent/{id}

// 创建智能体
POST /api/agent

// 更新智能体
PUT /api/agent/{id}

// 删除智能体
DELETE /api/agent/{id}
```

### 4. 数据源管理

**访问路径**: `/datasource`

**主要功能**:
- 配置数据库连接
- 测试连接状态
- 管理数据源权限
- 关联智能体

**API接口**:
```javascript
// 获取数据源列表
GET /api/datasource

// 测试数据源连接
POST /api/datasource/{id}/test

// 获取智能体数据源
GET /api/datasource/agent/{agentId}
```

## 🛠️ 开发指南

### 项目结构

```
spring-ai-alibaba-nl2sql-web-ui/
├── src/
│   ├── components/          # 公共组件
│   ├── views/              # 页面组件
│   │   ├── BusinessKnowledge.vue
│   │   ├── SemanticModel.vue
│   │   ├── AgentDetail.vue
│   │   └── ...
│   ├── utils/              # 工具函数
│   │   └── api.js          # API接口定义
│   ├── styles/             # 样式文件
│   ├── App.vue             # 根组件
│   └── main.js             # 入口文件
├── package.json            # 项目配置
├── vite.config.js          # Vite配置
└── README.md              # 项目说明
```

### 构建部署

```bash
# 构建生产版本
npm run build

# 预览构建结果
npm run preview
```

构建产物将输出到 `dist/` 目录，可以部署到任何静态文件服务器。

## 🔍 故障排除

### 常见问题

#### 1. 端口被占用

**现象**: 启动时提示端口3000被占用

**解决方案**: 
- Vite会自动切换到下一个可用端口（3001, 3002...）
- 或手动指定端口：`npm run dev -- --port 3002`

#### 2. 后端连接失败

**现象**: API请求返回404或连接超时

**解决方案**:
```bash
# 检查后端服务状态
curl http://localhost:8065/actuator/health

# 启动后端服务
./script/start_all_service.sh start
```

#### 3. npm安装失败

**现象**: npm install 报错

**解决方案**:
```bash
# 清理缓存
npm cache clean --force

# 删除node_modules重新安装
rm -rf node_modules package-lock.json
npm install
```

#### 4. 页面空白或加载失败

**现象**: 浏览器显示空白页面

**解决方案**:
- 检查浏览器控制台错误信息
- 确认Node.js版本 >= 16
- 清除浏览器缓存
- 检查网络连接

### 日志查看

```bash
# 查看Web UI日志
tail -f logs/webui.log

# 查看后端日志
tail -f logs/nl2sql-mvp1.log
```

### 性能优化

- 使用Chrome DevTools分析性能
- 启用Vite的HMR热更新
- 合理使用Vue的响应式特性
- 避免不必要的API请求

## 📞 技术支持

如遇到问题，请：

1. 查看本文档的故障排除章节
2. 检查日志文件获取详细错误信息
3. 确认系统环境符合要求
4. 联系开发团队获取支持

---

**版本**: 1.0.0  
**更新日期**: 2025-08-10  
**维护团队**: NL2SQL Development Team
