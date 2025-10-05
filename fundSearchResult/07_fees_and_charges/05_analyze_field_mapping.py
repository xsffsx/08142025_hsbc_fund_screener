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

# 基础路径 - 相对于脚本位置 (02_fund_profile目录)
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
    """提取第一个产品的数据作为样本"""
    products = api_data.get("responseData", {}).get("products", [])
    if not products:
        return {}
    
    return products[0]  # 返回第一个产品数据

def map_fields_to_api(frontend_fields: Dict[str, str], sample_product: Dict[str, Any]) -> Dict[str, Dict[str, Any]]:
    """将前端字段映射到API数据字段"""

    field_mapping = {}

    # 基金名称
    fund_name = sample_product.get("header", {}).get("name", "")
    field_mapping["Fund"] = {
        "frontend_name": "Fund",
        "frontend_description": "基金名称",
        "api_path": "header.name",
        "api_value": fund_name,
        "data_type": "string",
        "example": fund_name if fund_name else "N/A"
    }

    # HSBC initial charge (HSBC初始费用)
    initial_charge = sample_product.get("profile", {}).get("initialCharge", None)
    field_mapping["HSBC initial charge"] = {
        "frontend_name": "HSBC initial charge",
        "frontend_description": "HSBC初始费用/认购费",
        "api_path": "profile.initialCharge",
        "api_value": initial_charge,
        "data_type": "percentage",
        "example": f"{initial_charge:.2f}%" if initial_charge is not None else "N/A"
    }

    # Annual management fee (maximum) (年度管理费最高)
    annual_mgmt_fee = sample_product.get("profile", {}).get("annualManagementFee", None)
    field_mapping["Annual management fee (maximum)"] = {
        "frontend_name": "Annual management fee (maximum)",
        "frontend_description": "年度管理费(最高)",
        "api_path": "profile.annualManagementFee",
        "api_value": annual_mgmt_fee,
        "data_type": "percentage",
        "example": f"{annual_mgmt_fee:.2f}%" if annual_mgmt_fee is not None else "N/A"
    }

    # HSBC minimum investment amount (HSBC最低投资金额)
    min_investment = sample_product.get("summary", {}).get("switchInMinAmount", None)
    min_investment_currency = sample_product.get("summary", {}).get("switchInMinAmountCurrencyCode", "")
    field_mapping["HSBC minimum investment amount"] = {
        "frontend_name": "HSBC minimum investment amount",
        "frontend_description": "HSBC最低投资金额",
        "api_path": "summary.switchInMinAmount",
        "api_value": min_investment,
        "data_type": "currency",
        "currency_code": min_investment_currency,
        "example": f"{min_investment_currency} {min_investment:,.0f}" if min_investment is not None else "N/A"
    }

    # Expense ratio (费用比率)
    expense_ratio = sample_product.get("profile", {}).get("expenseRatio", None)
    field_mapping["Expense ratio"] = {
        "frontend_name": "Expense ratio",
        "frontend_description": "费用比率/总费用率",
        "api_path": "profile.expenseRatio",
        "api_value": expense_ratio,
        "data_type": "percentage",
        "example": f"{expense_ratio:.2f}%" if expense_ratio is not None else "N/A"
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

    # 查找字段数据文件 - 使用最新的文件
    mapping_dir = SCRIPT_DIR / "mapping"
    frontend_json_files = list(mapping_dir.glob("frontend_fields_*.json"))
    frontend_csv_files = list(mapping_dir.glob("frontend_fields_*.csv"))

    # 获取最新的字段数据文件
    latest_json_file = max(frontend_json_files, key=lambda x: x.stat().st_mtime) if frontend_json_files else None
    latest_csv_file = max(frontend_csv_files, key=lambda x: x.stat().st_mtime) if frontend_csv_files else None

    # 查找字段数据文件 - 使用最新的文件
    mapping_dir = SCRIPT_DIR / "mapping"
    frontend_json_files = list(mapping_dir.glob("frontend_fields_*.json"))
    frontend_csv_files = list(mapping_dir.glob("frontend_fields_*.csv"))
    
    # 获取最新的字段数据文件
    latest_json_file = max(frontend_json_files, key=lambda x: x.stat().st_mtime) if frontend_json_files else None
    latest_csv_file = max(frontend_csv_files, key=lambda x: x.stat().st_mtime) if frontend_csv_files else None

    # 构建文件链接 - 使用相对路径，动态获取当前目录名
    current_dir_name = SCRIPT_DIR.name  # 获取当前脚本所在目录名（如 02_fund_profile）
    screenshot_link = f"{current_dir_name}/result/{screenshot_files[0].name}" if screenshot_files else f"{current_dir_name}/result/fund_screener_result_*.png"
    page_link = f"{current_dir_name}/result/{page_files[0].name}" if page_files else f"{current_dir_name}/result/fund_screener_page_*.html"
    page_info_link = f"{current_dir_name}/result/{page_info_files[0].name}" if page_info_files else f"{current_dir_name}/result/page_info_*.json"
    
    # 字段数据文件链接
    json_link = f"{current_dir_name}/mapping/{latest_json_file.name}" if latest_json_file else f"{current_dir_name}/mapping/frontend_fields_*.json"
    csv_link = f"{current_dir_name}/mapping/{latest_csv_file.name}" if latest_csv_file else f"{current_dir_name}/mapping/frontend_fields_*.csv"

    # 字段数据文件链接
    json_link = f"{current_dir_name}/mapping/{latest_json_file.name}" if latest_json_file else f"{current_dir_name}/mapping/frontend_fields_*.json"
    csv_link = f"{current_dir_name}/mapping/{latest_csv_file.name}" if latest_csv_file else f"{current_dir_name}/mapping/frontend_fields_*.csv"

    report = f"""# HSBC Fund Screener 前端字段与API数据映射分析报告

生成时间: {timestamp}

{overview_section}

## 相关文件链接

### API数据文件
- 📤 [API请求数据]({current_dir_name}/result/fundSearchResult_request.json) - 完整的API请求参数
- 📥 [API响应数据]({current_dir_name}/result/fundSearchResult_response.json) - 完整的API响应数据
- 🔍 [纯净响应数据]({current_dir_name}/result/fundSearchResult_data.json) - 提取的核心响应数据

### 页面截图和源码
- 📸 [页面截图]({screenshot_link}) - 基金 Fund Profile 页面截图
- 📄 [页面源码]({page_link}) - 完整的HTML源码
- 📋 [页面信息]({page_info_link}) - 页面元数据信息

### 提取的字段数据
- 📊 [前端字段数据 (JSON)]({json_link}) - 提取的所有产品字段数据
- 📈 [前端字段数据 (CSV)]({csv_link}) - CSV格式的表格数据

## 前端界面截图

下图展示了HSBC基金 Fund Profile 页面的前端界面，包含了我们需要映射的所有字段：

![HSBC Fund Profile Interface]({screenshot_link})

*图：HSBC基金 Fund Profile 界面 - 显示了基金详情的各个字段*

## 字段映射详情

"""

    for i, (field_key, mapping) in enumerate(field_mapping.items(), 1):
        # 生成详细的字段说明
        field_descriptions = {
            'Fund': '基金完整名称，包含基金公司和份额类别',
            'HSBC initial charge': 'HSBC初始费用/认购费，投资者购买基金时需要支付的一次性费用',
            'Annual management fee (maximum)': '年度管理费(最高)，基金管理公司收取的年度管理费用上限',
            'HSBC minimum investment amount': 'HSBC最低投资金额，通过HSBC投资该基金的最低金额要求',
            'Expense ratio': '费用比率/总费用率，基金运营的总费用占基金资产净值的比例'
        }

        field_remarks = {
            'Fund': '基金名称是用户识别产品的主要标识，通常包含基金公司、投资策略和份额类别信息',
            'HSBC initial charge': '初始费用直接影响投资成本，较低的初始费用有利于提高投资收益率，部分基金可能提供费用优惠',
            'Annual management fee (maximum)': '管理费是基金的主要运营成本，较低的管理费有助于提高长期投资回报，投资者应关注费用与基金表现的性价比',
            'HSBC minimum investment amount': '最低投资金额决定了投资门槛，不同币种和地区可能有不同的最低投资要求，影响投资者的准入条件',
            'Expense ratio': '费用比率是衡量基金成本效率的重要指标，包含管理费、托管费、审计费等各项费用，较低的费用比率通常有利于长期投资表现'
        }

        # 确定数据类型说明
        api_data_type = mapping['data_type']
        frontend_data_type = 'formatted number + currency' if 'currency_path' in mapping else (
            'percentage' if api_data_type == 'percentage' else
            'formatted number' if 'calculation' in mapping else
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

| 前端字段 | API数据路径 | 数据类型 | 示例值 | 字段类型 |
|---------|------------|----------|--------|----------|
"""

    for field_key, mapping in field_mapping.items():
        api_path = mapping['api_path']
        data_type = mapping['data_type']
        example = mapping['example']

        # 检查是否为计算字段
        if mapping.get('is_calculated', False):
            field_type = "🔴 **计算字段 (Calculated)**"
            api_path_display = f"`{api_path}` <br/> 📋 **计算公式**: {mapping.get('calculation_formula', 'N/A')}"
        else:
            field_type = "🟢 **API映射字段**"
            api_path_display = f"`{api_path}`"

        report += f"| **{mapping['frontend_name']}** | {api_path_display} | {data_type} | {example} | {field_type} |\n"

    # 添加API路径详细说明
    report += """
## 📋 API路径详细说明

### 🟢 API映射字段
这些字段直接从API响应数据中获取，无需额外计算：

"""

    # 添加API映射字段的详细说明
    for field_key, mapping in field_mapping.items():
        if not mapping.get('is_calculated', False):
            api_path = mapping['api_path']
            description = mapping['frontend_description']
            report += f"- **{mapping['frontend_name']}**: `{api_path}`\n"
            report += f"  - 📝 **说明**: {description}\n"
            report += f"  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取\n\n"

    report += """### 🔴 计算字段 (Calculated Fields)
这些字段不直接存在于API响应中，需要通过计算得出：

"""

    # 添加计算字段的详细说明
    for field_key, mapping in field_mapping.items():
        if mapping.get('is_calculated', False):
            api_path = mapping['api_path']
            formula = mapping.get('calculation_formula', 'N/A')
            dependencies = mapping.get('calculation_dependencies', [])
            description = mapping['frontend_description']

            report += f"- **{mapping['frontend_name']}**: `{api_path}`\n"
            report += f"  - 📝 **说明**: {description}\n"
            report += f"  - 🧮 **计算公式**: `{formula}`\n"
            report += f"  - 📊 **依赖字段**: {', '.join(dependencies)}\n"
            report += f"  - 🔍 **计算逻辑**: 从API获取依赖字段的值，然后按公式计算得出最终结果\n\n"

    # 动态生成前端字段与API数据对应关系图
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

### 评级字段
- Morningstar rating 在API中以字符串形式存储（如 "3", "4", "5"），前端可能显示为星级图标

### 四分位排名字段
- 四分位排名在API中以数字形式存储（1, 2, 3, 4），前端需要转换为序数格式：
  - 1 → "1st"
  - 2 → "2nd"
  - 3 → "3rd"
  - 4 → "4th"

### 信用质量字段
- Average credit quality 在API中可能为null，前端应显示为 "N/A" 或 "-"

## 使用建议

1. 前端渲染时应根据数据类型进行相应的格式化
2. 评级字段可以考虑使用图标或星级显示
3. 四分位排名需要转换为用户友好的序数格式
4. 对于null值字段，应显示合适的占位符（如 "N/A"）
5. 晨星评级可以用星级图标增强视觉效果

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

    # 保存报告到fundSearchResult根目录 - 使用动态目录名
    current_dir_name = SCRIPT_DIR.name.upper()  # 获取当前目录名并转为大写
    report_file = SCRIPT_DIR.parent / f"{current_dir_name}_FIELD_MAPPING_{file_timestamp}.md"
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
            print("\n🔄 更新报告中的文件链接...")
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
