# NL2SQL 数据流程架构图

**创建时间**: 2025年8月8日 11:42:00
**更新时间**: 2025年8月8日 12:35:00
**文档类型**: Architecture
**版本**: v2.0

## 目录

- [1. 系统架构总览](#1-系统架构总览)
- [2. 数据更新流程](#2-数据更新流程)
- [3. 前端同步机制](#3-前端同步机制)
- [4. 语义模型配置更新流程](#4-语义模型配置更新流程)
- [5. 问题诊断流程](#5-问题诊断流程)
- [6. 服务重构对比](#6-服务重构对比)

## 1. 系统架构总览

### 1.1 完整系统架构

```mermaid
flowchart TD
    subgraph "用户层 User Layer"
        U1[👤 用户] --> U2[🌐 浏览器]
    end
    
    subgraph "应用层 Application Layer"
        A1[📱 前端页面<br/>business-knowledge.html] 
        A2[🔌 API Controller<br/>BusinessKnowledgeController]
        A3[⚙️ Service Layer<br/>BusinessKnowledgeService]
    end
    
    subgraph "AI处理层 AI Processing Layer"
        AI1[🤖 NL2SQL Engine]
        AI2[🧠 LM Studio LLM]
        AI3[📊 Vector Store]
    end
    
    subgraph "数据层 Data Layer"
        D1[🗄️ MySQL Database<br/>business_knowledge]
        D2[📦 Qdrant Vector DB]
        D3[📄 SQL Scripts<br/>data.sql]
    end
    
    U2 --> A1
    A1 --> A2
    A2 --> A3
    A3 --> D1
    
    A3 --> AI1
    AI1 --> AI2
    AI1 --> AI3
    AI3 --> D2
    
    D3 --> D1
    
    style U1 fill:#af52de,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style A1 fill:#af52de,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style A2 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style A3 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style AI1 fill:#eab308,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style AI2 fill:#eab308,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style D1 fill:#06b6d4,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style D2 fill:#06b6d4,stroke:#ffffff,stroke-width:3px,color:#ffffff
```

### 1.2 数据访问层次

```mermaid
flowchart LR
    subgraph "数据访问层次"
        L1[#1 前端请求] --> L2[#2 Controller路由]
        L2 --> L3[#3 Service业务逻辑]
        L3 --> L4[#4 JdbcTemplate查询]
        L4 --> L5[#5 MySQL数据库]
    end
    
    style L1 fill:#af52de,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style L2 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style L3 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style L4 fill:#06b6d4,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style L5 fill:#06b6d4,stroke:#ffffff,stroke-width:3px,color:#ffffff
```

## 2. 数据更新流程

### 2.1 完整数据更新流程

```mermaid
flowchart TD
    subgraph "数据准备阶段"
        S1[📝 编写SQL脚本<br/>data.sql] --> S2[✅ 验证SQL语法]
        S2 --> S3[💾 保存到resources/sql/]
    end
    
    subgraph "数据库更新阶段"
        D1[🐳 Docker容器启动] --> D2[📊 执行初始化脚本]
        D2 --> D3[🗄️ 数据写入MySQL]
        
        M1[📋 手动SQL执行] --> D3
        A1[🔄 应用启动时执行] --> D3
    end
    
    subgraph "应用同步阶段"
        A2[🔄 重启Spring Boot应用] --> A3[⚙️ Service重新加载]
        A3 --> A4[🔌 API接口更新]
        A4 --> A5[🌐 前端数据刷新]
    end
    
    subgraph "验证阶段"
        V1[🔍 数据库验证] --> V2[🧪 API测试]
        V2 --> V3[👀 前端检查]
        V3 --> V4[✅ 完成确认]
    end
    
    S3 --> D1
    S3 --> M1
    S3 --> A1
    
    D3 --> A2
    A5 --> V1
    
    style S1 fill:#eab308,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style D3 fill:#06b6d4,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style A5 fill:#af52de,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style V4 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
```

### 2.2 三种更新方式对比

```mermaid
flowchart LR
    subgraph "方式A: SQL脚本更新"
        A1[📄 编辑data.sql] --> A2[🐳 Docker执行]
        A2 --> A3[🔄 重启应用]
        A3 --> A4[✅ 永久生效]
    end
    
    subgraph "方式B: 直接SQL命令"
        B1[💻 命令行执行] --> B2[🗄️ 直接写入]
        B2 --> B3[🔄 重启应用]
        B3 --> B4[✅ 永久生效]
    end
    
    subgraph "方式C: API接口"
        C1[🔌 HTTP请求] --> C2[⚙️ Service处理]
        C2 --> C3[🗄️ 数据库写入]
        C3 --> C4[⚡ 实时生效]
    end
    
    style A4 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style B4 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style C4 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
```

## 3. 前端同步机制

### 3.1 数据同步时序图

```mermaid
sequenceDiagram
    participant U as 👤 用户
    participant F as 🌐 前端页面
    participant C as 🔌 Controller
    participant S as ⚙️ Service
    participant J as 🔗 JdbcTemplate
    participant D as 🗄️ MySQL
    
    Note over U,D: 数据更新后的同步流程
    
    U->>F: #1 访问页面
    F->>F: #2 页面加载
    F->>C: #3 GET /api/business-knowledge
    
    Note over C,S: API处理阶段
    C->>S: #4 findAll()
    S->>J: #5 query(SELECT_ALL)
    J->>D: #6 执行SQL查询
    
    Note over D,J: 数据返回阶段
    D-->>J: #7 返回ResultSet
    J-->>S: #8 映射为对象列表
    S-->>C: #9 List<BusinessKnowledge>
    C-->>F: #10 JSON响应
    
    Note over F,U: 前端渲染阶段
    F->>F: #11 解析JSON数据
    F->>F: #12 渲染表格
    F-->>U: #13 显示最新数据
    
    rect rgb(16, 185, 129, 0.1)
        Note over U,D: ✅ 数据同步完成
    end
```

### 3.2 前端数据流

```mermaid
flowchart TD
    subgraph "前端数据处理流程"
        F1[🌐 页面加载] --> F2[📡 发起API请求]
        F2 --> F3[⏳ 显示加载状态]
        F3 --> F4[📥 接收JSON数据]
        F4 --> F5[🔄 解析数据结构]
        F5 --> F6[📊 渲染表格行]
        F6 --> F7[✅ 显示完成]
    end
    
    subgraph "错误处理"
        E1[❌ 请求失败] --> E2[⚠️ 显示错误信息]
        E2 --> E3[🔄 重试机制]
    end
    
    F2 -.-> E1
    F4 -.-> E1
    
    style F1 fill:#af52de,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style F7 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style E2 fill:#ef4444,stroke:#ffffff,stroke-width:3px,color:#ffffff
```

## 4. 语义模型配置更新流程

### 4.1 语义模型端点重定向流程

```mermaid
flowchart TD
    subgraph "问题识别阶段"
        P1[🔍 发现前端显示旧数据] --> P2[📊 检查数据库有新数据]
        P2 --> P3[🔧 发现Service使用内存存储]
    end

    subgraph "解决方案选择"
        S1[💡 方案A: 重构Service] --> S2[⚖️ 评估工作量]
        S3[💡 方案B: 使用现有数据库服务] --> S4[✅ 选择方案B]
        S2 --> S4
    end

    subgraph "实施阶段"
        I1[🔌 添加Controller端点] --> I2[📝 修改前端API调用]
        I2 --> I3[🔄 更新所有CRUD端点]
        I3 --> I4[🚀 重启应用验证]
    end

    P3 --> S1
    P3 --> S3
    S4 --> I1
    I4 --> V1[✅ 验证成功]

    style P1 fill:#ef4444,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style S4 fill:#eab308,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style V1 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
```

### 4.2 端点映射转换图

```mermaid
flowchart LR
    subgraph "内存存储端点 (旧)"
        OLD1[📡 /api/semantic-model<br/>GET 列表查询]
        OLD2[🔍 /api/semantic-model?keyword=<br/>GET 搜索]
        OLD3[💾 /api/semantic-model<br/>POST 保存]
        OLD4[✏️ /api/semantic-model/{id}<br/>PUT 更新]
        OLD5[🗑️ /api/semantic-model/{id}<br/>DELETE 删除]
    end

    subgraph "数据库存储端点 (新)"
        NEW1[📡 /api/fields<br/>GET 列表查询]
        NEW2[🔍 /api/fields/search?content=<br/>GET 搜索]
        NEW3[💾 /api/fields/add<br/>POST 保存]
        NEW4[✏️ /api/fields/{id}<br/>PUT 更新]
        NEW5[🗑️ /api/fields/{id}<br/>DELETE 删除]
    end

    OLD1 --> NEW1
    OLD2 --> NEW2
    OLD3 --> NEW3
    OLD4 --> NEW4
    OLD5 --> NEW5

    style OLD1 fill:#ef4444,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style OLD2 fill:#ef4444,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style OLD3 fill:#ef4444,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style OLD4 fill:#ef4444,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style OLD5 fill:#ef4444,stroke:#ffffff,stroke-width:3px,color:#ffffff

    style NEW1 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style NEW2 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style NEW3 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style NEW4 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style NEW5 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
```

### 4.3 语义模型数据流对比

```mermaid
sequenceDiagram
    participant F as 🌐 前端页面
    participant C1 as 🔌 SemanticModelController
    participant S1 as ⚙️ SemanticModelService
    participant M1 as 💾 内存存储
    participant C2 as 🔌 SemanticModelPersistenceController
    participant S2 as ⚙️ SemanticModelPersistenceService
    participant D as 🗄️ MySQL数据库

    Note over F,D: 修改前 - 内存存储流程
    F->>C1: GET /api/semantic-model
    C1->>S1: findAll()
    S1->>M1: 从ConcurrentHashMap读取
    M1-->>S1: 返回硬编码数据
    S1-->>C1: List<SemanticModel>
    C1-->>F: 旧的示例数据

    Note over F,D: 修改后 - 数据库存储流程
    F->>C2: GET /api/fields
    C2->>S2: getFieldByAgentId(2L)
    S2->>D: SELECT * FROM semantic_model WHERE agent_id=2
    D-->>S2: 返回基金字段数据
    S2-->>C2: List<SemanticModel>
    C2-->>F: 最新的基金数据

    rect rgb(16, 185, 129, 0.1)
        Note over F,D: ✅ 数据同步完成
    end
```

## 5. 问题诊断流程

### 4.1 故障排除决策树

```mermaid
flowchart TD
    START[🚨 前端显示异常] --> Q1{前端显示什么?}
    
    Q1 -->|加载中...| Q2{API响应正常?}
    Q1 -->|旧数据| Q3{Service使用数据库?}
    Q1 -->|空白/错误| Q4{应用启动正常?}
    
    Q2 -->|是| FIX1[🔧 检查前端JS错误]
    Q2 -->|否| Q5{应用运行正常?}
    
    Q3 -->|是| FIX2[🔧 检查数据库数据]
    Q3 -->|否| FIX3[🔧 重构Service使用JdbcTemplate]
    
    Q4 -->|是| FIX4[🔧 检查API路由配置]
    Q4 -->|否| FIX5[🔧 查看应用启动日志]
    
    Q5 -->|是| FIX6[🔧 检查网络连接]
    Q5 -->|否| FIX7[🔧 重启应用服务]
    
    style START fill:#ef4444,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style FIX3 fill:#eab308,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style FIX7 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
```

### 4.2 验证检查流程

```mermaid
flowchart LR
    subgraph "四层验证流程"
        V1[#1 🗄️ 数据库验证<br/>SELECT COUNT(*)] --> V2[#2 🔌 API验证<br/>curl /api/business-knowledge]
        V2 --> V3[#3 🌐 前端验证<br/>打开页面检查]
        V3 --> V4[#4 🧪 功能验证<br/>测试增删改查]
    end
    
    style V1 fill:#06b6d4,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style V2 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style V3 fill:#af52de,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style V4 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
```

## 6. 服务重构对比

### 6.1 两种解决方案对比

```mermaid
flowchart TD
    subgraph "业务知识管理 - Service重构方案"
        BK1[🌐 前端页面] --> BK2[🔌 BusinessKnowledgeController]
        BK2 --> BK3[⚙️ BusinessKnowledgeService<br/>重构后]
        BK3 --> BK4[🔗 JdbcTemplate]
        BK4 --> BK5[🗄️ MySQL数据库]
    end

    subgraph "语义模型配置 - 端点重定向方案"
        SM1[🌐 前端页面<br/>修改API调用] --> SM2[🔌 SemanticModelPersistenceController]
        SM2 --> SM3[⚙️ SemanticModelPersistenceService<br/>现有服务]
        SM3 --> SM4[🔗 JdbcTemplate]
        SM4 --> SM5[🗄️ MySQL数据库]

        SM6[🔌 SemanticModelController<br/>内存存储] -.-> SM7[❌ 不再使用]
    end

    style BK3 fill:#eab308,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style SM1 fill:#eab308,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style SM3 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style SM7 fill:#ef4444,stroke:#ffffff,stroke-width:3px,color:#ffffff
```

### 6.2 重构前后架构对比

```mermaid
flowchart TD
    subgraph "重构前 - 内存存储架构"
        OLD1[🌐 前端页面] --> OLD2[🔌 API Controller]
        OLD2 --> OLD3[⚙️ BusinessKnowledgeService]
        OLD3 --> OLD4[💾 ConcurrentHashMap<br/>内存存储]
        OLD5[🗄️ MySQL数据库] -.-> OLD6[❌ 被忽略]
        
        OLD7[📝 硬编码数据<br/>initSampleData()] --> OLD4
    end
    
    subgraph "重构后 - 数据库存储架构"
        NEW1[🌐 前端页面] --> NEW2[🔌 API Controller]
        NEW2 --> NEW3[⚙️ BusinessKnowledgeService]
        NEW3 --> NEW4[🔗 JdbcTemplate]
        NEW4 --> NEW5[🗄️ MySQL数据库]
        
        NEW6[📄 SQL脚本<br/>data.sql] --> NEW5
    end
    
    style OLD4 fill:#ef4444,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style OLD6 fill:#ef4444,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style OLD7 fill:#ef4444,stroke:#ffffff,stroke-width:3px,color:#ffffff
    
    style NEW4 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style NEW5 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style NEW6 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
```

### 6.3 数据流对比

```mermaid
flowchart LR
    subgraph "重构前数据流"
        A1[应用启动] --> A2[执行initSampleData()]
        A2 --> A3[硬编码数据写入内存]
        A4[API请求] --> A5[从内存读取]
        A5 --> A6[返回固定数据]
    end
    
    subgraph "重构后数据流"
        B1[应用启动] --> B2[连接数据库]
        B3[API请求] --> B4[执行SQL查询]
        B4 --> B5[从数据库读取]
        B5 --> B6[返回最新数据]
    end
    
    style A3 fill:#ef4444,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style A6 fill:#ef4444,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style B5 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style B6 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
```

## 7. 完整更新架构总览

### 7.1 系统更新前后对比

```mermaid
flowchart TD
    subgraph "更新前系统架构"
        BEFORE1[🌐 前端页面] --> BEFORE2[🔌 Controller层]
        BEFORE2 --> BEFORE3[⚙️ Service层<br/>内存存储]
        BEFORE3 --> BEFORE4[💾 ConcurrentHashMap]
        BEFORE5[🗄️ MySQL数据库<br/>有数据但被忽略] -.-> BEFORE6[❌ 未使用]
    end

    subgraph "更新后系统架构"
        AFTER1[🌐 前端页面] --> AFTER2[🔌 Controller层]
        AFTER2 --> AFTER3[⚙️ Service层<br/>数据库存储]
        AFTER3 --> AFTER4[🔗 JdbcTemplate]
        AFTER4 --> AFTER5[🗄️ MySQL数据库<br/>基金/UT数据]
    end

    style BEFORE4 fill:#ef4444,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style BEFORE6 fill:#ef4444,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style AFTER4 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
    style AFTER5 fill:#10b981,stroke:#ffffff,stroke-width:3px,color:#ffffff
```

### 7.2 数据配置模块状态

| 配置模块 | 更新前状态 | 更新后状态 | 数据量 | 验证结果 |
|----------|------------|------------|--------|----------|
| **业务知识管理** | ❌ 内存存储<br/>硬编码示例数据 | ✅ 数据库存储<br/>Service重构 | 8条基金业务知识 | ✅ 验证通过 |
| **语义模型配置** | ❌ 内存存储<br/>硬编码示例数据 | ✅ 数据库存储<br/>端点重定向 | 24条基金字段配置 | ✅ 验证通过 |

---

**架构总结**: 通过两种不同的解决方案（Service重构 + 端点重定向），成功实现了 NL2SQL 系统从内存存储到数据库存储的完整转换，建立了数据更新与前端同步的完整机制，确保系统能够准确处理基金/UT领域的自然语言查询。
