import asyncio
from pathlib import Path
import json
import csv
import random
from datetime import datetime
from typing import List, Dict, Any, Tuple

from playwright.async_api import async_playwright, Request, Response

# Generate timestamp for this run
timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")

# Output paths relative to project root of this submodule
BASE_DIR = Path(__file__).resolve().parents[1]
DATA_DIR = BASE_DIR / f"data_{timestamp}"
DATA_DIR.mkdir(parents=True, exist_ok=True)

# 基金详情页面URL模板
FUND_DETAIL_URL_TEMPLATE = "https://investments3.personal-banking.hsbc.com.hk/public/utb/en-gb/fundDetail/{product_code}"

# 默认测试的产品代码
DEFAULT_PRODUCT_CODE = "U43051"

# 网络请求存储
network_requests: List[Dict[str, Any]] = []
# 必须捕获的8个核心API（完整性校验用）
REQUIRED_APIS = {
    "amh_ut_product",
    "wmds_quoteDetail",
    "wmds_fundQuoteSummary",
    "wmds_holdingAllocation",
    "wmds_topTenHoldings",
    "wmds_otherFundClasses",
    "wmds_fundSearchCriteria",
    "wmds_advanceChart",
}

network_responses: List[Dict[str, Any]] = []

def read_product_codes_csv(csv_file_path: str) -> List[Tuple[str, int]]:
    """读取产品代码CSV文件"""
    product_codes = []
    csv_path = Path(csv_file_path)

    if not csv_path.exists():
        print(f"❌ CSV文件不存在: {csv_file_path}")
        return product_codes

    try:
        with open(csv_path, 'r', encoding='utf-8') as f:
            reader = csv.DictReader(f)
            for row in reader:
                product_code = row.get('Product_Code', '').strip()
                index = int(row.get('Index', 0))
                if product_code:
                    product_codes.append((product_code, index))

        print(f"✅ 成功读取CSV文件: {len(product_codes)}个产品代码")
        return product_codes

    except Exception as e:
        print(f"❌ 读取CSV文件时出错: {e}")
        return product_codes

def should_capture_request(request: Request) -> bool:
    """判断是否应该捕获该请求"""
    url = request.url.lower()
    resource_type = request.resource_type

    # 过滤掉静态资源
    static_extensions = ['.js', '.css', '.png', '.jpg', '.jpeg', '.gif', '.svg', '.ico',
                        '.woff', '.woff2', '.ttf', '.otf', '.eot', '.webp', '.bmp', '.html']

    # 过滤掉静态资源类型
    static_resource_types = ['stylesheet', 'script', 'image', 'font', 'media', 'document']

    # 过滤掉包含静态资源关键词的URL
    static_keywords = ['/css/', '/js/', '/fonts/', '/images/', '/img/', '/static/',
                      'stylesheet', 'javascript', '.min.js', '.min.css', 'bundle.js',
                      'bundle.css', 'jquery', 'bootstrap', 'fontawesome']

    # 过滤掉特定的API端点和第三方服务
    excluded_apis = [
        '/amh/ut/sdktoken',
        'funddetail/',  # 主页面请求
        'logx.optimizely.com',  # Optimizely日志
        'lpcdn-vip.lpsnmedia.net',  # LivePerson客服
        'match.adsrvr.org',  # 广告匹配
        'quotespeed.morningstar.com',  # 晨星服务
        'sp.analytics.yahoo.com',  # Yahoo分析
        'td.doubleclick.net',  # Google DoubleClick
        'www.google.com/ccm',  # Google CCM
        'www.googletagmanager.com',  # Google Tag Manager
        'www.facebook.com/tr',  # Facebook像素
        'visitor-service-ap-northeast-1.tealiumiq.com',  # Tealium访客服务
        'googleads.g.doubleclick.net',  # Google广告
        'google.com/ccm/form-data',  # Google表单数据
        'bat.bing.com',  # Bing广告
        'cm.g.doubleclick.net',  # DoubleClick Cookie匹配
        'connect.facebook.net',  # Facebook连接
        'accdn-vip.lpsnmedia.net',  # LivePerson CDN
        'a19069622224.cdn.optimizely.com'  # Optimizely CDN
    ]

    # 检查文件扩展名
    for ext in static_extensions:
        if ext in url:
            return False

    # 检查资源类型
    if resource_type in static_resource_types:
        return False

    # 检查URL关键词
    for keyword in static_keywords:
        if keyword in url:
            return False

    # 检查排除的API端点和域名
    for api in excluded_apis:
        if api in url:
            return False

    # 只保留HSBC核心API和晨星数据API
    hsbc_api_patterns = [
        'investments3.personal-banking.hsbc.com.hk/shp/wealth-mobile-',
        'quotespeed.morningstar.com/ra/'
    ]

    # 检查是否是我们需要的API
    for pattern in hsbc_api_patterns:
        if pattern in url:
            return True

    # 默认不捕获其他请求
    return False

async def capture_request(request: Request):
    """捕获HTTP请求（过滤静态资源）"""
    try:
        # 过滤静态资源
        if not should_capture_request(request):
            return

        request_data = {
            "timestamp": datetime.now().isoformat(),
            "method": request.method,
            "url": request.url,
            "headers": dict(request.headers),
            "resource_type": request.resource_type,
            "post_data": request.post_data if request.post_data else None,
            "is_navigation_request": request.is_navigation_request()
        }
        network_requests.append(request_data)
        print(f"📤 请求: {request.method} {request.url}")
    except Exception as e:
        print(f"⚠️  捕获请求时出错: {e}")

def should_capture_response(response: Response) -> bool:
    """判断是否应该捕获该响应（与请求过滤逻辑一致）"""
    url = response.url.lower()

    # 过滤掉静态资源
    static_extensions = ['.js', '.css', '.png', '.jpg', '.jpeg', '.gif', '.svg', '.ico',
                        '.woff', '.woff2', '.ttf', '.otf', '.eot', '.webp', '.bmp', '.html']

    # 过滤掉包含静态资源关键词的URL
    static_keywords = ['/css/', '/js/', '/fonts/', '/images/', '/img/', '/static/',
                      'stylesheet', 'javascript', '.min.js', '.min.css', 'bundle.js',
                      'bundle.css', 'jquery', 'bootstrap', 'fontawesome']

    # 过滤掉特定的API端点和第三方服务
    excluded_apis = [
        '/amh/ut/sdktoken',
        'funddetail/',  # 主页面请求
        'logx.optimizely.com',  # Optimizely日志
        'lpcdn-vip.lpsnmedia.net',  # LivePerson客服
        'match.adsrvr.org',  # 广告匹配
        'quotespeed.morningstar.com',  # 晨星服务
        'sp.analytics.yahoo.com',  # Yahoo分析
        'td.doubleclick.net',  # Google DoubleClick
        'www.google.com/ccm',  # Google CCM
        'www.googletagmanager.com',  # Google Tag Manager
        'www.facebook.com/tr',  # Facebook像素
        'visitor-service-ap-northeast-1.tealiumiq.com',  # Tealium访客服务
        'googleads.g.doubleclick.net',  # Google广告
        'google.com/ccm/form-data',  # Google表单数据
        'bat.bing.com',  # Bing广告
        'cm.g.doubleclick.net',  # DoubleClick Cookie匹配
        'connect.facebook.net',  # Facebook连接
        'accdn-vip.lpsnmedia.net',  # LivePerson CDN
        'a19069622224.cdn.optimizely.com'  # Optimizely CDN
    ]

    # 检查文件扩展名
    for ext in static_extensions:
        if ext in url:
            return False

    # 检查URL关键词
    for keyword in static_keywords:
        if keyword in url:
            return False

    # 检查排除的API端点和域名
    for api in excluded_apis:
        if api in url:
            return False

    # 只保留HSBC核心API和晨星数据API
    hsbc_api_patterns = [
        'investments3.personal-banking.hsbc.com.hk/shp/wealth-mobile-',
        'quotespeed.morningstar.com/ra/'
    ]

    # 检查是否是我们需要的API
    for pattern in hsbc_api_patterns:
        if pattern in url:
            return True

    # 默认不捕获其他响应
    return False

async def capture_response(response: Response):
    """捕获HTTP响应（过滤静态资源）"""
    try:
        # 过滤静态资源
        if not should_capture_response(response):
            return

        # 获取响应体（仅对特定类型）
        response_body = None
        content_type = response.headers.get("content-type", "")

        if any(t in content_type.lower() for t in ["json", "text", "xml", "html"]):
            try:
                response_body = await response.text()
            except:
                response_body = None

        response_data = {
            "timestamp": datetime.now().isoformat(),
            "url": response.url,
            "status": response.status,
            "status_text": response.status_text,
            "headers": dict(response.headers),
            "content_type": content_type,
            "response_body": response_body,
            "ok": response.ok,
            "from_service_worker": response.from_service_worker
        }
        network_responses.append(response_data)
        print(f"📥 响应: {response.status} {response.url}")
    except Exception as e:
        print(f"⚠️  捕获响应时出错: {e}")

def extract_api_name(url: str) -> str:
    """从URL中提取API名称"""
    # 查找HSBC API路径
    if '/shp/wealth-mobile-' in url:
        # 提取API路径的最后部分
        parts = url.split('/')
        for i, part in enumerate(parts):
            if part.startswith('v0') and i + 1 < len(parts):
                # 获取v0后面的路径部分，用下划线连接
                api_parts = parts[i+1:]
                # 移除查询参数
                if '?' in api_parts[-1]:
                    api_parts[-1] = api_parts[-1].split('?')[0]
                return '_'.join(api_parts)

    # 对于其他URL，尝试提取有意义的部分
    if 'morningstar.com' in url:
        if '/ra/' in url:
            path_part = url.split('/ra/')[-1].split('?')[0]
            return f"morningstar_{path_part}"

    # 默认情况，使用域名和路径
    try:
        from urllib.parse import urlparse
        parsed = urlparse(url)
        domain = parsed.netloc.split('.')[0]  # 取域名第一部分
        path = parsed.path.strip('/').replace('/', '_')
        if path:
            return f"{domain}_{path}"
        else:
            return domain
    except:
        return "unknown_api"

async def save_network_data(product_code: str, item_number: int, task_requests: List[Dict[str, Any]], task_responses: List[Dict[str, Any]]):
    """保存网络数据到临时目录并原子落盘到最终目录，确保只在完成时生成输出"""

    # 目录: 使用现有的 .NNN_Code.working 目录（由调度器/worker创建）
    working_dir = DATA_DIR / f".{item_number:03d}_{product_code}.working"
    working_dir.mkdir(parents=True, exist_ok=True)
    # 最终目录由外层 complete_work 控制切换为 .done


    # 按API分组保存请求和响应
    api_groups: Dict[str, Dict[str, List[Dict[str, Any]]]] = {}

    for req in task_requests:
        api_name = extract_api_name(req["url"])
        api_groups.setdefault(api_name, {"requests": [], "responses": []})
        api_groups[api_name]["requests"].append(req)

    for resp in task_responses:
        api_name = extract_api_name(resp["url"])
        api_groups.setdefault(api_name, {"requests": [], "responses": []})
        api_groups[api_name]["responses"].append(resp)

    saved_files = []

    # 为每个API保存单独的请求和响应文件（写到工作目录）
    for api_name, data in api_groups.items():
        if data["requests"]:
            request_file = working_dir / f"{api_name}.request.json"
            with open(request_file, 'w', encoding='utf-8') as f:
                json.dump({
                    "api_name": api_name,
                    "product_code": product_code,
                    "timestamp": timestamp,
                    "total_requests": len(data["requests"]),
                    "requests": data["requests"]
                }, f, indent=2, ensure_ascii=False)
            saved_files.append(request_file)

        if data["responses"]:
            response_file = working_dir / f"{api_name}.response.json"
            with open(response_file, 'w', encoding='utf-8') as f:
                json.dump({
                    "api_name": api_name,
                    "product_code": product_code,
                    "timestamp": timestamp,
                    "total_responses": len(data["responses"]),
                    "responses": data["responses"]
                }, f, indent=2, ensure_ascii=False)
            saved_files.append(response_file)

    # 保存汇总信息（写到工作目录）
    summary_file = working_dir / "summary.json"
    with open(summary_file, 'w', encoding='utf-8') as f:
        json.dump({
            "product_code": product_code,
            "timestamp": timestamp,
            "session_dir": str(working_dir),
            "summary": {
                "total_requests": len(task_requests),
                "total_responses": len(task_responses),
                "api_count": len(api_groups),
                "api_names": list(api_groups.keys()),
                "unique_domains": list(set([req["url"].split("/")[2] for req in task_requests if "url" in req])),
                "request_methods": list(set([req["method"] for req in task_requests if "method" in req])),
                "response_status_codes": list(set([resp["status"] for resp in task_responses if "status" in resp]))
            }
        }, f, indent=2, ensure_ascii=False)
    saved_files.append(summary_file)

    # 移动截图文件到工作目录
    temp_screenshot_file = DATA_DIR / f"fund_detail_{product_code}_{timestamp}.png"
    if temp_screenshot_file.exists():
        final_screenshot_file = working_dir / f"fund_detail_{product_code}_{timestamp}.png"
        temp_screenshot_file.rename(final_screenshot_file)
        saved_files.append(final_screenshot_file)
        print(f"📸 截图已移动到工作目录: {final_screenshot_file}")

    # 完整性校验：要求8个核心API全部存在且每个至少有1个响应
    found_api_names = set(api_groups.keys())
    missing = list(REQUIRED_APIS - found_api_names)
    has_all_required = len(missing) == 0 and all(len(api_groups.get(api, {}).get("responses", [])) > 0 for api in REQUIRED_APIS)

    if not has_all_required:
        # 不落盘最终目录，保留工作目录用于诊断，并返回失败标记
        print(f"❌ 完整性校验失败：缺少API {missing} 或某些API无响应。保留工作目录: {working_dir}")
        final_effective = None
        return {
            "session_dir": final_effective,
            "saved_files": saved_files,
            "api_groups": api_groups,
            "complete": False,
            "missing": missing,
        }

    # 在并发队列模式下，不在此处重命名最终目录；由 complete_work 统一切换为 .done
    final_effective = working_dir

    print(f"\n💾 网络数据已写入工作目录: {final_effective}")
    print(f"📊 API分组数量: {len(api_groups)}")
    for api_name in sorted(api_groups.keys()):
        req_count = len(api_groups[api_name]["requests"])
        resp_count = len(api_groups[api_name]["responses"])
        print(f"   📁 {api_name}: {req_count}个请求, {resp_count}个响应")

    return {
        "session_dir": final_effective,
        "saved_files": saved_files,
        "api_groups": api_groups,
        "complete": True
    }

async def main(product_code: str = DEFAULT_PRODUCT_CODE, item_number: int = 1, headless: bool = True):
    """主函数：访问基金详情页面并捕获网络请求"""

    # 为每个任务创建独立的网络请求存储
    task_network_requests: List[Dict[str, Any]] = []
    task_network_responses: List[Dict[str, Any]] = []

    async def capture_request(request: Request):
        """捕获HTTP请求（任务级别）"""
        try:
            # 过滤静态资源
            if not should_capture_request(request):
                return

            request_data = {
                "timestamp": datetime.now().isoformat(),
                "method": request.method,
                "url": request.url,
                "headers": dict(request.headers),
                "resource_type": request.resource_type,
                "post_data": request.post_data if request.post_data else None,
                "is_navigation_request": request.is_navigation_request()
            }
            task_network_requests.append(request_data)
            print(f"📤 [{product_code}] 请求: {request.method} {request.url}")
        except Exception as e:
            print(f"⚠️  [{product_code}] 捕获请求时出错: {e}")

    async def capture_response(response: Response):
        """捕获HTTP响应（任务级别）"""
        try:
            # 过滤静态资源
            if not should_capture_response(response):
                return

            # 获取响应体（仅对特定类型）
            response_body = None
            content_type = response.headers.get("content-type", "")

            if any(t in content_type.lower() for t in ["json", "text", "xml", "html"]):
                try:
                    response_body = await response.text()
                except:
                    response_body = None

            response_data = {
                "timestamp": datetime.now().isoformat(),
                "url": response.url,
                "status": response.status,
                "status_text": response.status_text,
                "headers": dict(response.headers),
                "content_type": content_type,
                "response_body": response_body,
                "ok": response.ok,
                "from_service_worker": response.from_service_worker
            }
            task_network_responses.append(response_data)
            print(f"📥 [{product_code}] 响应: {response.status} {response.url}")
        except Exception as e:
            print(f"⚠️  [{product_code}] 捕获响应时出错: {e}")

    fund_detail_url = FUND_DETAIL_URL_TEMPLATE.format(product_code=product_code)

    print("="*60)
    print(f"🚀 开始捕获基金详情页面网络请求")
    print(f"📋 产品代码: {product_code}")
    print(f"🌐 目标URL: {fund_detail_url}")
    print(f"📁 输出目录: {DATA_DIR}")
    print("="*60)

    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=headless)
        context = await browser.new_context()
        page = await context.new_page()

        # 设置网络监听器（使用任务级别的捕获函数）
        page.on("request", capture_request)
        page.on("response", capture_response)

        print(f"\n=== 步骤 1: 访问基金详情页面 ===")
        print(f"🌐 正在访问: {fund_detail_url}")

        try:
            # 访问基金详情页面
            await page.goto(fund_detail_url, wait_until="domcontentloaded", timeout=60000)

            # 等待网络请求完成
            print("⏳ 等待页面加载完成...")
            try:
                await page.wait_for_load_state("networkidle", timeout=30000)
            except:
                print("⚠️  网络空闲等待超时，继续执行")
                await page.wait_for_timeout(5000)

            # 验证页面加载成功
            page_title = await page.title()
            print(f"📄 页面标题: {page_title}")

            # 检查页面是否包含基金相关内容
            if product_code in page_title or "fund" in page_title.lower() or "基金" in page_title:
                print("✅ 页面加载成功，包含基金相关内容")
            else:
                print("⚠️  页面标题未包含预期的基金信息")

            # 等待额外时间确保所有异步请求完成
            print("⏳ 等待异步请求完成...")
            await page.wait_for_timeout(10000)

            # 拍摄页面截图 - 先创建临时文件，稍后移动到会话目录
            temp_screenshot_file = DATA_DIR / f"fund_detail_{product_code}_{timestamp}.png"
            await page.screenshot(path=str(temp_screenshot_file), full_page=True, scale="css")
            print(f"📸 页面截图已保存: {temp_screenshot_file}")

        except Exception as e:
            print(f"❌ 访问页面时出错: {e}")

        print(f"\n=== 步骤 2: 保存网络数据 ===")
        result = await save_network_data(product_code, item_number, task_network_requests, task_network_responses)

        print(f"\n📊 网络请求统计:")
        print(f"   📤 总请求数: {len(task_network_requests)}")
        print(f"   📥 总响应数: {len(task_network_responses)}")

        if task_network_requests:
            domains = set([req["url"].split("/")[2] for req in task_network_requests if "url" in req])
            methods = set([req["method"] for req in task_network_requests if "method" in req])
            print(f"   🌐 涉及域名: {len(domains)} 个")
            print(f"   🔧 请求方法: {', '.join(methods)}")

        if task_network_responses:
            status_codes = set([resp["status"] for resp in task_network_responses if "status" in resp])
            print(f"   📊 响应状态码: {', '.join(map(str, status_codes))}")

        await context.close()
        await browser.close()

        if result and result.get("complete", True) is False:
            # 标记未完成，抛出异常让上层处理（并发池记录为失败，不产出最终目录）
            missing = result.get("missing")
            raise RuntimeError(f"完整性校验失败，缺少API: {missing}")

        print(f"\n🎯 任务完成！所有数据已保存到: {DATA_DIR}")
        return result

async def process_single_product(product_code: str, item_number: int, headless: bool = True):
    """处理单个产品代码"""
    print(f"\n{'='*60}")
    print(f"🎯 处理产品 #{item_number}: {product_code}")
    print(f"{'='*60}")

    try:
        await main(product_code=product_code, item_number=item_number, headless=headless)
        print(f"✅ 产品 {product_code} 处理完成")
        return True
    except Exception as e:
        print(f"❌ 产品 {product_code} 处理失败: {e}")
        return False

async def initialize_work_queue(products: List[Tuple[str, int]]):
    """初始化工作队列：为所有产品创建 .init 状态目录"""
    print(f"\n🔧 初始化工作队列: {len(products)} 个产品")

    for product_code, item_number in products:
        init_dir = DATA_DIR / f".{item_number:03d}_{product_code}.init"
        init_dir.mkdir(parents=True, exist_ok=True)

        # 创建状态文件
        status_file = init_dir / "status.json"
        with open(status_file, 'w', encoding='utf-8') as f:
            json.dump({
                "product_code": product_code,
                "item_number": item_number,
                "status": "init",
                "created_at": datetime.now().isoformat(),
                "retry_count": 0,
                "max_retries": 3
            }, f, indent=2, ensure_ascii=False)

    print(f"✅ 工作队列初始化完成")

async def claim_next_work() -> Tuple[str, int, Path]:
    """主动获取下一个工作项：原子地将 .init 重命名为 .working"""
    init_dirs = list(DATA_DIR.glob("*.init"))

    for init_dir in init_dirs:
        # 解析目录名获取产品信息
        dir_name = init_dir.name
        if not dir_name.startswith('.') or not dir_name.endswith('.init'):
            continue

        try:
            # 解析 .NNN_ProductCode.init
            base_name = dir_name[1:-5]  # 去掉开头的 . 和结尾的 .init
            parts = base_name.split('_', 1)
            if len(parts) != 2:
                continue
            item_number = int(parts[0])
            product_code = parts[1]

            # 原子重命名为 .working
            working_dir = DATA_DIR / f".{item_number:03d}_{product_code}.working"
            try:
                init_dir.rename(working_dir)
                print(f"🔒 获取工作: #{item_number} {product_code}")
                return product_code, item_number, working_dir
            except FileExistsError:
                # 已被其他worker获取，继续下一个
                continue
        except (ValueError, IndexError):
            # 解析失败，跳过
            continue

    # 没有可用工作
    return None, None, None

async def complete_work(product_code: str, item_number: int, working_dir: Path, success: bool, error: str = None):
    """完成工作：将 .working 重命名为 .done 或 .error"""
    status_file = working_dir / "status.json"

    # 读取当前状态
    try:
        with open(status_file, 'r', encoding='utf-8') as f:
            status = json.load(f)
    except:
        status = {"retry_count": 0, "max_retries": 3}

    if success:
        # 成功：重命名为 .done
        done_dir = DATA_DIR / f"{item_number:03d}_{product_code}.done"
        status.update({
            "status": "done",
            "completed_at": datetime.now().isoformat(),
            "success": True
        })
        with open(status_file, 'w', encoding='utf-8') as f:
            json.dump(status, f, indent=2, ensure_ascii=False)

        try:
            working_dir.rename(done_dir)
            print(f"✅ 完成工作: #{item_number} {product_code}")
        except Exception as e:
            print(f"⚠️ 重命名为done失败: {e}")
    else:
        # 失败：检查重试次数
        retry_count = status.get("retry_count", 0)
        max_retries = status.get("max_retries", 3)

        if retry_count < max_retries:
            # 重试：重命名回 .init
            retry_count += 1
            status.update({
                "status": "init",
                "retry_count": retry_count,
                "last_error": error,
                "last_attempt_at": datetime.now().isoformat()
            })
            with open(status_file, 'w', encoding='utf-8') as f:
                json.dump(status, f, indent=2, ensure_ascii=False)

            init_dir = DATA_DIR / f".{item_number:03d}_{product_code}.init"
            try:
                working_dir.rename(init_dir)
                print(f"🔄 重试工作: #{item_number} {product_code} (第{retry_count}次)")
            except Exception as e:
                print(f"⚠️ 重命名为init失败: {e}")
        else:
            # 超过重试次数：重命名为 .error
            error_dir = DATA_DIR / f"{item_number:03d}_{product_code}.error"
            status.update({
                "status": "error",
                "completed_at": datetime.now().isoformat(),
                "success": False,
                "final_error": error,
                "retry_count": retry_count
            })
            with open(status_file, 'w', encoding='utf-8') as f:
                json.dump(status, f, indent=2, ensure_ascii=False)

            try:
                working_dir.rename(error_dir)
                print(f"❌ 错误工作: #{item_number} {product_code} (重试{retry_count}次后失败)")
            except Exception as e:
                print(f"⚠️ 重命名为error失败: {e}")

async def worker_process(worker_id: int, headless: bool = True):
    """工作进程：主动获取并处理工作项"""
    print(f"🔧 启动工作进程 #{worker_id}")

    while True:
        # 主动获取工作
        product_code, item_number, working_dir = await claim_next_work()

        if product_code is None:
            # 没有可用工作，等待一下再检查
            await asyncio.sleep(1)
            continue

        print(f"🎯 工作进程 #{worker_id} 处理: #{item_number} {product_code}")

        # 处理工作
        success = False
        error = None
        try:
            success = await process_single_product(product_code, item_number, headless)
        except Exception as e:
            error = str(e)
            success = False

        # 完成工作
        await complete_work(product_code, item_number, working_dir, success, error)

async def process_with_pool(products: List[Tuple[str, int]], pool_size: int, headless: bool = True):
    """使用基于文件系统状态的工作队列并发池"""
    total = len(products)
    print(f"\n🚀 工作队列并发池启动: 总计 {total} 个产品, 池大小={pool_size}")

    # 初始化工作队列
    await initialize_work_queue(products)

    # 启动工作进程
    workers = []
    for i in range(pool_size):
        worker = asyncio.create_task(worker_process(i + 1, headless))
        workers.append(worker)

    # 监控进度
    while True:
        # 统计各状态的数量
        init_count = len(list(DATA_DIR.glob("*.init")))
        working_count = len(list(DATA_DIR.glob("*.working")))
        done_count = len(list(DATA_DIR.glob("*.done")))
        error_count = len(list(DATA_DIR.glob("*.error")))

        total_processed = done_count + error_count

        if total_processed >= total:
            # 全部完成，停止工作进程
            for worker in workers:
                worker.cancel()
            break

        # 每10秒报告一次进度
        print(f"📊 进度: ✅{done_count} 完成, ❌{error_count} 错误, 🔄{working_count} 处理中, ⏳{init_count} 等待")
        await asyncio.sleep(10)

    # 等待所有工作进程结束
    await asyncio.gather(*workers, return_exceptions=True)

    # 生成结果
    results = []
    for status_dir in DATA_DIR.glob("*.done"):
        status_file = status_dir / "status.json"
        if status_file.exists():
            with open(status_file, 'r', encoding='utf-8') as f:
                status = json.load(f)
                results.append({
                    "product_code": status["product_code"],
                    "item_number": status["item_number"],
                    "success": True,
                    "session_dir": str(status_dir)
                })

    for status_dir in DATA_DIR.glob("*.error"):
        status_file = status_dir / "status.json"
        if status_file.exists():
            with open(status_file, 'r', encoding='utf-8') as f:
                status = json.load(f)
                results.append({
                    "product_code": status["product_code"],
                    "item_number": status["item_number"],
                    "success": False,
                    "error": status.get("final_error", "Unknown error"),
                    "session_dir": None
                })

    results.sort(key=lambda x: x["item_number"])
    success_count = sum(1 for r in results if r["success"])
    failed_count = total - success_count

    print(f"\n🎯 工作队列处理完成: ✅{success_count} 成功, ❌{failed_count} 失败")
    return results

async def process_batch_concurrent(batch_products: List[Tuple[str, int]], headless: bool = True):
    """并发处理一批产品（例如 10 个），每个任务随机延迟 1-10 秒启动，避免同时并发尖峰"""
    print(f"\n🔄 并发处理批次: {len(batch_products)}个产品 (随机启动 1-10s)")
    for product_code, item_number in batch_products:
        print(f"   📋 #{item_number}: {product_code}")

    async def delayed_start(product_code: str, item_number: int, headless: bool):
        delay = random.randint(1, 10)
        print(f"⏳ 任务预热: #{item_number} {product_code} 将在 {delay}s 后启动")
        await asyncio.sleep(delay)
        return await process_single_product(product_code, item_number, headless)

    # 创建并发任务（带随机延迟）
    tasks = []
    for product_code, item_number in batch_products:
        task = asyncio.create_task(
            delayed_start(product_code, item_number, headless),
            name=f"product_{item_number}_{product_code}"
        )
        tasks.append((task, product_code, item_number))

    # 等待所有任务完成
    results = []
    for task, product_code, item_number in tasks:
        try:
            success = await task
            results.append((product_code, item_number, success))
        except Exception as e:
            print(f"❌ 产品 {product_code} 处理异常: {e}")
            results.append((product_code, item_number, False))

    # 统计结果
    success_count = sum(1 for _, _, success in results if success)
    failed_count = len(results) - success_count

    print(f"🎯 批次处理完成: ✅{success_count}个成功, ❌{failed_count}个失败")
    return results

async def process_csv_file(csv_file_path: str, headless: bool = True, start_index: int = 1, end_index: int = None, pool_size: int = 3):
    """批量处理CSV文件中的产品代码（支持并发）"""
    product_codes = read_product_codes_csv(csv_file_path)

    if not product_codes:
        print("❌ 没有找到有效的产品代码")
        return

    # 过滤索引范围
    if end_index is None:
        end_index = len(product_codes)

    filtered_products = [(code, idx) for code, idx in product_codes if start_index <= idx <= end_index]

    # 兼容旧日志字段所需的变量名（仅用于打印），与并发池大小保持一致
    batch_size = pool_size

    print(f"\n🚀 开始并发池处理:")
    print(f"   📁 CSV文件: {csv_file_path}")
    print(f"   📊 总产品数: {len(product_codes)}")
    print(f"   🎯 处理范围: {start_index} - {end_index}")
    print(f"   📋 实际处理: {len(filtered_products)}个产品")
    print(f"   � 并发批次大小: {batch_size}")
    print(f"   �📁 输出目录: data_{timestamp}")

    # 使用并发池处理
    results = await process_with_pool(filtered_products, pool_size, headless)

    # 生成汇总报告
    success_count = sum(1 for r in results if r["success"])
    failed_count = len(results) - success_count

    report = {
        "timestamp": timestamp,
        "csv_file": csv_file_path,
        "range": {"start_index": start_index, "end_index": end_index},
        "pool_size": pool_size,
        "totals": {"success": success_count, "failed": failed_count},
        "items": results
    }

    report_file = DATA_DIR / "run_report.json"
    with open(report_file, 'w', encoding='utf-8') as f:
        json.dump(report, f, indent=2, ensure_ascii=False)

    print(f"\n📄 运行报告已生成: {report_file}")
    print(f"🎯 全部完成: ✅{success_count}, ❌{failed_count}")

if __name__ == "__main__":
    import sys

    # 检查是否指定了CSV文件
    if len(sys.argv) > 1 and sys.argv[1].endswith('.csv'):
        # CSV批量处理模式
        csv_file_path = sys.argv[1]

        # 获取处理范围参数
        start_index = int(sys.argv[2]) if len(sys.argv) > 2 else 1
        end_index = int(sys.argv[3]) if len(sys.argv) > 3 else None

        # 获取并发池大小参数
        pool_size = int(sys.argv[4]) if len(sys.argv) > 4 and sys.argv[4].isdigit() else 3

        # 获取是否无头模式参数
        headless = True
        if len(sys.argv) > 5 and sys.argv[5].lower() in ['false', 'no', '0']:
            headless = False

        print(f"🎯 CSV并发池处理模式:")
        print(f"   📁 CSV文件: {csv_file_path}")
        print(f"   📊 处理范围: {start_index} - {end_index if end_index else '末尾'}")
        print(f"   🔄 并发池大小: {pool_size}")
        print(f"   🖥️  无头模式: {headless}")

        # 运行并发池批量处理
        asyncio.run(process_csv_file(csv_file_path, headless, start_index, end_index, pool_size))

    else:
        # 单个产品处理模式
        product_code = sys.argv[1] if len(sys.argv) > 1 else DEFAULT_PRODUCT_CODE
        item_number = int(sys.argv[2]) if len(sys.argv) > 2 and sys.argv[2].isdigit() else 1

        # 获取是否无头模式参数
        headless = True
        if len(sys.argv) > 3 and sys.argv[3].lower() in ['false', 'no', '0']:
            headless = False

        print(f"🎯 单个产品处理模式:")
        print(f"   📋 产品代码: {product_code}")
        print(f"   🔢 项目编号: {item_number}")
        print(f"   🖥️  无头模式: {headless}")
        print(f"   📁 输出目录: data_{timestamp}")

        # 运行单个产品处理
        asyncio.run(main(product_code=product_code, item_number=item_number, headless=headless))

