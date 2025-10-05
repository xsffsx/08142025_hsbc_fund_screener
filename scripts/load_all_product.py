#!/usr/bin/env python3
"""
ETL Script: Load All Products into hsbc_fund_unified
从所有产品的 8 个 JSON 文件中提取数据，批量映射到 PostgreSQL 表结构并插入
"""

import json
import os
import sys
import re
import glob
from datetime import datetime
from decimal import Decimal
from concurrent.futures import ThreadPoolExecutor, as_completed
import psycopg2
from psycopg2.extras import Json
from psycopg2.pool import ThreadedConnectionPool

# 数据库连接配置
DB_CONFIG = {
    'host': 'localhost',
    'port': 5433,
    'database': 'hsbc_fund',
    'user': 'hsbc_user',
    'password': 'hsbc_pass'
}

# JSON 文件路径配置
OUTPUT_DIR = "../output"

# 文件类型映射
FILE_TYPES = [
    'amh_ut_product',
    'wmds_advanceChart',
    'wmds_fundQuoteSummary',
    'wmds_fundSearchCriteria',
    'wmds_holdingAllocation',
    'wmds_otherFundClasses',
    'wmds_quoteDetail',
    'wmds_topTenHoldings'
]

def discover_products():
    """发现所有产品代码"""
    script_dir = os.path.dirname(os.path.abspath(__file__))
    output_path = os.path.join(script_dir, OUTPUT_DIR)

    # 查找所有 amh_ut_product 文件来获取产品列表
    pattern = os.path.join(output_path, "*_amh_ut_product_response_body.json")
    files = glob.glob(pattern)

    products = []
    for file in files:
        # 从文件名提取产品代码，格式: 001_U43051.done_amh_ut_product_response_body.json
        match = re.search(r'(\d+)_(U\d+)\.done_amh_ut_product', os.path.basename(file))
        if match:
            seq_num, product_code = match.groups()
            products.append((seq_num, product_code))

    products.sort(key=lambda x: int(x[0]))  # 按序号排序
    print(f"✓ 发现 {len(products)} 个产品")
    return products

def get_json_files_for_product(seq_num, product_code):
    """获取指定产品的所有 JSON 文件路径"""
    files = {}
    for file_type in FILE_TYPES:
        filename = f"{seq_num}_{product_code}.done_{file_type}_response_body.json"
        filepath = os.path.join(OUTPUT_DIR, filename)
        files[file_type] = filepath
    return files

def load_json_files_for_product(seq_num, product_code):
    """加载指定产品的所有 JSON 文件"""
    json_files = get_json_files_for_product(seq_num, product_code)
    data = {}
    missing_files = []

    for key, filepath in json_files.items():
        try:
            with open(filepath, 'r', encoding='utf-8') as f:
                data[key] = json.load(f)
        except FileNotFoundError:
            missing_files.append(filepath)
        except json.JSONDecodeError as e:
            print(f"✗ JSON 解析错误 {product_code} {key}: {e}")
            return None, [f"JSON解析错误: {key}"]

    if missing_files:
        return None, missing_files

    return data, []

def safe_decimal(value, default=None):
    """安全转换为 Decimal"""
    if value is None or value == "":
        return default
    try:
        return Decimal(str(value))
    except:
        return default

def safe_date(value, default=None):
    """安全转换为日期字符串"""
    if value is None or value == "":
        return default
    # 假设输入格式为 YYYY-MM-DD
    try:
        datetime.strptime(str(value), '%Y-%m-%d')
        return str(value)
    except:
        return default

def safe_bool(value, default=None):
    """Y/N 转换为布尔值"""
    if value == "Y":
        return True
    elif value == "N":
        return False
    return default

def safe_int(value, default=None):
    """安全转换为整数"""
    if value is None or value == "":
        return default
    try:
        return int(value)
    except:
        return default

def safe_array(value, default=None):
    """安全转换为数组，处理单个字符串值"""
    if value is None:
        return default
    if isinstance(value, list):
        return value
    if isinstance(value, str):
        return [value]  # 将单个字符串转换为数组
    return default

def extract_isin_code(prod_alt_num_segs):
    """从 prodAltNumSegs 中提取 ISIN 代码"""
    if not prod_alt_num_segs:
        return None
    for seg in prod_alt_num_segs:
        if seg.get('prodCdeAltClassCde') == 'I':
            return seg.get('prodAltNum')
    return None

def map_data_to_record(data, product_code):
    """将 JSON 数据映射到数据库记录"""

    # 提取各个数据源
    amh = data.get('amh_ut_product', {})
    quote_detail = data.get('wmds_quoteDetail', {})
    fund_summary = data.get('wmds_fundQuoteSummary', {})
    advance_chart = data.get('wmds_advanceChart', {})
    holding_allocation = data.get('wmds_holdingAllocation', {})
    top_ten_holdings = data.get('wmds_topTenHoldings', {})
    other_fund_classes = data.get('wmds_otherFundClasses', {})
    search_criteria = data.get('wmds_fundSearchCriteria', {})

    # 获取核心数据结构
    amh_payload = amh.get('payload', [{}])[0] if amh.get('payload') else {}
    attribute_map = amh_payload.get('attributeMap', {})
    price_quote = quote_detail.get('priceQuote', {})
    prod_alt_num_segs = quote_detail.get('prodAltNumSegs', [])
    summary = fund_summary.get('summary', {})
    profile = summary.get('profile', {})
    holding_details = summary.get('holdingDetails', {})
    to_new_investors = summary.get('toNewInvestors', {})
    fees_and_expenses = summary.get('feesAndExpenses', {})
    morningstar_ratings = summary.get('morningstar_ratings', {})
    mgmt_contact = summary.get('mgmtAndContactInfo', {})
    investment_strategy = summary.get('investmentStrategy', {})
    yield_and_credit = summary.get('yieldAndCredit', {})
    rating = summary.get('rating', {})
    risk_data = summary.get('risk', [])
    
    # 构建记录
    record = {
        # 标识与基础信息
        'product_code': amh_payload.get('productAlternativeNumber') or price_quote.get('symbol'),
        'isin_code': extract_isin_code(prod_alt_num_segs),
        'family_code': profile.get('familyCode') or attribute_map.get('fundHouseCde'),
        'family_name': profile.get('familyName') or attribute_map.get('fundHouseName'),
        'name': profile.get('name') or price_quote.get('companyName'),
        'risk_level': safe_int(profile.get('riskLvlCde') or attribute_map.get('riskLvlCde')),
        
        # 分类与币种
        'category_lv1_code': price_quote.get('categoryLevel1Code') or profile.get('categoryLevel1Code'),
        'category_lv1_name': price_quote.get('categoryLevel1Name') or profile.get('categoryLevel1Name'),
        'hsbc_category_code': profile.get('hsbcCategoryCode'),
        'hsbc_category_name': profile.get('hsbcCategoryName'),
        'currency': profile.get('currency') or attribute_map.get('ccyProdCde'),
        'tradable_ccy': safe_array(attribute_map.get('ccyProdTradeCde', [])),
        
        # 市场价格与区间
        'nav': safe_decimal(attribute_map.get('prodNavPrcAmt') or price_quote.get('priceQuote')),
        'nav_date': safe_date(profile.get('dayEndNAVDate') or price_quote.get('bidPriceDate')),
        'week_range_low': safe_decimal(summary.get('weekRangeLow')),
        'week_range_high': safe_decimal(summary.get('weekRangeHigh')),
        'week_range_ccy': summary.get('weekRangeCurrency'),
        
        # 交易许可
        'allow_buy': safe_bool(profile.get('allowBuy') or attribute_map.get('allowBuyProdInd') or price_quote.get('allowBuy')),
        'allow_sell': safe_bool(profile.get('allowSell') or attribute_map.get('allowSellProdInd')),
        'allow_sw_in': safe_bool(profile.get('allowSwInProdInd')),
        'allow_sw_out': safe_bool(profile.get('allowSwOutProdInd') or attribute_map.get('allowSwOutProdInd')),
        'allow_sell_mip': safe_bool(profile.get('allowSellMipProdInd') or attribute_map.get('allowSellMipProdInd')),
        
        # 最小金额与持有约束
        'inv_min_init': safe_decimal(to_new_investors.get('hsbcMinInitInvst')),
        'inv_min_subq': safe_decimal(to_new_investors.get('hsbcMinSubqInvst')),
        'invst_init_min_amt': safe_decimal(attribute_map.get('invstInitMinAmt')),
        'fund_sw_in_min_amt': safe_decimal(attribute_map.get('fundSwInMinAmt')),
        'fund_sw_out_min_amt': safe_decimal(attribute_map.get('fundSwOutMinAmt')),
        'rdm_min_amt': safe_decimal(attribute_map.get('rdmMinAmt')),
        'mip_min_amt': safe_decimal(attribute_map.get('invstMipMinAmt')),
        'mip_incrm_min_amt': safe_decimal(attribute_map.get('invstMipIncrmMinAmt')),
        'retain_min_units': safe_decimal(attribute_map.get('utRtainMinNum')),
        'sw_out_retain_min_units': safe_decimal(attribute_map.get('utSwOutRtainMinNum')),
        
        # 费用与规模
        'expense_ratio': safe_decimal(profile.get('expenseRatio')),
        'ongoing_charge': safe_decimal(fees_and_expenses.get('onGoingChargeFigure')),
        'annual_mgmt_fee': safe_decimal(fees_and_expenses.get('actualManagementFee') or profile.get('annualManagementFee')),
        'assets_under_mgmt': safe_decimal(profile.get('assetsUnderManagement')),
        'aum_ccy': profile.get('assetsUnderManagementCurrencyCode'),
        'shares_outstanding': safe_decimal(holding_details.get('sharesOutstanding')),
        
        # 关键日期
        'next_deal_date': safe_date(profile.get('nextDealDate')),
        'inception_date': safe_date(profile.get('inceptionDate')),
        'launch_date': safe_date(price_quote.get('launchDate')),
        
        # JSONB 原始结构
        'attribute_map': Json(attribute_map),
        'alt_codes': Json(prod_alt_num_segs),
        'summary_calendar_year': Json(summary.get('calendarYearTotalReturns')),
        'summary_cumulative': Json(summary.get('cumulativeTotalReturns')),
        'price_quote': Json(price_quote),
        'holding_details': Json(holding_details),
        'to_new_investors': Json(to_new_investors),
        'fees_and_expenses': Json(fees_and_expenses),
        'morningstar_ratings': Json(morningstar_ratings),
        'mgmt_contact': Json(mgmt_contact),
        'investment_strategy': Json(investment_strategy),
        'yield_and_credit': Json(yield_and_credit),
        'rating': Json(rating),
        'risk_json': Json(risk_data),
        'profile': Json(profile),
        'holding_allocation': Json(holding_allocation.get('holdingAllocation', [])),
        'top10_holdings': Json(top_ten_holdings.get('top10Holdings')),
        'chart_timeseries': Json(advance_chart.get('result', [{}])[0].get('data', []) if advance_chart.get('result') else []),
        'other_fund_classes': Json(other_fund_classes.get('assetClasses')),
        'search_criteria': Json(search_criteria),
        'raw_files': Json({
            'files': FILE_TYPES,
            'product_code': product_code,
            'loaded_at': datetime.now().isoformat()
        })
    }
    
    return record

def insert_record(conn, record):
    """插入记录到数据库"""

    # 构建 INSERT 语句
    columns = list(record.keys())
    placeholders = [f'%({col})s' for col in columns]

    sql = f"""
    INSERT INTO public.hsbc_fund_unified ({', '.join(columns)})
    VALUES ({', '.join(placeholders)})
    ON CONFLICT (product_code) DO UPDATE SET
        updated_at = NOW(),
        {', '.join([f'{col} = EXCLUDED.{col}' for col in columns if col != 'product_code'])}
    """

    with conn.cursor() as cur:
        cur.execute(sql, record)
        conn.commit()

def process_single_product(seq_num, product_code, conn_pool):
    """处理单个产品"""
    try:
        # 加载 JSON 数据
        data, errors = load_json_files_for_product(seq_num, product_code)
        if data is None:
            return product_code, False, f"缺失文件: {len(errors)} 个"

        # 映射数据
        record = map_data_to_record(data, product_code)

        # 插入数据库
        conn = conn_pool.getconn()
        try:
            insert_record(conn, record)
            return product_code, True, None
        finally:
            conn_pool.putconn(conn)

    except Exception as e:
        return product_code, False, str(e)

def main():
    """主函数"""
    print("=== HSBC Fund ETL: 批量加载所有产品 ===")

    # 切换到脚本目录
    script_dir = os.path.dirname(os.path.abspath(__file__))
    os.chdir(script_dir)

    # 发现所有产品
    print("\n1. 发现产品...")
    products = discover_products()
    if not products:
        print("✗ 未发现任何产品")
        sys.exit(1)

    print(f"✓ 发现 {len(products)} 个产品")

    # 创建连接池
    print("\n2. 创建数据库连接池...")
    try:
        conn_pool = ThreadedConnectionPool(
            minconn=1,
            maxconn=10,
            **DB_CONFIG
        )
        print("✓ 连接池创建成功")
    except psycopg2.Error as e:
        print(f"✗ 数据库连接错误: {e}")
        sys.exit(1)

    # 批量处理产品
    print(f"\n3. 批量处理 {len(products)} 个产品...")
    success_count = 0
    error_count = 0
    errors = []

    # 使用线程池并发处理
    with ThreadPoolExecutor(max_workers=8) as executor:
        # 提交所有任务
        future_to_product = {
            executor.submit(process_single_product, seq_num, product_code, conn_pool): (seq_num, product_code)
            for seq_num, product_code in products
        }

        # 处理结果
        for i, future in enumerate(as_completed(future_to_product), 1):
            _, product_code = future_to_product[future]
            _, success, error_msg = future.result()

            if success:
                success_count += 1
                if i % 50 == 0 or i <= 10:  # 每50个或前10个显示进度
                    print(f"  [{i:4d}/{len(products)}] ✓ {product_code}")
            else:
                error_count += 1
                error_info = f"{product_code}: {error_msg}"
                errors.append(error_info)
                print(f"  [{i:4d}/{len(products)}] ✗ {error_info}")

    # 关闭连接池
    conn_pool.closeall()

    # 输出统计结果
    print(f"\n=== ETL 完成 ===")
    print(f"✓ 成功处理: {success_count} 个产品")
    print(f"✗ 处理失败: {error_count} 个产品")

    if errors:
        print(f"\n失败详情:")
        for error in errors[:10]:  # 只显示前10个错误
            print(f"  - {error}")
        if len(errors) > 10:
            print(f"  ... 还有 {len(errors) - 10} 个错误")

    print(f"\n总计: {len(products)} 个产品")

if __name__ == '__main__':
    main()
