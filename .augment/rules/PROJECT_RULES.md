---
type: "always_apply"
---

#1 rewrite中文回答

#1.1 **代码修改确认规则**:
    - 默认alibaba的源代码是正确的
    - 如果需要修改原来码，必须停止并等待用户确认
    - 不要擅自修改已有的正确代码逻辑

#2 **智能文档路径发现与选择规则**:
    ## 2.1 增强文档路径发现
    - **主要搜索命令**: `find . -type d \( -name "doc" -o -name "docs" \) | grep -v node_modules | grep -v ".venv" | grep -v target | grep -v build | grep -v dist`
    - **子目录深度搜索**: 同时搜索doc下的功能子目录，如 `doc/feature`, `doc/analysis`, `doc/guide`
    - **完整搜索策略**:
      ```bash
      # 搜索所有doc相关目录
      find . -type d \( -name "doc" -o -name "docs" \) -not -path "*/node_modules/*" -not -path "*/.venv/*" -not -path "*/target/*" -not -path "*/build/*" -not -path "*/dist/*"

      # 搜索doc子目录
      find . -path "*/doc/*" -type d -not -path "*/node_modules/*" -not -path "*/.venv/*" -not -path "*/target/*"
      ```
    - **排除目录**: node_modules, .venv, target, build, dist, .git, __pycache__
    - **优先级排序**: 功能级doc > 模块级doc > 项目级doc > 根目录doc

    ## 2.2 智能路径选择逻辑
    - **业务相关性**: 根据问题内容关键词匹配项目/模块/功能名称
    - **代码距离**: 选择与相关代码文件最近的doc文件夹
    - **功能匹配**: 优先选择有对应功能子目录的doc文件夹
    - **文档类型映射**:
      * API文档 → `./service_name/doc/api/`
      * 架构文档 → `./project_name/doc/architecture/`
      * 技术文档 → `./module_name/doc/technical/`
      * 业务文档 → `./module_name/doc/business/`
      * 分析文档 → `./module_name/doc/analysis/`
      * 指南文档 → `./module_name/doc/guide/`

    ## 2.3 强制文档命名规范
    - **强制格式**: `yyyymmddhh24mmss_<编号>_<文档类型>_<简短描述>.md`
    - **时间格式**: 24小时制，精确到秒 (例: 20250104143025)
    - **编号规则**: 两位数字，同日期同小时内递增 (01, 02, 03...)
    - **文档类型**: API, Architecture, Technical, Business, Guide, Analysis, Test, Config
    - **命名示例**:
      * `20250104143025_01_Architecture_AI_Decision_System.md`
      * `20250104143026_02_Technical_Text2SQL_Implementation.md`
      * `20250104143027_03_Business_Dataset_Selection_Matrix.md`
    - **验证规则**: 文件名必须匹配正则表达式 `^\d{14}_\d{2}_[A-Z][a-zA-Z]+_[a-zA-Z0-9_]+\.md$`

#3 **测试文档管理规则**:
    - 项目最近的文件夹 ./test，如果test文件夹不存在则创建
    - 测试文档文件名前缀: `yyyymmddhhmmss_`
    - 测试文档应包含: 测试场景、预期结果、实际结果

#4 **Git提交规则**:
    - commit message前缀: "[no-jira] "
    - 格式: "[no-jira] 文档类型: 简短描述"
    - 示例: "[no-jira] docs: 添加AI决策系统架构文档"

#5 **文档更新优先规则**:
    ## 5.1 更新检查流程
    - 每次添加md前，先扫描目标doc文件夹现有文档
    - 检查是否存在相同主题或相关内容的文档
    - 优先更新现有文档，而非创建新文档

    ## 5.2 更新判断标准
    - 主题相似度 > 70%: 更新现有文档
    - 内容互补性: 可合并到现有文档的新章节
    - 版本迭代: 同一功能的不同版本文档

#6 **MD文档存放规则**:
    - **就近原则**: 新增Markdown文档放到项目最接近的 ./doc 文件夹
    - **避免根目录**: 不要放到workspace根目录的 /doc 文件夹
    - **模块化管理**: 确保文档与相关项目代码保持接近
    - **层级结构**:
      * 项目级: `./project_name/doc/`
      * 模块级: `./project_name/module_name/doc/`
      * 功能级: `./project_name/module_name/doc/feature`

#8 **文档质量标准**:
    ## 8.1 必需元素
    - 文档标题和创建时间
    - 目录结构 (TOC)
    - 核心内容章节
    - Mermaid图表 (架构/流程文档必需)
    - 代码示例 (技术文档必需)

    ## 8.2 格式要求
    - 使用中文编写，专业术语保留英文
    - 代码块使用语法高亮
    - 图表使用Mermaid格式
    - 链接使用相对路径

#9 **Mermaid图表样式规范**:
    ## 9.1 标准样式模板
    - **参考标准**: 使用 `/Users/paulo/IdeaProjects/20250707_MCP/.augment/20250802140100_01_Level1_Architecture_Diagram.md` 作为样式模板
    - **颜色编码**: 业务逻辑颜色编码系统
      * 绿色 (#10b981): 快速操作 (<1s)
      * 黄色 (#eab308): AI处理 (10-20s)
      * 红色 (#ef4444): 瓶颈操作 (120s+)
      * 蓝色 (#3b82f6): 外部服务
      * 青色 (#06b6d4): 数据层
      * 紫色 (#af52de): UI层

    ## 9.2 图表元素规范
    - **节点样式**: `fill:transparent,stroke:#颜色,stroke-width:3px,color:#ffffff`
    - **框架样式**: `fill:transparent,stroke:#颜色,stroke-width:2px,stroke-dasharray: 4 2,color:#ffffff`
    - **连接线样式**: `stroke:#颜色,stroke-width:3px`
    - **编号格式**: 使用 #1, #2 文本编号格式 (绝不修改避免错误)

    ## 9.3 图表类型标准
    - **架构图**: flowchart TD (自上而下)
    - **流程图**: flowchart LR (从左到右)
    - **时间线**: timeline
    - **饼图**: pie title "标题"
    - **甘特图**: gantt
    - **思维导图**: mindmap

    ## 9.4 图表排版要求
    - **层级结构**: 使用 subgraph 明确分层
    - **图标使用**: 节点名称包含相关emoji图标
    - **注释规范**: 使用 %% 添加样式注释
    - **响应式设计**: 确保在不同屏幕尺寸下可读