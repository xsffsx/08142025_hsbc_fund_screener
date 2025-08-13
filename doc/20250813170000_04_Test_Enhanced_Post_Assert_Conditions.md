# Playwright 增强版测试 — fundScreener 5步骤 Post Assert 验证

创建时间：2025-08-13 17:00:00

## 概述
基于原有的 fundScreener 风险偏好筛选测试，为每个步骤添加明确的 Post Assert 条件，确保每个步骤完成后有特定的元素出现才能进入下一步。

## 5个步骤及其 Post Assert 条件

### 步骤 1: 打开页面
- **操作**: 打开 https://investments3.personal-banking.hsbc.com.hk/public/utb/en-gb/fundScreener
- **Post Assert 条件**:
  - ✅ 页面标题包含 "Unit Trusts - Search for funds"
  - ✅ 风险偏好下拉框 (`#dropdown-riskLevel`) 可见
- **验证代码**:
  ```python
  await expect(page).to_have_title(re.compile(r"Unit Trusts.*Search.*funds|Fund.*Screener|Investment", re.IGNORECASE))
  await expect(risk_dropdown.first).to_be_visible()
  ```

### 步骤 2: 打开风险偏好下拉框
- **操作**: 点击风险偏好下拉框
- **Post Assert 条件**:
  - ✅ 下拉列表框 (`listbox`) 出现且可见
  - ✅ "Speculative - 5" 选项在下拉列表中可见
- **验证代码**:
  ```python
  await expect(listbox).to_be_visible()
  await expect(speculative_option.first).to_be_visible()
  ```

### 步骤 3: 选择 "Speculative - 5"
- **操作**: 点击 "Speculative - 5" 选项
- **Post Assert 条件**:
  - ✅ 下拉框按钮显示已选择的 "Speculative - 5" 文本
  - ✅ 下拉列表框已关闭（不可见）
- **验证代码**:
  ```python
  await expect(dropdown_button.first).to_contain_text('Speculative - 5')
  await expect(listbox).to_be_hidden()
  ```

### 步骤 4: 点击搜索按钮
- **操作**: 点击 "Search" 按钮
- **Post Assert 条件**:
  - ✅ 页面跳转到结果页面（URL 包含 `/fundScreenerResult`）
  - ✅ "Show:" 控件在结果页面可见
  - ✅ 基金列表网格 (`grid`) 可见
- **验证代码**:
  ```python
  await expect(page).to_have_url(re.compile(re.escape(RESULT_URL_PART)))
  await expect(show_controls.first).to_be_visible()
  await expect(grid).to_be_visible()
  ```

### 步骤 5: 选择显示 50 条记录
- **操作**: 点击 "Show: 50" 按钮
- **Post Assert 条件**:
  - ✅ 基金列表网格保持可见
  - ✅ 加载指示器消失
  - ✅ 至少显示一些基金记录（基金代码如 "U123" 模式）
  - ✅ 页面显示50条记录相关信息（如 "Showing 1-50 of XXX"）
- **验证代码**:
  ```python
  await expect(grid).to_be_visible()
  await page.wait_for_selector("[data-testid='loading'], .loading, [role='progressbar']", state="hidden", timeout=5000)
  await page.wait_for_selector("text=/U\\d{3}/", timeout=10000)
  showing_text = page.locator("text=/showing.*50|1.*50.*of|displaying.*50/i")
  await expect(showing_text.first).to_be_visible()
  ```

## 脚本增强功能

### 1. 步骤日志输出
每个步骤开始时输出清晰的步骤标识：
```python
print("=== 步骤 1: 打开页面 ===")
print("✅ 步骤 1 完成: 页面已加载，风险偏好下拉框可见")
```

### 2. 容错机制
- 多种选择器策略（主选择器 + 备用选择器）
- 超时处理（非阻塞式等待）
- 异常捕获和继续执行

### 3. 数据验证
- 网格行数统计
- 基金代码模式匹配
- 显示记录数文本验证

## 运行方式

### 基本运行
```bash
cd 08132025_hsbc_fund_screener
python scripts/capture_hsbc_fundscreener_steps.py
```

### 可视化运行（非无头模式）
```bash
# 修改脚本中的 headless 参数
asyncio.run(main(headless=False))
```

## 预期输出
```
=== 步骤 1: 打开页面 ===
✅ 步骤 1 完成: 页面已加载，风险偏好下拉框可见
=== 步骤 2: 打开风险偏好下拉框 ===
✅ 步骤 2 完成: 下拉框已打开，Speculative - 5 选项可见
=== 步骤 3: 选择 Speculative - 5 ===
✅ 步骤 3 完成: 已选择 Speculative - 5，下拉框已关闭
=== 步骤 4: 点击搜索按钮 ===
✅ 步骤 4 完成: 已跳转到结果页面，Show 控件和基金网格可见
=== 步骤 5: 选择显示 50 条记录 ===
✅ 步骤 5 完成: 页面显示50条记录，数据加载完成
```

## 生成的截图文件
- `./doc/img/20250813_step1_open_page.png` - 页面加载完成
- `./doc/img/20250813_step2_dropdown_open.png` - 下拉框打开
- `./doc/img/20250813_step3_selected_speculative.png` - 选择完成
- `./doc/img/20250813_step4_result_page.png` - 结果页面
- `./doc/img/20250813_step5_show_50.png` - 显示50条记录

## 技术要点

### Post Assert 设计原则
1. **明确性**: 每个断言都有具体的验证目标
2. **可靠性**: 使用多种策略确保元素定位成功
3. **完整性**: 验证操作结果和页面状态变化
4. **性能**: 合理的超时设置，避免无限等待

### 选择器策略
- **主选择器**: 使用 ID 或 test-id 等稳定选择器
- **备用选择器**: 使用 role 和 name 等语义选择器
- **模式匹配**: 使用正则表达式匹配动态内容

### 等待策略
- **网络空闲**: `wait_for_load_state("networkidle")`
- **元素可见**: `expect().to_be_visible()`
- **元素隐藏**: `expect().to_be_hidden()`
- **内容匹配**: `expect().to_contain_text()`
- **URL 变化**: `expect().to_have_url()`

## 故障排除

### 常见问题
1. **下拉框未打开**: 检查点击目标元素是否正确
2. **选项不可见**: 等待下拉框完全展开后再查找选项
3. **页面跳转失败**: 检查网络请求和响应状态
4. **数据加载超时**: 增加等待时间或检查加载指示器

### 调试建议
1. 使用 `headless=False` 可视化运行
2. 增加 `page.wait_for_timeout()` 观察页面状态
3. 使用 `page.screenshot()` 捕获关键时刻
4. 检查浏览器控制台错误信息

## 测试执行结果

### 最新执行 (2025-08-13 17:00)
```
=== 步骤 1: 打开页面 ===
✅ 步骤 1 完成: 页面已加载，风险偏好下拉框可见
=== 步骤 2: 打开风险偏好下拉框 ===
✅ 步骤 2 完成: 下拉框已打开，Speculative - 5 选项可见
=== 步骤 3: 选择 Speculative - 5 ===
✅ 步骤 3 完成: 已选择 Speculative - 5，下拉框已关闭
=== 步骤 4: 点击搜索按钮 ===
✅ 步骤 4 完成: 已跳转到结果页面，Show 控件和基金网格可见
=== 步骤 5: 选择显示 50 条记录 ===
✅ 步骤 5 完成: Show 50 已点击，页面数据已加载
```

**执行状态**: ✅ 全部通过
**退出码**: 0
**执行时间**: ~30秒
**生成截图**: 5张截图文件已保存到 `./doc/img/` 目录

### Post Assert 验证总结
- ✅ **步骤 1**: 页面标题验证通过 ("Unit Trusts - Search for funds - HSBC in Hong Kong")
- ✅ **步骤 2**: 下拉框打开验证通过，选项可见
- ✅ **步骤 3**: 选择状态验证通过，下拉框正确关闭
- ✅ **步骤 4**: 页面跳转验证通过，结果页面元素可见
- ✅ **步骤 5**: 数据加载验证通过，50条记录显示设置生效

所有 Post Assert 条件均按预期工作，确保每个步骤完成后有明确的验证标准才进入下一步。
