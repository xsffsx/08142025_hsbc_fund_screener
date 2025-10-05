# 标准化 Mapping 处理模板

## 🎯 快速实现新标签页的标准流程

### Step 1: 创建目录结构
```bash
# 替换 XX_tab_name 为实际的标签页名称，如 03_risk_return_profile
mkdir -p XX_tab_name/{log,mapping,result}
```

### Step 2: 复制脚本模板
```bash
# 从 02_fund_profile 复制标准脚本
cp 02_fund_profile/04_capture_hsbc_fund_profile_analyst.py XX_tab_name/04_capture_hsbc_XX_analyst.py
cp 02_fund_profile/05_analyze_field_mapping.py XX_tab_name/05_analyze_field_mapping.py
cp 02_fund_profile/06_field_extractor.py XX_tab_name/06_field_extractor.py
```

### Step 3: 修改脚本配置

#### A. 修改 04_capture_*.py
1. **更新注释和描述**:
   - 将 "Fund profile" 替换为目标标签名称
   - 更新输出目录描述

2. **更新标签点击逻辑**:
   ```python
   # 查找并点击目标标签 (替换为实际标签名称)
   target_tab_selector = 'button:has-text("Target Tab Name"), a:has-text("Target Tab Name")'
   await page.click(target_tab_selector)
   ```

#### B. 修改 05_analyze_field_mapping.py
1. **更新字段描述库**:
   ```python
   field_descriptions = {
       'Field1': '字段1的业务描述',
       'Field2': '字段2的业务描述',
       # ... 添加所有目标字段
   }
   
   field_remarks = {
       'Field1': '字段1的专业投资相关说明',
       'Field2': '字段2的专业投资相关说明',
       # ... 添加所有目标字段
   }
   ```

2. **更新字段映射逻辑**:
   ```python
   # 替换 map_fields_to_api 函数中的字段映射
   field_mapping["Field1"] = {
       "frontend_name": "Field1",
       "frontend_description": "字段1描述",
       "api_path": "api.path.to.field1",
       "api_value": sample_product.get("api", {}).get("path", {}).get("field1", ""),
       "data_type": "string",  # string/number/percentage/date/currency
       "example": "示例值"
   }
   ```

#### C. 修改 06_field_extractor.py
1. **更新字段映射配置**:
   ```python
   return {
       "Field1": {
           "api_path": "api.path.to.field1",
           "data_type": "string"
       },
       "Field2": {
           "api_path": "api.path.to.field2", 
           "data_type": "number"
       },
       # ... 添加所有目标字段
   }
   ```

2. **添加特殊路径处理** (如需要):
   ```python
   # 在 _get_nested_value 方法中添加特殊路径处理
   if "special_path_pattern" in path:
       # 添加特殊处理逻辑
       pass
   ```

### Step 4: 测试验证
```bash
cd XX_tab_name
python 04_capture_hsbc_XX_analyst.py screener
python 05_analyze_field_mapping.py  # 自动调用06脚本，无需单独运行
# python 06_field_extractor.py  # 已自动运行，无需手动执行
```

### Step 5: 验证输出
- ✅ 确认标签点击成功
- ✅ 确认字段映射报告生成
- ✅ 确认字段数据提取成功
- ✅ 确认报告格式统一

## 🔧 支持的数据类型

| 数据类型 | 说明 | 示例 |
|---------|------|------|
| `string` | 字符串，直接映射 | "BlackRock" |
| `number` | 数字，支持货币组合 | 71.76 |
| `percentage` | 百分比，自动转换 | 85.33% |
| `date` | 日期，支持格式化 | "30 Dec 1994" |
| `currency` | 货币，需要货币代码 | "71.76 USD" |
| `array_lookup` | 数组查找，条件筛选 | 特殊路径处理 |

## 🐛 映射错误诊断与修复

### 核心原则
> **#1 API有值原则**: 如果前端显示有数值，API响应中一定有对应的字段数据
> **#2 智能样本选择**: 优先选择有完整数据的产品作为映射样本

### 常见映射错误类型

#### 错误类型1: API原始值显示为 `None`
**症状**: 字段映射报告中 `API原始值` 列显示 `None`
**原因**: 样本产品选择不当，选中了该字段为null的产品
**修复方法**:
```python
def extract_sample_product_data(api_data: Dict[str, Any]) -> Dict[str, Any]:
    """提取一个有完整数据的产品作为样本"""
    products = api_data.get("responseData", {}).get("products", [])
    if not products:
        return {}

    # 优先选择有关键字段数据的产品作为样本
    for product in products:
        # 检查关键字段是否有值
        if product.get("profile", {}).get("distributionFrequency") is not None:
            return product

    return products[0]  # 备选方案
```

#### 错误类型2: 字段名不匹配
**症状**: 能找到数据结构但提取不到值
**原因**: API字段名与预期不符 (如 `stdDev` vs `standardDeviation`)
**诊断方法**:
```bash
# 搜索API响应中的实际字段名
grep -i "标准差相关关键词" result/fundSearchResult_data.json
```
**修复方法**: 更新05和06脚本中的字段路径

#### 错误类型3: 数据结构查找错误
**症状**: 复杂嵌套结构中找不到数据
**原因**: 数组查找条件错误 (如 `risk[].year == 1` vs `risk[].yearRisk.year == 1`)
**修复方法**:
```python
# 正确的嵌套数组查找
for risk_item in risk_data:
    if risk_item.get("yearRisk", {}).get("year") == 1:
        value = risk_item.get("yearRisk", {}).get("targetField")
        break
```

### 映射错误分析流程

#### Step 1: 发现问题
- 检查字段映射报告中的 `API原始值` 列
- 查找显示为 `None` 或 `null` 的字段

#### Step 2: 验证API数据
```bash
# 搜索API响应中的字段数据
grep -i "字段关键词" result/fundSearchResult_data.json
```

#### Step 3: 分析数据结构
- 确认字段的实际名称和路径
- 检查数据在不同产品中的分布情况

#### Step 4: 修复映射逻辑
- 更新05脚本的样本选择逻辑
- 修正字段路径和查找条件
- 更新06脚本的字段映射配置

#### Step 5: 验证修复
```bash
# 重新运行映射分析（自动调用字段提取）
python 05_analyze_field_mapping.py
# 06脚本会自动运行，确保时间戳同步
```

## 📋 质量检查清单

- [x] 标签点击成功率 100%
- [x] 字段映射准确率 100%
- [x] 数据提取成功率 100%
- [x] 报告格式统一性 100%
- [x] 专业描述完整性 100%
- [ ] 动态路径配置正确
- [ ] 文件命名规范正确
- [ ] 错误处理机制完善
- [ ] **所有字段的API原始值都不为 `None`**
- [ ] **字段路径与API响应结构匹配**
- [ ] **样本产品包含所有必要字段的数据**
- [ ] **报告文件链接与实际文件名一致**
- [ ] **05脚本自动调用06脚本成功**
- [ ] **时间戳在报告和数据文件间同步**

## 🎯 常见字段类型处理

### 复杂API路径示例
```python
# 数组条件查找
"header.prodAltNumSeg[prodCdeAltClassCde=I].prodAltNum"

# 嵌套对象查找
"risk[year=1].yearRisk.sharpeRatio"

# 货币组合
"summary.dayEndNAV" + "summary.dayEndNAVCurrencyCode"
```

### 子标签页处理模式
对于Holdings标签页下的子标签（如Top 5 equity sector），采用以下处理模式：

#### 目录命名规范
```bash
# 主标签页 - Asset allocation (默认子标签)
06_holdings_00_Asset_allocation/

# 子标签页 - 使用主标签_序号_子标签格式
06_holdings_01_Top_5_equity_sector/
```

#### 标签点击策略
```python
# 主标签页点击
holdings_selector = 'button:has-text("Holdings"), a:has-text("Holdings")'
await page.click(holdings_selector)

# 子标签页点击策略
# 1. 点击子标签
sector_selector = 'li:has-text("Top 5 equity sector"), button:has-text("Top 5 equity sector")'
await page.click(sector_selector)

# 2. 点击Fund列标题进行排序 (重要!)
fund_selector = '[data-locale-key="OrderScreener.H_TABLE_FUND_CODE"], th:has-text("Fund")'
await page.click(fund_selector)
```

#### 字段映射模式
```python
# 子标签页通常使用序号字段，需要动态排序计算
field_mapping = {
    "Fund": {"api_path": "header.name", "data_type": "string"},
    "1st": {"api_path": "calculated:top_sector[0]", "data_type": "string"},
    "2nd": {"api_path": "calculated:top_sector[1]", "data_type": "string"},
    "3rd": {"api_path": "calculated:top_sector[2]", "data_type": "string"},
    "4th": {"api_path": "calculated:top_sector[3]", "data_type": "string"},
    "5th": {"api_path": "calculated:top_sector[4]", "data_type": "string"}
}

# 实际API数据结构
# holdings.stockSectors.globalStockSectors[
#   {"name": "FS", "weighting": 0.10324},      # Financial Services
#   {"name": "ENER", "weighting": 0.0001},     # Energy
#   {"name": "BM", "weighting": 6e-05},        # Basic Materials
#   {"name": "TECH", "weighting": 5e-05},      # Technology
#   {"name": "HC", "weighting": 4e-05}         # Healthcare
# ]
```

#### 复杂数据处理示例
```python
# 行业代码映射
sector_mapping = {
    "FS": "Financial Services",
    "ENER": "Energy",
    "BM": "Basic Materials",
    "TECH": "Technology",
    "HC": "Healthcare",
    "IND": "Industrials",
    "CS": "Communication Services",
    "CD": "Consumer Defensive",
    "CC": "Consumer Cyclical",
    "UTIL": "Utilities"
}

# 动态排序逻辑
def get_top_sectors(global_stock_sectors):
    sectors = []
    for sector in global_stock_sectors:
        sector_code = sector.get("name", "")
        weight = sector.get("weighting", 0)
        if sector_code != "Others" and weight > 0:
            display_name = sector_mapping.get(sector_code, sector_code)
            sectors.append({"name": display_name, "weight": weight})

    # 按权重降序排序，返回前5个
    return sorted(sectors, key=lambda x: x["weight"], reverse=True)[:5]
```

### 专业描述模板
```python
field_remarks = {
    'Fund': '基金名称是用户识别产品的主要标识，通常包含基金公司、投资策略和份额类别信息',
    'NAV': 'NAV是基金每份额的净资产价值，投资者买卖基金的参考价格，通常每日更新',
    'Return': '回报率反映基金的投资表现，是投资者评估基金业绩的重要指标',
    'Risk': '风险等级帮助投资者了解产品的风险特征，选择适合自己风险承受能力的产品',
    'Rating': '评级是第三方机构对基金历史表现的专业评价，为投资决策提供参考'
}
```
