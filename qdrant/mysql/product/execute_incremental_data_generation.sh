#!/bin/bash

# =====================================================
# Cognos增量测试数据生成执行脚本
# 在现有数据基础上增加3000-5000条新记录
# 创建时间: 2025-01-04 16:55:00
# =====================================================

set -e  # 遇到错误立即退出

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

# 检查MySQL容器是否运行
check_mysql_container() {
    log_info "检查MySQL容器状态..."
    if ! docker ps | grep -q mysql-nl2sql-mvp1; then
        log_error "MySQL容器未运行，请先启动容器"
        exit 1
    fi
    log_success "MySQL容器运行正常"
}

# 检查数据库连接
check_database_connection() {
    log_info "检查数据库连接..."
    if ! docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "USE nl2sql; SELECT 1;" > /dev/null 2>&1; then
        log_error "无法连接到数据库"
        exit 1
    fi
    log_success "数据库连接正常"
}

# 显示当前数据统计
show_current_data_statistics() {
    log_info "显示当前数据统计..."
    echo "========================================"
    echo "📊 当前数据库统计信息"
    echo "========================================"
    
    docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 --default-character-set=utf8mb4 -e "
    USE nl2sql;
    SELECT 
        'TB_PROD' AS table_name, 
        COUNT(*) AS current_count,
        MIN(PROD_ID) AS min_id,
        MAX(PROD_ID) AS max_id
    FROM TB_PROD
    UNION ALL
    SELECT 
        'TB_PROD_USER_DEFIN_EXT_FIELD', 
        COUNT(*),
        MIN(PROD_ID),
        MAX(PROD_ID)
    FROM TB_PROD_USER_DEFIN_EXT_FIELD
    UNION ALL
    SELECT 
        'TB_DEBT_INSTM', 
        COUNT(*),
        MIN(PROD_ID_DEBT_INSTM),
        MAX(PROD_ID_DEBT_INSTM)
    FROM TB_DEBT_INSTM
    UNION ALL
    SELECT 
        'TB_EQTY_LINK_INVST', 
        COUNT(*),
        MIN(PROD_ID_EQTY_LINK_INVST),
        MAX(PROD_ID_EQTY_LINK_INVST)
    FROM TB_EQTY_LINK_INVST
    UNION ALL
    SELECT 
        'TB_EQTY_LINK_INVST_UNDL_STOCK', 
        COUNT(*),
        MIN(PROD_ID_EQTY_LINK_INVST),
        MAX(PROD_ID_EQTY_LINK_INVST)
    FROM TB_EQTY_LINK_INVST_UNDL_STOCK
    UNION ALL
    SELECT 
        'TB_PROD_ALT_ID', 
        COUNT(*),
        MIN(PROD_ID),
        MAX(PROD_ID)
    FROM TB_PROD_ALT_ID;
    " 2>/dev/null
    
    echo "========================================"
}

# 执行SQL文件
execute_sql_file() {
    local sql_file=$1
    local description=$2
    local start_time=$(date +%s)
    
    log_info "开始执行: $description ($sql_file)"
    
    if [ ! -f "$sql_file" ]; then
        log_error "SQL文件不存在: $sql_file"
        return 1
    fi
    
    # 执行SQL文件
    if docker exec -i mysql-nl2sql-mvp1 mysql -u root -proot123 --default-character-set=utf8mb4 nl2sql < "$sql_file"; then
        local end_time=$(date +%s)
        local duration=$((end_time - start_time))
        log_success "$description 完成 (耗时: ${duration}秒)"
        return 0
    else
        log_error "$description 失败"
        return 1
    fi
}

# 显示增量数据统计
show_incremental_data_statistics() {
    log_info "显示增量数据统计..."
    echo "========================================"
    echo "📈 增量数据生成统计"
    echo "========================================"
    
    docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 --default-character-set=utf8mb4 -e "
    USE nl2sql;
    SELECT 
        'TB_PROD (新增)' AS table_name, 
        COUNT(*) AS new_records,
        CASE 
            WHEN COUNT(*) >= 2500 THEN '✅ 达标'
            WHEN COUNT(*) >= 1500 THEN '⚠️ 部分达标'
            ELSE '❌ 未达标'
        END AS status
    FROM TB_PROD WHERE PROD_ID BETWEEN 4001 AND 7000
    UNION ALL
    SELECT 
        'TB_PROD_USER_DEFIN_EXT_FIELD (新增)', 
        COUNT(*),
        CASE 
            WHEN COUNT(*) >= 8000 THEN '✅ 达标'
            WHEN COUNT(*) >= 5000 THEN '⚠️ 部分达标'
            ELSE '❌ 未达标'
        END
    FROM TB_PROD_USER_DEFIN_EXT_FIELD WHERE PROD_ID BETWEEN 4001 AND 7000
    UNION ALL
    SELECT 
        'TB_DEBT_INSTM (新增)', 
        COUNT(*),
        CASE 
            WHEN COUNT(*) >= 200 THEN '✅ 达标'
            WHEN COUNT(*) >= 100 THEN '⚠️ 部分达标'
            ELSE '❌ 未达标'
        END
    FROM TB_DEBT_INSTM WHERE PROD_ID_DEBT_INSTM BETWEEN 4001 AND 7000
    UNION ALL
    SELECT 
        'TB_EQTY_LINK_INVST (新增)', 
        COUNT(*),
        CASE 
            WHEN COUNT(*) >= 200 THEN '✅ 达标'
            WHEN COUNT(*) >= 100 THEN '⚠️ 部分达标'
            ELSE '❌ 未达标'
        END
    FROM TB_EQTY_LINK_INVST WHERE PROD_ID_EQTY_LINK_INVST BETWEEN 4001 AND 7000
    UNION ALL
    SELECT 
        'TB_EQTY_LINK_INVST_UNDL_STOCK (新增)', 
        COUNT(*),
        CASE 
            WHEN COUNT(*) >= 400 THEN '✅ 达标'
            WHEN COUNT(*) >= 200 THEN '⚠️ 部分达标'
            ELSE '❌ 未达标'
        END
    FROM TB_EQTY_LINK_INVST_UNDL_STOCK WHERE PROD_ID_EQTY_LINK_INVST BETWEEN 4001 AND 7000
    UNION ALL
    SELECT 
        'TB_PROD_ALT_ID (新增)', 
        COUNT(*),
        CASE 
            WHEN COUNT(*) >= 10000 THEN '✅ 达标'
            WHEN COUNT(*) >= 5000 THEN '⚠️ 部分达标'
            ELSE '❌ 未达标'
        END
    FROM TB_PROD_ALT_ID WHERE PROD_ID BETWEEN 4001 AND 7000;
    " 2>/dev/null
    
    echo "========================================"
}

# 显示产品类型分布
show_product_type_distribution() {
    log_info "显示新增产品类型分布..."
    echo "========================================"
    echo "📊 新增产品类型分布 (ID: 4001-7000)"
    echo "========================================"
    
    docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 --default-character-set=utf8mb4 -e "
    USE nl2sql;
    SELECT 
        PROD_TYPE_CDE AS 产品类型,
        COUNT(*) AS 产品数量,
        ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM TB_PROD WHERE PROD_ID BETWEEN 4001 AND 7000), 2) AS 百分比
    FROM TB_PROD 
    WHERE PROD_ID BETWEEN 4001 AND 7000
    GROUP BY PROD_TYPE_CDE
    ORDER BY COUNT(*) DESC;
    " 2>/dev/null
    
    echo "========================================"
}

# 显示样本数据
show_sample_data() {
    log_info "显示新增数据样本..."
    echo "========================================"
    echo "📋 新增产品数据样本 (前5条)"
    echo "========================================"
    
    docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 --default-character-set=utf8mb4 -e "
    USE nl2sql;
    SELECT 
        PROD_ID,
        PROD_CDE,
        PROD_TYPE_CDE,
        LEFT(PROD_NAME, 30) AS PROD_NAME,
        CCY_PROD_CDE,
        RISK_LVL_CDE,
        ROUND(PROD_MKT_PRC_AMT, 2) AS MARKET_PRICE
    FROM TB_PROD 
    WHERE PROD_ID BETWEEN 4001 AND 7000
    ORDER BY PROD_ID 
    LIMIT 5;
    " 2>/dev/null
    
    echo "========================================"
}

# 主函数
main() {
    echo "========================================"
    echo "🚀 Cognos增量测试数据生成"
    echo "========================================"
    
    local total_start_time=$(date +%s)
    
    # 检查前置条件
    check_mysql_container
    check_database_connection
    
    # 显示当前数据统计
    show_current_data_statistics
    
    # 询问是否继续
    echo ""
    log_warning "即将生成增量测试数据，这将："
    echo "  • 在现有数据基础上增加约3000条新产品记录 (ID: 4001-7000)"
    echo "  • 生成约9000条产品扩展字段记录"
    echo "  • 生成约250条债券工具记录"
    echo "  • 生成约250条股票挂钩投资记录"
    echo "  • 生成约500条标的股票记录"
    echo "  • 生成约12000条产品替代ID记录"
    echo ""
    echo "  总计: 约25000+条新记录"
    echo "  预计耗时: 3-8分钟"
    echo ""
    
    read -p "是否继续执行？(y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        log_info "用户取消操作"
        exit 0
    fi
    
    echo ""
    log_info "开始生成增量测试数据..."
    
    # 执行增量数据生成脚本
    if execute_sql_file "06_generate_incremental_test_data.sql" "生成增量产品和扩展字段数据"; then
        if execute_sql_file "07_generate_incremental_related_data.sql" "生成增量关联表数据"; then
            local total_end_time=$(date +%s)
            local total_duration=$((total_end_time - total_start_time))
            
            echo ""
            log_success "🎉 所有增量数据生成完成！"
            log_info "总耗时: ${total_duration}秒"
            
            # 显示统计信息
            echo ""
            show_incremental_data_statistics
            show_product_type_distribution
            show_sample_data
            
            echo ""
            log_success "✅ Cognos增量测试数据生成成功完成！"
            echo "========================================"
            echo "📝 后续操作建议:"
            echo "  • 验证数据完整性: 检查外键关系和业务逻辑"
            echo "  • 测试查询性能: 运行复杂查询测试"
            echo "  • 备份数据库: mysqldump -u root -proot123 nl2sql > backup_with_incremental.sql"
            echo "  • 更新应用程序: 确保应用程序能正确处理新数据"
            echo "========================================"
        else
            log_error "增量关联表数据生成失败"
            exit 1
        fi
    else
        log_error "增量基础数据生成失败"
        exit 1
    fi
}

# 执行主函数
main "$@"
