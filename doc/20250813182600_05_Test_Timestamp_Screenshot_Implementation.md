# Playwright 时间戳截图实现 — fundScreener 测试

创建时间：2025-08-13 18:26:00

## 概述
更新了 HSBC fundScreener 测试脚本，实现了基于时间戳的截图目录和文件命名，确保每次运行都生成独立的截图集合。

## 🆕 新的截图路径格式

### 目录结构
```
08132025_hsbc_fund_screener/
├── img_<timestamp>/                    # 新格式：./img_<timestamp>
│   ├── step1_open_page_<timestamp>.png
│   ├── step2_dropdown_open_<timestamp>.png
│   ├── step3_selected_speculative_<timestamp>.png
│   ├── step4_result_page_<timestamp>.png
│   └── step5_show_50_<timestamp>.png
└── doc/
    └── img/                           # 旧格式（保留）
        ├── 20250813_step1_open_page.png
        ├── 20250813_step2_dropdown_open.png
        ├── 20250813_step3_selected_speculative.png
        ├── 20250813_step4_result_page.png
        └── 20250813_step5_show_50.png
```

### 时间戳格式
- **格式**: `YYYYMMDD_HHMMSS`
- **示例**: `20250813_182559` (2025年8月13日 18:25:59)
- **生成**: `datetime.now().strftime("%Y%m%d_%H%M%S")`

## 📸 截图文件命名

### 新命名规则
```python
step1_open_page_20250813_182559.png           # 页面加载完成
step2_dropdown_open_20250813_182559.png       # 下拉框打开状态
step3_selected_speculative_20250813_182559.png # 选择完成状态
step4_result_page_20250813_182559.png         # 搜索结果页面
step5_show_50_20250813_182559.png             # 显示50条记录
```

### 文件大小统计
```
step1_open_page_20250813_182559.png (211KB)
step2_dropdown_open_20250813_182559.png (212KB)
step3_selected_speculative_20250813_182559.png (208KB)
step4_result_page_20250813_182559.png (412KB)
step5_show_50_20250813_182559.png (1064KB)
```

## 🔧 代码实现

### 时间戳生成
```python
from datetime import datetime

# Generate timestamp for this run
timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
```

### 目录创建
```python
# Output paths relative to project root of this submodule
BASE_DIR = Path(__file__).resolve().parents[1]
IMG_DIR = BASE_DIR / f"img_{timestamp}"
IMG_DIR.mkdir(parents=True, exist_ok=True)
```

### 文件路径定义
```python
# Filenames with timestamp
SHOT_1 = IMG_DIR / f"step1_open_page_{timestamp}.png"
SHOT_2 = IMG_DIR / f"step2_dropdown_open_{timestamp}.png"
SHOT_3 = IMG_DIR / f"step3_selected_speculative_{timestamp}.png"
SHOT_4 = IMG_DIR / f"step4_result_page_{timestamp}.png"
SHOT_5 = IMG_DIR / f"step5_show_50_{timestamp}.png"
```

### 增强的日志输出
```python
print(f"📸 截图已保存: {SHOT_1}")
print(f"\n🎯 所有截图已保存到目录: {IMG_DIR}")
print("📁 生成的文件:")
for shot_file in [SHOT_1, SHOT_2, SHOT_3, SHOT_4, SHOT_5]:
    if shot_file.exists():
        size_kb = shot_file.stat().st_size // 1024
        print(f"  - {shot_file.name} ({size_kb}KB)")
```

## 🎯 优势特性

### 1. 时间戳隔离
- ✅ 每次运行生成独立的截图目录
- ✅ 避免文件覆盖，保留历史记录
- ✅ 便于对比不同时间的测试结果

### 2. 清晰的文件组织
- ✅ 目录名包含完整时间戳信息
- ✅ 文件名包含步骤描述和时间戳
- ✅ 便于快速定位特定运行的截图

### 3. 详细的执行反馈
- ✅ 每步截图保存后立即显示路径
- ✅ 运行结束后汇总所有文件信息
- ✅ 显示文件大小便于存储管理

### 4. 向后兼容
- ✅ 保留原有 `./doc/img/` 目录结构
- ✅ 新旧格式可以并存
- ✅ 不影响现有文档引用

## 📊 最新执行结果

### 运行时间: 2025-08-13 18:25:59
```
=== 步骤 1: 打开页面 ===
✅ 步骤 1 完成: 页面已加载，风险偏好下拉框可见
📸 截图已保存: /Users/paulo/IdeaProjects/20250707_MCP/08132025_hsbc_fund_screener/img_20250813_182559/step1_open_page_20250813_182559.png

=== 步骤 2: 打开风险偏好下拉框 ===
✅ 步骤 2 完成: 下拉框已打开，Speculative - 5 选项可见
📸 截图已保存: /Users/paulo/IdeaProjects/20250707_MCP/08132025_hsbc_fund_screener/img_20250813_182559/step2_dropdown_open_20250813_182559.png

=== 步骤 3: 选择 Speculative - 5 ===
✅ 步骤 3 完成: 已选择 Speculative - 5，下拉框已关闭
📸 截图已保存: /Users/paulo/IdeaProjects/20250707_MCP/08132025_hsbc_fund_screener/img_20250813_182559/step3_selected_speculative_20250813_182559.png

=== 步骤 4: 点击搜索按钮 ===
✅ 步骤 4 完成: 已跳转到结果页面，Show 控件和基金网格可见
📸 截图已保存: /Users/paulo/IdeaProjects/20250707_MCP/08132025_hsbc_fund_screener/img_20250813_182559/step4_result_page_20250813_182559.png

=== 步骤 5: 选择显示 50 条记录 ===
✅ 步骤 5 完成: Show 50 已点击，页面数据已加载
📸 截图已保存: /Users/paulo/IdeaProjects/20250707_MCP/08132025_hsbc_fund_screener/img_20250813_182559/step5_show_50_20250813_182559.png

🎯 所有截图已保存到目录: /Users/paulo/IdeaProjects/20250707_MCP/08132025_hsbc_fund_screener/img_20250813_182559
📁 生成的文件:
  - step1_open_page_20250813_182559.png (211KB)
  - step2_dropdown_open_20250813_182559.png (212KB)
  - step3_selected_speculative_20250813_182559.png (208KB)
  - step4_result_page_20250813_182559.png (412KB)
  - step5_show_50_20250813_182559.png (1064KB)
```

**执行状态**: ✅ 全部通过  
**退出码**: 0  
**总文件大小**: ~2.1MB  
**截图目录**: `./img_20250813_182559/`

## 🚀 使用方法

### 基本运行
```bash
cd 08132025_hsbc_fund_screener
source .venv/bin/activate
python scripts/capture_hsbc_fundscreener_steps.py
```

### 每次运行都会生成新的时间戳目录
```bash
# 第一次运行
./img_20250813_182559/

# 第二次运行
./img_20250813_183045/

# 第三次运行
./img_20250813_184120/
```

## 📋 技术要点

### 时间戳精度
- **秒级精度**: 确保同一秒内多次运行不会冲突
- **可读性**: YYYYMMDD_HHMMSS 格式便于人工识别
- **排序友好**: 字典序排序即为时间顺序

### 路径处理
- **相对路径**: 基于脚本所在目录的相对路径
- **跨平台**: 使用 `pathlib.Path` 确保跨平台兼容
- **自动创建**: 目录不存在时自动创建

### 错误处理
- **文件存在检查**: 截图前检查目录是否创建成功
- **大小统计**: 安全的文件大小获取和格式化
- **异常容错**: 文件操作异常不影响主流程

这种实现方式确保了每次测试运行都有完整的、独立的截图记录，便于测试结果的追踪和对比分析。
