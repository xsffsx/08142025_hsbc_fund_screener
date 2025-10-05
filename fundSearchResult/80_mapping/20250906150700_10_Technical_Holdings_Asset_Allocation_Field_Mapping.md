# HSBC Holdings Asset Allocation 数据库验证字段映射

生成时间: 2025-09-06 15:07:00

## 数据库连接验证

**数据库配置**:
- Host: 127.0.0.1:5433
- Database: price
- Schema: schema_price
- User: hsbc_user
- 主视图: `v_ut_hldg_alloc`

## 数据流转路径
**数据库 → DAO层 → Service层 → Controller层 → API响应 → 前端UI**

## Holdings Asset Allocation 字段映射表 (数据库验证)

| NO | 前端字段 | UI数值 | API数值 | API数据路径 | 数据库实体字段 | 数据库列名 | 数据转换 | 验证状态 |
|----|---------|--------|---------|-------------|---------------|-----------|----------|----------|
| 1 | **Fund** | BlackRock Asian Tiger Bond Fund (Class A2) | `"BlackRock Asian Tiger Bond Fund (Class A2)"` | `header.name` | `prodName` | `prod_name` | 直接映射 | ✅ 已验证 |
| 2 | **Stock** | -0.23% | `-0.22746` | `holdings.assetAlloc.assetAllocations[name=Stock].weighting` | `holdingAllocWeightNet` | `hldg_alloc_wght_net` | 百分比格式化 | ✅ 已验证 |
| 3 | **Bond** | 79.58% | `79.58465` | `holdings.assetAlloc.assetAllocations[name=Bond].weighting` | `holdingAllocWeightNet` | `hldg_alloc_wght_net` | 百分比格式化 | ✅ 已验证 |
| 4 | **Cash** | 16.96% | `16.96025` | `holdings.assetAlloc.assetAllocations[name=Cash].weighting` | `holdingAllocWeightNet` | `hldg_alloc_wght_net` | 百分比格式化 | ✅ 已验证 |
| 5 | **Others** | 3.68% | `3.6825599999999987` | `calculated:100-Stock-Bond-Cash` | N/A | N/A | 计算字段 | 🔴 计算字段 |

## 数据库字段验证结果

### 1. 数据库直接验证字段 (4个)
- **Fund**: `prod_name` ✅
- **Stock**: `hldg_alloc_wght_net` (where `hldg_alloc_class_type='ASET_ALLOC' AND hldg_alloc_class_name='Stock'`) ✅
- **Bond**: `hldg_alloc_wght_net` (where `hldg_alloc_class_type='ASET_ALLOC' AND hldg_alloc_class_name='Bond'`) ✅
- **Cash**: `hldg_alloc_wght_net` (where `hldg_alloc_class_type='ASET_ALLOC' AND hldg_alloc_class_name='Cash'`) ✅

### 2. 计算字段 (1个)
- **Others**: 通过计算得出 (100% - Stock% - Bond% - Cash%) 🔴

### 3. 数据库验证查询
```sql
SELECT performance_id, hldg_alloc_class_type, hldg_alloc_class_name, hldg_alloc_wght_net
FROM schema_price.v_ut_hldg_alloc 
WHERE hldg_alloc_class_type = 'ASET_ALLOC'
AND performance_id IS NOT NULL 
LIMIT 10;
```

### 4. 实际数据示例
```
performance_id | hldg_alloc_class_type | hldg_alloc_class_name | hldg_alloc_wght_net 
U42557         | ASET_ALLOC            | Stock                 |            81.79792
U42557         | ASET_ALLOC            | Bond                  |            15.20208
U42557         | ASET_ALLOC            | Cash                  |             3.00000
```

## 源码映射验证

### UTHoldingAlloc.java (实体类)

| 前端字段 | 数据库列名 | 实体字段 | @Column注解 | 验证状态 |
|---------|-----------|----------|-------------|----------|
| Fund | `prod_name` | `prodName` | `@Column(name = "PROD_NAME")` | ✅ 已验证 |
| Stock/Bond/Cash | `hldg_alloc_wght_net` | `holdingAllocWeightNet` | `@Column(name = "HLDG_ALLOC_WGHT_NET")` | ✅ 已验证 |

### UTHoldingAllocId.java (复合主键)

| 字段 | 数据库列名 | 实体字段 | @Column注解 | 验证状态 |
|------|-----------|----------|-------------|----------|
| Performance ID | `performance_id` | `performanceId` | `@Column(name = "PERFORMANCE_ID")` | ✅ 已验证 |
| Class Type | `hldg_alloc_class_type` | `holdingAllocClassType` | `@Column(name = "HLDG_ALLOC_CLASS_TYPE")` | ✅ 已验证 |
| Class Name | `hldg_alloc_class_name` | `holdingAllocClassName` | `@Column(name = "HLDG_ALLOC_CLASS_NAME")` | ✅ 已验证 |

### FundSearchResultServiceImpl.java (Service层)

| 前端字段 | Service方法 | 具体实现 | 验证状态 |
|---------|------------|----------|----------|
| Fund | `setHeader()` | `header.setName(utProdInstm.getProdName())` | ✅ 已验证 |
| Asset Allocation | `setAssetAllocation()` | `holdings.setAssetAlloc(setAssetAllocation(utProdInstm, holdingAllocMap, assetAlloc, "ASET_ALLOC"))` | ✅ 已验证 |

## 数据转换规则

### 1. 直接映射字段
- **Fund**: 基金名称直接映射，无需转换

### 2. 百分比格式化字段
- **Stock/Bond/Cash**: 数据库存储为小数 (81.79792)，前端显示为百分比 (81.80%)

### 3. 计算字段
- **Others**: 通过公式计算 `100% - Stock% - Bond% - Cash%`

### 4. Asset Allocation数据结构
```java
// Asset Allocation数据设置
private AssetAllocations setAssetAllocation(final UtProdInstm utProdInstm,
    final Map<String, List<UTHoldingAlloc>> holdingAllocMap, final AssetAllocations assetAllocations, final String classType) {
    
    if (null != holdingAllocMap) {
        List<UTHoldingAlloc> assetAllocList = holdingAllocMap.get(utProdInstm.getPerformanceId());
        List<HoldingAllocation> assetAllocationsList = new ArrayList<HoldingAllocation>();
        
        if (ListUtil.isValid(assetAllocList)) {
            for (UTHoldingAlloc assetAlloc : assetAllocList) {
                if (assetAlloc.getUtHoldingAllocId().getHoldingAllocClassType().equals(classType)) {
                    HoldingAllocation holdingAllocation = new HoldingAllocation();
                    holdingAllocation.setName(assetAlloc.getUtHoldingAllocId().getHoldingAllocClassName());
                    holdingAllocation.setWeighting(assetAlloc.getHoldingAllocWeightNet());
                    assetAllocationsList.add(holdingAllocation);
                }
            }
        }
        assetAllocations.setAssetAllocations(assetAllocationsList);
    }
    return assetAllocations;
}
```

### 5. 数据库查询逻辑
```java
// DAO层查询Holdings Allocation数据
Map<String, List<UTHoldingAlloc>> holdingAllocMap = this.fundSearchResultDao.searchHldgAllocMap(prodIds_DB, "ASET_ALLOC");

// 按performance_id分组，按hldg_alloc_class_type过滤
// ASET_ALLOC: Asset Allocation (Stock, Bond, Cash)
// STOCK_SEC: Stock Sectors
// STOCK_GEO: Stock Geographic
// BOND_SEC: Bond Sectors
// BOND_GEO: Bond Geographic
```

## 验证总结

### ✅ 验证成功的字段 (4个)
| 字段类型 | 验证状态 | 数量 |
|---------|----------|------|
| 数据库直接验证 | ✅ 已验证 | 4个字段 |
| 计算字段 | 🔴 计算字段 | 1个字段 |
| 实时API调用 | N/A | 0个字段 |
| 数据稀少字段 | N/A | 0个字段 |

### 🎯 关键发现
1. **数据库字段完整性**: Asset Allocation字段在数据库中都有对应存储
2. **源码映射完整**: 实体类和Service层映射关系完整且正确
3. **数据质量良好**: 查询结果显示数据完整，无NULL值问题
4. **转换规则简单**: 主要是百分比格式化
5. **Holdings数据结构**: 通过holdingAllocMap统一管理不同类型的配置数据
6. **分类查询**: 通过hldg_alloc_class_type区分不同类型的配置 (ASET_ALLOC, STOCK_SEC, STOCK_GEO等)

**结论**: Holdings Asset Allocation字段映射关系完全正确，除Others字段需要计算外，其他字段都有数据库支持。
