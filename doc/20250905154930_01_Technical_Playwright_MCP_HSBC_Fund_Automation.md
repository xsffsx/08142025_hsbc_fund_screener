# Playwright MCP 工具自动化 HSBC 基金筛选页面操作指南

**创建时间**: 2025-09-05 15:49:30  
**文档类型**: Technical  
**目标**: 使用 Playwright MCP 工具自动化访问 HSBC 基金筛选页面并完成风险等级筛选操作

## 目录
- [操作概述](#操作概述)
- [详细步骤](#详细步骤)
- [关键元素定位](#关键元素定位)
- [验证点](#验证点)
- [最终状态](#最终状态)

## 操作概述

本文档记录了使用 Playwright MCP 工具自动化操作 HSBC 基金筛选页面的完整流程。主要任务包括：

1. 导航到 HSBC 基金筛选页面
2. 设置风险等级为 "Speculative - 5"
3. 执行基金搜索
4. 调整显示数量为每页 50 条记录

**目标网址**: `https://investments3.personal-banking.hsbc.com.hk/public/utb/en-gb/fundScreener`

## 详细步骤

### 步骤 1: 导航到基金筛选页面

```javascript
// 函数调用
browser_navigate_Playwright({
  url: "https://investments3.personal-banking.hsbc.com.hk/public/utb/en-gb/fundScreener"
})
```

**预期结果**: 页面加载完成，显示基金筛选表单

### 步骤 2: 等待页面完全加载

```javascript
// 函数调用
browser_wait_for_Playwright({
  time: 5
})
```

**预期结果**: 页面元素完全渲染，可以进行交互操作

### 步骤 3: 获取页面快照验证状态

```javascript
// 函数调用
browser_snapshot_Playwright()
```

**预期结果**: 确认页面已加载，可以看到"By your risk tolerance"部分的下拉框

### 步骤 4: 点击风险偏好下拉框

```javascript
// 函数调用
browser_click_Playwright({
  element: "风险偏好下拉框按钮",
  ref: "e129"
})
```

**元素定位**: `#dropdown-riskLevel`  
**预期结果**: 下拉框展开，显示所有风险等级选项

### 步骤 5: 选择 "Speculative - 5" 风险等级

```javascript
// 函数调用
browser_click_Playwright({
  element: "Speculative - 5 选项",
  ref: "e253"
})
```

**元素定位**: `option "Speculative - 5"`  
**预期结果**: 
- 下拉框显示 "Speculative - 5"
- 搜索按钮变为可用状态（不再是 disabled）

### 步骤 6: 执行基金搜索

```javascript
// 函数调用
browser_click_Playwright({
  element: "搜索按钮",
  ref: "e196"
})
```

**元素定位**: `button[data-testid="Search"]`  
**预期结果**: 
- 页面跳转到结果页面
- URL 变更为 `fundScreenerResult`
- 显示搜索结果统计

### 步骤 7: 调整显示数量为 50 条

```javascript
// 函数调用
browser_click_Playwright({
  element: "显示50条记录按钮",
  ref: "e923"
})
```

**元素定位**: `button[name="50"]`  
**预期结果**: 
- 页面显示设置更新为 "50 selected"
- 基金列表显示 50 条记录
- 分页信息相应调整

### 步骤 8: 拍摄最终状态截图

```javascript
// 函数调用
browser_take_screenshot_Playwright({
  filename: "hsbc_fund_screener_final_result.png",
  fullPage: true
})
```

**预期结果**: 生成完整页面截图，保存操作结果

## 关键元素定位

### 主要页面元素

| 元素描述 | 选择器 | Ref 值 | 备注 |
|---------|--------|--------|------|
| 风险偏好下拉框 | `#dropdown-riskLevel` | e129 | 初始状态为空 |
| Speculative - 5 选项 | `option[name="Speculative - 5"]` | e253 | 下拉框展开后可见 |
| 搜索按钮 | `button[data-testid="Search"]` | e196 | 选择风险等级后启用 |
| 显示50条按钮 | `button[name="50"]` | e923 | 结果页面底部 |

### 状态指示元素

| 状态描述 | 验证方法 | 预期值 |
|---------|----------|--------|
| 风险等级已选择 | 按钮文本内容 | "Speculative - 5" |
| 搜索结果数量 | 页面文本 | "1430 matches" |
| 显示数量设置 | 按钮状态 | "50 selected" |

## 验证点

### 每步操作后的验证方法

1. **页面导航验证**
   - 检查 URL 是否正确
   - 确认页面标题包含 "Unit Trusts - Search for funds"

2. **下拉框操作验证**
   - 确认下拉框展开显示所有选项
   - 验证 "Speculative - 5" 选项可见

3. **风险等级选择验证**
   - 按钮文本更新为 "Speculative - 5"
   - 搜索按钮状态从 disabled 变为可用

4. **搜索执行验证**
   - URL 变更为 fundScreenerResult
   - 页面显示 "Results 1430 matches"
   - 筛选条件显示 "By your risk tolerance: Speculative - 5"

5. **显示设置验证**
   - 显示设置更新为 "50 selected"
   - 基金列表显示 50 条记录
   - 分页显示调整为 29 页

## 最终状态

### 操作完成后的页面状态

- **URL**: `https://investments3.personal-banking.hsbc.com.hk/public/utb/en-gb/fundScreenerResult`
- **筛选条件**: "By your risk tolerance: Speculative - 5"
- **搜索结果**: 1430 个匹配基金
- **显示设置**: 每页 50 条记录
- **当前页面**: 第 1 页，共 29 页
- **基金列表**: 显示完整的基金信息表格，包含：
  - 基金名称和代码
  - NAV 价格和变化
  - YTD 回报率
  - 1年回报率
  - 基金货币
  - 夏普比率
  - 基金规模
  - HSBC 风险等级
  - 晨星评级

### 截图文件

最终状态截图保存为: `hsbc-fund-screener-final-result.png`

---

**注意事项**: 
- 所有操作均基于 Playwright MCP 工具执行
- 元素定位器（ref 值）可能因页面更新而变化
- 建议在实际使用时先获取页面快照确认元素状态
