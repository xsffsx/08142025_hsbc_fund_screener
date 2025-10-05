# HSBC Fund Screener 搜索字段映射分析报告

**生成时间**: 2025-09-07 07:59:17  
**分析目标**: 搜索表单字段与API参数的映射关系  
**重点关注**: By your risk tolerance 下拉菜单字段映射

## 📋 概述

本报告分析HSBC基金筛选器搜索表单中各个字段与后端API参数的对应关系，特别关注"By your risk tolerance"下拉菜单的字段映射。

### API请求信息
- **请求URL**: `https://investments3.personal-banking.hsbc.com.hk/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundSearchResult`
- **请求方法**: `POST`
- **时间戳**: `2025-09-06T09:57:38.541432`

## 🎯 搜索字段映射详情

### 1. By your risk tolerance (风险承受能力)

| 属性 | 前端显示值 | API参数值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | By your risk tolerance | detailedCriterias[criteriaKey=RISK] | 风险等级筛选字段 |
| **字段类型** | 下拉菜单 (单选/多选) | criteriaValue | 支持多个风险等级选择 |
| **前端选项** | Secure - 0 | "0" | 保守型投资者 |
| | Very cautious - 1 | "1" | 非常谨慎型投资者 |
| | Cautious - 2 | "2" | 谨慎型投资者 |
| | Balanced - 3 | "3" | 平衡型投资者 |
| | Adventurous - 4 | "4" | 冒险型投资者 |
| **当前API值** | 0:1:2:3:4:5 | 多个值用冒号分隔 | 当前选择的风险等级 |
| **操作符** | in | in | 包含操作，支持多选 |
| **数据类型** | multi_select_string | 字符串数组 | 多选值的字符串表示 |
| **转换规则** | 前端选项值 → API数字字符串 | 0:1:2:3:4 | 多个选择用冒号连接 |

**专业说明**: 风险承受能力是基金投资的核心筛选条件，帮助投资者根据自身风险偏好选择合适的基金产品。API支持同时选择多个风险等级，为投资者提供更灵活的筛选选项。

### 2. 其他搜索字段映射


#### Fund code / Keywords

| 属性 | 值 | 说明 |
|------|---|------|
| **前端字段** | Fund code / Keywords | 基金代码或关键词搜索 |
| **字段类型** | text_input | 前端控件类型 |
| **API路径** | detailedCriterias[criteriaKey=FUND_CODE_OR_NAME].criteriaValue | API参数路径 |
| **当前值** | null (未使用) | 当前API参数值 |
| **数据类型** | string | 数据类型 |
| **转换说明** | 文本输入框，直接传递搜索关键词 | 前端到API的转换规则 |

#### Asset class

| 属性 | 值 | 说明 |
|------|---|------|
| **前端字段** | Asset class | 资产类别选择，如股票、债券、混合等 |
| **字段类型** | multi_select | 前端控件类型 |
| **API路径** | detailedCriterias[criteriaKey=ASSET_CLASS].criteriaValue | API参数路径 |
| **当前值** | null (未使用) | 当前API参数值 |
| **数据类型** | multi_select_string | 数据类型 |
| **转换说明** | 多选下拉菜单，选中值用冒号分隔 | 前端到API的转换规则 |

#### Geography

| 属性 | 值 | 说明 |
|------|---|------|
| **前端字段** | Geography | 地理区域选择，如亚洲、欧洲、美洲等 |
| **字段类型** | multi_select | 前端控件类型 |
| **API路径** | detailedCriterias[criteriaKey=GEOGRAPHY].criteriaValue | API参数路径 |
| **当前值** | null (未使用) | 当前API参数值 |
| **数据类型** | multi_select_string | 数据类型 |
| **转换说明** | 多选下拉菜单，选中值用冒号分隔 | 前端到API的转换规则 |

#### Fund house

| 属性 | 值 | 说明 |
|------|---|------|
| **前端字段** | Fund house | 基金公司选择，如BlackRock、Fidelity等 |
| **字段类型** | multi_select | 前端控件类型 |
| **API路径** | detailedCriterias[criteriaKey=FUND_HOUSE].criteriaValue | API参数路径 |
| **当前值** | null (未使用) | 当前API参数值 |
| **数据类型** | multi_select_string | 数据类型 |
| **转换说明** | 多选下拉菜单，选中值用冒号分隔 | 前端到API的转换规则 |

#### Fund currency

| 属性 | 值 | 说明 |
|------|---|------|
| **前端字段** | Fund currency | 基金计价货币选择，如USD、HKD、EUR等 |
| **字段类型** | multi_select | 前端控件类型 |
| **API路径** | detailedCriterias[criteriaKey=CURRENCY].criteriaValue | API参数路径 |
| **当前值** | null (未使用) | 当前API参数值 |
| **数据类型** | multi_select_string | 数据类型 |
| **转换说明** | 多选下拉菜单，选中值用冒号分隔 | 前端到API的转换规则 |

## 🔧 API参数配置

### 核心搜索参数

| 参数名 | 当前值 | 说明 |
|--------|--------|------|
| **productType** | `UT` | 产品类型，固定为UT（Unit Trust） |
| **sortBy** | `return1Yr` | 排序字段，当前为return1Yr（1年回报率） |
| **sortOrder** | `desc` | 排序方向，desc为降序，asc为升序 |
| **numberOfRecords** | `10` | 返回记录数量，当前为10条 |
| **restrOnlScribInd** | `Y` | 限制订阅指标，Y表示仅显示可订阅的基金 |

### detailedCriterias 结构分析

当前API请求中的 `detailedCriterias` 数组包含以下筛选条件:

```json
[
  {
    "criteriaKey": "RISK",
    "criteriaValue": "0:1:2:3:4:5",
    "operator": "in"
  },
  {
    "criteriaKey": "GNRA",
    "criteriaValue": "Y:null",
    "operator": "in"
  }
]
```

**说明**:
- `criteriaKey`: 筛选字段的标识符
- `criteriaValue`: 筛选值，多个值用冒号(:)分隔
- `operator`: 操作符，"in"表示包含操作

## 🎯 关键发现

### 1. 风险等级映射规律
- **前端显示**: 文字描述 + 数字等级 (如 "Adventurous - 4")
- **API传递**: 纯数字字符串 (如 "4")
- **多选支持**: 多个等级用冒号分隔 (如 "0:1:2:3:4")

### 2. 搜索字段通用模式
- **单选字段**: 直接传递选中值
- **多选字段**: 选中值用冒号(:)连接
- **文本字段**: 直接传递输入内容
- **未使用字段**: 不出现在detailedCriterias数组中

### 3. API设计特点
- 使用 `detailedCriterias` 数组统一管理所有筛选条件
- 每个筛选条件包含 key、value、operator 三个属性
- 支持灵活的多选和组合筛选

## 📊 实际应用示例

### 选择不同风险等级的API调用

```javascript
// 选择单个风险等级 (Adventurous - 4)
{
  "criteriaKey": "RISK",
  "criteriaValue": "4",
  "operator": "in"
}

// 选择多个风险等级 (Balanced + Adventurous)
{
  "criteriaKey": "RISK", 
  "criteriaValue": "3:4",
  "operator": "in"
}

// 选择所有风险等级 (当前API状态)
{
  "criteriaKey": "RISK",
  "criteriaValue": "0:1:2:3:4", 
  "operator": "in"
}
```

## 🔍 技术实现建议

### 前端实现
1. **下拉菜单组件**: 支持单选或多选模式
2. **值映射**: 维护显示文本到API值的映射表
3. **状态管理**: 跟踪用户选择状态，动态构建API参数

### API集成
1. **参数构建**: 根据用户选择动态构建detailedCriterias数组
2. **值格式化**: 多选值用冒号连接
3. **请求优化**: 未选择的字段不添加到请求中

## 📋 文件链接

- **API请求数据**: `../01_summary/result/fundSearchResult_request.json`
- **映射配置**: `mapping/search_field_mapping_20250907_075917.json`

---

**报告生成时间**: 2025-09-07 07:59:17  
**分析完成**: ✅ 搜索字段映射分析完成
