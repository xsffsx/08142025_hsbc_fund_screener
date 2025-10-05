# HSBC Fund Screener 搜索字段映射表

**生成时间**: 2025-09-07 08:00:00  
**数据来源**: fundSearchCriteria_tmp.md + 前端截图分析  
**API端点**: `/wmds/fundSearchCriteria`

## 🔍 搜索字段完整映射表

| 前端字段 | 前端显示值 | UI控件类型 | API参数路径 | API数值 | 数据转换 | 说明 |
|---------|-----------|-----------|-------------|---------|----------|------|
| **Fund code / Keywords** | 文本输入框 | text_input | `detailedCriterias[criteriaKey=FUND_CODE_OR_NAME].criteriaValue` | 用户输入的文本 | 直接传递 | 基金代码或关键词搜索 |
| **Asset class** | 下拉多选菜单 | multi_select | `detailedCriterias[criteriaKey=ASSET_CLASS].criteriaValue` | 选中值用冒号分隔 | 多选值连接 | 按基金类别搜索 |
| **Geography** | 下拉多选菜单 | multi_select | `detailedCriterias[criteriaKey=GEOGRAPHY].criteriaValue` | 选中值用冒号分隔 | 多选值连接 | 按主要投资地区搜索 |
| **Fund house** | 下拉多选菜单 | multi_select | `detailedCriterias[criteriaKey=FUND_HOUSE].criteriaValue` | 选中值用冒号分隔 | 多选值连接 | 按基金公司搜索 |
| **By your risk tolerance** | 下拉多选菜单 | multi_select | `detailedCriterias[criteriaKey=RISK].criteriaValue` | `"0:1:2:3:4:5"` | 风险等级数字连接 | 按风险承受能力搜索 |
| **Fund currency** | 下拉多选菜单 | multi_select | `detailedCriterias[criteriaKey=CURRENCY].criteriaValue` | 选中值用冒号分隔 | 多选值连接 | 按基金计价货币搜索 |

## 🎯 风险承受能力详细映射

| 前端显示值 | 风险等级说明 | API数值 | 对应基金风险等级 |
|-----------|-------------|---------|-----------------|
| **Secure** | 无投资风险产品 | `"0"` | 无投资风险 |
| **Very cautious** | 非常谨慎型 | `"1"` | 风险等级: 1 |
| **Cautious** | 谨慎型 | `"2"` | 风险等级: 1-2 |
| **Balanced** | 平衡型 | `"3"` | 风险等级: 1-3 |
| **Adventurous** | 冒险型 | `"4"` | 风险等级: 1-4 |
| **Speculative** | 投机型 | `"5"` | 风险等级: 1-5 |

## 📊 其他选项映射

| 前端字段 | 前端显示值 | UI控件类型 | API参数路径 | API数值 | 说明 |
|---------|-----------|-----------|-------------|---------|------|
| **GBA Wealth Connect – Southbound Scheme** | 复选框 | checkbox | `detailedCriterias[criteriaKey=GBA_SOUTHBOUND].criteriaValue` | `"Y"` / `"N"` | 大湾区理财通南向通 |
| **Environmental, Social and Governance (ESG) or sustainable investments** | 复选框 | checkbox | `detailedCriterias[criteriaKey=ESG].criteriaValue` | `"Y"` / `"N"` | ESG或可持续投资 |

## 🔧 高级筛选器映射

| 前端字段 | 前端显示值 | UI控件类型 | API参数路径 | 数值范围 | 数据转换 |
|---------|-----------|-----------|-------------|----------|----------|
| **1 year annualised return** | 滑动条范围选择 | range_slider | `detailedCriterias[criteriaKey=RETURN_1YR].criteriaValue` | `-51.05%` 到 `133.35%` | 百分比转小数 |
| **Dividend yield** | 滑动条范围选择 | range_slider | `detailedCriterias[criteriaKey=DIVIDEND_YIELD].criteriaValue` | `0.00%` 到 `10.11%` | 百分比转小数 |
| **Morningstar rating** | 星级选择 | star_rating | `detailedCriterias[criteriaKey=MORNINGSTAR_RATING].criteriaValue` | `1` 到 `5` | 星级数字 |
| **Average credit quality** | 等级选择 | credit_rating | `detailedCriterias[criteriaKey=CREDIT_QUALITY].criteriaValue` | `AAA` 到 `C` | 信用等级代码 |
| **1 year quartile ranking** | 四分位选择 | quartile_select | `detailedCriterias[criteriaKey=QUARTILE_1YR].criteriaValue` | `1` 到 `4` | 四分位数字 |

## 📋 API请求结构示例

### 基础搜索请求
```json
{
  "productType": "UT",
  "listCriteriaKeys": ["ALL"],
  "minMaxCriteriaKeys": ["ALL"],
  "restrOnlScribInd": "Y",
  "detailedCriterias": [
    {
      "criteriaKey": "RISK",
      "criteriaValue": "0:1:2:3:4:5",
      "operator": "in"
    }
  ]
}
```

### 完整搜索请求示例
```json
{
  "productType": "UT",
  "listCriteriaKeys": ["ALL"],
  "minMaxCriteriaKeys": ["ALL"],
  "restrOnlScribInd": "Y",
  "detailedCriterias": [
    {
      "criteriaKey": "FUND_CODE_OR_NAME",
      "criteriaValue": "BlackRock",
      "operator": "like"
    },
    {
      "criteriaKey": "ASSET_CLASS",
      "criteriaValue": "EQUITY:BOND",
      "operator": "in"
    },
    {
      "criteriaKey": "GEOGRAPHY",
      "criteriaValue": "ASIA:GLOBAL",
      "operator": "in"
    },
    {
      "criteriaKey": "FUND_HOUSE",
      "criteriaValue": "BLACKROCK:FIDELITY",
      "operator": "in"
    },
    {
      "criteriaKey": "RISK",
      "criteriaValue": "3:4:5",
      "operator": "in"
    },
    {
      "criteriaKey": "CURRENCY",
      "criteriaValue": "USD:HKD",
      "operator": "in"
    },
    {
      "criteriaKey": "ESG",
      "criteriaValue": "Y",
      "operator": "eq"
    },
    {
      "criteriaKey": "RETURN_1YR",
      "criteriaValue": "5.0:50.0",
      "operator": "between"
    },
    {
      "criteriaKey": "DIVIDEND_YIELD",
      "criteriaValue": "2.0:8.0",
      "operator": "between"
    },
    {
      "criteriaKey": "MORNINGSTAR_RATING",
      "criteriaValue": "4:5",
      "operator": "in"
    }
  ]
}
```

## 🎯 关键映射规则

### 1. 多选字段规则
- **分隔符**: 冒号 (`:`) 连接多个选中值
- **操作符**: `"in"` 表示包含操作
- **示例**: `"3:4:5"` 表示选择风险等级3、4、5

### 2. 范围字段规则
- **分隔符**: 冒号 (`:`) 连接最小值和最大值
- **操作符**: `"between"` 表示范围操作
- **示例**: `"5.0:50.0"` 表示5%到50%的范围

### 3. 单选字段规则
- **操作符**: `"eq"` 表示等于操作
- **示例**: `"Y"` 表示选中，`"N"` 表示未选中

### 4. 文本搜索规则
- **操作符**: `"like"` 表示模糊匹配
- **示例**: `"BlackRock"` 进行关键词搜索

## 💡 前端实现建议

### JavaScript映射配置
```javascript
const searchFieldMapping = {
  riskTolerance: {
    'Secure': '0',
    'Very cautious': '1',
    'Cautious': '2', 
    'Balanced': '3',
    'Adventurous': '4',
    'Speculative': '5'
  },
  
  buildCriteria: function(fieldKey, selectedValues, operator = 'in') {
    return {
      criteriaKey: fieldKey,
      criteriaValue: Array.isArray(selectedValues) ? selectedValues.join(':') : selectedValues,
      operator: operator
    };
  }
};
```

### 使用示例
```javascript
// 构建风险等级筛选条件
const riskCriteria = searchFieldMapping.buildCriteria(
  'RISK', 
  ['3', '4', '5']  // Balanced, Adventurous, Speculative
);

// 构建范围筛选条件
const returnCriteria = searchFieldMapping.buildCriteria(
  'RETURN_1YR',
  ['10.0', '30.0'],  // 10%到30%回报率
  'between'
);
```

---

**总结**: 此映射表提供了HSBC基金筛选器所有搜索字段的完整前端到API的映射关系，确保前端UI与后端API参数的一一对应。
