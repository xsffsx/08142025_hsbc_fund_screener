#!/usr/bin/env python3
"""
HSBC Fund Screener Field Mapping Analyzer
分析前端显示字段与 fundSearchResult API 响应数据的对应关系
"""

import json
import re
import shutil
from pathlib import Path
from typing import Dict, List, Any, Tuple
from datetime import datetime

# 基础路径 - 相对于脚本位置 (01_summary目录)
SCRIPT_DIR = Path(__file__).resolve().parent
BASE_DIR = SCRIPT_DIR.parents[1]  # 项目根目录
RESULT_DIR = SCRIPT_DIR / "result"
MAPPING_DIR = SCRIPT_DIR / "mapping"
MAPPING_DIR.mkdir(parents=True, exist_ok=True)

def cleanup_mapping_files():
    """清理 mapping 目录中的旧文件"""
    print("🧹 清理 mapping 目录中的旧文件...")

    if MAPPING_DIR.exists():
        mapping_files = list(MAPPING_DIR.glob('*'))
        cleaned_count = 0

        for file_path in mapping_files:
            if file_path.is_file():
                file_path.unlink()
                cleaned_count += 1

        print(f"✅ 已清理 mapping 目录中的 {cleaned_count} 个文件")
    else:
        print("📁 mapping 目录不存在，无需清理")

def load_api_response() -> Dict[str, Any]:
    """加载 API 响应数据"""
    response_file = RESULT_DIR / "fundSearchResult_response.json"
    with open(response_file, 'r', encoding='utf-8') as f:
        return json.load(f)

def load_api_request() -> Dict[str, Any]:
    """加载 API 请求数据"""
    request_file = RESULT_DIR / "fundSearchResult_request.json"
    with open(request_file, 'r', encoding='utf-8') as f:
        return json.load(f)

def analyze_frontend_fields() -> Dict[str, str]:
    """根据图片分析前端显示的字段"""
    # 根据您提供的图片，前端表格显示的字段
    frontend_fields = {
        "Fund": "基金名称",
        "NAV": "净资产值",
        "YTD return": "年初至今回报",
        "1Y return": "1年回报",
        "Fund class currency": "基金类别货币",
        "1 year sharpe ratio": "1年夏普比率",
        "Fund size (USD/Million)": "基金规模(美元/百万)",
        "HSBC risk level": "汇丰风险等级",
        "Morningstar rating": "晨星评级"
    }
    return frontend_fields

def extract_sample_product_data(api_data: Dict[str, Any]) -> Dict[str, Any]:
    """提取一个有完整数据的产品作为样本"""
    products = api_data.get("responseData", {}).get("products", [])
    if not products:
        return {}

    # 优先选择有完整数据的产品作为样本
    # 检查关键字段：rating.morningstarRating, risk数据等
    for product in products:
        rating = product.get("rating", {})
        risk_data = product.get("risk", [])
        if (rating.get("morningstarRating") is not None and
            len(risk_data) > 0):
            return product

    # 如果没有找到有完整数据的产品，返回第一个产品
    return products[0]

def map_fields_to_api(frontend_fields: Dict[str, str], sample_product: Dict[str, Any]) -> Dict[str, Dict[str, Any]]:
    """将前端字段映射到API数据字段"""
    
    field_mapping = {}
    
    # 基金名称
    field_mapping["Fund"] = {
        "frontend_name": "Fund",
        "frontend_description": "基金名称",
        "api_path": "header.name",
        "api_value": sample_product.get("header", {}).get("name", ""),
        "data_type": "string",
        "example": "BlackRock World Gold Fund (Class A2)"
    }
    
    # NAV (净资产值)
    field_mapping["NAV"] = {
        "frontend_name": "NAV",
        "frontend_description": "净资产值",
        "api_path": "summary.dayEndNAV",
        "api_value": sample_product.get("summary", {}).get("dayEndNAV", 0),
        "currency_path": "summary.dayEndNAVCurrencyCode",
        "currency_value": sample_product.get("summary", {}).get("dayEndNAVCurrencyCode", ""),
        "data_type": "number",
        "example": "71.76 USD"
    }
    
    # YTD return (年初至今回报)
    field_mapping["YTD return"] = {
        "frontend_name": "YTD return",
        "frontend_description": "年初至今回报",
        "api_path": "performance.calendarReturns.returnYTD",
        "api_value": sample_product.get("performance", {}).get("calendarReturns", {}).get("returnYTD", 0),
        "data_type": "percentage",
        "example": "85.33%"
    }
    
    # 1Y return (1年回报)
    field_mapping["1Y return"] = {
        "frontend_name": "1Y return",
        "frontend_description": "1年回报",
        "api_path": "performance.annualizedReturns.return1Yr",
        "api_value": sample_product.get("performance", {}).get("annualizedReturns", {}).get("return1Yr", 0),
        "data_type": "percentage",
        "example": "76.36%"
    }
    
    # Fund class currency (基金类别货币)
    field_mapping["Fund class currency"] = {
        "frontend_name": "Fund class currency",
        "frontend_description": "基金类别货币",
        "api_path": "header.currency",
        "api_value": sample_product.get("header", {}).get("currency", ""),
        "data_type": "string",
        "example": "USD"
    }
    
    # 1 year sharpe ratio (1年夏普比率)
    risk_data = sample_product.get("risk", [])
    sharpe_1yr = None
    for risk_item in risk_data:
        if risk_item.get("yearRisk", {}).get("year") == 1:
            sharpe_1yr = risk_item.get("yearRisk", {}).get("sharpeRatio")
            break
    
    field_mapping["1 year sharpe ratio"] = {
        "frontend_name": "1 year sharpe ratio",
        "frontend_description": "1年夏普比率",
        "api_path": "risk[year=1].yearRisk.sharpeRatio",
        "api_value": sharpe_1yr,
        "data_type": "number",
        "example": "1.635"
    }
    
    # Fund size (基金规模)
    field_mapping["Fund size (USD/Million)"] = {
        "frontend_name": "Fund size (USD/Million)",
        "frontend_description": "基金规模(美元/百万)",
        "api_path": "summary.assetsUnderManagement",
        "api_value": sample_product.get("summary", {}).get("assetsUnderManagement", 0),
        "currency_path": "summary.assetsUnderManagementCurrencyCode",
        "currency_value": sample_product.get("summary", {}).get("assetsUnderManagementCurrencyCode", ""),
        "data_type": "number_millions",
        "example": "6911.59 (Million USD)",
        "calculation": "assetsUnderManagement / 1000000"
    }
    
    # HSBC risk level (汇丰风险等级)
    field_mapping["HSBC risk level"] = {
        "frontend_name": "HSBC risk level",
        "frontend_description": "汇丰风险等级",
        "api_path": "summary.riskLvlCde",
        "api_value": sample_product.get("summary", {}).get("riskLvlCde", ""),
        "data_type": "string",
        "example": "5"
    }
    
    # Morningstar rating (晨星评级)
    field_mapping["Morningstar rating"] = {
        "frontend_name": "Morningstar rating",
        "frontend_description": "晨星评级",
        "api_path": "rating.morningstarRating",
        "api_value": sample_product.get("rating", {}).get("morningstarRating", ""),
        "data_type": "string",
        "example": "3 (stars)"
    }
    
    return field_mapping

def generate_mapping_report(field_mapping: Dict[str, Dict[str, Any]], request_url: str = "", file_timestamp: str = "") -> str:
    """生成字段映射报告"""

    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    # 如果没有提供文件时间戳，使用当前时间戳
    if not file_timestamp:
        file_timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")

    # 构建概述部分，包含request URL
    overview_section = f"""## 概述

本报告分析了HSBC基金筛选器前端显示的字段与 `fundSearchResult` API 响应数据的对应关系。"""

    if request_url:
        overview_section += f"""

**API请求URL**: `{request_url}`

**请求方法**: POST"""

    # 查找实际的截图文件
    screenshot_files = list(RESULT_DIR.glob("fund_screener_result_*.png"))
    page_files = list(RESULT_DIR.glob("fund_screener_page_*.html"))
    page_info_files = list(RESULT_DIR.glob("page_info_*.json"))

    # 构建文件链接
    screenshot_link = f"01_summary/result/{screenshot_files[0].name}" if screenshot_files else "01_summary/result/fund_screener_result_*.png"
    page_link = f"01_summary/result/{page_files[0].name}" if page_files else "01_summary/result/fund_screener_page_*.html"
    page_info_link = f"01_summary/result/{page_info_files[0].name}" if page_info_files else "01_summary/result/page_info_*.json"

    report = f"""# HSBC Fund Screener 前端字段与API数据映射分析报告

生成时间: {timestamp}

{overview_section}

## 相关文件链接

### API数据文件
- 📤 [API请求数据](01_summary/result/fundSearchResult_request.json) - 完整的API请求参数
- 📥 [API响应数据](01_summary/result/fundSearchResult_response.json) - 完整的API响应数据
- 🔍 [纯净响应数据](01_summary/result/fundSearchResult_data.json) - 提取的核心响应数据

### 页面截图和源码
- 📸 [页面截图]({screenshot_link}) - 基金筛选结果页面截图
- 📄 [页面源码]({page_link}) - 完整的HTML源码
- 📋 [页面信息]({page_info_link}) - 页面元数据信息

### 提取的字段数据
- 📊 [前端字段数据 (JSON)](01_summary/mapping/frontend_fields_{file_timestamp}.json) - 提取的所有产品字段数据
- 📈 [前端字段数据 (CSV)](01_summary/mapping/frontend_fields_{file_timestamp}.csv) - CSV格式的表格数据

## 前端界面截图

下图展示了HSBC基金筛选器的前端界面，包含了我们需要映射的所有字段：

![HSBC Fund Screener Interface]({screenshot_link})

*图：HSBC基金筛选器界面 - 显示了基金列表的各个字段*

## 字段映射详情

"""

    for i, (field_key, mapping) in enumerate(field_mapping.items(), 1):
        # 生成详细的字段说明
        field_descriptions = {
            'Fund': '基金完整名称，包含基金公司和份额类别',
            'NAV': '净资产值，显示当日收盘价格',
            'YTD return': '从年初到当前日期的投资回报率',
            '1Y return': '过去12个月的年化投资回报率',
            'Fund class currency': '基金份额的计价货币',
            '1 year sharpe ratio': '1年期夏普比率，衡量风险调整后收益',
            'Fund size (USD/Million)': '基金管理的总资产规模',
            'HSBC risk level': '汇丰银行内部风险评级',
            'Morningstar rating': '晨星公司的基金评级',
            'ISIN code': '国际证券识别编码，全球唯一的基金标识符',
            'Fund house': '基金管理公司名称',
            'Fund class inception date': '基金份额类别的成立日期',
            'HSBC investment category': '汇丰银行的投资类别分类',
            'Target dividend distribution frequency': '目标分红频率',
            'Dividend yield': '股息收益率，年化分红与净值的比率'
        }

        field_remarks = {
            'Fund': '基金名称是用户识别产品的主要标识，通常包含基金公司、投资策略和份额类别信息',
            'NAV': 'NAV是基金每份额的净资产价值，投资者买卖基金的参考价格，通常每日更新',
            'YTD return': 'YTD回报反映基金在当前年度的表现，是投资者评估短期业绩的重要指标',
            '1Y return': '1年回报是评估基金中期表现的关键指标，剔除了短期市场波动的影响',
            'Fund class currency': '基金计价货币影响汇率风险，投资者需要考虑货币兑换成本',
            '1 year sharpe ratio': '夏普比率越高表示单位风险获得的超额回报越多，是重要的风险调整收益指标',
            'Fund size (USD/Million)': '基金规模影响流动性和管理效率，过大或过小都可能影响投资表现',
            'HSBC risk level': '汇丰风险等级帮助投资者了解产品的风险特征，数值越高风险越大',
            'Morningstar rating': '晨星评级是业界权威的基金评价体系，星级越高表示历史表现越好',
            'ISIN code': 'ISIN代码是国际标准的证券识别码，用于全球范围内唯一识别金融工具',
            'Fund house': '基金管理公司的声誉和管理能力是投资者选择基金的重要考虑因素',
            'Fund class inception date': '成立时间影响基金的历史业绩数据可用性，较新的基金可能缺乏长期表现记录',
            'HSBC investment category': '汇丰的分类体系帮助投资者了解基金的投资策略和风险特征',
            'Target dividend distribution frequency': '分红频率影响现金流规划，适合不同收入需求的投资者',
            'Dividend yield': '股息收益率是评估基金收入能力的重要指标，特别适合追求稳定收入的投资者'
        }

        # 确定数据类型说明
        api_data_type = mapping['data_type']
        frontend_data_type = 'formatted number + currency' if 'currency_path' in mapping else (
            'percentage' if api_data_type == 'percentage' else
            'formatted number' if 'calculation' in mapping else
            'date format' if 'date' in mapping.get('frontend_name', '').lower() else
            api_data_type
        )

        # 构建API路径显示
        api_paths = [mapping['api_path']]
        if 'currency_path' in mapping:
            api_paths.append(mapping['currency_path'])
        api_path_display = ', '.join([f"`{path}`" for path in api_paths])

        # 构建示例对比
        if 'currency_path' in mapping:
            api_example = f"`{mapping['api_value']}` + `\"{mapping['currency_value']}\"`"
        else:
            api_example = f"`{mapping['api_value']}`"

        # 构建转换说明
        if mapping['data_type'] == 'percentage':
            conversion_note = '需要将小数转换为百分比格式'
        elif 'currency_path' in mapping:
            conversion_note = '需要组合数值和货币代码'
        elif 'calculation' in mapping:
            conversion_note = f'需要计算转换: {mapping["calculation"]}'
        elif 'date' in mapping.get('frontend_name', '').lower():
            conversion_note = '需要将日期格式化为可读格式'
        else:
            conversion_note = '直接映射，无需转换'

        field_desc = field_descriptions.get(mapping['frontend_name'], mapping['frontend_description'])
        field_remark = field_remarks.get(mapping['frontend_name'], '详细说明待补充')

        report += f"""### {i}. {mapping['frontend_name']} ({mapping['frontend_description']})

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | {mapping['frontend_name']} | {mapping['api_path']} | {field_desc} |
| **数据类型** | {frontend_data_type} | {api_data_type} | {conversion_note} |
| **示例对比** | {mapping['example']} | {api_example} | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | {api_path_display} | 从响应数据中获取字段值的路径 |
| **备注** | {field_remark} | | |"""

        if 'currency_path' in mapping:
            report += f"""
| **货币处理** | 数值 + 货币代码 | 分别从数值字段和货币字段获取 | 前端需要将数值和货币代码组合显示 |"""

        if 'calculation' in mapping:
            report += f"""
| **计算公式** | 显示值 = 原始值 {mapping['calculation'].replace('assetsUnderManagement', '').strip()} | 原始数值 | 需要进行数学运算转换 |"""

        report += "\n\n"

    # 添加字段映射汇总表
    report += """## 字段映射汇总表

| 前端字段 | API数据路径 | 数据类型 | 示例值 |
|---------|------------|----------|--------|
"""

    for field_key, mapping in field_mapping.items():
        api_path = mapping['api_path']
        data_type = mapping['data_type']
        example = mapping['example']
        report += f"| **{mapping['frontend_name']}** | `{api_path}` | {data_type} | {example} |\n"

    # 动态生成前端字段与API数据对应关系图
    report += """
## 前端字段与API数据对应关系图

```
前端表格字段                    API响应数据路径
┌─────────────────────┐       ┌──────────────────────────────────┐"""

    # 动态生成字段映射关系
    for field_key, mapping in field_mapping.items():
        frontend_name = mapping['frontend_name']
        api_path = mapping['api_path']
        
        # 处理长字段名，确保对齐
        frontend_display = f"│ {frontend_name:<19} │"
        
        # 处理长API路径，可能需要分行显示
        if len(api_path) > 32:
            # 分行显示长路径
            lines = []
            current_line = ""
            parts = api_path.split('.')
            
            for part in parts:
                if len(current_line + part) > 30:
                    if current_line:
                        lines.append(current_line.rstrip('.'))
                        current_line = part + "."
                    else:
                        lines.append(part)
                        current_line = ""
                else:
                    current_line += part + "."
            
            if current_line:
                lines.append(current_line.rstrip('.'))
            
            # 输出第一行
            api_display = f"│ {lines[0]:<32} │"
            report += f"
{frontend_display}  ←──  {api_display}"
            
            # 输出后续行
            for line in lines[1:]:
                empty_frontend = "│                     │"
                api_display = f"│ {line:<32} │"
                report += f"
{empty_frontend}       {api_display}"
        else:
            # 单行显示短路径
            api_display = f"│ {api_path:<32} │"
            report += f"
{frontend_display}  ←──  {api_display}"

    report += """
└─────────────────────┘       └──────────────────────────────────┘
```

## 数据转换说明

### 百分比字段
- YTD return 和 1Y return 字段在API中以小数形式存储（如 85.33058），前端显示时需要添加 % 符号

### 货币字段
- NAV 和 Fund size 字段都有对应的货币代码字段
- Fund size 需要从原始值除以1,000,000转换为百万单位

### 评级字段
- Morningstar rating 在API中以字符串形式存储（如 "3"），前端可能显示为星级图标

### 风险数据
- 1年夏普比率需要从 risk 数组中查找 year=1 的数据

## 使用建议

1. 前端渲染时应根据数据类型进行相应的格式化
2. 百分比字段需要添加 % 符号
3. 货币字段需要显示货币代码
4. 大数值字段（如基金规模）需要转换为合适的单位
5. 评级字段可以考虑使用图标或星级显示

"""
    
    return report

def copy_interface_screenshot():
    """复制界面截图到 mapping 目录"""
    # 查找最新的截图文件
    result_dir = BASE_DIR / "fundSearchResult" / "result"
    screenshot_files = list(result_dir.glob("fund_screener_result_*.png"))

    if screenshot_files:
        # 选择最新的截图文件
        latest_screenshot = max(screenshot_files, key=lambda x: x.stat().st_mtime)

        # 复制到 mapping 目录并重命名
        target_file = MAPPING_DIR / "hsbc_fund_screener_interface.png"

        import shutil
        shutil.copy2(latest_screenshot, target_file)
        print(f"📸 界面截图已复制到: {target_file}")
        return True
    else:
        print("⚠️  未找到界面截图文件")
        return False

def main():
    """主函数"""
    print("🔍 开始分析 HSBC Fund Screener 字段映射...")

    # 清理旧的映射文件
    cleanup_mapping_files()

    # 加载API响应数据
    api_data = load_api_response()
    print("✅ API响应数据加载完成")

    # 加载API请求数据
    request_data = load_api_request()
    request_url = request_data.get("url", "")
    print("✅ API请求数据加载完成")

    # 分析前端字段
    frontend_fields = analyze_frontend_fields()
    print("✅ 前端字段分析完成")

    # 提取样本产品数据
    sample_product = extract_sample_product_data(api_data)
    print("✅ 样本产品数据提取完成")

    # 生成字段映射
    field_mapping = map_fields_to_api(frontend_fields, sample_product)
    print("✅ 字段映射生成完成")

    # 复制界面截图
    copy_interface_screenshot()

    # 生成报告
    file_timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')
    report = generate_mapping_report(field_mapping, request_url, file_timestamp)

    # 保存报告到fundSearchResult根目录
    report_file = SCRIPT_DIR.parent / f"01_SUMMARY_FIELD_MAPPING_{file_timestamp}.md"
    with open(report_file, 'w', encoding='utf-8') as f:
        f.write(report)

    # 保存JSON格式的映射数据到 mapping 目录
    json_file = MAPPING_DIR / f"field_mapping_{file_timestamp}.json"
    with open(json_file, 'w', encoding='utf-8') as f:
        json.dump(field_mapping, f, indent=2, ensure_ascii=False)
    
    print(f"📊 字段映射分析报告已保存: {report_file}")
    print(f"📋 字段映射JSON数据已保存: {json_file}")

    # 自动调用06脚本进行字段提取，使用相同的时间戳
    print("\n🔄 自动运行字段提取脚本...")
    try:
        import subprocess
        import sys

        # 设置环境变量传递时间戳
        import os
        os.environ['FIELD_TIMESTAMP'] = file_timestamp

        # 调用06脚本
        result = subprocess.run([sys.executable, "06_field_extractor.py"],
                              cwd=SCRIPT_DIR,
                              capture_output=True,
                              text=True)

        if result.returncode == 0:
            print("✅ 字段提取脚本运行成功")
            print(result.stdout)
            
            # 重新生成报告，更新文件链接
            print("
🔄 更新报告中的文件链接...")
            updated_report = generate_mapping_report(field_mapping, request_url, file_timestamp)
            with open(report_file, 'w', encoding='utf-8') as f:
                f.write(updated_report)
            print("✅ 报告文件链接已更新")
        else:
            print("❌ 字段提取脚本运行失败")
            print(result.stderr)

    except Exception as e:
        print(f"⚠️  自动调用字段提取脚本失败: {e}")
        print("请手动运行: python 06_field_extractor.py")

    print("\n🎯 分析完成！")

if __name__ == "__main__":
    main()
