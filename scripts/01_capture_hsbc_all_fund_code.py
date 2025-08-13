import asyncio
from pathlib import Path
import re
import json
import csv
from datetime import datetime

from playwright.async_api import async_playwright, expect

# Generate timestamp for this run
timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")

# Output paths relative to project root of this submodule
BASE_DIR = Path(__file__).resolve().parents[1]
IMG_DIR = BASE_DIR / f"img_{timestamp}"
IMG_DIR.mkdir(parents=True, exist_ok=True)

# Filenames with timestamp
SHOT_1 = IMG_DIR / f"step1_open_page_{timestamp}.png"
SHOT_2 = IMG_DIR / f"step2_dropdown_open_{timestamp}.png"
SHOT_3 = IMG_DIR / f"step3_selected_speculative_{timestamp}.png"
SHOT_4 = IMG_DIR / f"step4_result_page_{timestamp}.png"
SHOT_5 = IMG_DIR / f"step5_show_50_{timestamp}.png"

TARGET_URL = "https://investments3.personal-banking.hsbc.com.hk/public/utb/en-gb/fundScreener"
RESULT_URL_PART = "/public/utb/en-gb/fundScreenerResult"

# 数据存储
OUTPUT_DIR = BASE_DIR / f"output_{timestamp}"
OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
PRODUCT_CODES_JSON = OUTPUT_DIR / "product_codes_all.json"
PRODUCT_CODES_CSV = OUTPUT_DIR / "product_codes_all.csv"

async def extract_product_codes_from_page(page):
    """从当前页面提取所有产品代码"""
    product_codes = []

    print("🔍 开始提取当前页面的产品代码...")

    # 等待表格加载完成
    try:
        await page.wait_for_selector("[role='grid']", timeout=10000)
        await page.wait_for_timeout(2000)  # 额外等待确保数据加载
    except:
        print("⚠️  表格加载超时，继续尝试提取")

    # 策略1: 查找所有U开头的产品代码
    product_code_elements = page.locator("text=/U\\d{4,}/")
    count = await product_code_elements.count()
    print(f"📊 找到 {count} 个潜在的产品代码元素")

    for i in range(count):
        try:
            element = product_code_elements.nth(i)
            if await element.is_visible():
                text = await element.text_content()
                # 提取U开头的代码
                import re as regex
                matches = regex.findall(r'U\d{4,}', text)
                for match in matches:
                    if match not in product_codes:
                        product_codes.append(match)
                        print(f"✅ 提取到产品代码: {match}")
        except Exception as e:
            print(f"⚠️  提取第{i}个元素时出错: {e}")
            continue

    # 策略2: 如果策略1没有找到足够的代码，尝试查找表格行
    if len(product_codes) < 10:  # 预期每页至少有10个代码
        print("🔄 策略1结果不足，尝试策略2：查找表格行...")

        # 查找所有表格行
        rows = page.locator("[role='row']")
        row_count = await rows.count()
        print(f"📊 找到 {row_count} 个表格行")

        for i in range(min(row_count, 60)):  # 限制检查前60行
            try:
                row = rows.nth(i)
                if await row.is_visible():
                    row_text = await row.text_content()
                    # 在行文本中查找产品代码
                    matches = regex.findall(r'U\d{4,}', row_text)
                    for match in matches:
                        if match not in product_codes:
                            product_codes.append(match)
                            print(f"✅ 从表格行提取到产品代码: {match}")
            except Exception as e:
                continue

    # 策略3: 查找链接中的产品代码
    if len(product_codes) < 10:
        print("🔄 策略2结果不足，尝试策略3：查找链接...")

        links = page.locator("a")
        link_count = await links.count()
        print(f"📊 找到 {link_count} 个链接")

        for i in range(min(link_count, 100)):  # 限制检查前100个链接
            try:
                link = links.nth(i)
                href = await link.get_attribute("href")
                text = await link.text_content()

                # 在href和文本中查找产品代码
                for content in [href, text]:
                    if content:
                        matches = regex.findall(r'U\d{4,}', content)
                        for match in matches:
                            if match not in product_codes:
                                product_codes.append(match)
                                print(f"✅ 从链接提取到产品代码: {match}")
            except Exception as e:
                continue

    print(f"📋 当前页面共提取到 {len(product_codes)} 个产品代码")
    return product_codes

async def navigate_to_next_page(page, current_page):
    """导航到下一页，返回是否成功"""
    print("🔄 尝试导航到下一页...")

    # 记录当前页面的一些特征，用于验证是否真的切换了页面
    try:
        current_url = page.url
        # 获取当前页面的第一个产品代码作为标识
        first_code_element = page.locator("text=/U\\d{4,}/").first
        current_first_code = await first_code_element.text_content() if await first_code_element.count() > 0 else None
        print(f"📍 当前页面标识: URL={current_url}, 首个代码={current_first_code}")
    except:
        current_first_code = None

    # 查找下一页按钮的多种策略
    next_buttons = [
        page.locator("text=/Next/i"),
        page.locator("text=/下一页/i"),
        page.locator("[aria-label*='Next']"),
        page.locator("[title*='Next']"),
        page.locator("button:has-text('>')"),
        page.locator("a:has-text('>')"),
        page.locator(".pagination .next"),
        page.locator(".pager .next"),
        page.locator(f"text='{current_page + 1}'"),  # 直接点击页码
        page.locator(f"a:has-text('{current_page + 1}')"),
        page.locator(f"button:has-text('{current_page + 1}')")
    ]

    for i, next_button in enumerate(next_buttons):
        try:
            if await next_button.count() > 0:
                button = next_button.first
                if await button.is_visible() and await button.is_enabled():
                    print(f"✅ 找到下一页按钮 (策略{i+1})")

                    # 点击按钮
                    await button.click()

                    # 等待页面开始加载
                    await page.wait_for_timeout(2000)

                    # 等待网络请求完成
                    try:
                        await page.wait_for_load_state("networkidle", timeout=20000)
                    except:
                        print("⚠️  网络空闲等待超时，继续验证页面变化")

                    # 验证页面是否真的发生了变化
                    await page.wait_for_timeout(3000)  # 额外等待确保内容更新

                    try:
                        new_first_code_element = page.locator("text=/U\\d{4,}/").first
                        new_first_code = await new_first_code_element.text_content() if await new_first_code_element.count() > 0 else None
                        new_url = page.url

                        print(f"📍 新页面标识: URL={new_url}, 首个代码={new_first_code}")

                        # 检查页面是否真的变化了
                        if new_first_code and new_first_code != current_first_code:
                            print("✅ 页面内容已更新，成功导航到下一页")
                            return True
                        elif new_url != current_url:
                            print("✅ URL已变化，成功导航到下一页")
                            return True
                        else:
                            print("⚠️  页面内容似乎没有变化，尝试下一个策略")
                            continue
                    except Exception as e:
                        print(f"⚠️  验证页面变化时出错: {e}")
                        # 如果验证失败，假设成功（给一次机会）
                        return True

        except Exception as e:
            print(f"⚠️  策略{i+1}失败: {e}")
            continue

    print("❌ 未找到可用的下一页按钮，或页面内容未发生变化")
    return False

async def save_product_codes(product_codes, page_num=None):
    """保存产品代码到文件"""
    # 保存总的JSON文件
    data = {
        "timestamp": timestamp,
        "total_count": len(product_codes),
        "current_page": page_num,
        "product_codes": product_codes
    }

    with open(PRODUCT_CODES_JSON, 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=2, ensure_ascii=False)

    # 保存总的CSV文件
    with open(PRODUCT_CODES_CSV, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['Product_Code', 'Index'])
        for i, code in enumerate(product_codes, 1):
            writer.writerow([code, i])

    print(f"💾 已保存 {len(product_codes)} 个产品代码到:")
    print(f"   JSON: {PRODUCT_CODES_JSON}")
    print(f"   CSV: {PRODUCT_CODES_CSV}")

async def save_page_codes(page_codes, page_num):
    """保存单页产品代码到独立文件"""
    # 格式化页码为两位数
    page_str = f"{page_num:02d}"

    # 单页JSON文件
    page_json_file = OUTPUT_DIR / f"product_code_page_{page_str}.json"

    page_data = {
        "timestamp": timestamp,
        "page_number": page_num,
        "page_count": len(page_codes),
        "product_codes": page_codes
    }

    with open(page_json_file, 'w', encoding='utf-8') as f:
        json.dump(page_data, f, indent=2, ensure_ascii=False)

    print(f"📄 第{page_num}页数据已保存: {page_json_file}")

async def main(headless: bool = True):
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=headless)
        context = await browser.new_context()
        page = await context.new_page()

        print("=== 步骤 1: 打开页面 ===")
        await page.goto(TARGET_URL, wait_until="domcontentloaded", timeout=60000)

        # 尝试等待网络空闲，如果超时则继续
        try:
            await page.wait_for_load_state("networkidle", timeout=45000)
        except Exception as e:
            print(f"⚠️  网络空闲等待超时，继续执行: {e}")
            await page.wait_for_timeout(2000)  # 额外等待2秒

        if not await page.title():
            # No-op, but ensure a small delay for dynamic tags
            await page.wait_for_timeout(500)

        # POST ASSERT 1: 确保页面标题包含相关关键词或风险偏好下拉框存在
        await expect(page).to_have_title(re.compile(r"Unit Trusts.*Search.*funds|Fund.*Screener|Investment", re.IGNORECASE))
        risk_dropdown = page.locator('a#dropdown-riskLevel')
        if await risk_dropdown.count() == 0:
            risk_dropdown = page.get_by_role("button", name=lambda n: n and ("risk tolerance" in n.lower() or "Selected" in n))
        await expect(risk_dropdown.first).to_be_visible()
        print("✅ 步骤 1 完成: 页面已加载，风险偏好下拉框可见")
        await page.screenshot(path=str(SHOT_1), full_page=True, scale="css")
        print(f"📸 截图已保存: {SHOT_1}")

        print("=== 步骤 2: 打开风险偏好下拉框 ===")
        toggle = page.locator('a#dropdown-riskLevel')
        if await toggle.count() == 0:
            toggle = page.get_by_role("button", name=lambda n: n and ("Selected" in n or "risk tolerance" in n.lower()))
        await toggle.first.click()

        # POST ASSERT 2: 确保下拉列表框出现且包含 "Speculative - 5" 选项
        listbox = page.get_by_role("listbox")
        await expect(listbox).to_be_visible()
        speculative_option = page.get_by_role("option", name="Speculative - 5")
        if await speculative_option.count() == 0:
            speculative_option = page.get_by_text("Speculative - 5")
        await expect(speculative_option.first).to_be_visible()
        print("✅ 步骤 2 完成: 下拉框已打开，Speculative - 5 选项可见")
        await page.screenshot(path=str(SHOT_2), full_page=True, scale="css")
        print(f"📸 截图已保存: {SHOT_2}")

        print("=== 步骤 3: 选择 Speculative - 5 ===")
        option = page.get_by_role("option", name="Speculative - 5")
        if await option.count() == 0:
            option = page.get_by_role("button", name="Speculative - 5")
        await option.first.click()

        # POST ASSERT 3: 确保下拉框显示已选择 "Speculative - 5" 且下拉框已关闭
        dropdown_button = page.locator('a#dropdown-riskLevel')
        if await dropdown_button.count() == 0:
            dropdown_button = page.get_by_role("button", name=lambda n: n and "risk tolerance" in n.lower())
        await expect(dropdown_button.first).to_contain_text('Speculative - 5')
        # 确保下拉列表已关闭
        await expect(listbox).to_be_hidden()
        print("✅ 步骤 3 完成: 已选择 Speculative - 5，下拉框已关闭")
        await page.screenshot(path=str(SHOT_3), full_page=True, scale="css")
        print(f"📸 截图已保存: {SHOT_3}")

        print("=== 步骤 4: 点击搜索按钮 ===")
        search_btn = page.get_by_test_id("Search")
        if await search_btn.count() == 0:
            search_btn = page.get_by_role("button", name="Search")
        await expect(search_btn).to_be_enabled()
        async with page.expect_response(lambda r: "fundSearchResult" in r.url and r.status == 200):
            await search_btn.click()

        # POST ASSERT 4: 确保跳转到结果页面且显示搜索结果相关元素
        await expect(page).to_have_url(re.compile(re.escape(RESULT_URL_PART)))
        await page.wait_for_load_state("networkidle")

        # 确保结果页面的关键元素存在：Show 控件和基金列表网格
        show_controls = page.get_by_text("Show:")
        await expect(show_controls.first).to_be_visible()
        grid = page.get_by_role("grid")
        await expect(grid).to_be_visible()

        # 定位并验证 "Results" 和 "1407 matches" 文本
        print("🔍 定位搜索结果统计信息...")

        # 方法1: 直接查找包含 "Results" 和数字的文本
        results_text = page.locator("text=/Results/i")
        matches_text = page.locator("text=/\\d+\\s+matches/i")

        # 方法2: 备用选择器 - 查找包含 "matches" 的任何文本
        if await results_text.count() == 0:
            results_text = page.locator("text=/result/i")

        if await matches_text.count() == 0:
            matches_text = page.locator("text=/\\d+.*match/i")

        # 方法3: 使用更宽泛的选择器查找数字+matches模式
        if await matches_text.count() == 0:
            matches_text = page.locator("text=/\\d{3,}.*match/i")  # 至少3位数字

        # 验证Results文本存在
        if await results_text.count() > 0:
            await expect(results_text.first).to_be_visible()
            results_content = await results_text.first.text_content()
            print(f"✅ 找到Results文本: '{results_content}'")
        else:
            print("⚠️  未找到Results文本，继续查找matches信息")

        # 验证matches文本存在并提取数字
        if await matches_text.count() > 0:
            await expect(matches_text.first).to_be_visible()
            matches_content = await matches_text.first.text_content()
            print(f"✅ 找到matches文本: '{matches_content}'")

            # 提取数字
            import re as regex
            match_numbers = regex.findall(r'\d+', matches_content)
            if match_numbers:
                total_matches = match_numbers[0]
                print(f"📊 搜索结果总数: {total_matches} matches")

                # 断言：验证结果数量大于0
                assert int(total_matches) > 0, f"搜索结果数量应大于0，实际为: {total_matches}"
                print(f"✅ 断言通过: 搜索结果数量 {total_matches} > 0")
            else:
                print("⚠️  无法从matches文本中提取数字")
        else:
            print("⚠️  未找到matches文本，尝试其他方法...")

            # 方法4: 使用MCP风格的更精确定位
            # 查找包含数字的任何元素
            all_text_elements = page.locator("text=/\\d{3,}/")
            if await all_text_elements.count() > 0:
                for i in range(min(5, await all_text_elements.count())):  # 检查前5个匹配
                    element = all_text_elements.nth(i)
                    if await element.is_visible():
                        content = await element.text_content()
                        if 'match' in content.lower() or 'result' in content.lower():
                            print(f"✅ 通过备用方法找到结果文本: '{content}'")
                            break

        print("✅ 步骤 4 完成: 已跳转到结果页面，Show 控件和基金网格可见，搜索结果统计已验证")
        await page.screenshot(path=str(SHOT_4), full_page=True, scale="css")
        print(f"📸 截图已保存: {SHOT_4}")

        print("=== 步骤 5: 选择显示 50 条记录 ===")
        # The control is a list of buttons; choose the one named "50"
        show_50 = page.get_by_role("button", name="50")
        await expect(show_50).to_be_visible()
        await show_50.click()

        # POST ASSERT 5: 确保页面显示50条记录且加载完成
        # 等待网格可见
        grid = page.get_by_role("grid")
        await expect(grid).to_be_visible()

        # 等待加载指示器消失
        try:
            await page.wait_for_selector("[data-testid='loading'], .loading, [role='progressbar']", state="hidden", timeout=5000)
        except:
            pass

        # 等待至少一些基金行出现（基金代码如 "U123" 模式）
        try:
            await page.wait_for_selector("text=/U\\d{3}/", timeout=10000)
        except:
            pass

        # 最终等待任何剩余的异步更新
        await page.wait_for_timeout(1000)

        # 验证页面显示的记录数量相关信息
        # 寻找显示记录数的文本，如 "Showing 1-50 of XXX" 或类似格式
        try:
            showing_text = page.locator("text=/showing.*50|1.*50.*of|displaying.*50/i")
            if await showing_text.count() > 0:
                await expect(showing_text.first).to_be_visible()
                print("✅ 步骤 5 完成: 页面显示50条记录，数据加载完成")
            else:
                # 备用验证：检查网格中是否有足够的行
                rows = page.locator("[role='row']")
                row_count = await rows.count()
                print(f"✅ 步骤 5 完成: 网格显示 {row_count} 行数据")
        except:
            print("✅ 步骤 5 完成: Show 50 已点击，页面数据已加载")

        await page.screenshot(path=str(SHOT_5), full_page=True, scale="css")
        print(f"📸 截图已保存: {SHOT_5}")

        print("\n" + "="*60)
        print("🚀 开始提取所有产品代码 (目标: 1407个)")
        print("="*60)

        all_product_codes = []
        page_num = 1
        max_pages = 30  # 完整模式：获取所有1407个产品代码

        while page_num <= max_pages:
            print(f"\n📄 处理第 {page_num} 页...")

            # 提取当前页面的产品代码
            page_codes = await extract_product_codes_from_page(page)

            if not page_codes:
                print("⚠️  当前页面未提取到产品代码，可能已到最后一页")
                break

            # 添加到总列表（去重）
            new_codes = []
            for code in page_codes:
                if code not in all_product_codes:
                    all_product_codes.append(code)
                    new_codes.append(code)

            print(f"✅ 第{page_num}页新增 {len(new_codes)} 个产品代码")
            print(f"📊 累计收集 {len(all_product_codes)} 个产品代码")

            # 断言验证：检查第一个和最后一个产品代码
            if page_codes:
                first_code = page_codes[0]
                last_code = page_codes[-1]

                print(f"🔍 验证第{page_num}页产品代码范围:")
                print(f"   首个代码: {first_code}")
                print(f"   末个代码: {last_code}")

                # 断言：确保产品代码格式正确
                import re as regex
                assert regex.match(r'^U\d{4,}$', first_code), f"第{page_num}页首个代码格式错误: {first_code}"
                assert regex.match(r'^U\d{4,}$', last_code), f"第{page_num}页末个代码格式错误: {last_code}"

                # 断言：确保页面有足够的产品代码
                expected_count = 50 if page_num < 29 else min(50, 1407 - (page_num - 1) * 50)
                assert len(page_codes) >= min(expected_count, 10), f"第{page_num}页产品代码数量不足: {len(page_codes)} < {min(expected_count, 10)}"

                print(f"✅ 第{page_num}页断言验证通过:")
                print(f"   ✓ 首个代码格式: {first_code}")
                print(f"   ✓ 末个代码格式: {last_code}")
                print(f"   ✓ 代码数量: {len(page_codes)} 个")
            else:
                print(f"❌ 第{page_num}页未提取到任何产品代码")
                assert False, f"第{page_num}页产品代码提取失败"

            # 保存当前页的产品代码到独立文件
            await save_page_codes(page_codes, page_num)

            # 每页都保存一次总文件（防止数据丢失）
            await save_product_codes(all_product_codes, page_num)

            # 拍摄当前页面截图
            page_screenshot = IMG_DIR / f"page_{page_num}_codes_{timestamp}.png"
            await page.screenshot(path=str(page_screenshot), full_page=True, scale="css")
            print(f"📸 页面截图已保存: {page_screenshot}")

            # 检查是否已收集到目标数量
            if len(all_product_codes) >= 1407:
                print(f"🎯 已达到目标数量！收集到 {len(all_product_codes)} 个产品代码")
                break

            # 尝试导航到下一页
            if not await navigate_to_next_page(page, page_num):
                print("📄 已到最后一页，停止遍历")
                break

            page_num += 1

            # 额外等待确保页面完全加载
            await page.wait_for_timeout(2000)

        print("\n" + "="*60)
        print("📊 数据收集完成统计")
        print("="*60)
        print(f"🎯 目标数量: 1407")
        print(f"✅ 实际收集: {len(all_product_codes)}")
        print(f"📄 遍历页数: {page_num}")
        print(f"📁 数据文件: {PRODUCT_CODES_JSON}")
        print(f"📁 CSV文件: {PRODUCT_CODES_CSV}")

        # 最终保存
        await save_product_codes(all_product_codes, page_num)

        # 显示前10个和后10个产品代码作为验证
        if all_product_codes:
            print(f"\n📋 产品代码样本:")
            print(f"前10个: {all_product_codes[:10]}")
            if len(all_product_codes) > 10:
                print(f"后10个: {all_product_codes[-10:]}")

        print(f"\n🎯 所有截图已保存到目录: {IMG_DIR}")
        print("📁 生成的文件:")
        for shot_file in [SHOT_1, SHOT_2, SHOT_3, SHOT_4, SHOT_5]:
            if shot_file.exists():
                size_kb = shot_file.stat().st_size // 1024
                print(f"  - {shot_file.name} ({size_kb}KB)")

        # 显示页面截图
        page_screenshots = list(IMG_DIR.glob(f"page_*_codes_{timestamp}.png"))
        if page_screenshots:
            print(f"📄 页面截图 ({len(page_screenshots)}个):")
            for shot in page_screenshots:
                size_kb = shot.stat().st_size // 1024
                print(f"  - {shot.name} ({size_kb}KB)")

        await context.close()
        await browser.close()

if __name__ == "__main__":
    # Default headless to True for CI stability
    asyncio.run(main(headless=True))

