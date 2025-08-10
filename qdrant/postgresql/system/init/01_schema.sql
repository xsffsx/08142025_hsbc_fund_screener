-- PostgreSQL数据库初始化脚本，兼容Spring Boot SQL初始化
-- 启用pgvector扩展
CREATE EXTENSION IF NOT EXISTS vector;

-- 业务知识表
CREATE TABLE IF NOT EXISTS business_knowledge (
  id BIGSERIAL PRIMARY KEY,
  business_term VARCHAR(255) NOT NULL, -- 业务名词
  description TEXT, -- 描述
  synonyms TEXT, -- 同义词
  is_recall BOOLEAN DEFAULT true, -- 是否召回
  data_set_id VARCHAR(255), -- 数据集id
  agent_id BIGINT, -- 关联的智能体ID
  created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
  updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 更新时间
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_business_knowledge_term ON business_knowledge(business_term);
CREATE INDEX IF NOT EXISTS idx_business_knowledge_data_set_id ON business_knowledge(data_set_id);
CREATE INDEX IF NOT EXISTS idx_business_knowledge_agent_id ON business_knowledge(agent_id);
CREATE INDEX IF NOT EXISTS idx_business_knowledge_is_recall ON business_knowledge(is_recall);

-- 语义模型表
CREATE TABLE IF NOT EXISTS semantic_model (
  id BIGSERIAL PRIMARY KEY,
  agent_id BIGINT, -- 关联的智能体ID
  field_name VARCHAR(255) NOT NULL DEFAULT '', -- 智能体字段名称
  synonyms TEXT, -- 字段名称同义词
  origin_name VARCHAR(255) DEFAULT '', -- 原始字段名
  description TEXT, -- 字段描述
  origin_description VARCHAR(255), -- 原始字段描述
  type VARCHAR(255) DEFAULT '', -- 字段类型 (integer, varchar....)
  created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
  updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 更新时间
  is_recall BOOLEAN DEFAULT true, -- 是否启用召回
  status BOOLEAN DEFAULT true -- 是否启用
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_semantic_model_agent_id ON semantic_model(agent_id);
CREATE INDEX IF NOT EXISTS idx_semantic_model_field_name ON semantic_model(field_name);
CREATE INDEX IF NOT EXISTS idx_semantic_model_status ON semantic_model(status);
CREATE INDEX IF NOT EXISTS idx_semantic_model_is_recall ON semantic_model(is_recall);

-- 智能体表
CREATE TABLE IF NOT EXISTS agent (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL, -- 智能体名称
  description TEXT, -- 智能体描述
  avatar VARCHAR(500), -- 头像URL
  status VARCHAR(50) DEFAULT 'draft', -- 状态：draft-待发布，published-已发布，offline-已下线
  prompt TEXT, -- 自定义Prompt配置
  category VARCHAR(100), -- 分类
  admin_id BIGINT, -- 管理员ID
  tags TEXT, -- 标签，逗号分隔
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 更新时间
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_agent_name ON agent(name);
CREATE INDEX IF NOT EXISTS idx_agent_status ON agent(status);
CREATE INDEX IF NOT EXISTS idx_agent_category ON agent(category);
CREATE INDEX IF NOT EXISTS idx_agent_admin_id ON agent(admin_id);

-- 智能体知识表
CREATE TABLE IF NOT EXISTS agent_knowledge (
  id BIGSERIAL PRIMARY KEY,
  agent_id BIGINT NOT NULL, -- 智能体ID
  title VARCHAR(255) NOT NULL, -- 知识标题
  content TEXT, -- 知识内容
  type VARCHAR(50) DEFAULT 'document', -- 知识类型：document-文档，qa-问答，faq-常见问题
  category VARCHAR(100), -- 知识分类
  tags TEXT, -- 标签，逗号分隔
  status VARCHAR(50) DEFAULT 'active', -- 状态：active-启用，inactive-禁用
  source_url VARCHAR(500), -- 来源URL
  file_path VARCHAR(500), -- 文件路径
  file_size BIGINT, -- 文件大小（字节）
  file_type VARCHAR(100), -- 文件类型
  embedding_status VARCHAR(50) DEFAULT 'pending', -- 向量化状态：pending-待处理，processing-处理中，completed-已完成，failed-失败
  creator_id BIGINT, -- 创建者ID
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 更新时间
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_agent_knowledge_agent_id ON agent_knowledge(agent_id);
CREATE INDEX IF NOT EXISTS idx_agent_knowledge_title ON agent_knowledge(title);
CREATE INDEX IF NOT EXISTS idx_agent_knowledge_type ON agent_knowledge(type);
CREATE INDEX IF NOT EXISTS idx_agent_knowledge_status ON agent_knowledge(status);
CREATE INDEX IF NOT EXISTS idx_agent_knowledge_category ON agent_knowledge(category);
CREATE INDEX IF NOT EXISTS idx_agent_knowledge_embedding_status ON agent_knowledge(embedding_status);

-- 数据源表
CREATE TABLE IF NOT EXISTS datasource (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL, -- 数据源名称
  type VARCHAR(50) NOT NULL, -- 数据源类型：mysql, postgresql, oracle
  host VARCHAR(255) NOT NULL, -- 主机地址
  port INTEGER NOT NULL, -- 端口号
  database_name VARCHAR(255) NOT NULL, -- 数据库名称
  username VARCHAR(255) NOT NULL, -- 用户名
  password VARCHAR(255) NOT NULL, -- 密码（加密存储）
  connection_url VARCHAR(1000), -- 完整连接URL
  status VARCHAR(50) DEFAULT 'active', -- 状态：active-启用，inactive-禁用
  test_status VARCHAR(50) DEFAULT 'unknown', -- 连接测试状态：success-成功，failed-失败，unknown-未知
  description TEXT, -- 描述
  creator_id BIGINT, -- 创建者ID
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 更新时间
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_datasource_name ON datasource(name);
CREATE INDEX IF NOT EXISTS idx_datasource_type ON datasource(type);
CREATE INDEX IF NOT EXISTS idx_datasource_status ON datasource(status);
CREATE INDEX IF NOT EXISTS idx_datasource_creator_id ON datasource(creator_id);

-- 智能体数据源关联表
CREATE TABLE IF NOT EXISTS agent_datasource (
  id BIGSERIAL PRIMARY KEY,
  agent_id BIGINT NOT NULL, -- 智能体ID
  datasource_id BIGINT NOT NULL, -- 数据源ID
  is_active INTEGER DEFAULT 1, -- 是否启用 (0=禁用, 1=启用)
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 更新时间
);

-- 创建索引和约束
CREATE UNIQUE INDEX IF NOT EXISTS uk_agent_datasource ON agent_datasource(agent_id, datasource_id);
CREATE INDEX IF NOT EXISTS idx_agent_datasource_agent_id ON agent_datasource(agent_id);
CREATE INDEX IF NOT EXISTS idx_agent_datasource_datasource_id ON agent_datasource(datasource_id);
CREATE INDEX IF NOT EXISTS idx_agent_datasource_is_active ON agent_datasource(is_active);

-- 智能体预设问题表
CREATE TABLE IF NOT EXISTS agent_preset_question (
  id BIGSERIAL PRIMARY KEY,
  agent_id BIGINT NOT NULL, -- 智能体ID
  question TEXT NOT NULL, -- 预设问题内容
  sort_order INTEGER DEFAULT 0, -- 排序顺序
  is_active BOOLEAN DEFAULT true, -- 是否启用
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 更新时间
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_agent_preset_question_agent_id ON agent_preset_question(agent_id);
CREATE INDEX IF NOT EXISTS idx_agent_preset_question_sort_order ON agent_preset_question(sort_order);
CREATE INDEX IF NOT EXISTS idx_agent_preset_question_is_active ON agent_preset_question(is_active);
