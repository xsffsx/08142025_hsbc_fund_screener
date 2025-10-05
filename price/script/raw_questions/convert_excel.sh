#!/bin/bash
# Excel转Markdown批处理脚本

# 检查参数
if [ $# -eq 0 ]; then
    echo "使用方法: $0 <excel文件路径> [输出目录]"
    echo "示例: $0 'Fund Questions v2.xlsx'"
    echo "示例: $0 'Fund Questions v2.xlsx' ./output"
    exit 1
fi

EXCEL_FILE="$1"
OUTPUT_DIR="$2"

# 检查Excel文件是否存在
if [ ! -f "$EXCEL_FILE" ]; then
    echo "错误: Excel文件不存在: $EXCEL_FILE"
    exit 1
fi

# 检查Python和依赖
echo "检查Python环境..."
if ! command -v python &> /dev/null; then
    echo "错误: 未找到Python"
    exit 1
fi

# 检查依赖包
echo "检查依赖包..."
python -c "import pandas, openpyxl, tabulate" 2>/dev/null
if [ $? -ne 0 ]; then
    echo "安装依赖包..."
    pip install -r requirements.txt
    if [ $? -ne 0 ]; then
        echo "错误: 依赖包安装失败"
        exit 1
    fi
fi

# 运行转换脚本
echo "开始转换Excel文件..."
if [ -n "$OUTPUT_DIR" ]; then
    python excel_to_markdown.py "$EXCEL_FILE" -o "$OUTPUT_DIR"
else
    python excel_to_markdown.py "$EXCEL_FILE"
fi

if [ $? -eq 0 ]; then
    echo "✓ 转换完成!"
else
    echo "✗ 转换失败!"
    exit 1
fi
