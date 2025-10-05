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
    """提取一个有完整数据的产品作为样本"""
    products = api_data.get("responseData", {}).get("products", [])
    if not products:
        return {}

    # 优先选择有 distributionFrequency 数据的产品作为样本
    for product in products:
        profile = product.get("profile", {})
        if profile.get("distributionFrequency") is not None:
            return product

    # 如果没有找到有 distributionFrequency 的产品，返回第一个产品
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

    # ISIN code (国际证券识别编码)
    # 从 prodAltNumSeg 数组中查找 prodCdeAltClassCde = "I" 的项目
    isin_code = ""
    prod_alt_num_seg = sample_product.get("header", {}).get("prodAltNumSeg", [])
    for seg_item in prod_alt_num_seg:
        if seg_item.get("prodCdeAltClassCde") == "I":
            isin_code = seg_item.get("prodAltNum", "")
            break

    field_mapping["ISIN code"] = {
        "frontend_name": "ISIN code",
        "frontend_description": "国际证券识别编码",
        "api_path": "header.prodAltNumSeg[prodCdeAltClassCde=I].prodAltNum",
        "api_value": isin_code,
        "data_type": "string",
        "example": "LU0055631609"
    }
    
    # Fund house (基金管理公司)
    field_mapping["Fund house"] = {
        "frontend_name": "Fund house",
        "frontend_description": "基金管理公司",
        "api_path": "header.familyName",
        "api_value": sample_product.get("header", {}).get("familyName", ""),
        "data_type": "string",
        "example": "BlackRock"
    }

    # Fund class inception date (基金份额成立日期)
    field_mapping["Fund class inception date"] = {
        "frontend_name": "Fund class inception date",
        "frontend_description": "基金份额成立日期",
        "api_path": "profile.inceptionDate",
        "api_value": sample_product.get("profile", {}).get("inceptionDate", ""),
        "data_type": "date",
        "example": "30 Dec 1994"
    }

    # HSBC investment category (汇丰投资类别)
    field_mapping["HSBC investment category"] = {
        "frontend_name": "HSBC investment category",
        "frontend_description": "汇丰投资类别",
        "api_path": "header.categoryName",
        "api_value": sample_product.get("header", {}).get("categoryName", ""),
        "data_type": "string",
        "example": "Commodity Funds"
    }
    
    # Target dividend distribution frequency (目标分红频率)
    field_mapping["Target dividend distribution frequency"] = {
        "frontend_name": "Target dividend distribution frequency",
        "frontend_description": "目标分红频率",
        "api_path": "profile.distributionFrequency",
        "api_value": sample_product.get("profile", {}).get("distributionFrequency", ""),
        "data_type": "string",
        "example": "Monthly"
    }

    # Dividend yield (股息收益率)
    field_mapping["Dividend yield"] = {
        "frontend_name": "Dividend yield",
        "frontend_description": "股息收益率",
        "api_path": "profile.distributionYield",
        "api_value": sample_product.get("profile", {}).get("distributionYield", 0),
        "data_type": "percentage",
        "example": "0.00%"
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

    # 构建文件链接 - 使用相对路径，动态获取当前目录名
    current_dir_name = SCRIPT_DIR.name  # 获取当前脚本所在目录名（如 02_fund_profile）
    screenshot_link = f"{current_dir_name}/result/{screenshot_files[0].name}" if screenshot_files else f"{current_dir_name}/result/fund_screener_result_*.png"
    page_link = f"{current_dir_name}/result/{page_files[0].name}" if page_files else f"{current_dir_name}/result/fund_screener_page_*.html"
    page_info_link = f"{current_dir_name}/result/{page_info_files[0].name}" if page_info_files else f"{current_dir_name}/result/page_info_*.json"
    
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
            'ISIN code': '国际证券识别编码，全球唯一的基金标识符',
            'Fund house': '基金管理公司名称',
            'Fund class inception date': '基金份额类别的成立日期',
            'HSBC investment category': '汇丰银行的投资类别分类',
            'Target dividend distribution frequency': '目标分红频率',
            'Dividend yield': '股息收益率，年化分红与净值的比率'
        }

        field_remarks = {
            'Fund': '基金名称是用户识别产品的主要标识，通常包含基金公司、投资策略和份额类别信息',
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
