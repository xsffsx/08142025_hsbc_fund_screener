# Maven离线构建快速启动指南

**创建时间**: 2025-08-11 16:20:00  
**适用项目**: Spring AI Alibaba NL2SQL  
**目标用户**: 开发人员、运维人员

## 快速开始

### 1. 环境要求
- Java 17+ (脚本会自动检测和配置)
- 网络连接 (仅首次预热需要)
- 磁盘空间 500MB+ (用于本地仓库)

### 2. 一键启动
```bash
# 进入项目目录
cd /Users/paulo/PycharmProjects/20250809

# 快速完整流程 (推荐)
./script/start_maven_stand_alone.sh quick-all
```

### 3. 分步执行
```bash
# 步骤1: 快速初始化 (需要网络)
./script/start_maven_stand_alone.sh quick-init

# 步骤2: 离线构建
./script/start_maven_stand_alone.sh build

# 步骤3: 启动服务
./script/start_maven_stand_alone.sh start
```

## 脚本命令详解

### 可用命令
| 命令 | 功能 | 网络需求 | 耗时 |
|------|------|----------|------|
| `quick-init` | 快速预热核心依赖 | 需要 | 5-10分钟 |
| `init` | 完整预热所有依赖 | 需要 | 15-25分钟 |
| `build` | 离线构建项目 | 不需要 | 2-5分钟 |
| `start` | 启动NL2SQL服务 | 不需要 | 30秒 |
| `quick-all` | 快速完整流程 | 需要 | 8-15分钟 |
| `all` | 完整流程 | 需要 | 20-30分钟 |
| `status` | 检查服务状态 | 不需要 | 5秒 |

### 并行优化说明
- **quick-init**: 12线程下载 + 4线程构建
- **init**: 8线程下载 + 4线程构建
- 根据机器配置自动调整性能

## 文件结构说明

### 新增文件
```
spring-ai-alibaba/
├── .mvn/
│   ├── maven.config          # Maven默认参数
│   ├── settings.xml          # 离线模式配置
│   ├── local-repo/          # 本地依赖仓库 (139MB+)
│   └── parallel-config.md    # 并行配置说明
├── .gitattributes           # Git LFS配置
└── script/
    └── start_maven_stand_alone.sh  # 主启动脚本
```

### 配置文件作用
- **maven.config**: 设置离线模式和本地仓库路径
- **settings.xml**: 配置镜像仓库为本地文件系统
- **local-repo/**: 存储所有Maven依赖和插件
- **.gitattributes**: 配置JAR文件使用Git LFS

## 常见问题解决

### Q1: Java版本错误
```bash
# 错误信息
[ERROR] 需要Java 17或更高版本，当前版本：11

# 解决方案
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
# 或者让脚本自动检测 (推荐)
```

### Q2: 网络连接失败
```bash
# 错误信息
[WARN] 无法连接到网络，跳过依赖预热

# 解决方案
# 1. 检查网络连接
ping 8.8.8.8

# 2. 使用已有的本地仓库
./script/start_maven_stand_alone.sh build
```

### Q3: 依赖缺失
```bash
# 错误信息
[WARNING] The POM for xxx:jar:xxx is missing

# 解决方案
# 1. 重新预热依赖
./script/start_maven_stand_alone.sh quick-init

# 2. 或使用完整预热
./script/start_maven_stand_alone.sh init
```

### Q4: 构建失败
```bash
# 错误信息
[ERROR] Failed to execute goal

# 解决方案
# 1. 查看详细日志
tail -f logs/maven_standalone_*.log

# 2. 清理后重试
rm -rf spring-ai-alibaba/.mvn/local-repo
./script/start_maven_stand_alone.sh quick-init
```

## 性能优化建议

### 1. 机器配置调优
```bash
# 高配置机器 (8核+)
export MAVEN_OPTS="-Xmx4g -Xms2g"
# 修改脚本中的线程数为16

# 低配置机器 (4核-)
export MAVEN_OPTS="-Xmx2g -Xms1g"
# 修改脚本中的线程数为4
```

### 2. 网络优化
```bash
# 设置更长的超时时间
-Dmaven.wagon.http.connectionTimeout=120000
-Dmaven.wagon.http.readTimeout=120000
```

### 3. 磁盘优化
- 使用SSD存储本地仓库
- 定期清理不需要的依赖版本

## 服务访问

### 启动成功后访问地址
- **NL2SQL服务**: http://localhost:8065
- **健康检查**: http://localhost:8065/actuator/health
- **API文档**: http://localhost:8065/swagger-ui.html (如果可用)

### 服务状态检查
```bash
# 检查服务是否启动
./script/start_maven_stand_alone.sh status

# 手动检查
curl -s http://localhost:8065/actuator/health
```

## 维护说明

### 1. 依赖更新
当项目依赖更新时：
```bash
# 1. 清理旧依赖
rm -rf spring-ai-alibaba/.mvn/local-repo

# 2. 重新预热
./script/start_maven_stand_alone.sh quick-init
```

### 2. 版本管理
建议为不同版本创建不同的本地仓库：
```bash
# 重命名当前仓库
mv .mvn/local-repo .mvn/local-repo-v1.0.0.3

# 创建新版本仓库
./script/start_maven_stand_alone.sh quick-init
```

### 3. 空间清理
```bash
# 查看仓库大小
du -sh spring-ai-alibaba/.mvn/local-repo

# 清理不需要的依赖 (谨慎操作)
# 建议备份后再清理
```

## 故障排除

### 日志位置
- **脚本日志**: `logs/maven_standalone_*.log`
- **Maven日志**: 在脚本日志中包含
- **应用日志**: 控制台输出

### 常用调试命令
```bash
# 查看Java环境
java -version
echo $JAVA_HOME

# 查看Maven配置
cat spring-ai-alibaba/.mvn/maven.config

# 查看本地仓库内容
ls -la spring-ai-alibaba/.mvn/local-repo

# 测试离线构建
cd spring-ai-alibaba
./mvnw clean compile -o -pl spring-ai-alibaba-nl2sql
```

---

**文档版本**: v1.0  
**最后更新**: 2025-08-11 16:20:00  
**支持联系**: 开发团队
