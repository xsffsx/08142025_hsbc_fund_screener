#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Excel转Markdown工具
将Excel文件的所有工作表转换为Markdown格式

依赖包:
pip install pandas openpyxl

使用方法:
python excel_to_markdown.py "Fund Questions v2.xlsx"
"""

import pandas as pd
import os
import sys
from datetime import datetime
import argparse
import re

# 全局变量记录脱敏操作
desensitization_log = []


def sanitize_filename(filename):
    """清理文件名，移除不合法字符"""
    invalid_chars = '<>:"/\\|?*'
    for char in invalid_chars:
        filename = filename.replace(char, '_')
    return filename


def clean_cell_content(text):
    """
    清理单元格内容，处理长文本和换行问题，并进行数据脱敏
    """
    if not text or text == 'nan':
        return ""

    # 转换为字符串
    text = str(text)

    # 数据脱敏处理
    text = apply_desensitization(text)

    # 替换换行符为空格
    text = re.sub(r'\n+', ' ', text)
    text = re.sub(r'\r+', ' ', text)

    # 移除多余的空格
    text = re.sub(r'\s+', ' ', text)

    # 清理首尾空格
    text = text.strip()

    # 如果文本太长，进行适当截断
    max_length = 100
    if len(text) > max_length:
        # 在合适的位置截断
        text = text[:max_length-3] + "..."

    # 转义Markdown特殊字符
    text = text.replace('|', '\\|')

    return text


def apply_desensitization(text):
    """
    应用数据脱敏规则
    """
    if not text:
        return text

    original_text = text

    # 脱敏规则映射
    desensitization_rules = {
        # 银行名称脱敏
        r'\bHSBC\b': 'hhhh',
        r'\bhsbc\b': 'hhhh',
        r'\bHsbc\b': 'hhhh',

        # 可以添加更多脱敏规则
        # r'\bDBS\b': 'dddd',
        # r'\bUOB\b': 'uuuu',
    }

    # 应用脱敏规则
    for pattern, replacement in desensitization_rules.items():
        if re.search(pattern, text, flags=re.IGNORECASE):
            # 记录脱敏操作
            matches = re.findall(pattern, text, flags=re.IGNORECASE)
            for match in matches:
                if match not in [log['original'] for log in desensitization_log]:
                    desensitization_log.append({
                        'original': match,
                        'replacement': replacement,
                        'type': '银行名称脱敏'
                    })

        text = re.sub(pattern, replacement, text, flags=re.IGNORECASE)

    return text


def create_clean_markdown_table(df):
    """
    创建清洁的Markdown表格
    """
    if df.empty:
        return "*该工作表没有数据*\n"

    # 创建表头
    headers = [str(col).replace('|', '\\|') for col in df.columns]
    header_row = "| " + " | ".join(headers) + " |"
    separator_row = "|" + "|".join([" --- " for _ in headers]) + "|"

    table_lines = [header_row, separator_row]

    # 创建数据行
    for _, row in df.iterrows():
        row_data = []
        for col in df.columns:
            cell_value = str(row[col]) if pd.notna(row[col]) else ""
            row_data.append(cell_value)

        data_row = "| " + " | ".join(row_data) + " |"
        table_lines.append(data_row)

    return "\n".join(table_lines).strip()


def excel_to_markdown(excel_file_path, output_dir=None):
    """
    将Excel文件的所有工作表转换为Markdown格式
    
    Args:
        excel_file_path (str): Excel文件路径
        output_dir (str): 输出目录，默认为Excel文件所在目录
    
    Returns:
        list: 生成的Markdown文件路径列表
    """
    if not os.path.exists(excel_file_path):
        raise FileNotFoundError(f"Excel文件不存在: {excel_file_path}")
    
    # 设置输出目录
    if output_dir is None:
        output_dir = os.path.dirname(excel_file_path)
        if not output_dir:  # 如果是当前目录的文件，dirname返回空字符串
            output_dir = '.'

    if not os.path.exists(output_dir):
        os.makedirs(output_dir)
    
    # 读取Excel文件
    try:
        excel_file = pd.ExcelFile(excel_file_path)
        sheet_names = excel_file.sheet_names
        print(f"发现 {len(sheet_names)} 个工作表: {sheet_names}")
    except Exception as e:
        raise Exception(f"读取Excel文件失败: {str(e)}")
    
    generated_files = []
    base_filename = os.path.splitext(os.path.basename(excel_file_path))[0]
    timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
    
    # 处理每个工作表
    for i, sheet_name in enumerate(sheet_names, 1):
        try:
            # 读取工作表数据
            df = pd.read_excel(excel_file_path, sheet_name=sheet_name)
            
            # 生成Markdown文件名
            safe_sheet_name = sanitize_filename(sheet_name)
            markdown_filename = f"{timestamp}_{i:02d}_Technical_{base_filename}_{safe_sheet_name}.md"
            markdown_path = os.path.join(output_dir, markdown_filename)
            
            # 生成Markdown内容
            markdown_content = generate_markdown_content(
                sheet_name, df, excel_file_path, i, len(sheet_names)
            )
            
            # 写入Markdown文件
            with open(markdown_path, 'w', encoding='utf-8') as f:
                f.write(markdown_content)
            
            generated_files.append(markdown_path)
            print(f"✓ 已生成: {markdown_filename}")
            
        except Exception as e:
            print(f"✗ 处理工作表 '{sheet_name}' 时出错: {str(e)}")
            continue
    
    return generated_files


def generate_markdown_content(sheet_name, df, excel_file_path, sheet_index, total_sheets):
    """
    生成Markdown内容
    
    Args:
        sheet_name (str): 工作表名称
        df (DataFrame): 数据框
        excel_file_path (str): Excel文件路径
        sheet_index (int): 工作表索引
        total_sheets (int): 总工作表数量
    
    Returns:
        str: Markdown内容
    """
    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    
    markdown_content = f"""# {sheet_name}

**文档创建时间**: {timestamp}  
**源文件**: {os.path.basename(excel_file_path)}  
**工作表**: {sheet_index}/{total_sheets}

## 目录
- [数据概览](#数据概览)
- [数据表格](#数据表格)
- [数据统计](#数据统计)

## 数据概览

- **行数**: {len(df)}
- **列数**: {len(df.columns)}
- **工作表名称**: {sheet_name}

### 列信息
"""
    
    # 添加列信息
    for i, col in enumerate(df.columns, 1):
        non_null_count = df[col].count()
        data_type = str(df[col].dtype)
        markdown_content += f"{i}. **{col}** - 类型: `{data_type}`, 非空值: {non_null_count}\n"
    
    markdown_content += "\n## 数据表格\n\n"
    
    # 处理空的DataFrame
    if df.empty:
        markdown_content += "*该工作表没有数据*\n\n"
    else:
        # 转换DataFrame为Markdown表格
        # 处理NaN值
        df_display = df.fillna('')

        # 限制显示的行数（避免文件过大）
        max_rows = 1000
        if len(df_display) > max_rows:
            df_display = df_display.head(max_rows)
            markdown_content += f"*注意: 仅显示前{max_rows}行数据*\n\n"

        # 清理和格式化数据
        df_clean = df_display.copy()

        # 处理每个单元格的内容
        for col in df_clean.columns:
            df_clean[col] = df_clean[col].apply(lambda x: clean_cell_content(str(x)) if pd.notna(x) and str(x).strip() != '' else "")

        # 转换为Markdown表格
        try:
            markdown_table = create_clean_markdown_table(df_clean)
            markdown_content += markdown_table + "\n\n"
        except Exception as e:
            # 如果转换失败，使用简单格式
            markdown_content += "```\n"
            markdown_content += str(df_display)
            markdown_content += "\n```\n\n"
    
    # 添加数据统计
    markdown_content += "## 数据统计\n\n"
    
    if not df.empty:
        # 数值列统计
        numeric_cols = df.select_dtypes(include=['number']).columns
        if len(numeric_cols) > 0:
            markdown_content += "### 数值列统计\n\n"
            try:
                stats_df = df[numeric_cols].describe()
                stats_markdown = stats_df.to_markdown(tablefmt='github')
                markdown_content += stats_markdown + "\n\n"
            except Exception:
                markdown_content += "*无法生成数值统计*\n\n"
        
        # 文本列信息
        text_cols = df.select_dtypes(include=['object']).columns
        if len(text_cols) > 0:
            markdown_content += "### 文本列信息\n\n"
            for col in text_cols:
                unique_count = df[col].nunique()
                most_common = df[col].mode().iloc[0] if not df[col].mode().empty else "N/A"
                markdown_content += f"- **{col}**: {unique_count} 个唯一值, 最常见值: `{most_common}`\n"
            markdown_content += "\n"
    else:
        markdown_content += "*该工作表没有数据可供统计*\n\n"
    
    # 添加页脚
    markdown_content += f"""---
*由 Excel转Markdown工具 生成于 {timestamp}*
"""
    
    return markdown_content


def main():
    """主函数"""
    parser = argparse.ArgumentParser(description='将Excel文件转换为Markdown格式')
    parser.add_argument('excel_file', help='Excel文件路径')
    parser.add_argument('-o', '--output', help='输出目录', default=None)
    
    args = parser.parse_args()
    
    try:
        print(f"开始处理Excel文件: {args.excel_file}")
        generated_files = excel_to_markdown(args.excel_file, args.output)
        
        print(f"\n✓ 转换完成! 共生成 {len(generated_files)} 个Markdown文件:")
        for file_path in generated_files:
            print(f"  - {os.path.basename(file_path)}")
            
    except Exception as e:
        print(f"✗ 转换失败: {str(e)}")
        sys.exit(1)


if __name__ == "__main__":
    main()
