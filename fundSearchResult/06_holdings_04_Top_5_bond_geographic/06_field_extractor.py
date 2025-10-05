#!/usr/bin/env python3
"""
HSBC Fund Screener Field Extractor
根据字段映射关系从API数据中提取前端显示字段
"""

import json
import shutil
from pathlib import Path
from typing import Dict, List, Any, Optional
from datetime import datetime

# 基础路径 - 相对于脚本位置 (动态获取当前目录)
SCRIPT_DIR = Path(__file__).resolve().parent
BASE_DIR = SCRIPT_DIR.parents[1]  # 项目根目录
RESULT_DIR = SCRIPT_DIR / "result"
MAPPING_DIR = SCRIPT_DIR / "mapping"
MAPPING_DIR.mkdir(parents=True, exist_ok=True)

def cleanup_extracted_files():
    """清理之前提取的字段文件"""
    print("🧹 清理之前提取的字段文件...")

    if MAPPING_DIR.exists():
        # 只清理字段提取相关的文件
        field_files = list(MAPPING_DIR.glob('frontend_fields_*'))
        cleaned_count = 0

        for file_path in field_files:
            if file_path.is_file():
                file_path.unlink()
                cleaned_count += 1

        print(f"✅ 已清理 {cleaned_count} 个字段提取文件")
    else:
        print("📁 mapping 目录不存在，无需清理")

class FieldExtractor:
    """字段提取器"""
    
    def __init__(self):
        self.field_mapping = self._load_field_mapping()
    
    def _load_field_mapping(self) -> Dict[str, Dict[str, Any]]:
        """加载字段映射配置"""
        # 内置字段映射配置
        return {
            "Fund": {
                "api_path": "header.name",
                "data_type": "string"
            },
            "1st": {
                "api_path": "calculated:top_bond_geographic[0]",
                "data_type": "string"
            },
            "2nd": {
                "api_path": "calculated:top_bond_geographic[1]",
                "data_type": "string"
            },
            "3rd": {
                "api_path": "calculated:top_bond_geographic[2]",
                "data_type": "string"
            },
            "4th": {
                "api_path": "calculated:top_bond_geographic[3]",
                "data_type": "string"
            },
            "5th": {
                "api_path": "calculated:top_bond_geographic[4]",
                "data_type": "string"
            }
        }
    
    def _get_nested_value(self, data: Dict[str, Any], path: str) -> Any:
        """从嵌套字典中获取值"""
        # 处理top bond geographic排序字段：calculated:top_bond_geographic[index]
        if path.startswith("calculated:top_bond_geographic[") and path.endswith("]"):
            # 提取索引
            import re
            match = re.search(r'top_bond_geographic\[(\d+)\]', path)
            if match:
                index = int(match.group(1))

                # 获取bondRegional数据并按权重排序
                holdings_data = data.get("holdings", {})
                bond_regional_data = holdings_data.get("bondRegional", {})
                bond_regional_exposures = bond_regional_data.get("bondRegionalExposures", [])

                # 创建债券地理区域权重列表，过滤掉Others和权重为0的项
                regions = []
                bond_geographic_mapping = {
                    "US": "United States",
                    "LA": "Latin America",
                    "UK": "United Kingdom",
                    "EZ": "Eurozone",
                    "AE": "Asia - Emerging",
                    "CA": "Canada",
                    "AU": "Australasia",
                    "EU": "Europe",
                    "AD": "Asia - Developed",
                    "AFRICA": "Africa",
                    "JAPAN": "Japan",
                    "CHINA": "China"
                }

                for region in bond_regional_exposures:
                    region_code = region.get("name", "")
                    weight = region.get("weighting", 0)

                    # 跳过Others和权重为0的项
                    if region_code != "Others" and weight > 0:
                        display_name = bond_geographic_mapping.get(region_code, region_code)
                        regions.append({"name": display_name, "weight": weight})

                # 按权重降序排序
                sorted_regions = sorted(regions, key=lambda x: x["weight"], reverse=True)

                # 返回指定索引的债券地理区域名称和权重百分比
                if index < len(sorted_regions):
                    region_name = sorted_regions[index]["name"]
                    region_weight = sorted_regions[index]["weight"]
                    return f"{region_name}\n{region_weight:.2f}%"
                return ""

        # 处理计算字段：calculated:100-Stock-Bond-Cash
        if path.startswith("calculated:"):
            if path == "calculated:100-Stock-Bond-Cash":
                # 获取Stock, Bond, Cash的值
                holdings_data = data.get("holdings", {})
                asset_alloc_data = holdings_data.get("assetAlloc", {})
                asset_allocations = asset_alloc_data.get("assetAllocations", [])

                asset_map = {}
                for allocation in asset_allocations:
                    name = allocation.get("name", "")
                    weighting = allocation.get("weighting", 0)
                    asset_map[name] = weighting

                stock_val = asset_map.get("Stock", 0)
                bond_val = asset_map.get("Bond", 0)
                cash_val = asset_map.get("Cash", 0)

                # 计算Others = 100 - Stock - Bond - Cash
                calculated_others = 100 - stock_val - bond_val - cash_val
                return calculated_others
            return None

        # 处理特殊路径：header.prodAltNumSeg[prodCdeAltClassCde=I].prodAltNum
        if "prodAltNumSeg[prodCdeAltClassCde=I]" in path:
            header_data = data.get("header", {})
            prod_alt_num_seg = header_data.get("prodAltNumSeg", [])
            for seg_item in prod_alt_num_seg:
                if seg_item.get("prodCdeAltClassCde") == "I":
                    return seg_item.get("prodAltNum")
            return None

        # 处理特殊路径：risk[year=1].yearRisk.sharpeRatio
        if "risk[year=1]" in path:
            risk_data = data.get("risk", [])
            for risk_item in risk_data:
                if risk_item.get("yearRisk", {}).get("year") == 1:
                    remaining_path = path.replace("risk[year=1].", "")
                    return self._get_nested_value(risk_item, remaining_path)
            return None

        # 处理特殊路径：holdings.assetAlloc.assetAllocations[name=Stock].weighting
        if "assetAllocations[name=" in path:
            # 提取资产类型名称
            import re
            match = re.search(r'assetAllocations\[name=([^\]]+)\]\.weighting', path)
            if match:
                asset_name = match.group(1)
                holdings_data = data.get("holdings", {})
                asset_alloc_data = holdings_data.get("assetAlloc", {})
                asset_allocations = asset_alloc_data.get("assetAllocations", [])

                for allocation in asset_allocations:
                    if allocation.get("name") == asset_name:
                        return allocation.get("weighting")
                return None

        # 普通嵌套路径处理
        keys = path.split('.')
        current = data

        for key in keys:
            if isinstance(current, dict) and key in current:
                current = current[key]
            else:
                return None

        return current
    
    def _format_value(self, value: Any, data_type: str, currency: Optional[str] = None) -> str:
        """格式化值"""
        if value is None:
            return "N/A"
        
        if data_type == "string":
            return str(value)
        
        elif data_type == "number":
            if currency:
                return f"{value} {currency}"
            return str(value)
        
        elif data_type == "percentage":
            # 处理百分比，Performance字段的API值已经是百分比形式，直接格式化
            if isinstance(value, (int, float)):
                return f"{value:.2f}%"
            return f"{value:.2f}%"

        elif data_type == "date":
            # 处理日期格式，如 "30 Dec 1994"
            if isinstance(value, str):
                try:
                    # 尝试解析日期并格式化
                    from datetime import datetime
                    # 假设输入格式是 "30 Dec 1994"
                    parsed_date = datetime.strptime(value, "%d %b %Y")
                    return parsed_date.strftime("%d %b %Y")
                except:
                    # 如果解析失败，直接返回原值
                    return str(value)
            return str(value)

        elif data_type == "number_millions":
            millions = value / 1000000
            if currency:
                return f"{millions:.2f} (Million {currency})"
            return f"{millions:.2f} Million"

        elif data_type == "quartile_ranking":
            # 将数字转换为四分位排名格式
            if value is None:
                return "N/A"
            if value == 1:
                return "1st"
            elif value == 2:
                return "2nd"
            elif value == 3:
                return "3rd"
            elif value == 4:
                return "4th"
            else:
                return str(value)

        return str(value)
    
    def extract_product_fields(self, product_data: Dict[str, Any]) -> Dict[str, str]:
        """从单个产品数据中提取所有前端字段"""
        extracted_fields = {}
        
        for field_name, mapping in self.field_mapping.items():
            # 获取主要值
            value = self._get_nested_value(product_data, mapping["api_path"])
            
            # 获取货币值（如果有）
            currency = None
            if "currency_path" in mapping:
                currency = self._get_nested_value(product_data, mapping["currency_path"])
            
            # 格式化值
            formatted_value = self._format_value(value, mapping["data_type"], currency)
            extracted_fields[field_name] = formatted_value
        
        return extracted_fields
    
    def extract_all_products(self, api_data: Dict[str, Any]) -> List[Dict[str, str]]:
        """从API数据中提取所有产品的前端字段"""
        products = api_data.get("responseData", {}).get("products", [])
        extracted_products = []
        
        for product in products:
            extracted_fields = self.extract_product_fields(product)
            extracted_products.append(extracted_fields)
        
        return extracted_products

def load_api_data() -> Dict[str, Any]:
    """加载API响应数据"""
    response_file = RESULT_DIR / "fundSearchResult_response.json"
    with open(response_file, 'r', encoding='utf-8') as f:
        return json.load(f)

def save_extracted_data(extracted_data: List[Dict[str, str]], output_file: Path):
    """保存提取的数据"""
    output_data = {
        "timestamp": datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
        "total_products": len(extracted_data),
        "fields": list(extracted_data[0].keys()) if extracted_data else [],
        "products": extracted_data
    }
    
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(output_data, f, indent=2, ensure_ascii=False)

def generate_csv_output(extracted_data: List[Dict[str, str]], csv_file: Path):
    """生成CSV格式输出"""
    import csv
    
    if not extracted_data:
        return
    
    fieldnames = list(extracted_data[0].keys())
    
    with open(csv_file, 'w', newline='', encoding='utf-8') as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(extracted_data)

def main():
    """主函数"""
    print("🔍 开始提取前端显示字段...")

    # 清理之前的字段提取文件
    cleanup_extracted_files()

    # 加载API数据
    api_data = load_api_data()
    print("✅ API数据加载完成")
    
    # 创建字段提取器
    extractor = FieldExtractor()
    print("✅ 字段提取器初始化完成")
    
    # 提取所有产品的前端字段
    extracted_data = extractor.extract_all_products(api_data)
    print(f"✅ 已提取 {len(extracted_data)} 个产品的字段数据")
    
    # 保存提取的数据
    # 优先使用环境变量中的时间戳（来自05脚本），否则使用当前时间戳
    import os
    timestamp = os.environ.get('FIELD_TIMESTAMP', datetime.now().strftime("%Y%m%d_%H%M%S"))
    
    # JSON格式 - 保存到 mapping 目录
    json_file = MAPPING_DIR / f"frontend_fields_{timestamp}.json"
    save_extracted_data(extracted_data, json_file)
    print(f"📋 JSON数据已保存: {json_file}")

    # CSV格式 - 保存到 mapping 目录
    csv_file = MAPPING_DIR / f"frontend_fields_{timestamp}.csv"
    generate_csv_output(extracted_data, csv_file)
    print(f"📊 CSV数据已保存: {csv_file}")
    
    # 显示前5个产品的示例
    print("\n📋 前5个产品的字段示例:")
    for i, product in enumerate(extracted_data[:5], 1):
        print(f"\n产品 {i}:")
        for field, value in product.items():
            print(f"  {field}: {value}")
    
    print(f"\n🎯 字段提取完成！共处理 {len(extracted_data)} 个产品")

if __name__ == "__main__":
    main()
