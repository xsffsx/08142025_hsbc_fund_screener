#!/usr/bin/env python3
"""
ETL Script: Load U43051 Sample Data into hsbc_fund_unified
从 8 个 JSON 文件中提取数据，映射到 PostgreSQL 表结构并插入
"""

import json
import os
import sys
from datetime import datetime
from decimal import Decimal
import psycopg2
from psycopg2.extras import Json

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
PRODUCT_CODE = "U43051"

JSON_FILES = {
    'amh_ut_product': f'{OUTPUT_DIR}/001_{PRODUCT_CODE}.done_amh_ut_product_response_body.json',
    'wmds_advanceChart': f'{OUTPUT_DIR}/001_{PRODUCT_CODE}.done_wmds_advanceChart_response_body.json',
    'wmds_fundQuoteSummary': f'{OUTPUT_DIR}/001_{PRODUCT_CODE}.done_wmds_fundQuoteSummary_response_body.json',
    'wmds_fundSearchCriteria': f'{OUTPUT_DIR}/001_{PRODUCT_CODE}.done_wmds_fundSearchCriteria_response_body.json',
    'wmds_holdingAllocation': f'{OUTPUT_DIR}/001_{PRODUCT_CODE}.done_wmds_holdingAllocation_response_body.json',
    'wmds_otherFundClasses': f'{OUTPUT_DIR}/001_{PRODUCT_CODE}.done_wmds_otherFundClasses_response_body.json',
    'wmds_quoteDetail': f'{OUTPUT_DIR}/001_{PRODUCT_CODE}.done_wmds_quoteDetail_response_body.json',
    'wmds_topTenHoldings': f'{OUTPUT_DIR}/001_{PRODUCT_CODE}.done_wmds_topTenHoldings_response_body.json'
}

def load_json_files():
    """加载所有 JSON 文件"""
    data = {}
    for key, filepath in JSON_FILES.items():
        try:
            with open(filepath, 'r', encoding='utf-8') as f:
                data[key] = json.load(f)
            print(f"✓ 已加载: {key}")
        except FileNotFoundError:
            print(f"✗ 文件未找到: {filepath}")
            sys.exit(1)
        except json.JSONDecodeError as e:
            print(f"✗ JSON 解析错误 {key}: {e}")
            sys.exit(1)
    return data

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

def extract_isin_code(prod_alt_num_segs):
    """从 prodAltNumSegs 中提取 ISIN 代码"""
    if not prod_alt_num_segs:
        return None
    for seg in prod_alt_num_segs:
        if seg.get('prodCdeAltClassCde') == 'I':
            return seg.get('prodAltNum')
    return None

def map_data_to_record(data):
    """将 JSON 数据映射到数据库记录"""
    
    # 提取各个数据源
    amh = data['amh_ut_product']
    quote_detail = data['wmds_quoteDetail']
    fund_summary = data['wmds_fundQuoteSummary']
    advance_chart = data['wmds_advanceChart']
    holding_allocation = data['wmds_holdingAllocation']
    top_ten_holdings = data['wmds_topTenHoldings']
    other_fund_classes = data['wmds_otherFundClasses']
    search_criteria = data['wmds_fundSearchCriteria']
    
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
        'tradable_ccy': attribute_map.get('ccyProdTradeCde', []),
        
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
            'files': list(JSON_FILES.keys()),
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
        print(f"✓ 已插入/更新记录: {record['product_code']}")

def main():
    """主函数"""
    print("=== HSBC Fund ETL: U43051 样本数据加载 ===")
    
    # 切换到脚本目录
    script_dir = os.path.dirname(os.path.abspath(__file__))
    os.chdir(script_dir)
    
    # 加载 JSON 数据
    print("\n1. 加载 JSON 文件...")
    data = load_json_files()
    
    # 映射数据
    print("\n2. 映射数据到表结构...")
    record = map_data_to_record(data)
    print(f"✓ 产品代码: {record['product_code']}")
    print(f"✓ 基金名称: {record['name']}")
    print(f"✓ 风险等级: {record['risk_level']}")
    print(f"✓ NAV: {record['nav']}")
    
    # 连接数据库并插入
    print("\n3. 连接数据库并插入记录...")
    try:
        with psycopg2.connect(**DB_CONFIG) as conn:
            insert_record(conn, record)
            print("✓ 数据插入成功")
    except psycopg2.Error as e:
        print(f"✗ 数据库错误: {e}")
        sys.exit(1)
    
    print("\n=== ETL 完成 ===")

if __name__ == '__main__':
    main()
