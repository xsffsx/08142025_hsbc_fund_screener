# HSBC Fund Screener 自动化验证（Speculative - 5）

创建时间：2025-08-13 10:00:30

## 目录
- 背景与目标
- 验证环境
- 分步操作与元素定位
- 结果验证与证据
- Python Playwright 脚本示例
- Mermaid 流程图（操作步骤）
- 附录：关键网络请求与控制台日志片段

## 背景与目标
- 目标页面：https://investments3.personal-banking.hsbc.com.hk/public/utb/en-gb/fundScreener
- 需求：
  1) 打开页面
  2) 点击“By your risk tolerance”下拉框并选择“Speculative - 5”
  3) 点击“Search”按钮
  4) 通过页面变化与网络请求验证筛选提交成功

## 验证环境
- 工具：Playwright MCP（交互验证与元素抓取）
- 浏览器：Chromium（无痕）
- 语言：Python（脚本示例）

## 分步操作与元素定位
说明：以下定位同时给出“优选选择器 + 备用选择器”，以提高稳定性。

1) 打开页面
- URL: https://investments3.personal-banking.hsbc.com.hk/public/utb/en-gb/fundScreener
- 期望：页面标题包含“Unit Trusts - Search for funds - HSBC in Hong Kong”

2) 展开“By your risk tolerance”下拉
- 优选：page.locator('#dropdown-riskLevel').click()
- 备用：page.getByRole('button', { name: /Selected|By your risk tolerance/i }).click()
- 展开后应出现 Listbox，含各风险选项（Secure - 0 ... Speculative - 5）

3) 选择“Speculative - 5”
- 优选：page.getByRole('option', { name: 'Speculative - 5' }).click()
- 备用：page.getByRole('button', { name: 'Speculative - 5' }).click()
- 选择成功后，下拉收起且按钮文字变为“Speculative - 5”

4) 点击“Search”
- 优选：page.getByRole('button', { name: 'Search' }).click()
- 备用：page.locator('button:has-text("Search")').click()
- 期望：跳转到 /public/utb/en-gb/fundScreenerResult，页面顶部展示“By your risk tolerance: Speculative - 5”，出现结果表格与分页。

从 Playwright MCP 抓取到的关键元素参考：
- 风险下拉开关按钮（展开前）：button[aria-expanded="false"] 与 ID #dropdown-riskLevel（工具实际点击）
- “Speculative - 5”选项：listbox 内的 option/button 文本“Speculative - 5”
- Search 按钮：role=button, name=Search（初始可能 disabled，选择后 enabled）

## 结果验证与证据
- URL 变更：/public/utb/en-gb/fundScreener → /public/utb/en-gb/fundScreenerResult
- 页面状态：顶部“By your risk tolerance: Speculative - 5”；结果区显示“Results 1407 matches”（数字仅为当次抓取的示例）
- 关键网络请求（提交成功佐证）：
  - POST https://investments3.personal-banking.hsbc.com.hk/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundSearchResult  → 200

## Python Playwright 脚本示例
说明：脚本演示稳定选择器与基本断言，适用于 venv 环境。请先安装依赖：pip install playwright && playwright install

```python
import asyncio
from playwright.async_api import async_playwright, expect

TARGET_URL = "https://investments3.personal-banking.hsbc.com.hk/public/utb/en-gb/fundScreener"
RESULT_URL_PART = "/public/utb/en-gb/fundScreenerResult"

async def run():
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)
        context = await browser.new_context(no_viewport=False)
        page = await context.new_page()

        # 1. 打开页面
        await page.goto(TARGET_URL, wait_until="domcontentloaded")
        await expect(page).to_have_title(lambda t: "Unit Trusts" in t)

        # 2. 展开下拉
        toggle = page.locator('#dropdown-riskLevel')
        if await toggle.count() == 0:
            toggle = page.getByRole('button', name=lambda n: 'Selected' in n or 'risk tolerance' in n.lower())
        await toggle.first().click()

        # 3. 选择 "Speculative - 5"
        option = page.getByRole('option', name='Speculative - 5')
        if await option.count() == 0:
            option = page.getByRole('button', name='Speculative - 5')
        await option.first().click()

        # 4. 点击 Search
        search_btn = page.getByRole('button', name='Search')
        await expect(search_btn).to_be_enabled()
        with page.expect_response(lambda r: 'fundSearchResult' in r.url and r.status == 200):
            await search_btn.click()

        # 5. 结果页验证
        await expect(page).to_have_url(lambda u: RESULT_URL_PART in u)
        await expect(page.get_by_text('By your risk tolerance')).to_be_visible()
        await expect(page.get_by_text('Speculative - 5')).to_be_visible()

        await context.close()
        await browser.close()

if __name__ == "__main__":
    asyncio.run(run())
```

## Mermaid 流程图（操作步骤）
说明：样式参考项目标准架构图规范（透明填充、加粗描边、颜色编码、编号使用 #1/#2）。

```mermaid
flowchart LR
  %% 样式设定（参照标准）
  classDef ui fill:transparent,stroke:#af52de,stroke-width:3px,color:#ffffff;
  classDef fast fill:transparent,stroke:#10b981,stroke-width:3px,color:#ffffff;
  classDef ai fill:transparent,stroke:#eab308,stroke-width:3px,color:#ffffff;
  classDef data fill:transparent,stroke:#06b6d4,stroke-width:3px,color:#ffffff;

  A[🧑 UI #1 打开 Fund Screener]:::ui --> B[🧑 UI #2 展开 By your risk tolerance]:::ui
  B --> C[🧑 UI #3 选择 Speculative - 5]:::ui
  C --> D[🧑 UI #4 点击 Search]:::fast
  D --> E[(🗃️ Data 提交 fundSearchResult POST)]:::data
  E --> F[🧑 UI #5 跳转 Result 列表页并显示 1407 matches]:::ui
```

## 附录：关键网络请求与控制台日志片段
- 页面加载：
  - GET /public/utb/en-gb/fundScreener → 200
- 搜索提交：
  - POST /shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundSearchResult → 200
- 结果页：
  - GET /public/utb/en-gb/fundScreenerResult → 200

控制台日志（节选）：
- INFO Slow network is detected ...
- WARNING [Meta Pixel] - Duplicate Pixel ID: 291998267968113

-- 文档结束 --

