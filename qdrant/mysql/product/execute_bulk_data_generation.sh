#!/bin/bash

# =====================================================
# Cognos大量测试数据生成执行脚本
# 按顺序执行所有数据生成SQL脚本
# 创建时间: 2025-01-04 14:50:00
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

# 检查表结构是否存在
check_table_structure() {
    log_info "检查Cognos表结构..."
    if ! docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "USE nl2sql; SHOW TABLES LIKE 'TB_PROD';" | grep -q TB_PROD; then
        log_warning "Cognos表结构不存在，正在创建..."
        execute_sql_file "01_create_cognos_tables_mysql.sql" "创建表结构"
    else
        log_success "Cognos表结构已存在"
    fi
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
    if docker exec -i mysql-nl2sql-mvp1 mysql -u root -proot123 nl2sql < "$sql_file"; then
        local end_time=$(date +%s)
        local duration=$((end_time - start_time))
        log_success "$description 完成 (耗时: ${duration}秒)"
        return 0
    else
        log_error "$description 失败"
        return 1
    fi
}

# 显示数据统计
show_data_statistics() {
    log_info "显示数据统计..."
    echo "========================================"
    echo "📊 Cognos数据库统计信息"
    echo "========================================"
    
    docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
    USE nl2sql;
    SELECT 
        'TB_PROD' AS table_name, 
        COUNT(*) AS record_count,
        CASE 
            WHEN COUNT(*) >= 3000 THEN '✅ 达标'
            WHEN COUNT(*) >= 1000 THEN '⚠️ 部分达标'
            ELSE '❌ 未达标'
        END AS status
    FROM TB_PROD
    UNION ALL
    SELECT 
        'CDE_DESC_VALUE', 
        COUNT(*),
        CASE 
            WHEN COUNT(*) >= 100 THEN '✅ 达标'
            WHEN COUNT(*) >= 50 THEN '⚠️ 部分达标'
            ELSE '❌ 未达标'
        END
    FROM CDE_DESC_VALUE
    UNION ALL
    SELECT 
        'TB_PROD_USER_DEFIN_EXT_FIELD', 
        COUNT(*),
        CASE 
            WHEN COUNT(*) >= 5000 THEN '✅ 达标'
            WHEN COUNT(*) >= 2000 THEN '⚠️ 部分达标'
            ELSE '❌ 未达标'
        END
    FROM TB_PROD_USER_DEFIN_EXT_FIELD
    UNION ALL
    SELECT 
        'TB_DEBT_INSTM', 
        COUNT(*),
        CASE 
            WHEN COUNT(*) >= 500 THEN '✅ 达标'
            WHEN COUNT(*) >= 200 THEN '⚠️ 部分达标'
            ELSE '❌ 未达标'
        END
    FROM TB_DEBT_INSTM
    UNION ALL
    SELECT 
        'TB_EQTY_LINK_INVST', 
        COUNT(*),
        CASE 
            WHEN COUNT(*) >= 100 THEN '✅ 达标'
            WHEN COUNT(*) >= 50 THEN '⚠️ 部分达标'
            ELSE '❌ 未达标'
        END
    FROM TB_EQTY_LINK_INVST
    UNION ALL
    SELECT 
        'TB_EQTY_LINK_INVST_UNDL_STOCK', 
        COUNT(*),
        CASE 
            WHEN COUNT(*) >= 200 THEN '✅ 达标'
            WHEN COUNT(*) >= 100 THEN '⚠️ 部分达标'
            ELSE '❌ 未达标'
        END
    FROM TB_EQTY_LINK_INVST_UNDL_STOCK
    UNION ALL
    SELECT 
        'TB_PROD_ALT_ID', 
        COUNT(*),
        CASE 
            WHEN COUNT(*) >= 8000 THEN '✅ 达标'
            WHEN COUNT(*) >= 4000 THEN '⚠️ 部分达标'
            ELSE '❌ 未达标'
        END
    FROM TB_PROD_ALT_ID
    UNION ALL
    SELECT 
        'PROD_FORM_REQMT', 
        COUNT(*),
        CASE 
            WHEN COUNT(*) >= 4000 THEN '✅ 达标'
            WHEN COUNT(*) >= 2000 THEN '⚠️ 部分达标'
            ELSE '❌ 未达标'
        END
    FROM PROD_FORM_REQMT
    UNION ALL
    SELECT 
        'PROD_RESTR_CUST_CTRY', 
        COUNT(*),
        CASE 
            WHEN COUNT(*) >= 1000 THEN '✅ 达标'
            WHEN COUNT(*) >= 500 THEN '⚠️ 部分达标'
            ELSE '❌ 未达标'
        END
    FROM PROD_RESTR_CUST_CTRY
    UNION ALL
    SELECT 
        'PROD_OVRID_FIELD', 
        COUNT(*),
        CASE 
            WHEN COUNT(*) >= 500 THEN '✅ 达标'
            WHEN COUNT(*) >= 200 THEN '⚠️ 部分达标'
            ELSE '❌ 未达标'
        END
    FROM PROD_OVRID_FIELD;
    " 2>/dev/null
    
    echo "========================================"
}

# 显示样本数据
show_sample_data() {
    log_info "显示样本数据..."
    echo "========================================"
    echo "📋 产品数据样本 (前10条)"
    echo "========================================"
    
    docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
    USE nl2sql;
    SELECT 
        PROD_ID,
        PROD_CDE,
        PROD_TYPE_CDE,
        PROD_STAT_CDE,
        CCY_PROD_CDE,
        RISK_LVL_CDE,
        ROUND(PROD_MKT_PRC_AMT, 2) AS MARKET_PRICE
    FROM TB_PROD 
    WHERE PROD_ID >= 1000
    ORDER BY PROD_ID 
    LIMIT 10;
    " 2>/dev/null
    
    echo "========================================"
    echo "📊 产品类型分布"
    echo "========================================"
    
    docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
    USE nl2sql;
    SELECT 
        PROD_TYPE_CDE,
        COUNT(*) AS PRODUCT_COUNT,
        ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM TB_PROD WHERE PROD_ID >= 1000), 2) AS PERCENTAGE
    FROM TB_PROD 
    WHERE PROD_ID >= 1000
    GROUP BY PROD_TYPE_CDE
    ORDER BY PRODUCT_COUNT DESC;
    " 2>/dev/null
    
    echo "========================================"
}

# 主函数
main() {
    echo "========================================"
    echo "🚀 Cognos大量测试数据生成"
    echo "========================================"
    
    local total_start_time=$(date +%s)
    
    # 检查前置条件
    check_mysql_container
    check_database_connection
    check_table_structure
    
    # 询问是否继续
    echo ""
    log_warning "即将生成大量测试数据，这将："
    echo "  • 清理现有测试数据 (PROD_ID >= 1000)"
    echo "  • 生成约3000条产品记录"
    echo "  • 生成约8000条扩展字段记录"
    echo "  • 生成约800条债券记录"
    echo "  • 生成约200条股票挂钩投资记录"
    echo "  • 生成约12000条产品替代ID记录"
    echo "  • 生成约6000条表单要求记录"
    echo "  • 生成约1500条地域限制记录"
    echo "  • 生成约1000条字段覆盖记录"
    echo ""
    echo "  总计: 约32000+条记录"
    echo "  预计耗时: 5-15分钟"
    echo ""
    
    read -p "是否继续执行？(y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        log_info "用户取消操作"
        exit 0
    fi
    
    echo ""
    log_info "开始生成大量测试数据..."
    
    # 执行数据生成脚本
    if execute_sql_file "03_generate_bulk_test_data.sql" "生成代码描述和产品数据"; then
        if execute_sql_file "04_generate_related_tables_data.sql" "生成关联表数据"; then
            if execute_sql_file "05_generate_auxiliary_tables_data.sql" "生成辅助表数据"; then
                local total_end_time=$(date +%s)
                local total_duration=$((total_end_time - total_start_time))
                
                echo ""
                log_success "🎉 所有数据生成完成！"
                log_info "总耗时: ${total_duration}秒"
                
                # 显示统计信息
                echo ""
                show_data_statistics
                show_sample_data
                
                echo ""
                log_success "✅ Cognos大量测试数据生成成功完成！"
                echo "========================================"
                echo "📝 后续操作建议:"
                echo "  • 运行验证脚本: ./verify_cognos_data.sh"
                echo "  • 备份数据库: mysqldump -u root -proot123 nl2sql > backup.sql"
                echo "  • 测试查询性能: 运行复杂查询测试"
                echo "========================================"
            else
                log_error "辅助表数据生成失败"
                exit 1
            fi
        else
            log_error "关联表数据生成失败"
            exit 1
        fi
    else
        log_error "基础数据生成失败"
        exit 1
    fi
}

# 执行主函数
main "$@"
