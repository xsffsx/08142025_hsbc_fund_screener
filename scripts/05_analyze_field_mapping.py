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

# 基础路径
BASE_DIR = Path(__file__).resolve().parents[1]
RESULT_DIR = BASE_DIR / "fundSearchResult" / "result"
MAPPING_DIR = BASE_DIR / "fundSearchResult" / "mapping"
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

def generate_mapping_report(field_mapping: Dict[str, Dict[str, Any]]) -> str:
    """生成字段映射报告"""

    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    report = f"""# HSBC Fund Screener 前端字段与API数据映射分析报告

生成时间: {timestamp}

## 概述

本报告分析了HSBC基金筛选器前端显示的字段与 `fundSearchResult` API 响应数据的对应关系。

## 相关文件链接

### API数据文件
- 📤 [API请求数据](../result/fundSearchResult_request.json) - 完整的API请求参数
- 📥 [API响应数据](../result/fundSearchResult_response.json) - 完整的API响应数据
- 🔍 [纯净响应数据](../result/fundSearchResult_data.json) - 提取的核心响应数据

### 页面截图和源码
- 📸 [页面截图](../result/fund_screener_result_*.png) - 基金筛选结果页面截图
- 📄 [页面源码](../result/fund_screener_page_*.html) - 完整的HTML源码
- 📋 [页面信息](../result/page_info_*.json) - 页面元数据信息

### 提取的字段数据
- 📊 [前端字段数据 (JSON)](frontend_fields_*.json) - 提取的所有产品字段数据
- 📈 [前端字段数据 (CSV)](frontend_fields_*.csv) - CSV格式的表格数据

## 前端界面截图

下图展示了HSBC基金筛选器的前端界面，包含了我们需要映射的所有字段：

![HSBC Fund Screener Interface](hsbc_fund_screener_interface.png)

*图：HSBC基金筛选器界面 - 显示了基金列表的各个字段*

## 字段映射详情

"""
    
    for i, (field_key, mapping) in enumerate(field_mapping.items(), 1):
        report += f"""### {i}. {mapping['frontend_name']} ({mapping['frontend_description']})

- **前端显示名称**: {mapping['frontend_name']}
- **API数据路径**: `{mapping['api_path']}`
- **数据类型**: {mapping['data_type']}
- **示例值**: {mapping['example']}
- **API原始值**: `{mapping['api_value']}`
"""
        
        if 'currency_path' in mapping:
            report += f"- **货币字段路径**: `{mapping['currency_path']}`\n"
            report += f"- **货币值**: `{mapping['currency_value']}`\n"
        
        if 'calculation' in mapping:
            report += f"- **计算方式**: {mapping['calculation']}\n"
        
        report += "\n"

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

    report += """
## 前端字段与API数据对应关系图

```
前端表格字段                    API响应数据路径
┌─────────────────────┐       ┌──────────────────────────────────┐
│ Fund                │  ←──  │ header.name                      │
│ NAV                 │  ←──  │ summary.dayEndNAV +              │
│                     │       │ summary.dayEndNAVCurrencyCode   │
│ YTD return          │  ←──  │ performance.calendarReturns.     │
│                     │       │ returnYTD                        │
│ 1Y return           │  ←──  │ performance.annualizedReturns.   │
│                     │       │ return1Yr                        │
│ Fund class currency │  ←──  │ header.currency                  │
│ 1 year sharpe ratio │  ←──  │ risk[year=1].yearRisk.sharpeRatio│
│ Fund size           │  ←──  │ summary.assetsUnderManagement    │
│ HSBC risk level     │  ←──  │ summary.riskLvlCde               │
│ Morningstar rating  │  ←──  │ rating.morningstarRating         │
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
    report = generate_mapping_report(field_mapping)
    
    # 保存报告到根目录
    report_file = BASE_DIR / "fundSearchResult" / f"field_mapping_analysis_{datetime.now().strftime('%Y%m%d_%H%M%S')}.md"
    with open(report_file, 'w', encoding='utf-8') as f:
        f.write(report)

    # 保存JSON格式的映射数据到 mapping 目录
    json_file = MAPPING_DIR / f"field_mapping_{datetime.now().strftime('%Y%m%d_%H%M%S')}.json"
    with open(json_file, 'w', encoding='utf-8') as f:
        json.dump(field_mapping, f, indent=2, ensure_ascii=False)
    
    print(f"📊 字段映射分析报告已保存: {report_file}")
    print(f"📋 字段映射JSON数据已保存: {json_file}")
    print("\n🎯 分析完成！")

if __name__ == "__main__":
    main()
