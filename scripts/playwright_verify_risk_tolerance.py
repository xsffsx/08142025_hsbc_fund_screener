#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Playwright step-by-step verifier for HSBC fund screener:
1) Open fundScreener
2) Select "Speculative - 5" in "By your risk tolerance"
3) Click Search

Outputs:
- screenshots/step1_open.png, step2_select.png, step3_search.png
- output/playwright_verify_risk_tolerance_log.json
- Optional: generate a Markdown summary file under doc/ (see write_markdown_summary())

Usage (venv recommended):
  python3 scripts/playwright_verify_risk_tolerance.py --headless false
"""

import argparse
import json
import os
import sys
import time
import re
from datetime import datetime
from typing import Dict, Any, Optional

from playwright.sync_api import sync_playwright, TimeoutError as PWTimeoutError

BASE_DIR = os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir))
OUT_DIR = os.path.join(BASE_DIR, "output")
SS_DIR = os.path.join(OUT_DIR, "screenshots")
DOC_DIR = os.path.join(BASE_DIR, "doc")

URL = "https://investments3.personal-banking.hsbc.com.hk/public/utb/en-gb/fundScreener"


def ensure_dirs():
    os.makedirs(OUT_DIR, exist_ok=True)
    os.makedirs(SS_DIR, exist_ok=True)
    os.makedirs(DOC_DIR, exist_ok=True)


def dump_debug(page, name_prefix: str):
    try:
        html_path = os.path.join(OUT_DIR, f"{name_prefix}.html")
        with open(html_path, "w", encoding="utf-8") as f:
            f.write(page.content())
    except Exception:
        pass
    try:
        frames_info = []
        for f in page.frames:
            try:
                frames_info.append({"name": f.name, "url": f.url})
            except Exception:
                continue
        meta_path = os.path.join(OUT_DIR, f"{name_prefix}_frames.json")
        with open(meta_path, "w", encoding="utf-8") as f:
            json.dump(frames_info, f, ensure_ascii=False, indent=2)
    except Exception:
        pass



def now_iso():
    return datetime.now().strftime("%Y-%m-%dT%H:%M:%SZ")


def _take(page, name):
    path = os.path.join(SS_DIR, name)
    page.screenshot(path=path, full_page=True)
    return path


def maybe_accept_cookies(root):
    texts = ["Accept", "I agree", "Allow all", "同意", "接受", "明白"]
    for t in texts:
        try:
            root.get_by_role("button", name=t).click(timeout=2000)
            return True
        except Exception:
            try:
                root.get_by_text(t, exact=False).first.click(timeout=2000)
                return True
            except Exception:
                continue
    return False


def get_root(page):
    """Return a frame/root that contains the search form contents."""
    # First try main page
    try:
        if page.get_by_text("Search for funds", exact=False).count() > 0:
            return page, "main"
    except Exception:
        pass
    # Try frames by heuristic
    for f in page.frames:
        try:
            if f == page.main_frame:
                continue
            if f.get_by_text("Search for funds", exact=False).count() > 0:
                return f, f"frame:{f.url}"
            if f.get_by_role("button", name="Search").count() > 0:
                return f, f"frame:{f.url}"
        except Exception:
            continue
    return page, "fallback-main"


def find_risk_dropdown(root):
    """Try multiple strategies to locate the risk tolerance dropdown/combobox."""
    candidates = []
    try:
        candidates.append(root.get_by_role("combobox", name="By your risk tolerance"))
    except Exception:
        pass
    try:
        candidates.append(root.locator("label:has-text('By your risk tolerance')").locator("xpath=following::select[1]"))
        candidates.append(root.locator("label:has-text('By your risk tolerance')").locator("xpath=following::*[@role='combobox' or @role='listbox'][1]"))
        candidates.append(root.locator("text=By your risk tolerance").locator("xpath=..").locator("select, [role='combobox'], [role='listbox']"))
        # Heuristic: dropdown whose options include 'Speculative - 5'
        candidates.append(root.locator("select:has(option:has-text('Speculative - 5'))"))
        candidates.append(root.locator("[role='combobox']:has-text('Speculative - 5')"))
        # Heuristic: element currently showing default value 'Very cautious - 1'
        candidates.append(root.get_by_text("Very cautious - 1", exact=False))
    except Exception:
        pass

    for idx, loc in enumerate(candidates, start=1):
        try:
            if loc and loc.count() > 0:
                handle = loc.first
                handle.wait_for(state="visible", timeout=8000)
                return handle, f"strategy_{idx}"
        except Exception:
            continue
    return None, None


def open_risk_dropdown(root):
    """Open the 'By your risk tolerance' dropdown by trying multiple strategies."""
    # 1) Container based on label ancestor
    strategies = [
        "xpath=//*[normalize-space(text())='By your risk tolerance']/following::*[contains(@class,'select') or contains(@class,'dropdown') or @role='combobox' or @role='button'][1]",
        "xpath=//*[contains(normalize-space(.),'By your risk tolerance')]/ancestor::*[self::div or self::section][1]//*[contains(@class,'select') or contains(@class,'dropdown') or @role='combobox' or @role='button'][1]",
        # 2) Click current value cell
        "xpath=//*[normalize-space(text())='By your risk tolerance']/following::*[contains(normalize-space(.),'Very cautious - 1') or contains(normalize-space(.),'Speculative - 5')][1]",
    ]
    for s in strategies:
        try:
            el = root.locator(s).first
            el.scroll_into_view_if_needed()
            el.click()
            return s
        except Exception:
            continue
    # 3) Generic: click any element with visible current value
    try:
        root.get_by_text("Very cautious - 1", exact=False).first.click(timeout=2000)
        return "click current value 'Very cautious - 1'"
    except Exception:
        pass
    try:
        root.get_by_text("Speculative - 5", exact=False).first.click(timeout=2000)
        return "click current value 'Speculative - 5'"
    except Exception:
        pass
    return None

# Heuristic utilities for DOM proximity

def near_label_dropdown(root, label_text: str):
    try:
        # Try exact and regex form
        lbl = root.get_by_text(label_text, exact=False).first
    except Exception:
        try:
            lbl = root.locator(r"text=/risk\s*tolerance/i").first
        except Exception:
            return None
    try:
        lbl.scroll_into_view_if_needed()
    except Exception:
        pass
    # Try several sibling patterns
    patterns = [
        "xpath=following::*[self::select][1]",
        "xpath=following::*[@role='combobox' or @role='listbox'][1]",
        "xpath=following::div[contains(@class,'select') or contains(@class,'dropdown') or contains(@class,'combo')][1]",
        "xpath=following::input[1]",
        "xpath=following::button[1]",
    ]
    for p in patterns:
        try:
            loc = lbl.locator(p)
            loc.wait_for(state="visible", timeout=3000)
            return loc
        except Exception:
            continue
    return None



def select_speculative_5(page, dropdown):
    """Try select by standard <select> or custom menu click."""
    # Try native select
    try:
        dropdown.select_option(label="Speculative - 5")
        return "select_option(label)"
    except Exception:
        pass
    try:
        dropdown.select_option(value="5")  # if values are numeric strings
        return "select_option(value=5)"
    except Exception:
        pass
    # Custom widget path: click to open then click option
    try:
        dropdown.click()
        # Try role-based option
        opt = page.get_by_role("option", name="Speculative - 5")
        if opt.count() > 0:
            opt.first.click()
            return "click role=option[name='Speculative - 5']"
    except Exception:
        pass
    try:
        # Fallback: text match (visible)
        page.get_by_text("Speculative - 5", exact=False).first.wait_for(state="visible", timeout=5000)
        page.get_by_text("Speculative - 5", exact=False).first.click()
        return "click text=Speculative - 5"
    except Exception:
        pass
    # Last resort: try to click near the label to open then search again
    try:
        lbl = page.get_by_text("By your risk tolerance", exact=False).first
        lbl.scroll_into_view_if_needed()
        box = lbl.bounding_box()
        if box:
            page.mouse.click(box["x"] + min(300, box["width"] - 5 if box["width"] else 100), box["y"] + 15)
            page.wait_for_timeout(500)
            page.get_by_text("Speculative - 5", exact=False).first.click(timeout=5000)
            return "click near-label + click text"
    except Exception:
        pass
    # XPath contains (may work with offscreen lists)
    try:
        page.locator("xpath=//*[contains(normalize-space(.), 'Speculative - 5')]").first.click()
        return "click xpath contains"
    except Exception:
        pass
    raise RuntimeError("Failed to select 'Speculative - 5'")


def write_markdown_summary(results: Dict[str, Any]):
    ts = datetime.now().strftime("%Y%m%d%H%M%S")
    name = f"{ts}_02_Test_Playwright_Risk_Tolerance_Filter.md"
    path = os.path.join(DOC_DIR, name)
    lines = []
    lines.append(f"# Playwright验证报告 — 风险偏好筛选 (Speculative - 5)")
    lines.append("")
    lines.append(f"创建时间：{datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    lines.append("")
    lines.append("## 步骤与结果")
    for step in results.get("steps", []):
        status = "✅ 成功" if step.get("ok") else "❌ 失败"
        lines.append(f"- {step['id']}. {step['title']} — {status}")
        if step.get("details"):
            lines.append(f"  - 细节: {step['details']}")
        if step.get("screenshot"):
            rel = os.path.relpath(step["screenshot"], start=BASE_DIR)
            lines.append(f"  - 截图: {rel}")
    lines.append("")
    lines.append("## 关键信息")
    lines.append(f"- URL: {results.get('url')}")
    lines.append(f"- 运行时间: {results.get('run_at')}")
    lines.append(f"- 浏览器: chromium, headless={results.get('headless')}")
    with open(path, "w", encoding="utf-8") as f:
        f.write("\n".join(lines) + "\n")
    return path


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--headless", default="true", help="true|false")
    args = parser.parse_args()
    headless = str(args.headless).lower() != "false"

    ensure_dirs()

    results: Dict[str, Any] = {
        "url": URL,
        "run_at": now_iso(),
        "headless": headless,
        "steps": [],
    }

    def record(step_id: str, title: str, ok: bool, details: str = "", screenshot: Optional[str] = None):
        results["steps"].append({
            "id": step_id,
            "title": title,
            "ok": ok,
            "details": details,
            "screenshot": screenshot,
        })

    with sync_playwright() as p:
        browser = p.chromium.launch(headless=headless, args=["--disable-dev-shm-usage"])
        context = browser.new_context(locale="en-GB", viewport={"width": 1400, "height": 900})
        page = context.new_page()

        # Step 1: open page
        try:
            page.goto(URL, wait_until="domcontentloaded", timeout=120000)
            # 可能存在Cookie/Consent，需要尝试点击
            maybe_accept_cookies(page)
            root, root_from = get_root(page)
            # 等待关键元素出现
            try:
                root.get_by_role("button", name="Search").first.wait_for(state="visible", timeout=90000)
            except Exception:
                root.get_by_text("By your risk tolerance", exact=False).first.wait_for(state="visible", timeout=90000)
            ss1 = _take(page, "step1_open.png")
            dump_debug(page, "page_step1")
            record("#1", f"打开 fundScreener 页面（root={root_from})", True, screenshot=ss1)
        except Exception as e:
            record("#1", "打开 fundScreener 页面", False, details=str(e))
            raise

        # Step 2: select risk tolerance
        try:
            dropdown, strat = find_risk_dropdown(root)
            opened = None
            if not dropdown:
                # 扩展1：页面可能隐藏在“Add more filters”抽屉内
                try:
                    root.get_by_role("button", name=re.compile("Add more filters|更多|更多筛选", re.I)).first.click(timeout=3000)
                except Exception:
                    pass
                dropdown, strat = find_risk_dropdown(root)
            if not dropdown:
                # 扩展2：直接尝试打开下拉
                opened = open_risk_dropdown(root)
                if not opened:
                    # 扩展3：基于标签邻接关系查找
                    loc = near_label_dropdown(root, "By your risk tolerance")
                    if loc:
                        dropdown, strat = loc, "near_label_dropdown"
            if not dropdown:
                # 扩展4：滚动至容器区域，尝试显隐渲染
                try:
                    root.get_by_text("Search for funds", exact=False).first.scroll_into_view_if_needed()
                except Exception:
                    pass
                dropdown, strat = find_risk_dropdown(root)
            if not dropdown and not opened:
                dump_debug(page, "page_step2_fail")
                raise RuntimeError("未找到 'By your risk tolerance' 控件")
            details = f"定位策略: {strat if dropdown else 'opened-dropdown'}"
            # 如果已打开直接点选项
            method = None
            try:
                page.get_by_text("Speculative - 5", exact=False).first.click(timeout=5000)
                method = "click text=Speculative - 5 (opened)"
            except Exception:
                if dropdown:
                    method = select_speculative_5(page, dropdown)
                else:
                    raise
            details += f"; 选择方法: {method}"
            ss2 = _take(page, "step2_select.png")
            record("#2", "选择风险偏好=Speculative - 5", True, details=details, screenshot=ss2)
        except Exception as e:
            record("#2", "选择风险偏好=Speculative - 5", False, details=str(e))
            raise

        # Step 3: click Search
        try:
            # Prefer data-testid=Search within main form area to avoid header Quick search
            btn = root.get_by_test_id("Search")
            if btn.count() == 0:
                btn = root.get_by_role("button", name=re.compile(r"^\s*Search\s*$"))
            # Ensure visible and scroll
            el = btn.first
            try:
                el.scroll_into_view_if_needed()
            except Exception:
                pass
            el.click()
            # Wait for results page or in-place update
            try:
                page.wait_for_url("**/fundScreenerResult**", timeout=120000)
            except PWTimeoutError:
                # fallback: wait for a result table/any change in location
                page.wait_for_timeout(3000)
            ss3 = _take(page, "step3_search.png")
            record("#3", "点击 Search 按钮", True, screenshot=ss3)
        except Exception as e:
            record("#3", "点击 Search 按钮", False, details=str(e))
            raise
        finally:
            context.close()
            browser.close()

    # Write JSON log
    log_path = os.path.join(OUT_DIR, "playwright_verify_risk_tolerance_log.json")
    with open(log_path, "w", encoding="utf-8") as f:
        json.dump(results, f, ensure_ascii=False, indent=2)

    # Also write a markdown summary to doc/
    md_path = write_markdown_summary(results)
    print(json.dumps({
        "ok": True,
        "log": os.path.relpath(log_path, start=BASE_DIR),
        "summary_md": os.path.relpath(md_path, start=BASE_DIR),
        "screenshots": [
            os.path.relpath(os.path.join(SS_DIR, n), start=BASE_DIR)
            for n in ("step1_open.png", "step2_select.png", "step3_search.png")
        ]
    }, ensure_ascii=False))


if __name__ == "__main__":
    try:
        main()
    except Exception as e:
        print(json.dumps({"ok": False, "error": str(e)}), file=sys.stderr)
        sys.exit(1)

