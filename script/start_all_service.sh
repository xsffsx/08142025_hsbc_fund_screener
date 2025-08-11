#!/bin/bash

# =============================================================================
# NL2SQL 完整服务管理脚本
# 功能: 启动/重启/停止 外部服务 + Maven构建 + Spring Boot应用
# 作者: Augment Agent
# 日期: $(date +%Y%m%d)
# 基于: start_all_external_services.sh + 成功的Maven构建经验
# =============================================================================

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 路径配置 - 支持Spring AI Alibaba NL2SQL项目
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
WORKSPACE_ROOT="$(dirname "$SCRIPT_DIR")"
PROJECT_ROOT="$WORKSPACE_ROOT/spring-ai-alibaba/spring-ai-alibaba-nl2sql"
MANAGEMENT_MODULE="$PROJECT_ROOT/spring-ai-alibaba-nl2sql-management"
CHAT_MODULE="$PROJECT_ROOT/spring-ai-alibaba-nl2sql-chat"
COMMON_MODULE="$PROJECT_ROOT/spring-ai-alibaba-nl2sql-common"
LOG_FILE="$WORKSPACE_ROOT/logs/nl2sql-mvp1.log"
EXTERNAL_SERVICES_SCRIPT="$SCRIPT_DIR/start_all_external_services.sh"
PID_FILE="$WORKSPACE_ROOT/logs/spring-boot.pid"

# 检查Spring AI Alibaba NL2SQL项目是否存在
if [ ! -d "$PROJECT_ROOT" ]; then
    log_error "Spring AI Alibaba NL2SQL项目不存在: $PROJECT_ROOT"
    log_info "请确保已克隆Spring AI Alibaba项目"
    exit 1
fi

# 确保在项目根目录执行
cd "$PROJECT_ROOT"

# Java环境配置（修复版 - 使用正确的Java 17路径）
detect_java_home() {
    # 优先使用系统中的Java 17
    local java17_paths=(
        "/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home"
        "/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home"
        "/Users/paulo/IdeaProjects/zulu17.58.21-ca-jdk17.0.15-macosx_aarch64"
        "/opt/homebrew/opt/openjdk@17"
        "/usr/local/opt/openjdk@17"
    )

    for java_path in "${java17_paths[@]}"; do
        if [ -d "$java_path" ]; then
            export JAVA_HOME="$java_path"
            export PATH="$JAVA_HOME/bin:$PATH"
            log_info "检测到Java 17: $JAVA_HOME"
            return 0
        fi
    done

    log_error "未找到Java 17，请安装Java 17"
    return 1
}

# 调用Java检测
detect_java_home || exit 1
export SPRING_PROFILES_ACTIVE=mvp1

# JVM参数配置
export JAVA_OPTS="-Xmx8g -Xms4g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 归档旧日志文件
archive_old_logs() {
    local log_dir="$(dirname "$LOG_FILE")"
    local log_name="$(basename "$LOG_FILE")"
    local timestamp=$(date +%Y%m%d_%H%M%S)
    local archive_dir="$log_dir/archive"

    # 创建归档目录
    mkdir -p "$archive_dir"

    # 如果日志文件存在，则归档
    if [ -f "$LOG_FILE" ]; then
        local archive_file="$archive_dir/${log_name%.log}_${timestamp}.log"
        log_info "归档旧日志文件: $LOG_FILE -> $archive_file"
        mv "$LOG_FILE" "$archive_file"

        # 压缩归档文件以节省空间
        if command -v gzip >/dev/null 2>&1; then
            gzip "$archive_file"
            log_info "日志文件已压缩: ${archive_file}.gz"
        fi
    fi

    # 清理超过30天的归档文件
    if [ -d "$archive_dir" ]; then
        log_info "清理30天前的归档日志..."
        find "$archive_dir" -name "*.log*" -mtime +30 -delete 2>/dev/null || true
    fi
}

# 创建必要目录
create_directories() {
    log_info "创建必要目录..."
    mkdir -p "$(dirname "$LOG_FILE")"
    mkdir -p "$(dirname "$PID_FILE")"

    # 归档旧日志
    archive_old_logs

    log_success "目录创建完成"
}

# 检查Java环境
check_java() {
    log_info "检查Java环境..."
    if [ ! -d "$JAVA_HOME" ]; then
        log_error "Java环境不存在: $JAVA_HOME"
        return 1
    fi
    
    java -version 2>&1 | head -1
    log_success "Java环境检查通过"
}

# 启动外部服务 - 智能启动
start_external_services() {
    log_info "=== 智能启动外部服务 (跳过已运行的服务) ==="

    if [ ! -f "$EXTERNAL_SERVICES_SCRIPT" ]; then
        log_error "外部服务脚本不存在: $EXTERNAL_SERVICES_SCRIPT"
        return 1
    fi

    chmod +x "$EXTERNAL_SERVICES_SCRIPT"

    # 先验证外部服务状态
    cd "$WORKSPACE_ROOT"
    "$EXTERNAL_SERVICES_SCRIPT" verify
    local verify_result=$?

    if [ $verify_result -eq 0 ]; then
        log_success "所有外部服务已运行，跳过启动"
        return 0
    fi

    # 启动未运行的外部服务
    log_info "启动未运行的外部服务..."
    cd "$WORKSPACE_ROOT"
    "$EXTERNAL_SERVICES_SCRIPT" start

    if [ $? -eq 0 ]; then
        log_success "外部服务启动完成"
        return 0
    else
        log_error "外部服务启动失败"
        return 1
    fi
}

# 重启外部服务
restart_external_services() {
    log_info "=== 重启外部服务 ==="

    if [ ! -f "$EXTERNAL_SERVICES_SCRIPT" ]; then
        log_error "外部服务脚本不存在: $EXTERNAL_SERVICES_SCRIPT"
        return 1
    fi

    chmod +x "$EXTERNAL_SERVICES_SCRIPT"

    # 切换到工作空间根目录执行外部服务脚本
    cd "$WORKSPACE_ROOT"
    "$EXTERNAL_SERVICES_SCRIPT" restart

    if [ $? -eq 0 ]; then
        log_success "外部服务重启完成"
        return 0
    else
        log_error "外部服务重启失败"
        return 1
    fi
}

# 验证外部服务
verify_external_services() {
    log_info "验证外部服务状态..."

    # 切换到工作空间根目录执行外部服务脚本
    cd "$WORKSPACE_ROOT"
    "$EXTERNAL_SERVICES_SCRIPT" verify
    return $?
}

# 检查模块是否需要重新编译
needs_rebuild() {
    local module_path="$1"
    local module_name="$(basename "$module_path")"

    # 检查target目录是否存在
    if [ ! -d "$module_path/target" ]; then
        log_info "$module_name 需要首次编译"
        return 0
    fi

    # 检查是否有新的Java文件修改
    local newest_java=$(find "$module_path/src" -name "*.java" -newer "$module_path/target" 2>/dev/null | head -1)
    if [ -n "$newest_java" ]; then
        log_info "$module_name 检测到源码变更，需要重新编译"
        return 0
    fi

    # 检查pom.xml是否更新
    if [ "$module_path/pom.xml" -nt "$module_path/target" ]; then
        log_info "$module_name 检测到pom.xml变更，需要重新编译"
        return 0
    fi

    log_info "$module_name 无需重新编译"
    return 1
}

# 编译单个模块
compile_module() {
    local module_path="$1"
    local module_name="$(basename "$module_path")"
    local install_flag="${2:-install}"  # 默认使用install，确保依赖完整

    log_info "=== 编译模块: $module_name ==="

    if ! needs_rebuild "$module_path"; then
        log_success "$module_name 跳过编译"
        return 0
    fi

    cd "$module_path"
    log_info "当前目录: $(pwd)"

    # 应用代码格式化 (可选，失败不影响编译)
    log_info "尝试应用代码格式化..."
    mvn spring-javaformat:apply -q 2>/dev/null || log_warning "代码格式化失败，但不影响编译"

    # 默认使用clean install确保所有模块都安装到本地仓库
    local mvn_command="mvn clean install -DskipTests -Dmaven.test.skip=true -Dspring-javaformat.skip=true -Dspotless.skip=true"
    if [ "$install_flag" = "compile" ]; then
        mvn_command="mvn clean compile -DskipTests -Dmaven.test.skip=true -Dspring-javaformat.skip=true -Dspotless.skip=true"
        log_info "编译模块: $module_name (跳过格式检查)"
    else
        log_info "编译并安装到本地仓库: $module_name (跳过格式检查)"
    fi

    $mvn_command
    local build_result=$?

    if [ $build_result -eq 0 ]; then
        log_success "$module_name 编译成功"
        return 0
    else
        log_error "$module_name 编译失败 (退出码: $build_result)"
        log_error "请检查编译错误并修复后重试"
        return 1
    fi
}

# 检查并修复Maven依赖问题
check_maven_dependencies() {
    log_info "=== 检查Maven依赖状态 ==="

    # 检查关键类是否存在于本地仓库
    local chat_jar="$HOME/.m2/repository/com/alibaba/cloud/ai/spring-ai-alibaba-nl2sql-chat/1.0.0.3-SNAPSHOT/spring-ai-alibaba-nl2sql-chat-1.0.0.3-SNAPSHOT.jar"

    if [ -f "$chat_jar" ]; then
        # 检查CodeExecutorProperties是否存在于jar中
        if jar -tf "$chat_jar" | grep -q "CodeExecutorProperties.class"; then
            log_success "Maven依赖检查通过"
            return 0
        else
            log_warning "Chat模块jar文件缺少CodeExecutorProperties类，需要重新构建"
            return 1
        fi
    else
        log_warning "Chat模块jar文件不存在，需要重新构建"
        return 1
    fi
}

# Maven构建（优化版 - 支持增量编译和依赖管理）
maven_build() {
    log_info "=== 智能Maven构建项目 ==="

    # 确保在项目根目录
    cd "$PROJECT_ROOT"
    log_info "项目根目录: $(pwd)"

    # 检查模块是否存在
    if [ ! -d "$COMMON_MODULE" ]; then
        log_error "Common模块不存在: $COMMON_MODULE"
        return 1
    fi

    if [ ! -d "$CHAT_MODULE" ]; then
        log_error "Chat模块不存在: $CHAT_MODULE"
        return 1
    fi

    if [ ! -d "$MANAGEMENT_MODULE" ]; then
        log_error "Management模块不存在: $MANAGEMENT_MODULE"
        return 1
    fi

    # 检查依赖状态，如果有问题则强制重新构建依赖模块
    if ! check_maven_dependencies; then
        log_info "检测到依赖问题，强制重新构建依赖模块..."

        # 强制重新构建chat模块
        cd "$CHAT_MODULE"
        log_info "强制重新构建Chat模块..."
        mvn clean install -DskipTests -Dmaven.test.skip=true -Dspring-javaformat.skip=true -Dspotless.skip=true
        if [ $? -ne 0 ]; then
            log_error "Chat模块强制重建失败"
            return 1
        fi

        cd "$PROJECT_ROOT"
    fi

    # 1. 先编译并安装common模块（基础依赖）
    if ! compile_module "$COMMON_MODULE"; then
        log_error "Common模块编译失败，停止构建"
        return 1
    fi

    # 2. 再编译并安装chat模块（management模块依赖它）
    if ! compile_module "$CHAT_MODULE"; then
        log_error "Chat模块编译失败，停止构建"
        return 1
    fi

    # 3. 编译并安装management模块（主应用）
    if ! compile_module "$MANAGEMENT_MODULE"; then
        log_error "Management模块编译失败，停止构建"
        return 1
    fi

    # 4. Web UI模块是前端项目，不需要Maven编译
    local WEB_UI_MODULE="$PROJECT_ROOT/spring-ai-alibaba-nl2sql-web-ui"
    if [ -d "$WEB_UI_MODULE" ]; then
        log_info "检测到Web UI模块 (前端项目，跳过Maven编译)"
    fi

    log_success "所有模块构建完成"
    return 0
}

# 停止Spring Boot应用
stop_spring_boot() {
    log_info "停止Spring Boot应用..."
    
    if [ -f "$PID_FILE" ]; then
        local pid=$(cat "$PID_FILE")
        if ps -p $pid > /dev/null 2>&1; then
            log_info "正在停止Spring Boot应用 (PID: $pid)..."
            kill $pid
            
            # 等待进程结束
            for i in {1..30}; do
                if ! ps -p $pid > /dev/null 2>&1; then
                    log_success "Spring Boot应用已停止"
                    rm -f "$PID_FILE"
                    return 0
                fi
                sleep 1
            done
            
            # 强制终止
            log_warning "强制终止Spring Boot应用..."
            kill -9 $pid 2>/dev/null
            rm -f "$PID_FILE"
        else
            log_warning "PID文件存在但进程不存在，清理PID文件"
            rm -f "$PID_FILE"
        fi
    fi
    
    # 查找并终止可能的Spring Boot进程
    local spring_pids=$(ps aux | grep "spring-boot:run" | grep -v grep | awk '{print $2}')
    if [ -n "$spring_pids" ]; then
        log_info "发现运行中的Spring Boot进程，正在终止..."
        echo "$spring_pids" | xargs kill 2>/dev/null
        sleep 3
        echo "$spring_pids" | xargs kill -9 2>/dev/null
    fi
    
    log_success "Spring Boot应用停止完成"
}

# 启动Spring Boot应用 - 智能启动
start_spring_boot() {
    log_info "=== 检查并启动Spring Boot应用 ==="

    # 先检查应用是否已经运行
    if [ -f "$PID_FILE" ]; then
        local existing_pid=$(cat "$PID_FILE")
        if ps -p $existing_pid > /dev/null 2>&1; then
            log_info "检查现有Spring Boot应用状态 (PID: $existing_pid)..."

            # 检查API是否响应 (检查主页面而不是actuator)
            if curl -f http://localhost:8065/nl2sql.html > /dev/null 2>&1; then
                log_success "Spring Boot应用已运行且响应正常，跳过启动"
                return 0
            else
                log_warning "Spring Boot应用进程存在但API无响应，重新启动..."
                stop_spring_boot
            fi
        else
            log_warning "PID文件存在但进程不存在，清理并重新启动..."
            rm -f "$PID_FILE"
        fi
    fi

    # 检查端口是否被占用
    if lsof -i :8065 > /dev/null 2>&1; then
        log_warning "端口8065已被占用，尝试停止现有进程..."
        local port_pid=$(lsof -t -i :8065)
        if [ -n "$port_pid" ]; then
            kill $port_pid 2>/dev/null
            sleep 3
        fi
    fi

    cd "$MANAGEMENT_MODULE"

    log_info "Spring Boot配置:"
    log_info "- 主类: com.alibaba.cloud.ai.Application"
    log_info "- Profile: mvp1"
    log_info "- 端口: 8065"
    log_info "- JVM内存: 4GB-8GB"
    log_info "- 日志文件: $LOG_FILE"

    # 启动应用并将日志输出到指定文件
    log_info "启动Spring Boot应用..."
    nohup ../../mvnw spring-boot:run \
        -Dspring-boot.run.profiles=mvp1 \
        -Dmaven.test.skip=true \
        -Dspring-javaformat.skip=true \
        -Dspotless.skip=true \
        > "$LOG_FILE" 2>&1 &

    # 保存PID
    local spring_pid=$!
    echo $spring_pid > "$PID_FILE"

    log_info "Spring Boot应用已启动，PID: $spring_pid"
    log_info "日志文件: $LOG_FILE"

    # 等待应用启动
    log_info "等待应用启动完成..."
    for i in {1..60}; do
        if curl -f http://localhost:8065/nl2sql.html > /dev/null 2>&1; then
            log_success "Spring Boot应用启动成功！"
            return 0
        fi
        if ! ps -p $spring_pid > /dev/null 2>&1; then
            log_error "Spring Boot应用启动失败，进程已退出"
            rm -f "$PID_FILE"
            return 1
        fi
        echo -n "."
        sleep 2
    done

    log_error "Spring Boot应用启动超时"
    log_info "请检查日志文件: $LOG_FILE"
    return 1
}

# 检查应用状态
check_application_status() {
    log_info "=== 检查应用状态 ==="
    
    # 检查外部服务
    if ! verify_external_services; then
        log_error "外部服务状态异常"
        return 1
    fi
    
    # 检查Spring Boot应用
    if [ -f "$PID_FILE" ]; then
        local pid=$(cat "$PID_FILE")
        if ps -p $pid > /dev/null 2>&1; then
            log_success "Spring Boot应用运行正常 (PID: $pid)"
            
            # 检查API响应
            if curl -f http://localhost:8065/nl2sql.html > /dev/null 2>&1; then
                log_success "API响应正常"
            else
                log_warning "API无响应"
            fi
        else
            log_error "Spring Boot应用进程不存在"
            rm -f "$PID_FILE"
            return 1
        fi
    else
        log_error "Spring Boot应用未启动"
        return 1
    fi
    
    return 0
}

# 显示服务信息
show_service_info() {
    log_success "=== NL2SQL服务信息 ==="
    echo ""
    log_info "服务访问地址:"
    echo "- NL2SQL流式查询: http://localhost:8065/nl2sql/stream/search?query=您的查询"
    echo "- Web界面: http://localhost:8065/nl2sql.html"
    echo "- API文档: http://localhost:8065/swagger-ui.html"
    echo ""
    log_info "外部服务:"
    echo "- MySQL数据库: localhost:3306"
    echo "- Qdrant向量库: http://localhost:6333"
    echo "- LM Studio: http://localhost:1234"
    echo ""
    log_info "管理命令:"
    echo "- 查看日志: tail -f $LOG_FILE"
    echo "- 重启服务: $0 restart"
    echo "- 停止服务: $0 stop"
    echo "- 检查状态: $0 status"
}

# 快速重启（仅重启Spring Boot应用，用于开发调试）
quick_restart() {
    log_info "=== 快速重启Spring Boot应用 (开发模式) ==="

    # 检查外部服务状态
    if ! verify_external_services; then
        log_warning "外部服务状态异常，建议使用 'restart' 命令完整重启"
        read -p "是否继续快速重启? (y/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            log_info "取消快速重启"
            return 1
        fi
    fi

    # 停止Spring Boot应用
    stop_spring_boot

    # 增量编译
    if ! maven_build; then
        log_error "编译失败，无法启动应用"
        return 1
    fi

    # 启动Spring Boot应用
    if ! start_spring_boot; then
        log_error "应用启动失败"
        return 1
    fi

    show_service_info
    log_success "快速重启完成！"
}

# 强制重新编译所有模块
force_rebuild() {
    log_info "=== 强制重新编译所有模块 ==="

    cd "$PROJECT_ROOT"

    # 清理所有target目录和Maven缓存
    log_info "清理编译缓存..."
    find . -name "target" -type d -exec rm -rf {} + 2>/dev/null || true

    # 清理本地Maven仓库中的项目artifacts
    log_info "清理本地Maven仓库中的项目artifacts..."
    rm -rf "$HOME/.m2/repository/com/alibaba/cloud/ai/spring-ai-alibaba-nl2sql"* 2>/dev/null || true

    # 强制重新编译所有模块
    force_rebuild_all_modules
}

# 强制重新编译所有模块（不检查是否需要重建）
force_rebuild_all_modules() {
    log_info "=== 强制重新编译所有NL2SQL模块 ==="

    cd "$PROJECT_ROOT"

    # 定义所有Maven模块路径 (排除前端项目)
    local modules=(
        "$COMMON_MODULE"
        "$CHAT_MODULE"
        "$MANAGEMENT_MODULE"
    )

    # 逐个强制编译所有模块
    for module_path in "${modules[@]}"; do
        if [ -d "$module_path" ]; then
            local module_name="$(basename "$module_path")"
            log_info "=== 强制编译模块: $module_name ==="

            cd "$module_path"
            log_info "当前目录: $(pwd)"

            # 应用代码格式化
            log_info "尝试应用代码格式化..."
            mvn spring-javaformat:apply -q 2>/dev/null || log_warning "代码格式化失败，但不影响编译"

            # 强制clean install
            log_info "强制编译并安装到本地仓库: $module_name (跳过格式检查)"
            mvn clean install -DskipTests -Dmaven.test.skip=true -Dspring-javaformat.skip=true -Dspotless.skip=true

            local build_result=$?
            if [ $build_result -eq 0 ]; then
                log_success "$module_name 编译成功"
            else
                log_error "$module_name 编译失败 (退出码: $build_result)"
                log_error "请检查编译错误并修复后重试"
                return 1
            fi
        else
            log_warning "模块不存在，跳过: $module_path"
        fi
    done

    log_success "所有模块强制重建完成"
    return 0
}

# 主函数
main() {
    echo "=== NL2SQL 完整服务管理脚本 ==="
    echo "支持的操作: start, restart, stop, status, quick, rebuild"
    echo ""

    case "${1:-start}" in
        "start")
            log_info "智能启动所有服务 (跳过已运行的服务)..."
            create_directories
            check_java || exit 1
            start_external_services || exit 1
            maven_build || exit 1
            start_spring_boot || exit 1
            show_service_info
            ;;
        "restart")
            log_info "重启所有服务..."
            create_directories
            check_java || exit 1
            stop_spring_boot
            restart_external_services || exit 1
            maven_build || exit 1
            start_spring_boot || exit 1
            show_service_info
            ;;
        "quick")
            log_info "快速重启Spring Boot应用 (开发模式)..."
            create_directories
            check_java || exit 1
            quick_restart || exit 1
            ;;
        "rebuild")
            log_info "强制重新编译所有模块..."
            create_directories
            check_java || exit 1
            force_rebuild || exit 1
            log_success "重新编译完成"
            ;;
        "stop")
            log_info "停止所有服务..."
            stop_spring_boot
            log_success "服务停止完成"
            ;;
        "status")
            check_application_status
            if [ $? -eq 0 ]; then
                show_service_info
            fi
            ;;
        *)
            echo "用法: $0 {start|restart|quick|rebuild|stop|status}"
            echo ""
            echo "  start   - 智能启动所有服务 (跳过已运行的服务)"
            echo "  restart - 重启所有服务 (外部服务 + Maven构建 + Spring Boot)"
            echo "  quick   - 快速重启Spring Boot应用 (仅用于开发调试)"
            echo "  rebuild - 强制重新编译所有模块"
            echo "  stop    - 停止Spring Boot应用"
            echo "  status  - 检查服务状态"
            echo ""
            echo "开发建议:"
            echo "  - 首次启动: $0 start"
            echo "  - 代码修改后: $0 quick"
            echo "  - 编译问题时: $0 rebuild"
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"
